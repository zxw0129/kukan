package com.xk.xkds.component.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xk.xkds.R;
import com.xk.xkds.common.base.Global;
import com.xk.xkds.common.utils.AppUtil;
import com.xk.xkds.common.utils.DownLoadUtils;
import com.xk.xkds.entity.QRDialogBean;

/**
 * Created  LJP on 2017/10/30.
 */

public class QRCodeDialog extends BaseDialog implements View.OnClickListener, DownLoadUtils.DownLoadListener {

    private static String EXTRA_BUILDER ="extra_builder";
    private ImageView ivIcon;
    private TextView tvName,tvInstall,tvPushTips,tvExplain;
    private boolean installed;
    private QRDialogBean bean;
    private DownLoadUtils downLoadUtils;

    public static   QRCodeDialog  getInstance (QRDialogBean builder){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_BUILDER, builder);
        QRCodeDialog qrCodeDialog = new QRCodeDialog();
        qrCodeDialog.setArguments(args);
        return qrCodeDialog;
    }
    @Override
    protected int getlayoutRes() {
        return R.layout.dialog_qrcode;
    }

    @Override
    protected void initView() {
        ivIcon = findView(R.id.app_push_icon);
        tvName = findView(R.id.app_push_name);
        tvInstall = findView(R.id.tv_push_install);
        tvPushTips = findView(R.id.app_push_tips);
        tvExplain = findView(R.id.tv_qr_explain);
        tvInstall.setOnClickListener(this);
    }
    @Override
    protected void initData() {
        bean = (QRDialogBean) getArguments().getSerializable(EXTRA_BUILDER);
        tvName.setText(bean.getAppName());
        Glide.with(Global.mContext).load(bean.getIconUrl()).into(ivIcon);
        tvExplain.setText(bean.getTvExplain());
        installed = AppUtil.isInstalled(getContext(), bean.getPackageName());
        if (installed){
            tvInstall.setText("打开");
        }else {
            tvInstall.setText("安装");
        }
        downLoadUtils = new DownLoadUtils(getContext());
        downLoadUtils.setDownLoadListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tv_push_install){
            if (installed){
                // 打开
                AppUtil.openApp(getContext(),bean.getPackageName());
                dismiss();
            }else {
                //下载 app
                downLoadUtils.downloadAPK(bean.getAppUrl(),bean.getPackageName()+".apk");
            }
        }
    }

    @Override
    public void onSuccess() {
        dismiss();
    }

    @Override
    public void onFiled() {
        dismiss();
    }
}
