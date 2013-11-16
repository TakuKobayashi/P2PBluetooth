package com.tack.kobayashi.p2pbluetoothsample;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Set;
import java.util.UUID;

import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class BluetoothClientThread extends Thread {

	//UUIDの生成
	private static final String TAG = "P2PBluetooth_BluetoothClientThread";
	private Context _context;
	private BluetoothAdapter _clientAdapter;
	private ClientConnectListener _connectListener = null;
	private BluetoothSocket _socket = null;

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public BluetoothClientThread(Context context, BluetoothDevice device, BluetoothAdapter bta) {
		_clientAdapter = bta;
		UUID uuid = UUID.fromString(BluetoothConnectingThread.COMMON_UUID);
		try{
			//自デバイスのBluetoothクライアントソケットの取得
			_socket = device.createRfcommSocketToServiceRecord(uuid);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void run(){
		//接続要求を出す前に、検索処理を中断する。
		if(_clientAdapter.isDiscovering()){
			_clientAdapter.cancelDiscovery();
		}
		try{
			//サーバー側に接続要求
			_socket.connect();
		}catch(IOException e){
			e.printStackTrace();
			this.cancel();
			Log.d(TAG, "clientException");
		}
		BluetoothConnectingThread connecting = new BluetoothConnectingThread(_context, _socket, this.getClass().getName());
		connecting.start();
		if(this._connectListener != null){
			this._connectListener.success(connecting);
		}
	}

	public void cancel() {
		try {
			this._socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d(TAG,"clientCancel");
	}
	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * リスナーを追加する
	 */
	public void setOnConnectingListener(ClientConnectListener listener){
		this._connectListener = listener;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * リスナーを削除する
	 */
	public void removeListener(){
		this._connectListener = null;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public interface ClientConnectListener extends EventListener {

		public void success(BluetoothConnectingThread connecting);

	}
}
