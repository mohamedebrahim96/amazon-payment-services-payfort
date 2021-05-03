package com.payfort.fortapisimulator.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.iovation.mobile.android.DevicePrint;
import com.payfort.fortapisimulator.CustomUISample;
import com.payfort.fortapisimulator.R;
import com.payfort.fortapisimulator.adapters.FortParamsAdapter;
import com.payfort.fortapisimulator.data.beans.PfFortReqRespParams;
import com.payfort.fortapisimulator.data.constants.Constants;
import com.payfort.fortpaymentsdk.FortSdk;
import com.payfort.fortpaymentsdk.callbacks.FortCallBackManager;
import com.payfort.fortpaymentsdk.callbacks.FortCallback;
import com.payfort.fortpaymentsdk.callbacks.FortInterfaces;
import com.payfort.fortpaymentsdk.domain.model.FortRequest;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.iovation.mobile.android.DevicePrint.getBlackbox;


public class MainActivity extends AppCompatActivity {

    private TextView submitToTV = null;
    private Spinner languageSpinner = null;
    private Spinner commandSpinner = null;
    private Spinner paymentOptionSpinner = null;
    private Spinner installmentsSpinner = null;

    private EditText sdkTokenET = null;
    private TextView etDeviceId;

    private CheckBox doViewResponsePageCB = null;
    private CheckBox doAddDeviceFingerprintCB = null;
    private CheckBox showLoadingCB = null;
    private CheckBox showFraudExtrasParamsCB = null;

    private LinearLayout installmentsLL = null;
    private LinearLayout llAsList = null;

    private Context mContext = null;
    private Context appContext = null;

    private Button submitButton = null;

    private Button btnCustomScreen = null;
    private Button btnHalfPay = null;

    private Gson gson = new Gson();
    private String[] staticFields = {"merchant_identifier", "language", "command",
            "installments", "payment_option", "signature", "sdk_token", "access_code"};
    private ArrayList<String> staticParams = new ArrayList<>();
    private List<PfFortReqRespParams> fortParams = new ArrayList<>();

    private FortParamsAdapter mAdapter = null;
    private Constants.ENVIRONMENTS_VALUES mEnvironment;

    private FortCallBackManager fortCallback = null;

    private Spinner environmentSpinner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DevicePrint.start(getApplicationContext());
        this.etDeviceId = findViewById(R.id.etDeviceId);
        this.submitToTV = (TextView) findViewById(R.id.environmentNameTV);
        this.languageSpinner = (Spinner) findViewById(R.id.languagesSpinner);
        this.commandSpinner = (Spinner) findViewById(R.id.commandsSpinner);
        this.paymentOptionSpinner = (Spinner) findViewById(R.id.paymentOpSpinner);
        this.installmentsSpinner = (Spinner) findViewById(R.id.installmentsSpinner);
        this.llAsList = (LinearLayout) findViewById(R.id.otherParamsLL);
        this.installmentsLL = (LinearLayout) findViewById(R.id.installmentsLL);
        this.sdkTokenET = (EditText) findViewById(R.id.sdkTokenET);
        this.doViewResponsePageCB = (CheckBox) findViewById(R.id.doViewResponsePage);
        this.mContext = this;
        this.appContext = this.getApplicationContext();
        this.doAddDeviceFingerprintCB = (CheckBox) findViewById(R.id.doAddDeviceFingerprint);
        this.submitButton = (Button) findViewById(R.id.callSdkBtn);
        this.showLoadingCB = (CheckBox) findViewById(R.id.showLoading);
        this.showFraudExtrasParamsCB = (CheckBox) findViewById(R.id.showFraudExtrasCB);
        this.environmentSpinner = (Spinner) findViewById(R.id.environmentSpinner);
        btnHalfPay = findViewById(R.id.btnHalfPay);
        btnCustomScreen = findViewById(R.id.btnCustomScreen);

        setupEnvironmentListener();
        environmentSpinner.setSelection(0);
        staticParams.addAll(Arrays.asList(staticFields));
        viewsListeners();
        if (fortCallback == null)
            fortCallback = FortCallback.Factory.create();

        if (mAdapter == null)
            setupAdapter();

        for (PfFortReqRespParams param : fortParams) {
            param.setActive(true);
        }

        mAdapter.notifyDataSetChanged(llAsList, fortParams, mEnvironment);
        etDeviceId.setText(FortSdk.getDeviceId(this));
    }


    private void setupEnvironmentListener() {
        environmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //Sandbox
                        mEnvironment = Constants.ENVIRONMENTS_VALUES.SANDBOX2;
                        break;
                    case 1:
                        //production
                        mEnvironment = Constants.ENVIRONMENTS_VALUES.PRODUCTION;
                        break;
                }
                submitToTV.setText(mEnvironment.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void setupAdapter() {
        this.mAdapter = new FortParamsAdapter(mContext,
                R.layout.params_lv_row, fortParams, mEnvironment,
                FortSdk.getDeviceId(MainActivity.this));
        mAdapter.notifyDataSetChanged(llAsList, fortParams, mEnvironment);
    }

    private void viewsListeners() {
        commandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fortParams.clear();
                addOtherStaticParams();
                mAdapter.notifyDataSetChanged(llAsList, fortParams, mEnvironment);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        submitButton.setOnClickListener(v -> callSDK());

        showFraudExtrasParamsCB.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                addFraudExtrasParams();
            } else {
                fortParams.clear();
                addOtherStaticParams();
                mAdapter.notifyDataSetChanged(llAsList, fortParams, mEnvironment);
            }
        });

        btnCustomScreen.setOnClickListener(v -> moveToCustomScreen());

        btnHalfPay.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("env", mEnvironment.getSdkEnvironemt());
            bundle.putSerializable("fortRequest", getFortRequest());
            CustomUiDialog.newInstance(bundle).show(getSupportFragmentManager(), "TAG");
        });

    }

    private void moveToCustomScreen() {
        Intent intent = new Intent(MainActivity.this, CustomUISample.class);
        intent.putExtra("fortRequest", getFortRequest());
        intent.putExtra("env", mEnvironment.getSdkEnvironemt());
        startActivity(intent);
    }


    private void callSDK() {
        try {
            //collect request
            FortRequest fortRequest = getFortRequest();

            boolean showLoading = showLoadingCB.isChecked();
            FortSdk.getInstance().registerCallback(this, fortRequest, mEnvironment.getSdkEnvironemt(), 5, fortCallback, showLoading, new FortInterfaces.OnTnxProcessed() {
                @Override
                public void onCancel(Map<String, Object> requestParamsMap, Map<String, Object> responseMap) {
                    System.out.println("onCancel==REQ=>> " + responseMap.toString());
                    if (responseMap.size() > 0)
                        openResponsePage(gson.toJson(responseMap));
                }

                @Override
                public void onSuccess(Map<String, Object> requestParamsMap, Map<String, Object> fortResponseMap) {
                    System.out.println("onSuccess==REQ=>> " + requestParamsMap.toString());
                    if (fortResponseMap.size() > 0)
                        openResponsePage(gson.toJson(fortResponseMap));
                }

                            @Override
                            public void onFailure(Map<String, Object> requestParamsMap, Map<String, Object> fortResponseMap) {
                                System.out.println("onFailure==REQ=>> " + requestParamsMap.toString());
                                if (fortResponseMap.size() > 0)
                                    openResponsePage(gson.toJson(fortResponseMap));
                            }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NotNull
    private FortRequest getFortRequest() {
        FortRequest fortRequest = new FortRequest();
        fortRequest.setShowResponsePage(doViewResponsePageCB.isChecked());
        Map<String, Object> map = new TreeMap<String, Object>();
        map.put("language", String.valueOf(languageSpinner.getSelectedItem()));
        map.put("sdk_token", sdkTokenET.getText().toString());
        String command = String.valueOf(commandSpinner.getSelectedItem());
        map.put("command", command.equals("NULL") ? null : command);
        for (int i = 0; i < mAdapter.getCount(); i++) {
            if (!mAdapter.paramsValues.get(i).isEmpty())
                map.put(fortParams.get(i).getParamName(), mAdapter.paramsValues.get(i));
        }

        String paymentOption = String.valueOf(paymentOptionSpinner.getSelectedItem());
        String installments = String.valueOf(installmentsSpinner.getSelectedItem());
        if (paymentOption != null && !paymentOption.isEmpty()) {
            map.put("payment_option", paymentOption);
        }
        if (installments != null && !installments.isEmpty()) {
            map.put("installments", installments);
        }
        if (doAddDeviceFingerprintCB.isChecked()) {
            String fingerprintValue = getBlackbox(getApplicationContext());
            map.put("device_fingerprint", fingerprintValue);
        }

        fortRequest.setRequestMap(map);
        return fortRequest;
    }

    private void openResponsePage(String responseString) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent openResponseActivityIntent = new Intent(MainActivity.this, ResponseActivity.class);
            openResponseActivityIntent.putExtra("responseString", responseString);
            startActivity(openResponseActivityIntent);
        }, 100);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fortCallback.onActivityResult(requestCode, resultCode, data);
    }



    private void addOtherStaticParams() {

        String[] main = {"amount", "currency", "customer_email", "customer_name",
                "token_name", "check_fraud", "customer_ip", "cart_details", "check_3ds", "merchant_reference", "eci", "order_description", "card_number", "expiry_date", "card_security_code", "remember_me", "customer_type", "customer_first_name",
                "customer_middle_initial", "customer_address1", "customer_phone", "ship_type", "ship_first_name", "ship_last_name", "ship_method", "merchant_extra", "merchant_extra1", "merchant_extra2", "merchant_extra3", "settlement_reference"
                , "phone_number", "merchant_extra4", "dynamic_descriptor", "flex_value"};

        for (String s : main) {
            PfFortReqRespParams cartDetails = new PfFortReqRespParams();
            cartDetails.setParamName(s);
            cartDetails.setActive(true);
            fortParams.add(cartDetails);
        }
    }


    private void addFraudExtrasParams() {
        String[] extrasArray = {"customer_type", "customer_id", "customer_first_name", "customer_middle_initial", "customer_last_name",
                "customer_address1", "customer_address2", "customer_apartment_no", "customer_city", "customer_state", "customer_zip_code",
                "customer_country_code", "customer_phone", "customer_alt_phone", "customer_date_birth", "ship_type", "ship_first_name",
                "ship_middle_name", "ship_last_name", "ship_address1", "ship_address2", "ship_apartment_no", "ship_address_city", "ship_address_state",
                "ship_zip_code", "ship_country_code", "ship_phone", "ship_alt_phone", "ship_email", "ship_comments", "ship_method", "fraud_extra1",
                "fraud_extra2", "fraud_extra3", "fraud_extra4", "fraud_extra5", "fraud_extra6", "fraud_extra7", "fraud_extra8", "fraud_extra9",
                "fraud_extra10", "fraud_extra11", "fraud_extra12", "fraud_extra13", "fraud_extra14", "fraud_extra15", "fraud_extra16", "fraud_extra17",
                "fraud_extra18", "fraud_extra19", "fraud_extra20", "fraud_extra21", "fraud_extra22", "fraud_extra23", "fraud_extra24", "fraud_extra25"};

        for (String s : extrasArray) {
            PfFortReqRespParams cartDetails = new PfFortReqRespParams();
            cartDetails.setParamName(s);
            cartDetails.setActive(true);
            fortParams.add(cartDetails);
        }

        mAdapter.notifyDataSetChanged(llAsList, fortParams, mEnvironment);
    }


}
