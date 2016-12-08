package sirs.server;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

public class Cryptography
{
    private static final String KEYSTORE_DIR = "keys/serverkeystore.jks";
    private static final String PASSWORD = "sirs@childlocator";
    private static final String ALIAS = "selfsigned";

    // Server keys
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public Cryptography()
    {
        doKeys();
    }

    private void doKeys()
    {
        try {
            File file = new File(KEYSTORE_DIR);
            URI keystoreUri = file.toURI();
            URL keystoreUrl = keystoreUri.toURL();
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream stream;

            stream = keystoreUrl.openStream();
            keystore.load(stream, PASSWORD.toCharArray());
            privateKey = (PrivateKey) keystore.getKey(ALIAS, PASSWORD.toCharArray());
            Certificate cert = keystore.getCertificate(ALIAS);
            publicKey = cert.getPublicKey();

            stream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public SecretKey generateKey()
    {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            return keyGenerator.generateKey();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String encryptAES(String text, SecretKey key)
    {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec("0000000000000000".getBytes()));

            byte[] utf8 = text.getBytes("UTF-8");
            byte[] enc = cipher.doFinal(utf8);

            return new String(Base64.encodeBase64(enc), "UTF-8");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String decryptAES(String text, String key)
    {
        byte[] keyBytes = Base64.decodeBase64(key);
        SecretKey secretKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");

        return decryptAES(text, secretKey);
    }

    public String decryptAES(String text, SecretKey key)
    {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec("0000000000000000".getBytes()));

            byte[] dec = Base64.decodeBase64(text);
            byte[] utf8 = cipher.doFinal(dec);

            return new String(utf8, "UTF-8");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public SecretKey getKey(String key)
    {
        try {
            byte[] keyBytes = Base64.decodeBase64(key);
            String stringKey = new String(keyBytes, "UTF-8");

            return new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String encryptRSA(String text)
    {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] utf8 = text.getBytes("UTF-8");
            byte[] enc = cipher.doFinal(utf8);

            return new String(Base64.encodeBase64(enc), "UTF-8");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decryptRSA(String text)
    {
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] dec = Base64.decodeBase64(text);
            byte[] utf8 = cipher.doFinal(dec);

            return new String(utf8, "UTF-8");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean hashIsValid(String text, String hash)
    {
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(text).equals(hash);
    }

    public String hash(String text)
    {
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(text);
    }
}
