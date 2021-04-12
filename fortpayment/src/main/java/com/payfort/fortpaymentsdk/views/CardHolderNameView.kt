package com.payfort.fortpaymentsdk.views

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.text.Spanned
import android.util.AttributeSet
import com.payfort.fortpaymentsdk.utils.CreditCardUtils
import com.payfort.fortpaymentsdk.views.model.FortViewTypes
import kotlinx.android.synthetic.main.fort_view.view.*

class CardHolderNameView(context: Context, attrs: AttributeSet?) :
    FortView(context, FortViewTypes.CARD_HOLDER_NAME,attrs) {
    companion object {
        private const val DEFAULT_MAX_LENGTH = 50
    }
    init {
        inputLayout?.isErrorEnabled=false
        etText?.inputType = InputType.TYPE_CLASS_TEXT
        etText?.filters = arrayOf(InputFilter.LengthFilter(DEFAULT_MAX_LENGTH))
    }

    override fun isValid(): Boolean =CreditCardUtils.isValidCardHolderName(text.toString()) ==null



}