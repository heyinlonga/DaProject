package software.ecenter.study.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.R;
import software.ecenter.study.View.TextHtml.LinkMovementMethodExt;
import software.ecenter.study.View.TextHtml.MImageGetter;
import software.ecenter.study.View.TextHtml.MessageSpan;
import software.ecenter.study.bean.MineBean.MessageDetailBetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.MyWebViewClient;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * dec 消息详情
 */
public class MessageDetailActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.message_webview)
    WebView webView;
    @BindView(R.id.ll_top)
    LinearLayout ll_top;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_content)
    TextView tv_content;

    private String messageId; //消息id
    private boolean isAll; //是否群发
    private MessageDetailBetailBean mMessageDetailBetailBean;

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
        setContentView(R.layout.activity_message_detail);
        ButterKnife.bind(this);
        messageId = getIntent().getStringExtra("messageId");
        isAll = getIntent().getBooleanExtra("isall", false);
        webView.setBackgroundColor(0);
        messageDetail();
    }


    public void messageDetail() {
        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("messageId", messageId);
        JSONObject json = new JSONObject();
        int curMessageId = Integer.parseInt(messageId);
        if (curMessageId == -1)
            return;
        try {
            json.put("messageId", curMessageId);
            json.put("isBatch", isAll);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).messageDetail(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();

                        mMessageDetailBetailBean = ParseUtil.parseBean(response, MessageDetailBetailBean.class);
                        initView();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);


                    }

                });

    }

    public void initView() {
        if (mMessageDetailBetailBean == null) {
            return;
        }
        if (ToolUtil.getString(mMessageDetailBetailBean.getData().getType()).equals("1")
                &&ToolUtil.getString(mMessageDetailBetailBean.getData().getMessageTitle()).equals("反馈回复")) {
            ll_top.setVisibility(View.VISIBLE);
            titleContent.setText(mMessageDetailBetailBean.getData().getMessageTitle()+"");
            tv_title.setText(mMessageDetailBetailBean.getData().getMessageHead()+"");
            tv_content.setText(mMessageDetailBetailBean.getData().getFeedbackProblems()+"");
            showWebViewByContent(webView, mMessageDetailBetailBean.getData().getOpinion());
        } else {
            titleContent.setText(mMessageDetailBetailBean.getData().getMessageTitle()+"");
            showWebViewByContent(webView, mMessageDetailBetailBean.getData().getMessageContext());
        }
    }

    @OnClick(R.id.btn_left_title)
    public void onViewClicked() {
        onBackPressed();
    }

    public void lookPic(String url) {

        Intent intent = new Intent(mContext, BigPicActivity.class);
        intent.putExtra("ImageUrl", url);
        startActivity(intent);

    }

    private void showWebViewByContent(WebView webView, String content) {
//        webView.loadDataWithBaseURL("", content, "text/html", "utf-8", null);
        String html = "<html><header>" + css + "</header>" + content + "</html>";
        webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
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

    /**
     * Js調用Java接口
     */
    private interface JsCallJavaObj {
        void showBigImg(String url);
    }
}
