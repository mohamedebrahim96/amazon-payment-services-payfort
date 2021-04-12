package com.payfort.fortpaymentsdk.handlers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.payfort.fortpaymentsdk.R
import com.payfort.fortpaymentsdk.callbacks.FortPayInternalCallback
import com.payfort.fortpaymentsdk.callbacks.PayFortCallback
import com.payfort.fortpaymentsdk.domain.model.CardBrand
import com.payfort.fortpaymentsdk.domain.model.ErrorEnum
import com.payfort.fortpaymentsdk.domain.model.FortRequest
import com.payfort.fortpaymentsdk.domain.model.SdkResponse
import com.payfort.fortpaymentsdk.domain.usecase.PayUseCase
import com.payfort.fortpaymentsdk.domain.usecase.ValidateDataUseCase
import com.payfort.fortpaymentsdk.handlers.PayHandler.Companion.TAG
import com.payfort.fortpaymentsdk.presentation.threeds.ThreeDsActivity
import com.payfort.fortpaymentsdk.presentation.viewmodel.PayViewModel
import com.payfort.fortpaymentsdk.utils.CommonServiceUtil
import com.payfort.fortpaymentsdk.utils.InjectionUtils
import com.payfort.fortpaymentsdk.utils.MapUtils
import com.payfort.fortpaymentsdk.utils.Utils
import com.payfort.fortpaymentsdk.views.CardCvvView
import com.payfort.fortpaymentsdk.views.CardExpiryView
import com.payfort.fortpaymentsdk.views.CardHolderNameView
import com.payfort.fortpaymentsdk.views.FortCardNumberView
import com.payfort.fortpaymentsdk.views.model.PayComponents
import java.io.IOException

/**
 * This class responsible for creating a payment through  Custom Components or With Full Page
 *
 * @property appCompatButton AppCompatButton
 * @property TAG (kotlin.String..kotlin.String?)
 * @property context (android.content.Context..android.content.Context?)
 * @property resources (android.content.res.Resources..android.content.res.Resources?)
 * @property viewModel PayViewModel?
 * @property cardNumberView FortCardNumberView
 * @property cvvView CardCvvView
 * @property holderNameView CardHolderNameView
 * @property expiryView CardExpiryView
 * @property payFortCallback PayFortCallback?
 * @property fortInternalPayCallback FortPayInternalCallback?
 * @property request FortRequest
 * @property isRememberMe Boolean
 * @property withoutForm Boolean
 * @property isValidatedBefore Boolean
 * @constructor
 */
internal class PayHandler constructor(private val appCompatButton: AppCompatButton) {
    companion object {
        internal val THREEDS_INTENT_FILTER = "com.payfort.fortpaymentsdk.webview.threeds"
        private val TAG = PayHandler::class.java.simpleName

    }

    private val context = appCompatButton.context
    private val resources = appCompatButton.context.resources
    private var broadcastReceiver: BroadcastReceiver? = null

    private var viewModel: PayViewModel? = null
    private lateinit var cardNumberView: FortCardNumberView
    private lateinit var cvvView: CardCvvView
    private lateinit var holderNameView: CardHolderNameView
    private lateinit var expiryView: CardExpiryView

    private var payFortCallback: PayFortCallback? = null
    private var fortInternalPayCallback: FortPayInternalCallback? = null

    internal lateinit var request: FortRequest
    var isRememberMe = true
    var withoutForm = false
    internal var isValidatedBefore = false


    /**
     * this Setup used with DirectPay only
     * @param environment String
     * @param request FortRequest
     * @param payFortCallback PayFortCallback?
     */
    fun setUpPayButton(environment: String,
                       request: FortRequest,
                       payFortCallback: PayFortCallback?) {
        initViewModel(environment)
        this.request = request
        this.payFortCallback = payFortCallback
        withoutForm = true

    }

    /**
     * this Setup used with Pay with Full Components from outside SDK
     * @param environment String
     * @param request FortRequest
     * @param payComponents PayComponents
     * @param payFortCallback PayFortCallback?
     */
    fun setUpPayButton(environment: String, request: FortRequest,
                       payComponents: PayComponents,
                       payFortCallback: PayFortCallback?) {
        this.cardNumberView = payComponents.cardNumberView
        this.cvvView = payComponents.cvvView
        this.expiryView = payComponents.cardExpiryView
        this.holderNameView = payComponents.holderNameView
        initViewModel(environment)

        val paymentOption: String? = MapUtils.getPaymentOptionValue(request.requestMap)
        this.cardNumberView.setUp(environment, CardBrand.fromCodeOrNull(paymentOption))
        this.cvvView.setCardNumberView(this.cardNumberView)
        this.request = request
        this.payFortCallback = payFortCallback


    }

    /**
     * this Setup used with Pay with Full Components from Inside SDK (@CreditCardPaymentActivity.class)
     * @param environment String
     * @param request FortRequest
     * @param payComponents PayComponents
     * @param payFortCallback FortPayInternalCallback?
     */
    fun setUpPayButton(environment: String, request: FortRequest,
                       payComponents: PayComponents,
                       payFortCallback: FortPayInternalCallback?) {
        this.cardNumberView = payComponents.cardNumberView
        this.cvvView = payComponents.cvvView
        this.expiryView = payComponents.cardExpiryView
        this.holderNameView = payComponents.holderNameView
        initViewModel(environment)

        val paymentOption: String? = MapUtils.getPaymentOptionValue(request.requestMap)
        this.cardNumberView.setUp(environment, CardBrand.fromCodeOrNull(paymentOption))
        this.cvvView.setCardNumberView(this.cardNumberView)
        this.request = request
        this.fortInternalPayCallback = payFortCallback


    }

    /**
     * initialize ViewModel
     * @param environment String
     */
    private fun initViewModel(environment: String) {
        val validateDataUseCase = ValidateDataUseCase(InjectionUtils.provideFortRepository(environment))
        val payUseCase = PayUseCase(InjectionUtils.provideFortRepository(environment))
        viewModel = PayViewModel(validateDataUseCase, payUseCase)
    }

    /**
     * responsible for Detect if there is internet connection or Not
     * also Detect Payment Type if it's with fields or without
     */
    fun pay() {

        if (!Utils.haveNetworkConnection(context)) {
            val content = appCompatButton.rootView.findViewById<View>(android.R.id.content)
            CommonServiceUtil.displayConnectionAlert(content, context)
            return
        }

        if (withoutForm) {
            if (viewModel?.isFormWithoutFields(request) == true) {
                payWithoutFields()
            } else {
                payFortCallback?.onFailure(request.requestMap!!,
                    MapUtils.getTechnicalProblemResponse(
                        resources.getString(R.string.pf_technical_problem_missing_token_name_or_card_security),
                        request.requestMap).responseMap)
                WorkerHandler.createWorkerRequest(context,request,ErrorEnum.INTERNAL,Exception(resources.getString(R.string.pf_technical_problem_missing_token_name_or_card_security)))
            }

        } else
            normalPay()


    }

    /**
     * Pay With DirectPay (No Form Required)
     */
    private fun payWithoutFields() {
        viewModel?.validateAndPay(context, request, object : FortPayInternalCallback {
            override fun startLoading() {
                payFortCallback?.startLoading()
            }

            override fun onSuccess(sdkResponse: SdkResponse) {
                handleSuccessResponse(sdkResponse)

            }

            override fun onFailure(sdkResponse: SdkResponse) {
                payFortCallback?.onFailure(request.requestMap, sdkResponse.responseMap)
            }
        })
    }

    /**
     * Pay With Fields (Form Required)
     */
    private fun normalPay() {
        startValidationFields()
        if (isValid()) {
            val prepareSdkRequest = CreatorHandler.createSdkRequestFromComponents(
                context = context,
                cardNumberView = cardNumberView,
                cvvView = cvvView,
                holderNameView = holderNameView,
                expiryView = expiryView,
                isRememberMe = isRememberMe,
                fortRequest = request
            )
            viewModel?.validateAndPay(context, request, prepareSdkRequest, isValidatedBefore, object : FortPayInternalCallback {
                override fun startLoading() {
                    payFortCallback?.startLoading()
                    fortInternalPayCallback?.startLoading()
                }

                override fun onSuccess(sdkResponse: SdkResponse) {
                    handleSuccessResponse(sdkResponse)
                }

                override fun onFailure(sdkResponse: SdkResponse) {
                    payFortCallback?.onFailure(request.requestMap!!, sdkResponse.responseMap)
                    fortInternalPayCallback?.onFailure(sdkResponse)
                }
            })
        }
    }

    /**
     * handle response and if it's require to open 3DS Page
     * or Failure
     * @param sdkResponse SdkResponse
     */
    private fun handleSuccessResponse(sdkResponse: SdkResponse) {
        if (!sdkResponse.checker3DsURL.isNullOrEmpty()) {
            startInternalReceiverToGetResult()
            ThreeDsActivity.start(context, sdkResponse.checker3DsURL)
        } else {
            handleResponse(sdkResponse)
        }
    }


    /**
     * check if form is Valid
     * @return Boolean
     */
    private fun isValid() =
        (cvvView.isValid() &&
                cardNumberView.isValid() &&
                expiryView.isValid())


    /**
     * check if form Valid and start showing Error Messages
     */
    private fun startValidationFields() {
        cardNumberView.validateCardNumber()
        cvvView.validateCvc()
        expiryView.validateExpiryDate()
    }

    /**
     * Handle response from Internal Full Page
     * or outside Forms
     * @param sdkResponse SdkResponse
     */
    private fun handleResponse(sdkResponse: SdkResponse) {
        fortInternalPayCallback?.onSuccess(sdkResponse)
        if (sdkResponse.isSuccess) {
            payFortCallback?.onSuccess(request.requestMap, sdkResponse.responseMap)
        } else {
            payFortCallback?.onFailure(request.requestMap, sdkResponse.responseMap)
        }
    }

    private fun startInternalReceiverToGetResult() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == THREEDS_INTENT_FILTER && intent.getSerializableExtra(ThreeDsActivity.KEY_SUCCESS) != null) {
                    handleResponse(intent.getSerializableExtra(ThreeDsActivity.KEY_SUCCESS) as SdkResponse)

                } else
                    if (intent.action == THREEDS_INTENT_FILTER && intent.getBooleanExtra(ThreeDsActivity.KEY_FAILURE, false)) {
                        val sdkResponse = CreatorHandler.convertThrowableToSdkResponse(context, IOException(), request)
                        payFortCallback?.onFailure(request.requestMap, sdkResponse.responseMap)
                        fortInternalPayCallback?.onFailure(sdkResponse)
                    }

                removeInternalReceiver(context)
            }
        }
        context?.registerReceiver(broadcastReceiver, IntentFilter(THREEDS_INTENT_FILTER))
    }

    /**
     * To check if the Receiver is register or not
     */
    private fun removeInternalReceiver(context: Context?) {
        if (broadcastReceiver != null) {
            context?.unregisterReceiver(broadcastReceiver)
        }
    }


}