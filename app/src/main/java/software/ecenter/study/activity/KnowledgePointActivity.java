package software.ecenter.study.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.Adapter.SearchKnowPointAdapter;
import software.ecenter.study.R;
import software.ecenter.study.View.LoadMoreFooterView;
import software.ecenter.study.View.RefreshHeaderView;
import software.ecenter.study.bean.HuoDongBean.knowledgeDetailPoint;
import software.ecenter.study.bean.HuoDongBean.knowledgePoint;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * dec 知识点
 */
public class KnowledgePointActivity extends BaseActivity {

    @BindView(R.id.seach_edit)
    EditText seachEdit;
    @BindView(R.id.btn_right)
    TextView btnRight;
    @BindView(R.id.top)
    RelativeLayout top;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView swipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView swipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.list_search_class_line)
    CardView listSearchClassLine;
    @BindView(R.id.title_left)
    ImageView ivTitleLeft;

    private SearchKnowPointAdapter mAdapter;
    private List<knowledgePoint> mListData;
    private knowledgeDetailPoint mknowledgeDetailPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_point);
        ButterKnife.bind(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        swipeTarget.setLayoutManager(manager);
        swipeToLoadLayout.setRefreshEnabled(false);
        swipeToLoadLayout.setLoadMoreEnabled(false);
        mListData = new ArrayList<>();

        seachEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searContent = "";
                searContent = seachEdit.getText().toString().trim();
                if (TextUtils.isEmpty(searContent)) {
                    return;
                }
                getData(searContent);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void getData(String keyword) {
//        if (!showNetWaitLoding()) {
//            return;
//        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("keyword", keyword);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getKnowledgePointActivity(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
//                        dismissNetWaitLoging();
                        mknowledgeDetailPoint = ParseUtil.parseBean(response, knowledgeDetailPoint.class);
                        initView();
                    }

                    @Override
                    public void onFail(int type, String msg) {
//                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    public void initView() {
        mListData = mknowledgeDetailPoint.getData().getKnowledgePointList();
        if (mAdapter == null) {
            mAdapter = new SearchKnowPointAdapter(this, mListData);
            mAdapter.setItemClickListener(new SearchKnowPointAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent();
                    intent.putExtra("mknowledgePoint", mListData.get(position));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            });
            swipeTarget.setAdapter(mAdapter);
        } else {
            mAdapter.refreshData(mListData);
        }
    }

    @OnClick({R.id.btn_right, R.id.title_left})
    public void onViewClicked(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_right:
                search();
                break;
            case R.id.title_left:
                finish();
                break;
        }
    }

    private void search() {
        Intent intent = new Intent();
        intent.putExtra("mknowledgePoint", seachEdit.getText().toString() + "");
        setResult(123123, intent);
        finish();
        /*if ("取消".equals(btnRight.getText().toString())) {
            seachEdit.setText("");
            btnRight.setText("搜索");
        } else {
            if (TextUtils.isEmpty(seachEdit.getText().toString())) {
                ToastUtils.showToastSHORT(mContext, "请输入搜索内容");
                return;
            }
            getData(seachEdit.getText().toString());
        }
        */
    }
}
