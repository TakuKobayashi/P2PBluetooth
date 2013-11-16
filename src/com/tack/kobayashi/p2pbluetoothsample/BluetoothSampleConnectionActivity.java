package com.tack.kobayashi.p2pbluetoothsample;

import java.util.ArrayList;
import java.util.Set;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class BluetoothSampleConnectionActivity extends Activity {

	private static final String TAG = "P2PBluetooth_BluetoothSampleConnectionActivity";
	private SampleListAdapter _deviceListAdapter;
	private BluetoothBroadcastReceiver _receiver;
	private BluetoothAdapter _bluetoothAdapter;
	private static int REQUEST_ENABLE_BLUETOOTH = 1;
	private static int REQUEST_DISCOVERABLE_BLURTOOTH = 2;

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_sample_view);
		_deviceListAdapter = new SampleListAdapter(this);
		_receiver = new BluetoothBroadcastReceiver(_deviceListAdapter);
		_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		Button deviceSearchButton = (Button) findViewById(R.id.deviceSearchButton);
		deviceSearchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//インテントフィルターとBroadcastReceiverの登録
				IntentFilter filter = new IntentFilter();
				filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
				filter.addAction(BluetoothDevice.ACTION_FOUND);
				filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
				filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
				registerReceiver(_receiver, filter);
				if(_bluetoothAdapter.isDiscovering()){
					//検索中の場合は検出をキャンセルする
					_bluetoothAdapter.cancelDiscovery();
				}
				//デバイスを検索する
				//一定時間の間検出を行う
				_bluetoothAdapter.startDiscovery();
			}
		});
		Button deviceDescoverableButton = (Button) findViewById(R.id.deviceDescoverableButton);
		deviceDescoverableButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent discoverableOn = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableOn.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 600);
				startActivityForResult(discoverableOn, REQUEST_DISCOVERABLE_BLURTOOTH);
			}
		});
		ListView deviceList = (ListView) findViewById(R.id.DeviceList);
		deviceList.setAdapter(_deviceListAdapter);
		deviceList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View child, int position, long id) {
				BluetoothDevice device = (BluetoothDevice) _deviceListAdapter.getObject(position);
				Intent intent = new Intent(BluetoothSampleConnectionActivity.this, BluetoothSampleActionActivity.class);
				intent.putExtra(BluetoothSampleConnectionActivity.this.getString(R.string.mac_address_intent_key), device.getAddress());
				startActivity(intent);
			}
		});

		if(_bluetoothAdapter == null){
			finish();
		}else{
			if(_bluetoothAdapter.isEnabled()){
				//BluetoothがONだった場合の処理
				setBluetoothConnectionList(_bluetoothAdapter);
			}else{
				//強制的にbluetoothONにする場合
				//bta.enable();
				//OFFだった場合、ONにすることを促すダイアログを表示する画面に遷移
				Intent btOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(btOn, REQUEST_ENABLE_BLUETOOTH);
			}
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	private void setBluetoothConnectionList(BluetoothAdapter bta){
		Set<BluetoothDevice> devices = bta.getBondedDevices();
		ArrayList<String> list = new ArrayList<String>();
		if(devices.size() > 0){
			//接続履歴のあるデバイスが存在する
			for(BluetoothDevice device:devices){
				//接続履歴のあるデバイスの情報を順に取得してアダプタに詰める
				//getName()・・・デバイス名取得メソッド
				//getAddress()・・・デバイスのMACアドレス取得メソッド
				list.add(this.getString(R.string.device_name) + device.getName() + this.getString(R.string.new_line) + this.getString(R.string.device_mac_address) + device.getAddress() + this.getString(R.string.new_line) + this.getString(R.string.bond_status) +device.getBondState());
				_deviceListAdapter.addObject(device);
			}
			_deviceListAdapter.setStringList(list);
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_ENABLE_BLUETOOTH){
			if(resultCode == Activity.RESULT_OK){
				//BluetoothがONにされた場合の処理
				setBluetoothConnectionList(_bluetoothAdapter);
			}else{
				finish();
			}
		}
		if(requestCode == REQUEST_DISCOVERABLE_BLURTOOTH){
			if(resultCode == Activity.RESULT_CANCELED){
				ApplicationHelper.showToast(this, this.getString(R.string.show_bluetooth_on_message));
				finish();
			}else{
				ApplicationHelper.showToast(this, this.getString(R.string.show_bluetooth_on_success_message));
			}
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------
}