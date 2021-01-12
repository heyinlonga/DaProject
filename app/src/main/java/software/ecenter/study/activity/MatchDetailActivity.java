package software.ecenter.study.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.bean.TiBean;
import software.ecenter.study.R;
import software.ecenter.study.bean.Bean;
import software.ecenter.study.bean.MatchDetail;
import software.ecenter.study.bean.MatchDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ScreenUtil;
import software.ecenter.study.utils.TimeUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message 比赛详情
 * Create by Administrator
 * Create by 2019/10/28
 */
public class MatchDetailActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_guizhe)
    TextView tv_guizhe;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_enrollTime)
    TextView tv_enrollTime;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_AllTime)
    TextView tv_AllTime;
    @BindView(R.id.web_des)
    WebView web_des;
    @BindView(R.id.iv_icon)
    ImageView iv_icon;
    @BindView(R.id.ll_over)
    LinearLayout ll_over;
    @BindView(R.id.ll_djs)
    LinearLayout ll_djs;
    @BindView(R.id.tv_wdcj)
    TextView tv_wdcj;
    @BindView(R.id.tv_ckbd)
    TextView tv_ckbd;
    @BindView(R.id.tv_bao)
    TextView tv_bao;
    @BindView(R.id.tv_start)
    TextView tv_start;
    @BindView(R.id.tv_djs)
    TextView tv_djs;
    @BindView(R.id.tv_tishi)
    TextView tv_tishi;

    private String id;
    private boolean isEnrollEnd;
    private boolean isMatch;
    private boolean isEnroll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchdetail);
        ButterKnife.bind(this);
        web_des.setBackgroundColor(0);
        id = getIntent().getStringExtra("id");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    //获取数据
    public void getData() {
        if (TextUtils.isEmpty(id)) {
            return;
        }
//        if (!showNetWaitLoding()) {
//            return;
//        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("id", id);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getMatchDetail(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        MatchDetail bean = ParseUtil.parseBean(response, MatchDetail.class);
                        setView(bean);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    //设置数据
    @SuppressLint("SetTextI18n")
    private void setView(MatchDetail bean) {
        MatchDetail.DataBean data = bean.getData();
        if (data != null) {
            ViewGroup.LayoutParams layoutParams = iv_icon.getLayoutParams();
            layoutParams.width = ScreenUtil.getScreenWidth(this) - ScreenUtil.dip2px(mContext, 20);
            layoutParams.height = (ScreenUtil.getScreenWidth(this) - ScreenUtil.dip2px(mContext, 20)) * 332 / 710;
            iv_icon.setLayoutParams(layoutParams);
            try {
//                Glide.with(mContext).load(ToolUtil.getString(data.getImgUrl())).error(R.drawable.matchbg).into(iv_icon);
                Glide.with(mContext).load(ToolUtil.getString(data.getImgUrl())).error(R.drawable.matchbg).into(iv_icon);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tv_name.setText(ToolUtil.getString(data.getName()));
            tv_enrollTime.setText(ToolUtil.getString(data.getEnrollEndDate()));
            if (ToolUtil.getString(data.getMatchBeginDate()).equals(ToolUtil.getString(data.getMatchEndDate()))) {
                tv_time.setText(ToolUtil.getString(data.getMatchBeginDate()));
            } else {
                tv_time.setText(ToolUtil.getString(data.getMatchBeginDate()) + "至" + ToolUtil.getString(data.getMatchEndDate()));
            }
            tv_AllTime.setText(TimeUtil.getTimeLimit(ToolUtil.getLongValue(data.getTimeLimit())));
            String html = "<html><header>" + css + "</header>" + ToolUtil.getString(data.getDescription()) + "</html>";
            web_des.loadDataWithBaseURL("", html, "text/html", "utf-8", null);
            ll_over.setVisibility(View.GONE);
            ll_djs.setVisibility(View.GONE);
            tv_bao.setVisibility(View.GONE);
            tv_start.setVisibility(View.GONE);
            boolean isEnd = data.isIsEnd();
            isEnroll = data.isIsEnroll();
            isMatch = data.isMatch();
            if (isEnd) {//结束
                ll_over.setVisibility(View.VISIBLE);
                tv_wdcj.setVisibility(isEnroll && isMatch ? View.VISIBLE : View.GONE);
            } else {
                //是否到了比赛的时间
                long timeCha = TimeUtil.getTimeCha(TimeUtil.getTime(TimeUtil.NORMAL_PATTERN), data.getMatchBeginDate() + " 00:00:00");
                //是否报名截止时间
                isEnrollEnd = !data.isEndEnroll();
                if (timeCha > 0) {//没到比赛时间
                    if (isEnroll) {//已报名
                        ll_djs.setVisibility(View.VISIBLE);
                        tv_tishi.setText("已报名，请按时参赛。");
                        CountDownTimer countDownTimer = new CountDownTimer(timeCha, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                tv_djs.setText("距比赛开始：" + TimeUtil.getTimeMatch(millisUntilFinished));
                            }

                            @Override
                            public void onFinish() {
                                getData();
                            }
                        }.start();
                    } else {//未报名
                        tv_bao.setVisibility(View.VISIBLE);
                        tv_bao.setBackgroundResource(isEnrollEnd ? R.drawable.shape_f77450_20 : R.drawable.shape_a0a0a0_20);
                    }

                } else {//正在比赛
                    tv_start.setVisibility(View.VISIBLE);
                    if (isMatch) {//已参赛
                        tv_start.setText("已参赛");
                        tv_start.setBackgroundResource(R.drawable.shape_a0a0a0_20);
                    } else {
                        tv_start.setText("开始比赛");
                        tv_start.setBackgroundResource(isEnroll ? R.drawable.shape_0099ff_20 : R.drawable.shape_a0a0a0_20);
                    }
                }
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_guizhe, R.id.tv_start, R.id.tv_bao, R.id.tv_ckbd, R.id.tv_wdcj})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_guizhe://规则
                startActivity(new Intent(mContext, MatchGuiZheActivity.class));
                break;
            case R.id.tv_start://开始比赛
//                textmatch();
                //是否参赛
                if (!isMatch) {
                    if (isEnroll) {
                        startMatch();//开始比赛
                    } else {
                        ToastUtils.showToastSHORT(mContext, "报名才能参赛呦~，敬请关注下期比赛");
                    }
                }
                break;
            case R.id.tv_bao://报名
                if (isEnrollEnd) {
                    baoMing();
                } else {
                    ToastUtils.showToastSHORT(mContext, "报名已经截止，下次早点儿来报名呦~");
                }
                break;
            case R.id.tv_ckbd://查看榜单
                startActivity(new Intent(mContext, MatchRankActivity.class).putExtra("id", id));
                break;
            case R.id.tv_wdcj://我的成绩
                startActivity(new Intent(mContext, MeResultActivity.class).putExtra("id", id));
                break;
        }
    }

    private void textmatch() {
        List<TiBean> questionList = new ArrayList<>();
        TiBean tiBean = new TiBean();
        tiBean.setId("1");
        tiBean.setQuestion("测试1");
        List<String> opte = new ArrayList<>();
        opte.add("A、测试1");
        opte.add("B、测试1");
        opte.add("C、测试1");
        opte.add("D、测试1");
        tiBean.setOptions(opte);

        TiBean tiBean1 = new TiBean();
        tiBean1.setId("1");
        tiBean1.setQuestion("测试2");
        List<String> opte1 = new ArrayList<>();
        opte1.add("A、测试2");
        opte1.add("B、测试2");
        opte1.add("C、测试2");
        opte1.add("D、测试2");
        tiBean1.setOptions(opte1);

        questionList.add(tiBean);
        questionList.add(tiBean1);
        startActivityForResult(new Intent(mContext, MatchActivity.class)
                .putExtra("data", (Serializable) questionList)
                .putExtra("time", 60), 1);
    }

    //开始比赛
    private void startMatch() {
        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("id", id);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getBeginMatch(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        MatchDetailBean bean = ParseUtil.parseBean(response, MatchDetailBean.class);
                        if (bean != null) {
                            MatchDetailBean.DataBean data = bean.getData();
                            if (data != null) {
                                int timeLimit = data.getTimeLimit();
                                List<TiBean> questionList = data.getQuestionList();
                                startActivityForResult(new Intent(mContext, MatchActivity.class)
                                        .putExtra("id", id)
                                        .putExtra("data", (Serializable) questionList)
                                        .putExtra("time", timeLimit), 1);
                            } else {
                                ToastUtils.showToastSHORT(mContext, ToolUtil.getString(bean.getMessage()));
                            }
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    //报名
    private void baoMing() {
        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("id", id);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getMatchEnroll(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        Bean bean = ParseUtil.parseBean(response, Bean.class);
                        ToastUtils.showToastSHORT(mContext, ToolUtil.getString(bean.getMessage()));
                        getData();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == 1 && resultCode == 101) {//已答完题
            getData();
//            finish();
        }
    }

    public static String css = "<style type=\"text/css\"> img {" +
            "max-width: 100% !important;" +//限定图片宽度填充屏幕
            "height:auto !important;" +//限定图片高度自动
            "}" +
            "body {" +
            "margin-right:0px;" +//限定网页中的文字右边距为15px(可根据实际需要进行行管屏幕适配操作)
            "margin-left:0px;" +//限定网页中的文字左边距为15px(可根据实际需要进行行管屏幕适配操作)
            "margin-top:0px;" +//限定网页中的文字上边距为15px(可根据实际需要进行行管屏幕适配操作)
            "font-size:13px;" +//限定网页中文字的大小为40px,请务必根据各种屏幕分辨率进行适配更改
            "color:#6A3906;" +//限定网页中文字的大小为40px,请务必根据各种屏幕分辨率进行适配更改
            "word-wrap:break-word;" +//允许自动换行(汉字网页应该不需要这一属性,这个用来强制英文单词换行,类似于word/wps中的西文换行)
            "}" +
            "</style>";
}
