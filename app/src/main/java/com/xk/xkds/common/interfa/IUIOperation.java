package com.xk.xkds.common.interfa;

import android.content.Intent;
import android.view.View;


/**
 * Created by Administrator on 2017/2/8.
 */

public interface IUIOperation extends View.OnClickListener{
    /***
     * 返回activity 布局文件
     */
    int getLayoutRes();
    /**
     *intent
     */
    void initIntent(Intent intent);
    /**
     * 查找子控件
     */
    void initView();

    /**
     * 初始化数据
     */
    void initDatas();

    /**
     * 初始化监听器
     */
    void initListener();

    /**
     * 监听事件
     */
    void onClick(View view, int id);
}
