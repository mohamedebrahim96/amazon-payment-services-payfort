package com.payfort.fortpaymentsdk.data.repository

import com.payfort.fortpaymentsdk.data.repository.datasource.FortDataSource
import com.payfort.fortpaymentsdk.domain.model.SdkRequest
import com.payfort.fortpaymentsdk.domain.model.SdkResponse
import com.payfort.fortpaymentsdk.domain.repository.FortRepository
import io.reactivex.Observable

/**
 * FortRepositoryImpl implements FortRepository
 *
 * FortRepository is responsible for retrieving the data info from the api layer
 */
internal class FortRepositoryImpl(private val dataSource: FortDataSource) : FortRepository {


    /**
     * responsible for validate sdkRequest
     * @param body SdkRequest
     * @return Observable<SdkResponse>
     */
    override fun validateData(body: SdkRequest): Observable<SdkResponse> {
        return dataSource.validate(body)
    }

    /**
     * responsible for validate Card Nnumber
     * @param body SdkRequest
     * @return Observable<SdkResponse>
     */
    override fun validateCardNumber(body: SdkRequest): Observable<String> {
        return dataSource.validateCardNumber(body)
    }
    /**
     * responsible for doing pay operation
     * @param body SdkRequest
     * @return Observable<SdkResponse>
     */
    override fun processData(body: SdkRequest): Observable<SdkResponse> {
        return dataSource.pay(body)
    }

    /**
     * responsible for log failures
     * @param body SdkRequest
     * @return Observable<SdkResponse>
     */
    override fun logData(body: SdkRequest): Observable<SdkResponse> {
        return dataSource.logData(body)
    }
}