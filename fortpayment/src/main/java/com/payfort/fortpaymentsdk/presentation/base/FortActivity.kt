package com.payfort.fortpaymentsdk.presentation.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.payfort.fortpaymentsdk.constants.Constants
import com.payfort.fortpaymentsdk.domain.model.FortSdkCache
import com.payfort.fortpaymentsdk.domain.model.SdkResponse
import com.payfort.fortpaymentsdk.utils.CommonServiceUtil
import com.payfort.fortpaymentsdk.utils.LocalizationService
import com.payfort.fortpaymentsdk.utils.LocalizationServicePerDevice

internal abstract class FortActivity : AppCompatActivity() {
     protected lateinit var localizationService: LocalizationService

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         localizationService = LocalizationServicePerDevice()
     }

    override fun attachBaseContext(newBase: Context?) {
        localizationService = LocalizationServicePerDevice()
        val context = localizationService.updateByLanguage(newBase, FortSdkCache.REQUEST_LANGUAGE, FortSdkCache.DEFAULT_SYSTEM_LANGUAGE)
        super.attachBaseContext(context)

    }
    internal open fun failedMessage(sdkResponse: SdkResponse) {
        val intent = intent
        intent.putExtra(Constants.EXTRAS.SDK_RESPONSE, sdkResponse)
        this.setResult(RESULT_OK, intent)
        finish()
    }

    internal fun getEnvironment(): String = CommonServiceUtil.getEnvironment(intent)
    internal fun getMerchantRequest() = CommonServiceUtil.getMerchantRequestObjFromIntent(intent)


}