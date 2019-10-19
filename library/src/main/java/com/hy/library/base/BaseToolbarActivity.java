package com.hy.library.base;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.hy.library.Base;
import com.hy.library.R;
import com.hy.library.utils.BaseUtils;
import com.hy.utils.AppUtils;
import com.hy.utils.Logger;
import com.hy.utils.MyViewUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created time : 2018/11/22 15:55.
 *
 * @author HY
 */
public abstract class BaseToolbarActivity extends CommonBaseActivity {

    protected ImageView mIvLeft;
    protected TextView mTvTitle;
    protected ImageView mIvRight;
    protected TextView mTvRight;
    protected ViewStub mViewStub;
    protected View mShadow;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_toolbar);

        int color = BaseUtils.getTypeValueColor(this, android.R.attr.colorPrimaryDark);
        if (BaseUtils.isLightColor(color)) {
            AppUtils.processMIUI(this, true);
        }

        findView();
        mViewStub.setLayoutResource(layout());
        View inflate = mViewStub.inflate();
        mUnbinder = ButterKnife.bind(this, inflate);

        initView();

        afterSetContentView(savedInstanceState);
        mIvLeft.setOnClickListener(v -> onLeftClick());

        Looper.myQueue().addIdleHandler(() -> {
            try {
                onViewLoaded(savedInstanceState);
            } catch (Exception ex) {
                //使用反射捕获全局异常 正式打包时自动删除
                if (Base.getDelegate().debug()) {
                    debug(ex);
                } else {
                    throw ex;
                }
            }
            return false;
        });
    }

    public void debug(Exception ex) {
        try {
            Class<?> aClass = Class.forName("com.hy.crash.CrashHandler");

            Method getInstance = aClass.getDeclaredMethod("getInstance");
            Object invoke = getInstance.invoke(null);

            Method uncaughtException = aClass.getDeclaredMethod("uncaughtException", Thread.class, Throwable.class);

            uncaughtException.invoke(invoke, Thread.currentThread(), ex);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Logger.e(e.getMessage(), e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    protected void onRightClick() {

    }


    protected final void setRightImage(@DrawableRes int resId) {
        if (mTvRight.getVisibility() == View.VISIBLE) {
            mTvRight.setVisibility(View.GONE);
        }
        if (mIvRight.getVisibility() == View.GONE) {
            mIvRight.setVisibility(View.VISIBLE);
        }
        mIvRight.setImageResource(resId);
        mIvRight.setClickable(true);
        MyViewUtils.setClick(mIvRight, v -> onRightClick());
    }

    protected final void setRightText(@StringRes int resId, float textSize, @Nullable Integer textColor) {
        if (mIvRight.getVisibility() == View.VISIBLE) {
            mIvRight.setVisibility(View.GONE);
        }
        if (mTvRight.getVisibility() == View.GONE) {
            mTvRight.setVisibility(View.VISIBLE);
        }
        if (textSize != -1f) {
            mTvRight.setTextSize(textSize);
        }
        if (textColor != null) {
            mTvRight.setTextColor(textColor);
        }
        mTvRight.setText(resId);
        mTvRight.setClickable(true);
        MyViewUtils.setClick(mTvRight, v -> onRightClick());
    }

    protected final void setRightText(@StringRes int resId, float textSize) {
        setRightText(resId, textSize, null);
    }

    protected final void setRightText(@StringRes int resId) {
        setRightText(resId, -1);
    }


    @Override
    public void setTitle(int titleId) {
        mTvTitle.setText(titleId);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTvTitle.setText(title);
    }

    protected void onLeftClick() {
        finish();
    }

    private void findView() {
        mIvLeft = findViewById(R.id.base_iv_left);
        mTvTitle = findViewById(R.id.base_tv_title);
        mIvRight = findViewById(R.id.base_iv_right);
        mTvRight = findViewById(R.id.base_tv_right);
        mViewStub = findViewById(R.id.base_lyt_stub);
        mShadow = findViewById(R.id.base_shadow);
    }

}
