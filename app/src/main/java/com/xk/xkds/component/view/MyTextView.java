package com.xk.xkds.component.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xk.xkds.R;
import com.xk.xkds.common.utils.FontUtil;

public class MyTextView extends android.support.v7.widget.AppCompatTextView
{
	// 是否可以触摸回弹
	private boolean mEnableTouchSpring = false;
	private boolean mRemovePadding = false;
	private final boolean mEnableCircleAnimation;


	/*************** public method ********************/

	// 是否开启facebook动画
	public void setTouchSprint(boolean opened)
	{
		mEnableTouchSpring = opened;
		setClickable(mEnableTouchSpring);
	}

	/*************** public method ********************/

	public MyTextView(Context context)
	{
		this(context, null);
	}

	public MyTextView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public MyTextView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);

		TypedArray a = context
				.obtainStyledAttributes(attrs, R.styleable.MyTextView);

		float textSize = a.getDimension(R.styleable.MyTextView_textSize, 0);
		if( 0 != textSize )
		{
			getPaint().setTextSize(textSize);
		}
		mEnableTouchSpring = a
				.getBoolean(R.styleable.MyTextView_enableTouchSpring, false);
        mRemovePadding = a
				.getBoolean(R.styleable.MyTextView_removePadding, false);
		mEnableCircleAnimation = a
				.getBoolean(R.styleable.MyTextView_enableCircleAnimation, false);

		int mFont = a.getInt(R.styleable.MyTextView_fontFamily, 0);
		if( mFont == 1 )
		{
			FontUtil.setFontFamilyFontawesome(this);
		}
		else if( mFont == 2 )
		{
			FontUtil.setFontFamilyFzlt(this);
		}

		if( mEnableTouchSpring )
		{
			this.setClickable(true);
		}
		if( mEnableCircleAnimation )
		{
			this.setClickable(true);
		}

		a.recycle();
	}

	@Override
	protected void onAttachedToWindow()
	{
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
	}


	Paint.FontMetricsInt fontMetricsInt;

	@Override
	protected void onDraw(Canvas canvas)
	{
		if( mRemovePadding )
		{// 设置是否remove间距，true为remove
			if( fontMetricsInt == null )
			{
				fontMetricsInt = new Paint.FontMetricsInt();
				getPaint().getFontMetricsInt(fontMetricsInt);
			}
			canvas.translate(0, fontMetricsInt.top - fontMetricsInt.ascent);
		}
		super.onDraw(canvas);
	}
}
