package com.tack.kobayashi.p2pbluetoothsample;

import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

public class P2PSampleActivity extends Activity {

	private static final String TAG = "P2PBluetooth_P2PSampleActivity";
	private IntentFilter _intentFilter;
	private WifiP2pManager _wifiP2PManager;
	private Channel _channel;
	private P2PBroadcastReceiver _receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.p2p_sample_view);

		_intentFilter = new IntentFilter();
		_intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		_intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		_intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		_intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		_wifiP2PManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		//channelは、P2P機能を利用するのに必要なインスタンス
		_channel = _wifiP2PManager.initialize(this, this.getMainLooper(), new ChannelListener() {

			@Override
			public void onChannelDisconnected() {
				Log.d(TAG, "connect_kill");
				ApplicationHelper.showToast(P2PSampleActivity.this, "接続が切れちゃったよ～");
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		_receiver = new P2PBroadcastReceiver(_wifiP2PManager, _channel, this);
		this.registerReceiver(_receiver, _intentFilter);
	}
}
