package com.payfort.fortpaymentsdk.callbacks;

import java.util.Map;

public interface FortInterfaces {
    public interface InitializeCallback {

        void afterInitialized();
    }


    public interface OnTnxProcessed {

        void onCancel(Map<String, Object> requestParamsMap, Map<String, Object> fortResponseMap);

        void onSuccess(Map<String, Object> requestParamsMap, Map<String, Object> fortResponseMap);

        void onFailure(Map<String, Object> requestParamsMap, Map<String, Object> fortResponseMap);
    }
}
