package software.ecenter.study.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.Adapter.MatchRankAdapter;
import software.ecenter.study.bean.MatchDetail;
import software.ecenter.study.bean.MatchRankBean;
import software.ecenter.study.R;
import software.ecenter.study.View.MatchRankPopWindow;
import software.ecenter.study.View.RankManBean;
import software.ecenter.study.bean.MatchRankList;
import software.ecenter.study.bean.MyResultbean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.PermissionUtils;
import software.ecenter.study.utils.ScreenUtil;
import software.ecenter.study.utils.TimeUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message  榜单
 * Create by Administrator
 * Create by 2019/10/29
 */
public class MatchRankActivity extends BaseActivity {
    @BindView(R.id.ll_top)
    LinearLayout ll_top;
    @BindView(R.id.tv_matchName)
    TextView tv_matchName;
    @BindView(R.id.tv_tiNum)
    TextView tv_tiNum;
    @BindView(R.id.tv_tiTime)
    TextView tv_tiTime;
    @BindView(R.id.ll_meMess)
    LinearLayout ll_meMess;
    @BindView(R.id.tv_xuanyao)
    TextView tv_xuanyao;
    @BindView(R.id.tv_hjmd)
    TextView tv_hjmd;
    @BindView(R.id.tv_meTiNum)
    TextView tv_meTiNum;
    @BindView(R.id.tc_meZql)
    TextView tc_meZql;
    @BindView(R.id.tv_meTime)
    TextView tv_meTime;
    @BindView(R.id.tv_mePai)
    TextView tv_mePai;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    private String id;
    private MatchRankAdapter adapter;
    private MatchRankPopWindow matchRankPopWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchrank);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MatchRankAdapter(this);
        rv_list.setAdapter(adapter);
        matchRankPopWindow = new MatchRankPopWindow(mContext);
        getData();
        getRankList();
        getMatchPrizeList();
    }

    @OnClick({R.id.iv_back, R.id.tv_xuanyao, R.id.tv_hjmd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_xuanyao://炫耀一下
                PermissionUtils.newIntence().requestPerssion(this, ToolUtil.PERSSION_WRITE, 1, new PermissionUtils.IPermission() {
                    @Override
                    public void success(int code) {
                        startActivity(new Intent(mContext, ShareMatchActivity.class).putExtra("id", id));
                    }
                });
                break;
            case R.id.tv_hjmd://获奖名单
                if (matchRankPopWindow != null && ToolUtil.isList(matchRankPopWindow.getData())) {
                    matchRankPopWindow.showAtLocation(ll_top, Gravity.CENTER, 0, 0);
                } else {
                    ToastUtils.showToastSHORT(mContext, "暂无获奖名单");
                }
                break;
        }
    }

    //获取比赛排名
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

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getMatchRank(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        MatchRankBean bean = ParseUtil.parseBean(response, MatchRankBean.class);
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
    private void setView(MatchRankBean bean) {
        if (bean != null) {
            MatchRankBean.DataBean data = bean.getData();
            if (data != null) {
                boolean setPrize = data.isSetPrize();
                if (setPrize) {
                    tv_hjmd.setVisibility(View.VISIBLE);
                } else {
                    tv_hjmd.setVisibility(View.GONE);
                }
                tv_matchName.setText(ToolUtil.getString(data.getName()));
                tv_tiNum.setText(data.getTotalQuestion() + "题");
                tv_tiTime.setText(TimeUtil.getTimeLimit(data.getTimeLimit()));
                if (data.isIsMatch()) {
                    ll_meMess.setVisibility(View.VISIBLE);
                    tv_meTiNum.setText(data.getCorrect() + "/" + data.getTotalQuestion() + "题");
                    tc_meZql.setText(data.getCorrectPercent());
                    tv_meTime.setText(TimeUtil.getTimeLimit(data.getTimeCost()));
                    if (data.getRank().contains("超时")) {
//                        tv_mePai.getPaint().setTextSize(ScreenUtil.dip2px(mContext,13));
                        tv_mePai.setText(data.getRank());
                    } else {
                        String pai = data.getRank() + "/" + data.getTotal();
                        SpannableStringBuilder builder = new SpannableStringBuilder(pai);
                        ForegroundColorSpan startColor = new ForegroundColorSpan(getResources().getColor(R.color.err_ef5350));
                        builder.setSpan(startColor, 0, String.valueOf(data.getRank()).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tv_mePai.setText(builder);
                    }
                }
            }
        }
    }

    //获取状元榜
    private void getRankList() {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("curpage", curpage);
            json.put("pageNum", 1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getMatchRankList(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        MatchRankList bean = ParseUtil.parseBean(response, MatchRankList.class);
                        setRankView(bean);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    //设置状元榜
    private void setRankView(MatchRankList bean) {
        if (bean != null) {
            MatchRankList.DataBean data = bean.getData();
            if (data != null) {
                adapter.setData(data.getContent());
            }
        }
    }

    //获奖名单
    private void getMatchPrizeList() {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getMatchPrizeList(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        RankManBean bean = ParseUtil.parseBean(response, RankManBean.class);
                        if (bean != null) {
                            matchRankPopWindow.setData(bean.getData());
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

}
