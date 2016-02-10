package com.quanganhct.simplegame.test;

import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.quanganhct.simplegame.R;
import com.quanganhct.simplegame.Util.MLog;
import com.quanganhct.simplegame.basecomponent.AbstractP2PActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by quanganh.nguyen on 1/31/2016.
 */
public class TestNSDActivity extends AbstractP2PActivity implements View.OnClickListener {
    private TextView textView, txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.findViewById(R.id.btn_server).setOnClickListener(this);
        this.findViewById(R.id.btn_client).setOnClickListener(this);
        this.textView = (TextView) findViewById(R.id.textView);
        this.txtMessage = (TextView) findViewById(R.id.txtMessage);
    }

    @Override
    public void onClick(View v) {
        Map<String, String> map = new HashMap<>();
        map.put("message", "HELLO");
        if (v.getId() == R.id.btn_server) {
            this.wifiDirectManager.startAdvertisingService(this, map);
//            this.wifiDirectManager.startSearchingService(this, this, this);
            MLog.w("server", "click");
        } else if (v.getId() == R.id.btn_client) {
            this.wifiDirectManager.startSearchingService(this, this, this);
            MLog.w("client", "click");
        }
    }

    @Override
    public void onDnsSdTxtRecordAvailable(String fullDomainName, Map<String, String> txtRecordMap, WifiP2pDevice srcDevice) {
        String result = "";
        MLog.w("response", "here");
        for (String s : txtRecordMap.keySet()) {
            result += String.format("%s %s \n", s, txtRecordMap.get(s));
        }
        this.txtMessage.setText(result);
    }

    @Override
    public void onDnsSdServiceAvailable(String instanceName, String registrationType, WifiP2pDevice srcDevice) {
        String s = String.format("%s \n %s \n %s \n %s", srcDevice.deviceAddress, srcDevice.deviceName,
                srcDevice.primaryDeviceType, srcDevice.secondaryDeviceType);
        MLog.w("response", "here");
        this.textView.setText(s);
    }
}
