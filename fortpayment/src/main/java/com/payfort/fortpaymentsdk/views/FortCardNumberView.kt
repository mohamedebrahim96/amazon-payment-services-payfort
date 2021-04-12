package com.payfort.fortpaymentsdk.views

import android.content.Context
import android.text.InputFilter
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout.END_ICON_CUSTOM
import com.payfort.fortpaymentsdk.R
import com.payfort.fortpaymentsdk.constants.Constants
import com.payfort.fortpaymentsdk.domain.model.CardBrand
import com.payfort.fortpaymentsdk.domain.model.FortError
import com.payfort.fortpaymentsdk.domain.model.SdkRequest
import com.payfort.fortpaymentsdk.domain.model.StringResult
import com.payfort.fortpaymentsdk.domain.usecase.ValidateCardNumberUseCase
import com.payfort.fortpaymentsdk.utils.CreditCardUtils
import com.payfort.fortpaymentsdk.utils.InjectionUtils
import com.payfort.fortpaymentsdk.utils.Utils
import com.payfort.fortpaymentsdk.views.CardNumberHelper.DEFAULT_MAX_LENGTH
import com.payfort.fortpaymentsdk.views.model.FortViewTypes
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


class FortCardNumberView(context: Context, attrs: AttributeSet?) :
    FortView(context, FortViewTypes.CARD_NUMBER, attrs) {


    private var validateCardNumber: ValidateCardNumberUseCase? = null
    private var isValid = true
    private var cardBrand: CardBrand? = null
    private var lastCardBrand: CardBrand? = null
    private var serverCardBrand: CardBrand? = null
    internal var onMaskedNumberChanged: ((Boolean) -> Unit)? = null
    internal var onValueChanged: ((Boolean) -> Unit)? = null

    fun setOnMaskedNumberChanged(onMaskedNumberChanged: (Boolean) -> Unit) {
        this.onMaskedNumberChanged = onMaskedNumberChanged
    }

    internal fun onValueChanged(onValueChanged: (Boolean) -> Unit) {
        this.onValueChanged = onValueChanged
    }

    /**
     * The default payment option
     */
    private var defaultPaymentOption: CardBrand? = null
        set(value) {
            field = value
            if (value != null) {
                inputLayout?.setEndIconDrawable(value.icon)
            }
        }

    internal fun setUp(environment: String, defaultPaymentOption: CardBrand?) {
        this.defaultPaymentOption = defaultPaymentOption
        validateCardNumber = ValidateCardNumberUseCase(InjectionUtils.provideFortRepository(environment))
    }

    init {
        inputLayout?.isErrorEnabled = false
        inputLayout?.isEndIconVisible = true
        inputLayout?.endIconMode = END_ICON_CUSTOM
        etText?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(DEFAULT_MAX_LENGTH))
        etText?.keyListener = DigitsKeyListener.getInstance("1234567890")
        etText?.addTextChangedListener {
            val cardNumber = it.toString()
            val count = cardNumber.length
            serverCardBrand = null

            val cardBrand = CardBrand.fromCardNumber(cardNumber)
            if (cardBrand != CardBrand.UNKNOWN)
                lastCardBrand = cardBrand

            this.cardBrand = cardBrand
            if (lastCardBrand != null)
                etText?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(this.lastCardBrand?.defaultMaxLength!!))
            else
                etText?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(this.cardBrand?.defaultMaxLength!!))

            if (defaultPaymentOption == null) {
                if (cardNumber.length >= 4) {
                    inputLayout?.isEndIconVisible = this.cardBrand != CardBrand.UNKNOWN
                    inputLayout?.setEndIconDrawable(this.cardBrand?.icon!!)
                } else inputLayout?.isEndIconVisible = false
            }



            if (((count == 15 && cardBrand == CardBrand.AMEX) || count == 16 || count == 19) && !cardNumber.contains(Constants.INDICATORS.CARD_MASKED_STAR)) {
                checkCardNumber()
            }

            if (cardNumber.contains(Constants.INDICATORS.CARD_MASKED_STAR)) {
                onMaskedNumberChanged?.invoke(true)
            }

            onValueChanged?.invoke(true)

        }
        setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) validateCardNumber() else setError(null)
        }
    }

    internal fun validateCardNumber() {

        when (CreditCardUtils.isValidCardNumber(text.toString(), defaultPaymentOption, serverCardBrand)) {
            FortError.INVALID_PAYMENT_OPTION -> {
                setError(resources.getString(R.string.pf_errors_card_number_mismatch_po))
            }
            FortError.INVALID_CARD_LENGTH -> {
                setError(resources.getString(R.string.pf_errors_card_number_length))
            }
            FortError.INVALID_CARD_LENGTH_AMEX -> {
                setError(resources.getString(R.string.pf_errors_card_number_amex_length))
            }
            FortError.CARD_UNSUPPORTED,
            FortError.INVALID_CARD_NUMBER -> setError(resources.getString(R.string.pf_errors_card_number_invalid))
            FortError.EMPTY_CARD_NUMBER -> setError(resources.getString(R.string.pf_cancel_required_field))
            null -> setError(null)
        }
    }


    private fun checkCardNumber() {
        isValid = false
        val sdkRequest = SdkRequest()
        sdkRequest.deviceId = Utils.getDeviceId(context)
        val paramsMap: MutableMap<String, Any> = HashMap()
        paramsMap[Constants.FORT_PARAMS.CARD_NUMBER] = text.toString()
        sdkRequest.requestMap = paramsMap
        validateCardNumber?.let {
            it.execute(sdkRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    when (it) {
                        is StringResult.Success -> {
                            val cardType = it.response
                            serverCardBrand = CardBrand.fromCode(cardType)

                            if (defaultPaymentOption != null && defaultPaymentOption?.issuerName != cardType) {
                                isValid = false
                                setError(resources.getString(R.string.pf_errors_card_number_mismatch_po))
                            } else {
                                var cardBrand = 0
                                when (cardType) {
                                    Constants.CREDIT_CARDS_TYPES.VISA -> {
                                        cardBrand = R.drawable.ic_visa
                                    }
                                    Constants.CREDIT_CARDS_TYPES.MASTERCARD -> {
                                        cardBrand = R.drawable.ic_master_card
                                    }
                                    Constants.CREDIT_CARDS_TYPES.AMEX -> {
                                        cardBrand = R.drawable.ic_amex
                                    }

                                    Constants.CREDIT_CARDS_TYPES.MADA -> {
                                        cardBrand = R.drawable.ic_mada_card
                                    }
                                    Constants.CREDIT_CARDS_TYPES.MEEZA -> {
                                        cardBrand = R.drawable.ic_meza
                                    }
                                }
                                inputLayout?.setEndIconDrawable(cardBrand)
                                isValid = true
                                setError(null)
                            }

                        }
                        is StringResult.Failure -> {
                            validateCardNumber()
                        }
                    }
                }
        }


    }

    override fun isValid(): Boolean =
        isValid && CreditCardUtils.isValidCardNumber(text, defaultPaymentOption, serverCardBrand) == null


}