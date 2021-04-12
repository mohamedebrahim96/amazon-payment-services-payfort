package com.payfort.fortpaymentsdk.domain.usecase

interface UseCase<T, Result> {
    fun execute(t: T): Result
}