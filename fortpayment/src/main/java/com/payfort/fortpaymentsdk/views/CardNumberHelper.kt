package com.payfort.fortpaymentsdk.views

import android.text.Editable

internal object CardNumberHelper {

    internal const val DIVIDER = ' '
    internal const val DEFAULT_MAX_LENGTH = 19
    internal const val TOTAL_SYMBOLS = 21 // size of pattern 0000-0000-0000-0000
    internal const val TOTAL_DIGITS = 19 // max numbers of digits in pattern: 0000 x 4
    internal const val DIVIDER_MODULO = 5 // means divider position is every 5th symbol beginning with 1
    internal const val DIVIDER_POSITION =
        DIVIDER_MODULO - 1 // means divider position is every 4th symbol beginning with 0

    fun isInputCorrect(
        s: Editable,
        totalSymbols: Int,
        dividerModulo: Int,
        divider: Char
    ): Boolean {
        var isCorrect: Boolean = s.length <= totalSymbols // check size of entered string
        for (i in s.indices) { // check that every element is right
            isCorrect = if (i > 0 && (i + 1) % dividerModulo == 0)
                isCorrect and (divider == s[i])
            else {
                isCorrect and Character.isDigit(s[i])
            }
        }
        return isCorrect
    }

    fun buildCorrectString(digits: CharArray, dividerPosition: Int, divider: Char): String? {
        val formatted = StringBuilder()
        for (i in digits.indices) {
            if (digits[i] != 0.toChar()) {
                formatted.append(digits[i])
                if (i > 0 && i < digits.size - 1 && (i + 1) % dividerPosition == 0) {
                    formatted.append(divider)
                }
            }
        }
        return formatted.toString()
    }

    fun getDigitArray(s: Editable, size: Int): CharArray? {
        val digits = CharArray(size)
        var index = 0
        var i = 0
        while (i < s.length && index < size) {
            val current = s[i]
            if (Character.isDigit(current)) {
                digits[index] = current
                index++
            }
            i++
        }
        return digits
    }


}