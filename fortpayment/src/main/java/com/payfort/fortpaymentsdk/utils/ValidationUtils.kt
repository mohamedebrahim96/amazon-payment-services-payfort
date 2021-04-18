package com.payfort.fortpaymentsdk.utils

import android.text.TextUtils.isDigitsOnly
import androidx.core.text.isDigitsOnly
import java.util.*

internal object ValidationUtils {
    /**
     * Determines whether the input year-month pair has passed.
     *
     * @param year the input year, as a two or four-digit integer
     * @param month the input month
     * @param now the current time
     * @return `true` if the input time has passed the specified current time,
     * `false` otherwise.
     */
    fun hasMonthPassed(year: Int, month: Int, now: Calendar): Boolean {
        return if (hasYearPassed(year, now)) {
            true
        } else normalizeYear(year, now) == now[Calendar.YEAR] && month < (now[Calendar.MONTH] + 1)
        // Expires at end of specified month, Calendar month starts at 0
    }

    /**
     * Determines whether or not the input year has already passed.
     *
     * @param year the input year, as a two or four-digit integer
     * @param now, the current time
     * @return `true` if the input year has passed the year of the specified current time
     * `false` otherwise.
     */
    fun hasYearPassed(year: Int, now: Calendar): Boolean {
        val normalized: Int = normalizeYear(year, now)
        return normalized < now[Calendar.YEAR]
    }

    fun normalizeYear(year: Int, now: Calendar): Int {
        var year = year
        if (year < 100 && year >= 0) {
            val currentYear = now[Calendar.YEAR].toString()
            val prefix = currentYear.substring(0, currentYear.length - 2)
            year = String.format(Locale.US, "%s%02d", prefix, year).toInt()
        }
        return year
    }

    fun isWholePositiveNumber(value: String?): Boolean {

        return value != null && value.isDigitsOnlyKotlin()
    }

   internal fun String.isDigitsOnlyKotlin() = all(Char::isDigit) && isNotEmpty()

}