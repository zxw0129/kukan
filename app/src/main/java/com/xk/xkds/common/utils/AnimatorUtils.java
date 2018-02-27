package com.xk.xkds.common.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created  on 2017/2/9.
 * view放大和缩小动画
 */

public class AnimatorUtils  {
    public static void viewFocus(View view,boolean hasFocus){
        if (hasFocus) {
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.05f);
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.05f);
            AnimatorSet set  = new AnimatorSet();
            set.setDuration(300);
            set.playTogether(animatorX,animatorY);
            set.start();
        }else {
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "scaleX", 1.05f, 1f);
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "scaleY", 1.05f, 1f);
            AnimatorSet set  = new AnimatorSet();
            set.setDuration(300);
            set.playTogether(animatorX,animatorY);
            set.start();
        }
    }
}
