
package me.pjq.ftclient;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import me.pjq.ftclient.api.BaseAsyncTask;
import me.pjq.ftclient.api.BaseAsyncTask.TaskListener;
import me.pjq.ftclient.api.BaseTask;

/**
 * The BaseActivity,you had better extends this base class if you need add an
 * new Activity.
 * 
 * @author pjq0274
 */
public class BaseActivity extends Activity implements TaskListener {
    public static final int DEFAULT_TOAST_SHOW_DURATION = 1000;
    protected String mUserName;
    protected String mPassword;
    protected String mServerUrl;
    protected FTPreference mFTPreference;
    protected Context mContext;

    private View mTitleBarLeftView;
    private Button mTitleBarRightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        setTheme(R.style.FTClienttheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        mFTPreference = FTPreference.getInstance(getApplicationContext());
        mUserName = mFTPreference.getUserName();
        mPassword = mFTPreference.getPassword();
        mServerUrl = mFTPreference.getServerUrl();
        BaseTask.setServerUrl(mServerUrl);
    }

    protected void initTitleBar() {

        mTitleBarLeftView = (View) findViewById(R.id.common_titlebar_left_layout);
        mTitleBarRightButton = (Button) findViewById(R.id.common_titlebar_right_button);

        if (enableTitleBarLeftView()) {
            if (null != mTitleBarLeftView) {
                mTitleBarLeftView.setVisibility(View.VISIBLE);
            }
        }

        if (null != mTitleBarLeftView) {
            mTitleBarLeftView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onTitleBarLeftViewClicked();
                }
            });
        }

        if (null != mTitleBarRightButton) {
            mTitleBarRightButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onTitleBarRightViewClicked();
                }
            });
        }
    }

    protected boolean enableTitleBarLeftView() {
        return false;
    }

    protected void onTitleBarLeftViewClicked() {

    }

    protected void onTitleBarRightViewClicked() {

    }

    protected void runBaseTask(BaseTask baseTask) {
        // TODO Auto-generated method stub
        BaseAsyncTask asyncTask = new BaseAsyncTask(baseTask, this);

        asyncTask.execute("");
    }

    public void showSoftKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    protected void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
        View view = getCurrentFocus();
        // view = getWindow().getDecorView();
        if (null == view) {
            view = getWindow().getDecorView();
        }

        if (null == view) {
            return;
        }

        IBinder token = view.getWindowToken();
        if (null == token) {
            return;
        }

        inputMethodManager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * Show a toast.
     * 
     * @param message
     * @param duration
     */
    protected void showToast(String message, int duration) {
        Toast.makeText(getApplicationContext(), message, duration).show();
    }

    /**
     * Show a toast with the duration:{@link #DEFAULT_TOAST_SHOW_DURATION}.
     * 
     * @param message
     */
    protected void showToast(String message) {
        showToast(message, DEFAULT_TOAST_SHOW_DURATION);
    }

    @Override
    public void onTaskStart(Object task) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTaskProgressUpdate(Object task, Integer... values) {
        // TODO Auto-generated method stub

    }

    @Override
    public void OnTaskFinished(Object task) {
        // TODO Auto-generated method stub

    }

    protected void getUserTimeline() {
        getUserTimeline(mUserName, mPassword);
    }

    protected void getUserTimeline(String userName, String password) {
        me.pjq.ftclient.api.twitter.GetUserTimeline getUserTimeline = new me.pjq.ftclient.api.twitter.GetUserTimeline();
        getUserTimeline.setUserName(userName);
        getUserTimeline.setPassword(password);

        runBaseTask(getUserTimeline);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindDrawables(findViewById(R.id.root_view));
        System.gc();
    }

    /**
     * Unbind all the drawables.
     * 
     * @param view
     */
    public void unbindDrawables(View view) {
        if (view != null) {
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
            }

            if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }
                ((ViewGroup) view).removeAllViews();
            }
        }
    }
}
