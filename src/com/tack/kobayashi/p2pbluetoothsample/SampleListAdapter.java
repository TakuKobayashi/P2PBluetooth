package com.tack.kobayashi.p2pbluetoothsample;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SampleListAdapter extends BaseAdapter {

	private Activity _activity;
	private String[] _sampleTitleList = {"BluetoothSample", "P2PSample"};

	public SampleListAdapter(Activity act){
		_activity = act;
	}

	@Override
	public int getCount() {
		return _sampleTitleList.length;
	}

	@Override
	public Object getItem(int position) {
		return _sampleTitleList[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = new TextView(_activity);
		textView.setText(_sampleTitleList[position]);
		textView.setTextSize(20);
		textView.setMinHeight(100);
		return textView;
	}

}
