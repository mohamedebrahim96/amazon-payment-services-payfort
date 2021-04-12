package com.payfort.fortpaymentsdk.data.repository.datasource

import com.payfort.fortpaymentsdk.domain.model.SdkRequest
import com.payfort.fortpaymentsdk.domain.model.SdkResponse
import io.reactivex.Observable

/**
 * FortDataSource is responsible for responsible for fetching data from a given REST API.
 */
interface FortDataSource {

    fun validate(request: SdkRequest): Observable<SdkResponse>
    fun validateCardNumber(request: SdkRequest): Observable<String>
    fun pay(body: SdkRequest): Observable<SdkResponse>
    fun logData(body: SdkRequest): Observable<SdkResponse>

}