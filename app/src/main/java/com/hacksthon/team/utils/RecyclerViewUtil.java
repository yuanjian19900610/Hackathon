package com.hacksthon.team.utils;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.hacksthon.team.view.DividerDecoration;
import com.hacksthon.team.view.DividerGridItemDecoration;

/**
 * @author fanql
 */
public class RecyclerViewUtil {

    private RecyclerViewUtil() {

    }

    public static void initRecyclerViewV(Context context, RecyclerView view, RecyclerView.Adapter adapter) {
        initRecyclerViewV(context, null, view, adapter);
    }

    public static void initRecyclerViewV(Context context, DividerDecoration dividerDecoration, RecyclerView view, RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());

//        if (dividerDecoration == null) {
//            view.addItemDecoration(new DividerDecoration(context));
//        } else {
//            view.addItemDecoration(dividerDecoration);
//        }

        view.setAdapter(adapter);
    }

    public static void initRecyclerViewAddLine(Context context, DividerDecoration dividerDecoration, RecyclerView view, RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());

        if (dividerDecoration == null) {
            view.addItemDecoration(new DividerDecoration(context));
        } else {
            view.addItemDecoration(dividerDecoration);
        }

        view.setAdapter(adapter);
    }


    /**
     * 配置网格列表RecyclerView
     * @param view
     */
    public static void initRecyclerViewG(Context context, RecyclerView view, boolean isDivided,
            RecyclerView.Adapter adapter, int column) {
        GridLayoutManager layoutManager = new GridLayoutManager(context, column, LinearLayoutManager.VERTICAL, false);
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        if (isDivided) {
            view.addItemDecoration(new DividerGridItemDecoration(context));
        }
        view.setAdapter(adapter);
    }

    public static void initRecyclerViewG(Context context, RecyclerView view, RecyclerView.Adapter adapter, int column) {
        initRecyclerViewG(context, view, false, adapter, column);
    }

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param recyclerView 当前的RecyclerView
     * @param n            要跳转的位置
     */
    public static void moveToPosition(RecyclerView recyclerView, int n) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            recyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = recyclerView.getChildAt(n - firstItem).getTop();
            recyclerView.scrollBy(0, top);
        } else {
            recyclerView.scrollToPosition(n);
        }
    }

}
