package com.payfort.fortpaymentsdk

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.payfort.fortpaymentsdk.callbacks.FortCallBackManager
import com.payfort.fortpaymentsdk.callbacks.FortCallback
import com.payfort.fortpaymentsdk.callbacks.FortInterfaces
import com.payfort.fortpaymentsdk.callbacks.PayFortCallback
import com.payfort.fortpaymentsdk.constants.Constants
import com.payfort.fortpaymentsdk.domain.model.FortRequest
import com.payfort.fortpaymentsdk.domain.model.FortSdkCache
import com.payfort.fortpaymentsdk.domain.model.SdkResponse
import com.payfort.fortpaymentsdk.presentation.init.InitSecureConnectionActivity
import com.payfort.fortpaymentsdk.security.DataSecurityService
import com.payfort.fortpaymentsdk.utils.MapUtils
import com.payfort.fortpaymentsdk.utils.InjectionUtils
import com.payfort.fortpaymentsdk.utils.LocalizationService
import com.payfort.fortpaymentsdk.utils.Utils

class FortSdk private constructor() {

    object ENVIRONMENT {
        const val TEST: String = Constants.ENVIRONMENT_SANDBOX
        const val PRODUCTION: String = Constants.ENVIRONMENT_PRODUCTION
    }

    companion object {
        @Volatile
        private var instance: FortSdk? = null
        @JvmStatic
        fun getInstance(): FortSdk {
            return when {
                instance != null -> instance!!
                else -> synchronized(this) {
                    if (instance == null) instance = FortSdk()
                    instance!!
                }
            }
        }

        /**
         * @param context
         * @return FORT device fingerprint
         */
        @Synchronized
        @JvmStatic
        fun getDeviceId(context: Context): String? {
            return Utils.getDeviceId(context)
        }
    }

    /**
     * @param context
     * @param fortRequest
     * @param requestCode
     * @param callbackManager
     * @param callback
     * @throws Exception
     */
    @Deprecated("")
    @Throws(Exception::class)
    fun registerCallback(
        context: Activity, fortRequest: FortRequest, environment: String, requestCode: Int,
        callbackManager: FortCallBackManager,
        callback: FortInterfaces.OnTnxProcessed?
    ) {
        if (callbackManager !is FortCallback) {
            throw Exception(
                "Unexpected CallbackManager, " +
                        "please use the provided Factory."
            )
        }
        callbackManager.registerCallback(
            requestCode,
            object : FortCallback.Callback {
                override fun onActivityResult(
                    mRequestCode: Int,
                    resultCode: Int,
                    data: Intent?
                ): Boolean {
                    return if (requestCode == mRequestCode) {
                        onActivityResult(fortRequest.requestMap, resultCode, data, callback)
                    } else false
                }
            }
        )

        // clean merchant request
        DataSecurityService.cleanMerhcantRequestMap(fortRequest.requestMap)


        // shared variables initialization
         initializeSharedValues(context, fortRequest)
        val openSDK = Intent(context, InitSecureConnectionActivity::class.java)
        openSDK.putExtra(Constants.EXTRAS.SDK_MERCHANT_REQUEST, fortRequest)
        openSDK.putExtra(Constants.EXTRAS.SDK_ENVIRONMENT, environment)
        openSDK.putExtra(Constants.EXTRAS.SDK_SHOW_LOADING, true)
        context.startActivityForResult(openSDK, requestCode)
    }


    /**
     * @param context
     * @param fortRequest
     * @param requestCode
     * @param callbackManager
     * @param callback
     * @throws Exception
     */
    @Throws(java.lang.Exception::class)
    fun registerCallback(
        context: Activity, fortRequest: FortRequest, environment: String?, requestCode: Int,
        callbackManager: FortCallBackManager?, showLoading: Boolean,
        callback: FortInterfaces.OnTnxProcessed?
    ) {
        if (callbackManager !is FortCallback) {
            throw Exception(
                "Unexpected CallbackManager, " +
                        "please use the provided Factory."
            )
        }
        callbackManager.registerCallback(
            requestCode,
            object : FortCallback.Callback {
                override fun onActivityResult(
                    mRequestCode: Int,
                    resultCode: Int,
                    data: Intent?
                ): Boolean {
                    return if (requestCode == mRequestCode) {
                        onActivityResult(fortRequest.requestMap, resultCode, data, callback)
                    } else false
                }
            }
        )

        // clean merchant request
        DataSecurityService.cleanMerhcantRequestMap(fortRequest.requestMap)
        // shared variables initialization
        initializeSharedValues(context, fortRequest)
        val openSDK = Intent(context, InitSecureConnectionActivity::class.java)
        openSDK.putExtra(Constants.EXTRAS.SDK_MERCHANT_REQUEST, fortRequest)
        openSDK.putExtra(Constants.EXTRAS.SDK_ENVIRONMENT, environment)
        openSDK.putExtra(Constants.EXTRAS.SDK_SHOW_LOADING, showLoading)
        context.startActivityForResult(openSDK, requestCode)
    }




    /**
     * @param requestParams
     * @param resultCode
     * @param data
     * @param tnxProcessedCallback
     * @return
     */
    fun onActivityResult(
        requestParams: Map<String, Any>?,
        resultCode: Int,
        data: Intent?,
        tnxProcessedCallback: FortInterfaces.OnTnxProcessed?
    ): Boolean {
        //handle results and return it back to merchant
        if (!data?.extras?.containsKey(Constants.EXTRAS.SDK_RESPONSE)!!) {

            //canceled by user
            val msg: String? = if (MapUtils.getLanguage(requestParams) == Constants.LANGUAGES.ARABIC) {
                "تم الغاء العملية من المشتري"
            } else {
                "Transaction canceled by payer"
            }
            tnxProcessedCallback?.onCancel(
                requestParams,
                MapUtils.getCanceledByUserResponse(msg, requestParams)
            )
            return false
        }
        val response: SdkResponse? =
            data.getSerializableExtra(Constants.EXTRAS.SDK_RESPONSE) as SdkResponse?
        if (response?.isSuccess!!) {
            tnxProcessedCallback?.onSuccess(requestParams, response.responseMap)
        } else {
            tnxProcessedCallback?.onFailure(requestParams, response.responseMap)
        }
        return false
    }
    private fun initializeSharedValues(context: Context, fortRequest: FortRequest?) {
        if (fortRequest != null && fortRequest.requestMap != null) {
            FortSdkCache.REQUEST_LANGUAGE = MapUtils.getLanguage(fortRequest.requestMap)
        }
        FortSdkCache.DEFAULT_SYSTEM_LANGUAGE = LocalizationService.getDefaultLocale(context)
    }


    /**
     * Responsible for Validating FortRequest
     * @param context Context
     * @param environment String
     * @param fortRequest FortRequest
     * @param fortCallback PayFortCallback?
     */
    fun validate(context: Context,
                 environment: String
                 ,fortRequest: FortRequest,
                 fortCallback: PayFortCallback?){

        val viewModel = InjectionUtils.provideValidateViewModel(environment)
        viewModel.validateRequest(context,fortRequest,fortCallback)
    }


}