package com.payfort.fortpaymentsdk.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.payfort.fortpaymentsdk.callbacks.FortPayInternalCallback
import com.payfort.fortpaymentsdk.constants.Constants
import com.payfort.fortpaymentsdk.domain.model.CardBrand
import com.payfort.fortpaymentsdk.domain.model.FortRequest
import com.payfort.fortpaymentsdk.domain.model.Result
import com.payfort.fortpaymentsdk.domain.model.SdkRequest
import com.payfort.fortpaymentsdk.domain.usecase.PayUseCase
import com.payfort.fortpaymentsdk.domain.usecase.ValidateDataUseCase
import com.payfort.fortpaymentsdk.exceptions.InvalidCvcLengthException
import com.payfort.fortpaymentsdk.exceptions.MerchantTokenNoExistException
import com.payfort.fortpaymentsdk.handlers.CreatorHandler
import com.payfort.fortpaymentsdk.utils.Utils
import com.payfort.fortpaymentsdk.utils.ValidationUtils.isDigitsOnlyKotlin
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

internal class PayViewModel(private val validateDataUseCase: ValidateDataUseCase, private val payUseCase: PayUseCase) :
    ViewModel() {

    companion object {
        private const val KEY_CARD_SECURITY_CODE = "card_security_code"
        private const val KEY_TOKEN_NAME = "token_name"

    }


    fun isFormWithoutFields(fortRequest: FortRequest?): Boolean {
        return fortRequest?.requestMap?.containsKey(KEY_CARD_SECURITY_CODE) == true && fortRequest.requestMap?.containsKey(KEY_TOKEN_NAME)!!
    }

    fun validateAndPay(context: Context,
                       fortRequest: FortRequest?,
                       prepareSdkRequest: SdkRequest,
                       isValidatedBefore: Boolean = false, fortPayCallback: FortPayInternalCallback?) {

        val sdkRequest = CreatorHandler.createSdkRequest(context, fortRequest)

        val function: (t: Result) -> ObservableSource<out Result> = {
            when (it) {
                is Result.Success -> if (it.response.isSuccess) payUseCase.execute(prepareSdkRequest) else Observable.just(it)
                else -> Observable.just(it)
            }

        }
        val payObservable: Observable<Result> =
            if (isValidatedBefore)
                payUseCase.execute(prepareSdkRequest)
            else
                validateDataUseCase.execute(sdkRequest).flatMap(function)

        payObservable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                handleResult(it, fortPayCallback, context, fortRequest)

            },{
                val sdkResponse = CreatorHandler.convertThrowableToSdkResponse(context, it, fortRequest)
                fortPayCallback?.onFailure(sdkResponse)
            })
    }

    fun validateAndPay(context: Context,
                       fortRequest: FortRequest, fortPayCallback: FortPayInternalCallback?) {

        val cvc = fortRequest.requestMap?.get(KEY_CARD_SECURITY_CODE)?.toString()
        if (isFormWithoutFields(fortRequest)) {
            fortRequest.requestMap.remove(KEY_CARD_SECURITY_CODE)
        }
        val sdkRequest = CreatorHandler.createSdkRequest(context, fortRequest)
        val sdkToken = Utils.getParamValue(fortRequest.requestMap, Constants.FORT_PARAMS.SDK_TOKEN).toString()

        val function: (Result) -> Observable<Result>? = {
            when (it) {
                is Result.Success -> if (it.response.isSuccess) {
                    val merchantToken = it.response.merchantToken
                    if (merchantToken.maskedCardNumber != null) {
                        val cardBrand = CardBrand.fromCodeOrNull(merchantToken.paymentOptionName)
                        var execute: Observable<Result>? = null
                        cvc?.let {
                            execute = if (it.isDigitsOnlyKotlin() && cardBrand?.defaultCvcLength == it.toString().length) {
                                payUseCase.execute(CreatorHandler.createSdkRequestFromMerchantToken(context, sdkToken, merchantToken, cvc))

                            } else
                                Observable.just(Result.Failure(InvalidCvcLengthException(cardBrand)) as Result)
                        }

                        execute
                    } else
                        Observable.just(Result.Failure(MerchantTokenNoExistException()) as Result)
                } else {
                    Observable.just(it)
                }

                else -> Observable.just(it)
            }

        }

        validateDataUseCase.execute(sdkRequest).flatMap(function).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe ({

                if (it is Result.Failure || it is Result.Success) {
                    fortRequest.requestMap[KEY_CARD_SECURITY_CODE] = cvc
                }
                handleResult(it, fortPayCallback, context, fortRequest)

            },{
                val sdkResponse = CreatorHandler.convertThrowableToSdkResponse(context, it, fortRequest)
                fortPayCallback?.onFailure(sdkResponse)
            })

    }

    private fun handleResult(it: Result?, fortPayCallback: FortPayInternalCallback?, context: Context, fortRequest: FortRequest?) {
        when (it) {
            is Result.Success -> fortPayCallback?.onSuccess(it.response)
            is Result.Failure -> {
                val sdkResponse = CreatorHandler.convertThrowableToSdkResponse(context, it.throwable, fortRequest)
                fortPayCallback?.onFailure(sdkResponse)
            }
            Result.Loading -> fortPayCallback?.startLoading()

        }
    }


}