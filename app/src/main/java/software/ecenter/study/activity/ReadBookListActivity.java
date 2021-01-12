package software.ecenter.study.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.Adapter.MatchAdapter;
import software.ecenter.study.Interface.OnItemListener;
import software.ecenter.study.R;
import software.ecenter.study.View.SpinnerPopWindow;
import software.ecenter.study.bean.Bean;
import software.ecenter.study.bean.MatchBean;
import software.ecenter.study.bean.MatchList;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

import static software.ecenter.study.Interface.ConstantData.Grade;

/**
 * Message  比赛列表
 * Create by Administrator
 * Create by 2019/10/28
 */
public class ReadBookListActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.ll_grade)
    LinearLayout ll_grade;
    @BindView(R.id.ll_top)
    LinearLayout ll_top;
    @BindView(R.id.ll_error)
    LinearLayout ll_error;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.tv_grade)
    TextView tv_grade;
    @BindView(R.id.tv_guizhe)
    TextView tv_guizhe;
    @BindView(R.id.iv_tip)
    ImageView iv_tip;
    @BindView(R.id.rl_zzjx)
    RelativeLayout rl_zzjx;
    @BindView(R.id.rv_zzjx)
    RecyclerView rv_zzjx;
    @BindView(R.id.rl_jjks)
    RelativeLayout rl_jjks;
    @BindView(R.id.rv_jjks)
    RecyclerView rv_jjks;
    @BindView(R.id.rl_wqbs)
    RelativeLayout rl_wqbs;
    @BindView(R.id.rv_wqbs)
    RecyclerView rv_wqbs;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    private SpinnerPopWindow spinnerPopWindow;
    private String grade = "一年级上";
    private MatchAdapter zzjxAdapter;
    private MatchAdapter jjksAdapter;
    private MatchAdapter wqbsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readbooklist);
        ButterKnife.bind(this);
        initRv();
        grade = AccountUtil.getRealGrade(mContext);
        tv_grade.setText(grade);
        // 年级
        final List<String> spinnerList = ToolUtil.StringToArray(Grade, ",");
        spinnerPopWindow = new SpinnerPopWindow(mContext);
        spinnerPopWindow.refreshData(spinnerList);
        spinnerPopWindow.setPopTitle("选择年级");
        spinnerPopWindow.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                tv_grade.setText(spinnerList.get(position));
                grade = spinnerList.get(position);
                getListData();
            }
        });
        spinnerPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                iv_tip.setImageResource(R.drawable.grade_tip1);
            }
        });
        refresh.setColorSchemeResources(R.color.background);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListData();
    }

    //初始化列表
    private void initRv() {
        rv_zzjx.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        zzjxAdapter = new MatchAdapter(mContext, 1, listener);
        rv_zzjx.setAdapter(zzjxAdapter);
        rv_jjks.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        jjksAdapter = new MatchAdapter(mContext, 2, listener);
        rv_jjks.setAdapter(jjksAdapter);
        rv_wqbs.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        wqbsAdapter = new MatchAdapter(mContext, 3, listener);
        rv_wqbs.setAdapter(wqbsAdapter);
    }

    //点击事件
    private OnItemListener listener = new OnItemListener() {
        @Override
        public void onIndexClick(int index, int poc) {
            switch (index) {
                case 1://报名
                    boolean enroll = jjksAdapter.getChoseData(poc).isEnroll();
                    if (!enroll) {
                        baoMing(jjksAdapter.getChoseData(poc).getId());
                    }
                    break;
                case 2://我的成绩
                    startActivity(new Intent(mContext,MeResultActivity.class).putExtra("id",wqbsAdapter.getChoseData(poc).getId()));
                    break;
                case 3://查看榜单
                    startActivity(new Intent(mContext,MatchRankActivity.class).putExtra("id",wqbsAdapter.getChoseData(poc).getId()));
                    break;
            }
        }

        @Override
        public void onItemClick(int type, int poc) {
            Intent intent = new Intent(mContext, MatchDetailActivity.class);
            switch (type) {
                case 1://正在进行
                    intent.putExtra("id", zzjxAdapter.getChoseData(poc).getId());
                    break;
                case 2://即将开始
                    intent.putExtra("id", jjksAdapter.getChoseData(poc).getId());
                    break;
                case 3://往期比赛
                    intent.putExtra("id", wqbsAdapter.getChoseData(poc).getId());
                    break;
            }
            startActivity(intent);
        }
    };

    //报名
    private void baoMing(String id) {
        if (TextUtils.isEmpty(id)) {
            return;
        }
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
                        getListData();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    //获取比赛列表
    private void getListData() {
//        if (!showNetWaitLoding()) {
//            return;
//        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("grade", grade);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getMatchList(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ll_error.setVisibility(View.GONE);
                        MatchBean bean = ParseUtil.parseBean(response, MatchBean.class);
                        setView(bean);
                        refresh.setRefreshing(false);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ll_error.setVisibility(View.VISIBLE);
                        ll_no_data.setVisibility(View.GONE);
                        ToastUtils.showToastLONG(mContext, msg);
                        refresh.setRefreshing(false);
                    }
                });
    }

    //设置数据
    private void setView(MatchBean bean) {
        MatchBean.DataBean data = bean.getData();
        if (data != null) {
            List<MatchList> inProgressList = data.getInProgressList();
            List<MatchList> beginSoonList = data.getBeginSoonList();
            List<MatchList> endList = data.getEndList();
            zzjxAdapter.setData(inProgressList);
            jjksAdapter.setData(beginSoonList);
            wqbsAdapter.setData(endList);
            //正在进行
            if (ToolUtil.isList(inProgressList)) {
                rl_zzjx.setVisibility(View.VISIBLE);
            } else {
                rl_zzjx.setVisibility(View.GONE);
            }
            //即将开始
            if (ToolUtil.isList(beginSoonList)) {
                rl_jjks.setVisibility(View.VISIBLE);
            } else {
                rl_jjks.setVisibility(View.GONE);
            }
            //往期比赛
            if (ToolUtil.isList(endList)) {
                rl_wqbs.setVisibility(View.VISIBLE);
            } else {
                rl_wqbs.setVisibility(View.GONE);
            }
            if (ToolUtil.isList(inProgressList) || ToolUtil.isList(beginSoonList) || ToolUtil.isList(endList)) {
                ll_no_data.setVisibility(View.GONE);
            } else {
                ll_no_data.setVisibility(View.VISIBLE);
            }
        } else {
            ll_no_data.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.iv_back, R.id.ll_error, R.id.tv_guizhe, R.id.ll_grade})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_error://错误界面
                getListData();
                break;
            case R.id.tv_guizhe://规则
                startActivity(new Intent(mContext, MatchGuiZheActivity.class));
                break;
            case R.id.ll_grade://选择年级
                spinnerPopWindow.showAtLocation(ll_top, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                iv_tip.setImageResource(R.drawable.grade_tip2);
                break;
        }
    }
}
