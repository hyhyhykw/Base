package com.hy.library.utils;

import android.os.Handler;
import android.os.Message;

import com.hy.library.base.BaseMvpView;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;


/**
 * Created time : 2018/2/13 15:11.
 * 防止内存泄漏的handler
 *
 * @author HY
 */
public abstract class WeakHandler<T> extends Handler {

    private WeakReference<T> mReference;

    public WeakHandler(@NonNull T t) {
        mReference = new WeakReference<>(t);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        if (null == mReference) return;
        T t = mReference.get();

        if (null == t) return;

        if (t instanceof BaseMvpView) {
            if (!((BaseMvpView) t).isActive()) return;
        }
        handleMessage(msg, t);
    }

    protected abstract void handleMessage(@NonNull Message msg, @NonNull T t);
}
