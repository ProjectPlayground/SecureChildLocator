package com.locator.child.secure.application;

import android.util.Base64;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileInputStream;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Cryptography
{
    private static final String CERTIFICATE_DIR = "keys/serverCertificate.cer";
    private static final String SALT = "[B@3764951d";
    private static final int PASSWORD_LENGTH = 16;
    private static final int KEY_LENGTH = 256;
    private static final int ITERATIONS = 1000;

    private PublicKey certificatePublicKey;

    public Cryptography()
    {
        doKey();
    }

    private void doKey()
    {
        try {
            FileInputStream fileInputStream = new FileInputStream(CERTIFICATE_DIR);
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);
            certificatePublicKey = certificate.getPublicKey();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] passwordToKey(String password) throws WrongPasswordSizeException
    {
        String newPass=password;

        if (password.length()==0) {
            throw new WrongPasswordSizeException(password.length());
        }
        else if (password.length()<16) {
            while (newPass.length()<16) {
                newPass += password;
            }
        }

        newPass = newPass.substring(0,PASSWORD_LENGTH);
        if (newPass.length()!=PASSWORD_LENGTH) {
            throw new WrongPasswordSizeException(newPass.length());
        }

        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(newPass.toCharArray(), SALT.getBytes(), ITERATIONS, KEY_LENGTH);
            return f.generateSecret(spec).getEncoded();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public SecretKey generateKey()
    {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            SecretKey key = keyGenerator.generateKey();
            return key;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String encryptWithPassword(String message, String pass){
        try {
            byte[] pBytes = passwordToKey(pass);
            SecretKey key = new SecretKeySpec(pBytes, "AES");
            Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            ecipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec("0000000000000000".getBytes()));

            byte[] utf8 = message.getBytes("UTF-8");
            byte[] enc = ecipher.doFinal(utf8);

            return Base64.encodeToString(enc, Base64.DEFAULT);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String decryptWithPassword(String message, String pass){
        try {
            byte[] pBytes = passwordToKey(pass);

            SecretKey key = new SecretKeySpec(pBytes, "AES");
            Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            ecipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec("0000000000000000".getBytes()));

            byte[] dec = Base64.decode(message, Base64.DEFAULT);
            byte[] utf8 = ecipher.doFinal(dec);

            return new String(utf8, "UTF-8");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String encryptAES(String text, SecretKey key)
    {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec("0000000000000000".getBytes()));

            byte[] utf8 = text.getBytes("UTF-8");
            byte[] enc = cipher.doFinal(utf8);

            return Base64.encodeToString(enc, Base64.DEFAULT);
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

            byte[] dec = Base64.decode(text, Base64.DEFAULT);
            byte[] utf8 = cipher.doFinal(dec);

            return new String(utf8, "UTF-8");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public String encryptRSA(String text)
    {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, certificatePublicKey);

            byte[] utf8 = text.getBytes("UTF-8");
            byte[] enc = cipher.doFinal(utf8);

            return Base64.encodeToString(enc, Base64.DEFAULT);
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
