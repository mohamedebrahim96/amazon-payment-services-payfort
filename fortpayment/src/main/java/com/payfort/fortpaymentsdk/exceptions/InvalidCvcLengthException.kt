package com.payfort.fortpaymentsdk.exceptions

import com.payfort.fortpaymentsdk.domain.model.CardBrand

/**
 * Exception thrown when Pay with DirectPay and Cvv
 * does not Match the merchant Token Payment option Type
 */
internal class InvalidCvcLengthException( val cardBrand: CardBrand?) : Exception() {

    override val message: String
        get() = "Invalid Cvc Length Exception ${cardBrand?.issuerName}"
}