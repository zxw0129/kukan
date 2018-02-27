package com.xk.xkds.component.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.xk.xkds.common.utils.LogUtlis;



/**
 * Created  on 2017/2/10.
 * 可以让item 自己置顶的 recyview
 */

public class ScollRecycleView extends RecyclerView {
    private boolean move = false;
    private int mIndex = 0;

    public ScollRecycleView(Context context) {
        this(context, null);
    }

    public ScollRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        this.addOnScrollListener(new RecyclerViewListener());
    }

    public ScollRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public void moveToPosition(int n) {
        mIndex = n;
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        int lastItem = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            smoothScrollToPosition(n);
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = getChildAt(n - firstItem).getTop();
            smoothScrollBy(0, top);
            LogUtlis.getInstance().showLogE(" item areadly on scereen  + " +top +"firstItem = "+ firstItem);
            move = true;
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            smoothScrollToPosition(n);
            move = true;
            LogUtlis.getInstance().showLogE("need onScroll to top");
        }

    }

    class RecyclerViewListener extends RecyclerView.OnScrollListener {
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled(recyclerView, dx, dy);
//            //在这里进行第二次滚动（最后的100米！）
//            if (move) {
//                move = false;
//                //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
//                int n = mIndex - ((GridLayoutManager) getLayoutManager())
//                        .findFirstVisibleItemPosition();
//                if (0 <= n && n < getChildCount()) {
//                    //获取要置顶的项顶部离RecyclerView顶部的距离
//                    int top = getChildAt(n).getTop();
//                    //最后的移动
//                    smoothScrollBy(0, top);
//                }
//
//            }
//        }


        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (move) {
                move = false;
                int Position = ((LinearLayoutManager) getLayoutManager())
                        .findFirstVisibleItemPosition();
                int n = mIndex - Position;
                if (0 <= n && n < getChildCount()) {
                    int top = getChildAt(n).getTop();
                    smoothScrollBy(0, top);
                }
            }
        }
    }

}
