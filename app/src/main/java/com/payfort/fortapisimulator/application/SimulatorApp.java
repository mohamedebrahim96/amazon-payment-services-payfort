package com.payfort.fortapisimulator.application;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by gbarham on 11/29/2015.
 */
public class SimulatorApp extends Application {

    private static AsyncHttpClient httpClient = null;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.httpClient = new AsyncHttpClient();
        addHeaders();
    }

    private void addHeaders(){
        this.httpClient.addHeader("Cache-Control","no-cache");
    }

    public static AsyncHttpClient getHttpClient() {
        return httpClient;
    }
}
