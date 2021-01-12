package software.ecenter.study.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.Adapter.StudyReportAdapter;
import software.ecenter.study.Interface.OnItemListener;
import software.ecenter.study.R;
import software.ecenter.study.bean.ReportList;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * Message  学情报告
 * Create by Administrator
 * Create by 2019/11/5
 */
public class StudyreportActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_bao)
    TextView tv_bao;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;
    @BindView(R.id.ll_error)
    LinearLayout ll_error;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    private StudyReportAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyreport);
        ButterKnife.bind(this);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudyReportAdapter(mContext, new OnItemListener() {
            @Override
            public void onItemClick(int poc) {
                ReportList.DataBean chose = adapter.getChose(poc);
                if (chose != null) {
                    startActivity(new Intent(mContext, ReportDetailActivity.class).putExtra("isBuy", chose.isBuy()).putExtra("reportYear", chose.getReportYear()).putExtra("reportMonth", chose.getReportMonth()));
                }
            }
        });
        rv_list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    //获取数据
    private void getData() {
//        if (!showNetWaitLoding()) {
//            return;
//        }
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getReportList())
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ll_error.setVisibility(View.GONE);
                        ReportList bean = ParseUtil.parseBean(response, ReportList.class);
                        if (bean != null) {
                            List<ReportList.DataBean> data = bean.getData();
                            adapter.setData(data);
                            ll_no_data.setVisibility(adapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ll_error.setVisibility(View.VISIBLE);
                        ll_no_data.setVisibility(View.GONE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    @OnClick({R.id.iv_back, R.id.tv_bao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_bao://样例
                startActivity(new Intent(mContext, ReportDetailActivity.class).putExtra("type", 1));
                break;
        }
    }
}
