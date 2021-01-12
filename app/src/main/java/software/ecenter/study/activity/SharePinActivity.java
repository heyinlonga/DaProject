package software.ecenter.study.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.R;
import software.ecenter.study.View.SchoolPopWindow;
import software.ecenter.study.View.SharePopWindow;
import software.ecenter.study.bean.MatchShareBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.BitmapUtils;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.Share;
import software.ecenter.study.utils.ShareUtil;
import software.ecenter.study.utils.TimeUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * Message 拼读 分享页面
 * Create by Administrator
 * Create by 2019/10/31
 */
public class SharePinActivity extends BaseActivity implements SchoolPopWindow.MItemSelectListener {
    @BindView(R.id.ll_top)
    LinearLayout ll_top;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_share)
    TextView tv_share;
    @BindView(R.id.tv_data)
    TextView tv_data;
    @BindView(R.id.tv_num)
    TextView tv_num;
    @BindView(R.id.ll_share)
    LinearLayout ll_share;
    private SharePopWindow sharePopWindow;
    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharepin
        );
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        String month = getIntent().getStringExtra("month");
        int score = getIntent().getIntExtra("score", 0);
        sharePopWindow = new SharePopWindow(mContext);
        sharePopWindow.setItemSelectListener(this);
        sharePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                finish();
            }
        });
        tv_data.setText("练习日期 " + month);
        tv_num.setText("平均" + score + "分");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharePopWindow.showAtLocation(ll_top, Gravity.BOTTOM, 0, 0);
            }
        }, 500);
    }

    @OnClick({R.id.iv_back, R.id.tv_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_share://分享
                sharePopWindow.showAtLocation(ll_top, Gravity.BOTTOM, 0, 0);
                break;
        }
    }


    String localPath;

    @Override
    public void onItemClick(int position) {
        Share share = new Share();
        localPath = BitmapUtils.saveBitmap(SharePinActivity.this, BitmapUtils.loadBitmapFromView(ll_share), System.currentTimeMillis() + ".png");
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
