package com.hy.library;

import android.content.Context;

import com.hy.utils.SizeUtils;

/**
 * Created time : 2018/7/26 11:39.
 *
 * @author HY
 */
public class Base {
    private static BaseAppDelegate sDelegate;

    private static int screenWidth;
    private static int screenHeight;

    public static void init(BaseAppDelegate delegate) {
        sDelegate = delegate;

        screenWidth = SizeUtils.getScreenWidth(sDelegate.getContext());
        screenHeight = SizeUtils.getScreenHeight(sDelegate.getContext());

    }

    public static BaseAppDelegate getDelegate() {
        if (null == sDelegate) throw new UnsupportedOperationException("请先调用init()方法初始化Delegate");
        return sDelegate;
    }

    public static Context getContext() {
        return sDelegate.getContext();
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }
}
