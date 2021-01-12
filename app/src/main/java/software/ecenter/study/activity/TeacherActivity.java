package software.ecenter.study.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.umeng.commonsdk.debug.I;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import software.ecenter.study.View.SchoolPopWindow;
import software.ecenter.study.View.SpinnerPopWindow;
import software.ecenter.study.bean.ImageBean;
import software.ecenter.study.bean.MineBean.CheckTeacherBean;
import software.ecenter.study.bean.NameAndIdArrayBean;
import software.ecenter.study.bean.OssTokenBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.PermissionUtils;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * 教师资格认证
 */
public class TeacherActivity extends BasePicActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.xueke_tip)
    TextView xuekeTip;
    @BindView(R.id.xueke_text)
    TextView xuekeText;
    @BindView(R.id.xueke_img)
    ImageView xuekeImg;
    @BindView(R.id.btn_xueke)
    RelativeLayout btnXueke;
    @BindView(R.id.xingbie_tip)
    TextView xingbieTip;
    @BindView(R.id.xingbie_text)
    TextView xingbieText;
    @BindView(R.id.xingbie_img)
    ImageView xingbieImg;
    @BindView(R.id.btn_xingbie)
    RelativeLayout btnXingbie;
    @BindView(R.id.btn_shengshiqu)
    LinearLayout btn_shengshiqu;
    @BindView(R.id.shengshiqu_text)
    TextView shengshiqu_text;
    @BindView(R.id.shengshiqu_img)
    ImageView shengshiqu_img;
    @BindView(R.id.btn_xuexiao)
    LinearLayout btn_xuexiao;
    @BindView(R.id.xuexiao_edit)
    TextView xuexiaoEdit;
    @BindView(R.id.xuexiao_img)
    ImageView xuexiao_img;
    @BindView(R.id.zhicheng_tip)
    TextView zhichengTip;
    @BindView(R.id.zhicheng_text)
    TextView zhichengText;
    @BindView(R.id.zhicheng_img)
    ImageView zhichengImg;
    @BindView(R.id.btn_zhicheng)
    RelativeLayout btnZhicheng;
    @BindView(R.id.nianling_tip)
    TextView nianlingTip;
    @BindView(R.id.nianling_edit)
    EditText nianlingEdit;
    @BindView(R.id.btn_submission)
    Button btnSubmission;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.list_image)
    RecyclerView listImage;
    @BindView(R.id.scrl_teacher)
    ScrollView scrTeacher;
    @BindView(R.id.ll_check_teacher)
    LinearLayout llCheckTeacher;
    @BindView(R.id.tv_check_teacker)
    TextView tvCheckTeacher;

    private SpinnerPopWindow spinnerPopWindowXK;
    private SpinnerPopWindow spinnerPopWindowSex;
    private SpinnerPopWindow spinnerPopWindowTitle;
    private List<String> spinnerListXK;
    private List<String> spinnerListSex;
    private List<String> spinnerListTitle;

    private List<ImageBean> imageBeanList;
    private List<String> imageListForOss;
    CheckTeacherBean teacherBean = null;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (imageListForOss.size() == imageBeanList.size()) {
                Log.e(TAG, imageListForOss.size() + "");
                uploadResource(imageListForOss);
            }
        }
    };

    /**
     * 获取省列表
     */
    private SpinnerPopWindow spinnerPopCity;
    private List<NameAndIdArrayBean.DataBean> ProvincesList;
    private List<NameAndIdArrayBean.DataBean> CityList;
    private List<NameAndIdArrayBean.DataBean> AreaList;
    private List<String> spinnerProvincesName = new ArrayList<>();
    private List<String> spinnerCityName = new ArrayList<>();
    private List<String> spinnerAreaName = new ArrayList<>();
    private String addressProvince = "";
    private String provinceId = "";
    private String addressCity = "";
    private String cityId = "";
    private String addressArea = "";
    private String areaId = "";

    private List<NameAndIdArrayBean.DataBean> SchoolList;
    private SchoolPopWindow spinnerPopWindowSchool;    //学校
    private String schoolId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MAX_IMAGE_COUNT = 1;
        teacherBean = new CheckTeacherBean();
        setContentView(R.layout.activity_teacher);
        ButterKnife.bind(this);
        imageBeanList = new ArrayList<>();
        imageListForOss = new ArrayList<>();
        //学科
        spinnerListXK = ToolUtil.StringToArray(XueKe, ",");
        spinnerPopWindowXK = new SpinnerPopWindow(mContext);
        spinnerPopWindowXK.refreshData(spinnerListXK);
        spinnerPopWindowXK.setPopTitle("选择学科");
        spinnerPopWindowXK.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                xuekeText.setText(spinnerListXK.get(position));
            }
        });
        spinnerPopWindowXK.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                xuekeImg.setImageResource(R.drawable.xuanze1);
            }
        });


        //性别
        spinnerListSex = ToolUtil.StringToArray(Sex, ",");
        spinnerPopWindowSex = new SpinnerPopWindow(mContext);
        spinnerPopWindowSex.refreshData(spinnerListSex);
        spinnerPopWindowSex.setPopTitle("选择性别");
        spinnerPopWindowSex.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                xingbieText.setText(spinnerListSex.get(position));
            }
        });
        spinnerPopWindowSex.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                xingbieImg.setImageResource(R.drawable.xuanze1);
            }
        });

        //学校
        spinnerPopWindowSchool = new SchoolPopWindow(mContext);
        spinnerPopWindowSchool.setPopTitle("选择学校");
        spinnerPopWindowSchool.setItemSelectListener(new SchoolPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                schoolId = SchoolList.get(position).getId();
                String school = SchoolList.get(position).getName();
                xuexiaoEdit.setText(school);
            }
        });
        spinnerPopWindowSchool.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                xuexiao_img.setImageResource(R.drawable.xuanze1);
            }
        });
        getProvinces();
        // 城市
        spinnerPopCity = new SpinnerPopWindow(mContext);
        spinnerPopCity.setPopTitle("选择地区");
        spinnerPopCity.toTagMode("请选择省");
        spinnerPopCity.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                switch (spinnerPopCity.getLevel()) {
                    case 0:
                        spinnerPopCity.TagOneOk(spinnerProvincesName.get(position), "请选择市");
                        addressProvince = ProvincesList.get(position).getName();
                        provinceId = ProvincesList.get(position).getId();
                        getCityAndArea(provinceId, true);
                        break;
                    case 1:
                        spinnerPopCity.TagTwoOk(spinnerCityName.get(position), "请选择区");
                        addressCity = CityList.get(position).getName();
                        cityId = CityList.get(position).getId();
                        getCityAndArea(cityId, false);
                        break;
                    case 2:
                        addressArea = AreaList.get(position).getName();
                        areaId = AreaList.get(position).getId();
                        getSchool(areaId);
                        shengshiqu_text.setText(addressProvince + addressCity + addressArea);
                        schoolId = "";
                        xuexiaoEdit.setText("");
                        spinnerPopCity.dismiss();
                        break;

                }
            }
        });
        spinnerPopCity.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                shengshiqu_img.setImageResource(R.drawable.xuanze1);
            }
        });

        //职称
        spinnerListTitle = ToolUtil.StringToArray(TeacherTitle, ",");
        spinnerPopWindowTitle = new SpinnerPopWindow(mContext);
        spinnerPopWindowTitle.refreshData(spinnerListTitle);
        spinnerPopWindowTitle.setPopTitle("选择职称");
        spinnerPopWindowTitle.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                zhichengText.setText(spinnerListTitle.get(position));
            }
        });
        spinnerPopWindowTitle.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                zhichengImg.setImageResource(R.drawable.xuanze1);
            }
        });

        //图片
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        listImage.setLayoutManager(gridLayoutManager);
        imageAdapter = new ImageAdapter(mContext, listImageData);
        Log.i("++++++++++++++", "onCreate: " + listImageData.size());
        imageAdapter.setItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (listImageData.get(position).isAddImage()) {
                    spinnerPopWindow.setPopTitle("请上传教师资格证照片");
                    spinnerPopWindow.getmPopTile().setTextColor(getResources().getColor(R.color.textHoldColor));
                    PermissionUtils.newIntence().requestPerssion(TeacherActivity.this, ToolUtil.PERSSION_PHOTO, 101, new PermissionUtils.IPermission() {
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

        nianlingEdit.addTextChangedListener(new TextWatcher() {
            private String outStr = ""; //这个值存储输入超过两位数时候显示的内容

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String edit = s.toString();
                if (edit.length() == 2 && Integer.parseInt(edit) >= 10) {
                    outStr = edit;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String words = s.toString();
                //首先内容进行非空判断，空内容（""和null）不处理
                if (!TextUtils.isEmpty(words)) {
                    //1-100的正则验证
                    Pattern p = Pattern.compile("^(81|[1-9]\\d|\\d)$");
                    Matcher m = p.matcher(words);
                    if (m.find() || ("").equals(words)) {
                        //这个时候输入的是合法范围内的值
                    } else {
                        if (words.length() > 2) {
                            //若输入不合规，且长度超过2位，继续输入只显示之前存储的outStr
                            nianlingEdit.setText(outStr);
                            //重置输入框内容后默认光标位置会回到索引0的地方，要改变光标位置
                            nianlingEdit.setSelection(2);
                        }
                        //ToastUtil.showToast("请输入范围在1-100之间的整数");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //这里的处理是不让输入0开头的值
                String words = s.toString();
                //首先内容进行非空判断，空内容（""和null）不处理
                if (!TextUtils.isEmpty(words)) {
                    if (Integer.parseInt(s.toString()) <= 0) {
                        nianlingEdit.setText("");
                        // ToastUtil.showToast("请输入范围在1-100之间的整数");
                    }
                }

            }
        });

        getTeacherCheck();
    }

    @OnClick({R.id.btn_left_title, R.id.btn_xueke, R.id.btn_xuexiao, R.id.btn_shengshiqu, R.id.btn_xingbie, R.id.btn_zhicheng, R.id.btn_submission})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_xueke:
                spinnerPopWindowXK.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                xuekeImg.setImageResource(R.drawable.xuanze2);
                break;
            case R.id.btn_xingbie:
                spinnerPopWindowSex.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                xingbieImg.setImageResource(R.drawable.xuanze2);
                break;
            case R.id.btn_zhicheng:
                spinnerPopWindowTitle.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                zhichengImg.setImageResource(R.drawable.xuanze2);
                break;
            case R.id.btn_shengshiqu://地区
                if (spinnerPopCity.getLevel() != -1) {
                    spinnerPopCity.clearData();
                    spinnerPopCity.refreshData(spinnerProvincesName);
                    spinnerPopCity.setPopTitle("选择地区");
                    spinnerPopCity.toTagMode("请选择省");
//                    spinnerPopCity.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                } else {
                }
                spinnerPopCity.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                shengshiqu_img.setImageResource(R.drawable.xuanze2);
                break;
            case R.id.btn_xuexiao://学校
                if (SchoolList == null) return;
                spinnerPopWindowSchool.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                xuexiao_img.setImageResource(R.drawable.xuanze2);
                break;
            case R.id.btn_submission:
                if ("请选择".equals(xuekeText.getText().toString())) {
                    ToastUtils.showToastSHORT(mContext, "请选择学科");
                    return;
                }
                if ("请选择".equals(xingbieText.getText().toString())) {
                    ToastUtils.showToastSHORT(mContext, "请选择性别");
                    return;
                }
                if ("请选择".equals(zhichengText.getText().toString())) {
                    ToastUtils.showToastSHORT(mContext, "请选择职称");
                    return;
                }
                if (TextUtils.isEmpty(xuexiaoEdit.getText().toString())) {
                    ToastUtils.showToastSHORT(mContext, "请选择学校");
                    return;
                }
                if (TextUtils.isEmpty(nianlingEdit.getText().toString())) {
                    ToastUtils.showToastSHORT(mContext, "请输入教龄");
                    return;
                }
                if (listImageData.size() > 0) { //大于1 是因为还有一张默认图片

                    //qualityCertification();
                    updateImages2Oss();

                } else {
                    if (imageAdapter.getItemCount() > 0)
                        ToastUtils.showToastSHORT(mContext, "请重新上传教师资格证照片");
                    else
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
        if (imageBeanList != null) {
            if (imageBeanList.size() <= 0) {
                ToastUtils.showToastSHORT(mContext, "请上传教师资格证照片");
                return;
            }
            for (int i = 0; i < imageBeanList.size(); i++) {
                getOssToken(i, imageBeanList.get(i));
            }
        }
    }

    public void getOssToken(final int size, final ImageBean imageBean) {
        Map<String, String> header = new HashMap<>();
        header.put("token", AccountUtil.getToken(mContext) + "");
        //获取osstoken
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getOssTokenByTeacher(header))
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
        Log.d("111111uploadData", "onSuccess图片：" + OSSConfig.OSSPATH + path + pathName);

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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("111111uploadData", "onSuccess：" + "上传图片失败");
                                ToastUtils.showToastLONG(mContext, "头像上传失败，请稍后重试！");
                            }
                        });
                    }
                });
    }

    public void uploadResource(List<String> imageListForOss) {

        if (!showNetWaitLoding()) {
            return;
        }
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
            json.put("subject", xuekeText.getText().toString());
            if ("男".equals(xingbieText.getText().toString())) {
                json.put("sex", "1");
            } else {
                json.put("sex", "2");
            }
            if (teacherBean != null && teacherBean.getData().getId() > 0)
                json.put("id", teacherBean.getData().getId() + "");
            json.put("school", xuexiaoEdit.getText().toString());
            json.put("schoolId", schoolId);
            json.put("title", zhichengText.getText().toString());
            json.put("seniority", nianlingEdit.getText().toString());
            json.put("qualityPicturesUrl", qualityPicturesUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).qualityCertification(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, "提交成功");
                        AccountUtil.saveIsTeacherChecked(mContext, 0);
                        onBackPressed();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();

                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    public void getTeacherCheck() {
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).teacherCheck())
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        teacherBean = ParseUtil.parseBean(response, CheckTeacherBean.class);
                        if (teacherBean != null && teacherBean.getStatus() == 1) {
                            if (teacherBean.getData() != null)
                                setView(teacherBean.getData());
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {

                    }
                });
    }

    private void setView(CheckTeacherBean.DataBean data) {
        if (!data.isIsChecked()) {
            tvCheckTeacher.setText("请输入您的教师资质信息");
            return;
        }
        setViewCanEnable(data.getState());
        if (data.getState() == 0) {
            tvCheckTeacher.setText("教师资格认证审核中，请您耐心等候");
        }
        if (data.getState() == 1) {
            tvCheckTeacher.setText("审核通过");
        }
        if (data.getState() == 2) {
            tvCheckTeacher.setText("审核未通过\n " + data.getAuditContent());
        }
        if (!TextUtils.isEmpty(data.getAuditContent())) {
            tvCheckTeacher.setText(data.getAuditContent());
        }
        xuekeText.setText(data.getSubject());
        xingbieText.setText(data.getSex() == 1 ? "男" : "女");
        xuexiaoEdit.setText(data.getSchool());
        zhichengText.setText(data.getTitle());
        nianlingEdit.setText(data.getSeniority() + "");
        if (listImageData == null)
            listImageData = new ArrayList<>();
        else
            listImageData.clear();
        ImageBean imageBean = new ImageBean();
        imageBean.setTargetPicPath(data.getQualityPicturesUrl());
        listImageData.add(imageBean);
        if (imageAdapter == null)
            imageAdapter = new ImageAdapter(mContext, listImageData);
        else
            imageAdapter.refreshData(listImageData);

    }

    private void setViewCanEnable(int state) {
        if (state == 0) {
            boolean isfalse = false;
            btnXueke.setClickable(isfalse);
            btnXingbie.setClickable(isfalse);
            xuexiaoEdit.setFocusable(isfalse);
            nianlingEdit.setFocusable(isfalse);
            btnZhicheng.setClickable(isfalse);
            listImage.setClickable(isfalse);
            btnSubmission.setClickable(isfalse);
        } else {
            boolean isTrue = true;
            btnXueke.setClickable(isTrue);
            btnXingbie.setClickable(isTrue);
            xuexiaoEdit.setFocusable(isTrue);
            nianlingEdit.setFocusable(isTrue);
            btnZhicheng.setClickable(isTrue);
            listImage.setClickable(isTrue);
            btnSubmission.setClickable(isTrue);
        }
    }

    //获取省
    private void getProvinces() {
        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext, false).getProvinces(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        NameAndIdArrayBean pro = ParseUtil.parseBean(response, NameAndIdArrayBean.class);
                        ProvincesList = pro.getData();
                        spinnerProvincesName.clear();
                        for (NameAndIdArrayBean.DataBean bean : ProvincesList) {
                            spinnerProvincesName.add(bean.getName());
                        }
                        spinnerPopCity.refreshData(spinnerProvincesName);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }


    //获取学校
    private void getSchool(String id) {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("name", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext, false).getSchools(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        NameAndIdArrayBean pro = ParseUtil.parseBean(response, NameAndIdArrayBean.class);
                        SchoolList = pro.getData();
                        spinnerPopWindowSchool.refreshData(SchoolList);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    //获取市区
    private void getCityAndArea(String Id, final boolean isCity) {
        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("id", Id);
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext, false).getCityOrArea(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        NameAndIdArrayBean city = ParseUtil.parseBean(response, NameAndIdArrayBean.class);
                        if (isCity) {
                            CityList = city.getData();
                            spinnerCityName.clear();
                            for (NameAndIdArrayBean.DataBean bean : CityList) {
                                spinnerCityName.add(bean.getName());
                            }
                            spinnerPopCity.refreshData(spinnerCityName);
                        } else {
                            AreaList = city.getData();
                            spinnerAreaName.clear();
                            for (NameAndIdArrayBean.DataBean bean : AreaList) {
                                spinnerAreaName.add(bean.getName());
                            }
                            spinnerPopCity.refreshData(spinnerAreaName);
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }
}
