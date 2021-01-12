package software.ecenter.study.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.style.ImageSpan;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.Adapter.ExerciseAnswerAdapter;
import software.ecenter.study.R;
import software.ecenter.study.View.TextHtml.LinkMovementMethodExt;
import software.ecenter.study.View.TextHtml.MImageGetter;
import software.ecenter.study.View.TextHtml.MessageSpan;
import software.ecenter.study.bean.HomeBean.ExerciseAnswerBean;
import software.ecenter.study.bean.HomeBean.ExerciseBean;
import software.ecenter.study.bean.HomeBean.ExerciseDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.MyWebViewClient;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/***
 * dsc 在线练习
 *
 * */
public class ExerciseActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.list_answer)
    RecyclerView listAnswer;
    @BindView(R.id.btn_Previous)
    Button btnPrevious;
    @BindView(R.id.btn_Next)
    Button btnNext;
    @BindView(R.id.exercise_tip)
    TextView exerciseTip;
    @BindView(R.id.exercise_title)
    WebView exerciseTitle;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;

    private ExerciseAnswerAdapter exerciseAnswerAdapter;
    private List<ExerciseAnswerBean> listData = new ArrayList<>();


    private ExerciseDetailBean mExerciseDetailBean;
    private String resourceId;
    private String difficultyLevel;
    private long beginTime;
    private int curPosion; //当前第几题
    private List<ExerciseBean> exerciseList; //题目集合
    private ExerciseBean curExerciseBean;//当前的题目

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
        setContentView(R.layout.activity_exercise);
        ButterKnife.bind(this);
        resourceId = getIntent().getStringExtra("resourceId");
        difficultyLevel = getIntent().getStringExtra("difficultyLevel");


        //解决ScrollView中嵌入一个或多个RecyclerView页面滑动卡顿
//        listAnswer.setHasFixedSize(true);
//        listAnswer.setNestedScrollingEnabled(false);

        GridLayoutManager manager = new GridLayoutManager(mContext, 1){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        listAnswer.setLayoutManager(manager);

//        exerciseTitle.setMovementMethod(LinkMovementMethodExt.getInstance(handler, ImageSpan.class));

        getAllData();
    }

    public void getAllData() {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("resourceId", resourceId);
            json.put("difficultyLevel", difficultyLevel);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getResourceExercise(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.GONE);
                        ExerciseDetailBean bean = ParseUtil.parseBean(response, ExerciseDetailBean.class);
                        mExerciseDetailBean = bean;
                        setResponseView(curPosion);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });


    }

    public void setResponseView(int index) {
        if (mExerciseDetailBean == null) {
            return;
        }

        //当前的
        curPosion = index;
        exerciseList = mExerciseDetailBean.getData().getExerciseList();
        if (curPosion == 0) {
            btnPrevious.setVisibility(View.GONE);
        } else {
            btnPrevious.setVisibility(View.VISIBLE);
        }
        if (exerciseList.size() < 1) {
            return;
        }
        if ((exerciseList.size() - 1) == curPosion) {
            btnNext.setText("提交");
        } else {
            btnNext.setText("下一题");
        }
        //回到顶部
        scrollView.scrollTo(0, 0);

        curExerciseBean = exerciseList.get(index);


        if (exerciseList != null && exerciseList.size() > 0) {
//            exerciseTitle.setText(Html.fromHtml(curExerciseBean.getExerciseTitle(), new MImageGetter(exerciseTitle, this), null));
            showWebViewByContent();
            if (exerciseAnswerAdapter == null) {
                listData = curExerciseBean.getExerciseAnswer();
                exerciseAnswerAdapter = new ExerciseAnswerAdapter(mContext, listData, 0);
                exerciseAnswerAdapter.setItemClickListener(new ExerciseAnswerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        CheckItem(position);
                    }
                });
                listAnswer.setAdapter(exerciseAnswerAdapter);
            } else {
                listData = exerciseList.get(index).getExerciseAnswer();
                exerciseAnswerAdapter.refreshData(listData, 0);
            }


        }
    }

    public void CheckItem(int position) {
        //（1、单选，2、多选，3、判断）
        if (curExerciseBean.getExerciseType() == 1 || curExerciseBean.getExerciseType() == 3) {//单选
            for (ExerciseAnswerBean item : listData) { //全部置为未选中
                item.setAnswerIsRight(false);
            }
            listData.get(position).setAnswerIsRight(listData.get(position).isAnswerIsRight() ? false : true); //然后再选择一个

        } else if (curExerciseBean.getExerciseType() == 2) {
            listData.get(position).setAnswerIsRight(listData.get(position).isAnswerIsRight() ? false : true);
        }
        exerciseAnswerAdapter.refreshData(listData, 0);
    }


    @OnClick({R.id.btn_left_title, R.id.btn_Previous, R.id.btn_Next, R.id.btn_refresh_net})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                finish();
                break;
            case R.id.btn_Previous: //上一题
                if (curPosion >= 1) {
                    curPosion--;
                    setResponseView(curPosion);
                } else {
                    ToastUtils.showToastSHORT(mContext, "已经是第一题了");
                }

                break;
            case R.id.btn_Next: //下一题 or 提交
                //nextExerCise();
                if (curPosion < exerciseList.size() - 1) {
                    curPosion++;
                    setResponseView(curPosion);
                } else if (curPosion == exerciseList.size() - 1) {
                    //遍历选择，提交
                    String answer = "";
                    for (int j = 1; j <= exerciseList.size(); j++) {
                        ExerciseBean item = exerciseList.get(j - 1);//获取题
                        boolean hasRightAnswer = false;
                        for (int i = 1; i <= item.getExerciseAnswer().size(); i++) { //按第几个答案提交给后台
                            if (item.getExerciseAnswer().get(i - 1).isAnswerIsRight()) {
                                hasRightAnswer = true; //这题有选择
                                answer += item.getExerciseAnswer().get(i - 1).getAnswerId() + ",";
                            }
                        }
                        if (!hasRightAnswer) {
                            ToastUtils.showToastSHORT(mContext, "第" + j + "题没有选择答案");
                            return;
                        }
                        if (answer.endsWith("，")) { //如果以逗号结尾，那么换成分号，分号是用来分割题的
                            answer = answer.substring(0, answer.length() - 1) + ";";
                        } else {
                            answer += ";";
                        }
                    }

                    if (answer.endsWith(";")) {
                        answer = answer.substring(0, answer.length() - 1);
                    }
                    //提交答案
                    ExerciseSubmit(answer);

                }
                break;
            case R.id.btn_refresh_net:
                getAllData();
                break;
        }
    }

    /**
     * 提交答案
     *
     * @param answer 答案集合
     */
    public void ExerciseSubmit(String answer) {

        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("resourceId", resourceId);
            json.put("difficultyLevel", difficultyLevel);
            json.put("answerList", answer);
            json.put("beginTime", mExerciseDetailBean.getData().getBeginTime());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).resourceExerciseSubmit(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();

                        ExerciseDetailBean bean = ParseUtil.parseBean(response, ExerciseDetailBean.class);
                        Intent intent = new Intent(mContext, ExerciseExerciseActivity.class);
                        intent.putExtra("ExerciseDetailBean", bean);
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();

                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });

    }

    private void showWebViewByContent() {
        exerciseTitle.loadDataWithBaseURL("", curExerciseBean.getExerciseTitle(), "text/html", "utf-8", null);
        exerciseTitle.getSettings().setJavaScriptEnabled(true);
        exerciseTitle.setWebViewClient(new MyWebViewClient());
        exerciseTitle.addJavascriptInterface(new JsCallJavaObj() {
            @JavascriptInterface
            @Override
            public void showBigImg(String url) {
                lookPic(url);
            }
        }, "jsCallJavaObj");
    }

    public void lookPic(String url) {

        Intent intent = new Intent(mContext, BigPicActivity.class);
        intent.putExtra("ImageUrl", url);
        startActivity(intent);

    }

    /**
     * Js調用Java接口
     */
    private interface JsCallJavaObj {
        void showBigImg(String url);
    }
}
