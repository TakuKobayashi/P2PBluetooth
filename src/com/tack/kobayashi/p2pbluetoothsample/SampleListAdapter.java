package com.tack.kobayashi.p2pbluetoothsample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SampleListAdapter extends BaseAdapter {

	private Activity _activity;
	private ArrayList<String> _sampleTitleList;
	private ArrayList<Object> _contentObject;

	public SampleListAdapter(Activity act){
		_activity = act;
		_sampleTitleList = new ArrayList<String>();
		_contentObject = new ArrayList<Object>();
	}

	public void setStringList(String[] strings){
		_sampleTitleList.clear();
		_sampleTitleList.addAll(Arrays.asList(strings));
		this.notifyDataSetChanged();
	}

	public void setStringList(List<String> stringList){
		_sampleTitleList.clear();
		_sampleTitleList.addAll(stringList);
		this.notifyDataSetChanged();
	}

	public void addStringList(String[] strings){
		_sampleTitleList.addAll(Arrays.asList(strings));
		this.notifyDataSetChanged();
	}

	public void addStringList(List<String> stringList){
		_sampleTitleList.addAll(stringList);
		this.notifyDataSetChanged();
	}

	public void addString(String string){
		_sampleTitleList.add(string);
		this.notifyDataSetChanged();
	}

	public void addObject(Object obj){
		_contentObject.add(obj);
	}

	public Object getObject(int position){
		return _contentObject.get(position);
	}

	@Override
	public int getCount() {
		return _sampleTitleList.size();
	}

	@Override
	public Object getItem(int position) {
		return _sampleTitleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = _activity.getLayoutInflater().inflate(R.layout.adapter_cell, null);
		}
		TextView textView = (TextView) convertView.findViewById(R.id.adapterCellText);
		textView.setText(_sampleTitleList.get(position));
		return convertView;
	}

}
