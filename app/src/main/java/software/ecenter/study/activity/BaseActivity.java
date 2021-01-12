package software.ecenter.study.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

import software.ecenter.study.View.LoadingDialog;
import software.ecenter.study.utils.ActivityControl;
import software.ecenter.study.utils.NetworkUtil;
import software.ecenter.study.utils.StatusBarUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * Created by Mike on 2018/4/23.
 */

public class BaseActivity extends AppCompatActivity {
    public String TAG;
    public Context mContext;
    public LoadingDialog mNetWatiDialog;
    public final int pageNum = 2;
    public int curpage = 0;
    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "software.ecenter.study.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        mContext = this;
        mNetWatiDialog = new LoadingDialog(mContext);
        ActivityControl.getScreenManager().pushActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setStatubarColor();
    }

    //改变状态栏
    protected void setStatubarColor() {
        //改变状态栏字体颜色
        StatusBarUtil.setStatusBarColor(this);
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
        MobclickAgent.onPause(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityControl.getScreenManager().popActivity(this);
    }

    public boolean showNetWaitLoding() {
        if (NetworkUtil.isConnected(mContext)) {
            try {
                if (!mNetWatiDialog.isShowing() && !this.isFinishing()) {
                    mNetWatiDialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else {
            ToastUtils.showToastLONG(mContext, "网络未连接");
            return false;
        }

    }

    public void dismissNetWaitLoging() {
        try {
            if (mNetWatiDialog.isShowing()) {
                if (!this.isFinishing())
                    mNetWatiDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        if (getIntent() != null) {
            Uri data = getIntent().getData();
            if (data != null) {
                intent.setData(data);
            }
        }
        startActivityForResult(intent, 1);
    }

    //是否是授权登录
    public boolean isAuthLogin() {
        if (getIntent() != null) {
            Uri data = getIntent().getData();
            if (data != null) {
                String path = data.getPath();
                if (path != null && path.contains("authLogin")) {
                    String type = data.getQueryParameter("type");
                    if (ToolUtil.getString(type).equals("login"))
                        return true;
                }
            }
        }
        return false;
    }

    public void startActivity(Class<?> cls, boolean islogin) {
        Intent intent = new Intent(this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void startActivityForResult(Class<?> cls, int requestCode) {

        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, requestCode);
    }

}
