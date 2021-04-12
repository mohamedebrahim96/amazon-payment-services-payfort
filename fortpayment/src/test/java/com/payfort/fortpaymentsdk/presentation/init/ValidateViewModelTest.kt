package com.payfort.fortpaymentsdk.presentation.init

import android.content.Context
import android.os.Build
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.payfort.fortpaymentsdk.callbacks.PayFortCallback
import com.payfort.fortpaymentsdk.domain.model.FortRequest
import com.payfort.fortpaymentsdk.domain.model.Result
import com.payfort.fortpaymentsdk.domain.model.SdkRequest
import com.payfort.fortpaymentsdk.domain.model.SdkResponse
import com.payfort.fortpaymentsdk.domain.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
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
class ValidateViewModelTest {


    @Mock
    lateinit var useCase: UseCase<SdkRequest, Observable<Result>>

    private lateinit var validateViewModel: ValidateViewModel


    @Mock
    lateinit var observer: Observer<Result>

    @Mock
    lateinit var callback: PayFortCallback

    private val context: Context = ApplicationProvider.getApplicationContext()


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        RxJavaPlugins.setComputationSchedulerHandler { scheduler: Scheduler? -> Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { scheduler: Scheduler? -> Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { scheduler: Scheduler? -> Schedulers.trampoline() }
        RxJavaPlugins.setSingleSchedulerHandler { scheduler: Scheduler? -> Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler: Callable<Scheduler?>? -> Schedulers.trampoline() }
        validateViewModel = spy(ValidateViewModel(useCase))
    }

    @Test
    fun `given loading state, when validate data called, then Loading State return true`() {

        Mockito.`when`(useCase.execute(any())).thenAnswer {
            return@thenAnswer Observable.just(Result.Loading)
        }

        val fortRequest = FortRequest()
        // Given
        validateViewModel.result.observeForever(observer)
        // When
        validateViewModel.validateRequest(context, fortRequest)
        verify(observer).onChanged(Result.Loading)

    }

    @Test
    fun `given success state, when validate data called, then success state return`() {
        val response = SdkResponse()
        response.isSuccess = true

        Mockito.`when`(useCase.execute(any())).thenAnswer {

            return@thenAnswer Observable.just(Result.Success(response))
        }

        val fortRequest = FortRequest()
        // Given
        validateViewModel.result.observeForever(observer)
        // When
        validateViewModel.validateRequest(context, fortRequest)
        verify(observer).onChanged(Result.Success(response))

    }

    @Test
    fun `given failure state, when validate data called, then failure state return`() {
        var ioException = IOException()

        Mockito.`when`(useCase.execute(any())).thenAnswer {

            return@thenAnswer Observable.just(Result.Failure(ioException))
        }

        val fortRequest = FortRequest()
        // Given
        validateViewModel.result.observeForever(observer)
        // When
        validateViewModel.validateRequest(context, fortRequest)
        verify(observer).onChanged(Result.Failure(ioException))

    }

    @Test
    fun `given success state, when validate request called, then callback success state return`() {
        val response = SdkResponse()
        response.responseMap = HashMap<String, Any>()
        response.isSuccess = true

        val fortRequest = FortRequest()
        fortRequest.requestMap = HashMap<String, Any>()


        Mockito.`when`(useCase.execute(any())).thenAnswer {
            callback.onSuccess(any(), any())
            return@thenAnswer Observable.just(Result.Success(response))
        }

        // When
        validateViewModel.validateRequest(context, fortRequest, callback)
        verify(callback).onSuccess(any(), any())

    }


    @Test
    fun `given failure state, when validate request called, then callback failure state return`() {

        val fortRequest = FortRequest()
        fortRequest.requestMap = HashMap<String, Any>()


        var ioException = IOException()


        Mockito.`when`(useCase.execute(any())).thenAnswer {
            callback.onSuccess(any(), any())
            return@thenAnswer Observable.just(Result.Failure(ioException))
        }

        // When
        validateViewModel.validateRequest(context, fortRequest, callback)
        verify(callback).onFailure(any(), any())

    }

}