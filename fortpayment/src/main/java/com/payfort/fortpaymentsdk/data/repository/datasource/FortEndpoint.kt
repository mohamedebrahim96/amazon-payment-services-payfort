package com.payfort.fortpaymentsdk.data.repository.datasource

import com.payfort.fortpaymentsdk.constants.Constants
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FortEndpoint {


  @POST(Constants.FORT_URI.VALIDATE_URL)
  fun validateData(@Body bodyMap: String): Observable<String>



  @POST(Constants.FORT_URI.PROCESS_TNX_URL)
  fun processData(@Body bodyMap: String): Observable<String>

  @POST("/FortAPI/sdk/logging")
  fun logData(@Body bodyMap: String): Observable<String>





  @POST("/FortAPI/sdk/validate")
  fun getSslCertificate(): Observable<Response<String>>
}
