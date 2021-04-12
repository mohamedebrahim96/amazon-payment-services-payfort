package com.payfort.fortpaymentsdk.domain.usecase

import com.payfort.fortpaymentsdk.domain.model.SdkRequest
import com.payfort.fortpaymentsdk.domain.model.StringResult
import com.payfort.fortpaymentsdk.domain.repository.FortRepository
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class ValidateCardNumberUseCaseTest : TestCase() {

    @Mock
    lateinit var fortRepository: FortRepository
    lateinit var useCase: ValidateCardNumberUseCase
    private val sdkRequest = SdkRequest()

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setInitIoSchedulerHandler { Schedulers.trampoline() }
        MockitoAnnotations.initMocks(this)
        useCase = ValidateCardNumberUseCase(fortRepository)
    }

    @Test
    fun validateData_thenPositiveResponse() {
        val expectedResult = "VISA"

        sdkRequest.deviceId = "123"
        sdkRequest.deviceOS = "TestOs"
        // Mock API response
        Mockito.`when`(this.fortRepository.validateCardNumber(sdkRequest)).thenAnswer {
            return@thenAnswer Observable.just("VISA")
        }
        val test = useCase.execute(sdkRequest).test()
        checkLoadingResult(test)

        val successResult = test.values()[1] as StringResult.Success
        assert(successResult.response ==expectedResult)
    }

    @Test
    fun validateData_thenError() {
        val message = "Error Bad"
        val exception = RuntimeException(message)
        Mockito.`when`(fortRepository.validateCardNumber(sdkRequest)).thenReturn(Observable.error(exception))
        val test = useCase.execute(sdkRequest).test()

        val error = StringResult.Failure(exception)
        checkLoadingResult(test)
        val failureResult = test.values()[1]
        assert(failureResult == error)
        assert((failureResult as StringResult.Failure).throwable.message == message)
    }
    private fun checkLoadingResult(test: TestObserver<StringResult>) {
        val loadingResult = test.values()[0]
        assert(loadingResult == StringResult.Loading)
    }
}