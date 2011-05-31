package net.impjq.ftclient;

import net.impjq.ftclient.api.BaseTask;
import net.impjq.ftclient.api.BaseAsyncTask.TaskListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UpdateStatusActivity extends BaseActivity implements
		OnClickListener, TaskListener {
	public static final String TAG = UpdateStatusActivity.class.getSimpleName();

	private EditText mServerInputEditText;
	private EditText mUpdateStatusInputEditText;
	private Button mUpdateStatusUpdateButton;
	private ProgressBar mUpdateStatusProgressBar;
	private TextView mUpdateStatusResponseTextView;
	private EditText mUserNameInputEditText;
	private EditText mPasswordInputEditText;

	private FTPreference mFTPreference;
	private String mUserName;
	private String mPassword;
	private String mServerUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.update_status_layout);

		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		mFTPreference = FTPreference.getInstance(getApplicationContext());

		mServerInputEditText = (EditText) findViewById(R.id.update_status_server_input_edittext);
		mUpdateStatusInputEditText = (EditText) findViewById(R.id.update_status_input_edittext);
		mUpdateStatusUpdateButton = (Button) findViewById(R.id.update_status_update_button);
		mUpdateStatusProgressBar = (ProgressBar) findViewById(R.id.update_status_progressbar);
		mUpdateStatusResponseTextView = (TextView) findViewById(R.id.update_status_response_textview);
		mUserNameInputEditText = (EditText) findViewById(R.id.update_status_username_input_edittext);
		mPasswordInputEditText = (EditText) findViewById(R.id.update_status_password_input_edittext);

		mUpdateStatusUpdateButton.setOnClickListener(this);

		mUserNameInputEditText.setText(mFTPreference.getUserName());
		mPasswordInputEditText.setText(mFTPreference.getPassword());
		String serverUrl = mFTPreference.getServerUrl();
		if (Utils.isEmpty(serverUrl)) {
		} else {
			mServerInputEditText.setText(serverUrl);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		int id = v.getId();

		switch (id) {
		case R.id.update_status_update_button: {
			hideSoftKeyboard();
			String message = mUpdateStatusInputEditText.getText().toString();
			mUserName = mUserNameInputEditText.getText().toString();
			mPassword = mPasswordInputEditText.getText().toString();
			mServerUrl = mServerInputEditText.getText().toString();
			BaseTask.setServerUrl(mServerUrl);

			updateStatus(mUserName, mPassword, message);

			/*
			 * mFTPreference.storeUserName(mUserName);
			 * mFTPreference.storePassword(mPassword);
			 * mFTPreference.storeServerUrl(mServerUrl);
			 */
			mFTPreference.storeAll(mUserName, mPassword, mServerUrl);

			break;
		}

		default:
			break;
		}

	}

	private void updateStatus(String userName, String password, String message) {
		net.impjq.ftclient.api.twitter.UpdateStatus updateStatus = new net.impjq.ftclient.api.twitter.UpdateStatus();
		updateStatus.setUserName(userName);
		updateStatus.setPassword(password);
		updateStatus.setMessage(message);

		runBaseTask(updateStatus, this);
	}

	@Override
	public void OnTaskFinished(Object object) {
		// TODO Auto-generated method stub
		BaseTask baseTask = (BaseTask) object;

		int commandId = baseTask.getCommandId();

		switch (commandId) {
		case BaseTask.TWITTER_API_UPDATE_MESSAGE:
		case BaseTask.FACEBOOK_API_UPDATE_MESSAGE: {
			mUpdateStatusProgressBar.setVisibility(View.INVISIBLE);
			String response = baseTask.getResponse();
			mUpdateStatusResponseTextView.append(response + '\n');
			Utils.log(TAG, response);
			break;
		}

		default:
			break;
		}
	}

	@Override
	public void onTaskProgressUpdate(Object task, Integer... values) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTaskStart(Object task) {
		// TODO Auto-generated method stub
		BaseTask baseTask = (BaseTask) task;

		int commandId = baseTask.getCommandId();

		switch (commandId) {
		case BaseTask.TWITTER_API_UPDATE_MESSAGE:
		case BaseTask.FACEBOOK_API_UPDATE_MESSAGE: {
			mUpdateStatusProgressBar.setVisibility(View.VISIBLE);
			break;
		}

		default:
			break;
		}

	}

}
