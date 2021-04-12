package com.payfort.fortpaymentsdk.views

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import androidx.core.widget.addTextChangedListener
import com.payfort.fortpaymentsdk.R
import com.payfort.fortpaymentsdk.constants.Constants
import com.payfort.fortpaymentsdk.domain.model.CardBrand
import com.payfort.fortpaymentsdk.domain.model.FortError
import com.payfort.fortpaymentsdk.utils.CreditCardUtils
import com.payfort.fortpaymentsdk.views.model.FortViewTypes

class CardCvvView(context: Context, attrs: AttributeSet?) :
    FortView(context, FortViewTypes.CARD_CVV, attrs) {
    companion object {
        private const val DEFAULT_MAX_LENGTH = 3
    }

    private var cardNumberView: FortCardNumberView? = null
    private var defaultPaymentOption: CardBrand? = null

    init {
        inputLayout?.isErrorEnabled = false
        etText?.inputType = InputType.TYPE_CLASS_NUMBER
        etText?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(DEFAULT_MAX_LENGTH))
        etText?.keyListener = DigitsKeyListener.getInstance("1234567890")

        etText?.addTextChangedListener {
            setError(null)
        }


        setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateCvc()
            } else setError(null)
        }
    }

    internal fun setCardNumberView(cardNumberView: FortCardNumberView?) {
        this.cardNumberView = cardNumberView
        cardNumberView?.onValueChanged {
            getCardBrand()?.let {
                etText?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(it.defaultCvcLength))
            }
        }
    }


    internal fun validateCvc() {

        when (CreditCardUtils.isValidCardCvc(text.toString(), getCardBrand())) {
            null -> setError(null)
            FortError.EMPTY_CARD_CVC -> setError(resources.getString(R.string.pf_cancel_required_field))
            FortError.INVALID_CVC_FORMAT,
            FortError.INVALID_CVC_LENGTH_OTHERS -> setError(resources.getString(R.string.pf_cancel_cvv_length))
            FortError.INVALID_CVC_LENGTH_AMEX -> setError(resources.getString(R.string.pf_cancel_cvv_amex_length))
        }
    }

    override fun isValid(): Boolean =
        CreditCardUtils.isValidCardCvc(text.toString(), getCardBrand()) == null

    private fun getCardBrand(): CardBrand? {
        val cardBrand = CardBrand.fromCardNumberOrNull(cardNumberView?.text.orEmpty())
        return when {
            cardNumberView?.text.orEmpty().contains(Constants.INDICATORS.CARD_MASKED_STAR) -> {
                defaultPaymentOption
            }
            cardBrand == null -> {
                defaultPaymentOption

            }
            else -> cardBrand
        }

    }

    internal fun setUpPaymentOption(defaultPaymentOption: CardBrand?) {
        this.defaultPaymentOption = defaultPaymentOption
        defaultPaymentOption.let {
            etText?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(defaultPaymentOption?.defaultCvcLength!!))
        }
    }


}