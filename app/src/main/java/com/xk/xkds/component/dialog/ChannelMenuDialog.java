package com.xk.xkds.component.dialog;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.xk.xkds.R;
import com.xk.xkds.common.utils.ChannelResourceUtils;
import com.xk.xkds.common.utils.LogUtlis;
import com.xk.xkds.common.utils.SpUtils;
import com.xk.xkds.component.adapter.ChangeChannerAdapter;
import com.xk.xkds.component.view.ScollRecycleView;
import com.xk.xkds.entity.ChannelBean;

import java.util.ArrayList;

import static com.xk.xkds.common.base.Global.mContext;


/**
 * Created  LJP on 2017/6/29.
 * 菜单页
 */

public class ChannelMenuDialog extends BaseDialog implements ChangeChannerAdapter
        .OnchangeClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    private TextView mTv_1;
    private TextView mTv_2;
    private TextView mTv_3;
    private TextView mTv_4;
    private TextView mTv_5;
    private TextView mTv_6;
    private TextView mTv_7;
    private TextView mTv_8;
    private TextView mTv_9;
    private TextView mTv_10;
    private ScollRecycleView mRecyclerView;
    private ChangeChannerAdapter mAdapter;
    private ArrayList<ChannelBean> mListDatas;
    private ArrayList<ChannelBean> mList1 = new ArrayList<>();
    private ArrayList<ChannelBean> mList2 = new ArrayList<>();
    private ArrayList<ChannelBean> mList3 = new ArrayList<>();
    private ArrayList<ChannelBean> mList4 = new ArrayList<>();
    private ArrayList<ChannelBean> mList5 = new ArrayList<>();
    private ArrayList<ChannelBean> mList6 = new ArrayList<>();
    private ArrayList<ChannelBean> mList7 = new ArrayList<>();
    private ArrayList<ChannelBean> mList8 = new ArrayList<>();
    private ArrayList<ChannelBean> mList9 = new ArrayList<>();
    private ArrayList<ChannelBean> mList10 = new ArrayList<>();
    private int mCurrentPosition;

    @Override
    protected int getlayoutRes() {
        return R.layout.view_playdetail_change_channel;
    }

    @Override
    protected void initView() {
        mTv_1 = findView(R.id.tv_channel_1);
        mTv_2 = findView(R.id.tv_channel_2);
        mTv_3 = findView(R.id.tv_channel_3);
        mTv_4 = findView(R.id.tv_channel_4);
        mTv_5 = findView(R.id.tv_channel_5);
        mTv_6 = findView(R.id.tv_channel_6);
        mTv_7 = findView(R.id.tv_channel_7);
        mTv_8 = findView(R.id.tv_channel_8);
        mTv_9 = findView(R.id.tv_channel_9);
        mTv_10 = findView(R.id.tv_channel_10);
        mRecyclerView = findView(R.id.rlv_playdetail_channel_deatil);
        mTv_1.setOnFocusChangeListener(this);
        mTv_2.setOnFocusChangeListener(this);
        mTv_3.setOnFocusChangeListener(this);
        mTv_4.setOnFocusChangeListener(this);
        mTv_5.setOnFocusChangeListener(this);
        mTv_6.setOnFocusChangeListener(this);
        mTv_7.setOnFocusChangeListener(this);
        mTv_8.setOnFocusChangeListener(this);
        mTv_9.setOnFocusChangeListener(this);
        mTv_10.setOnFocusChangeListener(this);
        mTv_1.setOnKeyListener(this);
        mTv_2.setOnKeyListener(this);
        mTv_3.setOnKeyListener(this);
        mTv_4.setOnKeyListener(this);
        mTv_5.setOnKeyListener(this);
        mTv_6.setOnKeyListener(this);
        mTv_7.setOnKeyListener(this);
        mTv_8.setOnKeyListener(this);
        mTv_9.setOnKeyListener(this);
        mTv_10.setOnKeyListener(this);
        mListDatas = ChannelResourceUtils.getInstace().getAllList();
        loadList(0);
        mAdapter = new ChangeChannerAdapter(mContext, mListDatas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnchangeClickListener(this);
        setFocus(SpUtils.getInstance().getPosition());
    }

    @Override
    protected void initData() {

    }

    private void loadList(int position) {
        mListDatas.get(position).selected = true;
        mList1.clear();
        mList2.clear();
        mList3.clear();
        mList4.clear();
        mList5.clear();
        mList6.clear();
        mList7.clear();
        mList8.clear();
        mList9.clear();
        mList10.clear();
        for (int i = 0; i < mListDatas.size(); i++) {
            int channelType = mListDatas.get(i).getChannelType();
            if (channelType == 1) {
                mList1.add(mListDatas.get(i));
            } else if (channelType == 2) {
                mList2.add(mListDatas.get(i));
            } else if (channelType == 3) {
                mList3.add(mListDatas.get(i));
            } else if (channelType == 4) {
                mList4.add(mListDatas.get(i));
            } else if (channelType == 5) {
                mList5.add(mListDatas.get(i));
            } else if (channelType == 6) {
                mList6.add(mListDatas.get(i));
            } else if (channelType == 7) {
                mList7.add(mListDatas.get(i));
            } else if (channelType == 8) {
                mList8.add(mListDatas.get(i));
            } else if (channelType == 9) {
                mList9.add(mListDatas.get(i));
            } else if (channelType == 10) {
                mList10.add(mListDatas.get(i));
            }
        }
        if (mList1.size() == 0) mTv_1.setVisibility(View.GONE);
        if (mList2.size() == 0) mTv_2.setVisibility(View.GONE);
        if (mList3.size() == 0) mTv_3.setVisibility(View.GONE);
        if (mList4.size() == 0) mTv_4.setVisibility(View.GONE);
        if (mList5.size() == 0) mTv_5.setVisibility(View.GONE);
        if (mList6.size() == 0) mTv_6.setVisibility(View.GONE);
        if (mList7.size() == 0) mTv_7.setVisibility(View.GONE);
        if (mList8.size() == 0) mTv_8.setVisibility(View.GONE);
        if (mList9.size() == 0) mTv_9.setVisibility(View.GONE);
        if (mList10.size() == 0) mTv_10.setVisibility(View.GONE);

        LogUtlis.getInstance().showLogE("xxx  load has = " + mListDatas.size());
    }

    public void setFocus(int position) {
        mCurrentPosition = position - 1;
        mackAllSelectFalse();
        mAdapter.setPlayingPosition(position);
        if (mCurrentPosition > 0 && mCurrentPosition < mListDatas.size()) {
            int channelType = mListDatas.get(mCurrentPosition).getChannelType();
            selectOneTextView(channelType);
        }
    }

    private void selectOneTextView(int channelType) {
        if (!checkRecycleViewStatus()) return;
        int childPosition = 0;
        switch (channelType) {
            case 1:
                mTv_1.requestFocus();
                mTv_1.setSelected(true);
                mAdapter.setData(mList1);
                childPosition = getChildPosition(mList1, mCurrentPosition);
                mRecyclerView.moveToPosition(childPosition);
                break;
            case 2:
                mTv_2.setSelected(true);
                mTv_2.requestFocus();
                mAdapter.setData(mList2);
                childPosition = getChildPosition(mList2, mCurrentPosition);
                mRecyclerView.moveToPosition(childPosition);
                break;
            case 3:
                mTv_3.setSelected(true);
                mTv_3.requestFocus();
                mAdapter.setData(mList3);
                childPosition = getChildPosition(mList3, mCurrentPosition);
                mRecyclerView.moveToPosition(childPosition);
                break;
            case 4:
                mTv_4.setSelected(true);
                mTv_4.requestFocus();
                mAdapter.setData(mList4);
                LogUtlis.getInstance().showLogE("mRecyclerView.getChildCount" + mRecyclerView
                        .getChildCount());
                childPosition = getChildPosition(mList4, mCurrentPosition);
                LogUtlis.getInstance().showLogE("mRecyclerView.childPosition" + childPosition);
                mRecyclerView.moveToPosition(childPosition);
                break;
            case 5:
                mTv_5.setSelected(true);
                mTv_5.requestFocus();
                mAdapter.setData(mList5);
                childPosition = getChildPosition(mList5, mCurrentPosition);
                mRecyclerView.moveToPosition(childPosition);
                break;
            case 6:
                mTv_6.setSelected(true);
                mTv_6.requestFocus();
                mAdapter.setData(mList6);
                childPosition = getChildPosition(mList6, mCurrentPosition);
                mRecyclerView.moveToPosition(childPosition);
                break;
            case 7:
                mTv_7.setSelected(true);
                mTv_7.requestFocus();
                mAdapter.setData(mList7);
                childPosition = getChildPosition(mList7, mCurrentPosition);
                mRecyclerView.moveToPosition(childPosition);
                break;
            case 8:
                mTv_8.setSelected(true);
                mTv_8.requestFocus();
                mAdapter.setData(mList8);
                childPosition = getChildPosition(mList8, mCurrentPosition);
                mRecyclerView.moveToPosition(childPosition);
                break;
            case 9:
                mTv_9.setSelected(true);
                mTv_9.requestFocus();
                mAdapter.setData(mList9);
                childPosition = getChildPosition(mList9, mCurrentPosition);
                mRecyclerView.moveToPosition(childPosition);
                break;
            case 10:
                mTv_10.setSelected(true);
                mTv_10.requestFocus();
                mAdapter.setData(mList10);
                childPosition = getChildPosition(mList10, mCurrentPosition);
                mRecyclerView.moveToPosition(childPosition);
                break;
        }
    }

    /***
     * 在滑动或者计算layout的时候不刷新数据:
     * 判断是否正在滑动的方法是getScrollState(),如果它等于RecyclerView.SCROLL_STATE_IDLE说明不在滑动,
     * 判断有没有在计算layout的方法是isComputingLayout(),取否就可以得到不在计算的时候,通过这两个条件来决定是否刷新数据
     * @return
     */
    private boolean checkRecycleViewStatus() {
        int scrollBarStyle = mRecyclerView.getScrollBarStyle();
        if (scrollBarStyle == RecyclerView.SCROLL_STATE_IDLE && !mRecyclerView
                .isComputingLayout()) {
            return true;
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();
        //改变选中状态
        //显示右边列表
        if (hasFocus) {
            mackAllSelectFalse();
            switch (id) {
                case R.id.tv_channel_1: //CCTV
                    selectOneTextView(1);
                    break;
                case R.id.tv_channel_2://卫视
                    selectOneTextView(2);
                    break;
                case R.id.tv_channel_3: //高清
                    selectOneTextView(3);
                    break;
                case R.id.tv_channel_4: //乐视
                    selectOneTextView(4);
                    break;
                case R.id.tv_channel_5: //体育
                    selectOneTextView(5);
                    break;
                case R.id.tv_channel_6: //动漫
                    selectOneTextView(6);
                    break;
                case R.id.tv_channel_7://海外
                    selectOneTextView(7);
                    break;
                case R.id.tv_channel_8: //其他
                    selectOneTextView(8);
                    break;
                case R.id.tv_channel_9: //其他
                    selectOneTextView(9);
                    break;
                case R.id.tv_channel_10: //其他本地频道
                    selectOneTextView(10);
                    break;
            }
        }

    }

    private void mackAllSelectFalse() {
        mTv_1.setSelected(false);
        mTv_2.setSelected(false);
        mTv_3.setSelected(false);
        mTv_4.setSelected(false);
        mTv_5.setSelected(false);
        mTv_6.setSelected(false);
        mTv_7.setSelected(false);
        mTv_8.setSelected(false);
        mTv_9.setSelected(false);
        mTv_10.setSelected(false);
    }

    @Override
    public void onchangeChannel(View v, int channelNum) {
        if (onchangeClickListener != null) {
            onchangeClickListener.onchangeChannel(v, channelNum);
            dismiss();
        }
    }

    @Override
    public void onLeftKeyDown(int type) {
        selectOneTextView(type);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (mRecyclerView.getChildCount() > 0) {
                    mRecyclerView.getChildAt(0).requestFocus();
                    mAdapter.isToLeft = false;
                }
            }
        }
        return false;
    }

    /**
     * 获得当前电视节目,在集合位置
     *
     * @param mList
     * @param currentPosition
     * @return
     */
    public int getChildPosition(ArrayList<ChannelBean> mList, int currentPosition) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getChannelName().equals(mListDatas.get(currentPosition)
                    .getChannelName())) {
                return i;
            }
        }
        return 0;
    }

    public interface OnchangeClickListener {
        void onchangeChannel(View v, int channelNum);
    }

    private OnchangeClickListener onchangeClickListener;

    public void setOnChannelChangeClickListener(OnchangeClickListener onchangeClickListener) {
        this.onchangeClickListener = onchangeClickListener;
    }

}
