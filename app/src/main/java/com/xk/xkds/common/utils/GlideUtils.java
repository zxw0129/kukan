package com.xk.xkds.common.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xk.xkds.component.base.BaseApplication;

/**
 * Created  LJP on 2018/2/2.
 */

public class GlideUtils {
    public static void loadImage(String url, ImageView imageView) {
        Glide.with(BaseApplication.getInstance()).
                load(url).
                diskCacheStrategy(DiskCacheStrategy.NONE).
                into(imageView);
    }
    public static void clear(Context ctx)
    {
        if( null == ctx )
        {
            return;
        }
        ((Activity)ctx).runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Glide.get(BaseApplication.getInstance()).clearDiskCache();
                Glide.get(BaseApplication.getInstance()).clearMemory();
            }
        });

    }
}
