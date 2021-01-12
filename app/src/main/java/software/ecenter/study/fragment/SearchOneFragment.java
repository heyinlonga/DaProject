package software.ecenter.study.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import software.ecenter.study.activity.QuestionDetailActivity;
import software.ecenter.study.activity.SearchDActivity;
import software.ecenter.study.activity.SeeResourcesVideoActivity;
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
public class SearchOneFragment extends BaseFragment {
    @BindView(R.id.list_search_one)
    RecyclerView listSearchOne;
    @BindView(R.id.list_search_one_line)
    LinearLayout listSearchOneLine;
    @BindView(R.id.list_search_two)
    RecyclerView listSearchTwo;
    @BindView(R.id.list_search_two_line)
    LinearLayout listSearchTwoLine;
    @BindView(R.id.list_search_three)
    RecyclerView listSearchThree;
    @BindView(R.id.list_search_three_line)
    LinearLayout listSearchThreeLine;
    @BindView(R.id.ll_search_all_no_data)
    LinearLayout llSearchAllNoData;
    @BindView(R.id.list_search_one_foot)
    LinearLayout mListFootOne;
    @BindView(R.id.list_search_two_foot)
    LinearLayout mListFootTwo;
    @BindView(R.id.list_search_three_foot)
    LinearLayout mListFootThree;

    String searchKeyword = "";
    Unbinder unbinder;

    private List<BookBean> mBookListTop = new ArrayList<>();
    private List<ResourceBean> mResourceListTop = new ArrayList<>();
    private List<QuestionBean> mQuestionListTop = new ArrayList<>();

    private SearchTushuAdapter mSearchTushuAdapter;
    private SearchZiYuanAdapter mSearchZiYuanAdapter;
    private SearchDayiAdapter mSearchDayiAdapter;
    private boolean isInitView;//是否初始化

    public static SearchOneFragment newIntence(Bundle bundle) {
        SearchOneFragment fragment = new SearchOneFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_search_one, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        isInitView = true;
        initView();

        searchKeyword = ((SearchDActivity) getActivity()).getSearchKeyword();
        onSearch(true, this.searchKeyword);
        return inflate;
    }

    //搜索
    public void onSearch(boolean isShow, String searchKeyword) {
        if (!isInitView) return;
        this.searchKeyword = searchKeyword;
        getData(isShow);
        Log.d("Searchactivity1", "onSearch: " + searchKeyword);
    }

    //获取数据
    public void getData(boolean isShow) {
        if (isShow) {
            if (!showNetWaitLoding()) {
                return;
            }
        }
        if (TextUtils.isEmpty(searchKeyword)) return;

        JSONObject json = new JSONObject();
        try {
            json.put("searchKeyword", searchKeyword);
            json.put("pageNum", 3);
            json.put("curpage", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).search(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        llSearchAllNoData.setVisibility(View.GONE);
                        SearchBean mSearchBean = ParseUtil.parseBean(response, SearchBean.class);
                        if (mSearchBean != null) {
                            showData(mSearchBean);
                        } else {
                            llSearchAllNoData.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        llSearchAllNoData.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    //显示数据
    private void showData(SearchBean mSearchBean) {
        SearchBean.Data data = mSearchBean.getData();
        if (data != null) {
            List<BookBean> bookList = data.getBookList();
            if (bookList != null && bookList.size() > 0) {
                mBookListTop.clear();
                mBookListTop.addAll(bookList);
                mSearchTushuAdapter.refreshData(mBookListTop);
                listSearchOneLine.setVisibility(View.VISIBLE);
            } else {
                listSearchOneLine.setVisibility(View.GONE);
            }

            List<ResourceBean> resurceList = data.getResourceList();
            if (resurceList != null && resurceList.size() > 0) {
                mResourceListTop.clear();
                mResourceListTop.addAll(resurceList);
                mSearchZiYuanAdapter.refreshData(mResourceListTop);
                listSearchTwoLine.setVisibility(View.VISIBLE);
            } else {
                listSearchTwoLine.setVisibility(View.GONE);
            }

            List<QuestionBean> questList = data.getQuestionList();
            if (questList != null && questList.size() > 0) {
                mQuestionListTop.clear();
                mQuestionListTop.addAll(questList);
                mSearchDayiAdapter.refreshData(mQuestionListTop);
                listSearchThreeLine.setVisibility(View.VISIBLE);
            } else {
                listSearchThreeLine.setVisibility(View.GONE);
            }
            //3个都隐藏就显示无结果
            if (listSearchOneLine.getVisibility() == View.GONE && listSearchTwoLine.getVisibility() == View.GONE && listSearchThreeLine.getVisibility() == View.GONE) {
                llSearchAllNoData.setVisibility(View.VISIBLE);
            } else {
                llSearchAllNoData.setVisibility(View.GONE);
            }
        } else {
            llSearchAllNoData.setVisibility(View.VISIBLE);
        }
    }
    //初始化
    private void  initView(){
        //图书
        listSearchOne.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mSearchTushuAdapter = new SearchTushuAdapter(getActivity(), mBookListTop);
        listSearchOne.setAdapter(mSearchTushuAdapter);
        mListFootOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SearchDActivity) getActivity()).setCurrentTab(1);
            }
        });
        mSearchTushuAdapter.setItemClickListener(new SearchTushuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, BookDetailActivity.class);
                intent.putExtra("bookId", mBookListTop.get(position).getBookId());
                startActivity(intent);
            }
        });

        //资源
        listSearchTwo.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mSearchZiYuanAdapter = new SearchZiYuanAdapter(getActivity(), mResourceListTop);
        listSearchTwo.setAdapter(mSearchZiYuanAdapter);
        mListFootTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SearchDActivity) getActivity()).setCurrentTab(2);
            }
        });
        mSearchZiYuanAdapter.setItemClickListener(new SearchZiYuanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, SeeResourcesVideoActivity.class);
                intent.putExtra("resourceId", mResourceListTop.get(position).getResourceId());
                startActivity(intent);
            }
        });

        //答疑
        listSearchThree.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mSearchDayiAdapter = new SearchDayiAdapter(getActivity(), mQuestionListTop);
        listSearchThree.setAdapter(mSearchDayiAdapter);
        mListFootThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SearchDActivity) getActivity()).setCurrentTab(3);
            }
        });
        mSearchDayiAdapter.setItemClickListener(new SearchDayiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent intent = new Intent(mContext, QuestionDetailActivity.class);
                intent.putExtra("questionId", mQuestionListTop.get(position).getQuestionId());
                startActivity(intent);

            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
