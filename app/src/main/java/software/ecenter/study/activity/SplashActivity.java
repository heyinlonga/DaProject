package software.ecenter.study.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import software.ecenter.study.Interface.OnItemListener;
import software.ecenter.study.R;
import software.ecenter.study.tool.FileManager;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.StatusBarUtil;
import software.ecenter.study.utils.StorageUtil;
import software.ecenter.study.utils.ToolUtil;

/**
 * Created by zyt on 2018/7/16.
 * 启动页
 */

public class SplashActivity extends BaseActivity {
    private boolean isFirstOpen = true;

    @Override
    protected void setStatubarColor() {
        StatusBarUtil.StatusBarLightMode(this, R.color.transparent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        isFirstOpen = AccountUtil.getFirstApp(mContext);
        String ysState = StorageUtil.getSetting(mContext, AccountUtil.YSSTATE);
        if (ToolUtil.getString(ysState).equals("1")) {
            showGoning();
        } else {
            ToolUtil.showYSDialog(this, new OnItemListener() {
                @Override
                public void onCancle() {
                    finish();
                }

                @Override
                public void onConfig() {
                    StorageUtil.saveSetting(mContext, AccountUtil.YSSTATE, "1");
                    showGoning();
                }
            });
        }
        try {
            //删除上一次文件缓存目录
            ToolUtil.deleteDir(FileManager.getInstance(mContext).getTempDir() + File.separator + "documentTempDir");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "删除文件异常");
        }

    }

    private void showGoning() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (isFirstOpen) {
                    startActivity(WelcomeGuidActivity.class);
                    finish();
                    return;
                }

                startActivity(LogingActivity.class);
                finish();
            }
        }, 2000);
    }
}
