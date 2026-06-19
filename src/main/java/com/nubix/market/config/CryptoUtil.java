package com.nubix.market.config;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Clase de utilidad para operaciones criptográficas.
 * Proporciona métodos estáticos para generar claves, encriptar y desencriptar
 * información utilizando el algoritmo AES (Advanced Encryption Standard).
 */
public class CryptoUtil {

    /**
     * Algoritmo de encriptación utilizado por defecto en esta clase.
     */
    private static final String ALGORITHM = "AES";

    /**
     * Genera una nueva clave secreta (SecretKey) de 128 bits para el algoritmo AES.
     *
     * @return La clave secreta generada, lista para usarse en operaciones de encriptación y desencriptación.
     * @throws Exception Si ocurre un error al obtener la instancia del generador de claves.
     */
    public static SecretKey generarClave() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
        generator.init(128);
        return generator.generateKey();
    }

    /**
     * Encripta un arreglo de bytes utilizando el algoritmo AES y una clave secreta.
     *
     * @param datos Los datos originales (en texto plano) convertidos a arreglo de bytes que se desean ocultar.
     * @param clave La clave secreta generada previamente para realizar la encriptación.
     * @return Un arreglo de bytes que representa los datos ya encriptados.
     * @throws Exception Si ocurre un error durante la configuración del cifrado o la encriptación.
     */
    public static byte[] encriptar(byte[] datos, SecretKey clave) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, clave);
        return cipher.doFinal(datos);
    }

    /**
     * Desencripta un arreglo de bytes utilizando el algoritmo AES y la clave secreta original.
     *
     * @param datos Los datos encriptados en formato de arreglo de bytes.
     * @param clave La misma clave secreta que se utilizó originalmente para encriptar la información.
     * @return Un arreglo de bytes que representa los datos originales (texto plano).
     * @throws Exception Si ocurre un error durante la configuración del cifrado o la desencriptación.
     */
    public static byte[] desencriptar(byte[] datos, SecretKey clave) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, clave);
        return cipher.doFinal(datos);
    }
}
