package com.xk.xkds.component.dialog;

import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.xk.xkds.R;

/**
 * Created  LJP on 2017/6/30.
 */

public class ChangeSouceDialog extends BaseDialog implements View.OnKeyListener {

    private TextView tvResource_0;
    private TextView tvResource_1;
    private TextView tvResource_2;
    private TextView tvResource_3;
    private TextView tvResource_4;
    private TextView tvResource_5;
    private int souceNum = 0;
    private int num;


    @Override
    protected int getlayoutRes() {
        return R.layout.dialog_change_souce;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        tvResource_0 = findView(R.id.tv_resource_0);
        tvResource_1 = findView(R.id.tv_resource_1);
        tvResource_2 = findView(R.id.tv_resource_2);
        tvResource_3 = findView(R.id.tv_resource_3);
        tvResource_4 = findView(R.id.tv_resource_4);
        tvResource_5 = findView(R.id.tv_resource_5);
        tvResource_0.setOnKeyListener(this);
        tvResource_1.setOnKeyListener(this);
        tvResource_2.setOnKeyListener(this);
        tvResource_3.setOnKeyListener(this);
        tvResource_4.setOnKeyListener(this);
        tvResource_5.setOnKeyListener(this);
        setFouce();
    }

    private void setFouce() {
        switch (souceNum) {
            case 0:
                tvResource_0.requestFocus();
                break;
            case 1:
                tvResource_1.requestFocus();
                break;
            case 2:
                tvResource_2.requestFocus();
                break;
            case 3:
                tvResource_3.requestFocus();
                break;
            case 4:
                tvResource_4.requestFocus();
                break;
            case 5:
                tvResource_5.requestFocus();
                break;
        }
        switch (num) {
            case 0:
                tvResource_0.setVisibility(View.GONE);
                tvResource_1.setVisibility(View.GONE);
                tvResource_2.setVisibility(View.GONE);
                tvResource_3.setVisibility(View.GONE);
                tvResource_4.setVisibility(View.GONE);
                tvResource_5.setVisibility(View.GONE);
                break;
            case 1:
                tvResource_1.setVisibility(View.GONE);
                tvResource_2.setVisibility(View.GONE);
                tvResource_3.setVisibility(View.GONE);
                tvResource_4.setVisibility(View.GONE);
                tvResource_5.setVisibility(View.GONE);
                break;
            case 2:
                tvResource_2.setVisibility(View.GONE);
                tvResource_3.setVisibility(View.GONE);
                tvResource_4.setVisibility(View.GONE);
                tvResource_5.setVisibility(View.GONE);
                break;
            case 3:
                tvResource_3.setVisibility(View.GONE);
                tvResource_4.setVisibility(View.GONE);
                tvResource_5.setVisibility(View.GONE);
                break;
            case 4:
                tvResource_4.setVisibility(View.GONE);
                tvResource_5.setVisibility(View.GONE);
            case 5:
                tvResource_5.setVisibility(View.GONE);
                break;
        }
    }
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                switch (v.getId()) {
                    case R.id.tv_resource_0:
                        changeResource(0);
                        return true;
                    case R.id.tv_resource_1:
                        changeResource(1);
                        return true;
                    case R.id.tv_resource_2:
                        changeResource(2);
                        return true;
                    case R.id.tv_resource_3:
                        changeResource(3);
                        return true;
                    case R.id.tv_resource_4:
                        changeResource(4);
                        return true;
                    case R.id.tv_resource_5:
                        changeResource(5);
                        return true;
                }
            }/*else if (keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
               if (v.getId()==R.id.tv_resource_5){
                   tvResource_0.requestFocus();
               }
            }else if (keyCode==KeyEvent.KEYCODE_DPAD_LEFT){
                if (v.getId()==R.id.tv_resource_0){
                    tvResource_5.requestFocus();
                }
            }*/
        }
        return false;
    }

    public void setCurentSouce(int souceNum ,int num) {
        this.souceNum = souceNum;
        this.num = num;
    }

    private void changeResource(int i) {
        souceNum = i;
        if (onchangeClickListener != null) {
            onchangeClickListener.onchangeSouce(i);
        }
        dismiss();
    }

    public interface OnchangeClickListener {
        void onchangeSouce(int souceNum);
    }

    private OnchangeClickListener onchangeClickListener;

    public void setOnSouceChangeClickListener(OnchangeClickListener onchangeClickListener) {
        this.onchangeClickListener = onchangeClickListener;
    }

}
