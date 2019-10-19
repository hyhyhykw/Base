package com.hy.library.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.hy.library.Base;
import com.hy.utils.AppUtils;
import com.hy.utils.Logger;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created time : 2019-09-26 18:22.
 *
 * @author HY
 */
public abstract class CommonBaseActivity extends BaseSwipeBackActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (isSetPortrait()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        }
        beforeOnCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        Logger.d(getClass().getSimpleName() + ":onCreate()");
        initMvp();
    }


    /**
     * setview之前调用的方法
     */
    protected void beforeSetContentView(@Nullable Bundle savedInstanceState) {
    }

    /**
     * setview之后调用的方法
     */
    protected void afterSetContentView(@Nullable Bundle savedInstanceState) {
    }

    /**
     * 初始化mvp
     */
    protected void initMvp() {
    }

    @LayoutRes
    protected abstract int layout();

    /**
     * view完全加载时调用的方法
     */
    protected abstract void onViewLoaded();

    /**
     * 初始化view
     */
    protected abstract void initView();

    protected void onViewLoaded(@Nullable Bundle savedInstanceState) {
        onViewLoaded();
    }


    protected void beforeOnCreate(Bundle savedInstanceState) {
    }


    /**
     * 强制竖屏 防止横屏的一些bug
     *
     * @return true表示强制竖屏
     */
    protected boolean isSetPortrait() {
        return true;
    }


    public static final String BUNDLE_KEY = "bundle";

    /**
     * 跳转
     *
     * @param clazz 跳转的activity class
     */
    protected final void toActivity(Class<? extends Activity> clazz) {
        toActivity(clazz, null);
    }

    /**
     * 携带参数跳转
     *
     * @param clazz  跳转的activity class
     * @param bundle 参数
     */
    protected final void toActivity(@NonNull Class<? extends Activity> clazz, @Nullable Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtra(BUNDLE_KEY, bundle);
        }
        startActivity(intent);
    }

    protected final void toActivityForResult(@NonNull Class<? extends Activity> clazz, int requestCode, @Nullable Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtra(BUNDLE_KEY, bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    protected final void toActivityForResult(@NonNull Class<? extends Activity> clazz, int requestCode) {
        toActivityForResult(clazz, requestCode, null);
    }


    protected final void post(Runnable action) {
        AppUtils.post(action);
    }

    protected final void postDelayed(Runnable action, long delay) {
        AppUtils.postDelay(action, delay);
    }

    /**
     * 登录之后才可以跳转 ，没有登录就跳转到登录页
     *
     * @param clazz 跳转的类
     */
    protected final void toLogin(Class<? extends Activity> clazz) {
        toLogin(clazz, null);
    }

    /**
     * 登录之后才可以跳转 ，没有登录就跳转到登录页
     *
     * @param clazz  跳转的类
     * @param bundle 参数
     */
    protected final void toLogin(Class<? extends Activity> clazz, @Nullable Bundle bundle) {
        toLogin(() -> toActivity(clazz, bundle));
    }

    /**
     * 登录后才能执行的任务 ，没有登录就跳转到登录页
     *
     * @param action 任务
     */
    protected final void toLogin(Runnable action) {
        if (Base.getDelegate().isLogin()) {
            post(action);
        } else {
            toActivity(Base.getDelegate().getLoginActivity());
        }
    }

    @NonNull
    protected final Bundle getBundle() {
        Intent intent = getIntent();
        Bundle bundleExtra = intent.getBundleExtra(BUNDLE_KEY);
        if (bundleExtra == null) throw new UnsupportedOperationException("No bundle extra found");
        return bundleExtra;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d(getClass().getSimpleName() + ":onDestroy()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.d(getClass().getSimpleName() + ":onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d(getClass().getSimpleName() + ":onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d(getClass().getSimpleName() + ":onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.d(getClass().getSimpleName() + ":onStop()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.d(getClass().getSimpleName() + ":onStart()");
    }
}
