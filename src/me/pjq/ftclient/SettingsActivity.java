
package me.pjq.ftclient;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import me.pjq.ftclient.api.BaseAsyncTask.TaskListener;

public class SettingsActivity extends BaseActivity implements
        OnClickListener, TaskListener {
    public static final String TAG = SettingsActivity.class.getSimpleName();

    private EditText mServerInputEditText;
    private Button mCheckSettingsButton;
    private EditText mUserNameInputEditText;
    private EditText mPasswordInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Utils.log(TAG, "onCreate");

        setContentView(R.layout.settings_layout);

        init();
    }

    private void init() {
        mServerInputEditText = (EditText) findViewById(R.id.update_status_server_input_edittext);
        mUserNameInputEditText = (EditText) findViewById(R.id.update_status_username_input_edittext);
        mPasswordInputEditText = (EditText) findViewById(R.id.update_status_password_input_edittext);

        mUserNameInputEditText.setText(mUserName);
        mPasswordInputEditText.setText(mPassword);
        if (Utils.isEmpty(mServerUrl)) {
        } else {
            mServerInputEditText.setText(mServerUrl);
        }

        mCheckSettingsButton = (Button) findViewById(R.id.settings_check_button);
        mCheckSettingsButton.setOnClickListener(this);

        hideSoftKeyboard();

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.settings_check_button: {
                mUserName = mUserNameInputEditText.getText().toString();
                mPassword = mPasswordInputEditText.getText().toString();
                mServerUrl = mServerInputEditText.getText().toString();

                mFTPreference.storeAll(mUserName, mPassword, mServerUrl);
                break;
            }

            default:
                break;
        }

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

}
