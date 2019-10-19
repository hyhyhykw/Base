package com.hy.library.base;

import android.os.Bundle;
import android.os.Looper;

import com.hy.library.Base;
import com.hy.utils.Logger;
import com.hy.utils.SizeUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created time : 2018/11/21 9:02.
 *
 * @author HY
 */
public abstract class BaseActivity extends CommonBaseActivity {

    protected final int getStatusBarHeight() {
        return SizeUtils.getStatusBarHeight(this);
    }

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView(savedInstanceState);
        setContentView(layout());
        mUnbinder = ButterKnife.bind(this);
        afterSetContentView(savedInstanceState);

        initView();

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
}
