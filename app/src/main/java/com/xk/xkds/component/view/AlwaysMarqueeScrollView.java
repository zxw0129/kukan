package com.xk.xkds.component.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created  on 2017/4/14.
 */

public class AlwaysMarqueeScrollView extends TextView {
    // com.duopin.app.AlwaysMaguequeScrollView
    public AlwaysMarqueeScrollView(Context context) {
        super(context);
    }

    public AlwaysMarqueeScrollView(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    public AlwaysMarqueeScrollView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
    }
}
