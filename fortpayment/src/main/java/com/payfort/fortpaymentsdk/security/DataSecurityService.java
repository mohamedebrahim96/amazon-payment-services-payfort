package com.payfort.fortpaymentsdk.security;


import com.google.common.io.BaseEncoding;
import com.payfort.fortpaymentsdk.exceptions.FortException;
import com.payfort.fortpaymentsdk.security.aes.AESCipherManager;
import com.payfort.fortpaymentsdk.security.rsa.RSAEncryptUtil;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.crypto.spec.SecretKeySpec;


public class DataSecurityService {

    private static final String FORT_SECURITY_SEPARATOR = "___F0RT___";

    /**
     * @param certificates
     * @return
     */
    public static RSAPublicKey getPublicKey(List<Certificate> certificates) {
        X509Certificate certificate = null;
        if(certificates!=null)
        for (Certificate c : certificates) {
            certificate = (X509Certificate) c;
            if (certificate.getSubjectDN().getName().contains("payfort") || certificate.getSubjectDN().getName().contains("PAYFORT")) {
                break;
            }
        }
        if (certificate != null) {
            return (RSAPublicKey) certificate.getPublicKey();
        }
        return null;

    }


    /**
     * @param jsonReqString
     * @param publicKey
     * @param secretKeySpec
     * @return
     * @throws FortException
     */
    public static String encryptRequestData(String jsonReqString, RSAPublicKey publicKey, SecretKeySpec secretKeySpec) throws FortException {
        try {
            // 1. encrypt requestData with secretKeySpec
            AESCipherManager aesCipherManager = new AESCipherManager();
            String encryptedAESData = aesCipherManager.encryptData(jsonReqString, secretKeySpec);
            // 2. encrypt AES key with RSA public key
            byte[] encryptedAESKey = RSAEncryptUtil.encrypt(secretKeySpec.getEncoded(), publicKey);
            // as String
            return (encryptedAESData + FORT_SECURITY_SEPARATOR + BaseEncoding.base64().encode((encryptedAESKey)));
        } catch (Exception e) {
            throw new FortException("failed to encrypt data", e);
        }
    }

    public static void cleanMerhcantRequestMap(Map<String, Object> merchantRequestMap) {
        for(String key : merchantRequestMap.keySet()) {
            Object value = merchantRequestMap.get(key);
            merchantRequestMap.put(key, cleanXSS(value));
        }
    }

    /**
     * Clean request from XSS
     *
     * @param value
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Object cleanXSS(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Map) {
            for (String key : ((Map<String, Object>) value).keySet()) {
                ((Map<String, Object>) value).put(cleanXSS(key).toString(), cleanXSS(((Map<String, Object>) value).get(key)));
            }
        } else if (value instanceof List) {
            List cleanedValues = new LinkedList();
            for (Object vlu : ((List<?>) value)) {
                cleanedValues.add(cleanXSS(vlu));
            }
            ((List) value).clear();
            ((List) value).addAll(cleanedValues);
        } else {
            return stringCleanXSS(value);
        }
        return value;
    }

    /**
     * clean string from cross scripting
     *
     * @param val
     * @return
     */
    private static String stringCleanXSS(Object val) {

        String value = val != null ? val.toString() : null;
        if (value == null) {
            return null;
        }

        // Avoid null characters
        value = value.replaceAll("\0", "");

        // No need to check pattern matcher
        if (value.isEmpty())
            return value;

        // Remove all sections that match a pattern
        for (Pattern scriptPattern : FortSecurityConstant.xSSPatterns) {
            value = scriptPattern.matcher(value).replaceAll("");
        }
        return value;
    }

}
