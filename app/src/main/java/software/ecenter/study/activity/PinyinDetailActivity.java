package software.ecenter.study.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.Interface.OnItemListener;
import software.ecenter.study.OSSSImple.OSSConfig;
import software.ecenter.study.OSSSImple.PutObjectSamples;
import software.ecenter.study.R;
import software.ecenter.study.View.LoadingDialog;
import software.ecenter.study.View.RecordVideoView;
import software.ecenter.study.bean.Bean;
import software.ecenter.study.bean.OssTokenBean;
import software.ecenter.study.bean.PinDetailBean;
import software.ecenter.study.bean.PinOverBody;
import software.ecenter.study.bean.PinTiBean;
import software.ecenter.study.bean.PinUpBean;
import software.ecenter.study.bean.WxBean.AuthLoginBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.tool.mp3recorder.AudioPlayerManager;
import software.ecenter.study.tool.mp3recorder.PlayerCallback;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.PlayHelperUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message  拼音拼读详情
 * Create by Administrator
 * Create by 2019/11/8
 */
public class PinyinDetailActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_play_content)
    ImageView iv_play_content;
    @BindView(R.id.tv_next)
    TextView tv_next;
    @BindView(R.id.rvv_video)
    RecordVideoView rvv_video;
    @BindView(R.id.ll_num)
    LinearLayout ll_num;
    @BindView(R.id.tv_num)
    TextView tv_num;
    @BindView(R.id.tv_tiNum)
    TextView tv_tiNum;
    @BindView(R.id.tv_ti)
    TextView tv_ti;
    private List<PinTiBean> tiList;
    private List<PinTiBean> myList = new ArrayList<>();
    private int postion;
    private String audioTiUrl;
//    private String id = "22175";
    private String id = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinyindetail);
        ButterKnife.bind(this);
        mWaitDialog = new LoadingDialog(mContext);
        mWaitDialog.setCancelable(false);
        id = getIntent().getStringExtra("id");
        rvv_video.setActivity(this, "studyRecore", new OnItemListener<String>() {
            @Override
            public void onConfig(String s) {
                //录音完成
                ll_num.setVisibility(View.VISIBLE);
                int num = new Random().nextInt(100);
                tv_num.setText(String.valueOf(num));
                saveMyTiData(s, "",rvv_video.getCurrentrecordTime() / 1000, num + "");
            }

            @Override
            public void onCancle() {
                //录音取消
                saveMyTiData("","", 0, "");
                ll_num.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPay() {
                //播放原音  把题目的音频关闭
                if (PlayHelperUtil.newInrence().playStatus()) {
                    PlayHelperUtil.newInrence().stopPlay();
                    PlayHelperUtil.newInrence().release();
                    iv_play_content.setSelected(PlayHelperUtil.newInrence().playStatus());
                }
            }
        });
        initListener();
        getData();
        updateUserHeade();
    }

    /**
     * 保存我做的题目
     *
     * @param s        视频路径
     * @param duration 视频时长
     * @param score    得分
     */
    private void saveMyTiData(String s,String ossPath, int duration, String score) {
        if (ToolUtil.isList(myList) && postion < myList.size()) {
            PinTiBean pinTiBean = myList.get(postion);
            pinTiBean.setLocalUrl(s);
            pinTiBean.setAudioUrl(ossPath);
            pinTiBean.setDuration(duration);
            pinTiBean.setScore(score);
            myList.set(postion, pinTiBean);
        }
    }

    //获取题目
    private void getData() {
        if (TextUtils.isEmpty(id)||!showNetWaitLoding()) {
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getPinduExercise(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        PinDetailBean bean = ParseUtil.parseBean(response, PinDetailBean.class);
                        if (bean != null) {
                            tiList = bean.getData();
                            showTiLay();
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    //展示题目布局
    @SuppressLint("SetTextI18n")
    private void showTiLay() {
        if (ToolUtil.isList(tiList)) {
            PinTiBean pinTiBean = tiList.get(postion);
            PinTiBean myBean = new PinTiBean();
            if (pinTiBean != null) {
                audioTiUrl = pinTiBean.getAudioUrl();
                tv_ti.setText(ToolUtil.getString(pinTiBean.getTitle(), pinTiBean.getId()));
                myBean.setId(pinTiBean.getId());
                myBean.setTitle(pinTiBean.getTitle());
            } else {
                audioTiUrl = "";
                tv_ti.setText("");
            }
            myBean.setAudioUrl("");
            myBean.setDuration(0);
            myBean.setScore("");
            myList.add(myBean);

            tv_tiNum.setText((postion + 1) + "/" + tiList.size());
            ll_num.setVisibility(View.INVISIBLE);
            rvv_video.showLunState(RecordVideoView.RecordState.OVER);
            if (postion == tiList.size() - 1) {
                tv_next.setText("提交");
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_next, R.id.iv_play_content})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_play_content://播放题目语音
                if (TextUtils.isEmpty(audioTiUrl)) {
                    ToastUtils.showToastSHORT(mContext, "播放路径错误");
                    return;
                }
                if (rvv_video.getCurretnRecordState() == RecordVideoView.RecordState.RECORDING) {
                    ToastUtils.showToastSHORT(mContext, "当前正在录音");
                    return;
                }
                if (rvv_video.isPlaying()) {
                    rvv_video.stopPlay();
                }
                if (PlayHelperUtil.newInrence().playStatus()) {
                    PlayHelperUtil.newInrence().stopPlay();
                } else {
                    PlayHelperUtil.newInrence().startPlay(audioTiUrl);
                }
                iv_play_content.setSelected(PlayHelperUtil.newInrence().playStatus());
                break;
            case R.id.tv_next://下一个
                if (PlayHelperUtil.newInrence().playStatus()) {
                    PlayHelperUtil.newInrence().stopPlay();
                    iv_play_content.setSelected(PlayHelperUtil.newInrence().playStatus());
                }
                //是否完成答题
                if (ToolUtil.isList(myList) && postion < myList.size()) {
                    PinTiBean pinTiBean = myList.get(postion);
                    if (pinTiBean != null) {
                        //当前题是否已答
                        if (!TextUtils.isEmpty(pinTiBean.getLocalUrl())) {
                            int startIndex = pinTiBean.getLocalUrl().lastIndexOf("/");
                            String pathName = pinTiBean.getLocalUrl().substring(startIndex + 1, pinTiBean.getLocalUrl().length());
                            uploadData(pathName, pinTiBean.getLocalUrl());
                        } else {
                            ToastUtils.showToastSHORT(mContext, "请先答题");
                        }
                    }
                } else {
                    ToastUtils.showToastSHORT(mContext, "请先答题");
                }
                break;
        }
    }

    /**
     * oss上传
     *
     * @param path      上传路径
     * @param localpath 文件本地路径
     */
    public void uploadData(final String path, final String localpath) {
        showWaitLoding();
        final String p = OSSConfig.ossnoteFolder + path;
        new PutObjectSamples(OSSConfig.getInstance(getApplicationContext())
                , OSSConfig.testBucket, OSSConfig.ossnoteFolder + path, localpath)
                .asyncPutObjectFromLocalFile(new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissWaitLoging();
                                Log.d("111111uploadData", "onSuccess语音文件：" + OSSConfig.OSSPATH + p);
                                saveMyTiData(localpath, p, myList.get(postion).getDuration(), myList.get(postion).getScore());
                                if (ToolUtil.isList(tiList) && postion == tiList.size() - 1) {
                                    save();
                                    Log.d("vewqegywqge", myList.toString());
                                } else {
                                    postion++;
                                    showTiLay();
                                }

                            }
                        });
                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("111111uploadData", "onSuccess：" + "文件上传失败");
                                ToastUtils.showToastLONG(mContext, "文件上传失败，请稍后重试！");
                                dismissWaitLoging();
                            }
                        });
                    }
                });

    }

    //保存拼读
    private void save() {
        if (!showNetWaitLoding()) {
            return;
        }
        PinOverBody pinOverBody = new PinOverBody();
        pinOverBody.setResourceId(id);
        pinOverBody.setExerciseResults(myList);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(pinOverBody));

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getPinduSave(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        PinUpBean bean = ParseUtil.parseBean(response, PinUpBean.class);
                        if (bean != null &&bean.getData()!=null) {
                            int pinduRecordId = bean.getData().getPinduRecordId();
                            startActivity(new Intent(mContext, PinyinSpellListActivity.class).putExtra("id", String.valueOf(pinduRecordId)));
                            ToastUtils.showToastSHORT(mContext, bean.getMessage());
                            finish();
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        PlayHelperUtil.newInrence().onPause();
        rvv_video.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PlayHelperUtil.newInrence().onResume();
        rvv_video.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PlayHelperUtil.newInrence().onDestroy();
        rvv_video.onDestroy();
    }

    //录音监听
    private void initListener() {
        PlayHelperUtil.newInrence().listener(new PlayerCallback() {
            @Override
            public void onPreparing(Object dataSource, AudioPlayerManager manager) {

            }

            @Override
            public void onPlaying(Object dataSource, AudioPlayerManager manager) {

            }

            @Override
            public void onPause(Object dataSource, AudioPlayerManager manager) {

            }

            @Override
            public void onFinished(Object dataSource, AudioPlayerManager manager) {
                iv_play_content.setSelected(false);
            }

            @Override
            public void onStop(Object dataSource, AudioPlayerManager manager) {

            }

            @Override
            public void onError(Object dataSource, AudioPlayerManager manager) {
                iv_play_content.setSelected(false);
            }

            @Override
            public void onProgress(int progress, Object dataSource, AudioPlayerManager manager) {

            }

            @Override
            public void onSeeking(Object dataSource, AudioPlayerManager manager) {

            }

            @Override
            public void onBufferingUpdate(int percent, AudioPlayerManager manager) {

            }

            @Override
            public void onGetMaxDuration(int maxDuration) {

            }
        });
    }

    public LoadingDialog mWaitDialog;

    public void showWaitLoding() {
        if (!mWaitDialog.isShowing()) {
            mWaitDialog.show();
        }
    }

    public void dismissWaitLoging() {
        if (mWaitDialog.isShowing()) {
            if (!this.isFinishing())
                mWaitDialog.dismiss();
        }
    }

    /**
     * 获取osstoken鉴权、上传文件到oss、回传上传路径给服务端
     */
    public void updateUserHeade() {
        Map<String, String> header = new HashMap<>();
        header.put("token", AccountUtil.getToken(mContext) + "");
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getOssTokenByPindu(header))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        Log.e("osstoken", "osstoken : get oss token is successful");
                        OssTokenBean ossTokenBean1 = ParseUtil.parseBean(response, OssTokenBean.class);
                        if (ossTokenBean1 != null && ossTokenBean1.getData() != null) {
                            OssTokenBean ossTokenBean = OssTokenBean.getInstance();
                            ossTokenBean.getData().setAccessKeyId(ossTokenBean1.getData().getAccessKeyId());
                            ossTokenBean.getData().setAccessKeySecret(ossTokenBean1.getData().getAccessKeySecret());
                            ossTokenBean.getData().setBucket(ossTokenBean1.getData().getBucket());
                            ossTokenBean.getData().setEndPoint(ossTokenBean1.getData().getEndPoint());
                            ossTokenBean.getData().setExpiration(ossTokenBean1.getData().getExpiration());
                            ossTokenBean.getData().setSecurityToken(ossTokenBean1.getData().getSecurityToken());
                            ossTokenBean.getData().setUploadUrl(ossTokenBean1.getData().getUploadUrl());
                            ossTokenBean.getData().setUploadFilePath(ossTokenBean1.getData().getUploadFilePath());
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        Log.e("osstoken", msg);
                    }
                });
    }
}
