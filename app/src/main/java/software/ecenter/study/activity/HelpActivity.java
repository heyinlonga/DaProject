package software.ecenter.study.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.Adapter.HelpAdapter;
import software.ecenter.study.R;
import software.ecenter.study.bean.MineBean.FeedbackBean;
import software.ecenter.study.bean.MineBean.FeedbackDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * 帮助及反馈
 */
public class HelpActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_help_fankui)
    TextView btnHelpFankui;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    private List<FeedbackBean> ListData = new ArrayList<>();
    private HelpAdapter mAdapter;

    private FeedbackDetailBean mFeedbackDetailBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        getFeedbackList();
    }

    public void getFeedbackList() {
        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getFeedbackList(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        mFeedbackDetailBean = ParseUtil.parseBean(response, FeedbackDetailBean.class);
                        btnRefreshNet.setVisibility(View.GONE);
                        initView();

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });

    }


    public void initView() {
        if(mFeedbackDetailBean==null){
            return;
        }

        ListData =mFeedbackDetailBean.getData();

        mAdapter = new HelpAdapter(mContext, ListData);
        mAdapter.setItemClickListener(new HelpAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ListData.get(position).setCheck(ListData.get(position).isCheck() ? false : true);
                mAdapter.notifyDataSetChanged();
            }


        });

        recyclerView.setAdapter(mAdapter);


    }


    @OnClick({R.id.btn_left_title, R.id.btn_help_fankui,R.id.btn_refresh_net})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_refresh_net:
                getFeedbackList();
                break;
            case R.id.btn_help_fankui:
                startActivity(HelpUpDataActivity.class);
                break;
        }
    }


}
