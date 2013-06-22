
package me.pjq.ftclient;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import me.pjq.ftclient.api.BaseAsyncTask.TaskListener;
import me.pjq.ftclient.api.BaseTask;
import me.pjq.ftclient.api.twitter.GetUserTimeline;

import java.util.ArrayList;

public class HomeTimelineActivity extends BaseActivity implements
        OnClickListener, TaskListener, OnTouchListener, OnGestureListener {
    public static final String TAG = HomeTimelineActivity.class.getSimpleName();

    private ProgressBar mUpdateStatusProgressBar;
    private TextView mUpdateStatusResponseTextView;
    private Button mClearResponseButton;
    private Button mGetUserTimelineButton;
    
    // GestureDetector
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.log(TAG, "onCreate");

        setContentView(R.layout.hometimeline_layout);

        init();
    }

    private void init() {
        mUpdateStatusProgressBar = (ProgressBar) findViewById(R.id.update_status_progressbar);
        mUpdateStatusResponseTextView = (TextView) findViewById(R.id.update_status_response_textview);
        mClearResponseButton = (Button) findViewById(R.id.update_status_clear_response_button);
        mGetUserTimelineButton = (Button) findViewById(R.id.get_user_timeline_button);
        mClearResponseButton.setOnClickListener(this);
        mGetUserTimelineButton.setOnClickListener(this);

        mUpdateStatusResponseTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                setClearButtonVisible();

            }
        });

        setClearButtonVisible();

        // When start this activity,auto get the timeline.
        getUserTimeline();
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

        int id = v.getId();

        switch (id) {
            case R.id.update_status_clear_response_button: {
                mUpdateStatusResponseTextView.setText("");
                break;
            }

            case R.id.get_user_timeline_button: {
                // mUpdateStatusResponseTextView
                // .setText(getString(R.string.get_user_timeline));
                mUpdateStatusResponseTextView.setText(getString(R.string.please_wait));

                getUserTimeline(mUserName, mPassword);

                // asyncTaskTest(137);
                break;
            }

            default:
                break;
        }

    }

    protected void asyncTaskTest(int size) {
        for (int i = 0; i < size; i++) {
            Utils.log(TAG, "execute getUserTimeline " + i);
            getUserTimeline(mUserName, mPassword);
        }
    }

    public static final int REQUEST_CODE_REGISTER = 1001;
    public static final int REQUEST_CODE_CAPTURE_PHOTO = 10;
    public static final int REQUEST_CODE_GALLERY = REQUEST_CODE_CAPTURE_PHOTO + 1;

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
    public void OnTaskFinished(Object object) {
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
                ArrayList<String> userList = ((GetUserTimeline) baseTask).getUserList();
                userList = mFTPreference.storeUserList(userList);
                // setAutoCompleteTextViewAdapter(addAT(userList));
                break;
            }

            default:
                break;
        }
    }

    private ArrayList<String> addAT(ArrayList<String> arrayList) {
        ArrayList<String> list = new ArrayList<String>();
        for (String str : arrayList) {
            list.add(str);
            list.add("@" + str);
        }

        return list;
    }

    private void addResposeTextViewText(String response) {
        String text = response + '\n' + mUpdateStatusResponseTextView.getText();
        mUpdateStatusResponseTextView.setText(text + '\n');
    }

    @Override
    public void onTaskProgressUpdate(Object task, Integer... values) {

    }

    @Override
    public void onTaskStart(Object task) {
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
        // showToast("onTouch");
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // showToast("onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        showToast("onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        showToast("onSingleTapUp");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
            float distanceY) {
        showToast("onScroll");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        showToast("onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY) {

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

    @Override
    protected void onStart() {
        super.onStart();

        Utils.log(TAG, "onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Utils.log(TAG, "onReStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Utils.log(TAG, "onResume()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Utils.log(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.log(TAG, "onDestroy()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.log(TAG, "onPause()");
    }

}
