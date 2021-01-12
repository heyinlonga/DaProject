package software.ecenter.study.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.R;
import software.ecenter.study.bean.HuoDongBean.ActivityItemDetailBean;
import software.ecenter.study.bean.QualityEducationUrlBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.service.UpLoadIntentService;
import software.ecenter.study.utils.MyWebViewClient;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * 加载 活动链接
 */
@SuppressLint("SetTextI18n")
public class WebActivity extends AppCompatActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    public String urlPath;
    private String activityId;
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) {
            urlPath = getIntent().getStringExtra("url");
            activityId = getIntent().getStringExtra("activityId");
            String title = "";
            title = getIntent().getStringExtra("fuwuxieyi");
            if (!TextUtils.isEmpty(title)) {//服务协议
                titleContent.setText(title + "");
                webview.loadUrl(urlPath);
            } else {//活动
                getData();
            }
        } else {
            String title = getIntent().getStringExtra("title");
            titleContent.setText(title + "");
            activityId = getIntent().getStringExtra("id");
            getDataQulityEducation();
        }


        WebSettings webSettings = webview.getSettings();
        webview.addJavascriptInterface(this, "android");//添加js监听 这样html就能调用客户端
        webview.setWebChromeClient(webChromeClient);
        webview.setWebViewClient(webViewClient);

        webSettings.setJavaScriptEnabled(true);//允许使用js

        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

        //不显示webview缩放按钮
//        webSettings.setDisplayZoomControls(false);
    }

    private void getDataQulityEducation() {
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("id", activityId);

        new RetroFactory(this, RetroFactory.getRetroFactory(this).viewQualityEducation(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {

                    @Override
                    public void onSuccess(String response) {
                        QualityEducationUrlBean bean = ParseUtil.parseBean(response, QualityEducationUrlBean.class);
                        if (bean != null) {
                            QualityEducationUrlBean.DataBean data = bean.getData();
                            if (data != null) {
                                webview.loadUrl(data.getUrl());
                            }
                        }

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(WebActivity.this, msg);
                    }

                });
    }

    public void getData() {

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("activityId", activityId);

        new RetroFactory(this, RetroFactory.getRetroFactory(this).getActivityDetail(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {

                    @Override
                    public void onSuccess(String response) {
                        ActivityItemDetailBean bean = ParseUtil.parseBean(response, ActivityItemDetailBean.class);
                        if (bean != null) {
                            ActivityItemDetailBean.Data data = bean.getData();
                            if (data != null) {
                                titleContent.setText(data.getActivityTitle() + "");
//                                webview.loadUrl("https://gif.sina.com.cn/");
                                webview.loadUrl(data.getActivityUrl());
//                                webview.loadDataWithBaseURL("", data.getActivityContent(), "text/html", "utf-8", null);
//                                showWebViewByContent(webview,data.getActivityContent());
                            }
                        }

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(WebActivity.this, msg);
                    }

                });
    }
    private void showWebViewByContent(WebView webView, String content) {
        String html = "<html><header>" + ToolUtil.css + "</header>" + ToolUtil.getString(content,"暂无") + "</html>";
        webView.loadDataWithBaseURL("", html, "text/html", "utf-8", null);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.addJavascriptInterface(new WebActivity.JsCallJavaObj() {
            @JavascriptInterface
            @Override
            public void showBigImg(String url) {
                lookPic(url);
            }
        }, "jsCallJavaObj");

    }

    public void lookPic(String url) {

        Intent intent = new Intent(WebActivity.this, BigPicActivity.class);
        intent.putExtra("ImageUrl", url);
        startActivity(intent);

    }
    /**
     * Js調用Java接口
     */
    private interface JsCallJavaObj {
        void showBigImg(String url);
    }

    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            progressbar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                if (!url.startsWith("http")) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(urlPath));
//                    intent.setClassName("com.android.browser",
//                            "com.android.browser.BrowserActivity");
//                    startActivity(intent);
                    if (webview.canGoBack())
                        webview.goBack();
                    return true;
                }
            } catch (Exception e) {
                if (webview.canGoBack())
                    webview.goBack();
                return true;
            }
            webview.loadUrl(url);
            return true;


   /*         if(url.equals("http://www.google.com/")){
                Toast.makeText(WebActivity.this,"国内不能访问google,拦截该url",Toast.LENGTH_LONG).show();
                return true;//表示我已经处理过了
            }*/
//            return super.shouldOverrideUrlLoading(view, url);
        }

    };

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient() {
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定", null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();

            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);

        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressbar.setProgress(newProgress);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (webview.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {//点击返回按钮的时候判断有没有上一页
            webview.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * JS调用android的方法
     *
     * @param str
     * @return
     */
    @JavascriptInterface //仍然必不可少
    public void getClient(String str) {
        //html调用客户端
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //释放资源
        webview.destroy();
        webview = null;
    }

    @OnClick(R.id.btn_left_title)
    public void onViewClicked() {
        onBackPressed();
    }
}
