package com.payfort.fortpaymentsdk.validator.rules

import com.payfort.fortpaymentsdk.domain.model.CardBrand
import com.payfort.fortpaymentsdk.domain.model.FortError
import com.payfort.fortpaymentsdk.utils.Utils.normalize
import com.payfort.fortpaymentsdk.validator.CreditCardValidator

internal class CardNumberLengthValidator(var cardNumber: String?) :
    CreditCardValidator {


    override fun validate(): Pair<Boolean, FortError?> {
        cardNumber = cardNumber?.normalize()
        val cardBrand = CardBrand.fromCardNumber(cardNumber)

        return if (cardNumber != null && cardNumber?.length!! < 4)
            Pair(true, FortError.INVALID_CARD_NUMBER)
        else
            if (cardNumber != null && cardNumber!!.length >= 4 && cardNumber!!.length < cardBrand.defaultMinLength && cardBrand == CardBrand.AMEX)
                Pair(true, FortError.INVALID_CARD_LENGTH_AMEX)
            else
                if (cardNumber != null && !isValidCardLength(cardNumber, cardBrand))
                    Pair(true, FortError.INVALID_CARD_LENGTH)
                else Pair(false, null)
    }



/**
 * Checks to see whether the input number is of the correct length, given the assumed brand of
 * the card. This function does not perform a Luhn check.
 *
 * @param cardNumber the card number with no spaces or dashes
 * @param cardBrand  a [CardBrand] used to get the correct size
 * @return `true` if the card number is the correct length for the assumed brand
 */

private fun isValidCardLength(cardNumber: String?, cardBrand: CardBrand): Boolean {
    if (cardNumber == null) {
        return false
    }
    val length = cardNumber.length
    return length <= cardBrand.defaultMaxLength && length >= cardBrand.defaultMinLength
}

}