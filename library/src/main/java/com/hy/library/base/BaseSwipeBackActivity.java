package com.hy.library.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hy.library.Base;
import com.hy.library.R;
import com.hy.library.view.SlideBackView;
import com.hy.utils.Logger;
import com.hy.utils.SizeUtils;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created time : 2019-09-30 16:12.
 *
 * @author HY
 */
public class BaseSwipeBackActivity extends AppCompatActivity {

    private boolean isEage = false;//判断是否从边缘划过来

    private float shouldFinishPix;
    protected int screenWidth;
    protected int screenHeight;

    protected int mCanSlideLength;

    protected View backView;
    protected SlideBackView slideBackView;

    protected View containerView;
    protected FrameLayout slideContainerView;

    private float x;
    private float y;
    private float downX;

    private int slideHeight;
    private int offset;

    protected static final int LEFT = 0;
    protected static final int RIGHT = 1;
    protected static final int ALL = 2;

    @IntDef({
            LEFT,
            RIGHT,
            ALL
    })
    @interface SwipeDirection {
    }

    protected @SwipeDirection
    int swipeDirection() {
        return ALL;
    }

    /**
     * 不需要滑动返回的重写返回false
     */
    protected boolean isNeedSwipe() {
        return true;
    }

    @SwipeDirection
    private int swipeDirection = ALL;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenHeight = Base.getScreenHeight();
        screenWidth = Base.getScreenWidth();
        if (isNeedSwipe()) {
            addSlide();
        }
    }


    protected final int dp(int dpValue) {
        return SizeUtils.dp2px(this, dpValue);
    }


    protected final void removeSlide() {
        if (!isNeedSwipe()) return;
        if (slideContainerView == null) return;
        ViewGroup parent = (ViewGroup) slideContainerView.getParent();
        if (null == parent) return;
        parent.removeView(slideContainerView);
    }

    protected final void enableSlide() {
        if (isNeedSwipe()) return;
        if (slideContainerView == null) {
            addSlide();
            return;
        }
        ViewGroup parent = (ViewGroup) slideContainerView.getParent();
        ViewGroup container = (ViewGroup) getWindow().getDecorView();
        if (parent != null) {
            if (parent == container) return;
            parent.removeView(slideContainerView);
        }
        container.addView(slideContainerView);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addSlide() {
        shouldFinishPix = screenWidth / 4;
        mCanSlideLength = dp(16);
        slideHeight = dp(200);
        ViewGroup container = (ViewGroup) getWindow().getDecorView();
        containerView = LayoutInflater.from(this).inflate(R.layout.base_view_slide_container, container, false);
        slideContainerView = containerView.findViewById(R.id.base_slide_container);

        backView = View.inflate(this, R.layout.base_view_slideback, null);
        slideBackView = backView.findViewById(R.id.base_slide_view);


        slideContainerView.addView(backView);
        slideContainerView.setOnTouchListener((v, motionEvent) -> {
            x = Math.abs(screenWidth * offset - motionEvent.getRawX());
            y = motionEvent.getRawY();
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = motionEvent.getRawX();

                    //判断点击范围与设置的滑出区域是否符合
                    if (swipeDirection == LEFT) {
                        if (downX > screenWidth / 2f) {
                            //在右侧区域，直接return
                            return false;
                        } else {
                            offset = 0;
                        }
                    } else if (swipeDirection == RIGHT) {
                        if (downX < screenWidth / 2f) {
                            //在左侧区域，直接return
                            return false;
                        } else {
                            offset = 1;
                        }
                    } else if (swipeDirection == ALL) {
                        if (downX > screenWidth / 2f) {
                            //在右侧区域，设为RIGHT
                            offset = 1;
                        } else if (downX < screenWidth / 2f) {
                            //在左侧区域，设为LEFT
                            offset = 0;
                        }
                    }

                    x = Math.abs(screenWidth * offset - motionEvent.getRawX());

                    if (x <= mCanSlideLength) {
                        isEage = true;
                        slideBackView.updateControlPoint(Math.abs(x), offset);
                        setBackViewY(backView, (int) (motionEvent.getRawY()));
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    float moveX = Math.abs(screenWidth * offset - x) - downX;
                    if (isEage) {
                        if (Math.abs(moveX) <= shouldFinishPix) {
                            slideBackView.updateControlPoint(Math.abs(moveX) / 2, offset);
                        }
                        setBackViewY(backView, (int) (motionEvent.getRawY()));
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    //从边缘划过来，并且最后在屏幕的三分之一外
                    if (isEage) {
                        if (x >= shouldFinishPix) {
                            slideBackSuccess();
                        }
                    }
                    isEage = false;
                    slideBackView.updateControlPoint(0, offset);
                    break;
            }
            return isEage;
        });
        container.addView(slideContainerView);
    }


    public void setBackViewY(View view, int y) {
        //判断是否超出了边界
        int topMargin = y - slideHeight / 2;
        if (topMargin < 0) {
            topMargin = 0;
        } else if (y > screenHeight) {
            topMargin = screenHeight - slideHeight / 2;
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(view.getLayoutParams());
        layoutParams.topMargin = topMargin;
        view.setLayoutParams(layoutParams);
    }

    public void setSlideBackDirection(@SwipeDirection int value) {
        swipeDirection = value;
    }

    protected void slideBackSuccess() {
        onBackPressed();
        Logger.d("slide success");
    }
}
