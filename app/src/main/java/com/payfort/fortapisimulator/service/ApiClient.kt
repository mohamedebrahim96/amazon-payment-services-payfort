package com.payfort.fortapisimulator.service

import com.google.gson.Gson
import com.payfort.fortapisimulator.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    private var PRODUCTION = "https://sbpaymentservices.payfort.com/FortAPI/"

    private var production = PRODUCTION

    private var httpClientBuilder: OkHttpClient.Builder =
        OkHttpClient.Builder().readTimeout(15, TimeUnit.SECONDS)


    init {
        httpClientBuilder.connectTimeout(15, TimeUnit.SECONDS)
        httpClientBuilder.writeTimeout(15, TimeUnit.SECONDS)
        initHttpLogging(HttpLoggingInterceptor.Level.BODY)
    }


    private fun initHttpLogging(level: HttpLoggingInterceptor.Level) {
        val logging = HttpLoggingInterceptor()
        logging.level = level
        if (BuildConfig.DEBUG)
            httpClientBuilder.addInterceptor(logging)
    }


    fun provideApiRxWithHeader(): FortAPIs {
        return Retrofit.Builder()
            .baseUrl(production)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClientBuilder.build())
            .build()
            .create(FortAPIs::class.java)
    }

}