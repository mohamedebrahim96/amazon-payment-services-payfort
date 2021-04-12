package com.payfort.fortpaymentsdk.validator.rules

import com.payfort.fortpaymentsdk.domain.model.CardBrand
import com.payfort.fortpaymentsdk.domain.model.FortError
import com.payfort.fortpaymentsdk.utils.Utils.normalize
import com.payfort.fortpaymentsdk.validator.CreditCardValidator

internal class CardTypeValidator(var cardNumber: String?,
                                 private val defaultPaymentOption: CardBrand?,
                                 private val serverCardBrand: CardBrand?) :
    CreditCardValidator {


    override fun validate(): Pair<Boolean, FortError?> {
        cardNumber = cardNumber?.normalize()
        val cardBrand = CardBrand.fromCardNumber(cardNumber)
        return if (cardNumber != null && cardNumber?.length!! < 4)
            Pair(true, FortError.INVALID_CARD_NUMBER)

      else  if (cardNumber != null && cardBrand == CardBrand.UNKNOWN)
            Pair(true, FortError.CARD_UNSUPPORTED)

        else if (serverCardBrand != null && defaultPaymentOption != null && defaultPaymentOption.issuerName != serverCardBrand.issuerName)
            Pair(true, FortError.INVALID_PAYMENT_OPTION)
        else
            if (defaultPaymentOption != null && defaultPaymentOption.issuerName != cardBrand.issuerName && defaultPaymentOption != CardBrand.MADA)
                Pair(true, FortError.INVALID_PAYMENT_OPTION)
                else Pair(false, null)

    }


}