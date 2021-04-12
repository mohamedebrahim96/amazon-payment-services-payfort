package com.payfort.fortpaymentsdk.data.repository.datasource

import com.payfort.fortpaymentsdk.domain.model.MerchantToken
import com.payfort.fortpaymentsdk.domain.model.SdkRequest
import com.payfort.fortpaymentsdk.domain.model.SdkResponse
import io.reactivex.Observable

class FortDataSourceImplMock(val endpoint: FortEndpoint) : FortDataSource {
    override fun validate(request: SdkRequest): Observable<SdkResponse> {
        return endpoint.validateData("").map { sdkResponse() }
    }

    override fun validateCardNumber(request: SdkRequest): Observable<String> {
        return endpoint.validateData("")
    }

    override fun pay(body: SdkRequest): Observable<SdkResponse> {
        return endpoint.processData("").map { sdkResponse() }
    }

    private fun sdkResponse(): SdkResponse {
        val sdkResponse = SdkResponse()
        sdkResponse.isSuccess = true
        val merchantToken = MerchantToken()
        merchantToken.isRememberMe = false
        merchantToken.paymentOptionName = "VISA"
        sdkResponse.merchantToken = merchantToken
        return sdkResponse
    }
}