package software.ecenter.study.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import software.ecenter.study.Adapter.ImageAdapter;
import software.ecenter.study.OSSSImple.OSSConfig;
import software.ecenter.study.OSSSImple.PutObjectSamples;
import software.ecenter.study.R;
import software.ecenter.study.bean.ImageBean;
import software.ecenter.study.bean.OssTokenBean;
import software.ecenter.study.bean.QuestionBean.QuestionPayDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.PermissionUtils;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * dec 我要追问
 */
public class QuestionCloselyActivity extends BasePicActivity implements View.OnTouchListener {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.edit_miaoshu)
    EditText editMiaoshu;
    //    @BindView(R.id.btn_lv_yin)
//    RelativeLayout btnLvYin;
    @BindView(R.id.list_image)
    RecyclerView listImage;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.btn_submission)
    Button btnSubmission;
    @BindView(R.id.lv_text)
    TextView lvText;    //录制或播放时间
    @BindView(R.id.tip_ly)
    ImageButton tipLy;  //有录音文件的按钮，用于控制播放和停止播放
    @BindView(R.id.rl_lv_yin)
    RelativeLayout rlLuYin;  //录音播放暂停、显示时间、删除按钮对应布局
    @BindView(R.id.iv_play_or_pause)
    ImageView ivPlayOrPause;//录制和暂停录制按钮
    @BindView(R.id.iv_delete_lu_yin)
    ImageView ivDeleteLuYin; //删除录音按钮
    @BindView(R.id.dayi_tv_ly_submit)
    TextView tvDaYiLuYinSubmit; // 完成录音按钮
    @BindView(R.id.iv_auditions)
    ImageView ivAuditions;

    private String questionId;
    private String audioPath;
    private List<String> imagelistDatasForOss;
    public static List<ImageBean> imageBeanList;

    private boolean isreset = false; //设置点击回复初始状态


    Handler handler = new Handler() {
        public void handleMessage(Message message) {

            //首先判断是否有语音路径
            // 没语音、有图
            if (TextUtils.isEmpty(tempRecorderPath)) {
                if (imagelistDatasForOss != null && imagelistDatasForOss.size() == imageBeanList.size()) {
                    if (imageBeanList.size() == imagelistDatasForOss.size())
                        updateData();
                    return;
                }
                //如果有语音路径并且，有图片
            } else if (imagelistDatasForOss != null && imagelistDatasForOss.size() == imageBeanList.size()) {
                if (!TextUtils.isEmpty(audioPath)) {
                    if (imageBeanList.size() == imagelistDatasForOss.size())
                        updateData();
                    return;
                }
                //有语音没图片
            } else if (imagelistDatasForOss == null || imagelistDatasForOss.size() <= 0) {
                if (!TextUtils.isEmpty(audioPath)) {
                    if (imageBeanList.size() == imagelistDatasForOss.size())
                        updateData();
                    return;
                }
            }
            //没语音有图
            if (TextUtils.isEmpty(audioPath)) {
                if (imagelistDatasForOss != null && imagelistDatasForOss.size() == imageBeanList.size()) {
                    //updateData();
                    return;
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_closely);
        ButterKnife.bind(this);
        imagelistDatasForOss = new ArrayList<>();
        //图片
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        listImage.setLayoutManager(gridLayoutManager);
        imageAdapter = new ImageAdapter(mContext, listImageData);
        imageAdapter.setItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (listImageData == null || listImageData.size() < 1) {
                    spinnerPopWindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    return;
                }
                if (listImageData.get(position).isAddImage()) {
                    PermissionUtils.newIntence().requestPerssion(QuestionCloselyActivity.this, ToolUtil.PERSSION_PHOTO, 101, new PermissionUtils.IPermission() {
                        @Override
                        public void success(int code) {
                            spinnerPopWindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        }
                    });

                }
            }
        });
        imageAdapter.setmItemBtnAddClickListener(new ImageAdapter.OnItemBtnAddClickListener() {
            @Override
            public void onItemBtnAddClick(int position) {
                if (listImageData.size() > 0 && !listImageData.get(listImageData.size() - 1).isAddImage()) {//如果最后一个不是添加图，就加上
                    ImageBean item = new ImageBean();
                    item.setAddImage(true);
                    listImageData.add(item);
                }

                listImageData.remove(position);
                imageAdapter.refreshData(listImageData);

            }
        });
        listImage.setAdapter(imageAdapter);
        // btnLvYin.setOnTouchListener(this);

        questionId = getIntent().getStringExtra("questionId");
    }

    @OnClick({R.id.btn_left_title, R.id.btn_submission, R.id.iv_play_or_pause, R.id.dayi_tv_ly_submit, R.id.tip_ly,
            R.id.iv_delete_lu_yin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;

            case R.id.btn_submission:
                /*if(TextUtils.isEmpty(editMiaoshu.getText().toString())){
                    ToastUtils.showToastSHORT(mContext,"请输入内容");
                    return;
                }
                if( TextUtils.isEmpty(tempRecorderPath)){
                    ToastUtils.showToastSHORT(mContext,"请录音");
                    return;
                }

                if(listImageData!=null&&listImageData.size()>1){ //大于1 是因为还有一张默认图片
                    addUserQuestion();
                }else{
                    ToastUtils.showToastSHORT(mContext,"未选择图片");
                    return;
                }*/

                if (imagelistDatasForOss == null)
                    imagelistDatasForOss = new ArrayList<>();
                imagelistDatasForOss.clear();
                if (imageBeanList == null)
                    imageBeanList = new ArrayList<>();
                imageBeanList.clear();
                for (ImageBean imageBean : listImageData) {
                    if (!TextUtils.isEmpty(imageBean.getTargetPicPath())) {
                        imageBeanList.add(imageBean);
                    }
                }
              /*  ;
                startActivity(new Intent(mContext, AnswerBuyActivity.class));*/
                if (TextUtils.isEmpty(tempRecorderPath) && (imageBeanList == null || imageBeanList.size() < 1)) {
                    if (TextUtils.isEmpty(editMiaoshu.getText().toString().trim())) {
                        ToastUtils.showToastSHORT(mContext, "请输入文字、上传图片、上传语音三种类型中最少一种");
                        return;
                    }
                    updateData();
                    break;
                }
                if (TextUtils.isEmpty(tempRecorderPath) && imageBeanList.size() < 1) {
                    ToastUtils.showToastSHORT(mContext, "请录制语音或增加图片");
                    return;
                }
                if (isRecording) {
                    ToastUtils.showToastSHORT(mContext, "正在录音，请完成录音后提交");
                    return;
                }
                getOssToken();
                break;
            case R.id.btn_lv_yin:
                if (hasRecording && !isThouchModee) {
                    if (!isPlaying) {
                        startplayRecording();//播放
                    } else {
                        stopPlayRecording();//停止播放
                    }
                }
                break;
            case R.id.iv_play_or_pause:
                if (isreset) {
                    isreset = false;
                    ivPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.ks));
                    return;
                }
                audioPlayOrPause();
                break;
            case R.id.dayi_tv_ly_submit:
                if (isPause) { //判断是否暂停录音
                    resumeRecording();
                    ivPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.zt));
                }
                finishRecord();
                break;
            case R.id.tip_ly:
                if (hasRecording && !isRecording) {
                    if (!isPlaying) {
                        startplayRecording();//播放
                        tipLy.setBackground(getResources().getDrawable(R.drawable.hjk));
                    } else {
                        tipLy.setBackground(getResources().getDrawable(R.drawable.bf2));
                        stopPlayRecording();//停止播放
                    }
                }
                break;
            case R.id.iv_delete_lu_yin:
                ivPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.cf1));
                rlLuYin.setBackground(getResources().getDrawable(R.drawable.btn_ly_shape_circle_unclick));
                ivPlayOrPause.setEnabled(true);
                isreset = true;
                ivDeleteLuYin.setVisibility(View.GONE);
                tipLy.setBackground(getResources().getDrawable(R.drawable.bf1));
                lvText.setText("");
                if (isPlaying)
                    stopPlayRecording();
                clearAudio();
                break;
        }
    }


    @Override
    public void curRecordingSecond(int second) {
//        lvText.setText("正在录音(" + second + " s)");
        lvText.setText(second + "s");
        String str = "";
        ivAuditions.setVisibility(View.GONE);
    }

    @Override
    public void recordingFinished() {
        lvText.setText(recordingSecond + "s");
    }

    @Override
    public void curPlaySecond(int second) {
        lvText.setText(second + " s");
    }

    @Override
    public void recordingFinishedTooMin() {
    }

    @Override
    public void playFinished() {
        lvText.setText(recordingSecond + "s");
        tipLy.setBackground(getResources().getDrawable(R.drawable.bf2));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                if (!hasRecording) {
                    isThouchModee = true;
                    stopActionRecording(); //停止录音
                } else {
                    isThouchModee = false;
                }
            case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                if (!hasRecording) {
                    startRecording();//开始录音
                }
                break;
            default:
                break;
        }
        return false;
    }

    public void addUserQuestion() {
        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("questionId", questionId);
        map.put("questionText", editMiaoshu.getText().toString());
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (ImageBean item : listImageData) {
            if (!item.isAddImage()) {
                parts.add(RetroFactory.prepareFilePart("image", new File(item.getTargetPicPath())));
            }
        }
        MultipartBody.Part part = null;
        if (tempRecorderPath != null) {
            part = RetroFactory.prepareFilePart("audio", new File(tempRecorderPath));
        }
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).addUserQuestion(map, parts, part))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, "追问成功");
                        onBackPressed();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    public void getOssToken() {
        if (!showNetWaitLoding()) {
            return;
        }
        Map<String, String> header = new HashMap<>();
        header.put("token", AccountUtil.getToken(mContext) + "");
        //获取osstoken
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getOssTokenByDaYi(header))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        Log.e(TAG, "get oss token is successful");
                        OssTokenBean ossTokenBean1 = new OssTokenBean();
                        ossTokenBean1 = ParseUtil.parseBean(response, OssTokenBean.class);
                        OssTokenBean ossTokenBean = OssTokenBean.getInstance();
                        ossTokenBean.getData().setAccessKeyId(ossTokenBean1.getData().getAccessKeyId());
                        ossTokenBean.getData().setAccessKeySecret(ossTokenBean1.getData().getAccessKeySecret());
                        ossTokenBean.getData().setBucket(ossTokenBean1.getData().getBucket());
                        ossTokenBean.getData().setEndPoint(ossTokenBean1.getData().getEndPoint());
                        ossTokenBean.getData().setExpiration(ossTokenBean1.getData().getExpiration());
                        ossTokenBean.getData().setSecurityToken(ossTokenBean1.getData().getSecurityToken());
                        ossTokenBean.getData().setUploadUrl(ossTokenBean1.getData().getUploadUrl());
                        ossTokenBean.getData().setUploadFilePath(ossTokenBean1.getData().getUploadFilePath());
                        if ((imageBeanList != null && imageBeanList.size() > 0)) {
                            for (int i = 0; i < imageBeanList.size(); i++) {
                                if (!TextUtils.isEmpty(imageBeanList.get(i).getTargetPicPath()))
                                    putData2OSS(i + 1, imageBeanList.get(i));
                            }
                        }
                        if (!TextUtils.isEmpty(tempRecorderPath)) {
                            putData2OSS(0, null);
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(mContext, msg);
                        dismissWaitLoging();
                    }
                });

    }

    public void putData2OSS(final int size, final ImageBean imageBean) {
        String path = OssTokenBean.getInstance().getData().getUploadFilePath();
        String fileName = "";
        int fileIndex = 0;
        String localFileUrl = "";
        //判断是否是图片、否则判断是否是语音、否则return
        if (imageBean != null) {
            fileIndex = imageBean.getTargetPicPath().lastIndexOf("/");
            fileName = imageBean.getTargetPicPath().substring(fileIndex + 1, imageBean.getTargetPicPath().length());
            localFileUrl = imageBean.getTargetPicPath();
        } else if (!TextUtils.isEmpty(tempRecorderPath)) {
            fileIndex = tempRecorderPath.lastIndexOf("/");
            fileName = tempRecorderPath.substring(fileIndex + 1, tempRecorderPath.length());
            localFileUrl = tempRecorderPath;
        } else {
            return;
        }


        new PutObjectSamples(OSSConfig.getInstance(mContext),
                OssTokenBean.getInstance().getData().getBucket(),
                path + fileName, localFileUrl) {

        }.asyncPutObjectFromLocalFile(new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(final PutObjectRequest request, PutObjectResult result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        if (imageBean != null) {
                            imagelistDatasForOss.add(request.getObjectKey());
                            message.what = 1;
                            message.arg1 = size;
                        } else if (!TextUtils.isEmpty(tempRecorderPath)) {
                            audioPath = request.getObjectKey();
                            message.what = 2;
                            message.arg1 = size;
                        } else {
                            message.what = -2;
                            message.arg1 = size;
                        }
                        handler.sendMessage(message);
                    }
                });

            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {

            }
        });
    }

    public void updateData() {
        if (!showNetWaitLoding())
            return;

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        JSONObject json = new JSONObject();

        String questionText = "";
        String questionAudioUrl = "";
        questionText = editMiaoshu.getText().toString();
        StringBuffer imageUrl = new StringBuffer();

        if (imagelistDatasForOss != null && imagelistDatasForOss.size() > 0) {
            for (int i = 0; i < imagelistDatasForOss.size(); i++) {
                if (i == imagelistDatasForOss.size() - 1) {
                    imageUrl.append(imagelistDatasForOss.get(i));
                } else {
                    imageUrl.append(imagelistDatasForOss.get(i) + ",");
                }
            }
        }
        if (!TextUtils.isEmpty(audioPath)) {
            questionAudioUrl = audioPath;
        }
        try {
            json.put("questionId", questionId);
            json.put("questionText", questionText);
            json.put("questionImageUrl", imageUrl);
            json.put("questionAudioUrl", questionAudioUrl);
            json.put("audioDuration", recordingSecond);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, TAG + "提交追问json->" + json.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).submitUserMineQuestionJson(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        Log.e(QuestionCloselyActivity.class.getSimpleName(),
                                QuestionCloselyActivity.class.getSimpleName() + "updatadate-->" + response);
                        QuestionPayDetailBean mQuestionPayDetailBean = ParseUtil.parseBean(response, QuestionPayDetailBean.class);
                        //ToastUtils.showToastLONG(mContext, "提交成功");
                        //  if ("0".equals(mQuestionPayDetailBean.getData().getHasBuy())) {
                        if (mQuestionPayDetailBean != null) {
                            Intent intent = new Intent(mContext, AnswerBuyActivity.class);
                            intent.putExtra("describe", editMiaoshu.getText().toString());
                            intent.putExtra("mQuestionPayDetailBean", mQuestionPayDetailBean);
                            intent.putExtra("audioPath", tempRecorderPath);
                            intent.putExtra("buyId", mQuestionPayDetailBean.getData().getQuestionId());
                            intent.putExtra("type", 3);
                            intent.putExtra("buyType", 6);
                            intent.putExtra("recordingSecond", recordingSecond);
                            intent.putExtra("class", QuestionCloselyActivity.class.getSimpleName());
                            startActivityForResult(intent, 111);
                        }
                        /*} else {
                            ToastUtils.showToastSHORT(mContext, "提交成功");

                        }*/
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    private void audioPlayOrPause() {
        if (isRecording) {  //判断是否正在录音
            if (isPause) { //判断是否暂停录音
                resumeRecording();
                ivPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.zt));
            } else {
                pauseRecording();
                ivPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.ks));

            }
        } else {
            startRecording();
//            dayi_tv_ly_submit
            tvDaYiLuYinSubmit.setBackground(getResources().getDrawable(R.drawable.btn_ly_shape_circle_green_click));
            tvDaYiLuYinSubmit.setEnabled(true);
            ivPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.zt));
        }
    }

    /**
     * 点击完成录制相应操作
     */
    private void finishRecord() {
        ivPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.cf2));
        rlLuYin.setBackground(getResources().getDrawable(R.drawable.btn_ly_shape_circle_click));
        ivPlayOrPause.setEnabled(false);
        tvDaYiLuYinSubmit.setEnabled(false);
        tipLy.setBackground(getResources().getDrawable(R.drawable.bf2));
        tvDaYiLuYinSubmit.setBackground(getResources().getDrawable(R.drawable.btn_ly_shape_circle_unclick));
        ivDeleteLuYin.setVisibility(View.VISIBLE);
        stopActionRecording();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 111) {
            finish();
        }
        if (resultCode == 111 && requestCode == 111) {
            setResult(111,new Intent());
            finish();
        }
    }
}
