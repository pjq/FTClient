
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.update_status_layout);

        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        mServerInputEditText = (EditText) findViewById(R.id.update_status_server_input_edittext);
        mUpdateStatusInputEditText = (EditText) findViewById(R.id.update_status_input_edittext);
        mUpdateStatusUpdateButton = (Button) findViewById(R.id.update_status_update_button);
        mUpdateStatusProgressBar = (ProgressBar) findViewById(R.id.update_status_progressbar);
        mUpdateStatusResponseTextView = (TextView) findViewById(R.id.update_status_response_textview);

        mUpdateStatusUpdateButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        int id = v.getId();

        switch (id) {
            case R.id.update_status_update_button: {
                String message = mUpdateStatusInputEditText.getText().toString();
                String userName = "pjq";
                String password = "123";
                String server=mServerInputEditText.getText().toString();
                BaseTask.setServerUrl(server);

                updateStatus(userName, password, message);

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
