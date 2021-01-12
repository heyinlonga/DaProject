package software.ecenter.study.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.style.ImageSpan;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.Adapter.ExerciseExerciseAdapter;
import software.ecenter.study.R;
import software.ecenter.study.View.TextHtml.LinkMovementMethodExt;
import software.ecenter.study.View.TextHtml.MImageGetter;
import software.ecenter.study.View.TextHtml.MessageSpan;
import software.ecenter.study.bean.HuoDongBean.ActivityItemDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.MyWebViewClient;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/***
 * dec 活动详情
 */

public class HuoDongDetailActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.rl_tougao)
    RelativeLayout rlTougao;
    @BindView(R.id.btn_tougao)
    Button btnTougao;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.activity_image)
    ImageView activityImage;
    @BindView(R.id.activity_text)
    WebView activityText;

    private String activityId;
    private String activityType;

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
        setContentView(R.layout.activity_huo_dong_detail);
        ButterKnife.bind(this);
        activityId = getIntent().getStringExtra("activityId");
        activityType = getIntent().getStringExtra("activityType");
        if ("1".equals(activityType)) { //普通
            rlTougao.setVisibility(View.GONE);
        } else {
            rlTougao.setVisibility(View.VISIBLE);
        }
        getData();
    }


    public void lookPic(String url) {

        Intent intent = new Intent(mContext, BigPicActivity.class);
        intent.putExtra("ImageUrl", url);
        startActivity(intent);

    }

    public void getData() {
        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("activityId", activityId);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getActivityDetail(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.GONE);
                        ActivityItemDetailBean bean = ParseUtil.parseBean(response, ActivityItemDetailBean.class);
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

    public void setResponseView(ActivityItemDetailBean bean) {
        Glide.with(mContext).load(bean.getData().getActivityImage()).into(activityImage);
        titleContent.setText(bean.getData().getActivityTitle());//标题
//        activityText.setMovementMethod(LinkMovementMethodExt.getInstance(handler, ImageSpan.class));//富文本
//        activityText.setText(Html.fromHtml(bean.getData().getActivityContent(), new MImageGetter(activityText, this), null));
        showWebViewByContent(activityText, bean.getData().getActivityContent());

    }

    @OnClick({R.id.btn_left_title, R.id.btn_tougao, R.id.btn_refresh_net})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_refresh_net:
                getData();
                break;
            case R.id.btn_tougao:
                Intent intent = new Intent();
                if ("2".equals(activityType) || "3".equals(activityType)) { //教师//易错题
                    intent.setClass(mContext, ErrorSubmissionActivity.class);
                    intent.putExtra("activityId", activityId);
                    intent.putExtra("activityType", activityType);
                } else if ("4".equals(activityType)) { //作文
                    intent.setClass(mContext, CompositionSubmissionActivity.class);
                    intent.putExtra("activityId", activityId);
                }
                startActivity(intent);
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
