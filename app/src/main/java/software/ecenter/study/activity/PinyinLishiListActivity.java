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

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.Adapter.PyLishiAdapter;
import software.ecenter.study.Interface.OnItemListener;
import software.ecenter.study.R;
import software.ecenter.study.bean.CommType;
import software.ecenter.study.bean.PinLishi;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.TimeUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message 拼音拼读历史列表
 * Create by Administrator
 * Create by 2019/11/8
 */
public class PinyinLishiListActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.swipe_target)
    RecyclerView rv_list;
    @BindView(R.id.ll_error)
    LinearLayout ll_error;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    private PyLishiAdapter adapter;
    private boolean hasNextPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinyinlishilist);
        ButterKnife.bind(this);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PyLishiAdapter(mContext, new OnItemListener() {
            @Override
            public void onItemClick(int poc) {
                startActivity(new Intent(mContext, PinyinKenchenListActivity.class).putExtra("id", adapter.getChoseDataId(poc)));
            }
        });
        rv_list.setAdapter(adapter);
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeToLoadLayout.setRefreshing(true);
                curpage = 0;
                getData();
            }
        });
        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                if (hasNextPage) {
                    curpage++;
                    getData();
                } else {
                    //设置上拉加载更多结束
                    swipeToLoadLayout.setLoadingMore(false);
                    ToastUtils.showToastSHORT(mContext, "没有更多数据了");
                }
            }
        });
        getData();
    }

    private void closeRefresh() {
        if (curpage == 0) {
            swipeToLoadLayout.setRefreshing(false);
        } else {
            //设置上拉加载更多结束
            swipeToLoadLayout.setLoadingMore(false);
        }
    }

    //获取数据
    private void getData() {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("curpage", curpage);
            json.put("pageNum", 10);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getPinduHis(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        closeRefresh();
                        dismissNetWaitLoging();
                        ll_error.setVisibility(View.GONE);
                        ll_no_data.setVisibility(View.GONE);
                        PinLishi bean = ParseUtil.parseBean(response, PinLishi.class);
                        if (bean != null) {
                            PinLishi.DataBean data = bean.getData();
                            if (data != null) {
                                hasNextPage = data.isHasNextPage();
                                curpage = ToolUtil.getIntValue(data.getCurpage());
                                List<PinLishi.DataBean.Pindu> pinduExercises = data.getPinduExercises();
                                if (curpage == 0) {
                                    adapter.setData(pinduExercises);
                                } else {
                                    adapter.setMoreData(pinduExercises);
                                }
                            }
                            if (adapter.getItemCount() == 0) {
                                ll_no_data.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        closeRefresh();
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                        ll_error.setVisibility(View.VISIBLE);
                        ll_no_data.setVisibility(View.GONE);
                    }
                });
    }

    @OnClick({R.id.iv_back, R.id.ll_error})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_error:
                curpage = 0;
                getData();
                break;
        }
    }
}
