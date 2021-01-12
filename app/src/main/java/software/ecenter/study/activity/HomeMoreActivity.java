package software.ecenter.study.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import software.ecenter.study.Adapter.HomeMoreBooksAdapter;
import software.ecenter.study.Adapter.HomeMoreCurriculumAdapter;
import software.ecenter.study.Adapter.HomeMoreQualityEducationAdapter;
import software.ecenter.study.R;
import software.ecenter.study.View.LoadMoreFooterView;
import software.ecenter.study.View.RefreshHeaderView;
import software.ecenter.study.View.SpinnerPopWindow;
import software.ecenter.study.bean.HomeBean.HomeMoreBooksBean;
import software.ecenter.study.bean.HomeBean.HomeMoreCurriculumBean;
import software.ecenter.study.bean.HomeBean.HomeMoreQualityEducationBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.DataUtils;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

import static software.ecenter.study.Interface.ConstantData.Grade;

/**
 * auther: zzm
 * Date: 2019/8/14
 * Description:
 */
public class HomeMoreActivity extends BaseActivity implements OnLoadMoreListener {
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.grade_text)
    TextView gradeText;
    @BindView(R.id.grade_tip)
    ImageView gradeTip;
    @BindView(R.id.btn_grade)
    LinearLayout btnGrade;
    @BindView(R.id.btn_shuxue)
    ImageButton btnShuxue;
    @BindView(R.id.btn_yuwen)
    ImageButton btnYuwen;
    @BindView(R.id.btn_yingyu)
    ImageButton btnYingyu;
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
    @BindView(R.id.mRootView)
    LinearLayout mRootView;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.nodata)
    TextView nodata;
    private String mSubject = "数学";
    private String grade;
    private SpinnerPopWindow spinnerPopWindow;
    private int type = 0;//0图书 1精品 2素质
    private List<HomeMoreBooksBean.DataBean.BookListBean> booksList;
    private HomeMoreBooksAdapter homeMoreBooksAdapter;
    private HomeMoreBooksBean booksBean;
    private List<HomeMoreCurriculumBean.DataBean.CurriculumListBean> curriculumListBeans;
    private HomeMoreCurriculumAdapter homeMoreCurriculumAdapter;
    private HomeMoreCurriculumBean homeMoreCurriculumBean;
    private List<HomeMoreQualityEducationBean.DataBean.QualityEducationListBean> qualityEducationListBeans;
    private HomeMoreQualityEducationAdapter homeMoreQualityEducationAdapter;
    private HomeMoreQualityEducationBean homeMoreQualityEducationBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_more);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 0);
        mSubject = getIntent().getStringExtra("mSubject");
        grade = getIntent().getStringExtra("grade");
        if (TextUtils.isEmpty(grade)) {
            setGrade();
        } else {
            gradeText.setText(grade);
        }
        initView();
        showSubject();
    }

    private void initView() {
        btnShuxue.setBackground(getResources().getDrawable(R.drawable.shuxue2));
        btnYingyu.setBackground(getResources().getDrawable(R.drawable.yingyu1));
        btnYuwen.setBackground(getResources().getDrawable(R.drawable.yuwen1));
        // 年级
        final List<String> spinnerList = ToolUtil.StringToArray(Grade, ",");
        spinnerPopWindow = new SpinnerPopWindow(mContext);
        spinnerPopWindow.refreshData(spinnerList);
        spinnerPopWindow.setPopTitle("选择年级");
        spinnerPopWindow.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                gradeText.setText(spinnerList.get(position));
                grade = spinnerList.get(position);
                onRefresh();
            }
        });
        spinnerPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                gradeTip.setImageResource(R.drawable.grade_tip1);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        swipeTarget.setLayoutManager(manager);
        swipeToLoadLayout.setRefreshEnabled(false);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setLoadMoreEnabled(true);
        if (type == 0) {  //图书资源
            titleContent.setText("图书资源");
            booksList = new ArrayList<>();
            homeMoreBooksAdapter = new HomeMoreBooksAdapter(mContext, booksList);
            homeMoreBooksAdapter.setItemClickListener(new HomeMoreBooksAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(mContext, BookDetailActivity.class);
                    intent.putExtra("bookId", booksList.get(position).getBookId());
                    startActivity(intent);
                }
            });
            swipeTarget.setAdapter(homeMoreBooksAdapter);
        } else if (type == 1) {//精品课程
            titleContent.setText("精品课程");
            curriculumListBeans = new ArrayList<>();
            homeMoreCurriculumAdapter = new HomeMoreCurriculumAdapter(mContext, curriculumListBeans);
            homeMoreCurriculumAdapter.setItemClickListener(new HomeMoreCurriculumAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if ("CURRICULUM".equals(curriculumListBeans.get(position).getType())) {
                        Intent intent = new Intent(mContext, KengChengDetailActivity.class);
                        intent.putExtra("curriculumId", curriculumListBeans.get(position).getId());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent();
                        int categoryShowType = curriculumListBeans.get(position).getCategoryShowType();
                        intent.putExtra("id", curriculumListBeans.get(position).getId());
                        if (categoryShowType == 0) {//课程列表
                            intent.setClass(mContext, WholeBookCourseActivity.class);
                        } else {//章节列表
                            intent.setClass(mContext, WholeBookChapterActivity.class);
                        }
                        startActivity(intent);
                    }
                }
            });
            swipeTarget.setAdapter(homeMoreCurriculumAdapter);
        } else { //素质教育
            titleContent.setText("素质教育");
            qualityEducationListBeans = new ArrayList<>();
            homeMoreQualityEducationAdapter = new HomeMoreQualityEducationAdapter(mContext, qualityEducationListBeans);
            homeMoreQualityEducationAdapter.setItemClickListener(new HomeMoreQualityEducationAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (3 == qualityEducationListBeans.get(position).getType()) {
                        Intent intent = new Intent(mContext, KengChengDetailActivity.class);
                        intent.putExtra("curriculumId", qualityEducationListBeans.get(position).getId());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, WebActivity.class);
                        intent.putExtra("title", qualityEducationListBeans.get(position).getName());
                        intent.putExtra("id", qualityEducationListBeans.get(position).getId());
                        intent.putExtra("type", 1);
                        startActivity(intent);
                    }
                }
            });
            swipeTarget.setAdapter(homeMoreQualityEducationAdapter);
        }
    }

    private void setGrade() {
        //设置默认年级
        String curGrade = AccountUtil.getRealGrade(mContext);
        grade = curGrade;
        gradeText.setText(grade);
    }


    @OnClick({R.id.btn_left_title, R.id.btn_refresh_net, R.id.btn_grade, R.id.btn_shuxue, R.id.btn_yuwen, R.id.btn_yingyu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                finish();
                break;
            case R.id.btn_refresh_net:
                onRefresh();
                break;
            case R.id.btn_grade:
                spinnerPopWindow.showAtLocation(mRootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                gradeTip.setImageResource(R.drawable.grade_tip2);
                break;
            case R.id.btn_shuxue:
                if (!"数学".equals(mSubject)) {
                    mSubject = "数学";
                    showSubject();
                }
                break;
            case R.id.btn_yuwen:
                if (!"语文".equals(mSubject)) {
                    mSubject = "语文";
                    showSubject();
                }
                break;
            case R.id.btn_yingyu:
                if (!"英语".equals(mSubject)) {
                    mSubject = "英语";
                    showSubject();
                }
                break;
        }
    }

    private void showSubject() {
        switch (mSubject) {
            case "数学":
                btnShuxue.setBackground(getResources().getDrawable(R.drawable.shuxue2));
                btnYingyu.setBackground(getResources().getDrawable(R.drawable.yingyu1));
                btnYuwen.setBackground(getResources().getDrawable(R.drawable.yuwen1));
                break;
            case "语文":
                btnShuxue.setBackground(getResources().getDrawable(R.drawable.shuxue1));
                btnYingyu.setBackground(getResources().getDrawable(R.drawable.yingyu1));
                btnYuwen.setBackground(getResources().getDrawable(R.drawable.yuwen2));
                break;
            case "英语":
                btnShuxue.setBackground(getResources().getDrawable(R.drawable.shuxue1));
                btnYingyu.setBackground(getResources().getDrawable(R.drawable.yingyu2));
                btnYuwen.setBackground(getResources().getDrawable(R.drawable.yuwen1));
                break;
        }
        onRefresh();
    }

    private void onRefresh() {
        if (type == 0) {
            getBookListData(0);
        } else if (type == 1) {
            getCurriculumData(0);
        } else {
            getQualityEducationData(0);
        }
    }


    /**
     * 获取图书
     *
     * @param page
     */
    private void getBookListData(int page) {
        JSONObject json = new JSONObject();
        try {
            json.put("curpage", page);
            json.put("pageNum", 10);
            json.put("grade", grade);
            json.put("subject", mSubject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).moreBook(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        booksBean = ParseUtil.parseBean(response, HomeMoreBooksBean.class);
                        if ("0".equals(booksBean.getData().getCurpage()) && booksBean.getData().getBookList().size() == 0) {
                            nodata.setVisibility(View.VISIBLE);
                            listSearchClassLine.setVisibility(View.GONE);
                        } else {
                            if ("0".equals(booksBean.getData().getCurpage())) {
                                booksList.clear();
                            }
                            booksList.addAll(booksBean.getData().getBookList());
                            nodata.setVisibility(View.GONE);
                            listSearchClassLine.setVisibility(View.VISIBLE);
                            homeMoreBooksAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    /**
     * 获取精品
     *
     * @param page
     */
    private void getCurriculumData(int page) {
        JSONObject json = new JSONObject();
        try {
            json.put("curpage", page);
            json.put("pageNum", 10);
            json.put("grade", grade);
            json.put("subject", mSubject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).classIndex_curriculum(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        homeMoreCurriculumBean = ParseUtil.parseBean(response, HomeMoreCurriculumBean.class);
                        if ("0".equals(homeMoreCurriculumBean.getData().getCurpage()) && homeMoreCurriculumBean.getData().getCurriculumList().size() == 0) {
                            nodata.setVisibility(View.VISIBLE);
                            listSearchClassLine.setVisibility(View.GONE);
                        } else {
                            if ("0".equals(homeMoreCurriculumBean.getData().getCurpage())) {
                                curriculumListBeans.clear();
                            }
                            curriculumListBeans.addAll(homeMoreCurriculumBean.getData().getCurriculumList());
                            nodata.setVisibility(View.GONE);
                            listSearchClassLine.setVisibility(View.VISIBLE);
                            homeMoreCurriculumAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    /**
     * 获取素质
     *
     * @param page
     */
    private void getQualityEducationData(int page) {
        JSONObject json = new JSONObject();
        try {
            json.put("curpage", page);
            json.put("pageNum", 10);
            json.put("grade", grade);
            json.put("subject", mSubject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).moreQualityEducation(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        homeMoreQualityEducationBean = ParseUtil.parseBean(response, HomeMoreQualityEducationBean.class);
                        List<HomeMoreQualityEducationBean.DataBean.QualityEducationListBean> list = homeMoreQualityEducationBean.getData().getQualityEducationList();
                        if ("0".equals(homeMoreQualityEducationBean.getData().getCurpage()) && list.size() == 0) {
                            nodata.setVisibility(View.VISIBLE);
                            listSearchClassLine.setVisibility(View.GONE);
                        } else {
                            if ("0".equals(homeMoreQualityEducationBean.getData().getCurpage())) {
                                qualityEducationListBeans.clear();
                            }
                            qualityEducationListBeans.addAll(homeMoreQualityEducationBean.getData().getQualityEducationList());
                            nodata.setVisibility(View.GONE);
                            listSearchClassLine.setVisibility(View.VISIBLE);
                            homeMoreQualityEducationAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    @Override
    public void onLoadMore() {
        if (type == 0) {
            if (booksBean != null && booksBean.getData().isHasBookNextPage()) {
                getBookListData(Integer.parseInt(booksBean.getData().getCurpage()) + 1);
            } else {
                ToastUtils.showToastSHORT(mContext, "没有更多数据了");
            }
        } else if (type == 1) {
            if (homeMoreCurriculumBean.getData().isHasCurriculumNextPage()) {
                getCurriculumData(Integer.parseInt(homeMoreCurriculumBean.getData().getCurpage()) + 1);
            } else {
                ToastUtils.showToastSHORT(mContext, "没有更多数据了");
            }
        } else {
            if (homeMoreQualityEducationBean.getData().isHasNextPage()) {
                getQualityEducationData(Integer.parseInt(homeMoreQualityEducationBean.getData().getCurpage()) + 1);
            } else {
                ToastUtils.showToastSHORT(mContext, "没有更多数据了");
            }
        }

        //设置上拉加载更多结束
        swipeToLoadLayout.setLoadingMore(false);
    }


}
