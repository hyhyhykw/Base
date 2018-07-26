package com.hy.library;

/**
 * Created time : 2018/7/26 11:39.
 *
 * @author HY
 */
public class Base {
    private static BaseAppDelegate sDelegate;

    public static void init(BaseAppDelegate delegate) {
        sDelegate = delegate;
    }

    public static BaseAppDelegate getDelegate() {
        if (null == sDelegate) throw new UnsupportedOperationException("请先调用init()方法初始化Delegate");
        return sDelegate;
    }
}
