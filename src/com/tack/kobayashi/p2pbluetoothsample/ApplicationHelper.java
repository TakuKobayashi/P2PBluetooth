package com.tack.kobayashi.p2pbluetoothsample;

import android.content.Context;
import android.widget.Toast;

public class ApplicationHelper {
	public static void showToast(Context con, String message) {
		Toast toast = Toast.makeText(con, message, Toast.LENGTH_LONG);
		toast.show();
	}
}
