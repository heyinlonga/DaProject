package software.ecenter.study.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.activity.BaseActivity;
import software.ecenter.study.activity.BindPhoneActivity;
import software.ecenter.study.activity.HomeActivity;
import software.ecenter.study.bean.Bean;
import software.ecenter.study.bean.MineBean.PersonDetailBean;
import software.ecenter.study.net.OkHttpUtils;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.ConstantValue;
import software.ecenter.study.utils.JpushUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * 创建时间: 2018/5/21.
 * 编写人: wxy
 * 功能描述:集成微信登录sdk的接口
 * https://blog.csdn.net/m0_37919094/article/details/72956215
 */

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private IWXAPI api; // IWXAPI 是第三方app和微信通信的openapi接口
    private String thirdPartyId;
    private String nickname;
    private String headImage;
    private int tyrr;
    private String ty;
    private int type;
    private int loginType;
    private String logintype = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, ConstantValue.APP_ID, true);
        api.handleIntent(getIntent(), this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void Get(String noticezhi) {
        logintype = noticezhi;
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    //请求回调结果处理
    @Override
    public void onResp(BaseResp baseResp) {
        //登录回调
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                switch (baseResp.getType()) {
                    case ConstantsAPI.COMMAND_SENDAUTH:
                        String code = ((SendAuth.Resp) baseResp).code;
                        //获取用户信息
                        getAccessToken(code);
                        break;
                    case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                        finish();
                        break;
                }

                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                finish();
                break;
            default:
                finish();
                break;
        }
    }

    private void getAccessToken(String code) {
        //获取授权
        StringBuffer loginUrl = new StringBuffer();
        loginUrl.append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=")
                .append(ConstantValue.APP_ID)
                .append("&secret=")
                .append(ConstantValue.APPsecret)
                .append("&code=")
                .append(code)
                .append("&grant_type=authorization_code");
        OkHttpUtils.ResultCallback<String> resultCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                String access = null;
                String openId = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    access = jsonObject.getString("access_token");
                    openId = jsonObject.getString("openid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //获取个人信息
                String getUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access + "&openid=" + openId;
                OkHttpUtils.ResultCallback<String> reCallback = new OkHttpUtils.ResultCallback<String>() {
                    @Override
                    public void onSuccess(String responses) {

                        try {
                            JSONObject jsonObject = new JSONObject(responses);
                            thirdPartyId = jsonObject.getString("openid");
                            nickname = jsonObject.getString("nickname");
                            headImage = jsonObject.getString("headimgurl");
                            if (logintype.equals("wechatlogin")) {
                                loginType = 1;
                                Intent intent = new Intent();
                                intent.setAction("software.ecenter.study.wxLogin");
                                intent.putExtra("thirdPartyId", thirdPartyId);
                                intent.putExtra("nickname", nickname);
                                intent.putExtra("headImage", headImage);
                                sendBroadcast(intent);
                                finish();
//                                LoginWeChat(thirdPartyId, nickname, headImage, loginType);
                            }
                            if (logintype.equals("wechatbind")) {
                                type = 1;
                                bindWeChat(thirdPartyId, nickname, headImage, type);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(WXEntryActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                };
                OkHttpUtils.get(getUserInfo, reCallback);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(WXEntryActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        OkHttpUtils.get(loginUrl.toString(), resultCallback);
    }

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }

    //登录
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

    //绑定
    private void bindWeChat(String thirdPartyId, String nickname, String headImage, int type) {
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
                            finish();
                            return;
                        }
                        ToastUtils.showToastLONG(mContext, response);
                        finish();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                        finish();
                    }

                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
                            AccountUtil.saveUserInfo(mContext, personDetailBean);
                            JpushUtil.setJpushMsgTipAudio(mContext);
                            JPushInterface.resumePush(mContext);
                            startActivity(HomeActivity.class);
                            finish();
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                        finish();
                    }

                });

    }
}
