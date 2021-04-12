package com.payfort.fortapisimulator.service

import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface FortAPIs {

    @POST("paymentApi")
    fun getToken(@HeaderMap map: Map<String,String>, @Body bodyMap: Map<String,String?>): Observable<JsonObject>


}