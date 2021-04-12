package com.payfort.fortpaymentsdk.utils

import com.payfort.fortpaymentsdk.constants.Constants
import com.payfort.fortpaymentsdk.domain.model.CardBrand
import com.payfort.fortpaymentsdk.domain.model.FortError
import com.payfort.fortpaymentsdk.validator.CardValidator
import java.util.*

internal object CreditCardUtils {


    @JvmStatic
    fun isValidCardNumber(cardNumber: String?, defaultPaymentOption: CardBrand?, serverCardBrand: CardBrand?): FortError? {
        if (!cardNumber.isNullOrEmpty() && cardNumber.contains(Constants.INDICATORS.CARD_MASKED_STAR)) {
            return null
        }
        return CardValidator.CardValidatorBuilder()
            .addEmptyRule(cardNumber, FortError.EMPTY_CARD_NUMBER)
            .addCardTypeRule(cardNumber,defaultPaymentOption,serverCardBrand)
            .addCardNumberLengthRule(cardNumber)
            .addLuhnNumberRule(cardNumber)
            .build().execute()
    }



    @JvmStatic
    fun isValidCardCvc(cvc: String?, cardBrand: CardBrand?): FortError? =
        CardValidator.CardValidatorBuilder()
            .addEmptyRule(cvc, FortError.EMPTY_CARD_CVC)
            .addCvcLengthRule(cvc, cardBrand).addCvcFormatRule(cvc).build().execute()


    @JvmStatic
    fun isValidCardExpiryDate(calendar: Calendar, expiryMonth: Int?, expiryYear: Int?): FortError? {
        val date = expiryMonth!! + expiryYear!!
        if (date != null && date == -2)
            return FortError.INVALID_CARD_EXPIRY_FORMAT

        if (date < 0)
            return FortError.EMPTY_CARD_DATE

        return CardValidator.CardValidatorBuilder()
            .addCardExpiryDateRule(calendar, expiryMonth,
                expiryYear).build().execute()
    }

    @JvmStatic
    fun isValidCardExpiryDate(calendar: Calendar, expiryMonth: Int?, expiryYear: Int?, expiry: String?): FortError? {
        if (expiry.isNullOrEmpty())
            return FortError.EMPTY_CARD_DATE

        if (!expiry.isNullOrEmpty() && expiry.length == 1)
            return FortError.INVALID_CARD_EXPIRY_FORMAT


        return CardValidator.CardValidatorBuilder()
            .addCardExpiryDateRule(calendar, expiryMonth,
                expiryYear).build().execute()
    }


    @JvmStatic
    fun isValidCardHolderName(holderName: String?): FortError? =
        CardValidator.CardValidatorBuilder().addHolderNameRule(holderName).build().execute()


    /**
     * Returns a [CardBrand] corresponding to a partial card number,
     * or [CardBrand.UNKNOWN] if the card brand can't be determined from the input value.
     *
     * @param cardNumber a credit card number or partial card number
     * @return the [CardBrand] corresponding to that number,
     * or [CardBrand.UNKNOWN] if it can't be determined
     */

    @JvmStatic
    fun getPossibleCardType(cardNumber: String?): CardBrand {
        return CardBrand.fromCardNumber(cardNumber)
    }


}