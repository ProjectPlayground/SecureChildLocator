package com.locator.child.secure.application;

import android.util.Base64;

import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Cryptography
{
    private static final int PASSWORD_LENGTH = 16;
    private static final int KEY_LENGTH = 256;
    private static final int ITERATIONS = 1000;
    private static final String salt = "[B@3764951d";

    private SecureRandom secureRandom;

    public Cryptography()
    {
        secureRandom = new SecureRandom();
    }


    private byte[] passwordToBytes(String password) throws WrongPasswordSizeException
    {
        String newPass=password;
        if (password.length()==0)
            throw new WrongPasswordSizeException(password.length());
        else if (password.length()<16){
            while (newPass.length()<16)
                newPass+=password;
        }
        newPass = newPass.substring(0,PASSWORD_LENGTH);
        if(newPass.length()!=PASSWORD_LENGTH)
            throw new WrongPasswordSizeException(newPass.length());

        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] salt = "[B@3764951d".getBytes("UTF-8");
            KeySpec spec = new PBEKeySpec(newPass.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            return f.generateSecret(spec).getEncoded();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String encriptSymetricMessage(String message, String pass){
        try {
            byte[] pBytes = passwordToBytes(pass);
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

    public String desincriptSymetricMessage(String message, String pass){
        try {
            byte[] pBytes = passwordToBytes(pass);
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

}
