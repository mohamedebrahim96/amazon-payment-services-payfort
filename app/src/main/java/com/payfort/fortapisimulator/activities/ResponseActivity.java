package com.payfort.fortapisimulator.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.payfort.fortapisimulator.R;

import java.util.ArrayList;
import java.util.Map;

public class ResponseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);
        ListView responseLV = (ListView) findViewById(R.id.responseLV);
        String respAsString = getIntent().getExtras().getString("responseString");
        Gson gson = new Gson();
        Map<String, String> resposneMap = gson.fromJson(respAsString, Map.class);
        ArrayList<String> dataList = new ArrayList<>();
        for(String key: resposneMap.keySet()){

            dataList.add(key+"= "+resposneMap.get(key));
        }
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(ResponseActivity.this,R.layout.simple_list_item_1,dataList);
        responseLV.setAdapter(mAdapter);
    }
}
