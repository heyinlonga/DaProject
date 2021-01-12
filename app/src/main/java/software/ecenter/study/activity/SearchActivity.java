package software.ecenter.study.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
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
import software.ecenter.study.Adapter.SearchDayiAdapter;
import software.ecenter.study.Adapter.SearchTushuAdapter;
import software.ecenter.study.Adapter.SearchZiYuanAdapter;
import software.ecenter.study.R;
import software.ecenter.study.View.LoadMoreFooterView;
import software.ecenter.study.View.RefreshHeaderView;
import software.ecenter.study.bean.HomeBean.BookBean;
import software.ecenter.study.bean.HomeBean.ResourceBean;
import software.ecenter.study.bean.HomeBean.SearchBean;
import software.ecenter.study.bean.QuestionBean.QuestionBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/***
 * dec 搜索
 *
 * */
public class SearchActivity extends BaseActivity {

    @BindView(R.id.title_left)
    ImageView titleLeft;
    @BindView(R.id.seach_edit)
    EditText seachEdit;
    @BindView(R.id.seach_btn)
    LinearLayout seachBtn;
    @BindView(R.id.title_right)
    TextView titleRight;
    @BindView(R.id.top)
    RelativeLayout top;
    @BindView(R.id.tip_one)
    View tipOne;
    @BindView(R.id.btn_one)
    RelativeLayout btnOne;
    @BindView(R.id.tip_two)
    View tipTwo;
    @BindView(R.id.btn_two)
    RelativeLayout btnTwo;
    @BindView(R.id.tip_three)
    View tipThree;
    @BindView(R.id.btn_three)
    RelativeLayout btnThree;
    @BindView(R.id.tip_four)
    View tipFour;
    @BindView(R.id.btn_four)
    RelativeLayout btnFour;
    @BindView(R.id.tab_line)
    LinearLayout tabLine;
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
    @BindView(R.id.scroll_search)
    ScrollView scrollSearch;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView swipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView listSearchClass;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView swipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.list_search_class_line)
    CardView listSearchClassLine;
    @BindView(R.id.ll_search_all_no_data)
    LinearLayout llSearchAllNoData;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.tv_book)
    TextView tvBook;
    @BindView(R.id.tv_resouce)
    TextView tvResouce;
    @BindView(R.id.tv_question)
    TextView tvQuestion;


    private LinearLayout mListFootOne;
    private LinearLayout mListFootTwo;
    private LinearLayout mListFootThree;

    private final int MSG_SHOW_TAB = 0;
    private final int MSG_SHOW_ClASS_ONE = 1;
    private final int MSG_SHOW_ClASS_TWO = 2;
    private final int MSG_SHOW_ClASS_THREE = 3;
    private final int MSG_SHOW_ClASS_FOUR = 4;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SHOW_TAB:
                    showTab();
                    break;
                case MSG_SHOW_ClASS_ONE:
                    showClassOne();
                    break;
                case MSG_SHOW_ClASS_TWO:
                    showClassTwo();
                    break;
                case MSG_SHOW_ClASS_THREE:
                    showClassThree();
                    break;
                case MSG_SHOW_ClASS_FOUR:
                    showClassFour();
                    break;

            }

        }
    };


    private List<BookBean> mBookList;
    private List<ResourceBean> mResourceList;
    private List<QuestionBean> mQuestionList;

    private List<BookBean> mBookListTop;
    private List<ResourceBean> mResourceListTop;
    private List<QuestionBean> mQuestionListTop;


    private SearchTushuAdapter mSearchTushuAdapter;
    private SearchZiYuanAdapter mSearchZiYuanAdapter;
    private SearchDayiAdapter mSearchDayiAdapter;


    private SearchBean mSearchBean;

    private String searchKeyword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        mListFootOne = findViewById(R.id.list_search_one_foot);
        mListFootTwo = findViewById(R.id.list_search_two_foot);
        mListFootThree = findViewById(R.id.list_search_three_foot);

        mListFootOne.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(MSG_SHOW_ClASS_TWO);

            }
        });
        mListFootTwo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(MSG_SHOW_ClASS_THREE);
            }
        });
        mListFootThree.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(MSG_SHOW_ClASS_FOUR);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listSearchClass.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManagerOne = new LinearLayoutManager(this);
        linearLayoutManagerOne.setOrientation(LinearLayoutManager.VERTICAL);
        listSearchOne.setLayoutManager(linearLayoutManagerOne);

        LinearLayoutManager linearLayoutManagerTwo = new LinearLayoutManager(this);
        linearLayoutManagerTwo.setOrientation(LinearLayoutManager.VERTICAL);
        listSearchTwo.setLayoutManager(linearLayoutManagerTwo);

        LinearLayoutManager linearLayoutManagerThree = new LinearLayoutManager(this);
        linearLayoutManagerThree.setOrientation(LinearLayoutManager.VERTICAL);
        listSearchThree.setLayoutManager(linearLayoutManagerThree);

        mBookListTop = new ArrayList<>();
        mResourceListTop = new ArrayList<>();
        mQuestionListTop = new ArrayList<>();

        llSearchAllNoData.setVisibility(View.VISIBLE);

        //禁止下拉刷新
        swipeToLoadLayout.setRefreshEnabled(false);

    }


    public void showTab() {
        if (mSearchBean == null) {
            return;
        }
        tipChang(1);
//        titleRight.setText("取消");
        tabLine.setVisibility(View.VISIBLE);

        mBookList = mSearchBean.getData().getBookList();
        mResourceList = mSearchBean.getData().getResourceList();
        mQuestionList = mSearchBean.getData().getQuestionList();
        if (mBookList.size() <= 0 && mResourceList.size() <= 0 && mQuestionList.size() <= 0) {
            scrollSearch.setVisibility(View.GONE);
            listSearchClassLine.setVisibility(View.GONE);
            llSearchAllNoData.setVisibility(View.VISIBLE);
        } else {
            scrollSearch.setVisibility(View.VISIBLE);
            listSearchClassLine.setVisibility(View.GONE);
            if (mBookList.size() <= 0) listSearchOneLine.setVisibility(View.GONE);
            else listSearchOneLine.setVisibility(View.VISIBLE);
            if (mResourceList.size() <= 0) listSearchTwoLine.setVisibility(View.GONE);
            else listSearchTwoLine.setVisibility(View.VISIBLE);
            if (mQuestionList.size() <= 0) listSearchThreeLine.setVisibility(View.GONE);
            else listSearchThreeLine.setVisibility(View.VISIBLE);
        }

        mBookListTop.clear();
        if (mBookList.size() <= 3) {
            mBookListTop.addAll(mBookList);
        } else {
            mBookListTop = mBookList.subList(0, 3);
        }

        mResourceListTop.clear();
        if (mResourceList.size() <= 3) {
            mResourceListTop.addAll(mResourceList);
        } else {
            mResourceListTop = mResourceList.subList(0, 3);
        }

        mQuestionListTop.clear();
        if (mQuestionList.size() <= 3) {
            mQuestionListTop.addAll(mQuestionList);
        } else {
            mQuestionListTop = mQuestionList.subList(0, 3);
        }

        if (mSearchTushuAdapter != null) {
            mSearchTushuAdapter.refreshData(mBookListTop);
        } else {
            mSearchTushuAdapter = new SearchTushuAdapter(this, mBookListTop);
            listSearchOne.setVisibility(View.VISIBLE);
            listSearchOne.setAdapter(mSearchTushuAdapter);
        }
        mSearchTushuAdapter.setItemClickListener(new SearchTushuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, BookDetailActivity.class);
                intent.putExtra("bookId", mBookListTop.get(position).getBookId());
                startActivity(intent);
            }
        });
        mListFootOne.setVisibility(View.VISIBLE);

        if (mSearchZiYuanAdapter != null) {
            mSearchZiYuanAdapter.refreshData(mResourceListTop);
        } else {
            mSearchZiYuanAdapter = new SearchZiYuanAdapter(this, mResourceListTop);
            listSearchTwo.setAdapter(mSearchZiYuanAdapter);
        }
        mSearchZiYuanAdapter.setItemClickListener(new SearchZiYuanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, SeeResourcesVideoActivity.class);
                intent.putExtra("resourceId", mResourceListTop.get(position).getResourceId());
                startActivity(intent);
            }
        });
        mListFootTwo.setVisibility(View.VISIBLE);


        if (mSearchDayiAdapter != null) {
            mSearchDayiAdapter.refreshData(mQuestionListTop);
        } else {
            mSearchDayiAdapter = new SearchDayiAdapter(this, mQuestionListTop);
            listSearchThree.setAdapter(mSearchDayiAdapter);
        }
        mSearchDayiAdapter.setItemClickListener(new SearchDayiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent intent = new Intent(mContext, QuestionDetailActivity.class);
                intent.putExtra("questionId", mQuestionList.get(position).getQuestionId());
                startActivity(intent);

            }
        });
        mListFootThree.setVisibility(View.VISIBLE);

    }

    public void showClassOne() {
        tipChang(1);
        if (mBookList == null)
            mBookList = new ArrayList<>();
        if (mQuestionList == null)
            mQuestionList = new ArrayList<>();
        if (mResourceList == null)
            mResourceList = new ArrayList<>();
        if (mBookList.size() <= 0 && mQuestionList.size() <= 0 && mResourceList.size() <= 0) {
            llSearchAllNoData.setVisibility(View.VISIBLE);
            scrollSearch.setVisibility(View.GONE);
            listSearchClassLine.setVisibility(View.GONE);
        } else {
            scrollSearch.setVisibility(View.VISIBLE);
            listSearchClassLine.setVisibility(View.GONE);
            llSearchAllNoData.setVisibility(View.GONE);
        }
        if (mBookList.size() <= 0) listSearchOneLine.setVisibility(View.GONE);
        else listSearchOneLine.setVisibility(View.VISIBLE);

        if (mResourceList.size() <= 0) listSearchTwoLine.setVisibility(View.GONE);
        else listSearchTwoLine.setVisibility(View.VISIBLE);

        if (mQuestionList.size() <= 0) listSearchThreeLine.setVisibility(View.GONE);
        else listSearchThreeLine.setVisibility(View.VISIBLE);


    }

    public void showClassTwo() {

        tipChang(2);

        if (mBookList == null)
            mBookList = new ArrayList<>();
        if (mBookList.size() <= 0) {
//            ToastUtils.showToastSHORT(mContext, "暂无数据");
            scrollSearch.setVisibility(View.GONE);
            listSearchClassLine.setVisibility(View.GONE);
            llSearchAllNoData.setVisibility(View.VISIBLE);
        } else {
            scrollSearch.setVisibility(View.GONE);
            listSearchClassLine.setVisibility(View.VISIBLE);
            llSearchAllNoData.setVisibility(View.GONE);
        }
        if (mSearchTushuAdapter != null) {
            mSearchTushuAdapter.refreshData(mBookList);
        } else {
            mSearchTushuAdapter = new SearchTushuAdapter(this, mBookList);
        }
        mSearchTushuAdapter.setItemClickListener(new SearchTushuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, BookDetailActivity.class);
                intent.putExtra("bookId", mBookList.get(position).getBookId());
                startActivity(intent);
            }
        });
        listSearchClass.setAdapter(mSearchTushuAdapter);

        //为swipeToLoadLayout设置下拉刷新监听者
/*        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                ToastUtils.showToastSHORT(SearchActivity.this,"下拉刷新");

                //设置下拉刷新结束
                swipeToLoadLayout.setRefreshing(false);
            }
        });*/

        //为swipeToLoadLayout设置上拉加载更多监听者
        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mSearchBean.getData().isHasBookNextPage()) {
                    getMoreBook("" + (mSearchBean.getData().getBookCurPage() + 1));
                } else {
                    ToastUtils.showToastSHORT(mContext, "没有更多数据了");
                }
                //设置上拉加载更多结束
                swipeToLoadLayout.setLoadingMore(false);
            }
        });


    }

    public void showClassThree() {

        tipChang(3);
        if (mResourceList == null)
            mResourceList = new ArrayList<>();
        if (mResourceList.size() <= 0) {
//            ToastUtils.showToastSHORT(mContext, "暂无数据");
            scrollSearch.setVisibility(View.GONE);
            listSearchClassLine.setVisibility(View.GONE);
            llSearchAllNoData.setVisibility(View.VISIBLE);
        } else {
            scrollSearch.setVisibility(View.GONE);
            listSearchClassLine.setVisibility(View.VISIBLE);
            llSearchAllNoData.setVisibility(View.GONE);
        }
        if (mSearchZiYuanAdapter != null) {
            mSearchZiYuanAdapter.refreshData(mResourceList);
        } else {
            mSearchZiYuanAdapter = new SearchZiYuanAdapter(this, mResourceList);
        }
        mSearchZiYuanAdapter.setItemClickListener(new SearchZiYuanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, SeeResourcesVideoActivity.class);
                intent.putExtra("resourceId", mResourceList.get(position).getResourceId());
                startActivity(intent);
            }
        });
        listSearchClass.setAdapter(mSearchZiYuanAdapter);

        //为swipeToLoadLayout设置下拉刷新监听者
/*        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                ToastUtils.showToastSHORT(SearchActivity.this,"下拉刷新");
                //设置下拉刷新结束
                swipeToLoadLayout.setRefreshing(false);
            }
        });*/
        //为swipeToLoadLayout设置上拉加载更多监听者
        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mSearchBean.getData().isHasResourNextPage()) {
                    getMoreResource("" + (mSearchBean.getData().getResourceCurPage() + 1));
                } else {
                    ToastUtils.showToastSHORT(mContext, "没有更多数据了");
                }
                //设置上拉加载更多结束
                swipeToLoadLayout.setLoadingMore(false);
            }
        });

    }

    public void showClassFour() {

        tipChang(4);
        if (mQuestionList == null)
            mQuestionList = new ArrayList<>();
        if (mQuestionList.size() <= 0) {
//            ToastUtils.showToastSHORT(mContext, "暂无数据");
            llSearchAllNoData.setVisibility(View.VISIBLE);
        } else {
            scrollSearch.setVisibility(View.GONE);
            listSearchClassLine.setVisibility(View.VISIBLE);
            llSearchAllNoData.setVisibility(View.GONE);

        }
        if (mSearchDayiAdapter != null) {
            mSearchDayiAdapter.refreshData(mQuestionList);
        } else {
            mSearchDayiAdapter = new SearchDayiAdapter(this, mQuestionList);
        }
        mSearchDayiAdapter.setItemClickListener(new SearchDayiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, QuestionDetailActivity.class);
                intent.putExtra("questionId", mQuestionList.get(position).getQuestionId());
                startActivity(intent);
            }
        });
        listSearchClass.setAdapter(mSearchDayiAdapter);

        //为swipeToLoadLayout设置下拉刷新监听者
    /*    swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                ToastUtils.showToastSHORT(SearchActivity.this,"下拉刷新");
                //设置下拉刷新结束
                swipeToLoadLayout.setRefreshing(false);
            }
        });*/
        //为swipeToLoadLayout设置上拉加载更多监听者
        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mSearchBean.getData().isHasQuestionNextPage()) {
                    getMoreQuestion("" + (mSearchBean.getData().getQuestionCurPage() + 1));
                } else {
                    ToastUtils.showToastSHORT(mContext, "没有更多数据了");
                }
                //设置上拉加载更多结束
                swipeToLoadLayout.setLoadingMore(false);
            }
        });

    }


    @OnClick({R.id.title_left, R.id.title_right, R.id.btn_one, R.id.btn_two, R.id.btn_three, R.id.btn_four})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_left:
                finish();
                break;
            case R.id.title_right:
                if ("取消".equals(titleRight.getText().toString())) {
                    seachEdit.setText("");
                    titleRight.setText("搜索");
                    return;
                }
                searchKeyword = seachEdit.getText().toString();
                if (TextUtils.isEmpty(searchKeyword)) {
                    ToastUtils.showToastSHORT(mContext, "请输入搜索内容");
                    return;
                }
                getAllData(searchKeyword);
                break;
            case R.id.btn_one:
                tipChang(1);
                mHandler.sendEmptyMessage(MSG_SHOW_ClASS_ONE);
                break;
            case R.id.btn_two:
                tipChang(2);
                mHandler.sendEmptyMessage(MSG_SHOW_ClASS_TWO);
                break;
            case R.id.btn_three:
                tipChang(3);
                mHandler.sendEmptyMessage(MSG_SHOW_ClASS_THREE);
                break;
            case R.id.btn_four:
                tipChang(4);
                mHandler.sendEmptyMessage(MSG_SHOW_ClASS_FOUR);
                break;
        }
    }

    private void tipChang(int tab) {
        switch (tab) {
            case 1:
                tipOne.setBackgroundColor(getResources().getColor(R.color.textColor));
                tipTwo.setBackgroundColor(getResources().getColor(R.color.white));
                tipThree.setBackgroundColor(getResources().getColor(R.color.white));
                tipFour.setBackgroundColor(getResources().getColor(R.color.white));
                tvAll.setTextColor(getResources().getColor(R.color.textColor));
                tvBook.setTextColor(getResources().getColor(R.color.textHoldColor));
                tvQuestion.setTextColor(getResources().getColor(R.color.textHoldColor));
                tvResouce.setTextColor(getResources().getColor(R.color.textHoldColor));
                break;
            case 2:
                tipOne.setBackgroundColor(getResources().getColor(R.color.white));
                tipTwo.setBackgroundColor(getResources().getColor(R.color.textColor));
                tipThree.setBackgroundColor(getResources().getColor(R.color.white));
                tipFour.setBackgroundColor(getResources().getColor(R.color.white));
                tvAll.setTextColor(getResources().getColor(R.color.textHoldColor));
                tvBook.setTextColor(getResources().getColor(R.color.textColor));
                tvQuestion.setTextColor(getResources().getColor(R.color.textHoldColor));
                tvResouce.setTextColor(getResources().getColor(R.color.textHoldColor));
                break;
            case 3:
                tipOne.setBackgroundColor(getResources().getColor(R.color.white));
                tipTwo.setBackgroundColor(getResources().getColor(R.color.white));
                tipThree.setBackgroundColor(getResources().getColor(R.color.textColor));
                tipFour.setBackgroundColor(getResources().getColor(R.color.white));
                tvAll.setTextColor(getResources().getColor(R.color.textHoldColor));
                tvBook.setTextColor(getResources().getColor(R.color.textHoldColor));
                tvQuestion.setTextColor(getResources().getColor(R.color.textHoldColor));
                tvResouce.setTextColor(getResources().getColor(R.color.textColor));
                break;
            case 4:
                tipOne.setBackgroundColor(getResources().getColor(R.color.white));
                tipTwo.setBackgroundColor(getResources().getColor(R.color.white));
                tipThree.setBackgroundColor(getResources().getColor(R.color.white));
                tipFour.setBackgroundColor(getResources().getColor(R.color.textColor));
                tvAll.setTextColor(getResources().getColor(R.color.textHoldColor));
                tvBook.setTextColor(getResources().getColor(R.color.textHoldColor));
                tvQuestion.setTextColor(getResources().getColor(R.color.textColor));
                tvResouce.setTextColor(getResources().getColor(R.color.textHoldColor));
                break;

        }
    }


    //网路获取数据 --------------------
    public void getAllData(String searchKeyword) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("searchKeyword", searchKeyword);
            json.put("pageNum", 10);
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
                        mSearchBean = ParseUtil.parseBean(response, SearchBean.class);
                        mHandler.sendEmptyMessage(MSG_SHOW_TAB);

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    public void getMoreBook(final String curpage) {
        if (!showNetWaitLoding()) {
            return;
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
                        SearchBean Bean = ParseUtil.parseBean(response, SearchBean.class);
                        setMoreBookResponseView(Bean);

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    public void getMoreResource(final String curpage) {
        if (!showNetWaitLoding()) {
            return;
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

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).searchResource(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        SearchBean Bean = ParseUtil.parseBean(response, SearchBean.class);
                        setMoreResourceResponseView(Bean);

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    public void getMoreQuestion(final String curpage) {
        if (!showNetWaitLoding()) {
            return;
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

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).searchQuestion(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        SearchBean Bean = ParseUtil.parseBean(response, SearchBean.class);
                        setMoreQuestionResponseView(Bean);

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    public void setMoreBookResponseView(SearchBean Bean) {
        mSearchBean.getData().setBookCurPage(Bean.getData().getCurpage());
        mSearchBean.getData().setHasBookNextPage(Bean.getData().isHasBookNextPage());
        //图书 列表
        mBookList.addAll(Bean.getData().getBookList());
        mSearchTushuAdapter.refreshData(mBookList);
        if (mBookList.size() <= 0) {
//            ToastUtils.showToastSHORT(mContext, "暂无数据");

        }

    }

    public void setMoreResourceResponseView(SearchBean Bean) {
        mSearchBean.getData().setResourceCurPage(Bean.getData().getCurpage());
        mSearchBean.getData().setHasResourNextPage(Bean.getData().isHasResourNextPage());
        //图书 列表
        mResourceList.addAll(Bean.getData().getResourceList());
        if (mResourceList.size() <= 0) {
//            ToastUtils.showToastSHORT(mContext, "暂无数据");
        }
        mSearchZiYuanAdapter.refreshData(mResourceList);

    }

    public void setMoreQuestionResponseView(SearchBean Bean) {
        mSearchBean.getData().setQuestionCurPage(Bean.getData().getCurpage());
        mSearchBean.getData().setHasResourNextPage(Bean.getData().isHasQuestionNextPage());
        //套系 列表
        if (mQuestionList.size() <= 0) {
//            ToastUtils.showToastSHORT(mContext, "暂无数据");
        }
        mQuestionList.addAll(Bean.getData().getQuestionList());
        mSearchDayiAdapter.refreshData(mQuestionList);

    }


}
