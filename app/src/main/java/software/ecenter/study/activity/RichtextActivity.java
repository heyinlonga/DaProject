package software.ecenter.study.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.R;
import software.ecenter.study.View.TextHtml.LinkMovementMethodExt;
import software.ecenter.study.View.TextHtml.MImageGetter;
import software.ecenter.study.View.TextHtml.MessageSpan;
import software.ecenter.study.utils.MyWebViewClient;

/**
 * Created by zyt on 2018/6/4.
 */

public class RichtextActivity extends BaseActivity {
    @BindView(R.id.richtext_text)
    TextView textView;
    @BindView(R.id.title_content)
    TextView tvtitle;
    @BindView(R.id.btn_left_title)
    ImageView ivLeftTitle;
    @BindView(R.id.richtext_webvie)
    WebView richtext_webvie;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.richtext_activity);
        ButterKnife.bind(this);
        Intent intent = this.getIntent();
        String content = (String) intent.getSerializableExtra("bean");
        String title = (String) intent.getSerializableExtra("title");
        tvtitle.setText(title + "");//标题
//        textView.setMovementMethod(LinkMovementMethodExt.getInstance(handler, ImageSpan.class));//富文本
//        textView.setText(Html.fromHtml(content + "", new MImageGetter(textView, this), null));
        showWebViewByContent(richtext_webvie, content);

    }

    public void lookPic(String url) {

        Intent intent = new Intent(mContext, BigPicActivity.class);
        intent.putExtra("ImageUrl", url);
        startActivity(intent);

    }

    @OnClick({R.id.btn_left_title})
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
        }
    }
    private void showWebViewByContent(WebView webView, String content) {
        webView.loadDataWithBaseURL("", content, "text/html", "utf-8", null);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.addJavascriptInterface(new JsCallJavaObj() {
            @JavascriptInterface
            @Override
            public void showBigImg(String url) {
                lookPic(url);
            }
        }, "jsCallJavaObj");
    }

    /**
     * Js調用Java接口
     */
    private interface JsCallJavaObj {
        void showBigImg(String url);
    }
}
