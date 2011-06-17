
package net.impjq.ftclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.ArrayList;

/**
 * The FTClient Preference,used to store/get userName,Password,ServerUrl,etc.
 * 
 * @author pjq
 */
public class FTPreference {
    public static final String USER_NAME = "user_name";
    public static final String USER_PASSWORD = "password";
    public static final String SERVER_URL = "server_url";
    public static final String USER_LIST = "user_list";

    private static FTPreference mInstance;
    private static Context mContext;

    public FTPreference(Context context) {
        // TODO Auto-generated constructor stub
        if (null != context) {
            mContext = context;
        }

    }

    public static FTPreference getInstance(Context context) {
        if (null == mInstance) {
            mInstance = new FTPreference(context);
        }

        return mInstance;
    }

    /**
     * Store the UserName
     * 
     * @param userName
     */
    public void storeUserName(String userName) {
        store(USER_NAME, userName);
    }

    /**
     * Get the UserName
     * 
     * @return
     */
    public String getUserName() {
        return restore(USER_NAME, "");
    }

    /**
     * Store the Password.
     * 
     * @param password
     */
    public void storePassword(String password) {
        store(USER_PASSWORD, password);
    }

    /**
     * Get the Password.
     * 
     * @return
     */
    public String getPassword() {
        return restore(USER_PASSWORD, "");
    }

    /**
     * Store the Server Url.
     * 
     * @param serverUrl
     */
    public void storeServerUrl(String serverUrl) {
        store(SERVER_URL, serverUrl);
    }

    /**
     * Get the Server Url.
     * 
     * @return
     */
    public String getServerUrl() {
        return restore(SERVER_URL, "");
    }

    /**
     * Store all the values.
     * 
     * @param userName
     * @param password
     * @param serverUrl
     */
    public void storeAll(String userName, String password, String serverUrl) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        Editor editor = preferences.edit();

        if (!Utils.isEmpty(userName)) {
            editor.putString(USER_NAME, userName);
        }
        if (!Utils.isEmpty(password)) {
            editor.putString(USER_PASSWORD, password);
        }
        if (!Utils.isEmpty(serverUrl)) {
            editor.putString(SERVER_URL, serverUrl);
        }
        editor.commit();
    }

    private void store(String key, String value) {
        // If null just return.
        if (Utils.isEmpty(value)) {
            return;
        }

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Get the Preference.
     * 
     * @param key
     * @param defaultVal
     * @return
     */
    private String restore(String key, String defaultVal) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        return preferences.getString(key, defaultVal);
    }

    /**
     * Store the user list.And return all the user list.
     * 
     * @param userList
     */
    public synchronized ArrayList<String> storeUserList(ArrayList<String> userList) {
        ArrayList<String> currentList = getUserListWithoutAT();
        for (String str : userList) {
            if (!currentList.contains(str)) {
                currentList.add(str);
            }
        }

        store(USER_LIST, currentList.toString());
        return currentList;
    }

    /**
     * Get the user list.
     */
    public synchronized ArrayList<String> getUserListWithoutAT() {
        ArrayList<String> userList = new ArrayList<String>();

        String userStrings = restore(USER_LIST, "");

        userStrings = userStrings.replace(" ", "");
        userStrings = userStrings.replace("[", "");
        userStrings = userStrings.replace("]", "");
        String[] user = userStrings.split(",");

        for (String str : user) {
            if (!userList.contains(str)) {
                // Get two types.
                userList.add(str);
                // userList.add("@" + str);
            }
        }

        return userList;
    }

    /**
     * Get the user list.
     */
    public synchronized ArrayList<String> getUserList() {
        ArrayList<String> userList = new ArrayList<String>();

        String userStrings = restore(USER_LIST, "");

        userStrings = userStrings.replace("[", "");
        userStrings = userStrings.replace("]", "");
        String[] user = userStrings.split(",");

        for (String str : user) {
            if (!userList.contains(str)) {
                // Get two types.
                userList.add(str);
                userList.add("@" + str);
            }
        }

        return userList;
    }

}
