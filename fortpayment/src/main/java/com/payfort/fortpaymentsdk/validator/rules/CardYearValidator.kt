package com.payfort.fortpaymentsdk.validator.rules

import com.payfort.fortpaymentsdk.validator.CreditCardValidator
import com.payfort.fortpaymentsdk.domain.model.FortError
import com.payfort.fortpaymentsdk.utils.ValidationUtils
import java.util.*

internal class CardYearValidator(val calendar: Calendar, val expiryYear: Int?) :
  CreditCardValidator {

  override fun validate(): Pair<Boolean, FortError?> {
    return if(!isValidYear(calendar, expiryYear)) Pair(true, FortError.INVALID_CARD_EXPIRY_YEAR)
    else Pair(false, null)

  }


  private fun isValidYear(now: Calendar, expiryYear: Int?): Boolean {
    return expiryYear != null && !ValidationUtils.hasYearPassed(expiryYear, now)
  }

}