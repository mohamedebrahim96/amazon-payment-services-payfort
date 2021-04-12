package com.payfort.fortpaymentsdk.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.view.isVisible
import androidx.test.core.app.ApplicationProvider
import com.payfort.fortpaymentsdk.constants.Constants
import com.payfort.fortpaymentsdk.presentation.response.CreditCardResponseActivity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlertDialog
import org.robolectric.shadows.ShadowNetworkCapabilities


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class UtilsTest {
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
    }

    @Test
    fun `should be connected when connected to WiFi`() {
        val connectivityManager =context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = ShadowNetworkCapabilities.newInstance()
        shadowOf(networkCapabilities).addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        shadowOf(connectivityManager).setNetworkCapabilities(connectivityManager.activeNetwork, networkCapabilities)
        assertTrue(Utils.haveNetworkConnection(context))
    }

    @Test
    fun `should be connected when connected to CELLULAR`() {
        val connectivityManager =context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = ShadowNetworkCapabilities.newInstance()
        shadowOf(networkCapabilities).addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        shadowOf(connectivityManager).setNetworkCapabilities(connectivityManager.activeNetwork, networkCapabilities)
        assertTrue(Utils.haveNetworkConnection(context))
    }


    @Test
    fun `should be formatted when pass merchant request`(){
            val map = HashMap<String?,Any?>()
        map[Constants.FORT_PARAMS.AMOUNT] = "10"
        map[Constants.FORT_PARAMS.CURRENCY] = "SAR"
        assert(Utils.formatAmount(map,1)=="1.0 SAR")
    }


    @Test
    fun testShowAlertDialog(){
        val activity = Robolectric.buildActivity(CreditCardResponseActivity::class.java).create().start().resume().visible().get()
            Utils.displayCancelPaymentDialog(activity)
        var latestDialog = ShadowAlertDialog.getLatestDialog()
        assert(latestDialog.isShowing)
    }


    @Test
    fun testViewExKt(){
        val activity = Robolectric.buildActivity(CreditCardResponseActivity::class.java).create().start().resume().visible().get()
      activity.binding.responseContainerRL.visible()
        assert(activity.binding.responseContainerRL.isVisible)

        activity.binding.responseContainerRL.gone()
        assert(!activity.binding.responseContainerRL.isVisible)

        activity.binding.responseContainerRL.visible()
        activity.binding.responseContainerRL.invisible()
        assert(!activity.binding.responseContainerRL.isVisible)

    }
}