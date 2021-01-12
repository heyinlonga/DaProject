package software.ecenter.study.activity;

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
import software.ecenter.study.bean.HuoDongBean.knowledgeDetailPoint;
import software.ecenter.study.bean.HuoDongBean.knowledgePoint;
import software.ecenter.study.bean.ImageBean;
import software.ecenter.study.bean.OssTokenBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/***
 * des 上传资源
 *
 * */
public class TeacherUpdataActivity extends BasePicActivity implements ConstantData {

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
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.zhishidian_text)
    TextView zhishidianText;

    private SpinnerPopWindow spinnerPopWindowNJ;
    private SpinnerPopWindow spinnerPopWindowXK;

    private List<String> spinnerList;
    private List<String> spinnerListtwo;
    private List<String> imageListForOss;

    private String resourceId;

    private knowledgeDetailPoint mknowledgeDetailPoint;
    private List<ImageBean> imageBeanList;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (imageListForOss.size() == imageBeanList.size()) {
                Log.e(TAG, imageListForOss.size() + "");
                uploadResource(imageListForOss);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_updata);
        ButterKnife.bind(this);
        if (imageListForOss == null)
            imageListForOss = new ArrayList<>();
        if (imageBeanList == null) {
            imageBeanList = new ArrayList<>();
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
                    spinnerPopWindow.setPopTitle("请上传教师资格证照片");
                    spinnerPopWindow.getmPopTile().setTextColor(getResources().getColor(R.color.textblackColor));

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
        resourceId = getIntent().getStringExtra("resourceId");
        getData();
    }


    public void getData() {
        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("resourceId", resourceId);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getKnowledgePoint(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.GONE);
                        knowledgeDetailPoint bean = ParseUtil.parseBean(response, knowledgeDetailPoint.class);
                        setResponseView(bean);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });

    }

    public void setResponseView(knowledgeDetailPoint bean) {
        mknowledgeDetailPoint = bean;
        List<knowledgePoint> points = bean.getData().getKnowledgePointList();
        String zhishidian = "";
        for (knowledgePoint item : points) {
            zhishidian += item.getKnowledgePointName() + ",";
        }


        if (zhishidian.endsWith(",")) {
            zhishidian = zhishidian.substring(0, zhishidian.length() - 1);
        }
        zhishidianText.setText(zhishidian);
        nianjiText.setText(bean.getData().getGrade());
        xuekeText.setText(bean.getData().getSubject());
    }

    /**
     * todo 重写的返回服务器的方法
     */
    public void uploadResource(List<String> imageListForOss) {
        if (!showNetWaitLoding())
            return;

        List<knowledgePoint> points = mknowledgeDetailPoint.getData().getKnowledgePointList();
        String Ids = "";
        for (knowledgePoint item : points) {
            Ids += item.getKnowledgePointId() + ",";
        }

        if (Ids.endsWith(",")) {
            Ids = Ids.substring(0, Ids.length() - 1);
        }
        StringBuffer imageUrl = new StringBuffer();
        for (int i = 0; i < imageListForOss.size(); i++) {
            if (i == imageListForOss.size() - 1)
                imageUrl.append(imageListForOss.get(i));
            else
                imageUrl.append(imageListForOss.get(i) + ",");
        }

        JSONObject json = new JSONObject();
        try {
            json.put("resourceId", resourceId);
            json.put("knowledgePoint", Ids);
            json.put("grade", nianjiText.getText().toString());
            json.put("subject", xuekeText.getText().toString());
            json.put("imageUrl", imageUrl.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).uploadResource(body))
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

    public void uploadResource() {
        if (!showNetWaitLoding()) {
            return;
        }

        List<knowledgePoint> points = mknowledgeDetailPoint.getData().getKnowledgePointList();
        String Ids = "";
        for (knowledgePoint item : points) {
            Ids += item.getKnowledgePointId() + ",";
        }

        if (Ids.endsWith(",")) {
            Ids = Ids.substring(0, Ids.length() - 1);
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("resourceId", resourceId);
        map.put("knowledgePoint", Ids);
        map.put("grade", nianjiText.getText().toString());
        map.put("subject", xuekeText.getText().toString());
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (ImageBean item : listImageData) {
            if (!item.isAddImage()) {
                parts.add(RetroFactory.prepareFilePart("image", new File(item.getTargetPicPath())));
            }
        }

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).uploadResource(map, parts))
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


    @OnClick({R.id.btn_left_title, R.id.btn_nianji, R.id.btn_xueke, R.id.btn_refresh_net, R.id.btn_submission})
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
            case R.id.btn_refresh_net:
                getData();
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
                if (TextUtils.isEmpty(zhishidianText.getText().toString())){
                    ToastUtils.showToastSHORT(mContext, "请输入知识点");
                    return;
                }
                if (listImageData != null && listImageData.size() > 0) { //大于1 是因为还有一张默认图片
                    updateImages2Oss();
//                    uploadResource();

                } else {
                        ToastUtils.showToastSHORT(mContext, "请上传教师资格证照片");
                    return;
                }

                break;
        }
    }

    public void updateImages2Oss() {
        imageListForOss.clear();
        imageBeanList.clear();
        for (ImageBean imageBean : listImageData) {
            if (!TextUtils.isEmpty(imageBean.getTargetPicPath())) {
                imageBeanList.add(imageBean);
            }
        }
        if (imageBeanList != null && imageBeanList.size() > 0) {
            for (int i = 0; i < imageBeanList.size(); i++) {
                getOssToken(i, imageBeanList.get(i));
            }
        }else{
            ToastUtils.showToastSHORT(mContext,"请上传教师资格证照片");
        }
    }

    public void getOssToken(final int size, final ImageBean imageBean) {
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
                        uploadImage2Oss(size, imageBean);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(mContext, msg);
                        dismissWaitLoging();
                    }
                });

    }

    public void uploadImage2Oss(final int size, ImageBean imageBean) {
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
                                handler.sendMessage(message);
                            }
                        });

                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {

                    }
                });
    }
}