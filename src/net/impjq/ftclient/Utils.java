package net.impjq.ftclient;

import java.util.Date;

import android.util.Log;

public class Utils {
	public static final boolean DEBUG_ENABLE=true;
	
	public static void log(String TAG,String msg){
		if (DEBUG_ENABLE) {
			Log.i(TAG, msg);
		}			
	}
	
	public static String getTime(){
		Date date=new Date();
		String time=date.toLocaleString();
		return time;
	}

}
