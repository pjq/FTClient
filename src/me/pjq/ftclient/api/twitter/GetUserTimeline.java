
package me.pjq.ftclient.api.twitter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Build;

import me.pjq.ftclient.CommonParamString;
import me.pjq.ftclient.Utils;
import me.pjq.ftclient.api.BaseTask;
import me.pjq.httpclient.HttpClientHelper;


public class GetUserTimeline extends BaseTask {
    private static final String TAG = GetUserTimeline.class.getSimpleName();
    private ArrayList<String> mUserList = new ArrayList<String>();;

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
            mUserList = parserUserList();
            // Utils.log(TAG, mUserList.toString());
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            mResponse = e.getMessage();
        }
    }

    Pattern pattern = Pattern.compile("\\[.*/.*\\]");

    private ArrayList<String> parserUserList() {
        Utils.log(TAG, "parserUserList");

        Matcher matcher = pattern.matcher(mResponse);
        while (matcher.find()) {
            String str = matcher.group();
            str = str.replace("[", "");
            str = str.replace("]", "");
            str = str.split("/")[1];

            if (!mUserList.contains(str)) {
                mUserList.add(str);
                // mUserList.add("@"+str);
                // Utils.log(TAG, str);
            }
        }

        return mUserList;
    }

    public ArrayList<String> getUserList() {
        return mUserList;
    }
}
