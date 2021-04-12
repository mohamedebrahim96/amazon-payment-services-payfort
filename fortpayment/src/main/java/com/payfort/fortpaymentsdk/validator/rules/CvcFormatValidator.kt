package com.payfort.fortpaymentsdk.validator.rules

import com.payfort.fortpaymentsdk.validator.CreditCardValidator
import com.payfort.fortpaymentsdk.domain.model.FortError
import com.payfort.fortpaymentsdk.utils.ValidationUtils

internal class CvcFormatValidator(var cvcValue: String?) :
    CreditCardValidator {


  override fun validate(): Pair<Boolean, FortError?> {
    return if (cvcValue != null && cvcValue!!.isNotBlank() && !ValidationUtils.isWholePositiveNumber(cvcValue))
        return Pair(true, FortError.INVALID_CVC_FORMAT)
    else Pair(false, null)


  }


}