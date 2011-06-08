
package net.impjq.ftclient;

import net.impjq.ftclient.api.BaseTask;
import net.impjq.ftclient.api.BaseAsyncTask.TaskListener;
import net.impjq.photo.CapturePhoto;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UpdateStatusActivity extends BaseActivity implements
        OnClickListener, TaskListener, OnTouchListener, OnGestureListener {
    public static final String TAG = UpdateStatusActivity.class.getSimpleName();

    private EditText mServerInputEditText;
    private EditText mUpdateStatusInputEditText;
    private Button mUpdateStatusUpdateButton;
    private ProgressBar mUpdateStatusProgressBar;
    private TextView mUpdateStatusResponseTextView;
    private EditText mUserNameInputEditText;
    private EditText mPasswordInputEditText;
    private Button mClearResponseButton;
    private Button mGetUserTimelineButton;
    private Button mCapturePhotoButton;
    private Button mClearInputButton;
    private TextView mStatusLengthTextView;

    private FTPreference mFTPreference;
    private String mUserName;
    private String mPassword;
    private String mServerUrl;

    // GestureDetector
    private GestureDetector mGestureDetector;

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
        mClearResponseButton = (Button) findViewById(R.id.update_status_clear_response_button);
        mGetUserTimelineButton = (Button) findViewById(R.id.get_user_timeline_button);
        mCapturePhotoButton = (Button) findViewById(R.id.update_status_capture_button);
        mClearInputButton = (Button) findViewById(R.id.update_status_clear_input_button);
        mStatusLengthTextView = (TextView) findViewById(R.id.update_status_length_textview);

        mUpdateStatusUpdateButton.setOnClickListener(this);
        mClearResponseButton.setOnClickListener(this);
        mGetUserTimelineButton.setOnClickListener(this);
        mCapturePhotoButton.setOnClickListener(this);
        mClearInputButton.setOnClickListener(this);

        mUserNameInputEditText.setText(mFTPreference.getUserName());
        mPasswordInputEditText.setText(mFTPreference.getPassword());
        String serverUrl = mFTPreference.getServerUrl();
        if (Utils.isEmpty(serverUrl)) {
        } else {
            mServerInputEditText.setText(serverUrl);
        }

        mUpdateStatusResponseTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                setClearButtonVisible();

            }
        });

        mUpdateStatusInputEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                updateStatusLengthTextView();

            }
        });

        setClearButtonVisible();

        mUpdateStatusInputEditText.requestFocus();
        hideSoftKeyboard();

        // mUpdateStatusInputEditText.setOnTouchListener(this);
        // mUpdateStatusInputEditText.setLongClickable(true);
        // mGestureDetector = new GestureDetector(this);
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
        
        if (140==length) {
            showToast(getString(R.string.execeed_limit_length));
        }
    }

    private void setClearButtonVisible() {
        String text = mUpdateStatusResponseTextView.getText().toString();
        if (Utils.isEmpty(text)) {
            mClearResponseButton.setVisibility(View.GONE);
        } else {
            mClearResponseButton.setVisibility(View.VISIBLE);
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

            case R.id.update_status_clear_response_button: {
                mUpdateStatusResponseTextView.setText("");
                break;
            }

            case R.id.get_user_timeline_button: {
                mUpdateStatusResponseTextView
                        .setText(getString(R.string.get_user_timeline));
                hideSoftKeyboard();
                // String message =
                // mUpdateStatusInputEditText.getText().toString();
                mUserName = mUserNameInputEditText.getText().toString();
                mPassword = mPasswordInputEditText.getText().toString();
                mServerUrl = mServerInputEditText.getText().toString();
                BaseTask.setServerUrl(mServerUrl);

                getUserTimeline(mUserName, mPassword);
                mFTPreference.storeAll(mUserName, mPassword, mServerUrl);

                break;
            }

            case R.id.update_status_capture_button: {
                Intent intent = new Intent();
                intent.setClass(this, CapturePhoto.class);
                startActivityForResult(intent,
                        CapturePhoto.REQUEST_CODE_CAPTURE_PHOTO);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        // super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CapturePhoto.REQUEST_CODE_CAPTURE_PHOTO: {
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

    private void updateStatus(String userName, String password, String message) {
        net.impjq.ftclient.api.twitter.UpdateStatus updateStatus = new net.impjq.ftclient.api.twitter.UpdateStatus();
        updateStatus.setUserName(userName);
        updateStatus.setPassword(password);
        updateStatus.setMessage(message);

        runBaseTask(updateStatus, this);
    }

    private void getUserTimeline(String userName, String password) {
        net.impjq.ftclient.api.twitter.GetUserTimeline getUserTimeline = new net.impjq.ftclient.api.twitter.GetUserTimeline();
        getUserTimeline.setUserName(userName);
        getUserTimeline.setPassword(password);

        runBaseTask(getUserTimeline, this);
    }

    @Override
    public void OnTaskFinished(Object object) {
        // TODO Auto-generated method stub
        BaseTask baseTask = (BaseTask) object;

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

            case BaseTask.TWITTER_API_GET_USER_TIMELINE: {
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
            case BaseTask.FACEBOOK_API_UPDATE_MESSAGE:
            case BaseTask.TWITTER_API_GET_USER_TIMELINE: {
                mUpdateStatusProgressBar.setVisibility(View.VISIBLE);
                break;
            }

            default:
                break;
        }

    }

    private MyGestureDetector mGestureListener = new MyGestureDetector();

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    class MyGestureDetector extends SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {
            // TODO Auto-generated method stub
            // return super.onFling(e1, e2, velocityX, velocityY);

            Utils.log(TAG, "onFling");
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                // viewFlipper.setInAnimation(slideLeftIn);
                // viewFlipper.setOutAnimation(slideLeftOut);
                // viewFlipper.showNext();
                showToast("Left in");
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                // viewFlipper.setInAnimation(slideRightIn);
                // viewFlipper.setOutAnimation(slideRightOut);
                // viewFlipper.showPrevious();
                showToast("Right in");
            }
            return true;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        // showToast("onTouch");
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        // showToast("onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub
        showToast("onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        showToast("onSingleTapUp");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
            float distanceY) {
        // TODO Auto-generated method stub
        showToast("onScroll");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub
        showToast("onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY) {
        // TODO Auto-generated method stub

        showToast("onFling");
        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            // viewFlipper.setInAnimation(slideLeftIn);
            // viewFlipper.setOutAnimation(slideLeftOut);
            // viewFlipper.showNext();
            showToast("Left in");
        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            // viewFlipper.setInAnimation(slideRightIn);
            // viewFlipper.setOutAnimation(slideRightOut);
            // viewFlipper.showPrevious();
            showToast("Right in");
        }
        return false;
    }
}
