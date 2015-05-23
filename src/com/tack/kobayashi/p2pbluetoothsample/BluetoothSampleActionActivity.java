package com.tack.kobayashi.p2pbluetoothsample;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Set;

import com.tack.kobayashi.p2pbluetoothsample.BluetoothClientThread.ClientConnectListener;
import com.tack.kobayashi.p2pbluetoothsample.BluetoothConnectingThread.DataReceiveCallback;
import com.tack.kobayashi.p2pbluetoothsample.BluetoothServerThread.ServerConnectListener;

import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class BluetoothSampleActionActivity extends Activity {

	private static final String TAG = "P2PBluetooth_BluetoothSampleActionActivity";
	private BluetoothServerThread _serverThread;
	private BluetoothClientThread _clientThread;
	private BluetoothDevice _server;
	private BluetoothAdapter _bluetoothAdapter;
	private BluetoothConnectingThread _clientConnecting;
	private BluetoothConnectingThread _serverConnecting;
	private EditText _messageText;

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_sample_chatview);
		String address = this.getIntent().getStringExtra("address");
		_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		_server = _bluetoothAdapter.getRemoteDevice(address);
		if(_server == null){
			finish();
		}

		_messageText = (EditText) findViewById(R.id.messageSendEditText);
		Button sendButton = (Button) findViewById(R.id.sendButton);
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					_clientConnecting.sendMessage(_messageText.getText().toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	protected void onStart() {
		super.onResume();
		_serverThread = new BluetoothServerThread(this, _bluetoothAdapter);
		_serverThread.setOnConnectingListener(new ServerConnectListener() {
			@Override
			public void success(BluetoothConnectingThread connecting) {
				_serverConnecting = connecting;
				_serverConnecting.setOnDataReceiveCallback(new DataReceiveCallback() {
					@Override
					public void receive(byte[] data, int nbyte) {
						try {
							String message = new String(data, "UTF-8");
							ApplicationHelper.showToast(BluetoothSampleActionActivity.this, message);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		_serverThread.start();
		_clientThread = new BluetoothClientThread(BluetoothSampleActionActivity.this, _server, _bluetoothAdapter);
		_clientThread.setOnConnectingListener(new ClientConnectListener() {
			@Override
			public void success(BluetoothConnectingThread connecting) {
				_clientConnecting = connecting;
			}
		});
		_clientThread.start();
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	protected void onStop() {
		super.onPause();
		if(_serverThread.isAlive()){
			_serverThread.close();
		}
		if(_clientThread.isAlive()){
			_clientThread.close();
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------
}