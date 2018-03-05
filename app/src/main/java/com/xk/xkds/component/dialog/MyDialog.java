package com.xk.xkds.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;

import com.xk.xkds.R;
import com.xk.xkds.component.view.MyTextView;

public class MyDialog
{
	private Dialog mDialog = null;
	private MyTextView mTitleTv;
	private MyTextView mIconTv;
	private View mSplitLine;
	private MyTextView mMessageTv;
	private MyTextView mOkTv;
	private MyTextView mCancelTv;
	private Context mContext = null;

	private OkOnClickListener mOkListener = null;

	public interface OkOnClickListener
	{
		void onClick();

		void onCancel();
	}

	public void setOnOkClickListener(OkOnClickListener listener)
	{
		mOkListener = listener;
		return;
	}

	public MyDialog(Context ctx)
	{
		mContext = ctx;
		mDialog = new Dialog(ctx);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow()
				.setBackgroundDrawableResource(R.drawable.fn_dialog_bg);
		mDialog.setContentView(R.layout.linkit_dialog_layout);

		if( null == mTitleTv )
		{
			mIconTv = (MyTextView) getDialogView().findViewById(R.id.icon_tv);
			mTitleTv = (MyTextView) getDialogView().findViewById(R.id.title_tv);
			mMessageTv = (MyTextView) getDialogView()
					.findViewById(R.id.content_tv);
			mOkTv = (MyTextView) getDialogView().findViewById(R.id.ok_tv);
			mCancelTv = (MyTextView) getDialogView()
					.findViewById(R.id.cancel_tv);
			mSplitLine = (View) getDialogView().findViewById(R.id.split_line);
		}

		mCancelTv.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if( null != mOkListener )
				{
					mOkListener.onCancel();
				}
				closeDialog();
			}
		});
		mOkTv.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if( null != mOkListener )
				{
					mOkListener.onClick();
				}
				closeDialog();
			}
		});
	}

	public void setOkText(String s)
	{
		mOkTv.setText(s);
	}

	public void setCancelText(String s)
	{
		mCancelTv.setText(s);
	}

	public void setTag(Object tag)
	{
		if( null != mMessageTv )
		{
			mMessageTv.setTag(tag);
		}
		return;
	}

	public Object getTag()
	{
		if( null != mMessageTv )
		{
			return mMessageTv.getTag();
		}
		return null;
	}

	public void setContent(String content)
	{
		if( TextUtils.isEmpty(content) )
		{
			return;
		}
		mMessageTv.setText(content);
		return;
	}

	public void setTitleIcon(String icon)
	{
		if( TextUtils.isEmpty(icon) )
		{
			return;
		}
		mIconTv.setVisibility(View.VISIBLE);
		mTitleTv.setVisibility(View.GONE);
		mIconTv.setText(icon);
		return;
	}

	public void setTitle(String title)
	{
		String titleInfo = "";
		if( !TextUtils.isEmpty(title) )
		{
			titleInfo = title;
		}
		mIconTv.setVisibility(View.GONE);
		mTitleTv.setVisibility(View.VISIBLE);
		mTitleTv.setText(titleInfo);
		return;
	}

	public View getDialogView()
	{
		if( null != mDialog )
		{
			return mDialog.getWindow().getDecorView();
		}
		return null;
	}

	public boolean isShowing()
	{
		return mDialog.isShowing();
	}

	public void showMyDialog()
	{
		if( null != mDialog )
		{
			mDialog.show();
            mOkTv.requestFocus();
		}
		return;
	}

	public void closeDialog()
	{
		if( null != mDialog )
		{
			mDialog.dismiss();
		}
		return;
	}

	public void setCancelAble(boolean cancelAble)
	{
		mDialog.setCancelable(cancelAble);
		return;
	}

	public void setOnCancelListener()
	{
		mCancelTv.setVisibility(View.VISIBLE);
		mSplitLine.setVisibility(View.VISIBLE);
	}

}
