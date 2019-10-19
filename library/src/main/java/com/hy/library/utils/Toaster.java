package com.hy.library.utils;

import android.annotation.SuppressLint;
import android.os.Looper;
import android.widget.Toast;

import com.hy.library.Base;
import com.hy.utils.AppUtils;

import androidx.annotation.StringRes;

/**
 * Created time : 2019-09-26 22:04.
 *
 * @author HY
 */
public final class Toaster {

    private static Toast sToast;

    @SuppressLint("ShowToast")
    private synchronized static void check(){
        if (sToast == null) {
            sToast = Toast.makeText(Base.getContext().getApplicationContext(), "", Toast.LENGTH_SHORT);
        }
    }


    private static void toast(String msg) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            check();
            sToast.setText(msg);
            sToast.show();
        } else {
            AppUtils.post(() -> {
                check();
                sToast.setText(msg);
                sToast.show();
            });
        }

    }

    public static void toast(String msg, Object... args) {
        String text = String.format(msg, args);
        toast(text);
    }

    public static void toast(@StringRes int msg, Object... args) {
        String text = Base.getContext().getString(msg, args);
        toast(text);
    }

}
