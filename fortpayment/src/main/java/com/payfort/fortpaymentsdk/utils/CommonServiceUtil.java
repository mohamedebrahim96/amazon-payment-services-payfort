package com.payfort.fortpaymentsdk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.payfort.fortpaymentsdk.R;
import com.payfort.fortpaymentsdk.constants.Constants;
import com.payfort.fortpaymentsdk.domain.model.FortRequest;

import java.util.HashMap;


public class CommonServiceUtil {

    private CommonServiceUtil(){}

    public static String getEnvironment(Intent intent) {
        //set merchantRequest value
        if (intent.hasExtra(Constants.EXTRAS.SDK_ENVIRONMENT)) {
            return intent.getStringExtra(Constants.EXTRAS.SDK_ENVIRONMENT);
        }
        return null;
    }

    public static String hackMaskedCardForArabic(String maskedCard)
    {
        String beforeMask = maskedCard.substring(0, maskedCard.indexOf('*'));
        String masked = maskedCard.substring(maskedCard.indexOf('*'), maskedCard.lastIndexOf('*') + 1);
        String after = maskedCard.substring(maskedCard.lastIndexOf('*') + 1);

        return after + masked + beforeMask;
    }

    public static FortRequest getMerchantRequestObjFromIntent(Intent intent) {
        FortRequest merchantFortRequest = null;
        //set merchantRequest value
        if (intent.hasExtra(Constants.EXTRAS.SDK_MERCHANT_REQUEST)) {
            merchantFortRequest = (FortRequest) intent.getSerializableExtra(Constants.EXTRAS.SDK_MERCHANT_REQUEST);
        }

        // still empty?
        if (merchantFortRequest == null) {

            //init the req, so the FORT returns the MISSING_PARAM
            merchantFortRequest = new FortRequest();
            merchantFortRequest.setRequestMap(new HashMap<>());
        }

        return merchantFortRequest;
    }

        public static Snackbar displayConnectionAlert(View fullView, final Context activity) {

        Snackbar snackbar = Snackbar
                .make(fullView,
                        activity.getResources().getString(R.string.pf_no_connection), Snackbar.LENGTH_LONG)
                .setAction(activity.getResources().getString(R.string.pf_wifi_settings),
                        view -> activity.startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)));

        snackbar.show();
        return snackbar;
    }




}
