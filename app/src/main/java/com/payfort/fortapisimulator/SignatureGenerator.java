package com.payfort.fortapisimulator;

import com.payfort.fortpaymentsdk.domain.model.FortRequest;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by gbarham on 2/15/2016.
 */
public class SignatureGenerator {
    public static String generateSignatureByParamsMap(String passPhrase, String algorithm, TreeMap<String, String> paramsMap) throws Exception {

        String concatenatedData = "";
        for (String key : paramsMap.keySet()) {
            Object value = paramsMap.get(key);
            if (value != null && !value.equals("")) {
                concatenatedData += key + "=" + String.valueOf(value);
            }
        }
        // Add the merchant pass-phrase at the start and the end
        concatenatedData = passPhrase + concatenatedData + passPhrase;

        String sha = generateShaSign(algorithm,
                concatenatedData);
        return sha;
    }

    public static String generateShaSign(final String algorithm, String rawData) {
        String shaSign = null;

        if ("SHA-128".equals(algorithm)) {
            shaSign = generateSha1(rawData);
        } else if ("SHA-256".equals(algorithm)) {
            shaSign = generateSha256(rawData);
        } else if ("SHA-512".equals(algorithm)) {
            shaSign = generateSha512(rawData);
        } else {
            throw new IllegalArgumentException(new StringBuilder("The passed algorithm parameter is \"").append(algorithm).append("\"")
                    .append(" the expected value must be one of\"SHA-1, SHA-256 or SHA-512\"").toString());
        }

        return shaSign;
    }

    private static String generateSha1(String rawData) {
        try {
            byte []shaSignBytes = MessageDigest.getInstance("SHA-1").digest(rawData.getBytes("UTF-8"));

            return String.format("%040x", new BigInteger(1, shaSignBytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String generateSha256(String rawData) {
        try {
            byte []shaSignBytes = MessageDigest.getInstance("SHA-256").digest(rawData.getBytes("UTF-8"));

            return String.format("%064x", new BigInteger(1, shaSignBytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String generateSha512(String rawData) {
        try {
            byte []shaSignBytes = MessageDigest.getInstance("SHA-512").digest(rawData.getBytes("UTF-8"));

            return String.format("%0128x", new BigInteger(1, shaSignBytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }





}
