package com.payfort.fortapisimulator.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.payfort.fortapisimulator.R;
import com.payfort.fortpaymentsdk.FortSdk;
import com.payfort.fortpaymentsdk.callbacks.PayFortCallback;
import com.payfort.fortpaymentsdk.domain.model.FortRequest;
import com.payfort.fortpaymentsdk.views.CardCvvView;
import com.payfort.fortpaymentsdk.views.CardExpiryView;
import com.payfort.fortpaymentsdk.views.CardHolderNameView;
import com.payfort.fortpaymentsdk.views.FortCardNumberView;
import com.payfort.fortpaymentsdk.views.PayfortPayButton;
import com.payfort.fortpaymentsdk.views.model.PayComponents;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CustomUiJava extends AppCompatActivity {
    FortCardNumberView etCardNumberView;
    CardExpiryView etCardExpiry;
    CardCvvView etCardCvv;
    CardHolderNameView cardHolderNameView;
    PayfortPayButton btnPay;
    PayfortPayButton btnDirectPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_ui_java);
        etCardNumberView = findViewById(R.id.etCardNumberView);
        etCardExpiry = findViewById(R.id.etCardExpiry);
        etCardCvv = findViewById(R.id.etCardCvv);
        cardHolderNameView = findViewById(R.id.cardHolderNameView);
        btnPay = findViewById(R.id.btnPay);
        btnDirectPay = findViewById(R.id.btnDirectPay);

        // you need to Create FortRequest via FortRequest.class
        FortRequest fortRequest = new FortRequest(); // fill all the parameters required
        //then you need to chsose the environment Via FortSdk.ENVIRONMENT it's contains TEST and PRODUCTION ENVIRONMENTS
        String environment = FortSdk.ENVIRONMENT.TEST;
        // Finally you need to decleare a PayFortCallback
        PayFortCallback callback = new PayFortCallback() {
            @Override
            public void startLoading() { }
            @Override
            public void onSuccess(@NotNull Map<String, ?> requestParamsMap, @NotNull Map<String, ?> fortResponseMap) { }
            @Override
            public void onFailure(@NotNull Map<String, ?> requestParamsMap, @NotNull Map<String, ?> fortResponseMap) { }
        };

        FortSdk fortSdk = FortSdk.getInstance();
        fortSdk.validate(this,environment,fortRequest,callback);
    }
}