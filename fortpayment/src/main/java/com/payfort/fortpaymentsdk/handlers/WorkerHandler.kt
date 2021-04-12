package com.payfort.fortpaymentsdk.handlers

import android.content.Context
import com.payfort.fortpaymentsdk.constants.Constants
import com.payfort.fortpaymentsdk.domain.model.ErrorEnum
import com.payfort.fortpaymentsdk.domain.model.ErrorEnum.*
import com.payfort.fortpaymentsdk.domain.model.FortRequest
import com.payfort.fortpaymentsdk.worker.SendLogsWorker

internal object WorkerHandler {


    /**
     *
     * @param context Context
     * @param sdkRequest SdkRequest
     * @param enum ErrorEnum
     * @param throwable Throwable
     */
    fun createWorkerRequest(context: Context, fortRequest: FortRequest?, enum: ErrorEnum, throwable: Throwable) {
        val sdkRequest = CreatorHandler.createSdkRequestForWorker(context, fortRequest)
        sdkRequest.requestMap[Constants.FORT_PARAMS.CLIENT_SIDE_ERROR] = enum.name
        sdkRequest.requestMap[Constants.FORT_PARAMS.CLIENT_SIDE_ERROR_DETAILS] = throwable.message
        when (enum) {
            CONNECTION -> sdkRequest.requestMap[Constants.FORT_PARAMS.CLIENT_SIDE_ERROR_DESCRIPTION] = throwable.stackTrace
            INTERNAL -> sdkRequest.requestMap[Constants.FORT_PARAMS.CLIENT_SIDE_ERROR_DESCRIPTION] = throwable.message
            EXTERNAL -> sdkRequest.requestMap[Constants.FORT_PARAMS.CLIENT_SIDE_ERROR_DESCRIPTION] = throwable.stackTrace
        }


        if(isFortRequestContainsSdkToken(fortRequest))
        SendLogsWorker.sendLog( context,sdkRequest)
    }

    /**
     *
     * @param fortRequest FortRequest?
     * @return Boolean
     */
    private fun isFortRequestContainsSdkToken(fortRequest: FortRequest?): Boolean {
        return fortRequest?.requestMap?.get(Constants.FORT_PARAMS.SDK_TOKEN) != null
    }


}