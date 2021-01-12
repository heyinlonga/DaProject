package software.ecenter.study.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;


import software.ecenter.study.R;
import software.ecenter.study.View.SchoolPopWindow;
import software.ecenter.study.View.SharePopWindow;
import software.ecenter.study.utils.BitmapUtils;
import software.ecenter.study.utils.PermissionUtils;
import software.ecenter.study.utils.Share;
import software.ecenter.study.utils.ShareUtil;

/**
 * Message
 * Create by Administrator
 * Create by 2019/12/12
 */
public class AppShareActivity extends BaseActivity implements SchoolPopWindow.MItemSelectListener {
    private SharePopWindow sharePopWindow;
    private LinearLayout ll_share;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appshare);
        ll_share = findViewById(R.id.ll_share);
        getPermission();
        sharePopWindow = new SharePopWindow(mContext);
        sharePopWindow.setItemSelectListener(this);
        sharePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                finish();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharePopWindow.showAtLocation(ll_share, Gravity.BOTTOM, 0, 0);
            }
        }, 500);
    }

    /**
     * 获取权限
     */
    private void getPermission() {
        PermissionUtils.newIntence().requestPerssion(AppShareActivity.this, PERSSION_FILE, 101, new PermissionUtils.IPermission() {
            @Override
            public void success(int code) {
            }
        });
    }

    String localPath;
    //读取文件
    public static String[] PERSSION_FILE = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    public void onItemClick(int position) {
        Share share = new Share();
        localPath = BitmapUtils.saveBitmap(this, BitmapUtils.loadBitmapFromView(ll_share), System.currentTimeMillis() + ".png");
        share.setUrl(localPath);
        switch (position) {
            case 1://朋友圈
                share.setType(1);
                ShareUtil.imageShare(this, share);
                break;
            case 2://微信好友
                share.setType(2);
                ShareUtil.imageShare(this, share);
                break;
            case 3://qq
                ShareUtil.shareImgToQQ(this, share);
                break;
            case 4://qq空间
                ShareUtil.shareImgToQQzone(this, share);
                break;
        }
    }
}
