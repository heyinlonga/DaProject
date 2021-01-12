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
 * Message  分享页面
 * Create by Administrator
 * Create by 2019/10/31
 */
public class ShareMatchActivity extends BaseActivity implements SchoolPopWindow.MItemSelectListener {
    @BindView(R.id.ll_top)
    LinearLayout ll_top;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_share)
    TextView tv_share;
    @BindView(R.id.tv_data)
    TextView tv_data;
    @BindView(R.id.tv_tiNum)
    TextView tv_tiNum;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.ll_share)
    LinearLayout ll_share;
    private SharePopWindow sharePopWindow;
    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharematch);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        sharePopWindow = new SharePopWindow(mContext);
        sharePopWindow.setItemSelectListener(this);
        sharePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                finish();
            }
        });
        getData();
    }

    //获取分享数据
    private void getData() {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("id", id);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getShare(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        MatchShareBean bean = ParseUtil.parseBean(response, MatchShareBean.class);
                        setView(bean);
                        sharePopWindow.showAtLocation(ll_top, Gravity.BOTTOM, 0, 0);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    //设置数据
    @SuppressLint("StringFormatMatches")
    private void setView(MatchShareBean bean) {
        if (bean != null) {
            MatchShareBean.DataBean data = bean.getData();
            if (data != null) {
                tv_data.setText("参赛日期 " + data.getDate());
                tv_tiNum.setText(data.getCorrectNum() + "/" + data.getTotalNum() + "题");
                tv_time.setText(TimeUtil.getTimeLimit(data.getTimeCost()));
                StringBuilder str = new StringBuilder();
                str.append("我参加了“");
                str.append(data.getMatchName());
                str.append("”");
                String title = data.getTitle();
                String prizeName = data.getPrizeName();
                if (!TextUtils.isEmpty(title)) {//称谓
                    String text = String.format(getResources().getString(R.string.share_thr), data.getMatchName(), title);
                    int i = text.lastIndexOf(title);
                    SpannableStringBuilder builder = new SpannableStringBuilder(text);
                    ForegroundColorSpan startColor = new ForegroundColorSpan(getResources().getColor(R.color.color_0099ff));
                    builder.setSpan(startColor, i, i + title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_content.setText(builder);
                } else if (!TextUtils.isEmpty(prizeName)) {//奖项
                    String text = String.format(getResources().getString(R.string.share_two), data.getMatchName(), prizeName);
                    int i = text.lastIndexOf(prizeName);
                    SpannableStringBuilder builder = new SpannableStringBuilder(text);
                    ForegroundColorSpan startColor = new ForegroundColorSpan(getResources().getColor(R.color.color_0099ff));
                    builder.setSpan(startColor, i, i + prizeName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_content.setText(builder);
                } else {
                    tv_content.setText(String.format(getResources().getString(R.string.share_one), data.getMatchName()));
                }
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_share://分享
//                sharePopWindow.showAtLocation(ll_top, Gravity.BOTTOM, 0, 0);
                break;
        }
    }


    private void keepBitmap() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(localPath)) {
                    ToastUtils.showToastSHORT(mContext, "保存成功：" + localPath);
                    //发送广播通知系统图库刷新数据
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(localPath))));
                }
            }
        }, 500);
    }

    String localPath;
    @Override
    public void onItemClick(int position) {
        Share share = new Share();
        localPath = BitmapUtils.saveBitmap(ShareMatchActivity.this, BitmapUtils.loadBitmapFromView(ll_share), System.currentTimeMillis() + ".png");
        share.setUrl(localPath);
        switch (position) {
            case 1://朋友圈
                share.setType(1);
                ShareUtil.imageShare(this,share);
                break;
            case 2://微信好友
                share.setType(2);
                ShareUtil.imageShare(this,share);
                break;
            case 3://qq
                ShareUtil.shareImgToQQ(this,share);
                break;
            case 4://qq空间
                ShareUtil.shareImgToQQzone(this,share);
                break;
        }
    }
}
