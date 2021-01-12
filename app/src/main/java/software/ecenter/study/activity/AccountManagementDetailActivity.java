package software.ecenter.study.activity;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
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
import software.ecenter.study.Adapter.AccountDetailAdapter;
import software.ecenter.study.R;
import software.ecenter.study.bean.MineBean.TransactionBean;
import software.ecenter.study.bean.MineBean.TransactionDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;


/**
 * dec 账户明细
 */
public class AccountManagementDetailActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.list_account_detail)
    RecyclerView listAccountDetail;
    @BindView(R.id.cd_lay)
    CardView cd_lay;
    @BindView(R.id.ll_search_all_no_data)
    LinearLayout llSearchAllNoData;
    private List<TransactionBean> listData;
    private AccountDetailAdapter mAdapter;
    private TransactionDetailBean mTransactionDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management_detail);
        ButterKnife.bind(this);
        listData = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        listAccountDetail.setLayoutManager(manager);
        userAccountDetail();

    }

    /**
     * 获取用户账户明细
     */
    public void userAccountDetail() {
        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).userAccountDetail(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        mTransactionDetailBean = ParseUtil.parseBean(response, TransactionDetailBean.class);

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

        if (mTransactionDetailBean == null||mTransactionDetailBean.getData()==null) {
            llSearchAllNoData.setVisibility(View.VISIBLE);
            cd_lay.setVisibility(View.GONE);
            return;
        }
        listData = mTransactionDetailBean.getData().getData();
        mAdapter = new AccountDetailAdapter(mContext, listData);
        listAccountDetail.setAdapter(mAdapter);
        if (listData != null && listData.size() <= 0) {
            llSearchAllNoData.setVisibility(View.VISIBLE);
            cd_lay.setVisibility(View.GONE);
        } else {
            llSearchAllNoData.setVisibility(View.GONE);
            cd_lay.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_left_title)
    public void onViewClicked() {
        onBackPressed();
    }
}
