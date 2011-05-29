package net.impjq.ftclient;

import android.util.Log;

public class Utils {
	public static final boolean DEBUG_ENABLE=true;
	
	public static void log(String TAG,String msg){
		if (DEBUG_ENABLE) {
			Log.i(TAG, msg);
		}			
	}

}
