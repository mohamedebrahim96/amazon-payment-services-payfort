package com.payfort.fortpaymentsdk.domain.model;



import java.util.Map;

public class SdkRequest {
    private Map<String, Object> requestMap;
    private String deviceId;
    private String deviceOS;

    public SdkRequest() {
    }

    public String getDeviceOS() {
        return this.deviceOS;
    }

    public void setDeviceOS(String deviceOS) {
        this.deviceOS = deviceOS;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Map<String, Object> getRequestMap() {
        return this.requestMap;
    }

    public void setRequestMap(Map<String, Object> requestMap) {
        this.requestMap = requestMap;
    }


}
