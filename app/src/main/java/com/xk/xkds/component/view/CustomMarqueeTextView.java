package com.xk.xkds.component.view;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Created  on 2016/11/29. 跑马灯testview
 */
public class CustomMarqueeTextView extends android.support.v7.widget.AppCompatTextView {
    public CustomMarqueeTextView(Context context) {
        super(context);
        createView();
    }

    public CustomMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createView();
    }

    public CustomMarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        createView();
    }

    private void createView() {
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(-1);
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        if (focused) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean focused) {
        if (focused) {
            super.onWindowFocusChanged(focused);
        }
    }

    @Override
    public boolean isFocused() {
        return true;
    }

}
