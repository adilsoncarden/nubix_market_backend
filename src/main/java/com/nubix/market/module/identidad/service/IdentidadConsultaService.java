package com.nubix.market.module.identidad.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubix.market.module.identidad.dto.IdentidadConsultaResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class IdentidadConsultaService {

    private static final Logger log = LoggerFactory.getLogger(IdentidadConsultaService.class);

    private final String apiUrl;
    private final String apiToken;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public IdentidadConsultaService(
            @Value("${dni.ruc.api.url:}") String apiUrl,
            @Value("${dni.ruc.api.token:}") String apiToken,
            RestTemplate restTemplate,
            ObjectMapper objectMapper) {
        this.apiUrl = apiUrl;
        this.apiToken = apiToken;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public IdentidadConsultaResponse consultar(String documento) {
        String numero = StringUtils.trimToEmpty(documento);
        if (!numero.matches("\\d{8}") && !numero.matches("\\d{11}")) {
            throw new RuntimeException("El documento debe tener 8 dígitos (DNI) u 11 (RUC).");
        }
        if (StringUtils.isBlank(apiUrl) || StringUtils.isBlank(apiToken)) {
            log.error("Apisperu no configurado: apiUrl vacío={}, apiToken vacío={}",
                    StringUtils.isBlank(apiUrl), StringUtils.isBlank(apiToken));
            throw new RuntimeException("El servicio de consulta de documentos no está configurado.");
        }

        boolean esRuc = numero.length() == 11;
        String tipo = esRuc ? "RUC" : "DNI";
        String recurso = esRuc ? "ruc" : "dni";
        String url = buildApisperuUrl(recurso, numero);

        log.info("Consultando {} en Apisperu. URL={}", tipo, url);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class);

            log.debug("Apisperu respuesta OK {}: {}", tipo, response.getBody());
            return mapResponse(numero, tipo, response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            log.error("Apisperu 404 {} URL={} body={}", tipo, url, e.getResponseBodyAsString());
            throw new RuntimeException("No se encontró el " + tipo + " ingresado.");
        } catch (HttpStatusCodeException e) {
            log.error("Apisperu error HTTP {} {} URL={} body={}",
                    e.getStatusCode().value(), tipo, url, e.getResponseBodyAsString());
            throw new RuntimeException(resolveApiErrorMessage(e.getResponseBodyAsString(), tipo));
        } catch (RestClientException e) {
            log.error("Apisperu conexión fallida {} URL={} error={}", tipo, url, e.getMessage(), e);
            throw new RuntimeException("El servicio de consulta no está disponible. Intenta más tarde.");
        } catch (RuntimeException e) {
            log.error("Error de negocio consultando {} URL={} mensaje={}", tipo, url, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado consultando {} URL={}", tipo, url, e);
            throw new RuntimeException("No se pudo procesar la consulta de identidad.");
        }
    }

    /**
     * Formato estricto apisperu.com:
     * {apiUrl}/dni/{numero}?token={token}
     * {apiUrl}/ruc/{numero}?token={token}
     */
    private String buildApisperuUrl(String recurso, String numero) {
        String base = apiUrl.trim();
        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        return base + "/" + recurso + "/" + numero + "?token=" + apiToken.trim();
    }

    private IdentidadConsultaResponse mapResponse(String documento, String tipo, String body) {
        if (StringUtils.isBlank(body)) {
            throw new RuntimeException("No se encontró el " + tipo + " ingresado.");
        }

        try {
            JsonNode root = objectMapper.readTree(body);
            if (root == null || root.isNull()) {
                throw new RuntimeException("No se encontró el " + tipo + " ingresado.");
            }

            String apiError = extractErrorMessage(root);
            if (StringUtils.isNotBlank(apiError)) {
                throw new RuntimeException(apiError);
            }

            if (root.has("success") && root.get("success").isBoolean() && !root.get("success").asBoolean()) {
                String message = firstText(root, "message", "mensaje");
                throw new RuntimeException(
                        StringUtils.defaultIfBlank(message, "No se encontró el " + tipo + " ingresado."));
            }

            // Apisperu devuelve el JSON plano en la raíz (sin wrapper "data")
            JsonNode data = root.has("data") && root.get("data").isObject()
                    ? root.get("data")
                    : root;

            IdentidadConsultaResponse response = new IdentidadConsultaResponse();
            response.setDocumento(documento);
            response.setTipo(tipo);

            if ("DNI".equals(tipo)) {
                response.setNombreRazonSocial(resolveNombreDni(data));
            } else {
                response.setNombreRazonSocial(firstText(data,
                        "razonSocial", "razon_social", "nombre", "nombre_o_razon_social"));
                response.setDepartamento(firstText(data, "departamento"));
                response.setProvincia(firstText(data, "provincia"));
                response.setDistrito(firstText(data, "distrito"));
                response.setDireccion(firstText(data,
                        "direccion", "direccion_completa", "direccionCompleta", "domicilio_fiscal"));
            }

            if (StringUtils.isBlank(response.getNombreRazonSocial())) {
                log.error("Apisperu respondió sin nombre para {}. body={}", tipo, body);
                throw new RuntimeException("No se encontró el " + tipo + " ingresado.");
            }

            return response;
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error parseando respuesta Apisperu para {}. body={}", tipo, body, ex);
            throw new RuntimeException("No se pudo interpretar la respuesta del servicio de identidad.");
        }
    }

    /**
     * DNI Apisperu: nombres, apellidoPaterno, apellidoMaterno
     */
    private String resolveNombreDni(JsonNode data) {
        if (data == null || data.isNull()) {
            return "";
        }
        String completo = firstText(data,
                "nombre_completo", "nombreCompleto", "nombre_completo_o_razon_social");
        if (StringUtils.isNotBlank(completo)) {
            return completo.trim();
        }
        String nombres = firstText(data, "nombres", "nombre");
        String paterno = firstText(data, "apellidoPaterno", "apellido_paterno");
        String materno = firstText(data, "apellidoMaterno", "apellido_materno");
        return String.join(" ",
                StringUtils.defaultString(nombres),
                StringUtils.defaultString(paterno),
                StringUtils.defaultString(materno)).trim().replaceAll("\\s+", " ");
    }

    private String extractErrorMessage(JsonNode root) {
        if (root == null) {
            return null;
        }
        String rootMessage = firstText(root, "message", "mensaje");
        if (StringUtils.isNotBlank(rootMessage)) {
            return rootMessage;
        }
        if (!root.has("error") || root.get("error").isNull()) {
            return null;
        }
        JsonNode errorNode = root.get("error");
        if (errorNode.isTextual()) {
            return errorNode.asText("").trim();
        }
        if (errorNode.isObject()) {
            return firstText(errorNode, "message", "mensaje", "description");
        }
        return null;
    }

    private String resolveApiErrorMessage(String body, String tipo) {
        if (StringUtils.isBlank(body)) {
            return "No se pudo validar el " + tipo + ". Verifica el número e intenta de nuevo.";
        }
        try {
            JsonNode root = objectMapper.readTree(body);
            String error = extractErrorMessage(root);
            if (StringUtils.isNotBlank(error)) {
                return error;
            }
        } catch (Exception ex) {
            log.warn("No se pudo parsear cuerpo de error Apisperu: {}", body);
        }
        return "No se pudo validar el " + tipo + ". Verifica el número e intenta de nuevo.";
    }

    private String firstText(JsonNode node, String... fields) {
        if (node == null || node.isNull()) {
            return null;
        }
        for (String field : fields) {
            if (node.has(field) && !node.get(field).isNull()) {
                JsonNode valueNode = node.get(field);
                if (valueNode.isTextual() || valueNode.isNumber()) {
                    String value = valueNode.asText("").trim();
                    if (StringUtils.isNotBlank(value)) {
                        return value;
                    }
                }
            }
        }
        return null;
    }
}
