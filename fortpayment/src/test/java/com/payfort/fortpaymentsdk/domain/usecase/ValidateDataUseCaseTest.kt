package com.payfort.fortpaymentsdk.domain.usecase

import com.payfort.fortpaymentsdk.domain.model.Result
import com.payfort.fortpaymentsdk.domain.model.SdkRequest
import com.payfort.fortpaymentsdk.domain.model.SdkResponse
import com.payfort.fortpaymentsdk.domain.repository.FortRepository
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class ValidateDataUseCaseTest {


    @Mock
    lateinit var fortRepository: FortRepository

    lateinit var validateDataUseCase: ValidateDataUseCase
    val sdkRequest = SdkRequest()

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setInitIoSchedulerHandler { Schedulers.trampoline() }

        MockitoAnnotations.initMocks(this)
        validateDataUseCase = ValidateDataUseCase(fortRepository)
    }

    @Test
    fun validateData_thenPositiveResponse() {
        val googleLink = "www.google.com"
        val sdkResponse = SdkResponse()
        sdkResponse.isSuccess=true
        sdkResponse.checker3DsURL= googleLink

        sdkRequest.deviceId = "123"
        sdkRequest.deviceOS = "TestOs"
        // Mock API response
        Mockito.`when`(this.fortRepository.validateData(sdkRequest)).thenAnswer {
            return@thenAnswer Observable.just(sdkResponse)
        }
        val test = validateDataUseCase.execute(sdkRequest).test()
        checkLoadingResult(test)

        val successResult = test.values()[1] as Result.Success
        assert(successResult.response.checker3DsURL ==googleLink)

    }

    @Test
    fun validateData_thenError() {
        val message = "bad"
        val exception = RuntimeException(message)
        Mockito.`when`(fortRepository.validateData(sdkRequest)).thenReturn(Observable.error(exception))
        val test = validateDataUseCase.execute(sdkRequest).test()

        val error = Result.Failure(exception)
        checkLoadingResult(test)
        val failureResult = test.values()[1]
        assert(failureResult == error)
        assert((failureResult as Result.Failure).throwable.message == message)

    }

    private fun checkLoadingResult(test: TestObserver<Result>) {
        val loadingResult = test.values()[0]
        assert(loadingResult == Result.Loading)
    }
}