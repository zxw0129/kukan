package com.xk.xkds.component.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import com.xk.xkds.R;
import com.xk.xkds.component.base.BaseActivity;

/**
 * Created  on 2017/3/3.
 * 免责声明
 */

public class LiabilityActivity extends BaseActivity {
    @Override
    public int getLayoutRes() {
        return R.layout.activity_liability;
    }

    @Override
    public void initIntent(Intent intent) {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void onClick(View view, int id) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MUTE:
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initNewIntent(Intent intent) {

    }
}