package com.tack.kobayashi.p2pbluetoothsample;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

public class BluetoothServerThread extends Thread {

	private static final String TAG = "P2PBluetooth_BluetoothServerThread";
	private Context _context;
	private BluetoothServerSocket _serverSocket = null;
	private BluetoothAdapter _serverAdapter;
	private ServerConnectListener _connectListener = null;

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public BluetoothServerThread(Context context, BluetoothAdapter bta){
		//各種初期化
		UUID uuid = UUID.fromString(BluetoothConnectingThread.COMMON_UUID);
		_serverAdapter = bta;
		_context = context;

		//自デバイスのBluetoothサーバーソケットの取得
		try {
			_serverSocket = _serverAdapter.listenUsingRfcommWithServiceRecord(context.getPackageName(), uuid);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void run(){
		BluetoothSocket receivedSocket = null;
		while(true){
			try{
				//クライアント側からの接続要求待ち。ソケットが返される。
				receivedSocket = _serverSocket.accept();
			}catch(IOException e){
				Log.d(TAG, "serverException");
				e.printStackTrace();
				break;
			}
			if(receivedSocket != null){
				BluetoothConnectingThread connecting = new BluetoothConnectingThread(_context, receivedSocket, this.getClass().getName());
				connecting.start();
				if(this._connectListener != null){
					this._connectListener.success(connecting);
				}
				break;
			}
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void cancel(){
		try {
			_serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "searverCancel");
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void setOnConnectingListener(ServerConnectListener listener){
		this._connectListener = listener;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void removeListener(){
		this._connectListener = null;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public interface ServerConnectListener{
		public void success(BluetoothConnectingThread connecting);
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------
}
