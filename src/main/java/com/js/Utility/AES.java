package com.js.Utility;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AES {

    public String encrypt(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(passwordToHash.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            generatedPassword = no.toString(16);
            while (generatedPassword.length() < 32) {
                generatedPassword = 0 + generatedPassword;
            }
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
