package software.ecenter.study.activity;

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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.dinuscxj.progressbar.UnitUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
import software.ecenter.study.View.SchoolPopWindow;
import software.ecenter.study.View.SpinnerPopWindow;
import software.ecenter.study.bean.Bean;
import software.ecenter.study.bean.CityBean;
import software.ecenter.study.bean.ImageBean;
import software.ecenter.study.bean.NameAndIdArrayBean;
import software.ecenter.study.bean.OssTokenBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.EditUtils;
import software.ecenter.study.utils.KeyBoardUtil;
import software.ecenter.study.utils.Logger;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.PermissionUtils;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * dec投稿
 */
public class CompositionSubmissionActivity extends BasePicActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.composition_title)
    EditText compositionTitle;
    @BindView(R.id.composition_context)
    EditText compositionContext;
    @BindView(R.id.btn_submission)
    Button btnSubmission;
    @BindView(R.id.list_image)
    RecyclerView listImage;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.province_text)
    TextView provinceText;
    @BindView(R.id.province_img)
    ImageView provinceImg;
    @BindView(R.id.city_text)
    TextView cityText;
    @BindView(R.id.city_img)
    ImageView cityImg;
    @BindView(R.id.area_text)
    TextView areaText;
    @BindView(R.id.area_img)
    ImageView areaImg;
    @BindView(R.id.school_text)
    TextView schoolText;
    @BindView(R.id.school_img)
    ImageView schoolImg;
    @BindView(R.id.grade_text)
    TextView gradeText;
    @BindView(R.id.grade_img)
    ImageView gradeImg;
    @BindView(R.id.unit_text)
    TextView unitText;
    @BindView(R.id.unit_img)
    ImageView unitImg;
    @BindView(R.id.tvName)
    EditText tvName;
    @BindView(R.id.tvTeacher)
    EditText tvTeacher;
    @BindView(R.id.tvMobile)
    EditText tvMobile;
    @BindView(R.id.tvAddress)
    EditText tvAddress;
    @BindView(R.id.ll_fang)
    LinearLayout ll_fang;
    private String activityId;
    private List<String> imageListForOss;
    private List<ImageBean> imageBeanList;
    private SpinnerPopWindow spinnerPopWindowProvince; //省
    private SpinnerPopWindow spinnerPopWindowCity;  //市
    private SpinnerPopWindow spinnerPopWindowArea;   //区
    private SchoolPopWindow spinnerPopWindowSchool;    //学校
    private SpinnerPopWindow spinnerPopWindowGrade;  //班级
    private SpinnerPopWindow spinnerPopWindowUnit;   //单元
    private String addressProvince = "";
    private String addressCity = "";
    private String addressArea = "";
    private String school = "";
    private String grade = "";
    private String unit = "";

    private String host;
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
        MAX_IMAGE_COUNT = 5;
        setContentView(R.layout.activity_composition_submission);
        ButterKnife.bind(this);
        imageListForOss = new ArrayList<>();
        imageBeanList = new ArrayList<>();
        activityId = getIntent().getStringExtra("activityId");
        //图片
        initPop();
        getProvinces();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        listImage.setLayoutManager(gridLayoutManager);
        imageAdapter = new ImageAdapter(mContext, listImageData);
        imageAdapter.setItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                PermissionUtils.newIntence().requestPerssion(CompositionSubmissionActivity.this, ToolUtil.PERSSION_PHOTO, 101, new PermissionUtils.IPermission() {
                    @Override
                    public void success(int code) {
                        if (listImageData.get(position).isAddImage()) {
                            spinnerPopWindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        }
                    }
                });
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
        EditUtils.showEditNoEmoji(compositionTitle);
        EditUtils.showEditNoEmoji(compositionContext);
        EditUtils.showEditNoEmoji(tvName);
        EditUtils.showEditNoEmoji(tvTeacher);
        EditUtils.showEditNoEmoji(tvMobile);
        EditUtils.showEditNoEmoji(tvAddress);
        tvMobile.setText(AccountUtil.getRelMobile(mContext));
    }


    @OnClick({R.id.btn_left_title, R.id.ll_fang, R.id.btn_submission, R.id.btn_province, R.id.btn_city, R.id.btn_area, R.id.btn_school, R.id.btn_grade, R.id.btn_unit})
    public void onViewClicked(View view) {
        KeyBoardUtil.KeyBoard(mContext, "");
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.ll_fang://反馈
               startActivity(HelpUpDataActivity.class);
                break;
            case R.id.btn_submission:
                if (!Verification(6)) {
                    return;
                }

                if (TextUtils.isEmpty(tvName.getText().toString().trim())) {
                    ToastUtils.showToastSHORT(mContext, "请输入姓名");
                    return;
                }
//                if (TextUtils.isEmpty(tvTeacher.getText().toString().trim())) {
//                    ToastUtils.showToastSHORT(mContext, "请输入辅导人");
//                    return;
//                }
                if (TextUtils.isEmpty(tvMobile.getText().toString().trim())) {
                    ToastUtils.showToastSHORT(mContext, "请输入手机号");
                    return;
                }
                if (!ToolUtil.isPhoneNumber(tvMobile.getText().toString().trim())) {
                    ToastUtils.showToastLONG(mContext, "请输入正确的手机号");
                    return;
                }
//                if (TextUtils.isEmpty(tvAddress.getText().toString().trim())) {
//                    ToastUtils.showToastSHORT(mContext, "请输入通讯地址");
//                    return;
//                }
                if (TextUtils.isEmpty(compositionTitle.getText().toString().trim())) {
                    ToastUtils.showToastSHORT(mContext, "请输入标题");
                    return;
                }
                if (compositionTitle.getText().toString().trim().length() > 40) {
                    ToastUtils.showToastSHORT(mContext, "标题请不要超过40个字");
                    return;
                }

                if (listImageData != null && listImageData.size() > 1) { //大于1 是因为还有一张默认图片
                    updateImages2Oss();
//                    submitComposition();
                } else if (!TextUtils.isEmpty(compositionContext.getText().toString().trim())) {
                    if (imageListForOss == null)
                        imageListForOss = new ArrayList<>();
                    uploadResource(imageListForOss);
                    return;
                } else {
                    ToastUtils.showToastSHORT(mContext, "请填写作文正文或上传图片");
                    return;
                }


                break;
            case R.id.btn_province:
                if (ProvincesList == null) return;
                spinnerPopWindowProvince.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                provinceImg.setImageResource(R.drawable.xuanze2);
                break;
            case R.id.btn_city:
                if (Verification(1)) {
                    if (CityList == null) return;
                    spinnerPopWindowCity.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    cityImg.setImageResource(R.drawable.xuanze2);
                }
                break;
            case R.id.btn_area:
                if (Verification(2)) {
                    if (AreaList == null) return;
                    spinnerPopWindowArea.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    areaImg.setImageResource(R.drawable.xuanze2);
                }
                break;
            case R.id.btn_school:
                if (Verification(3)) {
                    if (SchoolList == null) return;
                    spinnerPopWindowSchool.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    schoolImg.setImageResource(R.drawable.xuanze2);
                }
                break;
            case R.id.btn_grade:
                if (Verification(4)) {
                    spinnerPopWindowGrade.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    gradeImg.setImageResource(R.drawable.xuanze2);
                }
                break;
            case R.id.btn_unit:
                if (Verification(5)) {
                    if (UnitList == null) return;
                    spinnerPopWindowUnit.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    unitImg.setImageResource(R.drawable.xuanze2);
                }
                break;
        }
    }


    //作文
    public void submitComposition() {
        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("activityId", activityId);
        map.put("compositionTitle", compositionTitle.getText().toString());
        map.put("compositionContent", compositionContext.getText().toString());
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (ImageBean item : listImageData) {
            if (!item.isAddImage()) {
                parts.add(RetroFactory.prepareFilePart("image", new File(item.getTargetPicPath())));
            }
        }

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).submitComposition(map, parts))
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
        }
    }

    public void getOssToken(final int size, final ImageBean imageBean) {
        if (!showNetWaitLoding()) {
            return;
        }
        Map<String, String> header = new HashMap<>();
        header.put("token", AccountUtil.getToken(mContext) + "");
        //获取osstoken
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getOssTokenByDaYiNew(header))
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
                        host = ossTokenBean1.getData().getUploadUrl();
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
        if (!showNetWaitLoding()) {
            return;
        }
        int startIndex = imageBean.getTargetPicPath().lastIndexOf("/");
        String pathName = imageBean.getTargetPicPath().substring(startIndex + 1, imageBean.getTargetPicPath().length());
        String path = OSSConfig.ossnoteFolder;
        Log.d("111111uploadData", "onSuccess图片：" + path + pathName);

        new PutObjectSamples(OSSConfig.getInstance(getApplicationContext())
                , OSSConfig.testBucket, path + pathName, imageBean.getTargetPicPath())
                .asyncPutObjectFromLocalFileNew(new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {

                    @Override
                    public void onSuccess(final PutObjectRequest request, PutObjectResult result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageListForOss.add(host + "/" + request.getObjectKey());
                                Message message = new Message();
                                message.what = size;
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

    public void uploadResource(List<String> imageListForOss) {
        if (!showNetWaitLoding())
            return;
        String name = tvName.getText().toString().trim();
        String teacher = tvTeacher.getText().toString().trim();
        String address = tvAddress.getText().toString().trim();
        String Mobile = tvMobile.getText().toString().trim();
//        StringBuffer imageUrl = new StringBuffer();
//        for (int i = 0; i < imageListForOss.size(); i++) {
//            if (i == imageListForOss.size() - 1)
//                imageUrl.append(host + "/" + imageListForOss.get(i));
//            else
//                imageUrl.append(host + "/" + imageListForOss.get(i) + ",");
//        }
//        String str = new Gson().toJson(imageListForOss);
        Map<String, Object> json = new HashMap<>();
        json.put("province", addressProvince);
        json.put("city", addressCity);
        json.put("area", addressArea);
        json.put("school", school);
        json.put("grade", grade);
        json.put("compositionUnitId", unit);
        json.put("studentName", name);
        json.put("teacherName", teacher);
        json.put("telephone", Mobile);
        json.put("address", address);
        json.put("activityId", activityId);
        json.put("title", compositionTitle.getText().toString().trim());
        json.put("content", compositionContext.getText().toString().trim());
        json.put("imgList", imageListForOss);
        String str = new Gson().toJson(json);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), str);
        Logger.d("zzm", str);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).submitCompositionNew(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {

                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        Bean bean = ParseUtil.parseBean(response, Bean.class);
                        if (bean != null) {
                            ToastUtils.showToastLONG(mContext, bean.getMessage());
                        } else {
                            ToastUtils.showToastLONG(mContext, "投稿成功！");
                        }
                        onBackPressed();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }


    private void initPop() {
        spinnerPopWindowProvince = new SpinnerPopWindow(mContext);
        spinnerPopWindowProvince.setPopTitle("选择省份");
        spinnerPopWindowProvince.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                if (!addressProvince.equals(ProvincesList.get(position).getName())) {
                    addressProvince = ProvincesList.get(position).getName();
                    provinceText.setText(addressProvince);
                    getCityAndArea(ProvincesList.get(position).getId(), true);
                    addressCity = "";
                    cityText.setText("");
                    addressArea = "";
                    areaText.setText("");
                    school = "";
                    schoolText.setText("");
                }
            }
        });
        spinnerPopWindowProvince.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                provinceImg.setImageResource(R.drawable.xuanze1);
            }
        });
        spinnerPopWindowCity = new SpinnerPopWindow(mContext);
        spinnerPopWindowCity.setPopTitle("选择城市");
        spinnerPopWindowCity.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                if (!addressCity.equals(CityList.get(position).getName())) {
                    addressCity = CityList.get(position).getName();
                    cityText.setText(addressCity);
                    getCityAndArea(CityList.get(position).getId(), false);
                    addressArea = "";
                    areaText.setText("");
                    school = "";
                    schoolText.setText("");
                }
            }
        });
        spinnerPopWindowCity.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                cityImg.setImageResource(R.drawable.xuanze1);
            }
        });
        spinnerPopWindowArea = new SpinnerPopWindow(mContext);
        spinnerPopWindowArea.setPopTitle("选择区县");
        spinnerPopWindowArea.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                if (!addressArea.equals(AreaList.get(position).getName())) {
                    addressArea = AreaList.get(position).getName();
                    areaText.setText(addressArea);
                    getSchool(AreaList.get(position).getId());
                    school = "";
                    schoolText.setText("");
                }
            }
        });
        spinnerPopWindowArea.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                areaImg.setImageResource(R.drawable.xuanze1);
            }
        });
        spinnerPopWindowSchool = new SchoolPopWindow(mContext);
        spinnerPopWindowSchool.setPopTitle("选择学校");
        spinnerPopWindowSchool.setItemSelectListener(new SchoolPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                school = SchoolList.get(position).getName();
                schoolText.setText(school);
            }
        });
        spinnerPopWindowSchool.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                schoolImg.setImageResource(R.drawable.xuanze1);
            }
        });
        GradeList = ToolUtil.StringToArray(Grade, ",");
        spinnerPopWindowGrade = new SpinnerPopWindow(mContext);
        spinnerPopWindowGrade.setPopTitle("选择年级");
        spinnerPopWindowGrade.refreshData(GradeList);
        spinnerPopWindowGrade.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                grade = GradeList.get(position);
                gradeText.setText(grade);
                getUnit(grade);
                unit = "";
                unitText.setText("");
            }
        });
        spinnerPopWindowGrade.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                gradeImg.setImageResource(R.drawable.xuanze1);
            }
        });
        spinnerPopWindowUnit = new SpinnerPopWindow(mContext);
        spinnerPopWindowUnit.setPopTitle("选择单元");
        spinnerPopWindowUnit.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                unit = UnitList.get(position).getId();
                unitText.setText(UnitList.get(position).getName());
            }
        });
        spinnerPopWindowUnit.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                unitImg.setImageResource(R.drawable.xuanze1);
            }
        });
    }

    /**
     * 获取省列表
     */

    List<NameAndIdArrayBean.DataBean> ProvincesList;
    List<NameAndIdArrayBean.DataBean> CityList;
    List<NameAndIdArrayBean.DataBean> AreaList;
    List<NameAndIdArrayBean.DataBean> SchoolList;
    List<NameAndIdArrayBean.DataBean> UnitList;
    List<String> GradeList;

    private void getProvinces() {
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getProvinces(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        NameAndIdArrayBean pro = ParseUtil.parseBean(response, NameAndIdArrayBean.class);
                        ProvincesList = pro.getData();
                        List<String> str = new ArrayList<>();
                        for (NameAndIdArrayBean.DataBean bean : ProvincesList) {
                            str.add(bean.getName());
                        }
                        spinnerPopWindowProvince.refreshData(str);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    private void getCityAndArea(String Id, final boolean isCity) {
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("id", Id);
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getCityOrArea(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        NameAndIdArrayBean city = ParseUtil.parseBean(response, NameAndIdArrayBean.class);
                        if (isCity) {
                            CityList = city.getData();
                            List<String> str = new ArrayList<>();
                            for (NameAndIdArrayBean.DataBean bean : CityList) {
                                str.add(bean.getName());
                            }
                            spinnerPopWindowCity.refreshData(str);
                        } else {
                            AreaList = city.getData();
                            List<String> str = new ArrayList<>();
                            for (NameAndIdArrayBean.DataBean bean : AreaList) {
                                str.add(bean.getName());
                            }
                            spinnerPopWindowArea.refreshData(str);
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    private void getSchool(String id) {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("name", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getSchools(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        NameAndIdArrayBean pro = ParseUtil.parseBean(response, NameAndIdArrayBean.class);
                        SchoolList = pro.getData();
//                        List<String> str = new ArrayList<>();
//                        for (NameAndIdArrayBean.DataBean bean : SchoolList) {
//                            str.add(bean.getName());
//                        }
                        spinnerPopWindowSchool.refreshData(SchoolList);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    private void getUnit(String name) {
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("grade", name);
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getCompositionUnits(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        NameAndIdArrayBean pro = ParseUtil.parseBean(response, NameAndIdArrayBean.class);
                        UnitList = pro.getData();
                        List<String> str = new ArrayList<>();
                        for (NameAndIdArrayBean.DataBean bean : UnitList) {
                            str.add(bean.getName());
                        }
                        spinnerPopWindowUnit.refreshData(str);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }


    private boolean Verification(int type) {
        if (TextUtils.isEmpty(addressProvince)) {
            ToastUtils.showToastSHORT(mContext, "请选择省份");
            return false;
        } else if (TextUtils.isEmpty(addressCity) && type > 1) {
            ToastUtils.showToastSHORT(mContext, "请选择城市");
            return false;
        } else if (TextUtils.isEmpty(addressArea) && type > 2) {
            ToastUtils.showToastSHORT(mContext, "请选择区县");
            return false;
        } else if (TextUtils.isEmpty(school) && type > 3) {
            ToastUtils.showToastSHORT(mContext, "请选择学校");
            return false;
        } else if (TextUtils.isEmpty(grade) && type > 4) {
            ToastUtils.showToastSHORT(mContext, "请选择年级");
            return false;
        } else if (TextUtils.isEmpty(unit) && type > 5) {
            ToastUtils.showToastSHORT(mContext, "请选择单元");
            return false;
        }
        return true;
    }
}
