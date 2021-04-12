package com.payfort.fortpaymentsdk.callbacks

import com.payfort.fortpaymentsdk.domain.model.SdkResponse

internal interface FortPayInternalCallback {
    fun startLoading()
    fun onSuccess(sdkResponse: SdkResponse)
    fun onFailure(sdkResponse: SdkResponse)
}