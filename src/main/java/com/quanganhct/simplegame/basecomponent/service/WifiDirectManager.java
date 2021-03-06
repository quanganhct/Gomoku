package com.quanganhct.simplegame.basecomponent.service;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;

import com.quanganhct.simplegame.basecomponent.service.listeneres.AddDiscoveryServiceRequestListener;
import com.quanganhct.simplegame.basecomponent.service.listeneres.AdvertiseServiceListener;
import com.quanganhct.simplegame.basecomponent.service.listeneres.InitiatePeerConnectionListener;
import com.quanganhct.simplegame.basecomponent.service.listeneres.InitiateServiceDiscoveryListener;

import org.andengine.util.SocketUtils;

import java.net.ServerSocket;
import java.util.Map;

/**
 * Created by quanganh.nguyen on 1/31/2016.
 */
public class WifiDirectManager implements WifiP2pManager.ChannelListener {
    private static WifiDirectManager instance;
    private Context context;
    private WifiP2pManager wifiP2pManager;
    private IntentFilter intentFilter;
    private WifiP2pManager.Channel mChannel;

    private AddDiscoveryServiceRequestListener discoveryRequestListener;
    private AdvertiseServiceListener advertiseServiceListener;
    private InitiateServiceDiscoveryListener initiateServiceListener;
    private InitiatePeerConnectionListener initiatePeerConnectionListener;

    private WifiP2pDnsSdServiceInfo info;
    private WifiP2pDnsSdServiceRequest serviceRequest;
    private ServerSocket serverSocket;


    private WifiDirectManager() {

    }

    public void setAdvertiseServiceCallback(AdvertiseServiceListener.AdvertiseCallback callback) {
        this.advertiseServiceListener.setCallback(callback);
    }

    public void setAddServiceCallback(AddDiscoveryServiceRequestListener.AddServiceCallback callback) {
        this.discoveryRequestListener.setCallback(callback);
    }

    public void setInitiateServiceCallback(InitiateServiceDiscoveryListener.InitiateDiscoveryCallback callback) {
        this.initiateServiceListener.setCallback(callback);
    }

    public void setInitiatePeerConnectionCallback(InitiatePeerConnectionListener.InitiatePeerConnectionCallback callback){
        this.initiatePeerConnectionListener.setCallback(callback);
    }

    public static WifiDirectManager getInstance() {
        if (instance == null) {
            instance = new WifiDirectManager();
        }
        return instance;
    }

    public void initialize(Context context) {
        this.context = context;
        this.intentFilter = new IntentFilter();
        this.wifiP2pManager = (WifiP2pManager) this.context.getSystemService(Activity.WIFI_P2P_SERVICE);
        this.intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        this.intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        this.intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        this.intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        this.advertiseServiceListener = new AdvertiseServiceListener();
        this.discoveryRequestListener = new AddDiscoveryServiceRequestListener();
        this.initiateServiceListener = new InitiateServiceDiscoveryListener();
        this.initiatePeerConnectionListener = new InitiatePeerConnectionListener();
    }

    public void startChannel(Context context) {
        this.mChannel = this.wifiP2pManager.initialize(context, context.getMainLooper(), this);
    }

    @Override
    public void onChannelDisconnected() {
        this.mChannel = null;
    }

    public void startAdvertisingService(Context context, Map<String, String> records) {
        if (this.mChannel == null) {
            this.startChannel(context);
        }
        this.info = WifiP2pDnsSdServiceInfo.newInstance("_gomoku", "_presence._tcp", records);
        this.wifiP2pManager.addLocalService(mChannel, info, advertiseServiceListener);
    }

    public void startSearchingService(Context context, WifiP2pManager.DnsSdServiceResponseListener listener1, WifiP2pManager.DnsSdTxtRecordListener listener2) {
        if (this.mChannel == null) {
            this.startChannel(context);
        }
        this.wifiP2pManager.setDnsSdResponseListeners(mChannel, listener1, listener2);
        this.serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        this.wifiP2pManager.addServiceRequest(mChannel, serviceRequest, discoveryRequestListener);
        this.wifiP2pManager.discoverServices(mChannel, initiateServiceListener);
    }

    public void stopAdvertisingService() {
        if (mChannel != null && info != null) {
            this.wifiP2pManager.removeLocalService(mChannel, info, null);
        }
    }

    public void stopSearchingService() {
        if (mChannel != null && serviceRequest != null) {
            this.wifiP2pManager.removeServiceRequest(mChannel, serviceRequest, null);
        }
    }

    public void connectToServerDevice(WifiP2pDevice server){
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = server.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        this.wifiP2pManager.connect(mChannel, config, initiatePeerConnectionListener);
        //TODO: need to set up broadcast listener for reponse from peer
    }
}
