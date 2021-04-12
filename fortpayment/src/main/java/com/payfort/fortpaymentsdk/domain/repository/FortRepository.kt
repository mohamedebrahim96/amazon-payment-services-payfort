package com.payfort.fortpaymentsdk.domain.repository

import com.payfort.fortpaymentsdk.domain.model.SdkRequest
import com.payfort.fortpaymentsdk.domain.model.SdkResponse
import io.reactivex.Observable

interface FortRepository {

    fun validateData(body: SdkRequest): Observable<SdkResponse>
    fun validateCardNumber(body: SdkRequest): Observable<String>
    fun processData(body: SdkRequest): Observable<SdkResponse>
    fun logData(body: SdkRequest): Observable<SdkResponse>


}
