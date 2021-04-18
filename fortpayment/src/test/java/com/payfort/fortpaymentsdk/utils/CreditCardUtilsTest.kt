package com.payfort.fortpaymentsdk.utils

import com.payfort.fortpaymentsdk.domain.model.CardBrand
import com.payfort.fortpaymentsdk.domain.model.FortError
import org.junit.Before
import org.junit.Test
import java.util.*

class CreditCardUtilsTest {
    private val YEAR_IN_FUTURE = 2200


    private val calendar = Calendar.getInstance()

    @Before
    fun setup() {
        calendar[Calendar.YEAR] = 1990
        calendar[Calendar.MONTH] = Calendar.APRIL
        calendar[Calendar.DAY_OF_MONTH] = 30
    }


    @Test
    fun invalidCardNumber_thenReturnError__INVALID_CARD_NUMBER() {
        val validator = CreditCardUtils.isValidCardNumber("411",null,null)
        print(validator.toString())
        assert(validator == FortError.INVALID_CARD_NUMBER)
    }

    @Test
    fun invalidCardNumber_thenReturnError_EMPTY_CARD_NUMBER() {
        val validator = CreditCardUtils.isValidCardNumber("",null,null)
        assert(validator == FortError.EMPTY_CARD_NUMBER)
    }

    @Test
    fun invalidCardNumber_thenReturnError_CARD_UNSUPPORTED() {
        val validator = CreditCardUtils.isValidCardNumber("1500000000000004",null,null)
        assert(validator == FortError.CARD_UNSUPPORTED)
    }

    @Test
    fun invalidCardNumber_thenReturnError_INVALID_CARD_NUMBER() {
        val validator = CreditCardUtils.isValidCardNumber("4111111111111112",null,null)
        assert(validator == FortError.INVALID_CARD_NUMBER)
    }

    @Test
    fun invalidCardHolderName_thenReturnError() {
        val validator = CreditCardUtils.isValidCardHolderName("123")
        assert(validator == FortError.INVALID_CARD_HOLDER_NAME)
    }

    @Test
    fun validCardHolderName_thenReturnNullError() {
        val validator = CreditCardUtils.isValidCardHolderName("Ali")
        assert(validator == null)
    }

    @Test
    fun validCvcWithAmexCardBrand_thenReturnTrue() {
        var validator = CreditCardUtils.isValidCardCvc("1244", CardBrand.AMEX)
        assert(validator == null)
    }

    @Test
    fun invalidCvcWithAmexCardBrand_thenReturnError() {
        var validator = CreditCardUtils.isValidCardCvc("124", CardBrand.AMEX)
        print(validator)
        assert(validator == FortError.INVALID_CVC_LENGTH_AMEX)
    }

    @Test
    fun validCvcWithVisaCardBrand_thenReturnTrue() {
        var validator = CreditCardUtils.isValidCardCvc("124", CardBrand.VISA)
        assert(validator == null)
    }
    @Test
    fun emptyCvcWithVisaCardBrand_thenReturn_EMPTY_CVC() {
        var validator = CreditCardUtils.isValidCardCvc("", CardBrand.VISA)
        assert(validator == FortError.EMPTY_CARD_CVC)
    }

    @Test
    fun enterPastYear_thenReturn_INVALID_CARD_EXPIRY_YEAR(){
        val validCardExpiryDate = CreditCardUtils.isValidCardExpiryDate(Calendar.getInstance(), 12, 1960)
        assert(validCardExpiryDate == FortError.INVALID_CARD_EXPIRY_YEAR)
    }


    @Test
    fun enterInvalidMonth_thenReturn_INVALID_CARD_EXPIRY_MONTH(){
        val validCardExpiryDate = CreditCardUtils.isValidCardExpiryDate(Calendar.getInstance(), 0, YEAR_IN_FUTURE)
        assert(validCardExpiryDate == FortError.INVALID_CARD_EXPIRY_MONTH)
    }

    @Test
    fun enterValidCardDate_thenReturn_NullError(){
        val validCardExpiryDate = CreditCardUtils.isValidCardExpiryDate(Calendar.getInstance(), 12, 2021)
        assert(validCardExpiryDate == null)
    }

}