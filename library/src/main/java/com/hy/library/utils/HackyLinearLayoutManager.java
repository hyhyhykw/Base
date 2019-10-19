package com.hy.library.utils;

import android.content.Context;

import com.hy.utils.Logger;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created time : 2019-09-26 22:39.
 *
 * @author HY
 */
public class HackyLinearLayoutManager extends LinearLayoutManager {

    public HackyLinearLayoutManager(Context context) {
        this(context,VERTICAL);
    }

    public HackyLinearLayoutManager(Context context, int orientation) {
        super(context, orientation, false);
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
