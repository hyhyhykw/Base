package com.hy.library.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hy.library.Base;
import com.hy.utils.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;

/**
 * Created time : 2018/4/3 10:46.
 *
 * @author HY
 */
@SuppressWarnings({"unused", "WeakerAccess", "SameParameterValue", "unchecked"})
public abstract class BaseListAdapter<T, V extends BaseListAdapter.BaseViewHolder> extends BaseAdapter {

    protected final List<T> mData = new ArrayList<>();


    protected Context mContext;

    protected final void toActivity(@NonNull Class<? extends Activity> clazz) {
        toActivity(clazz, null);
    }

    protected final void toActivity(@NonNull Class<? extends Activity> clazz, @Nullable Bundle bundle) {
        toActivity(clazz, bundle, null);
    }

    protected final void toActivity(@NonNull Class<? extends Activity> clazz, @Nullable Bundle bundle, @Nullable Uri data) {
        Intent intent = new Intent(mContext, clazz);
        if (null != bundle) {
            intent.putExtra("bundle", bundle);
        }

        if (null != data) {
            intent.setData(data);
        }

        mContext.startActivity(intent);
    }

    //如果已经登陆，跳转指定Activity 否则跳转到登陆
    protected final void toLogin(@NonNull Class<? extends Activity> clazz) {
        toLogin(clazz, null);
    }

    //如果已经登陆，跳转指定Activity 否则跳转到登陆 携带参数
    protected final void toLogin(@NonNull Class<? extends Activity> clazz, @Nullable Bundle bundle) {
        toLogin(() -> toActivity(clazz, bundle));
    }

    protected final void toLogin(@NonNull Runnable action) {
        if (Base.getDelegate().isLogin()) {
            post(action);
        } else {
            toActivity(Base.getDelegate().getLoginActivity());
        }
    }

    public void reset(@NonNull List<T> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void reset(@NonNull Collection<T> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void reset(@NonNull T[] data) {
        reset(Arrays.asList(data));
    }

    public void addData(@NonNull List<T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(@NonNull Collection<T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    protected final void post(Runnable action) {
        AppUtils.post(action);
    }

    protected final void postDelayed(Runnable action, long delay) {
        AppUtils.postDelay(action, delay);
    }

    public void addData(@NonNull T[] data) {
        addData(Arrays.asList(data));
    }

    public void addItem(T t) {
        mData.add(t);
        notifyDataSetChanged();
    }

    public void addItem(T t, int position) {
        mData.add(position, t);
        notifyDataSetChanged();
    }

    public void setItem(T t, int position) {
        mData.set(position, t);
        notifyDataSetChanged();
    }


    public void deleteItem(T t) {
        mData.remove(t);
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

    public T getFirst() {
        return mData.get(0);
    }

    public T getLast() {
        return mData.get(mData.size() - 1);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public List<T> getData() {
        return mData;
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == mContext) mContext = parent.getContext();
        V holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(layout(), parent, false);

            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (V) convertView.getTag();
        }

        holder.bind(position);
        return convertView;
    }


    protected abstract V createViewHolder(View itemView);

    @LayoutRes
    protected abstract int layout();


    public abstract class BaseViewHolder {
        protected final View itemView;

        public BaseViewHolder(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this,itemView);
        }

        protected abstract void bind(int position);
    }


}
