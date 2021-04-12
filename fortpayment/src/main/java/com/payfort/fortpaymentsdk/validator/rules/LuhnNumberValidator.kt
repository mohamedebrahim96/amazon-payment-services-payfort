package com.payfort.fortpaymentsdk.validator.rules

import com.payfort.fortpaymentsdk.validator.CreditCardValidator
import com.payfort.fortpaymentsdk.domain.model.FortError
import com.payfort.fortpaymentsdk.utils.Utils.normalize

internal class LuhnNumberValidator(var cardNumber: String?) :
  CreditCardValidator {


  override fun validate(): Pair<Boolean, FortError?> {
    cardNumber = cardNumber?.normalize()
    if(!isValidLuhnNumber(cardNumber)) return Pair(true,FortError.INVALID_CARD_NUMBER)

    return Pair(false, null)

  }

  /**
   * Checks the input string to see whether or not it is a valid Luhn number.
   *
   * @param cardNumber a String that may or may not represent a valid Luhn number
   * @return `true` if and only if the input value is a valid Luhn number
   */
  private fun isValidLuhnNumber(cardNumber: String?): Boolean {
    if(cardNumber == null) {
      return false
    }
    var isOdd = true
    var sum = 0
    for (index in cardNumber.length - 1 downTo 0) {
      val c = cardNumber[index]
      if(!Character.isDigit(c)) {
        return false
      }
      var digitInteger = Character.getNumericValue(c)
      isOdd = !isOdd
      if(isOdd) {
        digitInteger *= 2
      }
      if(digitInteger > 9) {
        digitInteger -= 9
      }
      sum += digitInteger
    }
    return sum % 10 == 0
  }

}