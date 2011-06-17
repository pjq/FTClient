
package net.impjq.ftclient.account;

import android.os.Build;

import java.io.InputStream;
import java.util.HashMap;

import net.impjq.ftclient.CommonParamString;
import net.impjq.ftclient.Utils;
import net.impjq.ftclient.api.BaseTask;
import net.impjq.httpclient.HttpClientHelper;

/**
 * Register the FTClient with UserName,Password,Email,and Twitter
 * UserName,Twitter Password.
 * 
 * @author pjq0274
 */
public class Register extends BaseTask {
    public static final String TAG = Register.class.getSimpleName();

    private String mTwitterUserName;
    private String mTwitterPassword;
    private String mEmail;

    public void setTwitterUserName(String twitterUserName) {
        mTwitterUserName = twitterUserName;
    }

    public void setTwitterPassword(String twitterPassword) {
        mTwitterPassword = twitterPassword;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public Register() {
        // TODO Auto-generated constructor stub
        mServiceType = SERVICE_TYPE_ACCOUNT;// Twitter
        mCommand = "Register";
        mCommandId = ACCOUNT_REGISTER;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

        HashMap<String, String> hashMap = new HashMap<String, String>();

        hashMap.put(CommonParamString.PARAM_MACHINE, Build.MODEL);
        // hashMap.put(CommonParamString.PARAM_USERNAME, getUserName());
        // hashMap.put(CommonParamString.PARAM_PASSWORD, getPassword());
        hashMap.put(CommonParamString.PARAM_USERNAME, getUserName());
        hashMap.put(CommonParamString.PARAM_PASSWORD, getPassword());
        hashMap.put(CommonParamString.PARAM_EMAIL, mEmail);
        hashMap.put(CommonParamString.PARAM_TWITTER_USER_NAME, mTwitterUserName);
        hashMap.put(CommonParamString.PARAM_TWITTER_USER_PASSWORD, mTwitterPassword);
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
