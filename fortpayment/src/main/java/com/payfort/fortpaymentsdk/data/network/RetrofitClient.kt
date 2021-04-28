package com.payfort.fortpaymentsdk.data.network

import com.payfort.fortpaymentsdk.BuildConfig
import com.payfort.fortpaymentsdk.data.repository.datasource.FortEndpoint
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * RetrofitClient is the adapter bridge Configuration between apis and SDK
 */
object RetrofitClient {
    private const val CONNECT_TIMEOUT_SECONDS: Long = 30000
    private var instance: FortEndpoint? = null


    /**
     * getInstance of @FortEndpoint.class
     * @param baseUrl String
     * @return FortEndpoint
     */
    fun getInstance(baseUrl: String): FortEndpoint {
            val httpClient = OkHttpClient.Builder()
                .writeTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build()


            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                . addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            instance = retrofit.create(FortEndpoint::class.java)

        return instance!!
    }


}