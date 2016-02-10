package com.quanganhct.simplegame.basecomponent.service.listeneres;

import android.net.wifi.p2p.WifiP2pManager;

/**
 * Created by quanganh.nguyen on 2/10/2016.
 */
public class InitiatePeerConnectionListener implements WifiP2pManager.ActionListener {
    public void setCallback(InitiatePeerConnectionCallback callback) {
        this.callback = callback;
    }

    private InitiatePeerConnectionCallback callback;

    @Override
    public void onSuccess() {
        if (callback != null) {
            callback.onInitiatePeerConnectionSuccess();
        }
    }

    @Override
    public void onFailure(int reason) {
        if (callback != null) {
            callback.onInitiatePeerConnectionFailure(reason);
        }
    }

    public static interface InitiatePeerConnectionCallback {
        void onInitiatePeerConnectionSuccess();

        void onInitiatePeerConnectionFailure(int reason);
    }
}
