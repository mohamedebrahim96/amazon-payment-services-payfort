package com.payfort.fortapisimulator.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.payfort.fortapisimulator.R;
import com.payfort.fortapisimulator.SignatureGenerator;
import com.payfort.fortapisimulator.adapters.FortParamsAdapter;
import com.payfort.fortapisimulator.application.SimulatorApp;
import com.payfort.fortapisimulator.connection.CustomSSLSocketFactory;
import com.payfort.fortapisimulator.data.beans.PfFortReqRespParams;
import com.payfort.fortapisimulator.data.constants.Constants;
import com.payfort.fortpaymentsdk.FortSdk;
import com.payfort.fortpaymentsdk.constants.Constants.*;
import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by gbarham on 2/15/2016.
 */
public class GenerateTokenActivity extends Activity {
    private List<PfFortReqRespParams> fortParams = new ArrayList<>();
    private FortParamsAdapter mAdapter = null;
    private String[] staticFields = {"language", "service_command", "signature", "sdk_token", "device_id"};
    private ArrayList<String> staticParams = new ArrayList<>();
    private LinearLayout llAsList = null;


    private TextView deviceIdValueTV = null;
    private Spinner languageSpinner, shaSpinner, environmentSpinner = null;
    private ProgressBar loadingPB = null;
    private EditText passphraseET = null;
    private Gson gson = new Gson();

    private Constants.ENVIRONMENTS_VALUES mEnvironment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);
        initActivity();

        // set default
        mEnvironment = Constants.ENVIRONMENTS_VALUES.STG;
        setupEnvironmentListener();
        staticParams.addAll(Arrays.asList(staticFields));
        //getParams();

        environmentSpinner.setSelection(1);
        shaSpinner.setSelection(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter == null) {
            setupAdapter();
        }
        this.deviceIdValueTV.setText(FortSdk.getDeviceId(GenerateTokenActivity.this));

        //enable
        findViewById(R.id.nextTV).setVisibility(View.VISIBLE);
        loadingPB.setVisibility(View.GONE);
        //enable all other elements
        for (PfFortReqRespParams param : fortParams) {
            param.setActive(true);
        }

        if(fortParams.isEmpty()) {
            String[] otherParams = {"merchant_identifier", "access_code"};
            for (String s : otherParams) {
                PfFortReqRespParams p = new PfFortReqRespParams();
                p.setParamName(s);
                p.setActive(true);
                fortParams.add(p);
            }
        }
        mAdapter.notifyDataSetChanged(llAsList, fortParams, mEnvironment);
        passphraseET.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        passphraseET.setEnabled(true);
        languageSpinner.setEnabled(true);
        shaSpinner.setEnabled(true);
    }

    private void initActivity() {
        this.deviceIdValueTV = (TextView) findViewById(R.id.deviceIdValueTV);
        this.languageSpinner = (Spinner) findViewById(R.id.languagesSpinner);
        this.shaSpinner = (Spinner) findViewById(R.id.shaSpinner);
        this.llAsList = (LinearLayout) findViewById(R.id.otherParamsLL);
        this.loadingPB = (ProgressBar) findViewById(R.id.loadingPB);
        this.passphraseET = (EditText) findViewById(R.id.passphraseET);
        this.environmentSpinner = (Spinner) findViewById(R.id.environmentSpinner);
    }

    private void setupAdapter() {
        this.mAdapter = new FortParamsAdapter(this,
                R.layout.params_lv_row, fortParams, mEnvironment,
                FortSdk.getDeviceId(GenerateTokenActivity.this));
        mAdapter.notifyDataSetChanged(llAsList, fortParams, mEnvironment);
    }

    public void getToken(View v) throws Exception {
        //show loading
        v.setVisibility(View.GONE);
        loadingPB.setVisibility(View.VISIBLE);
        //disable all other elements
        for (PfFortReqRespParams param : fortParams) {
            param.setActive(false);
        }
        mAdapter.notifyDataSetChanged(llAsList, fortParams, mEnvironment);
        passphraseET.setEnabled(false);
        passphraseET.setInputType(InputType.TYPE_NULL);
        languageSpinner.setEnabled(false);
        shaSpinner.setEnabled(false);

        //get params
        TreeMap<String, String> params = new TreeMap<>();
        params.put(com.payfort.fortpaymentsdk.constants.Constants.FORT_PARAMS.LANGUAGE, String.valueOf(languageSpinner.getSelectedItem()));
        params.put("service_command", "SDK_TOKEN");
        params.put("device_id", deviceIdValueTV.getText().toString());
        for (int i = 0; i < mAdapter.getCount(); i++) {
            params.put(fortParams.get(i).getParamName(), mAdapter.paramsValues.get(i));
        }
        //generate signature
        params.put("signature", SignatureGenerator.generateSignatureByParamsMap(passphraseET.getText().toString().trim(),
                String.valueOf(shaSpinner.getSelectedItem()), params));

        //all collected and ready to submit
        System.out.println("==== > Generate Token req param = " + params.toString());
        StringEntity entity = new StringEntity(gson.toJson(params));


        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);
        CustomSSLSocketFactory sf = new CustomSSLSocketFactory(trustStore);
        sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        SimulatorApp.getHttpClient().setSSLSocketFactory(sf);
        SimulatorApp.getHttpClient().post(GenerateTokenActivity.this, mEnvironment.getTokenCreationUrl(), entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Map<String, String> responseMap = gson.fromJson(response.toString(), Map.class);
                if (responseMap != null)
                    if (responseMap.containsKey(FORT_PARAMS.RESPONSE_CODE)) {
                        Toast.makeText(GenerateTokenActivity.this, responseMap.get(com.payfort.fortpaymentsdk.constants.Constants.FORT_PARAMS.RESPONSE_MSG), Toast.LENGTH_LONG).show();
                        if (responseMap.get(FORT_PARAMS.RESPONSE_CODE).substring(2).equals("000")) {
                            Intent responseIntent = new Intent(GenerateTokenActivity.this, MainActivity.class);
                            responseIntent.putExtra("sdk_token", responseMap.get("sdk_token"));
                            responseIntent.putExtra("environment", mEnvironment);
                            startActivity(responseIntent);
                        } else {
                            Intent responseIntent = new Intent(GenerateTokenActivity.this, ResponseActivity.class);
                            responseIntent.putExtra("responseString", response.toString());
                            startActivity(responseIntent);
                        }
                    }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("responseString = " + responseString);
            }
        });
    }

    private void getParams() {
        RequestParams params = new RequestParams();
        params.put("service_command", "SDK_TOKEN");
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            CustomSSLSocketFactory sf = new CustomSSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            SimulatorApp.getHttpClient().setSSLSocketFactory(sf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SimulatorApp.getHttpClient().post(mEnvironment.getParamsUrl(), params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray array = response.getJSONArray("list");
                    fortParams.clear();
                    for (int n = 0; n < array.length(); n++) {
                        PfFortReqRespParams ob = gson.fromJson(array.getJSONObject(n).toString(), PfFortReqRespParams.class);
                        if (!staticParams.contains(ob.getParamName())) {
                            ob.setActive(true);
                            fortParams.add(ob);
                        }
                    }
                    mAdapter.notifyDataSetChanged(llAsList, fortParams, mEnvironment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void setupEnvironmentListener() {
        environmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:

                        //STG
                        mEnvironment = Constants.ENVIRONMENTS_VALUES.STG;
                        break;

                    case 1:

                        //sandbox
                        mEnvironment = Constants.ENVIRONMENTS_VALUES.SANDBOX;
                        break;
                    case 2:

                        //Sandbox2
                        mEnvironment = Constants.ENVIRONMENTS_VALUES.SANDBOX2;
                        break;
                    case 3:

                        //production
                        mEnvironment = Constants.ENVIRONMENTS_VALUES.PRODUCTION;
                        break;
                    case 4:

                        //dev
                        mEnvironment = Constants.ENVIRONMENTS_VALUES.DEV;
                        break;
                    case 5:

                        // CYBERSOURCE_VALUES
                        mEnvironment = Constants.ENVIRONMENTS_VALUES.CYBERSOURCE;
                        break;
                    case 6:

                        // STG_NPS_VALUES
                        mEnvironment = Constants.ENVIRONMENTS_VALUES.STG_NPS;
                        break;
                }

                passphraseET.setText(mEnvironment.getRequestPassPhrase());
                mAdapter.notifyDataSetChanged(llAsList, fortParams, mEnvironment);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}