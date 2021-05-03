package com.payfort.fortpaymentsdk.presentation.init

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.payfort.fortpaymentsdk.callbacks.PayFortCallback
import com.payfort.fortpaymentsdk.domain.model.FortRequest
import com.payfort.fortpaymentsdk.domain.model.Result
import com.payfort.fortpaymentsdk.domain.model.SdkRequest
import com.payfort.fortpaymentsdk.domain.usecase.UseCase
import com.payfort.fortpaymentsdk.handlers.CreatorHandler
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

internal class ValidateViewModel(private val validateDataUseCase: UseCase<SdkRequest, Observable<Result>>) :
    ViewModel() {
    private var _result = MutableLiveData<Result>()
    val result: LiveData<Result>
        get() = _result

    fun validateRequest(context: Context, fortRequest: FortRequest) {

        val sdkRequest = CreatorHandler.createSdkRequest(context, fortRequest)

        validateDataUseCase.execute(sdkRequest).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                    _result.value = it
                },{
                    _result.value = Result.Failure(it)
                })
    }


    fun validateRequest(context: Context, fortRequest: FortRequest, callback: PayFortCallback?) {
        val sdkRequest = CreatorHandler.createSdkRequest(context, fortRequest)
        validateDataUseCase.execute(sdkRequest).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                when (it) {
                    Result.Loading -> callback?.startLoading()
                    is Result.Success -> {
                        if (it.response.isSuccess)
                            callback?.onSuccess(fortRequest.requestMap, it.response.responseMap)
                        else
                            callback?.onFailure(fortRequest.requestMap, it.response.responseMap)
                    }
                    is Result.Failure -> {
                        var sdkResponse = CreatorHandler.convertThrowableToSdkResponse(context, it.throwable, fortRequest)
                        callback?.onFailure(fortRequest.requestMap, sdkResponse.responseMap)
                    }
                }
            },{
                var sdkResponse = CreatorHandler.convertThrowableToSdkResponse(context, it, fortRequest)
                callback?.onFailure(fortRequest.requestMap, sdkResponse.responseMap)
            })
    }
}