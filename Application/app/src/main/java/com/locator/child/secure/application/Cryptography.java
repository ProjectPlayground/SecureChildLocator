package com.locator.child.secure.application;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;

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
    private static final String CERTIFICATE_DIR = "serverCertificate.cer";
    private static final String SALT = "[B@3764951d";
    private static final int PASSWORD_LENGTH = 16;
    private static final int KEY_LENGTH = 256;
    private static final int ITERATIONS = 1000;

    private PublicKey certificatePublicKey;

    public Cryptography(Context context)
    {
        doKey(context);
    }

    private void doKey(Context context)
    {
        try {

            String[] PERMISSIONS_STORAGE = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions((Activity)context, PERMISSIONS_STORAGE, 1);
            }

            AssetManager am = ((Activity)context).getAssets();
            AssetFileDescriptor fileDescriptor  = am.openFd(CERTIFICATE_DIR);
            FileInputStream fileInputStream = fileDescriptor.createInputStream();
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
