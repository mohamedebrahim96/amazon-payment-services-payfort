package com.payfort.fortpaymentsdk.handlers

import android.content.Context
import com.payfort.fortpaymentsdk.R
import com.payfort.fortpaymentsdk.constants.Constants
import com.payfort.fortpaymentsdk.domain.model.*
import com.payfort.fortpaymentsdk.exceptions.InvalidCvcLengthException
import com.payfort.fortpaymentsdk.exceptions.MerchantTokenNoExistException
import com.payfort.fortpaymentsdk.utils.MapUtils
import com.payfort.fortpaymentsdk.utils.Utils
import com.payfort.fortpaymentsdk.views.CardCvvView
import com.payfort.fortpaymentsdk.views.CardExpiryView
import com.payfort.fortpaymentsdk.views.CardHolderNameView
import com.payfort.fortpaymentsdk.views.FortCardNumberView
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * CreatorHandler responsible for Creating
 * Sdk Request from different type of Parameters
 * this is the only entry to Create SDK Request
 */
internal object CreatorHandler {
    /**
     *
     * @param context Context
     * @param fortRequest FortRequest?
     * @return SdkRequest
     */
    fun createSdkRequest(context: Context, fortRequest: FortRequest?): SdkRequest {
        val sdkRequest = SdkRequest()
        sdkRequest.deviceOS = Utils.getOsDetails(context)
        sdkRequest.deviceId = Utils.getDeviceId(context)
        sdkRequest.requestMap = fortRequest?.requestMap
        return sdkRequest
    }

    /**
     *
     * @param context Context
     * @param fortRequest FortRequest?
     * @return SdkRequest
     */
    fun createSdkRequestForWorker(context: Context, fortRequest: FortRequest?): SdkRequest {
        var sdkRequest = SdkRequest()
        sdkRequest.deviceOS = Utils.getOsDetails(context)
        sdkRequest.deviceId = Utils.getDeviceId(context)
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap[Constants.FORT_PARAMS.SDK_TOKEN] = fortRequest?.requestMap?.get(Constants.FORT_PARAMS.SDK_TOKEN)
        sdkRequest.requestMap = hashMap
        return sdkRequest
    }

    /**
     *
     * @param context Context
     * @param sdkToken String?
     * @param merchantToken MerchantToken?
     * @param cvc String?
     * @return SdkRequest
     */
    fun createSdkRequestFromMerchantToken(context: Context,
                                          sdkToken: String? = null,
                                          merchantToken: MerchantToken? = null,
                                          cvc: String? = null): SdkRequest {
        val sdkRequest = SdkRequest()
        val expDate: String = merchantToken?.expDate?.substring(2) + merchantToken?.expDate?.substring(0, 2).toString()
        sdkRequest.deviceId = Utils.getDeviceId(context)
        sdkRequest.deviceOS = Utils.getOsDetails(context)
        val payParamsMap: HashMap<String, Any?> = HashMap()
        payParamsMap[Constants.FORT_PARAMS.CARD_NUMBER] = merchantToken?.maskedCardNumber
        payParamsMap[Constants.FORT_PARAMS.CARD_SECURITY_CODE] = cvc
        payParamsMap[Constants.FORT_PARAMS.EXPIRY_DATE] = expDate
        payParamsMap[Constants.FORT_PARAMS.SDK_TOKEN] = sdkToken
        sdkRequest.requestMap = payParamsMap
        return sdkRequest
    }


    /**
     *
     * @param context Context
     * @param cardNumberView FortCardNumberView
     * @param cvvView CardCvvView
     * @param holderNameView CardHolderNameView
     * @param expiryView CardExpiryView
     * @param isRememberMe Boolean
     * @param fortRequest FortRequest?
     * @return SdkRequest
     */
    fun createSdkRequestFromComponents(context: Context,
                                       cardNumberView: FortCardNumberView,
                                       cvvView: CardCvvView,
                                       holderNameView: CardHolderNameView, expiryView: CardExpiryView, isRememberMe: Boolean, fortRequest: FortRequest?): SdkRequest {
        val cvv = cvvView.text.toString()
        val cardHolderNameView = holderNameView.text.toString()
        val cardNumber = cardNumberView.text.toString()
        var expiryDate = expiryView.text.toString()
        expiryDate = expiryDate.substring(3) + expiryDate.substring(0, 2)
        val isRememberMe = if (isRememberMe) Constants.INDICATORS.REMEMBER_ME_ON else Constants.INDICATORS.REMEMBER_ME_OFF
        val sdkToken = Utils.getParamValue(fortRequest?.requestMap, Constants.FORT_PARAMS.SDK_TOKEN).toString()

        val sdkRequest = SdkRequest()
        sdkRequest.deviceId = Utils.getDeviceId(context)
        sdkRequest.deviceOS = Utils.getOsDetails(context)
        val payParamsMap: HashMap<String, Any?> = HashMap()
        payParamsMap[Constants.FORT_PARAMS.CARD_NUMBER] = cardNumber
        payParamsMap[Constants.FORT_PARAMS.CARD_SECURITY_CODE] = cvv
        payParamsMap[Constants.FORT_PARAMS.EXPIRY_DATE] = expiryDate
        payParamsMap[Constants.FORT_PARAMS.CUSTOMER_NAME] = cardHolderNameView
        payParamsMap[Constants.FORT_PARAMS.SDK_TOKEN] = sdkToken
        payParamsMap[Constants.FORT_PARAMS.REMEMBER_ME] = isRememberMe
        sdkRequest.requestMap = payParamsMap
        return sdkRequest
    }

    /**
     *
     * @param context Context
     * @param throwable Throwable
     * @param fortRequest FortRequest?
     * @return SdkResponse
     */
    fun convertThrowableToSdkResponse(context: Context, throwable: Throwable, fortRequest: FortRequest?): SdkResponse {
        var sdkResponse: SdkResponse? = null
        when (throwable) {
            is IOException, is SocketTimeoutException -> {
                sdkResponse = MapUtils.getFailedToInitConnectionResponse(context.resources.getString(R.string.pf_no_connection), fortRequest?.requestMap)
                WorkerHandler.createWorkerRequest(context, fortRequest, ErrorEnum.CONNECTION, throwable)
            }
            is MerchantTokenNoExistException -> {
                sdkResponse = MapUtils.getTechnicalProblemResponse(context.resources.getString(R.string.pf_no_token_name), fortRequest?.requestMap)
                WorkerHandler.createWorkerRequest(context, fortRequest, ErrorEnum.INTERNAL, throwable)

            }

            is InvalidCvcLengthException -> {
                WorkerHandler.createWorkerRequest(context, fortRequest, ErrorEnum.INTERNAL, throwable)

                val cardBrand = throwable.cardBrand
                sdkResponse = if (cardBrand == CardBrand.AMEX)
                    MapUtils.getTechnicalProblemResponse(context.resources.getString(R.string.pf_cancel_cvv_amex_length), fortRequest?.requestMap)
                else
                    MapUtils.getTechnicalProblemResponse(context.resources.getString(R.string.pf_cancel_cvv_length), fortRequest?.requestMap)
            }

            is HttpException -> {
                WorkerHandler.createWorkerRequest(context, fortRequest, ErrorEnum.EXTERNAL, throwable)
            }

            else -> {
                sdkResponse = MapUtils.getTechnicalProblemResponse(context.resources.getString(R.string.pf_technical_problem), fortRequest?.requestMap)
                WorkerHandler.createWorkerRequest(context, fortRequest, ErrorEnum.EXTERNAL, throwable)

            }
        }

        return sdkResponse!!
    }


}