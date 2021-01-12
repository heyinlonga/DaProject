package software.ecenter.study.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.Adapter.TipTeacherResourceAdapter;
import software.ecenter.study.Interface.ConstantData;
import software.ecenter.study.R;
import software.ecenter.study.View.LandLayoutVideo;
import software.ecenter.study.bean.HomeBean.SeeTeacherResBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.tool.FileAccessor;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.NetworkUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.PermissionUtils;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * dec 资源查看
 * 单独的视频播放页面
 */
public class SeeTeacherResourcesVideoActivity extends BaseActivity implements ConstantData {


    @BindView(R.id.btn_fenxiang_img)
    ImageView btnFenxiangImg;
    @BindView(R.id.btn_shouchang_img)
    ImageView btnShouchangImg;
    @BindView(R.id.zan_img)
    ImageView zanImg;
    @BindView(R.id.zan_text)
    TextView zanText;
    @BindView(R.id.btn_zan)
    LinearLayout btnZan;
    @BindView(R.id.list_kecheng)
    RecyclerView listKecheng;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.post_detail_nested_scroll)
    NestedScrollView postDetailNestedScroll;
    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.detail_player)
    LandLayoutVideo detailPlayer;
    @BindView(R.id.detail_img)
    ImageView detailImg;
    @BindView(R.id.detail_text)
    TextView detailText;
    @BindView(R.id.activity_detail_player)
    RelativeLayout activityDetailPlayer;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.list_kecheng_no_resource)
    TextView tvkeChengNoResource;


    private boolean isPlay;
    private boolean isPause;

    private OrientationUtils orientationUtils;

    private String resourceId;
    private SeeTeacherResBean mResourceDetailBean;
    private List<SeeTeacherResBean.DataBean.ResourceListBean> ResourceListData;
    private boolean isCollection;//是否收藏了
    private boolean isVideo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_teacher_resources_video);
        ButterKnife.bind(this);
        resourceId = getIntent().getStringExtra("resourceId");
        initView();
    }


    @Override
    protected void onResume() {
        if (isVideo) {
            getCurPlay().onVideoResume(true);
        }
        getDataById();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (isVideo) {
            getCurPlay().onVideoPause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AccountUtil.saveSpeednum(mContext, 1);
        if (isPlay) {
            getCurPlay().release();
        }
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @OnClick({R.id.btn_left_title, R.id.tv_speed,
            R.id.btn_zan, R.id.btn_shouchang_img,
            R.id.detail_img, R.id.detail_text, R.id.btn_fenxiang_img
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.detail_img:
                clickBuydetailImg();
                break;
            case R.id.btn_zan:
                if (!NetworkUtil.isConnected(mContext)) return;
                submitResourceThumbUp(1);
                break;
            case R.id.btn_shouchang_img:
                if (!NetworkUtil.isConnected(mContext)) return;
                if (!isCollection) {
                    submitResourceCollection(1);
                } else {
                    submitResourceCollection(0);
                }
                break;
            case R.id.btn_fenxiang_img:
                if (!NetworkUtil.isConnected(mContext)) return;
                //TODO 分享不知道分享什么，没有对应资源的H5页面，他们没做
                break;
            case R.id.tv_speed:
                clickBuySpeed();
                break;
            case R.id.detail_text:
                clickBuydetailText();
                break;
        }
    }


    private void clickBuydetailText() {
        PermissionUtils.newIntence().requestPerssion(this, ToolUtil.PERSSION_WRITE, 101, new PermissionUtils.IPermission() {
            @Override
            public void success(int code) {
                //不需要购买，直接展示
                Intent intentTwo = new Intent(SeeTeacherResourcesVideoActivity.this, FileDisplayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("path", mResourceDetailBean.getData().getUrl());
                intentTwo.putExtras(bundle);
                startActivity(intentTwo);
                saveUserCheckResource();
            }
        });
    }


    /**
     * 设置视频播放速度
     */
    private void clickBuySpeed() {
        if (tvSpeed.getText().toString().equals("1X")) {
            if (detailPlayer.isInPlayingState())
                detailPlayer.setSpeedPlaying(1.5f, false);
            else
                detailPlayer.setSpeed(1.5f, false);
            tvSpeed.setText("1.5X");
            AccountUtil.saveSpeednum(mContext, 1.5f);
        } else if (tvSpeed.getText().toString().equals("1.5X")) {
            if (detailPlayer.isInPlayingState())
                detailPlayer.setSpeedPlaying(2f, false);
            else
                detailPlayer.setSpeed(2f, false);
            tvSpeed.setText("2X");
            AccountUtil.saveSpeednum(mContext, 2);
            return;
        } else if (tvSpeed.getText().toString().equals("2X")) {
            if (detailPlayer.isInPlayingState())
                detailPlayer.setSpeedPlaying(1f, false);
            else
                detailPlayer.setSpeed(1f, false);
            tvSpeed.setText("1X");
            AccountUtil.saveSpeednum(mContext, 1);
            return;
        }

    }


    private void clickBuydetailImg() {
        //不需要购买，直接展示
        Intent intent = new Intent(mContext, BigPicActivity.class);
        intent.putExtra("ImageUrl", mResourceDetailBean.getData().getUrl());
        startActivity(intent);
        saveUserCheckResource();
    }


    public void getData(boolean isShow) {
        if (isShow) {
            if (!showNetWaitLoding()) {
                return;
            }
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("id", resourceId);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getTeacherResourceDetail(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        SeeTeacherResBean bean = ParseUtil.parseBean(response, SeeTeacherResBean.class);
                        if (bean != null) {
                            setResponseView(bean);

                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });

    }


    /**
     * 设置资源详情数据
     *
     * @param bean 资源实体
     */
    public void setResponseView(SeeTeacherResBean bean) {
        Log.d("12312dwqd1", "设置资源详情数据");
        mResourceDetailBean = bean;
        if (mResourceDetailBean == null) return;
        zanText.setText(mResourceDetailBean.getData().getUps());//点赞数
        if (mResourceDetailBean.getData().isUp()) {
            zanImg.setImageResource(R.drawable.zan2);
            //点完就不让点了
            btnZan.setEnabled(false);
        }
        if (mResourceDetailBean.getData().isCollect()) {
            btnShouchangImg.setImageResource(R.drawable.shoucang2);
            isCollection = true;
        }


        ResourceListData = bean.getData().getResourceList();
        TipTeacherResourceAdapter adapterTwo = new TipTeacherResourceAdapter(ResourceListData);
        adapterTwo.setItemClickListener(new TipTeacherResourceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, SeeTeacherResourcesVideoActivity.class);
                intent.putExtra("resourceId", ResourceListData.get(position).getResourceId());
                startActivity(intent);

            }
        });
        listKecheng.setAdapter(adapterTwo);
        if (ResourceListData != null && ResourceListData.size() <= 0) {
            tvkeChengNoResource.setVisibility(View.VISIBLE);
        } else {
            tvkeChengNoResource.setVisibility(View.GONE);
        }
        netSoure();
        title.setText(mResourceDetailBean.getData().getName());
        if (!TextUtils.isEmpty(mResourceDetailBean.getData().getType()) && mResourceDetailBean.getData().getType().equals("richtext")
                || mResourceDetailBean.getData().getType().equals("richtext")) {
            detailText.setVisibility(View.VISIBLE);
//            detailText.setText(mResourceDetailBean.getData().getName());
            detailText.setBackground(getResources().getDrawable(R.drawable.doc_bg));
        }
        detailPlayer.setEnabled(true);
        detailImg.setEnabled(true);
        detailText.setEnabled(true);
    }


    public void initVideo(String url) {
        if (isPlay) {
            return;
        }
        resolveNormalVideoUI();

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, detailPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption//.setThumbImageView(imageView)
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setThumbPlay(true)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setSeekRatio(1)
                .setUrl(url)
                .setCacheWithPlay(false)
                .setVideoTitle(mResourceDetailBean.getData().getName())
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        Debuger.printfError("***** onPrepared **** " + objects[0]);
                        Debuger.printfError("***** onPrepared **** " + objects[1]);
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        if (!TextUtils.isEmpty(mResourceDetailBean.getData().getType()) && !mResourceDetailBean.getData().getType().equals("audio"))
                            orientationUtils.setEnable(true);

                        isPlay = true;
                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                        Debuger.printfError("***** onEnterFullscreen **** " + objects[0]);//title
                        Debuger.printfError("***** onEnterFullscreen **** " + objects[1]);//当前全屏player
                    }

                    @Override
                    public void onAutoComplete(String url, Object... objects) {
                        super.onAutoComplete(url, objects);
                        saveUserCheckResource();

                        tvSpeed.setText("2X");
                        clickBuySpeed();
                        Object o = objects[1];
                        if (o instanceof LandLayoutVideo) {
                            ((LandLayoutVideo) o).revice();
                        }
                    }

                    @Override
                    public void onClickStartError(String url, Object... objects) {
                        super.onClickStartError(url, objects);
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        Debuger.printfError("***** onQuitFullscreen **** " + objects[0]);//title
                        Debuger.printfError("***** onQuitFullscreen **** " + objects[1]);//当前非全屏player
                        float speed = detailPlayer.getSpeed();
                        if (tvSpeed != null) {
                            if (speed == 2) {
                                tvSpeed.setText("2X");
                            } else if (speed == 1.5) {
                                tvSpeed.setText("1.5X");
                            } else {
                                tvSpeed.setText("1X");
                            }
                        }

                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }
                })
                .setLockClickListener(new LockClickListener() {
                    @Override
                    public void onClick(View view, boolean lock) {
                        if (orientationUtils != null) {
                            //配合下方的onConfigurationChanged
                            if (!TextUtils.isEmpty(mResourceDetailBean.getData().getType()) && !mResourceDetailBean.getData().getType().equals("audio"))
                                orientationUtils.setEnable(!lock);
                        }
                    }
                })
                .setGSYVideoProgressListener(new GSYVideoProgressListener() {
                    @Override
                    public void onProgress(int progress, int secProgress, int currentPosition, int duration) {
                        View thumbImageView = detailPlayer.getThumbImageView();
                        if (thumbImageView != null) {
                            detailPlayer.getThumbImageViewLayout().setVisibility(View.VISIBLE);
                        }
                        Debuger.printfLog(" progress " + progress + " secProgress " + secProgress + " currentPosition " + currentPosition + " duration " + duration);
                    }
                })
                .build(detailPlayer);
        if (!TextUtils.isEmpty(mResourceDetailBean.getData().getType()) && mResourceDetailBean.getData().getType().equals("mp3")) {
            detailPlayer.getFullscreenButton().setVisibility(View.GONE);
        }
        detailPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer.startWindowFullscreen(SeeTeacherResourcesVideoActivity.this, true, true);
            }
        });
        detailPlayer.getStartButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailPlayer.startPlay();
            }
        });
        detailPlayer.getThumbImageViewLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailPlayer.startPlay();
            }
        });
    }

    /**
     * 保存用户观看完成记录
     */
    private void saveUserCheckResource() {

        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("resourceId", resourceId);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).saveUserCheckResource(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {

                    @Override
                    public void onSuccess(String response) {
                        Log.e(TAG, "save user playing is successful");
                        dismissNetWaitLoging();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        Log.e(TAG, "save user playing is fail");
                        dismissNetWaitLoging();

                    }
                });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay) {
            detailPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }

    private void resolveNormalVideoUI() {
        //增加title
        detailPlayer.getTitleTextView().setVisibility(View.GONE);
        detailPlayer.getBackButton().setVisibility(View.GONE);
    }

    private GSYVideoPlayer getCurPlay() {
        if (detailPlayer.getFullWindowPlayer() != null) {
            return detailPlayer.getFullWindowPlayer();
        }
        return detailPlayer;
    }


    //点赞
    public void submitResourceThumbUp(int type) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
            json.put("id", resourceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).teacherResourceUp(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        zanImg.setImageResource(R.drawable.zan2);
                        zanText.setText("" + (Integer.parseInt(mResourceDetailBean.getData().getUps()) + 1));
                        //点完就不让点了
                        btnZan.setEnabled(false);
                        ToastUtils.showToastSHORT(mContext, "点赞成功！");
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    //收藏
    public void submitResourceCollection(final int type) {
        if (!showNetWaitLoding()) {
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
            json.put("id", resourceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).teacherResourcecollect(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        if (type == 1) {
                            //收藏了
                            btnShouchangImg.setImageResource(R.drawable.shoucang2);
                            isCollection = true;
                            ToastUtils.showToastSHORT(mContext, "收藏成功！");
                        } else {
                            //收藏了
                            btnShouchangImg.setImageResource(R.drawable.shoucang1);
                            isCollection = false;
                            ToastUtils.showToastSHORT(mContext, "取消收藏成功！");
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    private void initView() {
        listKecheng.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

    }


    /**
     * 获取资源通过本地或者网络
     */
    private void getDataById() {
        getData(false);
    }


    /**
     * 网络获取资源
     */
    public void netSoure() {
        //判断资源格式
        if (mResourceDetailBean.getData().getType().toLowerCase().contains("mp3")
                || mResourceDetailBean.getData().getType().toLowerCase().contains("mp4")) {

            //如果是音频，需要在视频播放完或手动拖到视频结束时，才可以点击
            if (mResourceDetailBean.getData().getType().toLowerCase().contains("mp4")) {
                isVideo = true;
            }
            detailPlayer.setVisibility(View.VISIBLE);
            if (mResourceDetailBean.getData().getType().contains("MP3")
                    || mResourceDetailBean.getData().getType().contains("mp3")) {
                //增加封面
                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageResource(R.drawable.audio_bg);
                detailPlayer.setThumbImageView(imageView);
            }
            initVideo(mResourceDetailBean.getData().getUrl());
        } else if (mResourceDetailBean.getData().getType().toLowerCase().contains("img")) {
            //文件格式
            detailImg.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(mResourceDetailBean.getData().getUrl()).into(detailImg);
        } else if (
                mResourceDetailBean.getData().getType().toLowerCase().contains("word")||
                mResourceDetailBean.getData().getType().toLowerCase().contains("pdf")||
                mResourceDetailBean.getData().getType().toLowerCase().contains("ppt")
                ) {
            detailText.setVisibility(View.VISIBLE);
            detailText.setBackground(getResources().getDrawable(R.drawable.doc_bg));
        } else {
            ToastUtils.showToastSHORT(mContext, "此资源不支持在线观看，请前往PC端查看或下载");
        }
    }

}

