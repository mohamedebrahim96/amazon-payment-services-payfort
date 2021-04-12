package com.payfort.fortpaymentsdk.domain.usecase

import com.payfort.fortpaymentsdk.data.repository.FortRepositoryImpl
import com.payfort.fortpaymentsdk.data.repository.datasource.FortDataSource
import com.payfort.fortpaymentsdk.domain.model.MerchantToken
import com.payfort.fortpaymentsdk.domain.model.Result
import com.payfort.fortpaymentsdk.domain.model.SdkRequest
import com.payfort.fortpaymentsdk.domain.model.SdkResponse
import com.payfort.fortpaymentsdk.domain.repository.FortRepository
import com.payfort.fortpaymentsdk.exceptions.FortException
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import javax.sql.DataSource

class PayUseCaseTest {

    @Mock
    internal lateinit var dataSource: FortDataSource

    internal lateinit var fortRepository: FortRepositoryImpl

    lateinit var payUseCase: PayUseCase
    val sdkRequest = SdkRequest()

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setInitIoSchedulerHandler { Schedulers.trampoline() }
        MockitoAnnotations.initMocks(this)
        fortRepository=FortRepositoryImpl(dataSource)
        payUseCase = PayUseCase(fortRepository)
    }

    @Test
    fun payThenPositiveResponse() {
        val checker3DsURL = "www.google.com"
        val sdkResponse = SdkResponse()
        sdkResponse.isSuccess=true
        sdkResponse.checker3DsURL= checker3DsURL


        sdkRequest.deviceId = "123"
        sdkRequest.deviceOS = "TestOs"
        // Mock API response
        Mockito.`when`(this.fortRepository.processData(sdkRequest)).thenAnswer {
            return@thenAnswer Observable.just(sdkResponse)
        }
        val test = payUseCase.execute(sdkRequest).test()
        checkLoadingResult(test)

        val successResult = test.values()[1] as Result.Success
        assert(successResult.response.checker3DsURL ==checker3DsURL)

    }

    @Test
    fun payThenPositiveResponse_isSuccess_true() {
        val sdkResponse = SdkResponse()
        sdkResponse.isSuccess=true

        Mockito.`when`(this.fortRepository.processData(sdkRequest)).thenAnswer {
            return@thenAnswer Observable.just(sdkResponse)
        }
        val test = payUseCase.execute(sdkRequest).test()
        checkLoadingResult(test)

        val successResult = test.values()[1] as Result.Success
        assert(successResult.response.isSuccess ==true)

    }

    @Test
    fun payThenPositiveResponse_isSuccess_false() {
        val sdkResponse = SdkResponse()
        sdkResponse.isSuccess=false

        Mockito.`when`(this.fortRepository.processData(sdkRequest)).thenAnswer {
            return@thenAnswer Observable.just(sdkResponse)
        }
        val test = payUseCase.execute(sdkRequest).test()
        checkLoadingResult(test)

        val successResult = test.values()[1] as Result.Success
        assert(successResult.response.isSuccess ==false)

    }

    @Test
    fun payThenPositiveResponseMerchantTokenHaveData() {
        val sdkResponse = SdkResponse()
        sdkResponse.isSuccess=true
        val merchantToken = MerchantToken()
        merchantToken.isRememberMe= false
        merchantToken.paymentOptionName="VISA"
        sdkResponse.merchantToken =merchantToken

        Mockito.`when`(this.fortRepository.processData(sdkRequest)).thenAnswer {
            return@thenAnswer Observable.just(sdkResponse)
        }
        val test = payUseCase.execute(sdkRequest).test()
        checkLoadingResult(test)

        val successResult = test.values()[1] as Result.Success
        assert(successResult.response.isSuccess)
        assert(!successResult.response.merchantToken.isRememberMe)
    }

    @Test
    fun pay_thenError() {
        val message = "bad"
        val exception = FortException(message)
        Mockito.`when`(fortRepository.processData(sdkRequest)).thenReturn(Observable.error(exception))
        val test = payUseCase.execute(sdkRequest).test()

        val error = Result.Failure(exception)
        checkLoadingResult(test)
        val failureResult = test.values()[1] as Result.Failure
        assert(failureResult == error)
        assert(failureResult.throwable.message == message)

    }

    private fun checkLoadingResult(test: TestObserver<Result>) {
        val loadingResult = test.values()[0]
        assert(loadingResult == Result.Loading)
    }
}