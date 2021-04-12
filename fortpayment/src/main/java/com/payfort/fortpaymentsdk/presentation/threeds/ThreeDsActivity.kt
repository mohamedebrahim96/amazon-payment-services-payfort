package com.payfort.fortpaymentsdk.presentation.threeds

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.payfort.fortpaymentsdk.R
import com.payfort.fortpaymentsdk.constants.Constants
import com.payfort.fortpaymentsdk.domain.model.SdkResponse
import com.payfort.fortpaymentsdk.handlers.PayHandler
import com.payfort.fortpaymentsdk.utils.MapUtils
import com.payfort.fortpaymentsdk.utils.WebViewUtils
import com.payfort.fortpaymentsdk.utils.gone
import com.payfort.fortpaymentsdk.utils.visible


internal class ThreeDsActivity : AppCompatActivity() {

    companion object {
        const val URL = "URL"
        const val KEY_SUCCESS = "SUCCESS"
        const val KEY_FAILURE = "FAILURE"

        fun start(context: Context, url: String) {
            val intent = Intent(context, ThreeDsActivity::class.java)
            intent.putExtra(URL, url)
            context.startActivity(intent)
        }

    }

    private lateinit var mWebView: WebView
    private val url: String? by lazy { intent.extras?.getString(URL) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview)
        mWebView = findViewById(R.id.webView)
        WebViewUtils.setupWebView(mWebView)
        mWebView.webViewClient = webViewClient()
        mWebView.loadUrl(url!!)
        mWebView.gone()
    }

    override fun onStart() {
        super.onStart()
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
    }

    private fun webViewClient(): WebViewClient {
        return object : WebViewClient() {
            @TargetApi(Build.VERSION_CODES.O)
            override fun onRenderProcessGone(
                view: WebView,
                detail: RenderProcessGoneDetail
            ): Boolean {
                return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) false else try {
                    super.onRenderProcessGone(view, detail)
                } catch (e: Exception) {
                    false
                }
            }

            override fun onReceivedError(
                view: WebView?, errorCode: Int, description: String?, failingUrl: String?
            ) {
                if (errorCode == ERROR_TIMEOUT || errorCode == ERROR_BAD_URL ||
                    errorCode == ERROR_CONNECT || errorCode == ERROR_IO || errorCode == ERROR_HOST_LOOKUP
                ) {
                    setResultError()
                }
                super.onReceivedError(view, errorCode, description, failingUrl)
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (!url.contains(Constants.FORT_URI.WV_CHECKER_3DS_PARAMS_URL))
                    mWebView.visible()

                    if (url.contains(Constants.FORT_URI.WV_CHECKER_3DS_PARAMS_URL)) {
                        val sdkResponse = MapUtils.collectResponseFromURL(url)
                        setResultSuccess(sdkResponse)
                    }

            }
        }
    }

    override fun onBackPressed() {
    }


    private fun setResultSuccess(sdkResponse: SdkResponse) {
        val intent = Intent(PayHandler.THREEDS_INTENT_FILTER)
        intent.putExtra(KEY_SUCCESS, sdkResponse)
        sendBroadcast(intent)
        finish()
    }

    private fun setResultError() {
        val intent = Intent(PayHandler.THREEDS_INTENT_FILTER)
        intent.putExtra(KEY_FAILURE, true)
        sendBroadcast(intent)
        finish()
    }


}