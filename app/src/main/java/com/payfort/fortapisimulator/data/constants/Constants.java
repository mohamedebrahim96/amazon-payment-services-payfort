package com.payfort.fortapisimulator.data.constants;


import com.payfort.fortpaymentsdk.FortSdk;

public class Constants {

    private static final String ENV_SB = "https://sbcheckout.payfort.com/";
    private static final String ENV_PROD = "https://checkout.payfort.com/";



    private class PRODUCTION_VALUES {
        public static final String COMMAND_REQ_PARAMS_URL = ENV_SB + "apiSimulator/getRequestParams";
        public static final String SDK_TOKEN_CREATION_URL = ENV_PROD + "FortAPI/paymentApi";
        public static final String SDK_ENVIRONMENT = FortSdk.ENVIRONMENT.PRODUCTION;

    }


    private class SANDBOX_VALUES {
        public static final String COMMAND_REQ_PARAMS_URL = ENV_SB + "apiSimulator/getRequestParams";
        public static final String SDK_TOKEN_CREATION_URL = ENV_SB + "FortAPI/paymentApi";
        public static final String SDK_ENVIRONMENT = FortSdk.ENVIRONMENT.TEST;

    }


    public enum ENVIRONMENTS_VALUES {


        SANDBOX(SANDBOX_VALUES.COMMAND_REQ_PARAMS_URL, SANDBOX_VALUES.SDK_TOKEN_CREATION_URL, SANDBOX_VALUES.SDK_ENVIRONMENT),

        PRODUCTION(PRODUCTION_VALUES.COMMAND_REQ_PARAMS_URL, PRODUCTION_VALUES.SDK_TOKEN_CREATION_URL, PRODUCTION_VALUES.SDK_ENVIRONMENT);


        private String paramsUrl;
        private String tokenCreationUrl;
        private String sdkEnvironemt;

        ENVIRONMENTS_VALUES(String paramsUrl, String tokenCreationUrl, String sdkEnvironemt) {
            this.paramsUrl = paramsUrl;
            this.tokenCreationUrl = tokenCreationUrl;

            this.sdkEnvironemt = sdkEnvironemt;
        }

        public String getSdkEnvironemt() {
            return sdkEnvironemt;
        }

        public String getParamsUrl() {
            return paramsUrl;
        }

        public String getTokenCreationUrl() {
            return tokenCreationUrl;
        }


        @Override
        public String toString() {
            return this.name();
        }
    }
}