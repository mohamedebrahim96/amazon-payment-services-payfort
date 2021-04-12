package com.payfort.fortpaymentsdk.domain.model

sealed class Result {
    object Loading : Result()
    data class Success(val response: SdkResponse) : Result()
    data class Failure(val throwable: Throwable) : Result()
}

sealed class StringResult {
    object Loading : StringResult()
    data class Success(val response: String) : StringResult()
    data class Failure(val throwable: Throwable) : StringResult()
}