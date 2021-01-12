package software.ecenter.study.activity;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.Adapter.IntegralDetailAdapter;
import software.ecenter.study.R;
import software.ecenter.study.bean.MineBean.IntegralBean;
import software.ecenter.study.bean.MineBean.IntegralDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * dec 积分明细
 */
public class IntegralDetailActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.list_Integral_detail)
    RecyclerView listIntegralDetail;
    @BindView(R.id.card_lay)
    CardView card_lay;
    @BindView(R.id.btn_integral_detail)
    Button butintegraldetail;
    @BindView(R.id.ll_search_all_no_data)
    LinearLayout ll_search_all_no_data;
    private List<IntegralBean> listData;
    private IntegralDetailAdapter mAdapter;
    private IntegralDetailBean mIntegralDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_detail);
        ButterKnife.bind(this);
        listData = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        listIntegralDetail.setLayoutManager(manager);


        getIntegralDetail();
    }


    public void getIntegralDetail() {
        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getIntegralDetail(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        mIntegralDetailBean = ParseUtil.parseBean(response, IntegralDetailBean.class);

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

        if (mIntegralDetailBean == null) {
            return;
        }

        listData = mIntegralDetailBean.getData().getData();
        mAdapter = new IntegralDetailAdapter(mContext, listData);
        listIntegralDetail.setAdapter(mAdapter);
        if (listData != null && listData.size() <= 0) {
            ll_search_all_no_data.setVisibility(View.VISIBLE);
            card_lay.setVisibility(View.GONE);
        } else {
            ll_search_all_no_data.setVisibility(View.GONE);
            card_lay.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_left_title)
    public void onViewClicked() {
        onBackPressed();
    }

    @OnClick({R.id.btn_integral_detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_integral_detail:
                startActivity(LevelIntegralActivity.class);
                break;
        }
    }
}
