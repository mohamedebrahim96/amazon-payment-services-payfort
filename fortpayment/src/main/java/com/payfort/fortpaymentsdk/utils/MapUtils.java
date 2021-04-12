package com.payfort.fortpaymentsdk.utils;

import com.google.gson.Gson;
import com.payfort.fortpaymentsdk.constants.Constants;
import com.payfort.fortpaymentsdk.domain.model.SdkResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
public class MapUtils {
    /**
     * @param requestMap
     * @return
     */
    public static String getLanguage(Map<String, Object> requestMap) {
        if (requestMap != null && requestMap.get(Constants.FORT_PARAMS.LANGUAGE) != null){
            String language = String.valueOf(requestMap.get(Constants.FORT_PARAMS.LANGUAGE));
            switch (language) {
                case Constants.LANGUAGES.ARABIC:
                case Constants.LANGUAGES.ENGLISH:
                    return language;
            }
        }

        return Constants.LANGUAGES.ENGLISH;
    }

    /**
     * @param requestMap
     * @return
     */
    public static String getPaymentOptionValue(Map<String, Object> requestMap) {
        if (requestMap != null && requestMap.get(Constants.FORT_PARAMS.PAYMENT_OPTION) != null) {
            String paymentOption = String.valueOf(requestMap.get(Constants.FORT_PARAMS.PAYMENT_OPTION));
            switch (paymentOption) {
                case Constants.CREDIT_CARDS_TYPES.VISA:
                case Constants.CREDIT_CARDS_TYPES.MASTERCARD:
                case Constants.CREDIT_CARDS_TYPES.AMEX:
                case Constants.CREDIT_CARDS_TYPES.MADA:
                case Constants.CREDIT_CARDS_TYPES.MEEZA:
                    return paymentOption;
            }
        }
        return null;
    }



    public static boolean displayRememberMe(Map<String, Object> merchantRequestMap) {
        Object rememberMe = merchantRequestMap.get(Constants.FORT_PARAMS.REMEMBER_ME);
        return rememberMe == null || rememberMe.toString().trim().equals("YES");
    }


    /**
     *
     * @param gson
     * @param serverResponse
     * @param merchantRequestMap
     * @return
     */
    public static SdkResponse collectResponse(Gson gson,String serverResponse, Map<String, Object> merchantRequestMap) {
        if (isEmpty(serverResponse)) {
            return getTechnicalProblemResponse(null, merchantRequestMap);
        }

        SdkResponse sdkResponse = gson.fromJson(serverResponse, SdkResponse.class);
        return sdkResponse;
    }





    /**
     * collect technical proble response
     *
     * @param merchantRequestMap
     * @return
     */
    public static SdkResponse getTechnicalProblemResponse(String msg, Map<String, Object> merchantRequestMap) {
        SdkResponse sdkResponse = new SdkResponse();
        sdkResponse.setSuccess(false);
        Map<String, Object> responseMap = new HashMap<>(merchantRequestMap);
        responseMap.put(Constants.FORT_PARAMS.STATUS, Constants.FORT_STATUS.INVALID_REQUEST);
        responseMap.put(Constants.FORT_PARAMS.RESPONSE_CODE, Constants.FORT_STATUS.INVALID_REQUEST + Constants.FORT_CODE.TECHNICAL_PROBLEM);
        if(isEmpty(msg)){
            msg = Constants.FORT_MSGS.TECHNICAL_PROBLEM;
        }
        responseMap.put(Constants.FORT_PARAMS.RESPONSE_MSG, msg);

        sdkResponse.setResponseMap(responseMap);

        return sdkResponse;
    }

    /**
     * collect response when failed to init connection
     *
     * @param merchantRequestMap
     * @return
     */
    public static SdkResponse getFailedToInitConnectionResponse(String msg, Map<String, Object> merchantRequestMap) {
        SdkResponse sdkResponse = new SdkResponse();
        sdkResponse.setSuccess(false);
        Map<String, Object> responseMap = new HashMap<String, Object>(merchantRequestMap);
        responseMap.put(Constants.FORT_PARAMS.STATUS, Constants.FORT_STATUS.INVALID_REQUEST);
        responseMap.put(Constants.FORT_PARAMS.RESPONSE_CODE, Constants.FORT_STATUS.INVALID_REQUEST + Constants.FORT_CODE.INIT_CONNECTION_FAILED);
        responseMap.put(Constants.FORT_PARAMS.RESPONSE_MSG, msg);
        sdkResponse.setResponseMap(responseMap);

        return sdkResponse;
    }

    /**
     * collect response when cancelled by user response
     *
     * @param merchantRequestMap
     * @return
     */
    public static Map<String, Object> getCanceledByUserResponse(String msg, Map<String, Object> merchantRequestMap) {
        Map<String, Object> responseMap = new HashMap<String, Object>(merchantRequestMap);
        responseMap.put(Constants.FORT_PARAMS.STATUS, Constants.FORT_STATUS.INVALID_REQUEST);
        responseMap.put(Constants.FORT_PARAMS.RESPONSE_CODE, Constants.FORT_STATUS.INVALID_REQUEST + Constants.FORT_CODE.CANCELED_BY_USER);
        responseMap.put(Constants.FORT_PARAMS.RESPONSE_MSG, msg);

        return responseMap;
    }

    /**
     * @param urlString
     * @return
     */
    private static Map<String, Object> splitURI(String urlString) {
        try {
            URL url = new URL(urlString);
            Map<String, Object> query_pairs = new LinkedHashMap<String, Object>();
            String query = url.getQuery();
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            }
            return query_pairs;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param url
     * @return
     */
    public static SdkResponse collectResponseFromURL(String url) {
        SdkResponse sdkResponse = new SdkResponse();
        sdkResponse.setResponseMap(splitURI(url));
        if (sdkResponse.getResponseMap() != null)
            if (!sdkResponse.getResponseMap().isEmpty())
                if (isNotEmpty(sdkResponse.getResponseMap().get(Constants.FORT_PARAMS.RESPONSE_CODE)) && sdkResponse.getResponseMap().get(Constants.FORT_PARAMS.RESPONSE_CODE).toString().length() >= 5
                        && sdkResponse.getResponseMap().get(Constants.FORT_PARAMS.RESPONSE_CODE).toString().substring(2).equals("000")) {
                    sdkResponse.setSuccess(true);
                }
        return sdkResponse;
    }



    /**
     *
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(Object s) {
        if (s == null || s.toString().trim().isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     *
     *
     * @param s
     * @return
     */
    public static boolean isNotEmpty(Object s) {
        return !isEmpty(s);
    }
}
