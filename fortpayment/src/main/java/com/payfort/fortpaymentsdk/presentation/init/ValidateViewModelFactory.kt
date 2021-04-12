package com.payfort.fortpaymentsdk.presentation.init

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.payfort.fortpaymentsdk.domain.usecase.ValidateDataUseCase

internal class ValidateViewModelFactory(private val validateDataUseCase:ValidateDataUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ValidateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ValidateViewModel(validateDataUseCase) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}