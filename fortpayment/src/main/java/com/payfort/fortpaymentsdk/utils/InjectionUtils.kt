package com.payfort.fortpaymentsdk.utils

import com.payfort.fortpaymentsdk.data.network.RetrofitClient
import com.payfort.fortpaymentsdk.data.repository.FortRepositoryImpl
import com.payfort.fortpaymentsdk.data.repository.datasource.FortDataSource
import com.payfort.fortpaymentsdk.data.repository.datasource.FortDataSourceImpl
import com.payfort.fortpaymentsdk.domain.repository.FortRepository
import com.payfort.fortpaymentsdk.domain.usecase.ValidateDataUseCase
import com.payfort.fortpaymentsdk.presentation.init.ValidateViewModel
import com.payfort.fortpaymentsdk.presentation.init.ValidateViewModelFactory
import com.payfort.fortpaymentsdk.worker.SendLogsWorker

/**
 * Class to provide all layers required to do payment,validate operations
 */
internal object InjectionUtils {

    @JvmStatic
    fun provideValidateViewModel(environment: String): ValidateViewModel {
        return ValidateViewModel(ValidateDataUseCase(provideFortRepository(environment)))
    }

    @JvmStatic
    fun provideValidateViewModelFactory(environment: String): ValidateViewModelFactory {
        return ValidateViewModelFactory(ValidateDataUseCase(provideFortRepository(environment)))
    }

    fun provideFortRepository(environment: String): FortRepository {
        return FortRepositoryImpl(provideFortApi(environment))
    }

    private fun provideFortApi(environment: String): FortDataSource {
        SendLogsWorker.setEnvironment(environment)
        return FortDataSourceImpl(RetrofitClient.getInstance(environment))
    }

}