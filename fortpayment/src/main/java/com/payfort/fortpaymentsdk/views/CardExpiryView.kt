package com.payfort.fortpaymentsdk.views

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextUtils
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.payfort.fortpaymentsdk.R
import com.payfort.fortpaymentsdk.domain.model.FortError
import com.payfort.fortpaymentsdk.utils.CreditCardUtils
import com.payfort.fortpaymentsdk.views.model.FortViewTypes
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CardExpiryView(context: Context, attrs: AttributeSet?) :
    FortView(context, FortViewTypes.CARD_EXPIRY, attrs) {
    companion object {
        private const val DEFAULT_MAX_LENGTH = 5
        private const val DIVIDER = '/'
        private const val _DIVIDER = "/"

    }

    init {
        inputLayout?.isErrorEnabled = false
        etText?.inputType = InputType.TYPE_CLASS_NUMBER
        etText?.keyListener = DigitsKeyListener.getInstance("1234567890/-")
        etText?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(DEFAULT_MAX_LENGTH))
        var lastInput = ""
        etText?.addTextChangedListener {
            it?.let { it1 -> controlDigits(it1) }
            formatExpiryDate(etText!!, lastInput, it.toString())
            lastInput = text.toString()
            setError(null)
        }
        setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateExpiryDate()
            }
        }
    }

    internal fun getMonth(): Int {
        return TextUtils.split(text, _DIVIDER).orEmpty().getOrNull(0)?.toIntOrNull() ?: -1
    }

    internal fun getYear(): Int {
        return TextUtils.split(text, _DIVIDER).orEmpty().getOrNull(1)?.toIntOrNull() ?: -1
    }

    internal fun validateExpiryDate() {
        val expiryMonth = getMonth()
        val expiryYear = getYear()
        when (CreditCardUtils.isValidCardExpiryDate(Calendar.getInstance(), expiryMonth, expiryYear,text)) {
            null -> setError(null)

            FortError.EMPTY_CARD_DATE -> {setError(resources.getString(R.string.pf_cancel_required_field))}
            FortError.INVALID_CARD_EXPIRY_MONTH ,FortError.INVALID_CARD_EXPIRY_FORMAT-> setError(resources.getString(R.string.pf_cancel_exp_date_invalid))
            FortError.INVALID_CARD_EXPIRY_YEAR, FortError.INVALID_CARD_EXPIRY_DATE -> setError(resources.getString(R.string.pf_cancel_exp_date_in_past))

        }
    }

    override fun isValid(): Boolean = CreditCardUtils.isValidCardExpiryDate(Calendar.getInstance(), getMonth(), getYear(),text) ==null
    private fun formatExpiryDate(expiryDateET: EditText, lastInput: String, currentInput: String) {
        val formatter = SimpleDateFormat("MM/yy", Locale.US)
        val expiryDateDate = Calendar.getInstance()
        try {
            expiryDateDate.time = formatter.parse(currentInput)
        } catch (e: ParseException) {
            if (currentInput.length == 2 && !lastInput.endsWith("/")) {
                val month = currentInput.replace("/", "").toInt()
                if (month <= 12) {
                    expiryDateET.setText(expiryDateET.text.toString() + "/")
                    expiryDateET.setSelection(expiryDateET.text.toString().length)
                } else {
                    expiryDateET.setText("0" + expiryDateET.text.toString()[0] + "/" + expiryDateET.text.toString()[1])
                    expiryDateET.setSelection(expiryDateET.text.toString().length)
                }
            } else if (currentInput.length == 2 && lastInput.endsWith("/")) {
                val month = currentInput.replace("/", "").toInt()
                if (month <= 12) {
                    expiryDateET.setText(expiryDateET.text.toString().substring(0, 1))
                    expiryDateET.setSelection(expiryDateET.text.toString().length)
                } else {
                    expiryDateET.setText("")
                    expiryDateET.setSelection(expiryDateET.text.toString().length)
                }
            } else if (currentInput.length == 1) {
                val month = currentInput.replace("/", "").toInt()
                if (month > 1) {
                    expiryDateET.setText("""0${expiryDateET.text}/""")
                    expiryDateET.setSelection(expiryDateET.text.toString().length)
                }
            }
        }
    }
    private fun controlDigits(editable: Editable) {
        for (index in editable.toString().length downTo 1) {
            val digit = editable.subSequence(index - 1, index)[0]

            // only one / must be entered and that on the 2nd index MM/YY
            if ('/' == digit && index - 1 == 2) {
                continue
            }
            if (!Character.isDigit(digit)) {
                editable.replace(index - 1, index, "")
            }
        }
    }

}