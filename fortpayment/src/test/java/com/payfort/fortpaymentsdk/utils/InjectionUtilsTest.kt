package com.payfort.fortpaymentsdk.utils

import com.payfort.fortpaymentsdk.FortSdk
import org.junit.Before
import org.junit.Test

class InjectionUtilsTest {

    @Before
    fun setUp() {

    }

    @Test
    fun provideValidateViewModel() {
        val viewModel = InjectionUtils.provideValidateViewModel(FortSdk.ENVIRONMENT.TEST)
        assert(viewModel!=null)
    }

    @Test
    fun provideValidateViewModelFactory() {
        val viewModel = InjectionUtils.provideValidateViewModelFactory(FortSdk.ENVIRONMENT.TEST)
        assert(viewModel!=null)
    }

    @Test
    fun provideFortRepository() {
        val provideFortRepository = InjectionUtils.provideFortRepository(FortSdk.ENVIRONMENT.TEST)
        assert(provideFortRepository!=null)
    }
}