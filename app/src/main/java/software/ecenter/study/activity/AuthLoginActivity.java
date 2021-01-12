package software.ecenter.study.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.R;
import software.ecenter.study.View.CircleImageView;
import software.ecenter.study.bean.Bean;
import software.ecenter.study.bean.HomeBean.BuyDetailBean;
import software.ecenter.study.bean.WxBean.AuthLoginBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.ActivityControl;
import software.ecenter.study.utils.MyGlideUrl;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.StatusBarUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * Message 授权登录
 * Create by Administrator
 * Create by 2019/11/12
 */
public class AuthLoginActivity extends BaseActivity {

    @BindView(R.id.tv_cloase)
    TextView tv_cloase;
    @BindView(R.id.tv_authLogin)
    TextView tv_authLogin;
    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.ll_top)
    LinearLayout ll_top;

    @Override
    protected void setStatubarColor() {
        StatusBarUtil.setStatusBarWhite(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authlogin);
        ButterKnife.bind(this);
        if (TextUtils.isEmpty(AccountUtil.getToken(mContext))) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(mContext, SplashActivity.class);
                    if (getIntent() != null) {
                        Uri data = getIntent().getData();
                        if (data != null) {
                            intent.setData(data);
                        }
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent, 1);
                    finish();
                }
            }, 500);
        } else {
            Activity activity = ActivityControl.getScreenManager().firstActivity();
            if (AccountUtil.getLoginToAuth(mContext)) {
                ll_top.setVisibility(View.VISIBLE);
            } else {
                if (activity instanceof AuthLoginActivity) {
                    startActivity(HomeActivity.class);
                    finish();
                } else {
                    ll_top.setVisibility(View.VISIBLE);
                }
            }
//            if (!AccountUtil.getLoginToAuth(mContext)) {
//                startActivity(HomeActivity.class);
//                finish();
//            } else {
//                ll_top.setVisibility(View.VISIBLE);
//            }

        }
        String nickName = AccountUtil.getNickName(mContext);
        String phone = AccountUtil.getRelMobile(mContext);
        String headImage = AccountUtil.getHeadImage(mContext);
        if (!TextUtils.isEmpty(nickName)) {
            tv_name.setText(nickName);
        } else {
            tv_name.setText(phone);
        }
        if (!TextUtils.isEmpty(headImage)) {
            Glide.with(mContext).load(new MyGlideUrl(headImage)).error(R.drawable.morentx).into(imgHead);//用户头像
        } else {
            imgHead.setImageResource(R.drawable.morentx);
        }
    }

    @OnClick({R.id.tv_cloase, R.id.tv_authLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cloase://取消
                AccountUtil.saveLoginToAuth(mContext, false);
                finish();
//                gotoback();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = getPackageManager().getLaunchIntentForPackage("com.study.talkcompute");
                        if (intent != null) {
                            startActivity(intent);
                        }
                    }
                },200);

                break;
            case R.id.tv_authLogin://授权登录
                tv_authLogin.setClickable(false);
                getAuthorize();
                break;
        }
    }

    //授权登录
    public void getAuthorize() {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("type", 0);
            json.put("clientId", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getAuthorize(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        AuthLoginBean bean = ParseUtil.parseBean(response, AuthLoginBean.class);
                        if (bean != null) {
                            AuthLoginBean.Data data = bean.getData();
                            if (data != null) {
                                String openId = data.getOpenId();
                                AccountUtil.saveLoginToAuth(mContext, false);
                                tv_authLogin.setClickable(true);
                                sendBroadcast(new Intent().setAction("software.ecenter.study.authLogin.SUCCESS").putExtra("openId", openId));
                                finish();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = getPackageManager().getLaunchIntentForPackage("com.study.talkcompute");
                                        if (intent != null) {
                                            startActivity(intent);
                                        }
                                    }
                                },200);
//                                gotoback();
                            }
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                        tv_authLogin.setClickable(true);
                    }

                });
    }

    private void gotoback() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
