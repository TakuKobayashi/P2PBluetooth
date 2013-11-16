package com.tack.kobayashi.p2pbluetoothsample;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BluetoothBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = "P2PBluetooth_BlueToothBroadcastReceiver";
	private SampleListAdapter _listAdapter;

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public BluetoothBroadcastReceiver(SampleListAdapter sla){
		_listAdapter = sla;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		BluetoothDevice foundDevice;
		if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
			ApplicationHelper.showToast(context, context.getString(R.string.start_scan_message));
		}
		if(BluetoothDevice.ACTION_FOUND.equals(action)){
			//デバイスが検出された
			foundDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			if(foundDevice.getName() != null){
				if(foundDevice.getBondState() != BluetoothDevice.BOND_BONDED){
					//接続したことのないデバイスのみアダプタに詰める
					_listAdapter.addString(foundDevice.getName() + context.getString(R.string.new_line) + foundDevice.getAddress() + context.getString(R.string.new_line) + context.getString(R.string.bond_status) +foundDevice.getBondState());
					_listAdapter.addObject(foundDevice);
				}
			}
		}
		if(BluetoothDevice.ACTION_NAME_CHANGED.equals(action)){
			//名前が検出された
			foundDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			if(foundDevice.getBondState() != BluetoothDevice.BOND_BONDED){
				//接続したことのないデバイスのみアダプタに詰める
				_listAdapter.addString(foundDevice.getName() + context.getString(R.string.new_line) + foundDevice.getAddress() + context.getString(R.string.new_line) + context.getString(R.string.bond_status) +foundDevice.getBondState());
				_listAdapter.addObject(foundDevice);
			}
		}
		if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
			ApplicationHelper.showToast(context, context.getString(R.string.finish_scan_message));
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------------------------------------------
}
