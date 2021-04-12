package com.payfort.fortpaymentsdk.data.repository.datasource

import com.payfort.fortpaymentsdk.domain.model.*
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class FortDataSourceTest {

    @Mock
    internal lateinit var fortEndpoint: FortEndpoint

    internal lateinit var fortDataSource: FortDataSource


    val sdkRequest = SdkRequest()

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setInitIoSchedulerHandler { Schedulers.trampoline() }
        MockitoAnnotations.initMocks(this)
        fortDataSource = FortDataSourceImplMock(fortEndpoint)
    }

    @Test
    fun payThenPositiveResponse() {
        val sdkResponse = sdkResponse()

        sdkRequest.deviceId = "123"
        sdkRequest.deviceOS = "TestOs"

        // Mock API response
        Mockito.`when`(this.fortEndpoint.processData("")).thenAnswer {
            return@thenAnswer Observable.just("responseBody")
        }

        val test = fortDataSource.pay(sdkRequest).test()
        test.assertValue { it.isSuccess == sdkResponse.isSuccess }
    }


    @Test
    fun validateData_thenPositiveResponse() {
        val expectedResult = "VISA"

        sdkRequest.deviceId = "123"
        sdkRequest.deviceOS = "TestOs"
        // Mock API response
        Mockito.`when`(this.fortEndpoint.validateData("")).thenAnswer {
            return@thenAnswer Observable.just("VISA")
        }

        val test = fortDataSource.validate(sdkRequest).test()
        test.assertValue { it.isSuccess == sdkResponse().isSuccess }
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

