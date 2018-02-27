package com.xk.xkds.component.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.xk.xkds.R;
import com.xk.xkds.common.interfa.IUIOperation;



/**
 * Created  on 2017/2/8.
 */

public abstract class BaseActivity extends AppCompatActivity implements IUIOperation {
    protected ViewStub vb_title;
    protected ViewStub vb_root;
    protected RelativeLayout mBaseView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initRootView();
        initIntent(getIntent());
        //初始化界面
        initView();
        //初始化数据
        initDatas();
        //初始化监听
        initListener();
//        PushAgent.getInstance(this).onAppStart();
    }

    protected  void initRootView(){
        vb_root = findView(R.id.vb_content_base_ac);
        vb_root.setLayoutResource(getLayoutRes());
        vb_root.inflate();
        mBaseView = findView(R.id.rlt_base);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initNewIntent(intent);
    }

    protected abstract void initNewIntent(Intent intent);

    /**
     * findViewById 省略强转
     */
    public <T> T findView(int id) {
        T view = (T) findViewById(id);
        return view;
    }

    @Override
    public void onClick(View view) {
        onClick(view, view.getId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStart() {
        BaseApplication.getInstance().setCurrentActivity(this);
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
