package com.payfort.fortpaymentsdk.validator.rules

import com.payfort.fortpaymentsdk.validator.CreditCardValidator
import com.payfort.fortpaymentsdk.domain.model.FortError

internal class MonthRangeValidator(var expiryMonth: Int?) :
  CreditCardValidator {


  private fun isValidMonth(expiryMonth: Int?): Boolean {
    return expiryMonth != null && expiryMonth >= 1 && expiryMonth <= 12
  }

  override fun validate(): Pair<Boolean, FortError?> {

    if(!isValidMonth(expiryMonth)) return Pair(true, FortError.INVALID_CARD_EXPIRY_MONTH)

    return Pair(false, null)

  }

}