package com.tack.kobayashi.p2pbluetoothsample;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {

	private static final String TAG = "P2PBluetooth";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SampleListAdapter adapter = new SampleListAdapter(this);
		adapter.setStringList(this.getResources().getStringArray(R.array.titleList));
		ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View chile, int position,long id) {
				switch(position){
				case 0:
					Intent bluetoothIntent = new Intent(MainActivity.this, BluetoothSampleConnectionActivity.class);
					startActivity(bluetoothIntent);
					break;
				case 1:
					Intent P2PIntent = new Intent(MainActivity.this, P2PSampleActivity.class);
					startActivity(P2PIntent);
					break;
				default:
					break;
				}
			}
		});
	}
}
