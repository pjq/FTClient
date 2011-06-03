
package net.impjq.ftclient;

import net.impjq.ftclient.api.BaseAsyncTask;
import net.impjq.ftclient.api.BaseTask;
import net.impjq.ftclient.api.BaseAsyncTask.TaskListener;
import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * The BaseActivity,you had better extends this base class if you need add an
 * new Activity.
 * 
 * @author pjq0274
 */
public class BaseActivity extends Activity {
    public static final int DEFAULT_TOAST_SHOW_DURATION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    protected void runBaseTask(BaseTask baseTask, TaskListener listener) {
        // TODO Auto-generated method stub
        BaseAsyncTask asyncTask = new BaseAsyncTask(baseTask, listener);

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
}
