package com.xk.xkds.common.utils;

import android.graphics.Typeface;
import android.widget.TextView;


public class FontUtil
{

	private static Typeface mTypefaceFontawesome;
	private static Typeface mTypefaceNotoSansHans;

	public static void setFontFamilyFontawesome(TextView view)
	{
		if( null == mTypefaceFontawesome )
		{
			mTypefaceFontawesome = Typeface.createFromAsset(view.getContext()
					.getAssets(), "fonts/FontAwesome.otf");
		}

		view.setTypeface(mTypefaceFontawesome);
	}

	public static void setFontFamilyFzlt(TextView view)
	{
		if( null == mTypefaceNotoSansHans )
		{
			mTypefaceNotoSansHans = Typeface.createFromAsset(view.getContext()
					.getAssets(), "fonts/fzltzhunhk.ttf");
		}

		view.setTypeface(mTypefaceNotoSansHans);
	}
}
