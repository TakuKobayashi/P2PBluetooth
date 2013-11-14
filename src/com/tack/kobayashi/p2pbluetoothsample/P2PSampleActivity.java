package com.tack.kobayashi.p2pbluetoothsample;

import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.view.Menu;
import android.widget.ListView;

public class P2PSampleActivity extends Activity {

	private static final String TAG = "P2PBluetooth";
	private WifiP2pManager _wifiP2PManager;
	private Channel _channel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.p2p_sample_view);

	}
}
