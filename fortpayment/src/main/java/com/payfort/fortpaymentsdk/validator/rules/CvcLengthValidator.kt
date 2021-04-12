package com.payfort.fortpaymentsdk.validator.rules

import com.payfort.fortpaymentsdk.domain.model.CardBrand
import com.payfort.fortpaymentsdk.domain.model.FortError
import com.payfort.fortpaymentsdk.validator.CreditCardValidator

internal class CvcLengthValidator(var cvcValue: String?, var cardBrand:  CardBrand?) :
    CreditCardValidator {


    override fun validate(): Pair<Boolean, FortError?> {

        return if (cardBrand != null && cardBrand == CardBrand.AMEX && !isValidCVCLength(cvcValue, cardBrand!!.defaultCvcLength)) {
            return Pair(true, FortError.INVALID_CVC_LENGTH_AMEX)

        } else if (cardBrand != null && cardBrand != CardBrand.AMEX && !isValidCVCLength(cvcValue))
            return Pair(true, FortError.INVALID_CVC_LENGTH_OTHERS)
        else Pair(false, null)
    }

    private fun isValidCVCLength(cvcValue: String?, validLength: Int = 3): Boolean {
        return cvcValue != null && cvcValue.length == validLength
    }

}