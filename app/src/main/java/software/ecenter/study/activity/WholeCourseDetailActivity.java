package software.ecenter.study.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.util.Util;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import software.ecenter.study.R;
import software.ecenter.study.View.ExpandableTextView;
import software.ecenter.study.bean.CourseResBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.GlideCircleTransform;
import software.ecenter.study.utils.ToastUtils;

/**
 * Message 课程详情
 * Create by Administrator
 * Create by 2019/8/15
 */
public class WholeCourseDetailActivity extends BaseActivity implements View.OnClickListener {
    private ExpandableTextView expandableTextView;
    private ImageView iv_back;
    private TextView tv_title;
    private ImageView iv_icon;
    private ImageView iv_play;
    private ImageView iv_head;
    private TextView tv_name;
    private TextView tv_gold;
    private LinearLayout ll_oldPric;
    private TextView tv_pay;
    private WebView webview;
    private String id;
    private boolean isBuy;
    private boolean isHaveFile;
    private long resourceId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholecoursedetail);
        id = getIntent().getStringExtra("id");
        initView();

        getData();
    }

    //获取数据
    private void getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getlectureResource(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        CourseResBean bean = new Gson().fromJson(response, CourseResBean.class);
                        if (bean != null) {
                            CourseResBean.DataBean data = bean.getData();
                            if (data != null) {
                                if (Util.isOnMainThread()) {
                                    if (!isFinishing()) {
                                        Glide.with(mContext).load(data.getImgUrl()).error(R.drawable.morenshu).into(iv_icon);
                                        Glide.with(mContext).load(data.getTeacherPhoto()).transform(new CenterCrop(mContext), new GlideCircleTransform(mContext)).error(R.drawable.morentx).into(iv_head);//用户头像
                                    }
                                }
                                tv_title.setText(data.getName());
                                tv_name.setText(data.getName());
                                tv_gold.setText(data.getCoinPrice() + "元宝");
                                isBuy = data.isBuy();
                                isHaveFile = data.isHaveFile();
                                showBuyLay();
                                expandableTextView.setText(data.getDescription());
                                String html = "<html><header>" + css + "</header>" + data.getTeacherDescription() + "</html>";
                                webview.loadDataWithBaseURL(null,html , "text/html", "utf-8", null);
                                resourceId = data.getResourceId();
                            }
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });

    }
    //购买按钮
    private void showBuyLay() {
        if (isBuy){
            tv_pay.setText("播放");
            ll_oldPric.setVisibility(View.INVISIBLE);
            if (!isHaveFile){
                tv_pay.setBackgroundResource(R.drawable.background_gray_shape_circle);
            }
        }
    }
    public static String css = "<style type=\"text/css\"> img {" +
            "max-width: 100% !important;" +//限定图片宽度填充屏幕
            "height:auto !important;" +//限定图片高度自动
            "}" +
            "body {" +
            "margin-right:10px;" +//限定网页中的文字右边距为15px(可根据实际需要进行行管屏幕适配操作)
            "margin-left:10px;" +//限定网页中的文字左边距为15px(可根据实际需要进行行管屏幕适配操作)
            "margin-top:10px;" +//限定网页中的文字上边距为15px(可根据实际需要进行行管屏幕适配操作)
            "font-size:15px;" +//限定网页中文字的大小为40px,请务必根据各种屏幕分辨率进行适配更改
            "color:#723600;" +//限定网页中文字的大小为40px,请务必根据各种屏幕分辨率进行适配更改
            "word-wrap:break-word;" +//允许自动换行(汉字网页应该不需要这一属性,这个用来强制英文单词换行,类似于word/wps中的西文换行)
            "}" +
            "</style>";
    //初始化控件
    private void initView() {
        ll_oldPric = findViewById(R.id.ll_oldPric);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        iv_icon = findViewById(R.id.iv_icon);
        iv_play = findViewById(R.id.iv_play);
        tv_name = findViewById(R.id.tv_name);
        tv_gold = findViewById(R.id.tv_gold);
        tv_pay = findViewById(R.id.tv_pay);
        expandableTextView = findViewById(R.id.expandable_text);
        iv_head = findViewById(R.id.iv_head);
        webview = findViewById(R.id.webview);
        iv_play.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        tv_pay.setOnClickListener(this);
        webview.setBackgroundColor(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.tv_pay://支付
                if (resourceId == 0) {
                    return;
                }
                if (isBuy) {
                    if (isHaveFile) {
                        //进入资源
                        Intent intent = new Intent(mContext, SeeResourcesVideoActivity.class);
                        intent.putExtra("resourceId", String.valueOf(resourceId));
                        intent.putExtra("type", 1);
                        startActivity(intent);
                    }else {
                        ToastUtils.showToastSHORT(mContext,"讲座还未开始，敬请期待");
                    }
                } else {
                    Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                    intent.putExtra("Id", String.valueOf(id));
                    intent.putExtra("buyType", "10"); //10讲座资源
                    startActivityForResult(intent, 1);
                }
                break;
        }
    }

    //购买成功
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == 1 && resultCode == 1) {
            isBuy = true;
            showBuyLay();
        }
    }
}
