package com.payfort.fortpaymentsdk.validator.rules

import com.payfort.fortpaymentsdk.validator.CreditCardValidator
import com.payfort.fortpaymentsdk.domain.model.FortError

internal class EmptyValidator(var string: String?, val fortError: FortError) : CreditCardValidator {
    override fun validate(): Pair<Boolean, FortError?> {
        if (string.isNullOrBlank()) return Pair(true, fortError)
        return Pair(false, null)
    }
}