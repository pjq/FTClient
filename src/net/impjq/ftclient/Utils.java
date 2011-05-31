package net.impjq.ftclient;

import java.util.Date;

import android.util.Log;

public class Utils {
	public static final boolean DEBUG_ENABLE = true;

	public static void log(String TAG, String msg) {
		if (DEBUG_ENABLE && null != msg) {
			Log.i(TAG, msg);
		}
	}

	public static String getTime() {
		Date date = new Date();
		String time = date.toLocaleString();
		return time;
	}

	/**
	 * Check the string is empty
	 * 
	 * @param string
	 * @return true if the string is null or "".
	 */
	public static boolean isEmpty(String string) {
		boolean empty = false;

		if (null == string || (null != string && string.equals(""))) {
			empty = true;
		}

		return empty;
	}

}
