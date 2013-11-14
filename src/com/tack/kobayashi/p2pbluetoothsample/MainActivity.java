package com.tack.kobayashi.p2pbluetoothsample;

import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {

	private static final String TAG = "P2PBluetooth";
	private WifiP2pManager _wifiP2PManager;
	private Channel _channel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SampleListAdapter adapter = new SampleListAdapter(this);
		ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View chile, int position,long id) {
				switch(position){
				case 0:

					break;
				case 1:
					Intent intent = new Intent(MainActivity.this, P2PSampleActivity.class);
					startActivity(intent);
					break;
				default:
					break;
				}
			}
		});
	}
}
