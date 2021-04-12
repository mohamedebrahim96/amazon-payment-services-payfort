package com.payfort.fortpaymentsdk.validator.rules

import com.payfort.fortpaymentsdk.domain.model.FortError
import com.payfort.fortpaymentsdk.utils.ValidationUtils
import com.payfort.fortpaymentsdk.validator.CreditCardValidator
import java.util.*

internal class CardExpiryDateValidator(var calendar: Calendar, var expiryMonth: Int?, var expiryYear: Int?) :
    CreditCardValidator {
    override fun validate(): Pair<Boolean, FortError?> {


        if (expiryMonth!! > 0 && expiryYear == -1) {
            return Pair(true,
              FortError.INVALID_CARD_EXPIRY_FORMAT)
        }
        expiryMonth.toString() + expiryYear.toString()

        var monthRangeRule = MonthRangeValidator(
          expiryMonth
        ).validate()
        var yearRule = CardYearValidator(calendar, expiryYear).validate()

        if (monthRangeRule.first) return monthRangeRule
        else if (yearRule.first) return yearRule
        else if (!ValidationUtils.hasMonthPassed(expiryYear!!, expiryMonth!!, calendar)) Pair(true,
          FortError.INVALID_CARD_EXPIRY_DATE)

        return Pair(false, null)

    }


}