package software.ecenter.study.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
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
import software.ecenter.study.Adapter.MyTiAdapter;
import software.ecenter.study.bean.MyTiBean;
import software.ecenter.study.R;
import software.ecenter.study.bean.MyResultbean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.TimeUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message  我的成绩
 * Create by Administrator
 * Create by 2019/10/31
 */
public class MeResultActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_rightNum)
    TextView tv_rightNum;
    @BindView(R.id.tv_yongTime)
    TextView tv_yongTime;
    @BindView(R.id.tv_aql)
    TextView tv_aql;
    @BindView(R.id.tv_xianTime)
    TextView tv_xianTime;
    @BindView(R.id.tv_pai)
    TextView tv_pai;
    @BindView(R.id.tv_subTime)
    TextView tv_subTime;
    @BindView(R.id.tv_errorNum)
    TextView tv_errorNum;
    @BindView(R.id.tv_jiexi)
    TextView tv_jiexi;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;
    @BindView(R.id.ll_pai)
    LinearLayout ll_pai;

    private String id;
    private MyTiAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meresult);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        int type = getIntent().getIntExtra("type", 0);
        rv_list.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MyTiAdapter(mContext);
        rv_list.setAdapter(adapter);
        if (type == 1) {
            ll_pai.setVisibility(View.GONE);
        }

        getMyResult();
        getYyResultQuestion();
    }

    @OnClick({R.id.iv_back, R.id.tv_jiexi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_jiexi://错题解析
                startActivity(new Intent(mContext, ErrorTiActivity.class).putExtra("id", id));
                break;
        }
    }

    //我的成绩
    private void getMyResult() {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("id", id);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getMyResult(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        MyResultbean bean = ParseUtil.parseBean(response, MyResultbean.class);
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
    private void setView(MyResultbean bean) {
        if (bean != null) {
            MyResultbean.DataBean data = bean.getData();
            if (data != null) {
                tv_rightNum.setText(data.getCorrect() + "/" + data.getTotal() + "题");
                tv_yongTime.setText(TimeUtil.getTimeLimit(ToolUtil.getLongValue(data.getTimeCost())));
                tv_aql.setText(data.getCorrectPercent());
                tv_xianTime.setText(TimeUtil.getTimeLimit(ToolUtil.getLongValue(data.getTimeLimit())));
                if (!TextUtils.isEmpty(data.getRank()) && data.getRank().contains("/")) {
                    SpannableString spanStr = new SpannableString(ToolUtil.getString(data.getRank()));
                    //设置文字的前景色
                    spanStr.setSpan(new ForegroundColorSpan(Color.RED), 0, data.getRank().indexOf("/"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_pai.setText(spanStr);
                } else {
                    tv_pai.setText(data.getRank());
                }
                tv_subTime.setText(data.getSubmitTime());
                int errorNum = ToolUtil.getIntValue(data.getTotal()) - ToolUtil.getIntValue(data.getCorrect());
                tv_errorNum.setText(String.valueOf(errorNum));
                tv_jiexi.setVisibility(errorNum > 0 ? View.VISIBLE : View.GONE);
            }
        }
    }

    //题目
    private void getYyResultQuestion() {
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
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getMyResultQuestion(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        MyTiBean bean = ParseUtil.parseBean(response, MyTiBean.class);
                        if (bean != null) {
                            MyTiBean.DataBean data = bean.getData();
                            if (data != null) {
                                adapter.setData(data.getContent());
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
}
