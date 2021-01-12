package software.ecenter.study.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.R;
import software.ecenter.study.View.TextHtml.LinkMovementMethodExt;
import software.ecenter.study.View.TextHtml.MImageGetter;
import software.ecenter.study.View.TextHtml.MessageSpan;
import software.ecenter.study.bean.HomeBean.BannerDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.MyWebViewClient;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ScreenUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * dec 广告详情
 */
public class BannerDetailActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.banner_image)
    ImageView bannerImage;
    /* @BindView(R.id.banner_Content)
     TextView bannerContent;*/
    @BindView(R.id.wb_banner_content)
    WebView wvBannerContent;

    private String bannerId;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == 200) {
                MessageSpan ms = (MessageSpan) msg.obj;
                Object[] spans = (Object[]) ms.getObj();
                // final ArrayList<String> list = new ArrayList<>();
                for (Object span : spans) {
                    if (span instanceof ImageSpan) {
                        // Log.i("picUrl==", ((ImageSpan) span).getSource());
                        lookPic(((ImageSpan) span).getSource());
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_detail);
        ButterKnife.bind(this);

        bannerId = getIntent().getStringExtra("id");
        ViewGroup.LayoutParams layoutParams = bannerImage.getLayoutParams();
        layoutParams.height = ScreenUtil.getScreenWidth(mContext)*270/710;
        bannerImage.setLayoutParams(layoutParams);
        getData(bannerId);

    }

    public void setResponseView(BannerDetailBean bean) {
        Glide.with(mContext).load(bean.getData().getBannerImage()).into(bannerImage);
       /* bannerContent.setMovementMethod(LinkMovementMethodExt.getInstance(handler, ImageSpan.class));//富文本
        bannerContent.setText(Html.fromHtml(bean.getData().getBannerContent(), new MImageGetter(bannerContent, this), null));
     */
//        wvBannerContent.loadDataWithBaseURL(null, bean.getData().getBannerContent(), "text/html", "utf-8", null);
        showWebViewByContent(wvBannerContent, bean.getData().getBannerContent());
        titleContent.setText(bean.getData().getBannerTitle());

    }

    public void getData(String bannerId) {
        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("bannerId", bannerId);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).homeBannerDetai(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.GONE);
                        BannerDetailBean bean = ParseUtil.parseBean(response, BannerDetailBean.class);
                        setResponseView(bean);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }


    @OnClick({R.id.btn_left_title, R.id.btn_refresh_net})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_refresh_net:
                getData(bannerId);
                break;
        }
    }

    public void lookPic(String url) {

        Intent intent = new Intent(mContext, BigPicActivity.class);
        intent.putExtra("ImageUrl", url);
        startActivity(intent);

    }

    private void showWebViewByContent(WebView webView, String content) {
        String html = "<html><header>" + css + "</header>" + content + "</html>";
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.addJavascriptInterface(new JsCallJavaObj() {
            @JavascriptInterface
            @Override
            public void showBigImg(String url) {
                lookPic(url);
            }
        }, "jsCallJavaObj");
        webView.loadDataWithBaseURL("", html, "text/html", "utf-8", null);

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
            "word-wrap:break-word;" +//允许自动换行(汉字网页应该不需要这一属性,这个用来强制英文单词换行,类似于word/wps中的西文换行)
            "}" +
            "</style>";
    /**
     * Js調用Java接口
     */
    private interface JsCallJavaObj {
        void showBigImg(String url);
    }


}
