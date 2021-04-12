package com.payfort.fortpaymentsdk.domain.model

import androidx.annotation.DrawableRes
import com.payfort.fortpaymentsdk.R
import java.util.regex.Pattern

/**
 * this class responsible for detect cardBrand via cardNumber or issuerName
 * @property issuerName String
 * @property icon Int
 * @property defaultMaxLength Int
 * @property defaultMinLength Int
 * @property pattern Pattern?
 * @property defaultCvcLength Int
 * @constructor
 */
enum class CardBrand(
    val issuerName: String,
    @DrawableRes var icon: Int,

    /**
     * The default max length when the card number is formatted without spaces (e.g. "4242424242424242")
     */
    var defaultMaxLength: Int = 16,

    var defaultMinLength: Int = 16,

    /** Based on [Issuer identification number table](http://en.wikipedia.org/wiki/Bank_card_number#Issuer_identification_number_.28IIN.29)*/
    private val pattern: Pattern? = null,

    /** Patterns for discrete lengths*/

    var defaultCvcLength: Int = 3,

    ) {
    VISA(
        pattern = Pattern.compile("^4"),
        issuerName = "VISA",
        icon = R.drawable.ic_visa
    ),
    MASTERCARD(
        pattern = Pattern.compile("^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)"),
        issuerName = "MASTERCARD", icon = R.drawable.ic_master_card,

        ),
    AMEX(
        pattern = Pattern.compile("^3[47]"),
        issuerName = "AMEX", icon = R.drawable.ic_amex,
        defaultMaxLength = 15, defaultMinLength = 15, defaultCvcLength = 4
    ),
    MEEZA(
        pattern = Pattern.compile("^6(?:2(?:7(?:59[07]|632|382)|(?:805|766)9|2009|7(?:88|77)8)|0(?:3(?:6(?:65|06)|3(?:53|36)|370)|2(?:85[05]|050)|3794|3(?:73|34|01)8|1(?:844|52[06]|467|368)))[0-9]{10,13}"),
        issuerName = "MEEZA", icon = R.drawable.ic_meza, defaultMaxLength = 19, defaultMinLength = 16
    ),
    MEEZA_2(
        pattern = Pattern.compile("^9818"),
        issuerName = "MEEZA", icon = R.drawable.ic_meza, defaultMaxLength = 19

    ),
    MAESTRO(
        pattern = Pattern.compile("^(5[06-8]|6\\d)\\d{14}(\\d{2,3})?\$"),
        issuerName = "MEEZA", icon = R.drawable.ic_meza, defaultMaxLength = 19
    ),
    MADA(
        issuerName = "MADA", icon = R.drawable.ic_mada_card, defaultMaxLength = 16
    ),
    UNKNOWN("UNKNOWN", 0);


    private fun getPatternForLength(): Pattern? {
        return pattern
    }

    companion object {
        /**@param cardNumber a card number
         * @return the [CardBrand] that matches the [cardNumber]'s prefix, if one is found;
         * otherwise, [CardBrand.Unknown]
         */
        fun fromCardNumber(cardNumber: String?): CardBrand {
            if (cardNumber.isNullOrBlank()) {
                return UNKNOWN
            }

            return values()
                .firstOrNull { cardBrand ->
                    cardBrand.getPatternForLength()?.matcher(cardNumber)?.find() == true
                } ?: UNKNOWN
        }

        /** @param cardNumber a card number
          * @return the [CardBrand] that matches the [cardNumber]'s prefix, if one is found;
          * otherwise, [Null]
         */
        fun fromCardNumberOrNull(cardNumber: String?): CardBrand? {
            if (cardNumber.isNullOrBlank()) {
                return null
            }

            return values()
                .firstOrNull { cardBrand ->
                    cardBrand.getPatternForLength()?.matcher(cardNumber)
                        ?.find() == true
                } ?: null
        }

        /**
         * @param code a brand code, such as `Visa` or `American Express`. */
        fun fromCode(code: String?): CardBrand {
            return values().firstOrNull { it.issuerName.equals(code, ignoreCase = true) } ?: UNKNOWN
        }

        /**
       * @param code a brand code, such as `Visa` or `American Express`. */
        fun fromCodeOrNull(code: String?): CardBrand? {
            return values().firstOrNull { it.issuerName.equals(code, ignoreCase = true) } ?: null
        }

    }

}
