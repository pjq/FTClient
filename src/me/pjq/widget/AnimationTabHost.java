
package me.pjq.widget;


import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;

import me.pjq.ftclient.R;
import me.pjq.ftclient.R.anim;

/**
 * 继承 TabHost 组件，带有切入切出的滑动动画效果。
 * 
 * @author zhanwei
 * @since 1.0
 */
public class AnimationTabHost extends TabHost {
    private Animation slideLeftIn;
    private Animation slideLeftOut;
    private Animation slideRightIn;
    private Animation slideRightOut;

    /** 记录是否打开动画效果 */
    private boolean isOpenAnimation;
    /** 记录当前标签页的总数 */
    private int mTabCount;

    private int mPreviewIndex = -1;

    public AnimationTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);

        slideLeftIn = AnimationUtils.loadAnimation(context,
                R.anim.slide_left_in);
        slideLeftOut = AnimationUtils.loadAnimation(context,
                R.anim.slide_left_out);
        slideRightIn = AnimationUtils.loadAnimation(context,
                R.anim.slide_right_in);
        slideRightOut = AnimationUtils.loadAnimation(context,
                R.anim.slide_right_out);

        isOpenAnimation = true;
    }

    /**
     * 设置是否打开动画效果
     * 
     * @param isOpenAnimation true：打开
     */
    public void setOpenAnimation(boolean isOpenAnimation) {
        this.isOpenAnimation = isOpenAnimation;
    }

    /**
     * 设置标签滑动动画。<br>
     * 动画顺序为“左进——>左出——>右进——>左出”
     * 
     * @param animationResIDs 动画的资源文件ID
     * @return true：四个动画文件;<br>
     *         false：非四个动画文件（无法匹配，采用默认动画）
     */
    public boolean setTabAnimation(int[] animationResIDs) {
        if (3 == animationResIDs.length) {
            slideLeftIn = AnimationUtils.loadAnimation(getContext(),
                    animationResIDs[0]);
            slideLeftOut = AnimationUtils.loadAnimation(getContext(),
                    animationResIDs[1]);
            slideRightIn = AnimationUtils.loadAnimation(getContext(),
                    animationResIDs[2]);
            slideRightOut = AnimationUtils.loadAnimation(getContext(),
                    animationResIDs[3]);

            return true;
        } else {
            return false;
        }
    }

    /**
     * @return 返回当前标签页的总数
     */
    public int getTabCount() {
        return mTabCount;
    }

    @Override
    public void addTab(TabSpec tabSpec) {
        mTabCount++;
        super.addTab(tabSpec);
    }

    @Override
    public void setCurrentTab(int index) {
        int mCurrentTabID = getCurrentTab();

        if (null != getCurrentView()) {
            // 第一次设置 Tab 时，该值为 null。

            if (-1 != mPreviewIndex) {

                if (isOpenAnimation) {
                    if (mCurrentTabID == (mTabCount - 1) && index == 0) {
                        getCurrentView().startAnimation(slideLeftOut);
                    } else if (mCurrentTabID == 0 && index == (mTabCount - 1)) {
                        getCurrentView().startAnimation(slideRightOut);
                    } else if (index > mCurrentTabID) {
                        getCurrentView().startAnimation(slideLeftOut);
                    } else if (index < mCurrentTabID) {
                        getCurrentView().startAnimation(slideRightOut);
                    }
                }
            }
            
            // When first enter the TabHost,don't need show the animvation.
            mPreviewIndex = index;
        }

        super.setCurrentTab(index);

        if (-1 != mPreviewIndex) {
            // When first enter the TabHost,don't need show the animvation.
            mPreviewIndex = index;

            if (isOpenAnimation) {
                if (mCurrentTabID == (mTabCount - 1) && index == 0) {
                    getCurrentView().startAnimation(slideLeftIn);
                } else if (mCurrentTabID == 0 && index == (mTabCount - 1)) {
                    getCurrentView().startAnimation(slideRightIn);
                } else if (index > mCurrentTabID) {
                    getCurrentView().startAnimation(slideLeftIn);
                } else if (index < mCurrentTabID) {
                    getCurrentView().startAnimation(slideRightIn);
                }
            }
        }
        mPreviewIndex = index;
    }
}
