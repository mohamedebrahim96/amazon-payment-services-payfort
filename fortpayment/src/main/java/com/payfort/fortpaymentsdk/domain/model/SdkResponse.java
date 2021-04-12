package com.payfort.fortpaymentsdk.domain.model;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SdkResponse implements Serializable {

    private Map<String, Object> responseMap;
    private boolean isSuccess;
    private String checker3DsURL;
    private int currencyDecimalPoints;
    private MerchantToken merchantToken;

    public Map<String, Object> getResponseMap() {
        if (this.responseMap == null)
            return new HashMap<>();
        return responseMap;
    }

    public void setResponseMap(Map<String, Object> responseMap) {
        this.responseMap = responseMap;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getChecker3DsURL() {
        return checker3DsURL;
    }

    public void setChecker3DsURL(String checker3DsURL) {
        this.checker3DsURL = checker3DsURL;
    }

    public int getCurrencyDecimalPoints() {
        return currencyDecimalPoints;
    }

    public void setCurrencyDecimalPoints(int currencyDecimalPoints) {
        this.currencyDecimalPoints = currencyDecimalPoints;
    }

    public MerchantToken getMerchantToken() {
        return merchantToken;
    }

    public void setMerchantToken(MerchantToken merchantToken) {
        this.merchantToken = merchantToken;
    }
}
