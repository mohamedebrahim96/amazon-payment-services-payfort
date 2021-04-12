package com.payfort.fortpaymentsdk.exceptions;

import androidx.annotation.Nullable;

/**
 * Exception thrown when Pay with DirectPay and
 * merchant Token not exist
 */
public class MerchantTokenNoExistException extends Exception {

    @Nullable
    @Override
    public String getMessage() {
        return "Merchant Token No Exist Exception";
    }
}
