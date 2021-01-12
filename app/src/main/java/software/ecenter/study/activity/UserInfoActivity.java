package software.ecenter.study.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bumptech.glide.Glide;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;
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
import okhttp3.RequestBody;
import software.ecenter.study.Interface.ConstantData;
import software.ecenter.study.OSSSImple.GetObjectSamples;
import software.ecenter.study.OSSSImple.OSSConfig;
import software.ecenter.study.OSSSImple.PutObjectSamples;
import software.ecenter.study.R;
import software.ecenter.study.View.CircleImageView;
import software.ecenter.study.View.LoadingDialog;
import software.ecenter.study.View.MyDialog;
import software.ecenter.study.View.SchoolPopWindow;
import software.ecenter.study.View.SpinnerPopWindow;
import software.ecenter.study.bean.BaseBean;
import software.ecenter.study.bean.Bean;
import software.ecenter.study.bean.CityBean;
import software.ecenter.study.bean.MineBean.PersonDetailBean;
import software.ecenter.study.bean.NameAndIdArrayBean;
import software.ecenter.study.bean.OssTokenBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.tool.ImageTool;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.ConstantValue;
import software.ecenter.study.utils.MyGlideUrl;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.PermissionUtils;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * dec 个人信息
 */
public class UserInfoActivity extends BaseActivity implements ConstantData {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.info_head_img)
    CircleImageView infoHeadImg;
    @BindView(R.id.info_head_tip)
    ImageView infoHeadTip;
    @BindView(R.id.btn_info_head)
    RelativeLayout btnInfoHead;
    @BindView(R.id.info_nickname_text)
    TextView infoNicknameText;
    @BindView(R.id.info_nickname_tip)
    ImageView infoNicknameTip;
    @BindView(R.id.btn_info_nickname)
    RelativeLayout btnInfoNickname;
    @BindView(R.id.info_grade_text)
    TextView infoGradeText;
    @BindView(R.id.info_grade_tip)
    ImageView infoGradeTip;
    @BindView(R.id.btn_info_grade)
    RelativeLayout btnInfoGrade;
    @BindView(R.id.info_address_text)
    TextView infoAddressText;
    @BindView(R.id.info_address_tip)
    ImageView infoAddressTip;
    @BindView(R.id.btn_info_address)
    RelativeLayout btnInfoAddress;
    @BindView(R.id.info_shenfen_text)
    TextView infoShenfenText;
    @BindView(R.id.info_shenfen_tip)
    ImageView infoShenfenTip;
    @BindView(R.id.btn_info_shenfen)
    RelativeLayout btnInfoShenfen;
    @BindView(R.id.info_school_text)
    TextView infoSchoolText;
    @BindView(R.id.info_school_tip)
    ImageView infoSchoolTip;
    @BindView(R.id.btn_info_school)
    RelativeLayout btnInfoSchool;
    @BindView(R.id.info_phone_text)
    TextView infoPhoneText;
    @BindView(R.id.info_phone_tip)
    ImageView infoPhoneTip;
    @BindView(R.id.btn_info_phone)
    RelativeLayout btnInfoPhone;
    @BindView(R.id.info_password_tip)
    ImageView infoPasswordTip;
    @BindView(R.id.btn_info_password)
    RelativeLayout btnInfoPassword;
    @BindView(R.id.info_weixin_text)
    TextView infoWeixinText;
    @BindView(R.id.info_weixin_tip)
    ImageView infoWeixinTip;
    @BindView(R.id.btn_info_weixin)
    RelativeLayout btnInfoWeixin;
    @BindView(R.id.info_QQ_text)
    TextView infoQQText;
    @BindView(R.id.info_QQ_tip)
    ImageView infoQQTip;
    @BindView(R.id.btn_info_QQ)
    RelativeLayout btnInfoQQ;
    @BindView(R.id.btn_info_aq)
    RelativeLayout btnInfoAQ;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.rootView)
    LinearLayout rootView;

    private PersonDetailBean mPersonDetailBean;

    //年级
    private SpinnerPopWindow spinnerPopWindowNJ;
    private List<String> spinnerListNJ;
    private String mGrade;

    //地区
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
    private String school = "";
    private String schoolId = "";

    //身份
    private SpinnerPopWindow spinnerPopWindowSF;
    private List<String> spinnerListSF;
    private String mRole;

    //头像
    public SpinnerPopWindow spinnerPopWindow;
    private List<String> spinnerList;
    //相机
    public final int REQUEST_PIC_CARMER = 1;
    //相册
    public final int REQUEST_PHOTO_PHOTO = 2;
    //图片处理完毕
    public final int MSG_IAMGE_FINISHED = 21;
    public LoadingDialog mWaitDialog;
    protected String myTempFilePath;//随机图片名称

    private final int requestNameCode = 10;
    private final int requestSchoolCode = 11;

    @SuppressLint("HandlerLeak")
    protected Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_IAMGE_FINISHED:
                    thumImageOK(msg.arg1, String.valueOf(msg.obj));
                    break;
                default:
                    break;
            }
        }

        ;
    };
    private int mModifyType;
    private Bitmap mCurThumBitmap;

    private IWXAPI api; // IWXAPI 是第三方app和微信通信的openapi接口
    //QQ登录
    private Tencent mTencent;
    private UserInfo mInfo;
    private String name, figureurl, openid;
    private int loginType = 2;
    private String ty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        mWaitDialog = new LoadingDialog(mContext);
        mWaitDialog.setCancelable(false);
        requestWx();
        requestQq();

        // 地区
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
                        updateUserInfo(3, "", "", "", "");
                        schoolId = "";
                        spinnerPopCity.dismiss();
                        break;

                }
            }
        });
        //学校
        spinnerPopWindowSchool = new SchoolPopWindow(mContext);
        spinnerPopWindowSchool.setPopTitle("选择学校");
        spinnerPopWindowSchool.setItemSelectListener(new SchoolPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                schoolId = SchoolList.get(position).getId();
                school = SchoolList.get(position).getName();
                updateUserInfo(5, "", "", "", "");
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo(false);
    }

    private void requestQq() {
        // 实例化Tencent
        if (mTencent == null) {
            mTencent = Tencent.createInstance(ConstantValue.APP_ID_QQ, getApplicationContext());
        }
    }

    private void requestWx() {
        api = WXAPIFactory.createWXAPI(mContext, ConstantValue.APP_ID);
        api.registerApp(ConstantValue.APP_ID);
    }

    public void initView() {
        if (mPersonDetailBean == null) {
            return;
        }
        if (!TextUtils.isEmpty(mPersonDetailBean.getData().getHeadImage())) {
            Glide.with(mContext).load(new MyGlideUrl(mPersonDetailBean.getData().getHeadImage() + "")).error(R.drawable.morentx).into(infoHeadImg);//用户头像
        } else {
            infoHeadImg.setImageResource(R.drawable.morentx);
        }
        infoNicknameText.setText(mPersonDetailBean.getData().getNickname());
        infoGradeText.setText(mPersonDetailBean.getData().getGrade());
        infoAddressText.setText(mPersonDetailBean.getData().getAddressProvince() + mPersonDetailBean.getData().getAddressCity() + mPersonDetailBean.getData().getAddressArea());
        if (mPersonDetailBean.getData().getRole() == 1) {
            infoShenfenText.setText("学生");
        } else {
            infoShenfenText.setText("教师");
        }
        getProvinces();
        if (!TextUtils.isEmpty(mPersonDetailBean.getData().getAreaId())) {
            getSchool(mPersonDetailBean.getData().getAreaId());
        }
        if (TextUtils.isEmpty(mPersonDetailBean.getData().getSchool())) {
            infoSchoolText.setText("未设置");
            infoSchoolText.setTextColor(getResources().getColor(R.color.textColor));
        } else {
            infoSchoolText.setText(mPersonDetailBean.getData().getSchool());
            infoSchoolText.setTextColor(getResources().getColor(R.color.textHoldColor));
        }

        infoPhoneText.setText(mPersonDetailBean.getData().getPhoneNumber());

        if (mPersonDetailBean.getData().getIsBindWechat() == 1) {
            infoWeixinText.setText("解除绑定");
            infoWeixinText.setTextColor(getResources().getColor(R.color.textHoldColor));
            btnInfoWeixin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int typewx = 1;
                    showtoBindDialog(typewx);

                }
            });
        } else {
            infoWeixinText.setText("未绑定");
            infoWeixinText.setTextColor(getResources().getColor(R.color.textColor));
            btnInfoWeixin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showtoBindDialogs(1);

                }
            });
        }

        if (mPersonDetailBean.getData().getIsBindQQ() == 1) {
            infoQQText.setText("解除绑定");
            infoQQText.setTextColor(getResources().getColor(R.color.textHoldColor));
            btnInfoQQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int typeqq = 2;
                    showtoBindDialog(typeqq);
                }
            });
        } else {
            infoQQText.setText("未绑定");
            infoQQText.setTextColor(getResources().getColor(R.color.textColor));
            btnInfoQQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showtoBindDialogs(2);

                }
            });
        }

        // 年级
        spinnerListNJ = ToolUtil.StringToArray(registAndUserinfoGrade, ",");
        spinnerPopWindowNJ = new SpinnerPopWindow(mContext);
        spinnerPopWindowNJ.refreshData(spinnerListNJ);
        spinnerPopWindowNJ.setPopTitle("选择年级");
        spinnerPopWindowNJ.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                mGrade = spinnerListNJ.get(position);
                updateUserInfo(2, "", mGrade, "", "");
                spinnerPopWindowNJ.dismiss();

            }
        });

        // 身份
        spinnerListSF = ToolUtil.StringToArray(ShenFen, ",");
        spinnerPopWindowSF = new SpinnerPopWindow(mContext);
        spinnerPopWindowSF.refreshData(spinnerListSF);
        spinnerPopWindowSF.setPopTitle("选择身份");
        spinnerPopWindowSF.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                String role = "";
                mRole = spinnerListSF.get(position);
                if ("老师".equals(mRole)) {
                    role = "2";
                } else {
                    role = "1";
                }

                updateUserInfo(4, "", "", "", role);
                spinnerPopWindowSF.dismiss();
            }
        });

        // 照片
        spinnerList = ToolUtil.StringToArray(PhonePhoto, ",");
        spinnerPopWindow = new SpinnerPopWindow(mContext);
        spinnerPopWindow.refreshData(spinnerList);
        spinnerPopWindow.setPopTitle("照片");
        spinnerPopWindow.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0: //相机
                        startCamera();
                        break;

                    case 1: //相册
                        startPhoto();
                        break;
                }
            }
        });
    }

    private class BaseUiListener implements IUiListener {
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
            //Toast.makeText(getApplicationContext(), "绑定成功", Toast.LENGTH_SHORT).show();
            try {
                openid = ((JSONObject) response).getString("openid");
                mTencent.setOpenId(openid);
                mTencent.setAccessToken(((JSONObject) response).getString("access_token"),
                        ((JSONObject) response).getString("expires_in"));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            QQToken qqToken = mTencent.getQQToken();
            UserInfo info = new UserInfo(getApplicationContext(), qqToken);
            info.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    //用户信息获取到了
                    try {
                        openid = ((JSONObject) o).getString("openid");
                        name = ((JSONObject) o).getString("nickname");
                        figureurl = ((JSONObject) o).getString("figureurl_qq_2");
                        loginType = 2;

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(UiError uiError) {
                    Log.v("UserInfo", "onError");
                }

                @Override
                public void onCancel() {
                    Log.v("UserInfo", "onCancel");
                }
            });
            qq(openid, name, figureurl, loginType);
        }

        @Override
        public void onError(UiError uiError) {
            //登录失败
        }

        @Override
        public void onCancel() {
            //取消登录
        }
    }

    //绑定
    private void qq(String thirdPartyId, String nickname, String headImage, int type) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("thirdPartyId", thirdPartyId);
            json.put("nickname", nickname);
            json.put("headImage", headImage);
            json.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).bindThirdAccount(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        Bean netBaseBean = new Bean();
                        netBaseBean = ParseUtil.parseBean(response, Bean.class);
                        if (netBaseBean != null && netBaseBean.getStatus() == 1) {
                            ToastUtils.showToastSHORT(mContext, netBaseBean.getMessage());
                            getUserInfo(true);
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    //相机
    protected void startCamera() {
        if (spinnerPopWindow.isShowing()) {
            spinnerPopWindow.dismiss();
        }
        // 指定相机拍摄照片保存地址
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent();
            // 指定开启系统相机的Action
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            if (!outDir.exists()) {
                outDir.mkdirs();
            }

            //如果文件目录存在，那么就指定拍照路径
            File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
            myTempFilePath = outFile.getAbsolutePath();

            // 把文件地址转换成Uri格式
            Uri uri = ToolUtil.parUri(this, outFile);

            // 设置系统相机拍摄照片完成后图片文件的存放地址
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            // 此值在最低质量最小文件尺寸时是0，在最高质量最大文件尺寸时是１
            //intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            //Log.d(TAG, "指定了路径:" + myPicFilePath);
            startActivityForResult(intent, REQUEST_PIC_CARMER);
        } else {
            ToastUtils.showToastSHORT(mContext, "请确认已经插入SD卡");
        }
    }

    //相册选择
    protected void startPhoto() {
        if (spinnerPopWindow.isShowing()) {
            spinnerPopWindow.dismiss();
        }
        Intent openphotoIntent = new Intent(Intent.ACTION_PICK);
        openphotoIntent.setType("image/*");
        startActivityForResult(openphotoIntent, REQUEST_PHOTO_PHOTO);
    }

    public void thumImageOK(int requestCode, String targetPicPath) {
        mCurThumBitmap = null; //缩略图（用来展示图片）
        //拍照
        if (requestCode == REQUEST_PIC_CARMER) {
            if (targetPicPath != null) {
                mCurThumBitmap = ImageTool.decodeBitmap(targetPicPath);
            }
            if (mCurThumBitmap == null) {
                dismissWaitLoging();
                ToastUtils.showToastSHORT(mContext, "照片获取失败");
                return;
            }
            /**TODO 更换oss之前使用的方法上传头像
             updateUserInfo(6, "", "", "", "", "", "", targetPicPath);*/
//            dismissWaitLoging();
            //获取ossTOken
            updateUserHeade(targetPicPath);
            //相册
        } else if (requestCode == REQUEST_PHOTO_PHOTO) {
            if (targetPicPath != null) {
                mCurThumBitmap = ImageTool.decodeBitmap(targetPicPath);
            }
            if (mCurThumBitmap == null) {
                dismissWaitLoging();
                ToastUtils.showToastSHORT(mContext, "照片获取失败");
                return;
            }

            //上传头像
//            updateUserInfo(6, "", "", "", "", "", "", targetPicPath);
//            updateUserInfo(6,"","","","","","",targetPicPath);
            updateUserHeade(targetPicPath);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 11101) {
            Tencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener());
            Tencent.handleResultData(data, new BaseUiListener());
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == requestNameCode) {
                infoNicknameText.setText(data.getStringExtra("result"));
            } else if (requestCode == requestSchoolCode) {
                infoSchoolText.setText(data.getStringExtra("result"));
            } else {
                //指定路径拍照的话，返回 intent 为null
                //开启线程处理 图片结果，防止应用无响应
                showWaitLoding();
                //ToastUtils.showToastSHORT(mContext,"正在处理图片");
                MyThumRunnable run = new MyThumRunnable(requestCode, data);
                new Thread(run).start();
            }
        }

    }

    /**
     * 用来生成压缩图片的耗时操作
     */
    class MyThumRunnable implements Runnable {
        private int requestCode;
        private Intent data;

        public MyThumRunnable(int requestCode, Intent data) {
            this.requestCode = requestCode;
            this.data = data;
        }

        @Override
        public void run() {

            String TargetPicPath = null;
            Message msg = mHandler.obtainMessage();

            //拍照
            if (requestCode == REQUEST_PIC_CARMER) {
                TargetPicPath = ImageTool.getThumPicPath(mContext, myTempFilePath, null);

                //相册
            } else if (requestCode == REQUEST_PHOTO_PHOTO) {
                try {
                    if (data != null) {
                        Uri uri = data.getData();
                        String absolutePath = getAbsolutePath(mContext, uri);

                        TargetPicPath = ImageTool.getThumPicPath(mContext, absolutePath, myTempFilePath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            msg.what = MSG_IAMGE_FINISHED;
            msg.arg1 = requestCode;
            msg.obj = TargetPicPath;
            mHandler.sendMessage(msg);

        }
    }

    public String getAbsolutePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


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

    public void getUserInfo(boolean isShow) {
        if (isShow) {
            if (!showNetWaitLoding()) {
                return;
            }
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getUserInfo(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.GONE);
                        mPersonDetailBean = ParseUtil.parseBean(response, PersonDetailBean.class);
                        AccountUtil.saveUserInfo(mContext, mPersonDetailBean);
                        initView();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });

    }

    //入参类型（ 1、昵称 2、年级 3、地区 4、身份 5、学校 6、头像 ） 注：（后台根据modifyType 取对应的字段，以下是否可空也与此相关）
    public void updateUserInfo(final int modifyType, String nickname, String grade, String role, String headImage) {
        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        JSONObject json = new JSONObject();
        try {
            json.put("modifyType", "" + modifyType);
            switch (modifyType) {
                case 1:
                    json.put("nickname", nickname);
                    break;
                case 2:
                    json.put("grade", grade);
                    break;
                case 3:
                    json.put("provinceId", provinceId);
                    json.put("cityId", cityId);
                    json.put("areaId", areaId);
                    break;
                case 4:
                    json.put("role", role);
                    break;
                case 5:
                    json.put("schoolId", schoolId);
                    break;
                case 6:
                    json.put("headImageUrl", headImage);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mModifyType = modifyType;
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).updateUserInfo(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissWaitLoging();
                                dismissNetWaitLoging();

                                ToastUtils.showToastLONG(mContext, "修改成功");
                                switch (mModifyType) {
                                    case 1:
                                        break;
                                    case 2:
                                        infoGradeText.setText(mGrade);
                                        AccountUtil.saveGrade(mContext, mGrade);
                                        getUserInfo(true);
                                        break;
                                    case 3:
                                        infoAddressText.setText(addressProvince + addressCity + addressArea);
                                        infoSchoolText.setText("");
                                        break;
                                    case 4:
                                        infoShenfenText.setText(mRole);
                                        break;
                                    case 5:
                                        infoSchoolText.setText(school);
                                        break;
                                    case 6:
//                                        if (mCurThumBitmap != null) {
////                                            infoHeadImg.setImageBitmap(mCurThumBitmap);
////                                        }
                                        if (!TextUtils.isEmpty(myTempFilePath)) {
                                            Glide.with(mContext).load(myTempFilePath).error(R.drawable.morentx).into(infoHeadImg);//用户头像
                                        } else {
                                            infoHeadImg.setImageResource(R.drawable.morentx);
                                        }
                                        break;
                                }
                            }
                        });

                    }

                    @Override
                    public void onFail(int type, final String msg) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissWaitLoging();
                                ToastUtils.showToastLONG(mContext, msg);
                            }
                        });
                    }
                });
    }

    /**
     * 获取osstoken鉴权、上传文件到oss、回传上传路径给服务端
     *
     * @param localHeadUrl 本地图片路径
     */
    public void updateUserHeade(final String localHeadUrl) {
        Map<String, String> map = new HashMap<>();
        Map<String, String> header = new HashMap<>();
        header.put("token", AccountUtil.getToken(mContext) + "");
        showWaitLoding();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getOssToken(header))
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
                        int startIndex = localHeadUrl.lastIndexOf("/");
                        String pathName = localHeadUrl.substring(startIndex + 1, localHeadUrl.length());

                        uploadData("1", pathName, localHeadUrl);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(mContext, msg);
                        dismissWaitLoging();
                    }
                });
    }

    @OnClick({R.id.btn_left_title, R.id.btn_info_head, R.id.btn_info_aq, R.id.btn_info_nickname, R.id.btn_refresh_net, R.id.btn_info_grade, R.id.btn_info_address, R.id.btn_info_shenfen, R.id.btn_info_school, R.id.btn_info_phone, R.id.btn_info_password, R.id.btn_info_weixin, R.id.btn_info_QQ})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_info_head:
                PermissionUtils.newIntence().requestPerssion(this, ToolUtil.PERSSION_PHOTO, 101, new PermissionUtils.IPermission() {
                    @Override
                    public void success(int code) {
                        spinnerPopWindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    }
                });

                break;
            case R.id.btn_refresh_net:
                getUserInfo(true);
                break;
            case R.id.btn_info_nickname:
                Intent intent = new Intent(mContext, UserInfoChangeActivity.class);
                intent.putExtra("nickName", mPersonDetailBean.getData().getNickname());
                intent.putExtra("mModifyType", 1);
                startActivityForResult(intent, requestNameCode);
                break;
            case R.id.btn_info_grade:
                spinnerPopWindowNJ.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.btn_info_address://选择地区
                if (spinnerPopCity.getLevel() != -1) {
                    spinnerPopCity.clearData();
                    spinnerPopCity.refreshData(spinnerProvincesName);
                    spinnerPopCity.setPopTitle("选择地区");
                    spinnerPopCity.toTagMode("请选择省");
                    spinnerPopCity.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                } else {
                    spinnerPopCity.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                break;
            case R.id.btn_info_shenfen:
                //spinnerPopWindowSF.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.btn_info_school://选择学校
                if (SchoolList == null) return;
                spinnerPopWindowSchool.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.btn_info_phone:
                //donothing
                break;
            case R.id.btn_info_password:
                Intent intentPassword = new Intent(mContext, ForgetActivity.class);
                intentPassword.putExtra("reset_password", 1);
                intentPassword.putExtra("phone", mPersonDetailBean.getData().getRealPhone());
                startActivity(intentPassword);
                break;
            case R.id.btn_info_weixin:
                break;
            case R.id.btn_info_QQ:
                break;
            case R.id.btn_info_aq://账户安全
                startActivity(new Intent(mContext,SafeAccountActivity.class));
                break;

        }
    }

    /**
     * oss上传
     *
     * @param type      上传文件类型
     * @param path      上传路径
     * @param localpath 文件本地路径
     */
    public void uploadData(final String type, final String path, final String localpath) {
        final String p = OSSConfig.ossnoteFolder + path;

        Log.d("111111uploadData", "onSuccess图片：" + OSSConfig.OSSPATH + p);
        File file = new File(localpath);

        new PutObjectSamples(OSSConfig.getInstance(getApplicationContext())
                , OSSConfig.testBucket, OSSConfig.ossnoteFolder + path, localpath)
                .asyncPutObjectFromLocalFile(new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("111111uploadData", "onSuccess图片：" + OSSConfig.OSSPATH + p);
                                switch (type) {
                                    case "1":
                                        updateUserInfo(6, "", "", "", p + "");
//                                break;
                                    case "2":
                                        break;
                                }
                                Log.d("111111uploadData", "onSuccess：" + "上传图片成功");

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
                                dismissWaitLoging();
                            }
                        });
                    }
                });

    }

    private void unBind(int type) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).unbindThirdAccount(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        Bean baseBean = new Bean();
                        baseBean = ParseUtil.parseBean(response, Bean.class);
                        if (baseBean != null && baseBean.getStatus() == 1) {
                            ToastUtils.showToastSHORT(mContext, baseBean.getMessage());
                            getUserInfo(true);
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastSHORT(mContext, msg);
                    }

                });
    }

    public void showtoBindDialog(final int type) {

        MyDialog.Builder builder = new MyDialog.Builder(mContext);
        builder.setTitle("解除绑定");
        switch (type) {
            case 2:
                builder.setMessage("是否要解除QQ绑定");
                break;
            case 1:
                builder.setMessage("是否要解除微信绑定");
                break;
        }

        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
            }
        });

        builder.setNegativeButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        unBind(type);
                        dialog.dismiss();
                    }
                });

        builder.create().show();

    }

    /**
     * 提示用户是否绑定
     *
     * @param type
     */
    public void showtoBindDialogs(final int type) {

        MyDialog.Builder builder = new MyDialog.Builder(mContext);
        builder.setTitle("提示");
        switch (type) {
            case 2:
                builder.setMessage("是否进行QQ绑定");
                break;
            case 1:
                builder.setMessage("是否进行微信绑定");
                break;
        }

        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
            }
        });

        builder.setNegativeButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (type == 2) {
                            if (!mTencent.isSessionValid()) {
                                mTencent.login((Activity) mContext, "all", new BaseUiListener());
                            } else {
                                mTencent.logout(mContext);
                                mTencent.login((Activity) mContext, "all", new BaseUiListener());
                            }
                        }
                        if (type == 1) {
                            EventBus.getDefault().postSticky("wechatbind");
                            //登录微信，绑定微信
                            SendAuth.Req req = new SendAuth.Req();
                            req.scope = "snsapi_userinfo";
                            req.state = "wechat_sdk_demo_test";
                            api.sendReq(req);
                        }

                        dialog.dismiss();
                    }
                });

        builder.create().show();

    }
}
