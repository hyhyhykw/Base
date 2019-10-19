package com.hy.library.base;

import android.view.View;

import androidx.annotation.LayoutRes;

/**
 * Created time : 2019-09-27 11:12.
 *
 * @author HY
 */
public interface IBaseFragment {

    /**
     * 此方法用于返回Fragment设置ContentView的布局文件资源ID
     *
     * @return 布局文件资源ID
     */
    @LayoutRes
    int layout();

    /**
     * 此方法用于初始化成员变量及获取Intent传递过来的数据
     * 注意：这个方法中不能调用所有的View，因为View还没有被初始化，要使用View在initView方法中调用
     */
    void initVariable();

    /**
     * 此方法用于设置View显示数据
     */
    void initView(View inflateView);


    void initMvp();
}
