package com.xk.xkds.component.dialog;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.xk.xkds.R;

/**
 * Created  LJP on 2017/6/29.
 */

public abstract class BaseDialog extends DialogFragment {
    private View parent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.Dialog);
        parent = inflater.inflate(getlayoutRes(), container, false);
        initView();
        initData();
        return parent;

    }


    protected <T extends View> T findView(@IdRes int resId) {
        return (T) parent.findViewById(resId);
    }

    protected abstract int getlayoutRes();
    protected abstract void initView( );
    protected abstract void initData();

}
