package com.hy.library.utils;

import com.hy.utils.Logger;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created time : 2019-09-26 22:37.
 *
 * @author HY
 */
public class HackyStaggeredGridLayoutManager extends StaggeredGridLayoutManager {

    public HackyStaggeredGridLayoutManager(int spanCount) {
        super(spanCount, VERTICAL);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch ( IndexOutOfBoundsException e) {
                Logger.e(e.getMessage(), e);
        }
    }
}
