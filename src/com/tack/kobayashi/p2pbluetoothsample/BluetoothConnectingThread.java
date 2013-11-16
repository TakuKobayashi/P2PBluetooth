package com.tack.kobayashi.p2pbluetoothsample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Set;
import java.util.UUID;

import com.tack.kobayashi.p2pbluetoothsample.BluetoothClientThread.ClientConnectListener;

import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class BluetoothConnectingThread extends HandlerThread {

	public static final String COMMON_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	private static final String TAG = "P2PBluetooth_BluetoothServerCommon";
	private BluetoothSocket _socket = null;
	private Handler _handler;
	private boolean _cancelFlag = false;
	private DataReceiveCallback _dataReceiveCallback;
	private byte[] _buffer;
	private int _bytes;

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public BluetoothConnectingThread(Context context, BluetoothSocket socket, String name){
		super(name);
		_socket = socket;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void sendMessage(String message) throws IOException {
		OutputStream os = _socket.getOutputStream();
		os.write(message.getBytes());
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void run() {
		_handler = new Handler(this.getLooper());
		_bytes = 0;
		_buffer = new byte[1024];
		while(_cancelFlag){
			try {
				InputStream in = _socket.getInputStream();
				_bytes = in.read(_buffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(_bytes != 0 && _dataReceiveCallback != null){
				Log.d(TAG, "bytes:" + _bytes);
				_handler.post(new Runnable() {
					@Override
					public void run() {
						_dataReceiveCallback.receive(_buffer, _bytes);
					}
				});
			}
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void cancel(){
		_cancelFlag = true;
		try {
			_socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void setOnDataReceiveCallback(DataReceiveCallback callback){
		this._dataReceiveCallback = callback;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void removeCallback(){
		this._dataReceiveCallback = null;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public interface DataReceiveCallback{
		public void receive(byte[] data, int nbyte);
	}
}