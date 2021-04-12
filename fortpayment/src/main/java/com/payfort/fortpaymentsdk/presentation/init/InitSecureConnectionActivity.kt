package com.payfort.fortpaymentsdk.presentation.init

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.payfort.fortpaymentsdk.R
import com.payfort.fortpaymentsdk.constants.Constants
import com.payfort.fortpaymentsdk.domain.model.FortSdkCache
import com.payfort.fortpaymentsdk.domain.model.Result
import com.payfort.fortpaymentsdk.domain.model.SdkResponse
import com.payfort.fortpaymentsdk.handlers.CreatorHandler
import com.payfort.fortpaymentsdk.presentation.base.FortActivity
import com.payfort.fortpaymentsdk.presentation.credit.CreditCardPaymentActivity
import com.payfort.fortpaymentsdk.utils.*
import kotlinx.android.synthetic.main.activity_init_secure_conn.*

internal class InitSecureConnectionActivity : FortActivity() {

    private val viewModel by viewModels<ValidateViewModel> {
        InjectionUtils.provideValidateViewModelFactory(getEnvironment())
    }
    private lateinit var mMessageReceiver: BroadcastReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init_secure_conn)
        setFinishOnTouchOutside(false)
        handleShowLoading()
        setupLocalBroadCastConfigs()
        observeValues()
    }

    private fun handleShowLoading() {
        if (intent.hasExtra(Constants.EXTRAS.SDK_SHOW_LOADING) && !intent.extras!!.getBoolean(Constants.EXTRAS.SDK_SHOW_LOADING, true)) {
            initContainerRL.gone()
        }
    }

    private fun observeValues() {
        if (Utils.haveNetworkConnection(this)) {
            viewModel.validateRequest(this, getMerchantRequest())
            viewModel.result.observe(this) {
                when (it) {
                    is Result.Success -> {
                        val sdkResponse = it.response
                        if (sdkResponse.isSuccess) {
                            val intent = Intent(this@InitSecureConnectionActivity, CreditCardPaymentActivity::class.java)
                            intent.putExtra(Constants.EXTRAS.SDK_MERCHANT_REQUEST, getMerchantRequest())
                            intent.putExtra(Constants.EXTRAS.SDK_CURRENCY_DP, sdkResponse.currencyDecimalPoints)
                            if (sdkResponse.merchantToken != null)
                                intent.putExtra(Constants.EXTRAS.SDK_MERCHANT_TOKEN, sdkResponse.merchantToken)
                            intent.putExtra(Constants.EXTRAS.SDK_ENVIRONMENT, getEnvironment())
                            intent.putExtra(Constants.EXTRAS.SDK_DEFAULT_LOCALE, FortSdkCache.DEFAULT_SYSTEM_LANGUAGE)
                            startActivity(intent)
                            return@observe
                        }

                        failedMessage(sdkResponse)
                    }
                    is Result.Failure -> {
                        val sdkResponse = CreatorHandler.convertThrowableToSdkResponse(this, it.throwable, getMerchantRequest())
                        failedMessage(sdkResponse)
                    }

                }
            }
        } else {
            setNoInternetMessageResult()
        }
    }

    private fun setNoInternetMessageResult() {
        Toast.makeText(this@InitSecureConnectionActivity, resources.getString(R.string.pf_no_connection), Toast.LENGTH_LONG).show()
        val sdkResponse: SdkResponse = MapUtils.getFailedToInitConnectionResponse(resources.getString(
            R.string.pf_no_connection
        ), getMerchantRequest().requestMap)
        failedMessage(sdkResponse)
    }


    private fun setupLocalBroadCastConfigs() {
        mMessageReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val returnIntent = this@InitSecureConnectionActivity.intent
                if (intent.hasExtra(Constants.EXTRAS.SDK_RESPONSE))
                    returnIntent.putExtra(Constants.EXTRAS.SDK_RESPONSE, intent.getSerializableExtra(Constants.EXTRAS.SDK_RESPONSE))
                this@InitSecureConnectionActivity.setResult(RESULT_OK, returnIntent)
                finish()
            }
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(
            mMessageReceiver, IntentFilter(
                Constants.LOCAL_BROADCAST_EVENTS.RESPONSE_EVENT
            )
        )

    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this@InitSecureConnectionActivity).unregisterReceiver(mMessageReceiver)
        super.onDestroy()
    }

    override fun finish() {
        super.localizationService.restoreLocale(this@InitSecureConnectionActivity, FortSdkCache.DEFAULT_SYSTEM_LANGUAGE)
        super.finish()
    }


}