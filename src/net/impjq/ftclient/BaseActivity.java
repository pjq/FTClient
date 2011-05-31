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

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	protected void runBaseTask(BaseTask baseTask, TaskListener listener) {
		// TODO Auto-generated method stub
		BaseAsyncTask asyncTask = new BaseAsyncTask(baseTask,listener);

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
}
