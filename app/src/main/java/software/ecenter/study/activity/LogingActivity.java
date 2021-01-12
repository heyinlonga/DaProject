package software.ecenter.study.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import retrofit2.Retrofit;
import software.ecenter.study.R;
import software.ecenter.study.bean.MineBean.PersonDetailBean;
import software.ecenter.study.bean.YanzhengmaBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.ConstantValue;
import software.ecenter.study.utils.DESUtils;
import software.ecenter.study.utils.EditUtils;
import software.ecenter.study.utils.JpushUtil;
import software.ecenter.study.utils.NetworkUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;


/*
 * 这是一个最新的版本study代码    注意！！！！！！！！！！
 * */

/***
 * dec 登录
 *
 * */
public class LogingActivity extends BaseActivity {
    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.auth_code)
    EditText auth_code;
    @BindView(R.id.auth_code_pictrue)
    ImageView auth_code_pictrue;
    @BindView(R.id.auto_refresh_btn)
    ImageView auto_refresh_btn;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.edit_log_user)
    EditText editLogUser;
    @BindView(R.id.edit_log_password)
    EditText editLogPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    TextView btnRegister;
    @BindView(R.id.btn_login_forget)
    TextView btnLoginForget;
    @BindView(R.id.btn_weichar)
    ImageButton btnWeichar;
    @BindView(R.id.edit_psd_status)
    ImageView editStatus;
    @BindView(R.id.btn_qq)
    ImageButton btnQq;
    @BindView(R.id.linearLayout1)
    LinearLayout linearLayout1;

    private IWXAPI api; // IWXAPI 是第三方app和微信通信的openapi接口
    private Tencent mTencent;    //QQ登录
    private String name, figureurl, openid, openidqq;
    private int loginType;
    private String ty;
    private double qqloginTime = 0;
    private MyBroadcastReceiver myBroadcastReceiver;
    private String capToken;
    private JSONObject jsonObject;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loging);
        //绑定控件
        ButterKnife.bind(this);
        // 实例化Tencent
        if (mTencent == null) {
            mTencent = Tencent.createInstance(ConstantValue.APP_ID_QQ, getApplicationContext());
        }
        //监听微信登录
        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("software.ecenter.study.wxLogin");
        registerReceiver(myBroadcastReceiver, intentFilter);
        if (!TextUtils.isEmpty(AccountUtil.getToken(mContext))) {
            startActivity(HomeActivity.class);
            finish();
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (ToolUtil.getString(action).equals("software.ecenter.study.wxLogin")) {
                    String thirdPartyId = intent.getStringExtra("thirdPartyId");
                    String nickname = intent.getStringExtra("nickname");
                    String headImage = intent.getStringExtra("headImage");
                    LoginWeChat(thirdPartyId, nickname, headImage, 1);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getImgCode();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }

    //微信登录
    private void LoginWeChat(final String thirdPartyId, final String nickname, final String headImage, final int loginType) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("thirdPartyId", thirdPartyId);
            json.put("nickname", nickname);
            json.put("headImage", headImage);
            json.put("loginType", loginType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext, false).thirdPartyLogin(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        String token = "";
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //token = jsonObject.getString("access_token");
                            token = jsonObject.getJSONObject("data").getString("access_token");
                            AccountUtil.saveToken(mContext, token);
                            getUserInfo();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("NET", token);
                        AccountUtil.saveToken(mContext, token);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        Intent intent = new Intent(mContext, BindPhoneActivity.class);
                        ty = "wx";
                        intent.putExtra("type", ty);
                        intent.putExtra("thirdPartyId", thirdPartyId);
                        intent.putExtra("nickname", nickname);
                        intent.putExtra("headImage", headImage);
                        startActivity(intent);
                        finish();
                    }

                });
    }

    @OnClick({R.id.btn_left_title, R.id.btn_login, R.id.btn_register, R.id.btn_login_forget, R.id.btn_weichar, R.id.btn_qq, R.id.edit_psd_status, R.id.auth_code, R.id.auth_code_pictrue, R.id.auto_refresh_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_psd_status://密码显示隐藏
                EditUtils.showAndHidePsd(editStatus, editLogPassword);
                break;
            case R.id.btn_left_title:
                finish();
                break;
            case R.id.btn_login://登录
                netLoagin();
                break;
            case R.id.btn_register:
                startActivity(RegisterActivity.class);
                break;
            case R.id.btn_login_forget:
                startActivity(ForgetActivity.class);
                break;
            case R.id.btn_weichar:
                //微信登录
                weixinlogin();
                break;
            case R.id.btn_qq:
                //QQ登录
                qqlogin();

                break;
            case R.id.auto_refresh_btn:

                getImgCode();
                break;
        }
    }

    private void qqlogin() {
        if (!mTencent.isSessionValid()) {
            mTencent.logout(LogingActivity.this);
            mTencent.login(LogingActivity.this, "all", new BaseUiListener());
        } else {
            QQToken qqToken = mTencent.getQQToken();
            openid = mTencent.getOpenId();
            UserInfo info = new UserInfo(mContext, qqToken);
            info.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    //用户信息获取
                    try {
                        name = ((JSONObject) o).getString("nickname");
                        figureurl = ((JSONObject) o).getString("figureurl");
                        LoginQQ(openid, name, figureurl, 2);
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
        }
    }

    private class BaseUiListener implements IUiListener {


        public void onComplete(Object response) {
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
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
            UserInfo info = new UserInfo(mContext, qqToken);
            info.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    //用户信息获取
                    try {
                        //openidqq = ((JSONObject) o).getString("openid");
                        name = ((JSONObject) o).getString("nickname");
                        figureurl = ((JSONObject) o).getString("figureurl");

                        LoginQQ(openid, name, figureurl, 2);

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

    //qq登录
    private void LoginQQ(final String thirdPartyId, final String nickname, final String headImage, final int loginType) {
        if (!showNetWaitLoding()) {
            return;
        }
        String registrationId = "";
        registrationId = JPushInterface.getRegistrationID(mContext);
        final JSONObject json = new JSONObject();
        try {
            json.put("thirdPartyId", thirdPartyId);
            json.put("nickname", nickname);
            json.put("headImage", headImage);
            json.put("loginType", loginType);
            json.put("registrationId", registrationId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext, false).thirdPartyLogin(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        JPushInterface.resumePush(mContext);
                        String token = "";
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            token = jsonObject.getJSONObject("data").getString("access_token");
                            AccountUtil.saveToken(mContext, token);
                            getUserInfo();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("NET", token);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();

                        if (msg.contains("未绑定手机号")) {
                            Intent intent = new Intent(mContext, BindPhoneActivity.class);
                            ty = "qq";
                            intent.putExtra("type", ty);
                            intent.putExtra("thirdPartyId", thirdPartyId);
                            intent.putExtra("nickname", nickname);
                            intent.putExtra("headImage", headImage);
                            startActivity(intent);
                        } else {
                            ToastUtils.showToastSHORT(mContext, msg);
                        }
                    }
                });
    }

    //三方微信登录
    private void weixinlogin() {

        EventBus.getDefault().postSticky("wechatlogin");
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, ConstantValue.APP_ID, true);
        // 判断是否安装了微信客户端
        if (!api.isWXAppInstalled()) {
            Toast.makeText(getApplicationContext(), "您还未安装微信客户端！", Toast.LENGTH_SHORT).show();
            return;
        }
        api.registerApp(ConstantValue.APP_ID);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";//获取个人用户信息的权限
        req.state = "wechat_sdk_demo_test";//防止攻击
        api.sendReq(req);//向微信发送请求
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (HomeActivity.context != null) {
                HomeActivity.context.finish();
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 新登录
     */
    public void netLoagin() {
        String phone = editLogUser.getText().toString();
        String password = editLogPassword.getText().toString();
        String autocode = auth_code.getText().toString();
        String registrationId = "";
        registrationId = JPushInterface.getRegistrationID(mContext);
        //手机号码、登录密码没有填写或格式有误
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showToastLONG(mContext, "请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showToastLONG(mContext, "请输入密码");
            return;
        }
        if (!ToolUtil.isPhoneNumber(phone)) {
            ToastUtils.showToastLONG(mContext, "请输入正确的手机号");
            return;
        }

        if (!showNetWaitLoding()) {
            return;
        }


        Map<String, String> requestMap = new HashMap<String, String>();
        final JSONObject json = new JSONObject();

        try {
            json.put("phoneNumber", DESUtils.encrypt(phone)); ///phone
            json.put("password", DESUtils.encrypt(password)); ///password
            json.put("registrationId", registrationId);
            json.put("captcha", autocode);//新添加的
            json.put("capToken", capToken);//新添加的
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, "加密登录: "+json.toString());
//        requestMap.put("grant_type", "password");
        requestMap.put("username", phone);
        requestMap.put("password", password);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext, false).login(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        String token = "";
                        try {
                            jsonObject = new JSONObject(response);
                            token = jsonObject.getJSONObject("data").getString("access_token");
                            status = jsonObject.getInt("status");

                            String message = jsonObject.getString("message");
                            Log.e("message", message+"");
                            AccountUtil.saveToken(mContext, token);
                            AccountUtil.saveAccount_phone(mContext, editLogUser.getText().toString().trim());
                            Log.e(TAG, "onSuccess: 状态值为："+ status );
                            if (status == 1) {
                                Log.i("登陆返回值", "onSuccess: "+ status +"");
                                getUserInfo();
//                                Intent intent = new Intent(LogingActivity.this, HomeActivity.class);
//                                startActivity(intent);
                            }else {
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("NET", token);
                        AccountUtil.saveToken(mContext, token);
                        AccountUtil.saveAccount_phone(mContext, editLogUser.getText().toString().trim());
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);


                        //单独的请求验证码图片
                        OkHttpClient okHttpClient = new OkHttpClient();
                        Request build = new Request.Builder()
                                .url(RetroFactory.BASE_URL+"version2/userInfos/getCaptcha")
                                .post(new RequestBody() {
                                    @Override
                                    public MediaType contentType() {
                                        return null;
                                    }

                                    @Override
                                    public void writeTo(BufferedSink sink) throws IOException {

                                    }
                                }).build();
                        okHttpClient.newCall(build).enqueue(new Callback() {

                            private YanzhengmaBean yanzhengmaBean;


                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.i("失败", "111");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String string = response.body().string();
                                Log.e("response:      ", string);

                                String ssssss = yanzhengmaBean.toString();

                                Gson gson = new Gson();
                                yanzhengmaBean = gson.fromJson(string, YanzhengmaBean.class);
                                Log.e(TAG, "onResponse-------: " +  yanzhengmaBean.toString());
                                capToken = yanzhengmaBean.getData().getCapToken();
                                Log.e(TAG, "capToken: "+capToken );
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String imgUrl = yanzhengmaBean.getData().getImg();
                                        Log.e(TAG, "imgUrl:     " + imgUrl );
                                        //将Base64编码字符串解码成Bitmap
                                        byte[] decodedString = Base64.decode(imgUrl, Base64.DEFAULT);
                                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                        //设置ImageView图片
                                        auth_code_pictrue.setImageBitmap(decodedByte);

//                        Log.e("TAGG", uidFromBase64 + "");
//                            Glide.with(LogingActivity.this).load(str2).into(auth_code_pictrue);
                                    }
                                });
                            }
                        });
                    }

                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener());
//        Tencent.handleResultData(data, new BaseUiListener());
        if (data == null) return;
        if (resultCode == 101) {
            finish();
        }
    }

    public void getUserInfo() {
        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getUserInfo(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        PersonDetailBean personDetailBean = ParseUtil.parseBean(response, PersonDetailBean.class);
                        if (personDetailBean != null || personDetailBean.getData() != null) {
                            AccountUtil.saveAccount_phone(mContext, personDetailBean.getData().getPhoneNumber());
                            AccountUtil.saveUserInfo(mContext, personDetailBean);
                            JpushUtil.setJpushMsgTipAudio(mContext);
                            JPushInterface.resumePush(mContext);
//                            if (isAuthLogin()) {
//                                startActivity(AuthLoginActivity.class);
//                            } else {
//                                startActivity(
//                                HomeActivity.class);
                            startActivity(HomeActivity.class);
                            finish();
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);

                    }

                });
    }
    ///////新版本

    //这一块是登录页面一直存在的验证码图片

    public void getImgCode() {
        if (!NetworkUtil.isConnected(mContext)) {
            return;
        }

        //单独的请求验证码图片
        OkHttpClient okHttpClient = new OkHttpClient();
        Request build = new Request.Builder()
                .url(RetroFactory.BASE_URL+"version2/userInfos/getCaptcha")
                .post(new RequestBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public void writeTo(BufferedSink sink) throws IOException {

                    }
                }).build();
        okHttpClient.newCall(build).enqueue(new Callback() {

            private YanzhengmaBean yanzhengmaBean;

            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("失败", "111");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("response:      ", string);
                Gson gson = new Gson();
                yanzhengmaBean = gson.fromJson(string, YanzhengmaBean.class);
                Log.e(TAG, "onResponse: " +  yanzhengmaBean.toString());


                capToken = yanzhengmaBean.getData().getCapToken();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String imgUrl = yanzhengmaBean.getData().getImg();
                        Log.e(TAG, "imgUrl:     " + imgUrl );
                        //将Base64编码字符串解码成Bitmap
                        byte[] decodedString = Base64.decode(imgUrl, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        //设置ImageView图片
                        auth_code_pictrue.setImageBitmap(decodedByte);

//                        Log.e("TAGG", uidFromBase64 + "");
//                            Glide.with(LogingActivity.this).load(str2).into(auth_code_pictrue);
                    }
                });
            }
        });
    }
}


