package com.payfort.fortpaymentsdk.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.Window
import android.view.WindowManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.payfort.fortpaymentsdk.BuildConfig
import com.payfort.fortpaymentsdk.R
import com.payfort.fortpaymentsdk.constants.Constants
import com.payfort.fortpaymentsdk.domain.model.SdkResponse
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*


internal object Utils {
    private var uniqueID: String? = null
    private const val PREF_UNIQUE_ID = "PREF_UNIQUE_ID"

    /**
     * @return
     */
    fun getOsDetails(context: Context): String? {
        val screenSize = getScreenSize(context)
        val deviceDetails: MutableMap<String, Any> = HashMap()
        deviceDetails["OS"] = "ANDROID"
        deviceDetails["MANUFACTURER"] = Build.MANUFACTURER
        deviceDetails["MODEL"] = Build.MODEL
        deviceDetails["BASE_OS"] = Build.VERSION.RELEASE
        deviceDetails["SDK_INT"] = Build.VERSION.SDK_INT
        deviceDetails["VERSION_CODES.BASE"] = Build.VERSION_CODES.BASE
        deviceDetails["FORT_SDK_VERSION_NAME"] = BuildConfig.VERSION_NAME
        deviceDetails["SCREEN_WIDTH"] = screenSize.x
        deviceDetails["SCREEN_HEIGHT"] = screenSize.y
        return Gson().toJson(deviceDetails)
    }

    fun haveNetworkConnection(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val networks = connectivityManager.allNetworks
            var networkInfo: NetworkInfo?
            for (mNetwork in networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork)
                if (networkInfo!!.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        } else {
            val info = connectivityManager.allNetworkInfo
            for (anInfo in info) {
                if (anInfo.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * get device screen size
     *
     * @param activity
     * @return
     */
    fun getScreenSize(context: Context): Point {
        val wm: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }


    /**
     * @param context
     * @return FORT device fingerprint
     */
    @Synchronized
    fun getDeviceId(context: Context): String? {
        if (uniqueID == null) {
            val sharedPrefs = context.getSharedPreferences(PREF_UNIQUE_ID, Context.MODE_PRIVATE)
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null)
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString()
                val editor = sharedPrefs.edit()
                editor.putString(PREF_UNIQUE_ID, uniqueID)
                editor.commit()
            }
        }
        return uniqueID
    }
    internal fun saveLatestEnvironment(context: Context,environment: String) {
        val sharedPrefs = context.getSharedPreferences(PREF_UNIQUE_ID, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString(Constants.FORT_PARAMS.ENVIRONMENT, environment)
        editor.commit()
    }
    internal fun getLatestEnvironment(context: Context): String{
        val sharedPrefs = context.getSharedPreferences(PREF_UNIQUE_ID, Context.MODE_PRIVATE)
        return sharedPrefs.getString(Constants.FORT_PARAMS.ENVIRONMENT, "").toString()
    }

    fun formatAmount(merchantRequestMap: Map<String?, Any?>?, decimalPoints: Int): String {
        val amount: String? = getParamValue(merchantRequestMap, Constants.FORT_PARAMS.AMOUNT)
        val currency: String? = getParamValue(merchantRequestMap, Constants.FORT_PARAMS.CURRENCY)
        val amountAsBigInteger = BigInteger(amount)
        val amountAsBigDecimal = BigDecimal(amountAsBigInteger, decimalPoints)
        return amountAsBigDecimal.toString() + " " + currency?.toUpperCase()
    }

    /**
     * @param requestMap
     * @param key
     * @return
     */
    fun getParamValue(requestMap: Map<String?, Any?>?, key: String?): String? {
        if (requestMap != null) if (requestMap.containsKey(key)) {
            if (requestMap[key] != null) {
                return requestMap[key].toString()
            }
        }
        return null
    }

    internal fun String.normalize(): String {
        return this.let {
            it.trim { it <= ' ' }.replace("\\s+|-".toRegex(), "")
        }

    }

    fun backToMerchant(activity: Activity, sdkResponse: SdkResponse?) {
        val broadcastIntent = Intent(Constants.LOCAL_BROADCAST_EVENTS.RESPONSE_EVENT)
        broadcastIntent.putExtra(Constants.EXTRAS.SDK_RESPONSE, sdkResponse)
        LocalBroadcastManager.getInstance(activity).sendBroadcast(broadcastIntent)
        activity.finish()
    }


    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    @JvmStatic
    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun displayCancelPaymentDialog(activity: Activity) {
        val dialogBuilder = AlertDialog.Builder(activity)
        val cancelDialog = dialogBuilder.create()
        cancelDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogBuilder.setTitle(null)
        dialogBuilder.setMessage(activity.resources.getString(R.string.pf_cancel_payment_msg))
        dialogBuilder.setPositiveButton(activity.resources.getString(R.string.pf_cancel_payment_btn_yes)) { dialog, which ->
            LocalBroadcastManager.getInstance(activity).sendBroadcast(Intent(Constants.LOCAL_BROADCAST_EVENTS.RESPONSE_EVENT))
            dialog.dismiss()
            activity.finish()
        }
        dialogBuilder.setNegativeButton(activity.resources.getString(R.string.pf_cancel_payment_btn_no)) { dialog, which -> dialog.dismiss() }
        dialogBuilder.show()
    }
}