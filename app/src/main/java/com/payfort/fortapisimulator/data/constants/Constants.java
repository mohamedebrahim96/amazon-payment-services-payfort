package com.payfort.fortapisimulator.data.constants;


import com.payfort.fortpaymentsdk.FortSdk;

/**
 * Created by gbarham on 11/28/2016.
 */

public class Constants {

    private static final String ENV_HOTFIX = "https://hotfixcheckout.payfort.com/";
    private static final String ENV_SB = "https://sbcheckout.payfort.com/";
    private static final String ENV_PROD = "https://checkout.payfort.com/";
    private static final String ENV_STG = "https://stgpaymentservices.payfort.com/";
    private static final String ENV_DEV = "https://newdevcheckout.payfort.com/";
    private static final String ENV_PRE_PROD = "https://preprodcheckout.payfort.com/";
    private static final String ENV_DR = "https://18.195.86.104/";


    private class SANDBOX_VALUES {
        public static final String COMMAND_REQ_PARAMS_URL = ENV_SB + "apiSimulator/getRequestParams";
        public static final String SDK_TOKEN_CREATION_URL = ENV_SB + "FortAPI/paymentApi";
        public static final String SDK_ENVIRONMENT = FortSdk.ENVIRONMENT.TEST;

        public static final String REQUEST_PASSPHRASE = "maharequest";
        public static final String ACCESS_CODE = "zx0IPmPy5jp1vAzBPlWT";
        public static final String MERCHANT_IDENTIFIER = "shouldbegenerated";
    }

    private class PRODUCTION_VALUES {
        public static final String COMMAND_REQ_PARAMS_URL = ENV_SB + "apiSimulator/getRequestParams";
        public static final String SDK_TOKEN_CREATION_URL = ENV_PROD + "FortAPI/paymentApi";
        public static final String SDK_ENVIRONMENT = FortSdk.ENVIRONMENT.PRODUCTION;

        public static final String REQUEST_PASSPHRASE = "Duaa";
        public static final String ACCESS_CODE = "FGzgOYA2g3cS6M3LOxK8";
        public static final String MERCHANT_IDENTIFIER = "shouldbegenerated";
    }

    private class STG_VALUES {
        public static final String COMMAND_REQ_PARAMS_URL = ENV_SB + "apiSimulator/getRequestParams";
        public static final String SDK_TOKEN_CREATION_URL = ENV_STG + "integration-rest-0.1/paymentApi";
        public static final String SDK_ENVIRONMENT = ENV_STG+"integration-rest-0.1";

        public static final String REQUEST_PASSPHRASE = "Duaa";
        public static final String ACCESS_CODE = "JpxITGdDhhE0Mrq4VkgR";
        public static final String MERCHANT_IDENTIFIER = "bxgOIxIz";
    }

    private class SANDBOX2_VALUES {
        public static final String COMMAND_REQ_PARAMS_URL = ENV_SB + "apiSimulator/getRequestParams";
        public static final String SDK_TOKEN_CREATION_URL = ENV_SB + "FortAPI/paymentApi";
        public static final String SDK_ENVIRONMENT = FortSdk.ENVIRONMENT.TEST;

        public static final String REQUEST_PASSPHRASE = "Duaa";
        public static final String ACCESS_CODE = "13XJeixqg3SoqODuXRzz";
        public static final String MERCHANT_IDENTIFIER = "VVyHSRPl";
    }

    private class DEV_VALUES {
        public static final String COMMAND_REQ_PARAMS_URL = ENV_SB + "apiSimulator/getRequestParams";
        public static final String SDK_TOKEN_CREATION_URL = ENV_DEV + "integration-rest-0.1/paymentApi";
        public static final String SDK_ENVIRONMENT = ENV_DEV+"integration-rest-0.1";

        public static final String REQUEST_PASSPHRASE = "request";
        public static final String ACCESS_CODE = "AwvucffCjzibl0eZYTB3";
        public static final String MERCHANT_IDENTIFIER = "uZOJfKqb";
    }

    private class CYBERSOURCE_VALUES {
        public static final String COMMAND_REQ_PARAMS_URL = ENV_SB + "apiSimulator/getRequestParams";
        public static final String SDK_TOKEN_CREATION_URL = ENV_STG + "integration-rest-0.1/paymentApi";
        public static final String SDK_ENVIRONMENT = ENV_STG+"integration-rest-0.1";

        public static final String REQUEST_PASSPHRASE = "duaa";
        public static final String ACCESS_CODE = "oVSYR47vCEHWgcbJNZBC";
        public static final String MERCHANT_IDENTIFIER = "DTBdzNHW";
    }

    private class STG_NPS_VALUES {
        public static final String COMMAND_REQ_PARAMS_URL = ENV_SB + "apiSimulator/getRequestParams";
        public static final String SDK_TOKEN_CREATION_URL = ENV_STG + "integration-rest-0.1/paymentApi";
        public static final String SDK_ENVIRONMENT = ENV_STG+"integration-rest-0.1";

        public static final String REQUEST_PASSPHRASE = "Duaa";
        public static final String ACCESS_CODE = "WUGz4rFGlov8qTzjV9BR";
//        public static final String REQUEST_PASSPHRASE = "Dana123";
//        public static final String ACCESS_CODE = "kJ05aANFOMDaT9OEHHs0";
        public static final String MERCHANT_IDENTIFIER = "bxgOIxIz";
    }

    public enum ENVIRONMENTS_VALUES {

        STG(STG_VALUES.COMMAND_REQ_PARAMS_URL, STG_VALUES.SDK_TOKEN_CREATION_URL, STG_VALUES.REQUEST_PASSPHRASE, STG_VALUES.ACCESS_CODE, STG_VALUES.MERCHANT_IDENTIFIER, STG_VALUES.SDK_ENVIRONMENT),
        SANDBOX2(SANDBOX2_VALUES.COMMAND_REQ_PARAMS_URL, SANDBOX2_VALUES.SDK_TOKEN_CREATION_URL, SANDBOX2_VALUES.REQUEST_PASSPHRASE, SANDBOX2_VALUES.ACCESS_CODE, SANDBOX2_VALUES.MERCHANT_IDENTIFIER, SANDBOX2_VALUES.SDK_ENVIRONMENT),
        SANDBOX(SANDBOX_VALUES.COMMAND_REQ_PARAMS_URL, SANDBOX_VALUES.SDK_TOKEN_CREATION_URL, SANDBOX_VALUES.REQUEST_PASSPHRASE, SANDBOX_VALUES.ACCESS_CODE, SANDBOX_VALUES.MERCHANT_IDENTIFIER, SANDBOX_VALUES.SDK_ENVIRONMENT),
        PRODUCTION(PRODUCTION_VALUES.COMMAND_REQ_PARAMS_URL, PRODUCTION_VALUES.SDK_TOKEN_CREATION_URL, PRODUCTION_VALUES.REQUEST_PASSPHRASE, PRODUCTION_VALUES.ACCESS_CODE, PRODUCTION_VALUES.MERCHANT_IDENTIFIER, PRODUCTION_VALUES.SDK_ENVIRONMENT),
        DEV(DEV_VALUES.COMMAND_REQ_PARAMS_URL, DEV_VALUES.SDK_TOKEN_CREATION_URL, DEV_VALUES.REQUEST_PASSPHRASE, DEV_VALUES.ACCESS_CODE, DEV_VALUES.MERCHANT_IDENTIFIER, DEV_VALUES.SDK_ENVIRONMENT),
        CYBERSOURCE(CYBERSOURCE_VALUES.COMMAND_REQ_PARAMS_URL, CYBERSOURCE_VALUES.SDK_TOKEN_CREATION_URL, CYBERSOURCE_VALUES.REQUEST_PASSPHRASE, CYBERSOURCE_VALUES.ACCESS_CODE, CYBERSOURCE_VALUES.MERCHANT_IDENTIFIER, CYBERSOURCE_VALUES.SDK_ENVIRONMENT),
        STG_NPS(STG_NPS_VALUES.COMMAND_REQ_PARAMS_URL, STG_NPS_VALUES.SDK_TOKEN_CREATION_URL, STG_NPS_VALUES.REQUEST_PASSPHRASE, STG_NPS_VALUES.ACCESS_CODE, STG_NPS_VALUES.MERCHANT_IDENTIFIER, STG_NPS_VALUES.SDK_ENVIRONMENT);

        private String paramsUrl;
        private String tokenCreationUrl;
        private String requestPassPhrase;
        private String accessCode;
        private String merchantIdentifier;
        private String sdkEnvironemt;

        ENVIRONMENTS_VALUES(String paramsUrl, String tokenCreationUrl, String requestPassPhrase, String accessCode, String merchantIdentifier, String sdkEnvironemt) {
            this.paramsUrl = paramsUrl;
            this.tokenCreationUrl = tokenCreationUrl;
            this.requestPassPhrase = requestPassPhrase;
            this.accessCode = accessCode;
            this.merchantIdentifier = merchantIdentifier;
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

        public String getRequestPassPhrase() {
            return requestPassPhrase;
        }

        public String getAccessCode() {
            return accessCode;
        }

        public String getMerchantIdentifier() {
            return merchantIdentifier;
        }

        @Override
        public String toString() {
            return this.name();
        }
    }
}