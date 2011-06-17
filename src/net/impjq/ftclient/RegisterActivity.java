
package net.impjq.ftclient;

import net.impjq.ftclient.account.Register;
import net.impjq.ftclient.api.BaseTask;
import net.impjq.ftclient.api.BaseAsyncTask.TaskListener;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RegisterActivity extends BaseActivity implements TaskListener, OnClickListener {
    public static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText mUserNameInputEditText;
    private EditText mPasswordInputEditText;
    private EditText mEmailInputEditText;
    private EditText mTwitterUserNameInputEditText;
    private EditText mTwitterPasswordInputEditText;
    private Button mRegisterButton;
    private ProgressBar mProgressBar;
    private TextView mResponseTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register_layout);

        // String userName = "1234";
        // String password = "123";
        // String email = "pengjianqing@gmail.com";
        // String twitterUserName = "twitter username";
        // String twitterPassword = "twitter password";
        //
        // register(userName, password, email, twitterUserName,
        // twitterPassword);
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        mUserNameInputEditText = (EditText) findViewById(R.id.register_username_input_edittext);
        mPasswordInputEditText = (EditText) findViewById(R.id.register_password_input_edittext);
        mEmailInputEditText = (EditText) findViewById(R.id.register_email_input_edittext);
        mTwitterPasswordInputEditText = (EditText) findViewById(R.id.register_twitter_password_input_edittext);
        mTwitterUserNameInputEditText = (EditText) findViewById(R.id.register_username_input_edittext);
        mRegisterButton = (Button) findViewById(R.id.register_button);
        mProgressBar = (ProgressBar) findViewById(R.id.register_progress_bar);
        mResponseTextView = (TextView) findViewById(R.id.register_response_textview);

        mRegisterButton.setOnClickListener(this);

    }

    private void register(String userName, String password, String email, String twitterUserName,
            String twitterPassword) {
        Register register = new Register();
        register.setEmail(email);
        register.setPassword(password);
        register.setUserName(userName);
        register.setTwitterUserName(twitterUserName);
        register.setTwitterPassword(twitterPassword);
        runBaseTask(register, this);

    }

    @Override
    public void onTaskStart(Object task) {
        // TODO Auto-generated method stub

        mProgressBar.setVisibility(View.VISIBLE);
        mRegisterButton.setEnabled(false);

    }

    @Override
    public void onTaskProgressUpdate(Object task, Integer... values) {
        // TODO Auto-generated method stub

    }

    @Override
    public void OnTaskFinished(Object task) {
        // TODO Auto-generated method stub
        BaseTask baseTask = (BaseTask) task;
        String response = baseTask.getResponse();

        mProgressBar.setVisibility(View.GONE);
        mRegisterButton.setEnabled(true);

        if (response.contains("success")) {
            showToast(response);
            finish();
        } else {
            // showToast(response);
        }
        mResponseTextView.setText(response);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();

        switch (id) {
            case R.id.register_button: {
                String userName = mUserNameInputEditText.getText().toString();
                String password = mPasswordInputEditText.getText().toString();
                String email = mEmailInputEditText.getText().toString();
                String twitterUserName = mTwitterUserNameInputEditText.getText().toString();
                String twitterPassword = mTwitterPasswordInputEditText.getText().toString();

                if (Utils.isEmpty(userName) || Utils.isEmpty(password)
                        || Utils.isEmpty(twitterPassword) || Utils.isEmpty(twitterUserName)) {
                    showToast(getString(R.string.wrong_param));
                } else {
                    register(userName, password, email, twitterUserName, twitterPassword);
                }

                break;
            }

            default:
                break;
        }

    }

}
