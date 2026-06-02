package com.nubix.market.config;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class CryptoUtil {
    private static final String ALGORITHM = "AES";

    public static SecretKey generarClave() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
        generator.init(128);
        return generator.generateKey();
    }

    public static byte[] encriptar(byte[] datos, SecretKey clave) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, clave);
        return cipher.doFinal(datos);
    }

    public static byte[] desencriptar(byte[] datos, SecretKey clave) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, clave);
        return cipher.doFinal(datos);
    }
}
