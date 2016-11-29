package sirs.server;

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
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.util.Base64;

public class Cryptography
{
    private static final String KEYSTORE_DIR = "keys/serverkeystore.jks";
    private static final String PASSWORD = "sirs@childlocator";
    private static final String ALIAS = "selfsigned";
    private static final String salt = "[B@3764951d";
    private static final int KEY_LENGTH = 256;
    private static final int ITERATIONS = 1000;

    // Server keys
    private PublicKey publicKey;
    private PrivateKey privateKey;

    private SecureRandom secureRandom;

    public Cryptography()
    {
        this.secureRandom = new SecureRandom();
        doKeys();
    }

    private void doKeys()
    {
        try {
            File file = new File("keys/serverkeystore.jks");
            URI keystoreUri = file.toURI();
            URL keystoreUrl = keystoreUri.toURL();
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream stream = null;

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
            SecretKey key = keyGenerator.generateKey();
            return key;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void test(String text, KeyStore keyStore, String password)
    {
        try {
            System.out.println("Plain text: " + text);

            PrivateKey privateKey = (PrivateKey) keyStore.getKey("selfsigned", password.toCharArray());
            Certificate cert = keyStore.getCertificate("selfsigned");
            PublicKey publicKey = cert.getPublicKey();

            String encrypted = encryptRSA(text);
            System.out.println("Encrypted: " + encrypted);

            System.out.println("Plain text again: " + decryptRSA(encrypted));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String encryptAES(String text, SecretKey key)
    {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec("0000000000000000".getBytes()));

            byte[] utf8 = text.getBytes("UTF-8");
            byte[] enc = cipher.doFinal(utf8);

            return Base64.getEncoder().encodeToString(enc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String decryptAES(String text, SecretKey key)
    {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec("0000000000000000".getBytes()));

            Base64.Decoder decoder = Base64.getDecoder();
            byte[] dec = decoder.decode(text);
            byte[] utf8 = cipher.doFinal(dec);

            return new String(utf8, "UTF-8");
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public SecretKey getKey(String key)
    {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] keyBytes = decoder.decode(key);

        return new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
    }

    public String decryptAES(String text, String key)
    {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] keyBytes = decoder.decode(key);
        SecretKey secretKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");

        return decryptAES(text, secretKey);
    }

    public String encryptRSA(String text)
    {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] utf8 = text.getBytes("UTF-8");
            byte[] enc = cipher.doFinal(utf8);

            return Base64.getEncoder().encodeToString(enc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decryptRSA(String text)
    {
        try {
            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            Base64.Decoder decoder = Base64.getDecoder();
            byte[] dec = decoder.decode(text);
            byte[] utf8 = cipher.doFinal(dec);

            return new String(utf8, "UTF-8");
        }
        catch (Exception ex) {
            ex.printStackTrace();
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
