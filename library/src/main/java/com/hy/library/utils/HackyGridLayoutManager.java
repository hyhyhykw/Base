package com.hy.library.utils;

import android.content.Context;

import com.hy.utils.Logger;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created time : 2019-09-26 22:41.
 *
 * @author HY
 */
public class HackyGridLayoutManager extends GridLayoutManager {

    public HackyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        }catch (IndexOutOfBoundsException e){
                Logger.e(e.getMessage(), e);
        }
    }
}
