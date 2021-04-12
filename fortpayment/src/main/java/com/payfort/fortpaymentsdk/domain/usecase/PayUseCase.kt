package com.payfort.fortpaymentsdk.domain.usecase

import com.payfort.fortpaymentsdk.domain.model.Result
import com.payfort.fortpaymentsdk.domain.model.SdkRequest
import com.payfort.fortpaymentsdk.domain.repository.FortRepository
import io.reactivex.Observable

/**
 * PayUseCase is responsible for doing pay operation then
 * map convert result to Result Sealed Class
 * @property fortRepository FortRepository
 * @constructor
 */
class PayUseCase  constructor(private val fortRepository: FortRepository):UseCase<SdkRequest,Observable<Result>> {

   override fun execute(request: SdkRequest): Observable<Result> {
    return fortRepository.processData(request)
        .map { Result.Success(it) as Result }
        .onErrorReturn { Result.Failure(it) }
        .startWith(Result.Loading)
  }
}
