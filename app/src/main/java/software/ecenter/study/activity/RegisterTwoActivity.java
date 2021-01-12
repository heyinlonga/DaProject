package software.ecenter.study.activity;

import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.Interface.ConstantData;
import software.ecenter.study.R;
import software.ecenter.study.View.SchoolPopWindow;
import software.ecenter.study.View.SpinnerPopWindow;
import software.ecenter.study.bean.CityBean;
import software.ecenter.study.bean.HomeBean.HomeBean;
import software.ecenter.study.bean.MineBean.PersonDetailBean;
import software.ecenter.study.bean.NameAndIdArrayBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.DESUtils;
import software.ecenter.study.utils.EditUtils;
import software.ecenter.study.utils.JpushUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/***
 * dec 注册2
 *
 * */
public class RegisterTwoActivity extends BaseActivity implements ConstantData {


    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.set_school_text)
    TextView setSchoolText;
    @BindView(R.id.set_school_img)
    ImageView setSchoolImg;
    @BindView(R.id.btn_set_school)
    LinearLayout btnSetSchool;
    @BindView(R.id.set_nickName_text)
    EditText setNickNameText;
    @BindView(R.id.set_address_text)
    TextView setAddressText;
    @BindView(R.id.set_address_img)
    ImageView setAddressImg;
    @BindView(R.id.btn_set_address)
    LinearLayout btnSetAddress;
    @BindView(R.id.set_grade_text)
    TextView setGradeText;
    @BindView(R.id.set_grade_img)
    ImageView setGradeImg;
    @BindView(R.id.btn_set_grade)
    LinearLayout btnSetGrade;
    @BindView(R.id.set_shenfen_text)
    TextView setShenfenText;
    @BindView(R.id.set_shenfen_img)
    ImageView setShenfenImg;
    @BindView(R.id.btn_set_shenfen)
    LinearLayout btnSetShenfen;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.btn_xieyi)
    TextView btnXieyi;
    @BindView(R.id.rootView)
    LinearLayout rootView;



    private SpinnerPopWindow spinnerPopWindowNJ;
    private List<String> spinnerListNJ;

    private SpinnerPopWindow spinnerPopWindowSF;
    private List<String> spinnerListSF;

    private String phone;
    private String code;
    private String password;
    private String addressProvince = "";
    private String provinceId = "";
    private String addressCity = "";
    private String cityId = "";
    private String addressArea = "";
    private String areaId = "";
    private String school = "";
    private String schoolId = "";
    private String grade;
    private int role;

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

    private List<NameAndIdArrayBean.DataBean> SchoolList;
    private SchoolPopWindow spinnerPopWindowSchool;    //学校

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);
        ButterKnife.bind(this);
        EditUtils.showEditNoEmoji(setNickNameText);
        phone = getIntent().getStringExtra("phone");
        code = getIntent().getStringExtra("code");
        password = getIntent().getStringExtra("password");
        //学校
        spinnerPopWindowSchool = new SchoolPopWindow(mContext);
        spinnerPopWindowSchool.setPopTitle("选择学校");
        spinnerPopWindowSchool.setItemSelectListener(new SchoolPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                schoolId = SchoolList.get(position).getId();
                school = SchoolList.get(position).getName();
                setSchoolText.setText(school);
            }
        });
        spinnerPopWindowSchool.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setSchoolImg.setImageResource(R.drawable.zhangkai);
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
                        getCityAndArea(provinceId,true);
                        break;
                    case 1:
                        spinnerPopCity.TagTwoOk(spinnerCityName.get(position), "请选择区");
                        addressCity = CityList.get(position).getName();
                        cityId = CityList.get(position).getId();
                        getCityAndArea(cityId,false);
                        break;
                    case 2:
                        addressArea = AreaList.get(position).getName();
                        areaId = AreaList.get(position).getId();
                        getSchool(areaId);
                        setAddressText.setText(addressProvince+addressCity+addressArea);
                        schoolId = "";
                        setSchoolText.setText("");
                        spinnerPopCity.dismiss();
                        break;

                }
            }
        });
        spinnerPopCity.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAddressImg.setImageResource(R.drawable.zhangkai);
            }
        });


        // 年级
        spinnerListNJ = ToolUtil.StringToArray(registAndUserinfoGrade, ",");
        spinnerPopWindowNJ = new SpinnerPopWindow(mContext);
        spinnerPopWindowNJ.refreshData(spinnerListNJ);
        spinnerPopWindowNJ.setPopTitle("选择年级");
        spinnerPopWindowNJ.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                grade = spinnerListNJ.get(position);
                setGradeText.setText(grade);

            }
        });
        spinnerPopWindowNJ.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setGradeImg.setImageResource(R.drawable.zhangkai);
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
                if ("学生".equals(spinnerListSF.get(position))) {
                    setShenfenText.setText("学生");
                    role = 1;
                } else {
                    setShenfenText.setText("老师");
                    role = 2;
                }
            }
        });
        spinnerPopWindowSF.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setShenfenImg.setImageResource(R.drawable.zhangkai);
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



    @OnClick({R.id.btn_left_title, R.id.btn_set_address, R.id.btn_set_grade, R.id.btn_set_shenfen, R.id.btn_register, R.id.btn_xieyi, R.id.btn_set_school})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_set_address:
//                if (ProvincesList!=null && ProvincesList.size()>0){
//                    spinnerPopWindowProvince.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//                    setAddressImg.setImageResource(R.drawable.shouqi);
//                }else {
//                    getProvinces();
//                }
                if (spinnerPopCity.getLevel() != -1) {
                    spinnerPopCity.clearData();
                    spinnerPopCity.refreshData(spinnerProvincesName);
                    spinnerPopCity.setPopTitle("选择地区");
                    spinnerPopCity.toTagMode("请选择省");
                    spinnerPopCity.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    setAddressImg.setImageResource(R.drawable.shouqi);
                } else {
                    spinnerPopCity.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    setAddressImg.setImageResource(R.drawable.shouqi);
                }

                break;
            case R.id.btn_set_grade:
                spinnerPopWindowNJ.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                setGradeImg.setImageResource(R.drawable.shouqi);
                break;
            case R.id.btn_set_shenfen:
                spinnerPopWindowSF.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                setShenfenImg.setImageResource(R.drawable.shouqi);
                break;
            case R.id.btn_set_school://学校
                if (SchoolList == null) return;
                spinnerPopWindowSchool.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                setSchoolImg.setImageResource(R.drawable.shouqi);
                break;
            case R.id.btn_register:
                register();
                break;
            case R.id.btn_xieyi:
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("url", "http://xzykt.longmenshuju.com/userAgreement");
                intent.putExtra("fuwuxieyi", "用户协议");
                startActivity(intent);
                break;
        }
    }

    //注册
    public void register() {
        if (TextUtils.isEmpty(addressProvince) || TextUtils.isEmpty(addressCity) || TextUtils.isEmpty(addressArea)) {
            ToastUtils.showToastLONG(mContext, "请选择地区");
            return;
        }

        if (TextUtils.isEmpty(grade)) {
            ToastUtils.showToastLONG(mContext, "请选择年级");
            return;
        }
        if (role == 0) {
            ToastUtils.showToastLONG(mContext, "请选择身份");
            return;
        }
        if (TextUtils.isEmpty(setNickNameText.getText().toString())) {
            ToastUtils.showToastLONG(mContext, "请输入昵称");
            return;
        }
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            //新添加的加密功能
            json.put("phoneNumber", DESUtils.encrypt(phone));   //phone
            json.put("checkCode", code);
            json.put("password", DESUtils.encrypt(password));     //password
            json.put("provinceId", provinceId);
            json.put("cityId", cityId);
            json.put("areaId", areaId);
            json.put("schoolId", schoolId);
            json.put("nickName", setNickNameText.getText().toString());
            json.put("grade", grade);
            json.put("role", role);
            json.put("registrationId", JPushInterface.getRegistrationID(mContext));
            json.put("nickname", setNickNameText.getText().toString());
            Log.d(TAG, "注册传递数据：" + json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext, false).register(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
//                        AccountUtil.saveGrade(mContext, grade);
//                        ToastUtils.showToastSHORT(mContext, "注册成功");
//                        Intent intent = new Intent(mContext, LogingActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
                        String token = "";
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            token = jsonObject.getJSONObject("data").getString("access_token");
                            AccountUtil.saveToken(mContext, token);
                            AccountUtil.saveAccount_phone(mContext, phone);
                            ToastUtils.showToastSHORT(mContext, "注册成功");
                            startActivity(HomeActivity.class);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("NET", token);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }
}
