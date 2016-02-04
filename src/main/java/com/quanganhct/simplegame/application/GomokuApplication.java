package com.quanganhct.simplegame.application;

import android.app.Application;

import com.quanganhct.simplegame.basecomponent.service.WifiDirectManager;

/**
 * Created by quanganh.nguyen on 2/1/2016.
 */
public class GomokuApplication extends Application {
    public GomokuApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WifiDirectManager.getInstance().initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
