package net.impjq.ftclient;

import net.impjq.ftclient.api.BaseAsyncTask;
import net.impjq.ftclient.api.BaseTask;
import net.impjq.ftclient.api.BaseAsyncTask.TaskListener;
import android.app.Activity;
import android.os.Bundle;

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

}
