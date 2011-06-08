package net.impjq.ftclient.api.twitter;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Build;

import net.impjq.ftclient.CommonParamString;
import net.impjq.ftclient.Utils;
import net.impjq.ftclient.api.BaseTask;
import net.impjq.httpclient.HttpClientHelper;

public class GetUserTimeline extends BaseTask {
	private static final String TAG = GetUserTimeline.class.getSimpleName();

	public GetUserTimeline() {
		// TODO Auto-generated constructor stub
		mServiceType = SERVICE_TYPE_TWITTER;// Twitter
		mCommand = "GetUserTimeline";
		mCommandId = TWITTER_API_GET_USER_TIMELINE;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		HashMap<String, String> hashMap = new HashMap<String, String>();

		hashMap.put(CommonParamString.PARAM_MACHINE, Build.MODEL);
        // hashMap.put(CommonParamString.PARAM_USERNAME, getUserName());
        // hashMap.put(CommonParamString.PARAM_PASSWORD, getPassword());
		// hashMap.put(CommonParamString.PARAM_MESSAGE, getMessage());
		hashMap.put(CommonParamString.PARAM_TIME, Utils.getTime());
		try {
			InputStream inputStream = executeRequest(hashMap, true);

			String result = HttpClientHelper.getInstance().readFromInputStream(
					inputStream);
			Utils.log(TAG, "result=" + result);
			mResponse = result;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			mResponse = e.getMessage();
		}

	}

}
