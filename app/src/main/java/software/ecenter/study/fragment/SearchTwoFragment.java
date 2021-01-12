package software.ecenter.study.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.Adapter.SearchDayiAdapter;
import software.ecenter.study.Adapter.SearchTushuAdapter;
import software.ecenter.study.Adapter.SearchZiYuanAdapter;
import software.ecenter.study.R;
import software.ecenter.study.activity.BookDetailActivity;
import software.ecenter.study.activity.SearchDActivity;
import software.ecenter.study.bean.HomeBean.BookBean;
import software.ecenter.study.bean.HomeBean.ResourceBean;
import software.ecenter.study.bean.HomeBean.SearchBean;
import software.ecenter.study.bean.QuestionBean.QuestionBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * Message
 * Create by Administrator
 * Create by 2019/1/25
 */
public class SearchTwoFragment extends BaseFragment {
    @BindView(R.id.list_search_class_line)
    CardView listSearchClassLine;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.swipe_target)
    RecyclerView listSearchClass;
    @BindView(R.id.ll_search_all_no_data)
    LinearLayout llSearchAllNoData;

    String searchKeyword = "";
    int curpage ; //分页
    Unbinder unbinder;
    private boolean isInitView;//是否初始化
    private SearchTushuAdapter mSearchTushuAdapter;
    private List<BookBean> mBookList = new ArrayList<>();
    private SearchBean mSearchBean;

    public static SearchTwoFragment newIntence(Bundle bundle) {
        SearchTwoFragment fragment = new SearchTwoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_search_two, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        isInitView = true;
        initView();

        searchKeyword = ((SearchDActivity) getActivity()).getSearchKeyword();
        onSearch(true, searchKeyword);
        return inflate;
    }

    //搜索
    public void onSearch(boolean isShow, String searchKeyword) {
        curpage = 0;
        if (!isInitView) return;
        this.searchKeyword = searchKeyword;
        getData(isShow);
    }

    //获取数据
    public void getData(boolean isShow) {
        if (isShow) {
            if (!showNetWaitLoding()) {
                return;
            }
        }

        JSONObject json = new JSONObject();
        try {
            json.put("searchKeyword", searchKeyword);
            json.put("curpage", curpage);
            json.put("pageNum", 10);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).searchBook(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        mSearchBean = ParseUtil.parseBean(response, SearchBean.class);
                        setBookData();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                        if (curpage == 0){
                            llSearchAllNoData.setVisibility(View.VISIBLE);
                            listSearchClassLine.setVisibility(View.GONE);
                        }
                    }

                });
    }
    //设置数据
    private void setBookData() {
        if (mSearchBean!=null){
            SearchBean.Data data = mSearchBean.getData();
            if (data!=null){
                if (curpage == 0){
                    mBookList.clear();
                }
                mBookList.addAll(data.getBookList());
                mSearchTushuAdapter.refreshData(mBookList);
            }
        }
        //显示有无数据界面
        if (mBookList == null || mBookList.size()==0){
            llSearchAllNoData.setVisibility(View.VISIBLE);
            listSearchClassLine.setVisibility(View.GONE);
        }else {
            llSearchAllNoData.setVisibility(View.GONE);
            listSearchClassLine.setVisibility(View.VISIBLE);
        }
    }

    //初始化
    private void initView() {
        //图书
        listSearchClass.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSearchTushuAdapter = new SearchTushuAdapter(getActivity(), mBookList);
        listSearchClass.setAdapter(mSearchTushuAdapter);

        mSearchTushuAdapter.setItemClickListener(new SearchTushuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, BookDetailActivity.class);
                intent.putExtra("bookId", mBookList.get(position).getBookId());
                startActivity(intent);
            }
        });
        //禁止下拉刷新
        swipeToLoadLayout.setRefreshEnabled(false);
        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mSearchBean.getData().isHasBookNextPage()) {
                    curpage ++;
                    getData(true);
                } else {
                    ToastUtils.showToastSHORT(mContext, "没有更多数据了");
                }
                //设置上拉加载更多结束
                swipeToLoadLayout.setLoadingMore(false);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

}
