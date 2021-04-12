package com.payfort.fortpaymentsdk.worker

import android.content.Context
import androidx.work.*
import com.google.gson.Gson
import com.payfort.fortpaymentsdk.constants.Constants
import com.payfort.fortpaymentsdk.domain.model.SdkRequest
import com.payfort.fortpaymentsdk.utils.InjectionUtils
import com.payfort.fortpaymentsdk.utils.Utils
import io.reactivex.Single
import okio.IOException
import java.net.UnknownHostException


internal class SendLogsWorker(val appContext: Context, workerParams: WorkerParameters)
    : RxWorker(appContext, workerParams) {

    companion object {
        private var environment: String = ""
        private val gson = Gson()

        fun setEnvironment(environment: String) {
            this.environment = environment
        }

        fun sendLog(context: Context, sdkRequest: SdkRequest) {
            val hashMap = HashMap<String, Any>()
            hashMap[Constants.FORT_PARAMS.SDK_REQUEST] = gson.toJson(sdkRequest)

            val data: Data = Data.Builder()
                .putAll(hashMap)
                .build()

            // Create a Constraints that defines when the task should run
            val constraints: Constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
            if (environment.isNotEmpty()) {
                Utils.saveLatestEnvironment(context, environment)
            }

            val sendLogWork = OneTimeWorkRequest.Builder(SendLogsWorker::class.java).setConstraints(constraints).setInputData(data).build()
            WorkManager.getInstance(context).enqueue(sendLogWork)

        }
    }


    private fun getSdkRequest(): SdkRequest {
        return gson.fromJson(inputData.getString(Constants.FORT_PARAMS.SDK_REQUEST), SdkRequest::class.java)
    }

    override fun createWork(): Single<Result> {
        if(environment.isEmpty())
            environment=Utils.getLatestEnvironment(appContext)
        val provideFortRepository = InjectionUtils.provideFortRepository(environment)
        return Single.fromObservable(provideFortRepository.logData(getSdkRequest()).doOnError {
            if (it is IOException && it is UnknownHostException)
                Result.retry()
            else
                Result.failure()
        }.map { Result.success() })

    }
}