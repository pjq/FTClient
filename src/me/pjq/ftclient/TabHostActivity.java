
package me.pjq.ftclient;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import me.pjq.widget.AnimationTabHost;

public class TabHostActivity extends TabActivity implements OnClickListener {

    public static final String TAB_RECOMMEND = "tab0";
    public static final String TAB_NEW_BOOK = "tab1";
    public static final String TAB_TOP = "tab2";
    public static final String TAB_CATEGORY = "tab3";
    public static final String TAB_SEARCH = "tab4";
    final int maxTabs = 5;
    // private TabHost mTabHost;
    private ImageView[] mCoverBtns;
    private LinearLayout[] mTabs;
    int mPreClickID = 0;
    int mCurClickID = 0;

    private View mCommonTitleBarLeftView;
    private GestureDetector gestureDetector;
    private AnimationTabHost mTabHost;
    private TabWidget mTabWidget;
    private int mCurrentDisplayTabId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab_host);
        // showSearchButton();
        gestureDetector = new GestureDetector(new TabHostTouch());
        initView();

        hideCommonTitleBarLeftView();
        setTitleBarText(getString(R.string.app_name));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("tab") == true) {
            String tab = bundle.getString("tab");
            if (tab != null && tab.length() > 0) {
                mTabHost.setCurrentTabByTag(tab);
                switch (Integer.parseInt(tab.trim().substring(tab.length() - 1))) {
                    case 0:
                        mCurClickID = 0;
                        updataCurView(mCurClickID);
                        break;
                    case 1:
                        mCurClickID = 1;
                        updataCurView(mCurClickID);
                        break;
                    case 2:
                        mCurClickID = 2;
                        updataCurView(mCurClickID);
                        break;
                    case 3:
                        mCurClickID = 3;
                        updataCurView(mCurClickID);
                        break;
                    case 4:
                        mCurClickID = 4;
                        updataCurView(mCurClickID);

                    default:
                        break;
                }
            }
        }
    }

    private void hideCommonTitleBarLeftView() {
        mCommonTitleBarLeftView = (View) findViewById(R.id.common_titlebar_left_layout);
        mCommonTitleBarLeftView.setVisibility(View.GONE);
    }

    protected void setTitleBarText(String text) {
        TextView textView = (TextView) findViewById(R.id.common_titlebar_name);
        textView.setText(text);
    }

    // private void showSearchButton() {
    // ImageButton searchButton = (ImageButton)
    // findViewById(R.id.title_bar_more_app);
    // ImageView divider = (ImageView) findViewById(R.id.title_bar_divider);
    // searchButton.setVisibility(View.VISIBLE);
    // divider.setVisibility(View.VISIBLE);
    // searchButton.setBackgroundResource(R.drawable.ic_top_bar_search);
    //
    // searchButton.setOnClickListener(new OnClickListener() {
    //
    // @Override
    // public void onClick(View v) {
    // Intent intent = new Intent();
    // intent.setClass(PageYunChengTabHost.this, PageSearch.class);
    // startActivity(intent);
    // }
    // });
    // }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            event.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.dispatchTouchEvent(event);
    }

    private int currentTabID = 0;

    private class TabHostTouch extends SimpleOnGestureListener {
        /** ������ҳ������� */
        private static final int ON_TOUCH_DISTANCE = 80;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {

            // if (!shouldHandle(e1, e2, velocityX, velocityY)) {
            // return super.onFling(e1, e2, velocityX, velocityY);
            // }

            float x = Math.abs(e1.getX() - e2.getX());
            float y = Math.abs(e1.getY() - e2.getY());

            if (x <= ON_TOUCH_DISTANCE) {
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            if (Math.abs(x / Math.pow((x * x + y * y), 0.5f)) < (1.50f / 2.0f)) {// �����Ƕ�60�����²�����
                return false;
            } else {
                if (e1.getX() - e2.getX() <= (-ON_TOUCH_DISTANCE)) {
                    currentTabID = mTabHost.getCurrentTab() - 1;

                    if (currentTabID < 0) {
                        // TODO 在最左边，还向左滑动，直接跳出。
                        return true;
                        // mTabHost.setOpenAnimation(false);
                    } else {
                        mTabHost.setOpenAnimation(true);
                    }
                } else if (e1.getX() - e2.getX() >= ON_TOUCH_DISTANCE) {
                    currentTabID = mTabHost.getCurrentTab() + 1;

                    if (currentTabID >= mTabHost.getTabCount()) {
                        // currentTabID = 0;
                        // TODO 在最右边，还向右滑动，直接跳出。
                        return true;
                        // mTabHost.setOpenAnimation(false);
                    } else {
                        mTabHost.setOpenAnimation(true);
                    }
                }
            }
            mTabHost.setCurrentTab(currentTabID);

            // onClick(mTabs[mTabHost.getCurrentTab()]);
            startSlip(mTabs[mTabHost.getCurrentTab()]);
            return true;
        }

        /**
         * 如果在上面部分，不需要处理滑动事件
         * 
         * @return
         */
        // private boolean shouldHandle(MotionEvent e1, MotionEvent e2, float
        // velocityX,
        // float velocityY) {
        //
        // float y = e1.getY();
        // int screenHeight = Util.getScreenSizeHeight(getCurrentActivity());
        // float rate = y / screenHeight;
        // // 由于第一个界面需要处理Gallery滑动事件，所以在这里做特殊判断
        // int tabId = mTabHost.getCurrentTab();
        // if (rate < 0.3 && 0 == tabId) {
        // return false;
        // } else {
        // return true;
        // }
        // }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        mTabHost = (AnimationTabHost) getTabHost();
        setupIntent();
        setupTabSpec();
        mTabs = new LinearLayout[maxTabs];
        mTabs[0] = (LinearLayout) this.findViewById(R.id.tab1);
        mTabs[1] = (LinearLayout) this.findViewById(R.id.tab2);
        mTabs[2] = (LinearLayout) this.findViewById(R.id.tab3);
        mTabs[3] = (LinearLayout) this.findViewById(R.id.tab4);
        mTabs[4] = (LinearLayout) this.findViewById(R.id.tab5);
        mCoverBtns = new ImageView[maxTabs];
        mCoverBtns[0] = (ImageView) this.findViewById(R.id.cover_btn1);
        mCoverBtns[1] = (ImageView) this.findViewById(R.id.cover_btn2);
        mCoverBtns[2] = (ImageView) this.findViewById(R.id.cover_btn3);
        mCoverBtns[3] = (ImageView) this.findViewById(R.id.cover_btn4);
        mCoverBtns[4] = (ImageView) this.findViewById(R.id.cover_btn5);
        for (int i = 0; i < maxTabs; i++) {
            mTabs[i].setOnClickListener(this);
            mCoverBtns[i].setOnClickListener(this);
        }
        mPreClickID = 0;

    }

    private Intent mRecommendIntent;
    private Intent mNewBookIntent;
    private Intent mAllTopIntent;
    private Intent mCategoryIntent;
    private Intent mSearchIntent;

    private void setupIntent() {
        mRecommendIntent = new Intent(this, HomeTimelineActivity.class);
        mNewBookIntent = new Intent(this, PostActivity.class);
        mAllTopIntent = new Intent(this, SettingsActivity.class);
        mCategoryIntent = new Intent(this, RegisterActivity.class);
        mSearchIntent = new Intent(this, AboutActivity.class);
    }

    private void setupTabSpec() {
        TabSpec localTabSpec1 = mTabHost.newTabSpec(TAB_RECOMMEND).setContent(mRecommendIntent)
                .setIndicator(getString(R.string.tab_hometimeline));
        mTabHost.addTab(localTabSpec1);

        TabSpec localTabSpec2 = mTabHost.newTabSpec(TAB_NEW_BOOK).setContent(mNewBookIntent)
                .setIndicator(getString(R.string.tab_post));
        mTabHost.addTab(localTabSpec2);

        TabSpec localTabSpec3 = mTabHost.newTabSpec(TAB_TOP).setContent(mAllTopIntent)
                .setIndicator(getString(R.string.tab_settings));
        mTabHost.addTab(localTabSpec3);

        TabSpec localTabSpec4 = mTabHost.newTabSpec(TAB_CATEGORY).setContent(mCategoryIntent)
                .setIndicator(getString(R.string.tab_register));
        mTabHost.addTab(localTabSpec4);

        TabSpec localTabSpec5 = mTabHost.newTabSpec(TAB_SEARCH).setContent(mSearchIntent)
                .setIndicator(getString(R.string.tab_about));
        mTabHost.addTab(localTabSpec5);
    }

    private void updataCurView(int curClickID) {
        if (0 <= curClickID && maxTabs > curClickID) {
            mCoverBtns[mPreClickID].setVisibility(View.INVISIBLE);
            mCoverBtns[curClickID].setVisibility(View.VISIBLE);
            mPreClickID = curClickID;
        }
    }

    private void startSlip(View v) {
        Animation a = new TranslateAnimation(0.0f, v.getLeft() - mTabs[mPreClickID].getLeft(),
                0.0f, 0.0f);
        a.setDuration(200);
        a.setFillAfter(false);
        a.setFillBefore(false);
        for (int i = 0; i < maxTabs; i++) {
            if (mTabs[i] == v) {
                mCurClickID = i;
                break;
            }
        }
        a.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                updataCurView(mCurClickID);
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        mCoverBtns[mPreClickID].startAnimation(a);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab1:
            case R.id.cover_btn1: {
                String currentTabTag = mTabHost.getCurrentTabTag();
                if (null != currentTabTag && !currentTabTag.equalsIgnoreCase(TAB_RECOMMEND)) {
                    mTabHost.setCurrentTabByTag(TAB_RECOMMEND);
                    startSlip(v);
                }

                break;
            }
            case R.id.tab2:
            case R.id.cover_btn2: {
                String currentTabTag = mTabHost.getCurrentTabTag();
                if (null != currentTabTag && !currentTabTag.equalsIgnoreCase(TAB_NEW_BOOK)) {
                    mTabHost.setCurrentTabByTag(TAB_NEW_BOOK);
                    startSlip(v);
                }

                break;
            }
            case R.id.tab3:
            case R.id.cover_btn3: {
                String currentTabTag = mTabHost.getCurrentTabTag();
                if (null != currentTabTag && !currentTabTag.equalsIgnoreCase(TAB_TOP)) {
                    mTabHost.setCurrentTabByTag(TAB_TOP);
                    startSlip(v);
                }
                break;
            }

            case R.id.tab4:
            case R.id.cover_btn4: {
                String currentTabTag = mTabHost.getCurrentTabTag();
                if (null != currentTabTag && !currentTabTag.equalsIgnoreCase(TAB_CATEGORY)) {
                    mTabHost.setCurrentTabByTag(TAB_CATEGORY);
                    startSlip(v);
                }
                break;
            }

            case R.id.tab5:
            case R.id.cover_btn5: {
                String currentTabTag = mTabHost.getCurrentTabTag();
                if (null != currentTabTag && !currentTabTag.equalsIgnoreCase(TAB_SEARCH)) {
                    mTabHost.setCurrentTabByTag(TAB_SEARCH);
                    startSlip(v);
                }
                break;
            }

            default:
                break;
        }
    }
}
