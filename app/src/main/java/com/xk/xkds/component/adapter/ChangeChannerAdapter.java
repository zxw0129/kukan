package com.xk.xkds.component.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xk.xkds.R;
import com.xk.xkds.common.utils.GsonUtil;
import com.xk.xkds.component.view.CustomMarqueeTextView;
import com.xk.xkds.entity.ChannelBean;
import com.xk.xkds.entity.ParamBean;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created  on 2017/2/20.
 */

public class ChangeChannerAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<ChannelBean> mChannelList = new ArrayList<>();
    private int MCurrentPosition;
    public int isFirst = 0;
    public boolean isToLeft = false;
    OkHttpClient mOkHttpClient = new OkHttpClient();
    public ChangeChannerAdapter(Context mContext, ArrayList<ChannelBean> mChannelList) {
        this.mContext = mContext;
        this.mChannelList.addAll(mChannelList);
    }

    public void setData(ArrayList<ChannelBean> mChannelList) {
        this.mChannelList.clear();
        this.mChannelList.addAll(mChannelList);
        isFirst = 0;
        notifyDataSetChanged();
    }

    /***
     * 修改当前播放的台号
     * @param position
     */
    public void setPlayingPosition(int position) {
        this.MCurrentPosition = position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_channel,
                parent, false);
        ChannelHodel hodel = new ChannelHodel(inflate);
        return hodel;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ChannelHodel mHodel = (ChannelHodel) holder;
        mHodel.mchannelName.setText(mChannelList.get(position).getChannelName());
        mHodel.mchannelNum.setText(mChannelList.get(position).getChannelNum() + "");
        if (MCurrentPosition == mChannelList.get(position).getChannelNum()) {
            mHodel.mchannelPlayingIcon.setVisibility(View.VISIBLE);
            mHodel.mchannelNum.setVisibility(View.GONE);
            mHodel.mLinearLayout.setSelected(true);
            if (!isToLeft) {
                mHodel.mLinearLayout.requestFocus();
            }
        } else {
            mHodel.mchannelPlayingIcon.setVisibility(View.GONE);
            mHodel.mchannelNum.setVisibility(View.VISIBLE);
            mHodel.mLinearLayout.setSelected(false);
        }
        mHodel.tvChannelParam.setSelected(true);
        String url = "http://api.cntv.cn/epg/epginfo3?serviceId=cbox&c=" + mChannelList.get
                (position).getChannelId() + "&d=" + getData();
//        LogUtlis.getInstance().showLogE(" URL = " + url);
        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.method("GET", null);
        Request request = requestBuilder.build();
        int channelType = mChannelList.get(position).getChannelType();
        if (channelType!=10){
            Call mcall = mOkHttpClient.newCall(request);
            mcall.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) {
                    String string = null;
                    try {
                        string = response.body().string();
                        if (null != string) {
                            string = string.replace(mChannelList.get(position).getChannelId(),
                                    "liveBean");
                            ParamBean parse = GsonUtil.parse(string, ParamBean.class);
                            if (parse == null)
                            {
                                return;
                            }
                            if( null == parse.getliveBean() )
                            {
                                return;
                            }
                            String isLive = parse.getliveBean().getIsLive();
                            if ( TextUtils.isEmpty(isLive) )
                            {
                                return;
                            }
                            mHodel.tvChannelParam.setText(isLive);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        mHodel.mLinearLayout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                    if (isFirst == 0) {
                        isFirst = 1;
                        return true;
                    }
                    if (onchangeClickListener != null) {
                        onchangeClickListener.onchangeChannel(v, mChannelList.get(position)
                                .getChannelNum());
                    }
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    isToLeft = true;
                    if (event.getAction() == KeyEvent.ACTION_UP) return false;
                    if (onchangeClickListener != null) {
                        onchangeClickListener.onLeftKeyDown(mChannelList.get(position)
                                .getChannelType());
                        return true;
                    }
                }
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return mChannelList.size();
    }

    public String getData() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String d = format.format(System.currentTimeMillis());
        System.out.println("Format To String(Date):" + d);
        return d;
    }


//    public void changeSelect(int mCurrentKey) {
//        mChannelList.get(mLastSelect).selected = false; //改变原来的
//        mLastSelect = mCurrentKey - 1;
//        mChannelList.get(mLastSelect).selected = true; //修改现在选中的
//    }

    class ChannelHodel extends RecyclerView.ViewHolder {

        private final TextView mchannelName;
        private final RelativeLayout mLinearLayout;
        private final TextView mchannelNum;
        private final ImageView mchannelPlayingIcon;
        private final CustomMarqueeTextView tvChannelParam;

        public ChannelHodel(View itemView) {
            super(itemView);
            mLinearLayout = (RelativeLayout) itemView.findViewById(R.id.llt_channel_detail);
            mchannelName = (TextView) itemView.findViewById(R.id.tv_channel_name);
            mchannelNum = (TextView) itemView.findViewById(R.id.tv_channel_num);
            mchannelPlayingIcon = (ImageView) itemView.findViewById(R.id.iv_channel_playing);
            tvChannelParam = (CustomMarqueeTextView) itemView.findViewById(R.id
                    .tv_channel_play_param);
        }
    }

    public interface OnchangeClickListener {
        void onchangeChannel(View v, int channelNum);

        void onLeftKeyDown(int type);
    }

    private OnchangeClickListener onchangeClickListener;

    public void setOnchangeClickListener(OnchangeClickListener onchangeClickListener) {
        this.onchangeClickListener = onchangeClickListener;
    }

    private String decodeUnicode(String str) {
        Charset set = Charset.forName("UTF-16");
        Pattern p = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
        Matcher m = p.matcher(str);
        int start = 0;
        int start2 = 0;
        StringBuffer sb = new StringBuffer();
        while (m.find(start)) {
            start2 = m.start();
            if (start2 > start) {
                String seg = str.substring(start, start2);
                sb.append(seg);
            }
            String code = m.group(1);
            int i = Integer.valueOf(code, 16);
            byte[] bb = new byte[4];
            bb[0] = (byte) ((i >> 8) & 0xFF);
            bb[1] = (byte) (i & 0xFF);
            ByteBuffer b = ByteBuffer.wrap(bb);
            sb.append(String.valueOf(set.decode(b)).trim());
            start = m.end();
        }
        start2 = str.length();
        if (start2 > start) {
            String seg = str.substring(start, start2);
            sb.append(seg);
        }
        return sb.toString();
    }
}
