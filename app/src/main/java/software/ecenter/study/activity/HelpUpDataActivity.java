package software.ecenter.study.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.PermissionUtils;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * dec 反馈提交
 */
public class HelpUpDataActivity extends BasePicActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.btn_help_one_img)
    ImageView btnHelpOneImg;
    @BindView(R.id.btn_help_one)
    RelativeLayout btnHelpOne;
    @BindView(R.id.btn_help_two_img)
    ImageView btnHelpTwoImg;
    @BindView(R.id.btn_help_two)
    RelativeLayout btnHelpTwo;
    @BindView(R.id.btn_help_three_img)
    ImageView btnHelpThreeImg;
    @BindView(R.id.btn_help_three)
    RelativeLayout btnHelpThree;
    @BindView(R.id.composition_context)
    EditText compositionContext;
    @BindView(R.id.btn_submission)
    Button btnSubmission;
    @BindView(R.id.list_image)
    RecyclerView listImage;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.one_title)
    TextView oneTitle;
    @BindView(R.id.two_title)
    TextView twoTitle;
    @BindView(R.id.three_title)
    TextView threeTitle;
    @BindView(R.id.tv_tip)
    TextView tvTip;

    private String mTitle;

    private List<ImageBean> imageBeanList;
    private List<String> imageListForOss;
    private Map<Integer, String> imageMap;
    private List<Integer> imageIndex;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == 101) {
                btnSubmission.setClickable(true);
            } else if (imageIndex.size() == imageMap.size() && imageMap.size() == imageBeanList.size()) {
                dismissNetWaitLoging();
                for (int i = 0; i < imageIndex.size(); i++) {
                    imageListForOss.add(imageMap.get(imageIndex.get(i)));
                }
                uploadResource(imageListForOss);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_up_data);
        ButterKnife.bind(this);
        imageBeanList = new ArrayList<>();
        imageListForOss = new ArrayList<>();
        imageIndex = new ArrayList<>();
        imageMap = new HashMap<>();

        //图片
        listImage.setLayoutManager(new GridLayoutManager(mContext, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        imageAdapter = new ImageAdapter(mContext, listImageData);
        imageAdapter.setItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (listImageData.get(position).isAddImage()) {
                    PermissionUtils.newIntence().requestPerssion(HelpUpDataActivity.this, ToolUtil.PERSSION_PHOTO, 101, new PermissionUtils.IPermission() {
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
        mTitle = 0 + "";
    }


    public void chooseType(int type) {

        switch (type) {
            case 0:
                btnHelpOneImg.setVisibility(View.VISIBLE);
                btnHelpTwoImg.setVisibility(View.INVISIBLE);
                btnHelpThreeImg.setVisibility(View.INVISIBLE);
//                mTitle = oneTitle.getText().toString();
                mTitle = type + "";
                break;

            case 1:
                btnHelpOneImg.setVisibility(View.INVISIBLE);
                btnHelpTwoImg.setVisibility(View.VISIBLE);
                btnHelpThreeImg.setVisibility(View.INVISIBLE);
//                mTitle = twoTitle.getText().toString();
                mTitle = type + "";
                break;

            case 2:
                btnHelpOneImg.setVisibility(View.INVISIBLE);
                btnHelpTwoImg.setVisibility(View.INVISIBLE);
                btnHelpThreeImg.setVisibility(View.VISIBLE);
//                mTitle = threeTitle.getText().toString();
                mTitle = type + "";
                break;


        }

    }

    @OnClick({R.id.btn_left_title, R.id.btn_help_one_img, R.id.btn_help_one, R.id.btn_help_two_img, R.id.btn_help_two, R.id.btn_help_three_img, R.id.btn_help_three, R.id.btn_submission})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;

            case R.id.btn_help_one:
                chooseType(0);
                tvTip.setText(getResources().getString(R.string.help_up_data_book));
                break;

            case R.id.btn_help_two:
                chooseType(1);
                tvTip.setText(getResources().getString(R.string.help_up_data_app));
                break;

            case R.id.btn_help_three:
                chooseType(2);
                tvTip.setText(getResources().getString(R.string.help_up_data_other));
                break;
            case R.id.btn_submission:
                btnSubmission.setClickable(false);
                handler.sendEmptyMessageDelayed(101, 5000);
                if (TextUtils.isEmpty(compositionContext.getText().toString()) || !ToolUtil.isKongGe(compositionContext.getText().toString())) {
                    ToastUtils.showToastSHORT(mContext, "请输入内容");
                    return;
                }
                if (listImageData != null && listImageData.size() > 1) { //大于1 是因为还有一张默认图片
                    imageListForOss.clear();
                    imageIndex.clear();
                    imageMap.clear();
                    imageBeanList.clear();
                    for (ImageBean imageBean : listImageData) {
                        if (!TextUtils.isEmpty(imageBean.getTargetPicPath())) {
                            imageBeanList.add(imageBean);
                        }
                    }
                    if (imageBeanList != null && imageBeanList.size() > 0) {
                        PermissionUtils.newIntence().requestPerssion(HelpUpDataActivity.this, ToolUtil.PERSSION_WRITE, 101, new PermissionUtils.IPermission() {
                            @Override
                            public void success(int code) {
                                getOssToken();
                            }
                        });

                    }
                } else {
                    ToastUtils.showToastSHORT(mContext, "未选择图片");
                    return;
                }
                break;
        }
    }

    public void getOssToken() {
        Map<String, String> header = new HashMap<>();
        header.put("token", AccountUtil.getToken(mContext) + "");
        //获取osstoken
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getOssTokenByFanKui(header))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        Log.e(TAG, "get oss token is successful");
                        OssTokenBean ossTokenBean1 = ParseUtil.parseBean(response, OssTokenBean.class);
                        if (ossTokenBean1 == null || ossTokenBean1.getData() == null) return;
                        OssTokenBean ossTokenBean = OssTokenBean.getInstance();
                        ossTokenBean.getData().setAccessKeyId(ossTokenBean1.getData().getAccessKeyId());
                        ossTokenBean.getData().setAccessKeySecret(ossTokenBean1.getData().getAccessKeySecret());
                        ossTokenBean.getData().setBucket(ossTokenBean1.getData().getBucket());
                        ossTokenBean.getData().setEndPoint(ossTokenBean1.getData().getEndPoint());
                        ossTokenBean.getData().setExpiration(ossTokenBean1.getData().getExpiration());
                        ossTokenBean.getData().setSecurityToken(ossTokenBean1.getData().getSecurityToken());
                        ossTokenBean.getData().setUploadUrl(ossTokenBean1.getData().getUploadUrl());
                        ossTokenBean.getData().setUploadFilePath(ossTokenBean1.getData().getUploadFilePath());

                        if (!showNetWaitLoding()) {
                            return;
                        }
                        for (int i = 0; i < imageBeanList.size(); i++) {
                            uploadImage2Oss(i, imageBeanList.get(i));
                        }
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
        String pathName = size + "_" + imageBean.getTargetPicPath().substring(startIndex + 1, imageBean.getTargetPicPath().length());
        String path = OSSConfig.ossnoteFolder;
        Log.d("111111uploadData", "onSuccess图片：" + path + pathName);
        imageIndex.add(size);
        new PutObjectSamples(OSSConfig.getInstance(getApplicationContext())
                , OSSConfig.testBucket, path + pathName, imageBean.getTargetPicPath())
                .asyncPutObjectFromLocalFile(new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {

                    @Override
                    public void onSuccess(final PutObjectRequest request, PutObjectResult result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageMap.put(size, request.getObjectKey());
                                Message message = new Message();
                                message.what = size;
                                handler.sendMessage(message);
                            }
                        });

                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                        ToastUtils.showToastSHORT(mContext, "上传图片失败");
                        dismissNetWaitLoging();
                    }
                });
    }

    public void uploadResource(List<String> imageListForOss) {

        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        JSONObject json = new JSONObject();
        StringBuffer qualityPicturesUrl = new StringBuffer();
        for (int i = 0; i < imageListForOss.size(); i++) {
            if (i == imageListForOss.size() - 1) {
                qualityPicturesUrl.append(imageListForOss.get(i));
            } else {
                qualityPicturesUrl.append(imageListForOss.get(i) + ",");
            }
        }
        try {

            json.put("feedbackTitle", Integer.parseInt(mTitle));
            json.put("feedbackContext", compositionContext.getText().toString());
            json.put("feedbackImageUrl", qualityPicturesUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("shangccha", qualityPicturesUrl.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).feedbackSubmit(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, "提交成功");
                        onBackPressed();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }
}
