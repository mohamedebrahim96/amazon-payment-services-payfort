package com.payfort.fortpaymentsdk.presentation.viewmodel

import android.content.Context
import android.os.Build
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.payfort.fortpaymentsdk.callbacks.FortPayInternalCallback
import com.payfort.fortpaymentsdk.callbacks.PayFortCallback
import com.payfort.fortpaymentsdk.domain.model.FortRequest
import com.payfort.fortpaymentsdk.domain.model.Result
import com.payfort.fortpaymentsdk.domain.model.SdkRequest
import com.payfort.fortpaymentsdk.domain.model.SdkResponse
import com.payfort.fortpaymentsdk.domain.usecase.PayUseCase
import com.payfort.fortpaymentsdk.domain.usecase.UseCase
import com.payfort.fortpaymentsdk.domain.usecase.ValidateDataUseCase
import com.payfort.fortpaymentsdk.handlers.CreatorHandler
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException
import java.util.concurrent.Callable
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class PayViewModelTest {
    @Mock
    lateinit var validateDataUseCase: ValidateDataUseCase
    @Mock
    lateinit var payUseCase: PayUseCase

    private lateinit var payViewModel: PayViewModel


    @Mock
    internal lateinit var callback: FortPayInternalCallback

    private val context: Context = ApplicationProvider.getApplicationContext()


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        RxJavaPlugins.setComputationSchedulerHandler { scheduler: Scheduler? -> Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { scheduler: Scheduler? -> Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { scheduler: Scheduler? -> Schedulers.trampoline() }
        RxJavaPlugins.setSingleSchedulerHandler { scheduler: Scheduler? -> Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler: Callable<Scheduler?>? -> Schedulers.trampoline() }
        payViewModel = spy(PayViewModel(validateDataUseCase,payUseCase))
    }


    @Test
    fun provideEmptyHashMap_isFormWithoutFields_thenReturnFalse() {
        val fortRequest = FortRequest()
        fortRequest.requestMap = HashMap<String, Any>()
        assert(!payViewModel.isFormWithoutFields(fortRequest))
    }

    @Test
    fun provideValidHashMap_isFormWithoutFields_thenReturnTrue() {
        val fortRequest = FortRequest()
        var hashMap = HashMap<String, Any>()
        hashMap["card_security_code"]="123"
        hashMap["token_name"]="Saleh"
        fortRequest.requestMap = hashMap
        assert(payViewModel.isFormWithoutFields(fortRequest))
    }

    @Test
    fun givenFortRequest_validate_thenReturnStartLoading() {
        val fortRequest = FortRequest()
        fortRequest.requestMap = HashMap<String, Any>()

        Mockito.`when`(validateDataUseCase.execute(any())).thenAnswer {
            return@thenAnswer Observable.just(Result.Loading)
        }

        payViewModel.validateAndPay(context,fortRequest,callback)
        verify(callback).startLoading()
    }

    @Test
    fun givenFortRequest_validate_thenReturnSuccess() {
        var sdkResponse = SdkResponse()

        val fortRequest = FortRequest()
        val hashMap = HashMap<String, Any>()
        hashMap["card_security_code"]="123"
        hashMap["token_name"]="Saleh"
        fortRequest.requestMap = hashMap

        Mockito.`when`(validateDataUseCase.execute(any())).thenAnswer {
            return@thenAnswer Observable.just(Result.Success(sdkResponse))
        }

        Mockito.`when`(payUseCase.execute(any())).thenAnswer {
            return@thenAnswer Observable.just(Result.Success(sdkResponse))
        }


        payViewModel.validateAndPay(context,fortRequest,callback)
        verify(callback).onSuccess(sdkResponse)
    }


    @Test
    fun givenFortRequest_validate_thenReturnFailure() {
        var sdkResponse = SdkResponse()

        val fortRequest = FortRequest()
        val hashMap = HashMap<String, Any>()
        hashMap["card_security_code"]="123"
        hashMap["token_name"]="Saleh"
        fortRequest.requestMap = hashMap
        var throwable = IOException()
        Mockito.`when`(validateDataUseCase.execute(any())).thenAnswer {
            return@thenAnswer Observable.just(Result.Failure(throwable))
        }

        Mockito.`when`(payUseCase.execute(any())).thenAnswer {

            return@thenAnswer Observable.just(Result.Failure(throwable))
        }


        payViewModel.validateAndPay(context,fortRequest,callback)
        verify(callback).onFailure(any())
    }


}