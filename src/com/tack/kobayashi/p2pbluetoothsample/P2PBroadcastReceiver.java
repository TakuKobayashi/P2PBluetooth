package com.tack.kobayashi.p2pbluetoothsample;

import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

public class P2PBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = "P2PBluetooth_P2PBroadcastReceiver";
	private WifiP2pManager _wifiP2PManager;
	private Channel _channel;
	private Activity _activity;

	public P2PBroadcastReceiver(WifiP2pManager manager, Channel channel, Activity act){
		_wifiP2PManager = manager;
		_channel = channel;
		_activity = act;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.d(TAG, "P2Paction:"+action);
		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			// Check to see if Wi-Fi is enabled and notify appropriate activity
		} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			// Call WifiP2pManager.requestPeers() to get a list of current peers
		} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
			// Respond to new connection or disconnections
		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
			// Respond to this device's wifi state changing
		}

	}

}
