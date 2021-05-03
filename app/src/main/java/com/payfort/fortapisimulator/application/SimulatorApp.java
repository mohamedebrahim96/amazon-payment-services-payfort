package com.payfort.fortapisimulator.application;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;


/**
 * Created by gbarham on 11/29/2015.
 */
public class SimulatorApp extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


}
