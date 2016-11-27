package com.locator.child.secure.application;

import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Cryptography
{
    private static final int PASSWORD_LENGTH = 16;
    private static final int KEY_LENGTH = 256;
    private static final int ITERATIONS = 1000;
    private static final byte[] salt = "[B@3764951d".getBytes();

    private SecureRandom secureRandom;

    public Cryptography()
    {
        secureRandom = new SecureRandom();
    }

    public byte[] passwordToKey(String password)
            throws WrongPasswordSizeException
    {
        if (password.length() != PASSWORD_LENGTH) {
            throw new WrongPasswordSizeException(password.length());
        }

        return passwordToHash(password);
    }

    public byte[] passwordToHash(String password)
    {
        try {
            secureRandom.nextBytes(salt);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return f.generateSecret(spec).getEncoded();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
