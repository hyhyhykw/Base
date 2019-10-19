package com.hy.library;

import android.app.Activity;
import android.content.Context;

/**
 * Created time : 2018/7/24 15:37.
 *
 * @author HY
 */
public interface BaseAppDelegate {
    //
    boolean isLogin();

    //登陆页
    Class<? extends Activity> getLoginActivity();

    //
    Context getContext();

    default boolean debug() {
        return true;
    }
}
