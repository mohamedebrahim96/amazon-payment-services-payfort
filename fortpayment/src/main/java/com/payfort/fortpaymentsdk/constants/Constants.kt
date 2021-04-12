package com.payfort.fortpaymentsdk.constants

object Constants {

    const val ENVIRONMENT_SANDBOX = "https://sbcheckout.payfort.com"
    const val ENVIRONMENT_PRODUCTION = "https://checkout.payfort.com"

    object EXTRAS {
        const val SDK_MERCHANT_REQUEST = "merchantReq"
        const val SDK_RESPONSE = "sdkResp"
        const val SDK_CURRENCY_DP = "currencyDecimalPoints"
        const val SDK_MERCHANT_TOKEN = "merchantToken"
        const val SDK_ENVIRONMENT = "environment"
        const val SDK_SHOW_LOADING = "showLoading"
        const val SDK_DEFAULT_LOCALE = "defaultLocale"
    }

    object LANGUAGES {
        const val ARABIC = "ar"
        const val ENGLISH = "en"
    }

    object FORT_PARAMS {
        const val PAYMENT_OPTION = "payment_option"
        const val LANGUAGE = "language"
        const val STATUS = "status"
        const val RESPONSE_MSG = "response_message"
        const val RESPONSE_CODE = "response_code"
        const val ORDER_DESCRIPTION = "order_description"
        const val AMOUNT = "amount"
        const val CURRENCY = "currency"
        const val CARD_SECURITY_CODE = "card_security_code"
        const val CARD_NUMBER = "card_number"
        const val CUSTOMER_NAME = "customer_name"
        const val EXPIRY_DATE = "expiry_date"
        const val SDK_TOKEN = "sdk_token"
        const val FORT_ID = "fort_id"
        const val REMEMBER_ME = "remember_me"
        const val MERCHSNT_REFERENCE = "merchant_reference"
        const val DEVICE_FINGERPRINT = "device_fingerprint"
        const val CART_DETAILS = "cart_details"
        internal const val ENVIRONMENT = "ENVIRONMENT"
        internal const val SDK_REQUEST = "SDK_REQUEST"

        internal const val CLIENT_SIDE_ERROR = "client_side_error"
        internal const val CLIENT_SIDE_ERROR_DETAILS = "client_side_error_details"
        internal const val CLIENT_SIDE_ERROR_DESCRIPTION = "client_side_error_description"

    }

    object FORT_STATUS {
        const val INVALID_REQUEST = "00"
    }

    object FORT_CODE {
        const val TECHNICAL_PROBLEM = "006"
        const val INIT_CONNECTION_FAILED = "071"
        const val CANCELED_BY_USER = "072"
    }

    object FORT_MSGS {
        const val TECHNICAL_PROBLEM = "Technical problem"
    }

    object CREDIT_CARDS_TYPES {
        const val VISA = "VISA"
        const val MASTERCARD = "MASTERCARD"
        const val AMEX = "AMEX"
        const val MADA = "MADA"
        const val MEEZA = "MEEZA"
        const val MAESTRO = "MAESTRO"
    }

    object FORT_URI {
        const val WV_CHECKER_3DS_PARAMS_URL = "/FortAPI/sdk/process3DsMobile"
        const val VALIDATE_URL = "FortAPI/sdk/validate"
        const val PROCESS_TNX_URL = "/FortAPI/sdk/processSdkTnx"
        const val AUTH_FAILED_URL = "/FortAPI/sdk/authenticationFailed"
    }

    object LOCAL_BROADCAST_EVENTS {
        const val RESPONSE_EVENT = "responseEvent"
    }

    object INDICATORS {
        const val CARD_MASKED_STAR = "*"
        const val REMEMBER_ME_ON = "on"
        const val REMEMBER_ME_OFF = "off"
    }

}