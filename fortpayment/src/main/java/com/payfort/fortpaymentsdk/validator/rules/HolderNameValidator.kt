package com.payfort.fortpaymentsdk.validator.rules

import com.payfort.fortpaymentsdk.validator.CreditCardValidator
import com.payfort.fortpaymentsdk.domain.model.FortError

internal class HolderNameValidator(var holderName: String?) :
  CreditCardValidator {

  private  val EXPRESSION_HOLDER_NAME = "^[a-zA-Z ']{0,50}"

  override fun validate(): Pair<Boolean, FortError?> {
    return if(!isValidName(holderName)) Pair(true, FortError.INVALID_CARD_HOLDER_NAME)
    else Pair(false, null)
  }

  private fun isValidName(txt: String?): Boolean {
    return txt != null && txt.matches(Regex(EXPRESSION_HOLDER_NAME))
  }

}