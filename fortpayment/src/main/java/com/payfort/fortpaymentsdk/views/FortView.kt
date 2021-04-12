package com.payfort.fortpaymentsdk.views

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.google.android.material.textfield.TextInputLayout
import com.payfort.fortpaymentsdk.R
import com.payfort.fortpaymentsdk.utils.Utils.convertDpToPixel
import com.payfort.fortpaymentsdk.views.model.FortViewTypes
import com.payfort.fortpaymentsdk.views.model.FortViewTypes.*
import kotlinx.android.synthetic.main.fort_view.view.*

abstract class FortView @JvmOverloads constructor(
    context: Context, fortViewType: FortViewTypes, attrs: AttributeSet? = null
) : FrameLayout(context, attrs, 0) {

    companion object {
        private const val DEFAULT_SIZE = 13F
        private const val DEFAULT_BOX_SHAPE = 1
    }

    private val DEFAULT_COLOR = ContextCompat.getColor(context, android.R.color.black)
    private val GRAY_HINT_COLOR = ContextCompat.getColor(context, android.R.color.darker_gray)
    internal var etText: EditText? = null
    internal var inputLayout: TextInputLayout? = null

    abstract fun isValid(): Boolean


    /**
     * The text Color
     */
    var textColor: Int = Color.BLACK
        set(value) {
            field = value
            etText?.setTextColor(field)

        }

    /**
     * The text
     */
    internal var text: String? = ""
        set(value) {
            field = value
            etText?.setText(field)
        }
        get() = etText?.text.toString().trim()


    /**
     * The text size
     */
    var textSize: Float = DEFAULT_SIZE
        set(value) {
            field = value
            etText?.setTextSize(TypedValue.COMPLEX_UNIT_PX, field)
        }


    /**
     *  hint text
     */
    var hint: String? = ""
        set(value) {
            field = value
            inputLayout?.hint = field
            inputLayout?.hint
        }

    /**
     * hint Color
     */
    var hintTextColor: Int = Color.LTGRAY
        set(value) {
            field = value
            inputLayout?.hintTextColor = ColorStateList.valueOf(field)
        }

    /**
     * box Background Mode
     */
    var boxBackgroundMode: Int = DEFAULT_BOX_SHAPE
        set(value) {
            field = value
            inputLayout?.boxBackgroundMode = value
        }

    /**
     * The box Stroke Color
     */
    var boxBackgroundColor: Int = Color.BLACK
        set(value) {
            field = value
            inputLayout?.boxBackgroundColor = field
        }


    /**
     * error Text Appearance
     */
    var errorTextAppearance: Int = 0
        set(value) {
            field = value
            inputLayout?.setErrorTextAppearance(field)
        }

    /**
     * The box error Stroke Color
     */
    var boxStrokeErrorColor: Int = Color.RED
        set(value) {
            field = value
            inputLayout?.boxStrokeErrorColor = ColorStateList.valueOf(field)
        }


    /**
     * error Text Color
     */
    var errorTextColor: Int = Color.RED
        set(value) {
            field = value
            inputLayout?.setErrorTextColor(ColorStateList.valueOf(errorTextColor))
        }

    /**
     * error Text
     */
    open var errorText: String? = null
        set(value) {
            field = value
            inputLayout?.error = field
        }


    /**
     * is Field Enabled
     * @param enabled Boolean
     */
    override fun setEnabled(enabled: Boolean) {
        etText?.isEnabled = enabled
        inputLayout?.isEnabled = enabled
        super.setEnabled(enabled)
    }

    /**
     * Set Error text
     * @param error String?
     */
    fun setError(error: String?) = if (error == null) {
        inputLayout?.isErrorEnabled = false
        errorText = null
    } else {
        inputLayout?.isErrorEnabled = true
        errorText = error
    }

    override fun getOnFocusChangeListener(): OnFocusChangeListener? {
        return etText?.onFocusChangeListener
    }

    override fun setOnFocusChangeListener(l: OnFocusChangeListener?) {
        etText?.onFocusChangeListener = l
    }

    init {
        val mInflater =
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        val view = mInflater.inflate(R.layout.fort_view, this@FortView, true)
        etText = view.findViewById(R.id.etText)
        inputLayout = view.findViewById(R.id.inputLayout)
        ViewCompat.setBackgroundTintList(etText as View, ColorStateList.valueOf(Color.TRANSPARENT))

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FortView)
        textSize = typedArray.getDimension(R.styleable.FortView_textSize, convertDpToPixel(DEFAULT_SIZE,context))
        textColor = typedArray.getColor(R.styleable.FortView_textColor, DEFAULT_COLOR)


        hintTextColor = typedArray.getColor(R.styleable.FortView_hintTextColor, GRAY_HINT_COLOR)
        boxBackgroundColor = typedArray.getColor(R.styleable.FortView_boxBackgroundColor, DEFAULT_COLOR)
        boxBackgroundMode = typedArray.getInt(R.styleable.FortView_boxBackgroundShape, DEFAULT_BOX_SHAPE)
        errorTextAppearance = typedArray.getInt(R.styleable.FortView_errorTextAppearance, 0)
        errorTextColor = typedArray.getColor(R.styleable.FortView_errorTextColor, Color.RED)
        boxStrokeErrorColor =
            typedArray.getColor(R.styleable.FortView_boxStrokeErrorColor, Color.RED)

        when (fortViewType) {
            CARD_NUMBER -> {
                hint = getValueFromStyle(
                    typedArray,
                    R.styleable.FortView_hintText,
                    R.string.pf_card_number_hint
                )

            }
            CARD_EXPIRY -> {
                hint = getValueFromStyle(
                    typedArray,
                    R.styleable.FortView_hintText,
                    R.string.pf_expiry_date_hint
                )

            }
            CARD_HOLDER_NAME -> {
                hint = getValueFromStyle(
                    typedArray,
                    R.styleable.FortView_hintText,
                    R.string.pf_cardholder_name_hint
                )
            }
            CARD_CVV -> {
                hint = getValueFromStyle(
                    typedArray,
                    R.styleable.FortView_hintText,
                    R.string.pf_cvv_hint
                )
            }
        }

        typedArray.recycle()

    }

    private fun getValueFromStyle(a: TypedArray, resourcesIndex: Int, defaultValue: Int): String? {
        return when {
            a.hasValue(resourcesIndex) -> a.getString(resourcesIndex)
            else -> resources.getString(a.getResourceId(resourcesIndex, defaultValue))
        }
    }

}
