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
import software.ecenter.study.Adapter.MyBuyAdapter;
import software.ecenter.study.R;
import software.ecenter.study.bean.MineBean.MyBuyBean;
import software.ecenter.study.bean.MineBean.MyBuyDetailBean;
import software.ecenter.study.bean.MineBean.MyCommitDetailBean;
import software.ecenter.study.bean.MineBean.MyQusetionDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * dec 我的购买
 */
public class MyBuyActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.ll_search_all_no_data)
    LinearLayout llSearchAllNoData;

    private List<MyBuyBean> listData;
    private MyBuyAdapter mAdapter;

    private MyBuyDetailBean mMyBuyDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_buy);
        ButterKnife.bind(this);
        listData = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        getUserPurchase();
    }


    public void getUserPurchase() {
        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getUserPurchase(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        mMyBuyDetailBean = ParseUtil.parseBean(response, MyBuyDetailBean.class);
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
        if (mMyBuyDetailBean == null) {
            llSearchAllNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }
        listData = mMyBuyDetailBean.getData().getData();
        mAdapter = new MyBuyAdapter(mContext, listData);
        mAdapter.setItemClickListener(new MyBuyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MyBuyBean bean = listData.get(position);
                Intent intent = new Intent();
                switch (bean.getPurchaseType()) {
                    case 1:  //整书
                        intent.setClass(mContext, BookDetailActivity.class);
                        intent.putExtra("bookId", bean.getResourceId());
                        startActivity(intent);
                        break;
                    case 2: //精品课程
                        intent.setClass(mContext, KengChengDetailActivity.class);
                        intent.putExtra("curriculumId", bean.getResourceId());
                        startActivity(intent);
                        break;
                    case 3: //套系组合
                        intent.setClass(mContext, TaoXiDetailActivity.class);
                        intent.putExtra("packageId", bean.getResourceId());
                        startActivity(intent);
                        break;
                    case 4: //章节获取
                        intent.setClass(mContext, ChapterDetailsActivity.class);
                        intent.putExtra("chapterId", bean.getResourceId());
                        intent.putExtra("chapterName", "" + bean.getPurchaseTitle());
                        startActivity(intent);
                        break;
                    case 5: //课时资源
                        intent.setClass(mContext, SeeResourcesVideoActivity.class);
                        intent.putExtra("resourceId", bean.getResourceId());
                        startActivity(intent);
                        break;
                    case 6: //答疑
                        intent.setClass(mContext, QuestionDetailActivity.class);
                        intent.putExtra("questionId", bean.getResourceId());
                        startActivity(intent);
                        break;
                    case 7: //精品课程章节
                        intent.setClass(mContext, ChapterDetailsActivity.class);
                        intent.putExtra("chapterId", bean.getResourceId());
                        intent.putExtra("chapterName", "" + bean.getPurchaseTitle());
                        intent.putExtra("type", 1);
                        startActivity(intent);
                        break;
                    case 8: //讲座
                        int categoryShowType = bean.getCategoryShowType();
                        intent.putExtra("id", bean.getResourceId());
                        if (categoryShowType == 0) {//课程列表
                            intent.setClass(mContext, WholeBookCourseActivity.class);
                        } else {//章节列表
                            intent.setClass(mContext, WholeBookChapterActivity.class);
                        }
                        startActivity(intent);
                        break;
                    case 9: //讲座目录
                        intent.setClass(mContext, WholeBookCourseActivity.class);
                        intent.putExtra("id", bean.getResourceId());
                        startActivity(intent);
                        break;
                    case 10: //讲座资源
                        intent.setClass(mContext, SeeResourcesVideoActivity.class);
                        intent.putExtra("resourceId", bean.getResourceId());
                        intent.putExtra("type", 1);
                        startActivity(intent);
                        break;
                }

            }
        });
        recyclerView.setAdapter(mAdapter);
        if (listData.size() <= 0) {
            llSearchAllNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            llSearchAllNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }


    @OnClick({R.id.btn_left_title, R.id.btn_refresh_net})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_refresh_net:
                getUserPurchase();
                break;
        }
    }
}
