package com.payfort.fortpaymentsdk.domain.usecase

import com.payfort.fortpaymentsdk.domain.model.SdkRequest
import com.payfort.fortpaymentsdk.domain.model.StringResult
import com.payfort.fortpaymentsdk.domain.repository.FortRepository
import io.reactivex.Observable

/**
 * ValidateCardNumberUseCase is responsible for doing validate cardNumber operation then
 * map convert result to Result Sealed Class
 * @property fortRepository FortRepository
 * @constructor
 */
class ValidateCardNumberUseCase  constructor(private val fortRepository: FortRepository) :UseCase<SdkRequest,Observable<StringResult>> {

  override fun execute(request: SdkRequest): Observable<StringResult> {
    return fortRepository.validateCardNumber(request)
        .map { StringResult.Success(it) as StringResult }
        .onErrorReturn { StringResult.Failure(it) }
        .startWith(StringResult.Loading)
  }
}
