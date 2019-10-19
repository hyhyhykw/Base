package com.hy.library.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hy.library.Base;
import com.hy.utils.AppUtils;
import com.hy.utils.Logger;
import com.hy.utils.SizeUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created time : 2019-09-27 15:34.
 *
 * @author HY
 */
public abstract class BaseFragment extends Fragment implements IBaseFragment {
    private boolean viewCreate = false;
    private View mView = null;
    private Unbinder mUnbinder;


    protected final int getStatusBarHeight() {
        return SizeUtils.getStatusBarHeight(getContext());
    }

    @NonNull
    @Override
    public Context getContext() {
        Context context = super.getContext();
        if (context == null) throw new RuntimeException("null returned from getContext()");
        return context;
    }

    protected final void finish() {
        getNonActivity().finish();
    }

    @NonNull
    protected final Activity getNonActivity() {
        FragmentActivity activity = getActivity();
        if (activity == null) throw new RuntimeException("null returned from getActivity()");
        return activity;
    }

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!viewCreate) {
            initMvp();
            initVariable();
            viewCreate = true;
        }
    }

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
        Intent intent = new Intent(getContext(), clazz);
        if (bundle != null) {
            intent.putExtra(CommonBaseActivity.BUNDLE_KEY, bundle);
        }
        startActivity(intent);
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

    protected final void toActivityForResult(@NonNull Class<? extends Activity> clazz, int requestCode, @Nullable Bundle bundle) {
        Intent intent = new Intent(getContext(), clazz);
        if (bundle != null) {
            intent.putExtra(CommonBaseActivity.BUNDLE_KEY, bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    protected final void toActivityForResult(@NonNull Class<? extends Activity> clazz, int requestCode) {
        toActivityForResult(clazz, requestCode, null);
    }

    protected boolean isFirstVisible = false;

    @Override
    public void initVariable() {
        isFirstVisible = true;
        mView = null;
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d(getClass().getSimpleName() + ":onCreateView()");
        if (null == mView) {
            mView = inflater.inflate(layout(), container, false);
        }
        mUnbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mView == null) {
            return;
        }
        if (isFirstVisible && isVisibleToUser) {
            onLoad();
            isFirstVisible = false;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public final void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Logger.d(getClass().getSimpleName() + ":onViewCreated()");
        super.onViewCreated(view, savedInstanceState);


        if (viewCreate) {
            viewCreate = false;
            initView(view);
        }

        if (getUserVisibleHint() && isFirstVisible) {
            onLoad();
            isFirstVisible = false;
        }
    }

    @Override
    public void onDestroyView() {
        Logger.d(getClass().getSimpleName() + ":onDestroyView()");
        super.onDestroyView();
        mUnbinder.unbind();
    }

    protected abstract void onLoad();


    @Override
    public void initMvp() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Logger.d(getClass().getSimpleName() + ":onAttach()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d(getClass().getSimpleName() + ":onDestroy()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d(getClass().getSimpleName() + ":onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.d(getClass().getSimpleName() + ":onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.d(getClass().getSimpleName() + ":onStop()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.d(getClass().getSimpleName() + ":onStart()");
    }
}
