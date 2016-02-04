package com.quanganhct.simplegame.basecomponent.service.listeneres;

import android.net.wifi.p2p.WifiP2pManager;

/**
 * Created by quanganh.nguyen on 1/31/2016.
 */
public class InitiateServiceDiscoveryListener implements WifiP2pManager.ActionListener {

    private InitiateDiscoveryCallback callback;

    @Override
    public void onSuccess() {
        if (callback != null) {
            callback.onInitiateDiscoverySuccess();
        }
    }

    @Override
    public void onFailure(int reason) {
        if (callback != null) {
            callback.onInitiateDiscoveryFailure(reason);
        }
    }

    public void setCallback(InitiateDiscoveryCallback callback) {
        this.callback = callback;
    }

    public static interface InitiateDiscoveryCallback {
        void onInitiateDiscoverySuccess();

        void onInitiateDiscoveryFailure(int reason);
    }
}
