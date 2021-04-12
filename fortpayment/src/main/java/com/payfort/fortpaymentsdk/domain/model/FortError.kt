package com.payfort.fortpaymentsdk.domain.model

/**
 * Components Error Types
 * @property errorMessage String
 * @constructor
 */
enum class FortError(val errorMessage: String) {
    INVALID_CARD_NUMBER("Invalid card number"),
    INVALID_CARD_LENGTH("Invalid card length"),
    INVALID_CARD_LENGTH_AMEX("Invalid Card length"),
    INVALID_CVC_LENGTH_AMEX("Invalid CVV length"),
    INVALID_CVC_LENGTH_OTHERS("Invalid CVV length"),
    INVALID_CVC_FORMAT("Invalid CVV Format"),
    INVALID_CARD_EXPIRY_MONTH("Invalid Expiry Month"),
    INVALID_CARD_EXPIRY_FORMAT("Invalid Expiry Format"),
    INVALID_CARD_EXPIRY_YEAR("Invalid Expiry Year"),
    INVALID_CARD_EXPIRY_DATE("Invalid Expiry Date"),
    INVALID_CARD_HOLDER_NAME("Invalid Card Holder Name"),
    CARD_UNSUPPORTED("UnSupported Card"),
    INVALID_PAYMENT_OPTION("INVALID_PAYMENT_OPTION Card"),
    EMPTY_CARD_NUMBER("This Field is Required"),
    EMPTY_CARD_DATE("This Field is Required"),
    EMPTY_CARD_CVC("This Field is Required");

}