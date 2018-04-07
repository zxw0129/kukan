package com.xk.xkds.component.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.socks.library.KLogUtil;
import com.umeng.analytics.MobclickAgent;
import com.xk.xkds.R;
import com.xk.xkds.common.base.Global;
import com.xk.xkds.common.base.NetMessage;
import com.xk.xkds.common.manager.HttpManger;
import com.xk.xkds.common.manager.IHttpResponse;
import com.xk.xkds.common.utils.AppUtil;
import com.xk.xkds.common.utils.ChannelResourceUtils;
import com.xk.xkds.common.utils.FileUtils;
import com.xk.xkds.common.utils.GlideUtils;
import com.xk.xkds.common.utils.LogUtlis;
import com.xk.xkds.common.utils.SpUtils;
import com.xk.xkds.common.utils.StringUtils;
import com.xk.xkds.common.utils.UpdataUtils;
import com.xk.xkds.component.base.BaseActivity;
import com.xk.xkds.component.base.BaseApplication;
import com.xk.xkds.component.dialog.BackDialog;
import com.xk.xkds.component.dialog.ChangeSouceDialog;
import com.xk.xkds.component.dialog.ChannelMenuDialog;
import com.xk.xkds.component.dialog.UpdataDialog;
import com.xk.xkds.entity.ADBean;
import com.xk.xkds.entity.ChannelBean;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class XkdsActivity extends BaseActivity implements View.OnKeyListener,
        ChannelMenuDialog.OnchangeClickListener, ChangeSouceDialog.OnchangeClickListener {

    private VideoView mVideoView;
    private MediaController mMediaController;
    private String path;
    private Map<Integer, ChannelBean> mChannelMap;
    int mCurrentKey = 1;
    private boolean hasShowError = false;
    private boolean hasShowProgress = false;
    private boolean hasShowChannelInfo = false;
    private boolean hasShowSetting = false;
    private RelativeLayout mProgressBar;
    private TextView mErrorView;
    private TextView mNetSpeed;
    /**
     * 当前播放源
     */
    private int mReSourceNum = 0;
    /*显示台号**/
    private TextView mChannelNumber;
    private TextView mChannelName;
    private RelativeLayout mRltChannelInfo;
    /**
     * 错误超时
     */
    private Runnable runLoadErr;
    /**
     * 显示换台
     */
    private Runnable runShowChannelInfo;
    private boolean hasIntent = false;
    private RelativeLayout mSetting;
    private TextView mChangeControlChannel;
    private TextView mChangeDecode;
    /**
     * false 软解码
     */
    private boolean hardware;
    private boolean isUpTpNext;
    private ADBean adBean;
    /**
     * 存放播放地址的集合
     */
    private List<String> channelSourcesList;
    /***/
    private ChannelMenuDialog channelMenuDialog;
    private ImageView ivShopImage;
    private Runnable runnable;
    private Handler handler;
    private ChangeSouceDialog changeSouceDialog;
    private String Tag = "XkdsActivity";
    private TextView mTvNumChangeChannle;
    private EditText mEditText;
    /**
     * 数字换台
     */
    private String channelNum = "";
    /*数字换台**/
    private Runnable run;
    private View rootView;
    private boolean hasShowKeyboard=false;
    private InputMethodManager imm;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void initIntent(Intent intent) {
        mCurrentKey = intent.getIntExtra("channelNum", 0);
        if (mCurrentKey != 0) {
            hasIntent = true;
        }
    }

    @Override
    protected void initNewIntent(Intent intent) {
        if (intent == null) return;
        mCurrentKey = intent.getIntExtra("channelNum", 0);
        if (mCurrentKey != 0) {
            hasIntent = true;
        }
        checkMap();
        if (mVideoView != null) {
//            playRequset(mCurrentKey);
            playNewChannel();
        }
    }

    private void playNewChannel() {
        mReSourceNum = 0;
        if( mChannelMap.get(mCurrentKey)==null){
            mCurrentKey=0;
        }
        ChannelBean channelBean = mChannelMap.get(mCurrentKey);
        if (channelBean==null )return;
        channelSourcesList = channelBean.getResourceList();
        if (channelSourcesList==null)return;
        if (channelSourcesList.size() == 0) return;
        startPlayVideo(channelSourcesList.get(0));
    }


    @Override
    public void initView() {
        mVideoView = (VideoView) findViewById(R.id.vv_play);
        //初始化 vitamio
        Vitamio.isInitialized(this);
        mErrorView = (TextView) findViewById(R.id.tv_play_detail_errorview);
        //进度条
        mProgressBar = (RelativeLayout) findViewById(R.id.base_progress_bar);
        mNetSpeed = (TextView) findViewById(R.id.tv_new_speed);
        //显示台号
        mChannelNumber = (TextView) findViewById(R.id.tv_play_detail_channel_number);
        mChannelName = (TextView) findViewById(R.id.tv_play_detail_channel_name);
        mRltChannelInfo = (RelativeLayout) findViewById(R.id.rlt_play_detail_channel_info);
        //设置
        mSetting = (RelativeLayout) findViewById(R.id.rlt_seting);
        mChangeControlChannel = (TextView) findViewById(R.id.tv_change_up_down);
        mChangeDecode = (TextView) findViewById(R.id.tv_change_decode);
        mChangeControlChannel.setOnKeyListener(this);
        mChangeDecode.setOnKeyListener(this);
        //广告
        ivShopImage = findView(R.id.iv_shop_view);
        //数字换台
        mTvNumChangeChannle = findView(R.id.tv_num_change_channle);
        mEditText = (EditText) findViewById(R.id.et_EditText);
        //软键盘
        rootView = mChannelName.getRootView();
    }


    @Override
    public void initDatas() {
        settingVideoView();
        isUpTpNext = SpUtils.getInstance().getUpToNext();
        //加载电视台 数据
        mChannelMap = ChannelResourceUtils.getInstace().getChannerMap();
        if (!hasIntent) {
            mCurrentKey = SpUtils.getInstance().getPosition();
        }
        UpdataUtils.resetADBean();

        //请求更新
        checkUpdata();
    }

    private void checkUpdata() {
        HttpManger.getInstace().doGet(NetMessage.UPDATACHECK, new IHttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                if (body == null) return;
                String string = body.string();
                if (string == null) return;
                Log.d(Tag, " onSuccess string = " + string);
                String[] split = string.split(",");
                if (split.length < 2) return;
                int visionCode = Integer.parseInt(split[0]);
                UpdataUtils.updaUrl = split[1];
                int installed = AppUtil.isInstalled(XkdsActivity.this, getPackageName(),
                        visionCode);
                if (installed == AppUtil.LOWER_VERSION) {//更新
                    UpdataDialog dialog = new UpdataDialog();
                    dialog.show(BaseApplication.getInstance().getActivity().getFragmentManager(), "");
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkMap();
        path = mChannelMap.get(mCurrentKey).getResourceList().get(0);
        channelSourcesList = mChannelMap.get(mCurrentKey).getResourceList();
        if (path == null) {
            path = "http://live.52itv.cn/urls/10001";
            mCurrentKey = 1;
        }

        //需要延时一下 不然小米电视打不开
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startPlayVideo(path);
            }
        }, 1000);
        UpdataUtils.getAdBean();
    }
    int count = 0;
    private void checkMap() {
        count++;
        if (count==20){
            ChannelResourceUtils.getInstace().parseResource();
            count=0;
            checkMap();
        }
        if (mChannelMap != null && mCurrentKey <= mChannelMap.size()) {
            count=0;
        } else {
            mChannelMap = ChannelResourceUtils.getInstace().getChannerMap();
            if (mChannelMap != null) {
                if (mCurrentKey > mChannelMap.size()|| mCurrentKey < 0||mChannelMap.get(mCurrentKey).getResourceList()==null) {
                    mCurrentKey = 1;
                    if (mChannelMap.get(mCurrentKey).getResourceList()==null){
                        SystemClock.sleep(1000);
                        checkMap();
                    }
                } else {
                    SystemClock.sleep(1000);
                    checkMap();
                }
            } else {
                SystemClock.sleep(1000);
                checkMap();
            }
        }
    }

    @Override
    protected void onPause()
    {
        if( null != mVideoView )
        {
            mVideoView.pause();
        }
        GlideUtils.clear(this);
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        if( null != mVideoView )
        {
            mVideoView.stopPlayback();
        }
        super.onDestroy();
        FileUtils.cleanList();
        ChannelResourceUtils.getInstace().cleanMap();
        LogUtlis.getInstance().showLogE("xxx" + "onDestroy");
    }

    /**
     * vitamio设置
     */
    private void settingVideoView() {
        //设置播放清晰度 高清
        mVideoView.getHolder().setFormat(PixelFormat.RGBX_8888);
//        设置播放画质 高画质
        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
        //中等画质 清晰度
//        mVideoView.getHolder().setFormat(PixelFormat.RGBX_8888);
//        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_MEDIUM);
        //低等
//        mVideoView.getHolder().setFormat(PixelFormat.RGB_565);
//         mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_LOW);
        /**全屏*/
        mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);
        mMediaController = new MediaController(this);//实例化控制器
        mVideoView.setMediaController(mMediaController);//绑定控制器
        mVideoView.setBufferSize(128 * 1024);
//        mVideoView.requestFocus();//取得焦点
        mVideoView.setFocusable(false);
        hardware = SpUtils.getInstance().getHardware();
        mVideoView.setHardwareDecoder(hardware); //解码方式
        mMediaController.hide(); //隐藏控制器 电视台不需要
        mMediaController.setVisibility(View.GONE); //不显示
    }

    @Override
    public void initListener() {
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                LogUtlis.getInstance().showLogE(getString(R.string.vitamio_prepared));
                mVideoView.start();
                hideProgress();
                hideErrorView();
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                LogUtlis.getInstance().showLogE(getString(R.string.vitamio_error));
                doError();
                return true;
            }

        });
        //播放完成
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
//                LogUtlis.getInstance().showLogE(" vitamio onCompletioned");
            }
        });
        //0
        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START://开始缓冲
                        showProgress();
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END://结束缓冲
                        hideProgress();
                        break;
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        //显示 下载速度
                        if (hasShowProgress) {
                            String netSpeed = StringUtils.getNetSpeed(extra);
                            if (!netSpeed.equals("error")) {
                                mNetSpeed.setText(netSpeed);
                            }
                        }
                        break;
                }
                return false;
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();
                if (!string.equals("")) {
                    saveNum(Integer.parseInt(string));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                 /*判断是否是“GO”键*/
                if(actionId == EditorInfo.IME_ACTION_GO||actionId==EditorInfo.IME_ACTION_DONE){
                    /*隐藏软键盘*/
                  /*  InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }*/
                    changeChannelByNum();
                    mEditText.setVisibility(View.GONE);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view, int id) {
    }

    /**
     * 处理播放错误
     * 如果当前源播放错误 立即播放下一个 知道最后一个播放错误才显示播放错误页面
     */
    private void doError() {
        changeResource(mReSourceNum + 1);

    }


    private void hideErrorView() {
        if (hasShowError) {
            hasShowError = false;
            mErrorView.setVisibility(View.GONE);
        }
    }

    private void showErrorView() {
        if (!hasShowProgress) {
            hasShowError = true;
            mErrorView.setVisibility(View.VISIBLE);
        }
    }

    private void showProgress() {
        if (!hasShowProgress) {
            hasShowProgress = true;
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgress() {
        if (hasShowProgress) {
            hasShowProgress = false;
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void showSet() {
        if (!hasShowSetting) {
            hideSorftkeyboard();
            hasShowSetting = true;
            mSetting.setVisibility(View.VISIBLE);
            mChangeControlChannel.requestFocus();
        }
    }

    private void hideSet() {
        if (hasShowSetting) {
            hasShowSetting = false;
            mSetting.setVisibility(View.GONE);
        }
    }

    /***
     * 隐藏数字换台
     */
    private void hideChannelNum() {
        if (run != null) {
            Global.getHandler().removeCallbacks(run);
        }
        channelNum = "";
        mTvNumChangeChannle.setText("");
        mTvNumChangeChannle.setVisibility(View.GONE);
        mEditText.setVisibility(View.GONE);
    }

    private void hideSorftkeyboard() {
        mEditText.setVisibility(View.GONE);
        if (isKeyboardShow()) {
            hasShowKeyboard = false;
            if (imm == null) {
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            }
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    /***
     * 显示菜单
     */
    private void showMenu() {
        if(isKeyboardShow())return;
        hideSorftkeyboard();
        if (channelMenuDialog == null) {
            channelMenuDialog = new ChannelMenuDialog();
            channelMenuDialog.setOnChannelChangeClickListener(this);
        }
        channelMenuDialog.show(getSupportFragmentManager(), "");
    }


    private void showOtherResource() {
        if (channelSourcesList == null || channelSourcesList.size() == 0) return;
        hideSorftkeyboard();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        if (changeSouceDialog == null) {
            changeSouceDialog = new ChangeSouceDialog();
            changeSouceDialog.setOnSouceChangeClickListener(this);
        }
        changeSouceDialog.show(getSupportFragmentManager(), "");
        changeSouceDialog.setCurentSouce(mReSourceNum, channelSourcesList.size());
        if (runnable == null) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (changeSouceDialog != null) {
                        changeSouceDialog.dismiss();
                    }
                }
            };
        }
        if (handler == null) {
            handler = new Handler();
        }
        handler.postDelayed(runnable, 4000);
    }

    private void showChannelInfo() {
        if (!hasShowChannelInfo) {
            mRltChannelInfo.setVisibility(View.VISIBLE);
            try {
                LogUtlis.getInstance().showLogE("xxx mChannelMap.size" + mChannelMap.size());
                mChannelName.setText(mChannelMap.get(mCurrentKey).getChannelName());
                mChannelNumber.setText(mChannelMap.get(mCurrentKey).getChannelNum() + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (runShowChannelInfo != null) {
                Global.getHandler().removeCallbacks(runShowChannelInfo);
            }
            runShowChannelInfo = new Runnable() {
                @Override
                public void run() {
                    mRltChannelInfo.setVisibility(View.GONE);
                }
            };
            Global.getHandler().postDelayed(runShowChannelInfo, 5000);
        }
    }

    private void startPlayVideo(String path) {
        mVideoView.stopPlayback();
        LogUtlis.getInstance().showLogE(" playpath =" + path);
        if (path == null || path.isEmpty())
        {
            hideProgress();
            showErrorView();
            return;
        }
        else
        {
            showProgress();
            showChannelInfo();
            hideErrorView();
            hideSet();
            hideSorftkeyboard();
        }
        //统计播放次数
        if( null != mChannelMap.get(mCurrentKey) && null != mChannelMap.get(mCurrentKey).getChannelName() )
        {
            MobclickAgent.onEvent(this, "play_click", mChannelMap.get(mCurrentKey).getChannelName());
        }
        mVideoView.setVideoPath(path);//设置播放地址
        mVideoView.start();
        SpUtils.getInstance().savePosition(mCurrentKey);
        if (mChannelMap.get(mCurrentKey).isShop()) {
            ivShopImage.setVisibility(View.VISIBLE);
            switch (mChannelMap.get(mCurrentKey).getShopNum()) {
                case 1:
                    GlideUtils.loadImage(NetMessage.SHOP_IMAGE_URL_1,ivShopImage);
                    break;
                case 2:
                    GlideUtils.loadImage(NetMessage.SHOP_IMAGE_URL_2,ivShopImage);
                    break;
                case 3:
                    GlideUtils.loadImage(NetMessage.SHOP_IMAGE_URL_3,ivShopImage);
                    break;
                case 4:
                    GlideUtils.loadImage(NetMessage.SHOP_IMAGE_URL_4,ivShopImage);
                    break;
                default:
                    GlideUtils.loadImage(NetMessage.SHOP_IMAGE_URL_1,ivShopImage);
            }
        } else {
            ivShopImage.setVisibility(View.GONE);
        }
        // 设置加载超时,5S 未播放就停止播放 显示错误页面
        if (runLoadErr != null) {
            Global.getHandler().removeCallbacks(runLoadErr);
        }
        runLoadErr = new Runnable() {
            @Override
            public void run() {
                if (!mVideoView.isPlaying()) {
                    mVideoView.pause();
                    showErrorView();
                    hideProgress();
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        BackDialog backDialog = new BackDialog();
        backDialog.show(getFragmentManager(), "");
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                switch (v.getId()) {
                    case R.id.tv_change_decode: //硬解 和软解
                        hardware = !hardware;
                        SpUtils.getInstance().saveHardware(hardware);
                        mVideoView.setHardwareDecoder(hardware);
                        if (channelSourcesList.size() == 0) return true;
                        startPlayVideo(channelSourcesList.get(0));
                        if (hardware) {
                            Global.showToast("以切换成硬解码");
                        } else {
                            Global.showToast("以切换成软解码");
                        }
                        return true;
                    case R.id.tv_change_up_down: //按上下 换台 改变
                        isUpTpNext = !isUpTpNext;
                        SpUtils.getInstance().saveUpToNext(isUpTpNext);
                        if (isUpTpNext) {
                            Global.showToast("修改为按上键,切换上一个频道");
                        } else {
                            Global.showToast("修改为按上键,切换下一个频道");
                        }
                        return true;
                }
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent
                    .KEYCODE_DPAD_DOWN) {
                switch (v.getId()) {
                    case R.id.tv_change_decode:
                        mChangeControlChannel.requestFocus();
                        return true;
                    case R.id.tv_change_up_down:
                        mChangeDecode.requestFocus();
                        return true;
                }
            }
        }
        return false;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP://按上  上一个的电视台
                changeChnneal(isUpTpNext);
                LogUtlis.getInstance().showLogE("xxx isUpTpNext = " + isUpTpNext);
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:// 按下 下一个电视台
                changeChnneal(!isUpTpNext);
                LogUtlis.getInstance().showLogE("xxx isUpTpNext = " + isUpTpNext);
                return true;
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                //显示电视台菜单
                showMenu();
                return true;
            case KeyEvent.KEYCODE_BACK: //返回键
                if (hasShowSetting) {
                    hideSet();
                    return true;
                } else if (isKeyboardShow()) {
                    hideChannelNum();
                    return true;
                }
                return super.onKeyDown(keyCode, event);
            case KeyEvent.KEYCODE_MENU: //菜单键,换源
                showOtherResource();
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT: //左设置
                if (hasShowSetting) {
                    hideSet();
                    return true;
                }
                showSet();
                return true;
            case KeyEvent.KEYCODE_NUMPAD_0:
            case KeyEvent.KEYCODE_0:
                saveNum(0);
                return true;
            case KeyEvent.KEYCODE_NUMPAD_1:
            case KeyEvent.KEYCODE_1:
                saveNum(1);
                return true;
            case KeyEvent.KEYCODE_NUMPAD_2:
            case KeyEvent.KEYCODE_2:
                saveNum(2);
                return true;
            case KeyEvent.KEYCODE_NUMPAD_3:
            case KeyEvent.KEYCODE_3:
                saveNum(3);
                return true;
            case KeyEvent.KEYCODE_NUMPAD_4:
            case KeyEvent.KEYCODE_4:
                saveNum(4);
                return true;
            case KeyEvent.KEYCODE_NUMPAD_5:
            case KeyEvent.KEYCODE_5:
                saveNum(5);
                return true;
            case KeyEvent.KEYCODE_NUMPAD_6:
            case KeyEvent.KEYCODE_6:
                saveNum(6);
                return true;
            case KeyEvent.KEYCODE_NUMPAD_7:
            case KeyEvent.KEYCODE_7:
                saveNum(7);
                return true;
            case KeyEvent.KEYCODE_NUMPAD_8:
            case KeyEvent.KEYCODE_8:
                saveNum(8);
                return true;
            case KeyEvent.KEYCODE_NUMPAD_9:
            case KeyEvent.KEYCODE_9:
                saveNum(9);
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT: //软键盘
                if (isKeyboardShow()||hasShowSetting) return true;
                showSorftkeyboard();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 显示软键盘
     */
    private void showSorftkeyboard() {
        if (!isKeyboardShow()) {
            hasShowKeyboard = true;
            Global.showToast("请输入键盘数字");
            if (imm == null) {
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            }
            mEditText.setVisibility(View.VISIBLE);
            mEditText.requestFocus();
            imm.showSoftInput(mTvNumChangeChannle, 0);
        }
    }

    private boolean isKeyboardShow() {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * Global.mDensity;
    }
    /***
     * 处理数字
     * @param i
     */
    private void saveNum(int i) {
        mEditText.setText("");
        channelNum = channelNum + i + "";
        mTvNumChangeChannle.setVisibility(View.VISIBLE);
        mTvNumChangeChannle.setText(channelNum);
        if (run != null) {
            Global.getHandler().removeCallbacks(run);
        }
        run = new Runnable() {
            @Override
            public void run() {
                changeChannelByNum();
            }
        };
        Global.getHandler().postDelayed(run, 2500);
    }

    /**
     * 数字换台处理
     */
    private void changeChannelByNum() {
        try {
            int i = Integer.parseInt(channelNum);
            if (i == 0 || mChannelMap == null || i > mChannelMap.size() - 1) {
                Global.showToast("台号错误,没有找到这个台,请确定后重试");
            } else {
                LogUtlis.getInstance().showLogE("channelNum = " + channelNum);
                channelSourcesList = mChannelMap.get(i).getResourceList();
                path = channelSourcesList.get(0);
                if (path != null && !path.equals("")) {
                    mCurrentKey = i;
                    startPlayVideo(path);
                    if (run != null) {
                        Global.getHandler().removeCallbacks(run);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        hideChannelNum();
    }
    /***
     * 上下按键换台
     * @param up
     */
    private void changeChnneal(boolean up) {
        mReSourceNum = 0;
        checkMap();
        getCurrentKey(up);
        if (mChannelMap.get(mCurrentKey) == null) return;
        playNewChannel();
    }

    /**
     * 获取当前的key值
     */
    private void getCurrentKey(boolean up) {
        if (mChannelMap == null) return;
//            mChannelMap = ChannelResourceUtils.getInstace().getChannerMap();
        if (up) {
            if (mCurrentKey == 1) {
                mCurrentKey = mChannelMap.size();
            } else {
                mCurrentKey--;
            }
        } else {
            if (mCurrentKey == mChannelMap.size()) {
                mCurrentKey = 1;
            } else {
                mCurrentKey++;
            }
        }
    }

    /**
     * 换源
     *
     * @param position
     */
    private void changeResource(int position) {
        mReSourceNum = position;
        if (position < channelSourcesList.size()) {
            String path = channelSourcesList.get(position);
            if (path != null && !path.isEmpty()) {
                startPlayVideo(path);
            }
        } else {
            hideProgress();
            //显示 播放错误页面
            showErrorView();
        }

    }

    @Override
    public void onchangeSouce(int souceNum) {
        changeResource(souceNum);
    }

    /***
     *
     * 节目单换台回调
     * @param v
     * @param channelNum
     */
    @Override
    public void onchangeChannel(View v, int channelNum) {
        mCurrentKey = channelNum;
        playNewChannel();
//        playRequset();
    }
}
