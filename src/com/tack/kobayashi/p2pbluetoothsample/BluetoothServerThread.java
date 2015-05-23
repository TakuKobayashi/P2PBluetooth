package com.tack.kobayashi.p2pbluetoothsample;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.HandlerThread;
import android.util.Log;

public class BluetoothServerThread extends HandlerThread {

	private static final String TAG = "P2PBluetooth_BluetoothServerThread";
	private Context _context;
	private BluetoothServerSocket _serverSocket = null;
	private ServerConnectListener _connectListener = null;
	private static final int STATE_NONE = 0;
	private static final int STATE_CONNECTING = 2;
	private static final int STATE_LISTEN = 1;
	private static final int STATE_CONNECTED = 3;
	private int _connectionStatus = STATE_NONE;

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public BluetoothServerThread(Context context, BluetoothAdapter bta){
		super(BluetoothServerThread.class.toString());
		//各種初期化
		UUID uuid = UUID.fromString(Config.STANDARD_BLUETOOTH_UUID);
		_context = context;

		//自デバイスのBluetoothサーバーソケットの取得
		try {
			_serverSocket = bta.listenUsingRfcommWithServiceRecord(context.getPackageName(), uuid);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void run(){
		BluetoothSocket receivedSocket = null;
		int count = 0;
		while(_connectionStatus != STATE_CONNECTED){
			count++;
			Log.d(TAG, "count:"+count);
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
				_connectionStatus = STATE_CONNECTED;
				if(this._connectListener != null){
					this._connectListener.success(connecting);
				}
				this.close();
				break;
			}
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void close(){
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
