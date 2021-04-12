package com.payfort.fortpaymentsdk.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.payfort.fortpaymentsdk.callbacks.FortPayInternalCallback
import com.payfort.fortpaymentsdk.callbacks.PayFortCallback
import com.payfort.fortpaymentsdk.domain.model.FortRequest
import com.payfort.fortpaymentsdk.handlers.PayHandler
import com.payfort.fortpaymentsdk.views.model.PayComponents


class PayfortPayButton @kotlin.jvm.JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatButton(context, attrs, 0), View.OnClickListener {

    private val payHandler = PayHandler(this)


    internal fun isValidatedBefore(flag: Boolean) {
        payHandler.isValidatedBefore = flag
    }

    /**
     * Responsible for Save token or not
     * @param isRememberMe Boolean
     */
    fun isRememberMeEnabled(isRememberMe: Boolean) {
        payHandler.isRememberMe = isRememberMe
    }

    /**
     * Update Request After doing Setup
     * @param fortRequest FortRequest
     */
    fun updateRequest(fortRequest: FortRequest) {
        payHandler.request = fortRequest
    }


    /**
     * this Setup used with Pay with Full Components from outside SDK
     * @param environment String
     * @param request FortRequest
     * @param payComponents PayComponents
     * @param payFortCallback PayFortCallback
     */
    fun setup(environment: String,
              request: FortRequest,
              payComponents: PayComponents,
              payFortCallback: PayFortCallback) {
        payHandler.setUpPayButton(
            environment,
            request,
            payComponents,
            payFortCallback)
    }

    /**
     * this Setup used with DirectPay only
     * @param environment String
     * @param request FortRequest
     * @param payFortCallback PayFortCallback?
     */
    fun setup(environment: String,
              request: FortRequest,
              payFortCallback: PayFortCallback?) {
        payHandler.setUpPayButton(environment, request, payFortCallback)
    }


    internal fun setUpInternalButton(environment: String,
                                     request: FortRequest,
                                     payComponents: PayComponents, fortPayCallback: FortPayInternalCallback?) {
        payHandler.setUpPayButton(environment, request, payComponents, fortPayCallback)
    }

    init {
        setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        payHandler.pay()
    }


}