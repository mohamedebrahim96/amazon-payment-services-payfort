package com.payfort.fortapisimulator.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.payfort.fortapisimulator.R
import com.payfort.fortpaymentsdk.callbacks.PayFortCallback
import com.payfort.fortpaymentsdk.domain.model.FortRequest
import com.payfort.fortpaymentsdk.utils.gone
import com.payfort.fortpaymentsdk.utils.visible
import com.payfort.fortpaymentsdk.views.model.PayComponents
import kotlinx.android.synthetic.main.bottom_sheet_customui.*

class CustomUiDialog : DialogFragment(), PayFortCallback {


    var gson = Gson()

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): CustomUiDialog {
            val fragment = CustomUiDialog()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onStart() {
        super.onStart()

        dialog?.let {
            val window = it.window
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_customui, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        val fortRequest: FortRequest = requireArguments().getSerializable("fortRequest") as FortRequest
        val environment = requireArguments().getString("env", "")
        val payComponents = PayComponents(etCardNumberView, cvvView = etCardCvv, etCardExpiry, holderNameView = cardHolderNameView)
        btnPay.setup(environment!!, fortRequest, payComponents, this)
    }

    override fun startLoading() {
        Log.e("startLoading", "startLoading")
        progressContainer.visible()
        enableFields(false)
    }


    override fun onSuccess(requestParamsMap: Map<String, Any>, fortResponseMap: Map<String, Any>) {
        stopLoading()
        openResponsePage(gson.toJson(fortResponseMap))
    }

    override fun onFailure(requestParamsMap: Map<String, Any>, fortResponseMap: Map<String, Any>) {
        stopLoading()
        openResponsePage(gson.toJson(fortResponseMap))
    }


    private fun openResponsePage(responseString: String) {
        Log.e("Error", responseString)
        stopLoading()
        val openResponseActivityIntent = Intent(requireContext(), ResponseActivity::class.java)
        openResponseActivityIntent.putExtra("responseString", responseString)
        startActivity(openResponseActivityIntent)
    }


    fun stopLoading() {
        Log.e("startLoading", "startLoading")
        progressContainer.gone()
        enableFields(true)
    }

    private fun enableFields(enableFields: Boolean) {
        cardHolderNameView.isEnabled = enableFields
        etCardNumberView.isEnabled = enableFields
        etCardExpiry.isEnabled = enableFields
        etCardCvv.isEnabled = enableFields
        btnPay.isEnabled = enableFields
    }

}