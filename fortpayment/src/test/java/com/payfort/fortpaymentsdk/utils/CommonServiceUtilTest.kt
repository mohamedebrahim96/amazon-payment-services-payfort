package com.payfort.fortpaymentsdk.utils

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.SnackbarContentLayout
import com.payfort.fortpaymentsdk.R
import com.payfort.fortpaymentsdk.constants.Constants
import com.payfort.fortpaymentsdk.domain.model.FortRequest
import com.payfort.fortpaymentsdk.presentation.base.FortActivity
import com.payfort.fortpaymentsdk.presentation.credit.CreditCardPaymentActivity
import com.payfort.fortpaymentsdk.presentation.response.CreditCardResponseActivity
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class CommonServiceUtilTest {

    @Test
    fun getEnvironment_thenReturnEnv(){
        val intent = Intent()
        intent.putExtra(Constants.EXTRAS.SDK_ENVIRONMENT, "Env")
        assert(CommonServiceUtil.getEnvironment(intent) != null)
    }
    @Test
    fun getEnvironment_thenReturnNull(){
        val intent = Intent()
        assert(CommonServiceUtil.getEnvironment(intent) == null)
    }

    @Test
    fun getMerchantRequestObjFromIntent_thenReturnValidFortRequest() {
        val fortRequest = FortRequest()
        fortRequest.isShowResponsePage=true
        val hashMap = HashMap<String, Any>()
        fortRequest.requestMap=hashMap

        val intent = Intent()
        intent.putExtra(Constants.EXTRAS.SDK_MERCHANT_REQUEST, fortRequest)

        var merchantRequestObjFromIntent = CommonServiceUtil.getMerchantRequestObjFromIntent(intent)
        assert(merchantRequestObjFromIntent.isShowResponsePage == true)
    }

    @Test
    fun getMerchantRequestObjFromIntent_thenReturnInValidFortRequest() {
        val intent = Intent()
        var merchantRequestObjFromIntent = CommonServiceUtil.getMerchantRequestObjFromIntent(intent)
        assert(merchantRequestObjFromIntent.isShowResponsePage == false)
    }




}