package software.ecenter.study.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
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
import software.ecenter.study.utils.DESUtils;
import software.ecenter.study.utils.EditUtils;
import software.ecenter.study.utils.ExampleUtil;
import software.ecenter.study.utils.NetworkUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/***
 * dec 注册1
 *
 * */
public class RegisterActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.edit_register_user)
    EditText editRegisterUser;
    @BindView(R.id.edit_register_checkcode)
    EditText editRegisterCheckcode;
    @BindView(R.id.btn_register_get_code)
    Button btnRegisterGetCode;
    @BindView(R.id.edit_register_password)
    EditText editRegisterPassword;
    @BindView(R.id.btn_register_next)
    Button btnRegisterNext;
//    @BindView(R.id.linearLayout1)
//    LinearLayout linearLayout1;
    @BindView(R.id.ll_phone)
    LinearLayout llphone;
    @BindView(R.id.ll_code)
    LinearLayout llcode;
    @BindView(R.id.ll_password)
    LinearLayout llpassword;
    @BindView(R.id.regist_tv_err_phone)
    TextView registTvErrPhone;
    @BindView(R.id.regist_tv_err_code)
    TextView registTvErrCode;
    @BindView(R.id.regist_tv_err_password)
    TextView registTvErrPassword;
    @BindView(R.id.edit_psd_status)
    ImageView editStatus;
    @BindView(R.id.tv_yinsi)
    TextView tvYinsi;
    private final int MSG_GET_CHECKCODE = 1;// 倒计时消息
    private int mSecond = 60; //倒计时
    /**
     * 定时更新验证时间
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GET_CHECKCODE:
                    btnRegisterGetCode.setText(String.valueOf(mSecond) + "秒");
                    if (mSecond == 0) {
                        btnRegisterGetCode.setText(R.string.get_check_code);
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
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        EditUtils.showEditNoEmoji(editRegisterCheckcode);
        String text = "注册即表示同意“用户协议”和“隐私协议”。";
        tvYinsi.setText(getClickableSpan(RegisterActivity.this, text));
        tvYinsi.setMovementMethod(LinkMovementMethod.getInstance());
    }


    //设置超链接文字
    private static SpannableString getClickableSpan(final Activity activity, String text) {
        SpannableString spanStr = new SpannableString(text);
        //设置下划线文字
//        spanStr.setSpan(new UnderlineSpan(), 42, 54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的单击事件
        spanStr.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
//                super.updateDrawState(ds);
            }

            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(activity, WebActivity.class);
                intent.putExtra("url", "http://xzykt.longmenshuju.com/userAgreement");
                intent.putExtra("fuwuxieyi", "用户协议");
                activity.startActivity(intent);
            }
        }, 7, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的前景色
        spanStr.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.color_EE7C55)), 7, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置下划线文字
//        spanStr.setSpan(new UnderlineSpan(), 53, 65, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的单击事件
        spanStr.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
//                super.updateDrawState(ds);
            }

            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(activity, WebActivity.class);
                intent.putExtra("url", "http://xzykt.longmenshuju.com/Privacy");
                intent.putExtra("fuwuxieyi", "隐私政策");
                activity.startActivity(intent);
            }
        }, 14, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的前景色
        spanStr.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.color_EE7C55)), 14, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }



    /**
     * 获取验证码
     */
    private void getCheckCode() {
        String phone = editRegisterUser.getText().toString();
        llphone.setVisibility(View.GONE);
        //手机号码、登录密码没有填写或格式有误
        if (TextUtils.isEmpty(phone)) {
//            ToastUtils.showToastLONG(mContext, "请输入手机号");
            llphone.setVisibility(View.VISIBLE);
            registTvErrPhone.setText("手机号码不正确");
            return;
        }
        if (!ToolUtil.isPhoneNumber(phone)) {
//            ToastUtils.showToastLONG(mContext, "请输入正确的手机号");
            llphone.setVisibility(View.VISIBLE);
            registTvErrPhone.setText("手机号码不正确");
            return;
        }
        //getCheckCode(phone);
        getCheckPhone(phone);
    }

    public void getCheckPhone(final String phone) {
        if (!NetworkUtil.isConnected(mContext)) {
            ToastUtils.showToastLONG(mContext, "网络未连接");
            return ;
        }
        llphone.setVisibility(View.GONE);
        Map<String, String> map = new HashMap<>();
        //这是对手机号进行了加密
        map.put("phone", DESUtils.encrypt(phone));//phone

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext, false).checkPhone(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
//                        dismissNetWaitLoging();
                        getCheckCode(phone);
                        btnRegisterGetCode.setClickable(false);
                    }

                    @Override
                    public void onFail(int type, String msg) {
//                        dismissNetWaitLoging();
                        llphone.setVisibility(View.VISIBLE);
                        registTvErrPhone.setText(msg);
                        ToastUtils.showToastSHORT(mContext,msg);
                    }

                });
    }

    public void getCheckCode(String phone) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {

            json.put("phoneNumber", DESUtils.encrypt(phone));     //phone
            json.put("type", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext, false).getCheckCode(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        editRegisterCheckcode.requestFocus();
                        ToastUtils.showToastSHORT(mContext, "短信已发送，请注意手机接收");
                        startTimer();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        btnRegisterGetCode.setClickable(true);
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
                    btnRegisterGetCode.setClickable(true);
                    timer.cancel();
                }
                handler.sendEmptyMessage(MSG_GET_CHECKCODE);
            }
        };
        timer.schedule(task, 0, 1000);
    }

    @OnClick({R.id.btn_left_title, R.id.btn_register_get_code, R.id.btn_register_next, R.id.edit_psd_status})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_psd_status://密码显示隐藏
                EditUtils.showAndHidePsd(editStatus,editRegisterPassword);
                break;
            case R.id.btn_left_title:
                finish();
                break;
            case R.id.btn_register_get_code:
                getCheckCode();
                break;
            case R.id.btn_register_next:
//                startActivity(new Intent(mContext, RegisterTwoActivity.class));
                checkCanNext();
                break;
        }
    }

    private void checkCanNext() {
        String phone = editRegisterUser.getText().toString();
        String code = editRegisterCheckcode.getText().toString();
        String password = editRegisterPassword.getText().toString();
        //手机号码、登录密码没有填写或格式有误
        if (TextUtils.isEmpty(phone) || !ToolUtil.isPhoneNumber(phone)) {
//                    ToastUtils.showToastLONG(mContext, "请输入手机号");
            llphone.setVisibility(View.VISIBLE);
            registTvErrPhone.setText(getResources().getString(R.string.regist_err_phone_number));
            return;
        }

        if (TextUtils.isEmpty(code)) {
//                    ToastUtils.showToastLONG(mContext, "请输入验证码");
            llcode.setVisibility(View.VISIBLE);
            registTvErrCode.setText(getResources().getString(R.string.regist_err_code));
            return;
        }
        if (TextUtils.isEmpty(password)) {
//                    ToastUtils.showToastLONG(mContext, "请输入密码");
            llpassword.setVisibility(View.VISIBLE);
            registTvErrPassword.setText(getResources().getString(R.string.regist_password));
            return;
        }

        if (!ExampleUtil.checkPassword(password)) {
            llpassword.setVisibility(View.VISIBLE);
            registTvErrPassword.setText(getResources().getString(R.string.regist_password));
            editRegisterPassword.getText().clear();
            return;
        }

        llpassword.setVisibility(View.GONE);
        llphone.setVisibility(View.GONE);
        llcode.setVisibility(View.GONE);

        checkCodePwd();

    }

    /**
     * 验证验证码、密码是否正确
     */
    private void checkCodePwd() {
        if (!showNetWaitLoding()) {
            return;
        }
        JSONObject json = new JSONObject();
        final String phone = editRegisterUser.getText().toString().trim();
        final String password = editRegisterPassword.getText().toString().trim();
        final String code = editRegisterCheckcode.getText().toString().trim();
        try {
            json.put("password", DESUtils.encrypt(password));//password
            json.put("phoneNumber", DESUtils.encrypt(phone));//phone
            json.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext, false).checkCodePwd(body)).handleResponse(new RetroFactory.ResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                dismissNetWaitLoging();
                CheckCodeAndPwdBean checkCodeAndPwdBean = ParseUtil.parseBean(response, CheckCodeAndPwdBean.class);
                if (!checkCodeAndPwdBean.getData().isCodeCorrect()) {
                    llcode.setVisibility(View.VISIBLE);
                    registTvErrCode.setText(checkCodeAndPwdBean.getData().getMsg());
                    return;
                }
                if (!checkCodeAndPwdBean.getData().isPhoneCorrect()) {
                    llphone.setVisibility(View.VISIBLE);
                    registTvErrPhone.setText(checkCodeAndPwdBean.getData().getMsg());
                    return;
                }
                if (!checkCodeAndPwdBean.getData().isPwdCorrect()) {
                    llpassword.setVisibility(View.VISIBLE);
                    registTvErrPassword.setText(checkCodeAndPwdBean.getData().getMsg());
                    return;
                }
                Intent intent = new Intent(mContext, RegisterTwoActivity.class);
                intent.putExtra("phone", phone);
                intent.putExtra("code", code);
                intent.putExtra("password", password);
                startActivity(intent);
            }

            @Override
            public void onFail(int type, String msg) {
                dismissNetWaitLoging();
                ToastUtils.showToastLONG(mContext, msg);
            }
        });

    }
}
