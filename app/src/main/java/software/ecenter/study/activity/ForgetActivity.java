package software.ecenter.study.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.R;
import software.ecenter.study.bean.CheckCodeAndPwdBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.DESUtils;
import software.ecenter.study.utils.EditUtils;
import software.ecenter.study.utils.ExampleUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/***
 * dec 忘记密码
 *
 * */

public class ForgetActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.edit_forget_user)
    EditText editForgetUser;
    @BindView(R.id.edit_forget_checkcode)
    EditText editForgetCheckcode;
    @BindView(R.id.btn_forget_get_code)
    Button btnForgetGetCode;
    @BindView(R.id.edit_forget_password)
    EditText editForgetPassword;
    @BindView(R.id.btn_forget)
    Button btnForget;
    @BindView(R.id.linearLayout1)
    LinearLayout linearLayout1;
    @BindView(R.id.ll_password)
    LinearLayout llpassword;
    @BindView(R.id.ll_phone)
    LinearLayout llphone;
    @BindView(R.id.ll_code)
    LinearLayout llcode;
    @BindView(R.id.forget_tv_err_phone_tip)
    TextView forgetTvErrPhone;
    @BindView(R.id.forget_tv_err_check_code_tip)
    TextView forgetTvErrCode;
    @BindView(R.id.forget_tv_err_password_tip)
    TextView forgetTvErrPassword;
    @BindView(R.id.forget_title)
    TextView forgetTitle;
    @BindView(R.id.edit_psd_status)
    ImageView editStatus;

    private final int MSG_GET_CHECKCODE = 1;// 倒计时消息
    private int mSecond = 60; //倒计时
    private int reset = 0;
    private String phone = "";
    /**
     * 定时更新验证时间
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GET_CHECKCODE:
                    btnForgetGetCode.setText(String.valueOf(mSecond) + "秒");
                    if (mSecond == 0) {
                        btnForgetGetCode.setText(R.string.get_check_code);
                    }
                    mSecond--;
                    if (mSecond < 0)
                        mSecond = 60;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        ButterKnife.bind(this);
        reset = getIntent().getIntExtra("reset_password", 0);
        phone = getIntent().getStringExtra("phone");
        if (reset == 1) {
            forgetTitle.setText("修改密码");
            editForgetUser.setText(ToolUtil.getString(phone));
            editForgetUser.setEnabled(false);
        }
        EditUtils.showEditNoEmoji(editForgetCheckcode);
    }


    /**
     * 获取验证码
     */
    private void getCheckCode() {
        String phone = editForgetUser.getText().toString();
        //手机号码、登录密码没有填写或格式有误
        if (TextUtils.isEmpty(phone) || !ToolUtil.isPhoneNumber(phone)) {
//            ToastUtils.showToastLONG(mContext, "请输入手机号");
            llphone.setVisibility(View.VISIBLE);
            forgetTvErrPhone.setText(getResources().getString(R.string.forget_err_phone_number));
            return;
        }

//        if (!ExampleUtil.checkPassword(editForgetPassword.getText().toString().trim())) {
//            ToastUtils.showToastLONG(mContext, getResources().getString(R.string.forget_password));
//            editForgetPassword.getText().clear();
//            return;
//        }
        getCheckCode(phone);
        btnForgetGetCode.setClickable(false);
    }

    public void getCheckCode(String phone) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {

            json.put("phoneNumber", DESUtils.encrypt(phone));//phone
            json.put("type", 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext, false).getCheckCode(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        editForgetCheckcode.requestFocus();
                        ToastUtils.showToastSHORT(mContext, "短信已发送，请注意手机接收");
                        startTimer();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    /**
     * 验证码客户端定时器
     */
    private void startTimer() {
        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (mSecond == 0) {
                    btnForgetGetCode.setClickable(true);
                    timer.cancel();
                }
                handler.sendEmptyMessage(MSG_GET_CHECKCODE);
            }
        };
        timer.schedule(task, 0, 1000);
    }

    @OnClick({R.id.btn_left_title, R.id.btn_forget_get_code, R.id.btn_forget, R.id.edit_psd_status})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_psd_status://密码显示隐藏
                EditUtils.showAndHidePsd(editStatus,editForgetPassword);
                break;
            case R.id.btn_left_title:
                finish();
                break;
            case R.id.btn_forget_get_code:
                getCheckCode();
                break;
            case R.id.btn_forget:
                String phone = editForgetUser.getText().toString();
                String code = editForgetCheckcode.getText().toString();
                String password = editForgetPassword.getText().toString();
                //手机号码、登录密码没有填写或格式有误
                if (TextUtils.isEmpty(phone) || !ToolUtil.isPhoneNumber(phone)) {
//                    ToastUtils.showToastLONG(mContext, "请输入手机号");
                    llphone.setVisibility(View.VISIBLE);
                    forgetTvErrPhone.setText(getResources().getString(R.string.forget_err_phone_number));
                    return;
                }
                llphone.setVisibility(View.INVISIBLE);
                forgetTvErrPhone.setText("");
                if (TextUtils.isEmpty(code)) {
//                    ToastUtils.showToastLONG(mContext, "请输入验证码");
                    llcode.setVisibility(View.VISIBLE);
                    forgetTvErrCode.setText(getResources().getString(R.string.forget_err_code));
                    return;

                }
                llcode.setVisibility(View.INVISIBLE);
                forgetTvErrCode.setText("");
                if (TextUtils.isEmpty(password) || !ExampleUtil.checkPassword(password)) {
//                    ToastUtils.showToastLONG(mContext, "请输入密码");
                    llpassword.setVisibility(View.VISIBLE);
                    forgetTvErrPassword.setText(getResources().getString(R.string.forget_password));
                    editForgetPassword.getText().clear();
                    return;
                }
                llpassword.setVisibility(View.INVISIBLE);
                forgetTvErrPassword.setText("");
                llpassword.setVisibility(View.INVISIBLE);
                llphone.setVisibility(View.INVISIBLE);
                llcode.setVisibility(View.INVISIBLE);
                forgetPassword(phone, code, password);
                break;
        }
    }

    public void forgetPassword(String phoneNumber, String checkCode, String newPassword) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {

            json.put("phoneNumber", DESUtils.encrypt(phoneNumber));   //phoneNumber
            json.put("checkCode", checkCode);
            json.put("newPassword", DESUtils.encrypt(newPassword));//newPassword

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext, false).forgetPassword(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastSHORT(mContext, "修改成功");
                        Intent intent = new Intent(mContext, LogingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        if (msg.contains("验证码错误")) {
                            llcode.setVisibility(View.VISIBLE);
                            forgetTvErrCode.setText(msg);
                        }
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }
}
