package software.ecenter.study.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.R;
import software.ecenter.study.bean.BindBookBean;
import software.ecenter.study.bean.NetBaseBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.DESUtils;
import software.ecenter.study.utils.EditUtils;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * 绑定手机号
 */
public class BindPhoneActivity extends BaseActivity implements View.OnClickListener {

    private ImageView btnLeftTitle;
    private EditText editForgetUser;
    private EditText editForgetCheckcode;
    private Button btnForgetGetCode;
    private Button btnforget;
    private LinearLayout llPhone;
    private LinearLayout llCode;
    private TextView bindTvPhoneErrTip;
    private TextView bindTvCOdeErrTip;

    private String ty;
    private String thirdPartyId;
    private String nickname;
    private String headImage;
    private int loginType;

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
        setContentView(R.layout.activity_bind_phone);
        btnLeftTitle = findViewById(R.id.btn_left_title);
        editForgetUser = findViewById(R.id.edit_forget_user);
        editForgetCheckcode = findViewById(R.id.edit_forget_checkcode);
        btnForgetGetCode = findViewById(R.id.btn_forget_get_code);
        btnforget = findViewById(R.id.btn_forget);
        llPhone = findViewById(R.id.ll_phone);
        llCode = findViewById(R.id.ll_code);
        bindTvCOdeErrTip = findViewById(R.id.bind_tv_err_code_tip);
        bindTvPhoneErrTip = findViewById(R.id.bind_tv_err_phone_tip);
        Intent intent = getIntent();
        ty = intent.getStringExtra("type");
        thirdPartyId = intent.getStringExtra("thirdPartyId");
        nickname = intent.getStringExtra("nickname");
        headImage = intent.getStringExtra("headImage");
        btnLeftTitle.setOnClickListener(this);
        btnForgetGetCode.setOnClickListener(this);
        btnforget.setOnClickListener(this);
        if (ty.equals("wx")) {
            loginType = 1;
        }
        if (ty.equals("qq")) {
            loginType = 2;
        }
        EditUtils.showEditNoEmoji(editForgetCheckcode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left_title:
                finish();
                break;
            case R.id.btn_forget_get_code:
                getCheckCode();
                break;
            case R.id.btn_forget:
                String phone = editForgetUser.getText().toString();
                String code = editForgetCheckcode.getText().toString();
                //手机号码、登录密码没有填写或格式有误
                if (TextUtils.isEmpty(phone) || !ToolUtil.isPhoneNumber(phone)) {
//                    ToastUtils.showToastLONG(mContext, "请输入手机号");
                    llPhone.setVisibility(View.VISIBLE);
                    bindTvPhoneErrTip.setText(getResources().getString(R.string.bind_err_phone_number));
                    return;
                }

                if (TextUtils.isEmpty(code)) {
//                    ToastUtils.showToastLONG(mContext, "请输入验证码");
                    llCode.setVisibility(View.VISIBLE);
                    bindTvCOdeErrTip.setText(getResources().getString(R.string.bind_err_code));
                    return;
                }
                if (ty.equals("wx")) {
                    loginType = 1;
                }
                if (ty.equals("qq")) {
                    loginType = 2;
                }
                forgetPassword(thirdPartyId, nickname, headImage, loginType, phone, code);
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getCheckCode() {
        String phone = editForgetUser.getText().toString();
        //手机号码、登录密码没有填写或格式有误
        if (TextUtils.isEmpty(phone) || !ToolUtil.isPhoneNumber(phone)) {
//            ToastUtils.showToastLONG(mContext, "请输入手机号");
            llPhone.setVisibility(View.VISIBLE);
            bindTvPhoneErrTip.setText(getResources().getString(R.string.bind_err_phone_number));
            return;

        }

        try {
            getCheckPhone(phone, loginType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnForgetGetCode.setClickable(false);
    }

    public void getCheckCode(String phone) {
        if (!showNetWaitLoding()) {
            return;
        }
        llPhone.setVisibility(View.INVISIBLE);
        llCode.setVisibility(View.INVISIBLE);

        JSONObject json = new JSONObject();
        try {
            json.put("phoneNumber", DESUtils.encrypt(phone)); //phone
            json.put("type", 3);
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
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        if (msg.contains("验证码错误")) {
                            llCode.setVisibility(View.VISIBLE);
                            bindTvCOdeErrTip.setText("验证码错误");
                        } else {
                            llPhone.setVisibility(View.VISIBLE);
                            bindTvPhoneErrTip.setText(msg);
                        }
//                        ToastUtils.showToastLONG(mContext, msg);
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

    /**
     * 绑定手机号登录接口
     *
     * @param thirdPartyId
     * @param nickname
     * @param headImage
     * @param loginType
     * @param phoneNumber
     * @param checkCode
     */
    public void forgetPassword(String thirdPartyId, String nickname, String headImage, int loginType,
                               String phoneNumber, String checkCode) {
        llPhone.setVisibility(View.INVISIBLE);
        bindTvPhoneErrTip.setText("");
        llCode.setVisibility(View.INVISIBLE);
        bindTvCOdeErrTip.setText("");
        if (!showNetWaitLoding()) {
            return;
        }
        String registrationId = "";
        registrationId = JPushInterface.getRegistrationID(mContext);

        JSONObject json = new JSONObject();
        try {
            json.put("thirdPartyId", thirdPartyId);
            json.put("nickname", nickname);
            json.put("headImage", headImage);
            json.put("loginType", loginType);
            json.put("phone", phoneNumber);
            json.put("code", checkCode);
            json.put("registrationId", registrationId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext, false).bindPhone(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        NetBaseBean netBaseBean = ParseUtil.parseBean(response, NetBaseBean.class);
                        if (netBaseBean != null) {
                            if (netBaseBean.getStatus() == 1) {
                                String token = "";
                                token = netBaseBean.getData().getAccess_token();
                                AccountUtil.saveToken(mContext, token);
                                startActivity(HomeActivity.class);
                            } else {
                                ToastUtils.showToastSHORT(mContext, netBaseBean.getMessage());
                            }
                        } else {
                            JPushInterface.resumePush(mContext);
                            String token = "";
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                token = jsonObject.getJSONObject("data").getString("access_token");
                                startActivity(HomeActivity.class);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("NET", token);
                            AccountUtil.saveToken(mContext, token);
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastSHORT(mContext, msg);
                        if (msg.contains("手机")) {
                            llPhone.setVisibility(View.VISIBLE);
                            bindTvPhoneErrTip.setText(msg);
                        } else if (msg.contains("验证码")) {
                            llCode.setVisibility(View.VISIBLE);
                            bindTvCOdeErrTip.setText(msg);
                        }
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mContext != null) {
                BindPhoneActivity.this.finish();
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void getCheckPhone(final String phone, final int loginType) throws Exception {
        if (!showNetWaitLoding()) {
            return;
        }
        llPhone.setVisibility(View.INVISIBLE);
        JSONObject json = new JSONObject();
        json.put("phoneNumber", phone);
        json.put("type", loginType);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext, false)
                .isNotBindThirdAccount(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        Bean bean = ParseUtil.parseBean(response, Bean.class);
                        if (bean != null) {
                            switch (bean.getStatus()) {
                                case 1:
                                    getCheckCode(phone);
                                    startTimer();
                                    break;
                                case -1:
                                    llPhone.setVisibility(View.VISIBLE);
                                    bindTvPhoneErrTip.setText(bean.getMessage());
                                    break;
                            }

                        }

                        btnForgetGetCode.setClickable(false);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        llPhone.setVisibility(View.VISIBLE);
                        bindTvPhoneErrTip.setText(msg);
                    }

                });
    }


    public class Bean {
        private int status;
        private String message;
        private String data;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

}
