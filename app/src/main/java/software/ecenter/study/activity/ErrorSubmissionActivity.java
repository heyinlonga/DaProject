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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import software.ecenter.study.Interface.ConstantData;
import software.ecenter.study.OSSSImple.OSSConfig;
import software.ecenter.study.OSSSImple.PutObjectSamples;
import software.ecenter.study.R;
import software.ecenter.study.View.SpinnerPopWindow;
import software.ecenter.study.bean.HuoDongBean.knowledgePoint;
import software.ecenter.study.bean.ImageBean;
import software.ecenter.study.bean.OssTokenBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * des  易错题投稿
 */
public class ErrorSubmissionActivity extends BasePicActivity implements ConstantData {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.btn_nianji)
    RelativeLayout btnNianji;
    @BindView(R.id.btn_xueke)
    RelativeLayout btnXueke;
    @BindView(R.id.zhishidian_tip)
    TextView zhishidianTip;
    @BindView(R.id.btn_zhishidian)
    RelativeLayout btnZhishidian;
    @BindView(R.id.btn_submission)
    Button btnSubmission;
    @BindView(R.id.nianji_tip)
    TextView nianjiTip;
    @BindView(R.id.nianji_text)
    TextView nianjiText;
    @BindView(R.id.nianji_img)
    ImageView nianjiImg;
    @BindView(R.id.xueke_tip)
    TextView xuekeTip;
    @BindView(R.id.xueke_text)
    TextView xuekeText;
    @BindView(R.id.xueke_img)
    ImageView xuekeImg;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.list_image)
    RecyclerView listImage;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.zhishidian_text)
    TextView zhishidianText;

    private SpinnerPopWindow spinnerPopWindowNJ;
    private SpinnerPopWindow spinnerPopWindowXK;

    private List<String> spinnerList;
    private List<String> spinnerListtwo;

    private String activityId;
    private String activityType;
    private final int REQUEST_CODE = 111;
    private knowledgePoint mknowledgePoint;
    private List<String> imageListForOss;
    private List<ImageBean> imageBeanList;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            int arg1 = msg.arg1;
            if (imageListForOss.size() == imageBeanList.size()) {
                Log.e(TAG, imageListForOss.size() + "");
                uploadResource(arg1, imageListForOss);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_submission);
        ButterKnife.bind(this);
        imageListForOss = new ArrayList<>();
        imageBeanList = new ArrayList<>();
        MAX_IMAGE_COUNT = 5;

        activityId = getIntent().getStringExtra("activityId");
        activityType = getIntent().getStringExtra("activityType");
        if ("2".equals(activityType)) { //教师
            titleContent.setText("教师投稿");
        } else if ("3".equals(activityType)) { //易错题
            titleContent.setText("易错题投稿");
        }
        // 年级
        spinnerList = ToolUtil.StringToArray(Grade, ",");
        spinnerPopWindowNJ = new SpinnerPopWindow(mContext);
        spinnerPopWindowNJ.refreshData(spinnerList);
        spinnerPopWindowNJ.setPopTitle("选择年级");
        spinnerPopWindowNJ.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                nianjiText.setText(spinnerList.get(position));
            }
        });
        spinnerPopWindowNJ.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                nianjiImg.setImageResource(R.drawable.xuanze1);
            }
        });


        //学科
        spinnerListtwo = ToolUtil.StringToArray(XueKe, ",");
        spinnerPopWindowXK = new SpinnerPopWindow(mContext);
        spinnerPopWindowXK.refreshData(spinnerListtwo);
        spinnerPopWindowXK.setPopTitle("选择学科");
        spinnerPopWindowXK.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                xuekeText.setText(spinnerListtwo.get(position));
            }
        });
        spinnerPopWindowNJ.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                xuekeImg.setImageResource(R.drawable.xuanze1);
            }
        });

        //图片
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        listImage.setLayoutManager(gridLayoutManager);
        imageAdapter = new ImageAdapter(mContext, listImageData);
        imageAdapter.setItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (listImageData.get(position).isAddImage()) {
                    spinnerPopWindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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

    }

    @OnClick({R.id.btn_left_title, R.id.btn_nianji, R.id.btn_xueke, R.id.btn_zhishidian, R.id.btn_submission})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                finish();
                break;
            case R.id.btn_nianji:
                spinnerPopWindowNJ.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                nianjiImg.setImageResource(R.drawable.xuanze2);
                break;
            case R.id.btn_xueke:
                spinnerPopWindowXK.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                xuekeImg.setImageResource(R.drawable.xuanze2);
                break;
            case R.id.btn_zhishidian:
                Intent intent = new Intent(mContext, KnowledgePointActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.btn_submission:
                if ("请选择".equals(nianjiText.getText().toString())) {
                    ToastUtils.showToastSHORT(mContext, "请选择年级");
                    return;
                }
                if ("请选择".equals(xuekeText.getText().toString())) {
                    ToastUtils.showToastSHORT(mContext, "请选择学科");
                    return;
                }
                if (mknowledgePoint == null && zhishidianText.getText().toString().contains("请搜索知识点")) {
                    ToastUtils.showToastSHORT(mContext, "请选择知识点");
                    return;
                }

                if (listImageData != null && listImageData.size() > 1) { //大于1 是因为还有一张默认图片

                    if ("2".equals(activityType)) { //教师
                        updateImages2Oss(2);
                    } else if ("3".equals(activityType)) { //易错题
                        updateImages2Oss(3);
                    }

                } else {
                    ToastUtils.showToastSHORT(mContext, "未选择图片");
                    return;
                }

                break;
        }
    }

    //教师
    public void submitTeacher() {
        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("activityId", activityId);
        if (mknowledgePoint == null || TextUtils.isEmpty(mknowledgePoint.getKnowledgePointId()))
            map.put("knowledgePoint", zhishidianText.getText().toString().trim());
        else
            map.put("knowledgeItemId", mknowledgePoint.getKnowledgePointId());
        map.put("grade", nianjiText.getText().toString());
        map.put("subject", xuekeText.getText().toString());
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (ImageBean item : listImageData) {
            if (!item.isAddImage()) {
                parts.add(RetroFactory.prepareFilePart("image", new File(item.getTargetPicPath())));
            }
        }

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).submitTeacher(map, parts))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, "上传成功");
                        onBackPressed();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();

                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    //易错题
    public void submitEasyQuestion() {
        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("activityId", activityId);
        if (mknowledgePoint == null || TextUtils.isEmpty(mknowledgePoint.getKnowledgePointId()))
            map.put("knowledgePoint", zhishidianText.getText().toString().trim());
        else
            map.put("knowledgeItemId", mknowledgePoint.getKnowledgePointId());
        map.put("grade", nianjiText.getText().toString());
        map.put("subject", xuekeText.getText().toString());
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (ImageBean item : listImageData) {
            if (!item.isAddImage()) {
                parts.add(RetroFactory.prepareFilePart("image", new File(item.getTargetPicPath())));
            }
        }

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).submitEasyQuestion(map, parts))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, "上传成功");
                        onBackPressed();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();

                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            mknowledgePoint = (knowledgePoint) data.getSerializableExtra("mknowledgePoint");
            zhishidianText.setText(mknowledgePoint.getKnowledgePointName());
            return;

        }
        if (resultCode == 123123 && requestCode == REQUEST_CODE) {
            String zhishidian = data.getStringExtra("mknowledgePoint");
            if (!TextUtils.isEmpty(zhishidian))
                zhishidianText.setText(zhishidian);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void uploadResource(int type, List<String> imageListForOss) {
        if (!showNetWaitLoding())
            return;

        StringBuffer imageUrl = new StringBuffer();
        for (int i = 0; i < imageListForOss.size(); i++) {
            if (i == imageListForOss.size() - 1)
                imageUrl.append(imageListForOss.get(i));
            else
                imageUrl.append(imageListForOss.get(i) + ",");
        }

        JSONObject json = new JSONObject();
        try {
            json.put("activityId", activityId);
            if (mknowledgePoint == null || TextUtils.isEmpty(mknowledgePoint.getKnowledgePointId()))
                json.put("knowledgePoint", zhishidianText.getText().toString().trim());
            else
                json.put("knowledgeItemId", mknowledgePoint.getKnowledgePointId());
            json.put("grade", nianjiText.getText().toString());
            json.put("subject", xuekeText.getText().toString());
            json.put("imageUrl", imageUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, type == 3 ? RetroFactory.getRetroFactory(mContext).submitEasyQuestion(body) : RetroFactory.getRetroFactory(mContext).submitTeacher(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {

                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, "上传成功");
                        onBackPressed();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();

                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    public void updateImages2Oss(int type) {

        if (imageListForOss == null) {
            imageListForOss = new ArrayList<>();
        }
        imageListForOss.clear();
        imageBeanList.clear();
        for (ImageBean imageBean : listImageData) {
            if (!TextUtils.isEmpty(imageBean.getTargetPicPath())) {
                imageBeanList.add(imageBean);
            }
        }
        if (listImageData != null && imageBeanList.size() > 0) {
            for (int i = 0; i < imageBeanList.size(); i++) {
                getOssToken(type, i, imageBeanList.get(i));
            }
        }
    }

    public void getOssToken(final int type, final int size, final ImageBean imageBean) {
        if (!showNetWaitLoding()) {
            return;
        }
        Map<String, String> header = new HashMap<>();
        header.put("token", AccountUtil.getToken(mContext) + "");
        //获取osstoken
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getOssTokenByTouGao(header))
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
                        uploadImage2Oss(type, size, imageBean);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(mContext, msg);
                        dismissWaitLoging();
                    }
                });

    }

    public void uploadImage2Oss(final int type, final int size, ImageBean imageBean) {
        int startIndex = imageBean.getTargetPicPath().lastIndexOf("/");
        String pathName = imageBean.getTargetPicPath().substring(startIndex + 1, imageBean.getTargetPicPath().length());
        String path = OSSConfig.ossnoteFolder;
        Log.d("111111uploadData", "onSuccess图片：" + path + pathName);

        new PutObjectSamples(OSSConfig.getInstance(getApplicationContext())
                , OSSConfig.testBucket, path + pathName, imageBean.getTargetPicPath())
                .asyncPutObjectFromLocalFile(new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {

                    @Override
                    public void onSuccess(final PutObjectRequest request, PutObjectResult result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageListForOss.add(request.getObjectKey());
                                Message message = new Message();
                                message.what = size;
                                message.arg1 = type;
                                handler.sendMessage(message);
                            }
                        });

                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                        dismissNetWaitLoging();
                    }
                });
    }
}
