package com.js.Utility;

import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class OTPservice {
    public byte[] hmac_sha(String crypto, byte[] byteFinalValueKey, byte[] byteTime) {
        try {
            Mac hmac;
            hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey = new SecretKeySpec(byteFinalValueKey, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(byteTime);
        }
        catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }

    public byte[] hexStr2Bytes(String hex) {
        byte[] bArray = new BigInteger("10" + hex,16).toByteArray();
        byte[] ret = new byte[bArray.length - 1];
        for (int i = 0; i < ret.length; i++)
            ret[i] = bArray[i+1];
        return ret;
    }

    public String generateOTP(int interval, String key, String toEmail) {
        long presentTime = (System.currentTimeMillis()/1000);

        int finalKey = 0;
        int variable;
        for(int i=0; i<key.length(); i++) {
            char character = key.charAt(i);
            char mail = toEmail.charAt(i);
            variable = Integer.valueOf(character) + Integer.valueOf(mail);
            finalKey = finalKey + variable;
        }

        long timeLimit = (presentTime+(interval-(presentTime%interval)));
        String time = Long.toString(timeLimit);
        String finalValueKey = Integer.toString((int) (finalKey+timeLimit));

        if(presentTime < timeLimit) {
            byte[] byteTime = hexStr2Bytes(time);
            byte[] byteFinalValueKey = hexStr2Bytes(finalValueKey);
            byte[] hash = hmac_sha("HmacSHA1", byteFinalValueKey, byteTime);
            int offset = hash[hash.length - 1] & 0xf;
            int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);
            int otp = binary % 1000000;
            if (otp < 100000) {
                System.out.println("OYP BAN = "+otp);
                otp = 100000 + otp;
            }
            return String.valueOf(otp);
        }
        else {
            return null;
        }
    }
}