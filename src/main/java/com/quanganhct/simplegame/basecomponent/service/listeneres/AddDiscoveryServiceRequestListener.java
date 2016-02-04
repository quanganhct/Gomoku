package com.quanganhct.simplegame.basecomponent.service.listeneres;

import android.net.wifi.p2p.WifiP2pManager;

/**
 * Created by quanganh.nguyen on 1/31/2016.
 */
public class AddDiscoveryServiceRequestListener implements WifiP2pManager.ActionListener {

    private AddServiceCallback callback;

    @Override
    public void onSuccess() {
        if (callback != null) {
            callback.onAddServiceSuccess();
        }
    }

    @Override
    public void onFailure(int reason) {
        if (callback != null) {
            callback.onAddServiceFailure(reason);
        }
    }

    public void setCallback(AddServiceCallback callback) {
        this.callback = callback;
    }

    public static interface AddServiceCallback {
        void onAddServiceSuccess();

        void onAddServiceFailure(int reason);
    }
}
