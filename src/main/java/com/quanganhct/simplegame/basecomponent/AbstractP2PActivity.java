package com.quanganhct.simplegame.basecomponent;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.quanganhct.simplegame.R;
import com.quanganhct.simplegame.Util.MLog;
import com.quanganhct.simplegame.basecomponent.service.WifiDirectManager;

import java.util.Map;

/**
 * Created by quanganh.nguyen on 1/31/2016.
 */
public abstract class AbstractP2PActivity extends FragmentActivity implements
        WifiP2pManager.DnsSdTxtRecordListener, WifiP2pManager.DnsSdServiceResponseListener {

    protected WifiDirectManager wifiDirectManager;

    public int getMainLayoutResourceId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getMainLayoutResourceId() > 0) {
            this.setContentView(getMainLayoutResourceId());
        } else {
            this.setContentView(R.layout.content_main);
        }
        this.wifiDirectManager = WifiDirectManager.getInstance();
    }

    @Override
    public void onDnsSdTxtRecordAvailable(String fullDomainName, Map<String, String> txtRecordMap, WifiP2pDevice srcDevice) {
        MLog.w("DOMAIN", "" + fullDomainName);
        for (String k : txtRecordMap.keySet()) {
            MLog.w("RECORD", String.format("%s %s", k, txtRecordMap.get(k)));
        }
    }

    @Override
    public void onDnsSdServiceAvailable(String instanceName, String registrationType, WifiP2pDevice srcDevice) {
        MLog.w("DEVICE", srcDevice.deviceAddress);
        MLog.w("DEVICE", srcDevice.deviceName);
        MLog.w("DEVICE", srcDevice.primaryDeviceType);
        MLog.w("DEVICE", srcDevice.secondaryDeviceType);
    }
}
