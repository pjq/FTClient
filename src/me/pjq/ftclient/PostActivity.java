
package me.pjq.ftclient;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import me.pjq.ftclient.api.BaseTask;
import me.pjq.ftclient.photo.CapturePhoto;

public class PostActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = PostActivity.class.getSimpleName();
    private MultiAutoCompleteTextView mUpdateStatusInputEditText;
    private Button mUpdateStatusUpdateButton;
    private ProgressBar mUpdateStatusProgressBar;
    private Button mCapturePhotoButton;
    private Button mClearInputButton;
    private TextView mStatusLengthTextView;
    private TextView mUpdateStatusResponseTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.post_status_layout);

        init();
    }

    private void init() {
        mFTPreference = FTPreference.getInstance(getApplicationContext());

        mUpdateStatusInputEditText = (MultiAutoCompleteTextView) findViewById(R.id.update_status_input_edittext);
        mUpdateStatusUpdateButton = (Button) findViewById(R.id.post_button);
        mUpdateStatusProgressBar = (ProgressBar) findViewById(R.id.update_status_progressbar);
        mCapturePhotoButton = (Button) findViewById(R.id.update_status_capture_button);
        mClearInputButton = (Button) findViewById(R.id.update_status_clear_input_button);
        mStatusLengthTextView = (TextView) findViewById(R.id.update_status_length_textview);
        mUpdateStatusResponseTextView = (TextView) findViewById(R.id.update_status_response_textview);

        mUpdateStatusUpdateButton.setOnClickListener(this);
        mCapturePhotoButton.setOnClickListener(this);
        mClearInputButton.setOnClickListener(this);

        mUpdateStatusInputEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateStatusLengthTextView();
            }
        });

        mUpdateStatusInputEditText.requestFocus();
        hideSoftKeyboard();

        initAutoCompleteTextViewAdapter();
    }

    private void initAutoCompleteTextViewAdapter() {
        setAutoCompleteTextViewAdapter(mFTPreference.getUserList());
    }

    private void setAutoCompleteTextViewAdapter(ArrayList<String> userList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line, userList);
        mUpdateStatusInputEditText.setAdapter(adapter);
        mUpdateStatusInputEditText.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    private void updateStatusLengthTextView() {
        int length = mUpdateStatusInputEditText.getText().toString().length();

        String text = "" + length + "/140";
        mStatusLengthTextView.setText(text);

        if (0 == length) {
            mClearInputButton.setVisibility(View.GONE);
        } else {
            mClearInputButton.setVisibility(View.VISIBLE);
        }

        if (140 == length) {
            showToast(getString(R.string.execeed_limit_length));
        }
    }

    public static final int REQUEST_CODE_REGISTER = 1001;
    public static final int REQUEST_CODE_CAPTURE_PHOTO = 10;
    public static final int REQUEST_CODE_GALLERY = REQUEST_CODE_CAPTURE_PHOTO + 1;

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.post_button: {
                hideSoftKeyboard();
                String message = mUpdateStatusInputEditText.getText().toString();
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

            case R.id.update_status_capture_button: {
                Intent intent = new Intent();
                intent.setClass(this, CapturePhoto.class);
                startActivityForResult(intent,
                        REQUEST_CODE_CAPTURE_PHOTO);

                break;
            }

            case R.id.update_status_clear_input_button: {
                mUpdateStatusInputEditText.setText("");
                break;
            }

            default:
                break;
        }

    }

    private void updateStatus(String userName, String password, String message) {
        // If the input is empty,just return.
        if (Utils.isEmpty(userName) || Utils.isEmpty(password) || Utils.isEmpty(message)) {
            showToast(getString(R.string.wrong_param));
            return;
        }

        me.pjq.ftclient.api.twitter.UpdateStatus updateStatus = new me.pjq.ftclient.api.twitter.UpdateStatus();
        updateStatus.setUserName(userName);
        updateStatus.setPassword(password);
        updateStatus.setMessage(message);

        runBaseTask(updateStatus);
    }

    @Override
    public void onTaskProgressUpdate(Object task, Integer... values) {
        super.onTaskProgressUpdate(task, values);
    }

    @Override
    public void onTaskStart(Object task) {
        super.onTaskStart(task);

        mUpdateStatusProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnTaskFinished(Object task) {
        super.OnTaskFinished(task);
        mUpdateStatusProgressBar.setVisibility(View.GONE);

        BaseTask baseTask = (BaseTask) task;

        int commandId = baseTask.getCommandId();

        switch (commandId) {
            case BaseTask.TWITTER_API_UPDATE_MESSAGE:
            case BaseTask.FACEBOOK_API_UPDATE_MESSAGE: {
                mUpdateStatusProgressBar.setVisibility(View.GONE);
                String response = baseTask.getResponse();
                addResposeTextViewText(response);
                Utils.log(TAG, response);
                break;
            }

            default:
                break;
        }

    }

    private void addResposeTextViewText(String response) {
        String text = response + '\n' + mUpdateStatusResponseTextView.getText();
        mUpdateStatusResponseTextView.setText(text + '\n');
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_CAPTURE_PHOTO: {
                if (resultCode == RESULT_OK && null != data) {
                    String result = data
                            .getStringExtra(CapturePhoto.REQUEST_EXTRAL_URL);
                    mUpdateStatusInputEditText.append(result);
                }

                break;
            }

            default:
                break;
        }
    }

}
