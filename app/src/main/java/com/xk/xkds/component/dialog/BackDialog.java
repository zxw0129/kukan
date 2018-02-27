package com.xk.xkds.component.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.xk.xkds.R;
import com.xk.xkds.common.base.Global;
import com.xk.xkds.common.manager.FileManager;
import com.xk.xkds.common.utils.AppUtil;
import com.xk.xkds.common.utils.FinshUtil;
import com.xk.xkds.common.utils.UpdataUtils;
import com.xk.xkds.component.adapter.BackRecyAdapter;
import com.xk.xkds.entity.ADBean;

import java.util.List;

/**
 * Created  on 2017/3/3.
 */

public class BackDialog extends DialogFragment implements View.OnKeyListener, BackRecyAdapter
        .OnItemClick {
    private View parent;
    private TextView mTvBack;
    private TextView mTvCancel;
    private RecyclerView rvBack;
    private BackRecyAdapter adapter;
    private List<ADBean.ListBean> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.Dialog);
        parent = inflater.inflate(R.layout.dialog_back_new, container, false);
        mTvBack = (TextView) parent.findViewById(R.id.tv_back_Yes);
        mTvCancel = (TextView) parent.findViewById(R.id.tv_back_no);
        rvBack = (RecyclerView) parent.findViewById(R.id.rv_back);
        mTvBack.setOnKeyListener(this);
        mTvCancel.setOnKeyListener(this);
        initData();
        return parent;
    }

    private void initData() {
        rvBack.setLayoutManager(new LinearLayoutManager(Global.mContext, LinearLayoutManager
                .HORIZONTAL, false));

        ADBean adBean = UpdataUtils.getAdBean();
        if (adBean != null) {
            list = adBean.getList();
            if (list != null) {
                adapter = new BackRecyAdapter(Global.mContext, list);
                adapter.setOnItemClickListener(this);
                rvBack.setAdapter(adapter);
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mTvBack.requestFocus();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                if (v.getId() == R.id.tv_back_Yes) {
                    goback();
                    return true;
                } else if (v.getId() == R.id.tv_back_no) {
                    dismiss();
                    return true;
                }
            }
            if (v.getId() == R.id.tv_ad_install && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                return true;
            }
        }

        return false;
    }

    private void goback() {
        FinshUtil.getInstance().finishAllActivity();
    }

    @Override
    public void onItemClick(int position) {
        ADBean.ListBean listBean = list.get(position);
        if (AppUtil.isInstalled(Global.mContext, listBean.getPackageName())) {
            MobclickAgent.onEvent(Global.mContext, "back_click", listBean.getAPKName());
            AppUtil.openApp(Global.mContext,listBean.getPackageName());
        } else {
            UpdataUtils.updataServer(FileManager.getApksDownloadPath(Global.mContext) + listBean
                    .getAPKName() + ".apk", listBean.getAPKUrl());
            Global.showToast("正在下载,请稍后");
            MobclickAgent.onEvent(Global.mContext, "back_installed", listBean.getAPKName());
        }
        dismiss();

    }
}
