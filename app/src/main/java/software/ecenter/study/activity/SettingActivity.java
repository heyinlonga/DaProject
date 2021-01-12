package software.ecenter.study.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import retrofit2.Retrofit;
import software.ecenter.study.R;
import software.ecenter.study.bean.MineBean.SetDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.tool.FileAccessor;
import software.ecenter.study.tool.FileManager;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.JpushUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * dec 系统设置
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.btn_message)
    ImageView btnMessage;
    @BindView(R.id.btn_message_audio)
    ImageView btnMessageAudio;
    @BindView(R.id.btn_not_wifi)
    ImageView btnNotWifi;
    @BindView(R.id.btn_cache)
    TextView btnCache;
    @BindView(R.id.tv_yinshi)
    TextView tv_yinshi;
    @BindView(R.id.tv_xieyi)
    TextView tv_xieyi;
    @BindView(R.id.tv_share)
    TextView btnShare;
    @BindView(R.id.version_tip)
    LinearLayout versionTip;
    @BindView(R.id.btn_version)
    RelativeLayout btnVersion;
    @BindView(R.id.btn_exit)
    RelativeLayout btnExit;

    SetDetailBean mSetDetailBean;
    @BindView(R.id.version_text)
    TextView versionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        getData();
    }

    public void getData() {
        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getSettingList(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        mSetDetailBean = ParseUtil.parseBean(response, SetDetailBean.class);
                        initView();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                        initView();
                    }

                });

    }

    /**
     * 是否允许推送有提示音
     */
    public void setSystemOption() {
        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        if (AccountUtil.getMsgTip(mContext)) {
            map.put("messageTipOpen", "false");
        } else {
            map.put("messageTipOpen", "true");
        }

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).setSystemOption(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, "设置成功");
                        if (AccountUtil.getMsgTip(mContext)) {
                            AccountUtil.saveMsgTip(mContext, false);
                            btnMessageAudio.setImageResource(R.drawable.guan);
                        } else {
                            AccountUtil.saveMsgTip(mContext, true);
                            btnMessageAudio.setImageResource(R.drawable.kai);
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    public void initView() {

        if (AccountUtil.getMsgPush(mContext)) {
            btnMessage.setImageResource(R.drawable.kai);
        } else {
            btnMessage.setImageResource(R.drawable.guan);
        }
        if (AccountUtil.getMsgTip(mContext)) {
            btnMessageAudio.setImageResource(R.drawable.kai);
        } else {
            btnMessageAudio.setImageResource(R.drawable.guan);
        }

        if (AccountUtil.getWifiTipSet(mContext)) {
            btnNotWifi.setImageResource(R.drawable.kai);
        } else {
            btnNotWifi.setImageResource(R.drawable.guan);
        }
        versionTip.setVisibility(View.INVISIBLE);

        versionText.setText("版本：v" + getVersionName());
        if (mSetDetailBean != null) {
            AccountUtil.saveMsgTip(mContext, mSetDetailBean.getData().isMessageTipOpen());
            if (mSetDetailBean.getData().isMessageTipOpen()) {
                btnMessageAudio.setImageResource(R.drawable.kai);
            } else {
                btnMessageAudio.setImageResource(R.drawable.guan);
            }
            String sCurVersion = getVersionName();

            String[] strs = sCurVersion.split("\\.");
            StringBuilder builder = new StringBuilder();
            for (String string : strs) {
                builder.append(string);
            }

            double curVersion = Double.parseDouble(
                    TextUtils.isEmpty(builder.toString()) ? "0" : builder.toString());
            String sServiceVersion = mSetDetailBean.getData().getAppVersion();
            String[] sServiceVersionArray = sServiceVersion.split("\\.");
            StringBuilder serviceVersionbuilder = new StringBuilder();
            for (String string : sServiceVersionArray) {
                serviceVersionbuilder.append(string);
            }
            double serviceVersion = Double.parseDouble(TextUtils.isEmpty(serviceVersionbuilder.toString())
                    ? "0" : serviceVersionbuilder.toString());

            if (curVersion < serviceVersion) {
                versionTip.setVisibility(View.VISIBLE);
            }

        }


    }


    @OnClick({R.id.btn_left_title, R.id.btn_message, R.id.tv_yinshi, R.id.tv_xieyi, R.id.tv_share, R.id.btn_message_audio, R.id.btn_not_wifi, R.id.btn_cache, R.id.btn_version, R.id.btn_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_message:
                if (AccountUtil.getMsgPush(mContext)) {
                    AccountUtil.saveMsgPush(mContext, false);
                    JPushInterface.stopPush(mContext);
                    btnMessage.setImageResource(R.drawable.guan);
                } else {
                    AccountUtil.saveMsgPush(mContext, true);
                    JPushInterface.resumePush(mContext);
                    btnMessage.setImageResource(R.drawable.kai);
                }
                break;
            case R.id.btn_message_audio:
//                if (AccountUtil.getMsgTip(mContext)) {
//                    AccountUtil.saveMsgTip(mContext, false);
//                    btnMessageAudio.setImageResource(R.drawable.guan);
//                } else {
//                    AccountUtil.saveMsgTip(mContext, true);
//                    btnMessageAudio.setImageResource(R.drawable.kai);
//                }
                setSystemOption();
                break;
            case R.id.btn_not_wifi:
                if (AccountUtil.getWifiTipSet(mContext)) {
                    AccountUtil.saveWifiTipSet(mContext, false);
                    btnNotWifi.setImageResource(R.drawable.guan);
                } else {
                    AccountUtil.saveWifiTipSet(mContext, true);
                    btnNotWifi.setImageResource(R.drawable.kai);
                }
                break;
            case R.id.btn_cache:
                FileAccessor.deleteDir(FileManager.getInstance(mContext).getTempDir());
                ToastUtils.showToastSHORT(mContext, "清理完成");
                break;
            case R.id.tv_share://分享
                startActivity(new Intent(this, AppShareActivity.class));
                break;
            case R.id.tv_yinshi://隐私
                startActivity(new Intent(mContext, WebActivity.class).putExtra("url", "http://xzykt.longmenshuju.com/Privacy")
                        .putExtra("fuwuxieyi", "隐私政策"));
                break;
            case R.id.tv_xieyi://协议
                startActivity(new Intent(mContext, WebActivity.class).putExtra("url", "http://xzykt.longmenshuju.com/userAgreement")
                        .putExtra("fuwuxieyi", "用户协议"));
                break;
            case R.id.btn_version:
                //TODO 版本更新

                break;
            case R.id.btn_exit:
                logout();
                break;
        }
    }

    private void logout() {
        new RetroFactory(mContext, RetroFactory
                .getRetroFactory(mContext).logout()).handleResponse(new RetroFactory.ResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                String account = AccountUtil.getAccountMobile(mContext);
                AccountUtil.clearAccountInfo(mContext);
                AccountUtil.saveAccount_phone(mContext, account);
                AccountUtil.saveToken(mContext, "");

                ToastUtils.showToastSHORT(mContext, "您已退出登录");
                JPushInterface.stopPush(mContext);
                Intent intent = new Intent(mContext, LogingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFail(int type, String msg) {
                ToastUtils.showToastSHORT(mContext, msg);
            }
        });
    }

    private String getVersionName() {
        String verName = "";
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            verName = packInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

}
