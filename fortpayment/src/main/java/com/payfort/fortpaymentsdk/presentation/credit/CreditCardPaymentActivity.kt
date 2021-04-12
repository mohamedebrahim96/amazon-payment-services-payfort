package com.payfort.fortpaymentsdk.presentation.credit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.payfort.fortpaymentsdk.R
import com.payfort.fortpaymentsdk.callbacks.FortPayInternalCallback
import com.payfort.fortpaymentsdk.constants.Constants
import com.payfort.fortpaymentsdk.databinding.ActivityCcPaymentBinding
import com.payfort.fortpaymentsdk.domain.model.*
import com.payfort.fortpaymentsdk.presentation.base.FortActivity
import com.payfort.fortpaymentsdk.presentation.response.CreditCardResponseActivity
import com.payfort.fortpaymentsdk.utils.*
import com.payfort.fortpaymentsdk.views.model.PayComponents

internal class CreditCardPaymentActivity : FortActivity(), FortPayInternalCallback {

    private fun getMerchantToken(): MerchantToken? =
        intent.getSerializableExtra(Constants.EXTRAS.SDK_MERCHANT_TOKEN) as MerchantToken?

    private fun getCurrencyDecimalPoints() =
        intent.extras?.getInt(Constants.EXTRAS.SDK_CURRENCY_DP, 0)


    private lateinit var binding: ActivityCcPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCcPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
        initListeners()
    }

    private fun setupViews() {
        binding.amountTV.text = Utils.formatAmount(getMerchantRequest().requestMap, getCurrencyDecimalPoints()!!)
        val paymentOption: String? = MapUtils.getPaymentOptionValue(getMerchantRequest().requestMap)
            ?: getMerchantToken()?.paymentOptionName
        val defaultPaymentOption = CardBrand.fromCodeOrNull(paymentOption)
        binding.etCardNumberView.setUp(getEnvironment(), defaultPaymentOption)
        defaultPaymentOption?.let {
        binding.etCardCvv.setUpPaymentOption(it)
        }
        getMerchantToken()?.let {

            if (it.isRememberMe) {
                if (!MapUtils.displayRememberMe(getMerchantRequest().requestMap)) {
                    binding.rememberMeRL.gone()
                    binding.rememberMeTB.isChecked = false
                }

            } else {

                if (LocalizationService.getDefaultLocale(this).startsWith("ar"))
                    binding.etCardNumberView.text = (CommonServiceUtil.hackMaskedCardForArabic(getMerchantToken()?.maskedCardNumber))
                else binding.etCardNumberView.text = getMerchantToken()?.maskedCardNumber.toString() + ""

                if (getMerchantToken()?.expDate != null && !getMerchantToken()?.expDate?.isBlank()!! && getMerchantToken()?.expDate?.length!! >= 4) {
                    val expDate: String = getMerchantToken()?.expDate?.substring(0, 2)
                        .toString() + "/" + getMerchantToken()?.expDate?.substring(2)
                    binding.etCardExpiry.text = (expDate)
                }
            }

        }
        val payComponents = PayComponents(binding.etCardNumberView, binding.etCardCvv, binding.etCardExpiry, binding.cardHolderNameView)


        binding.btnPay.setUpInternalButton(getEnvironment(), getMerchantRequest(), payComponents, this)
        binding.btnPay.isValidatedBefore(true)
    }


    private fun initListeners() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.rememberMeTB.setOnCheckedChangeListener { _, isChecked ->
            binding.btnPay.isRememberMeEnabled(isChecked)

        }
        binding.etCardNumberView.setOnMaskedNumberChanged {
            binding.etCardExpiry.text = ""
            binding.etCardNumberView.text = ""
        }

    }


    override fun startLoading() {
        binding.loadingContainer.visible()
        binding.cardHolderNameView.isEnabled = false
        binding.etCardNumberView.isEnabled = false
        binding.etCardExpiry.isEnabled = false
        binding.etCardCvv.isEnabled = false
        binding.rememberMeRL.isEnabled = false
        binding.btnPay.isEnabled = false

    }

    override fun onSuccess(sdkResponse: SdkResponse) {
        if (getMerchantRequest().isShowResponsePage) {
            showResponsePage(sdkResponse)
        } else
            Utils.backToMerchant(this@CreditCardPaymentActivity, sdkResponse)

    }

    override fun onFailure(sdkResponse: SdkResponse) {
        Utils.backToMerchant(this@CreditCardPaymentActivity, sdkResponse)
    }


    private fun showResponsePage(sdkResponse: SdkResponse) {
        val intent = Intent(this@CreditCardPaymentActivity, CreditCardResponseActivity::class.java).apply {
            putExtra(Constants.EXTRAS.SDK_MERCHANT_REQUEST, getMerchantRequest())
            putExtra(Constants.EXTRAS.SDK_RESPONSE, sdkResponse)
            putExtra(Constants.EXTRAS.SDK_DEFAULT_LOCALE, FortSdkCache.DEFAULT_SYSTEM_LANGUAGE)
        }
        startActivity(intent)
        finish()
    }


    override fun onBackPressed() {
        if (binding.loadingContainer.visibility != View.VISIBLE) {
            Utils.displayCancelPaymentDialog(this@CreditCardPaymentActivity)
        } else {
            Toast.makeText(this@CreditCardPaymentActivity, resources.getString(R.string.pf_no_back_tnx_processing), Toast.LENGTH_LONG).show()
        }
    }

}