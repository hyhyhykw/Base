package com.hy.library.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hy.library.Base;
import com.hy.library.utils.BaseUtils;
import com.hy.utils.AppUtils;
import com.hy.utils.LifecycleUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;


/**
 * Created time : 2018/4/3 11:05.
 *
 * @author HY
 */
public abstract class BaseRecyclerAdapter<T, V extends BaseRecyclerAdapter.BaseViewHolder> extends RecyclerView.Adapter<V> {

    protected final List<T> mData = new ArrayList<>();

    protected Context mContext;

    public List<T> getData() {
        return mData;
    }

    protected final void toActivity(@NonNull Class<? extends Activity> clazz) {
        toActivity(clazz, null);
    }


    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (!LifecycleUtils.canLoadImage(recyclerView.getContext())) return;
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                Fresco.getImagePipeline().resume();
            } else {
                Fresco.getImagePipeline().pause();
            }
        }
    };

    protected final void post(Runnable action) {
        AppUtils.post(action);
    }

    protected final void postDelayed(Runnable action, long delay) {
        AppUtils.postDelay(action, delay);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(mOnScrollListener);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.removeOnScrollListener(mOnScrollListener);
        super.onDetachedFromRecyclerView(recyclerView);
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
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
            AppUtils.post(action);
        } else {
            toActivity(Base.getDelegate().getLoginActivity());
        }
    }

    public void reset(@NonNull List<T> data) {
        mData.clear();
        mData.addAll(data);

        if (!BaseUtils.isMarshmallow()) {
            post(this::notifyDataSetChanged);
        } else {
            postDelayed(this::notifyDataSetChanged, 500);
        }
    }

    public void reset(@NonNull Collection<? extends T> data) {
        mData.clear();
        mData.addAll(data);

        if (!BaseUtils.isMarshmallow()) {
            post(this::notifyDataSetChanged);
        } else {
            postDelayed(this::notifyDataSetChanged, 500);
        }
    }

    public void reset(@NonNull T[] data) {
        reset(Arrays.asList(data));
    }

    public void addData(@NonNull List<T> data) {
        mData.addAll(data);
        post(() -> notifyItemRangeInserted(mData.size() - data.size(), data.size()));
    }

    public void addData(@NonNull Collection<T> data) {
        mData.addAll(data);
        post(() -> notifyItemRangeInserted(mData.size() - data.size(), data.size()));
    }

    public void addData(@NonNull T[] data) {
        addData(Arrays.asList(data));
    }

    public void addData(int index, @NonNull List<T> data) {
        mData.addAll(index, data);
        post(() -> notifyItemRangeInserted(index, data.size()));
//        notifyDataSetChanged();
    }

    public void addData(int index, @NonNull Collection<T> data) {
        mData.addAll(index, data);
        post(() -> notifyItemRangeInserted(index, data.size()));
//        notifyDataSetChanged();
    }

    public void addData(int index, @NonNull T[] data) {
        addData(index, Arrays.asList(data));
    }

    public void addItem(T t) {
        mData.add(t);
        post(() -> notifyItemInserted(mData.size() - 1));
    }

    public void addItem(T t, int position) {
        mData.add(position, t);
        post(() -> notifyItemInserted(position));
    }

    public void deleteItem(int position) {
        mData.remove(position);
        post(() -> {
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mData.size() - position);
        });
    }

    public T getFirst() {
        return mData.get(0);
    }

    public T getLast() {
        return mData.get(mData.size() - 1);
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    public void changeItem(int position, T item) {
        mData.set(position, item);
        post(() -> notifyItemChanged(position));
    }

    @NonNull
    @Override
    public V onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (null == mContext) mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(getLayoutByType(viewType), parent, false);
        return createViewHolder(view, viewType);
    }

    @NonNull
    protected abstract V createViewHolder(View view, int viewType);

    protected int getLayoutByType(int viewType) {
        return layout();
    }

    @LayoutRes
    protected abstract int layout();

    @Override
    public void onBindViewHolder(@NonNull V holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public boolean isEmpty() {
        return mData.isEmpty();
    }

    public final void clear() {
        mData.clear();
        if (!BaseUtils.isMarshmallow()) {
            post(this::notifyDataSetChanged);
        } else {
            postDelayed(this::notifyDataSetChanged, 500);
        }
    }


    public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
        public final View itemView;

        public BaseViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        public abstract void bind();
    }


}
