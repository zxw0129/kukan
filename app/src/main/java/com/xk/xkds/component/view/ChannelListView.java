package com.xk.xkds.component.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xk.xkds.R;
import com.xk.xkds.common.utils.ChannelResourceUtils;
import com.xk.xkds.common.utils.FileUtils;
import com.xk.xkds.common.utils.LogUtlis;
import com.xk.xkds.component.adapter.ChangeChannerAdapter;
import com.xk.xkds.entity.ChannelBean;

import java.util.ArrayList;

import io.vov.vitamio.widget.VideoView;

/**
 * Created  on 2017/2/20.
 */

public class ChannelListView extends RelativeLayout implements View.OnFocusChangeListener,
        ChangeChannerAdapter.OnchangeClickListener, View.OnKeyListener {

    private TextView mTv_1;
    private TextView mTv_4;
    private TextView mTv_8;
    private TextView mTv_7;
    private TextView mTv_2;
    private ScollRecycleView mRecyclerView;
    private Context mContext;
    private ChangeChannerAdapter mAdapter;
    private TextView mTv_5;
    private TextView mTv_6;
    private TextView mTv_3;
    private VideoView mVideoView;
    private TextView mTv_9;
    private int mLastSelect = 0;
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
    private int mCurrentPosition;
    private RelativeLayout mRltDefinedChannel;
    private Button mBtCleanAll;


    public ChannelListView(Context context) {
        this(context, null);
    }

    public ChannelListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChannelListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_playdetail_change_channel, this);
        mTv_1 = (TextView) findViewById(R.id.tv_channel_1);
        mTv_2 = (TextView) findViewById(R.id.tv_channel_2);
        mTv_3 = (TextView) findViewById(R.id.tv_channel_3);
        mTv_4 = (TextView) findViewById(R.id.tv_channel_4);
        mTv_5 = (TextView) findViewById(R.id.tv_channel_5);
        mTv_6 = (TextView) findViewById(R.id.tv_channel_6);
        mTv_7 = (TextView) findViewById(R.id.tv_channel_7);
        mTv_8 = (TextView) findViewById(R.id.tv_channel_8);
        mTv_9 = (TextView) findViewById(R.id.tv_channel_9);
        mRecyclerView = (ScollRecycleView) findViewById(R.id.rlv_playdetail_channel_deatil);
        mTv_1.setOnFocusChangeListener(this);
        mTv_4.setOnFocusChangeListener(this);
        mTv_3.setOnFocusChangeListener(this);
        mTv_6.setOnFocusChangeListener(this);
        mTv_5.setOnFocusChangeListener(this);
        mTv_8.setOnFocusChangeListener(this);
        mTv_7.setOnFocusChangeListener(this);
        mTv_2.setOnFocusChangeListener(this);
        mTv_9.setOnFocusChangeListener(this);
        mTv_1.setOnKeyListener(this);
        mTv_4.setOnKeyListener(this);
        mTv_3.setOnKeyListener(this);
        mTv_6.setOnKeyListener(this);
        mTv_5.setOnKeyListener(this);
        mTv_8.setOnKeyListener(this);
        mTv_7.setOnKeyListener(this);
        mTv_2.setOnKeyListener(this);
        mTv_9.setOnKeyListener(this);
        mListDatas = ChannelResourceUtils.getInstace().getAllList();
        loadList(0);
        mAdapter = new ChangeChannerAdapter(mContext, mListDatas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnchangeClickListener(this);
        /*//自定义频道界面
        mRltDefinedChannel = (RelativeLayout) findViewById(R.id.rlt_defined_channel);
        mBtCleanAll = (Button) findViewById(R.id.bt_clean_all_defined_channel);
        mBtCleanAll.setOnKeyListener(this);
        mRltDefinedChannel.setVisibility(GONE);*/

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
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
            }
        }
        LogUtlis.getInstance().showLogE("xxx  load has = " + mListDatas.size());
        if (FileUtils.newCopy) {
            addSelefList();
            FileUtils.newCopy = false;
        }
    }

    public void addSelefList() {
        ArrayList<ChannelBean> selfList = FileUtils.getSelfList();
        if (null != selfList && selfList.size() > 0) {
            for (int i = 0; i < selfList.size(); i++) {
                mList9.add(selfList.get(i));
            }
        }
    }


    public void setFocus(int position) {
        mCurrentPosition = position - 1;
        mackAllSelectFalse();
        mAdapter.setPlayingPosition(position);
        if (mCurrentPosition > 0 && mCurrentPosition <mListDatas.size()) {
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
//                mTv_4.setSelected(true);
//                mAdapter.setData(mList4);
//                childPosition = getChildPosition(mList4, position - 1);
//                if (mRecyclerView.getChildCount() > childPosition) {
//                    mRecyclerView.getChildAt(childPosition).requestFocus();
//                }
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
        }
    }

    /***
     * 在滑动或者计算layout的时候不刷新数据:
     * 判断是否正在滑动的方法是getScrollState(),如果它等于RecyclerView.SCROLL_STATE_IDLE说明不在滑动,
     * 判断有没有在计算layout的方法是isComputingLayout(),取否就可以得到不在计算的时候,通过这两个条件来决定是否刷新数据
     *
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
                case R.id.tv_channel_9: //自定义
                    selectOneTextView(9);
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
        mRltDefinedChannel.setVisibility(GONE);
    }

    @Override
    public void onchangeChannel(View v, int channelNum) {
        if (onchangeClickListener != null) {
            onchangeClickListener.onchangeChannel(v, channelNum);
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
           /* if (v.getId() == R.id.bt_clean_all_defined_channel) {
                if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                    //清空自定义
//                    FileUtils.deletailFile();
//                    mAdapter.notifyDataSetChanged();
//                    mBtCleanAll.setText("已清空");
                }
            }*/
        }
        return false;
    }

    /**
     * 获得当前电视节目,再集合位置
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

    public void setOnchangeClickListener(OnchangeClickListener onchangeClickListener) {
        this.onchangeClickListener = onchangeClickListener;
    }

}
