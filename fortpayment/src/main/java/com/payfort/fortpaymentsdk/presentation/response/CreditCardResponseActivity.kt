package com.payfort.fortpaymentsdk.presentation.response

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.payfort.fortpaymentsdk.R
import com.payfort.fortpaymentsdk.constants.Constants
import com.payfort.fortpaymentsdk.databinding.ActivityCcResponseBinding
import com.payfort.fortpaymentsdk.domain.model.SdkResponse
import com.payfort.fortpaymentsdk.presentation.base.FortActivity
import com.payfort.fortpaymentsdk.utils.Utils

internal class CreditCardResponseActivity : FortActivity() {

    internal lateinit var binding: ActivityCcResponseBinding

    companion object {
        const val TIME_REQUEST = 5000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCcResponseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val createFromAsset = Typeface.createFromAsset(resources.assets, "fontello.ttf")
        binding.responseStatusIconIV.typeface = createFromAsset
        fillViews()
    }

    private fun fillViews() {
        getSdkResponse()?.let {
            if (it.isSuccess) {
                binding.responseContainerRL.setBackgroundColor(ContextCompat.getColor(this@CreditCardResponseActivity, R.color.pf_green))
                binding.responseStatusIconIV.text = resources.getString(R.string.icon_ok_circled)
                binding.responseStatusHintTV.text = resources.getString(R.string.pf_resp_page_great)
                binding.responseInfo1TV.text = Utils.getParamValue(it.responseMap, Constants.FORT_PARAMS.FORT_ID)
                binding.responseInfo2TV.text = Utils.getParamValue(it.responseMap, Constants.FORT_PARAMS.RESPONSE_MSG)
            } else {
                binding.responseContainerRL.setBackgroundColor(ContextCompat.getColor(this@CreditCardResponseActivity, R.color.pf_red))
                binding.responseStatusIconIV.text = resources.getString(R.string.icon_cancel_circled)
                binding.responseStatusHintTV.text = resources.getString(R.string.pf_resp_page_failed)
                val code = Constants.FORT_PARAMS.RESPONSE_CODE + " : " + Utils.getParamValue(it.responseMap, Constants.FORT_PARAMS.RESPONSE_CODE)
                binding.responseInfo1TV.text = code
                val message = Constants.FORT_PARAMS.RESPONSE_MSG + " : " + Utils.getParamValue(it.responseMap, Constants.FORT_PARAMS.RESPONSE_MSG)
                binding.responseInfo2TV.text = message
            }
        }
        //timer for DISPLAY_TIME sec(s)
        Handler().postDelayed({ finish() }, TIME_REQUEST)


    }

    private fun getSdkResponse() = intent.getSerializableExtra(Constants.EXTRAS.SDK_RESPONSE) as SdkResponse?


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun finish() {
        super.finish()
        val broadcastIntent = Intent(Constants.LOCAL_BROADCAST_EVENTS.RESPONSE_EVENT)
        broadcastIntent.putExtra(Constants.EXTRAS.SDK_RESPONSE, getSdkResponse())
        LocalBroadcastManager.getInstance(this@CreditCardResponseActivity)
            .sendBroadcast(broadcastIntent)
    }
}