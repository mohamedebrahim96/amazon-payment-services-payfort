package com.payfort.fortpaymentsdk.validator

import com.payfort.fortpaymentsdk.domain.model.CardBrand
import com.payfort.fortpaymentsdk.domain.model.FortError
import com.payfort.fortpaymentsdk.validator.rules.CardExpiryDateValidator
import com.payfort.fortpaymentsdk.validator.rules.CardNumberLengthValidator
import com.payfort.fortpaymentsdk.validator.rules.CardTypeValidator
import com.payfort.fortpaymentsdk.validator.rules.CardYearValidator
import com.payfort.fortpaymentsdk.validator.rules.CvcFormatValidator
import com.payfort.fortpaymentsdk.validator.rules.CvcLengthValidator
import com.payfort.fortpaymentsdk.validator.rules.EmptyValidator
import com.payfort.fortpaymentsdk.validator.rules.HolderNameValidator
import com.payfort.fortpaymentsdk.validator.rules.LuhnNumberValidator
import com.payfort.fortpaymentsdk.validator.rules.MonthRangeValidator
import java.util.*
import kotlin.collections.ArrayList

internal class CardValidator private constructor(builder: CardValidatorBuilder) {
  private var listOfCreditCards: ArrayList<CreditCardValidator>

  init {
    listOfCreditCards = builder.listOfRules;
  }

  fun execute(): FortError? {
    listOfCreditCards.forEach {
      var validate = it.validate()
      if(validate.first) return validate.second
    }
    return null
  }


  internal class CardValidatorBuilder {
    var listOfRules = ArrayList<CreditCardValidator>()

    fun addMonthRangeRule(expiryMonth: Int?) =
      apply { listOfRules.add(
        MonthRangeValidator(
          expiryMonth
        )
      ) }


    fun addLuhnNumberRule(cardNumber: String?) =
      apply { listOfRules.add(
        LuhnNumberValidator(
          cardNumber
        )
      ) }


    fun addHolderNameRule(holderName: String?) =
      apply { listOfRules.add(
        HolderNameValidator(
          holderName
        )
      ) }


    fun addCvcLengthRule(cvcValue: String?, cardBrand: CardBrand?) = apply { listOfRules.add(
      CvcLengthValidator(
        cvcValue,cardBrand
      )
    ) }


    fun addCvcFormatRule(cvcValue: String?) = apply { listOfRules.add(
      CvcFormatValidator(
        cvcValue
      )
    ) }


    fun addCardYearRule(calendar: Calendar, expiryYear: Int?) =
      apply { listOfRules.add(
        CardYearValidator(
          calendar,
          expiryYear
        )
      ) }


    fun addCardTypeRule(cardNumber: String?, defaultPaymentOption: CardBrand?, serverCardBrand: CardBrand?) = apply { listOfRules.add(
      CardTypeValidator(
        cardNumber,defaultPaymentOption,serverCardBrand
      )
    ) }


    fun addCardNumberLengthRule(cardNumber: String?) =
      apply { listOfRules.add(
        CardNumberLengthValidator(
          cardNumber
        )
      ) }

    fun addEmptyRule(strValue:String?, fortError: FortError)=apply {
      listOfRules.add(EmptyValidator(strValue,fortError))
    }


    fun addCardExpiryDateRule(calendar: Calendar, expiryMonth: Int?, expiryYear: Int?) = apply {
      listOfRules.add(
          CardExpiryDateValidator(
              calendar,
              expiryMonth,
              expiryYear
          )
      )
    }

    fun build() = CardValidator(this)

  }
}