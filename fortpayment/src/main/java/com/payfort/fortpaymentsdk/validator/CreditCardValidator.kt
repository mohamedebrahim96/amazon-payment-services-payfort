package com.payfort.fortpaymentsdk.validator

import com.payfort.fortpaymentsdk.domain.model.FortError


internal interface CreditCardValidator {
  fun validate(): Pair<Boolean, FortError?>

}