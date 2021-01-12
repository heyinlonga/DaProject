package software.ecenter.study.activity;

import android.content.Intent;
import android.os.Bundle;

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
import software.ecenter.study.Adapter.QuestionAdapter;
import software.ecenter.study.R;
import software.ecenter.study.bean.MineBean.MyQusetionDetailBean;
import software.ecenter.study.bean.QuestionBean.QuestionBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;


/**
 * dec 我的提问
 */
public class QuestionActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.list_question)
    RecyclerView listQuestion;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.ll_search_all_no_data)
    LinearLayout llSearchAllNoData;

    private List<QuestionBean> listData;
    private QuestionAdapter mAdapter;

    private MyQusetionDetailBean mMyQusetionDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);
        listData = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        listQuestion.setLayoutManager(manager);
        getQuestionList(true);
    }

    public void getQuestionList(boolean isShow) {
        if (isShow) {
            if (!showNetWaitLoding()) {
                return;
            }
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getQuestionList(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        mMyQusetionDetailBean = ParseUtil.parseBean(response, MyQusetionDetailBean.class);
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
        if (mMyQusetionDetailBean == null) {
            llSearchAllNoData.setVisibility(View.VISIBLE);
            listQuestion.setVisibility(View.GONE);
            return;
        }
        listData = mMyQusetionDetailBean.getData().getData();
        mAdapter = new QuestionAdapter(mContext, listData);
        mAdapter.setItemClickListener(new QuestionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, QuestionDetailActivity.class);
                intent.putExtra("questionId", listData.get(position).getQuestionId());
                startActivity(intent);

            }
        });
        listQuestion.setAdapter(mAdapter);
        if (listData.size() <= 0) {
            llSearchAllNoData.setVisibility(View.VISIBLE);
            listQuestion.setVisibility(View.GONE);
        } else {
            llSearchAllNoData.setVisibility(View.GONE);
            listQuestion.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.btn_left_title, R.id.btn_refresh_net})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_refresh_net:
                getQuestionList(true);
                break;
        }
    }
}
