package com.nubix.market.config;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Utilidades criptográficas AES para operaciones de cifrado y descifrado de datos
 * sensibles en el backend de Nubix Market.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class CryptoUtil {

    /** Algoritmo simétrico utilizado para generación de claves y cifrado. */
    private static final String ALGORITHM = "AES";

    /**
     * Genera una nueva clave secreta AES de 128 bits.
     *
     * @return clave secreta generada
     * @throws Exception si el proveedor JCE no está disponible o falla la generación
     */
    public static SecretKey generarClave() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
        generator.init(128);
        return generator.generateKey();
    }

    /**
     * Cifra un arreglo de bytes con la clave AES indicada.
     *
     * @param datos bytes en texto plano a cifrar
     * @param clave clave secreta AES
     * @return bytes cifrados
     * @throws Exception si falla la inicialización del cipher o el cifrado
     */
    public static byte[] encriptar(byte[] datos, SecretKey clave) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, clave);
        return cipher.doFinal(datos);
    }

    /**
     * Descifra un arreglo de bytes previamente cifrado con AES.
     *
     * @param datos bytes cifrados
     * @param clave clave secreta AES usada en el cifrado
     * @return bytes en texto plano
     * @throws Exception si falla la inicialización del cipher o el descifrado
     */
    public static byte[] desencriptar(byte[] datos, SecretKey clave) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, clave);
        return cipher.doFinal(datos);
    }
}
