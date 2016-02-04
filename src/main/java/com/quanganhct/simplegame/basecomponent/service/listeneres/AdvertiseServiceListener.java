package com.quanganhct.simplegame.basecomponent.service.listeneres;

import android.net.wifi.p2p.WifiP2pManager;

/**
 * Created by quanganh.nguyen on 1/31/2016.
 */
public class AdvertiseServiceListener implements WifiP2pManager.ActionListener {

    private AdvertiseCallback callback;

    @Override
    public void onSuccess() {
        if (callback != null) {
            callback.onAdvertiseSuccess();
        }
    }

    @Override
    public void onFailure(int reason) {
        if (callback != null) {
            callback.onAdvertiesFailure(reason);
        }
    }

    public void setCallback(AdvertiseCallback callback) {
        this.callback = callback;
    }

    public static interface AdvertiseCallback {
        void onAdvertiseSuccess();

        void onAdvertiesFailure(int reason);
    }
}
