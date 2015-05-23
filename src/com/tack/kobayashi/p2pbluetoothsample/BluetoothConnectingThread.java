package com.tack.kobayashi.p2pbluetoothsample;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Handler;
import android.os.HandlerThread;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

public class BluetoothConnectingThread extends HandlerThread {

	private static final String TAG = "P2PBluetooth_BluetoothServerCommon";
	private BluetoothSocket _socket = null;
	private Handler _handler;
	private boolean _cancelFlag = false;
	private DataReceiveCallback _dataReceiveCallback;
	private byte[] _buffer;
	private int _bytes;

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public BluetoothConnectingThread(Context context, BluetoothSocket socket, String prefix){
		super(prefix + BluetoothConnectingThread.class.toString());
		_socket = socket;
		_handler = new Handler();
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void sendMessage(String message) throws IOException {
		OutputStream os = _socket.getOutputStream();
		os.write(message.getBytes());
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void run() {
		_bytes = 0;
		_buffer = new byte[1024];
		while(true){
			try {
				InputStream in = _socket.getInputStream();
				_bytes = in.read(_buffer);
				if(_bytes != 0 && _dataReceiveCallback != null){
					Log.d(TAG, "bytes:" + _bytes);
					/*
					_handler.post(new Runnable() {
						@Override
						public void run() {
							_dataReceiveCallback.receive(_buffer, _bytes);
						}
					});
					*/
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void cancel(){
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

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------
}