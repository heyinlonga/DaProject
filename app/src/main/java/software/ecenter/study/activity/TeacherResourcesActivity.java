package software.ecenter.study.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import software.ecenter.study.Adapter.HomeMoreBooksAdapter;
import software.ecenter.study.Adapter.TeacherResAdapter;
import software.ecenter.study.R;
import software.ecenter.study.View.LoadMoreFooterView;
import software.ecenter.study.View.RefreshHeaderView;
import software.ecenter.study.View.SpinnerPopWindow;
import software.ecenter.study.bean.HomeBean.HomeMoreBooksBean;
import software.ecenter.study.bean.HomeBean.TeacherResBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

import static software.ecenter.study.Interface.ConstantData.Grade;

/**
 * auther: zzm
 * Date: 2019/8/15
 * Description: 教师资源
 */
public class TeacherResourcesActivity extends BaseActivity implements OnLoadMoreListener {
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.grade_text)
    TextView gradeText;
    @BindView(R.id.ll_search_all_no_data)
    LinearLayout ll_search_all_no_data;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView swipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView swipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.tv_Activation)
    TextView tvActivation;
    @BindView(R.id.btn_shuxue)
    ImageButton btnShuxue;
    @BindView(R.id.btn_yuwen)
    ImageButton btnYuwen;
    @BindView(R.id.btn_yingyu)
    ImageButton btnYingyu;
    @BindView(R.id.list_search_class_line)
    LinearLayout listSearchClassLine;
    @BindView(R.id.grade_tip)
    ImageView gradeTip;
    @BindView(R.id.mRootView)
    LinearLayout mRootView;
    private String mSubject = "数学";
    private String grade;
    private SpinnerPopWindow spinnerPopWindow;

    private List<TeacherResBean.DataBean.PackagesBean> teacherResList;
    private TeacherResAdapter teacherResAdapter;
    private TeacherResBean teacherResBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_resources);
        ButterKnife.bind(this);
        grade = getIntent().getStringExtra("grade");
        if (TextUtils.isEmpty(grade)) {
            setGrade();
        } else {
            gradeText.setText(grade);
        }
        initView();
        onRefresh();
    }

    private void setGrade() {
        //设置默认年级
        String curGrade = AccountUtil.getRealGrade(mContext);
        grade = curGrade;
        gradeText.setText(grade);
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
        teacherResList = new ArrayList<>();
        teacherResAdapter = new TeacherResAdapter(teacherResList);
        teacherResAdapter.setItemClickListener(new TeacherResAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                TeacherResBean.DataBean.PackagesBean bean = teacherResList.get(position);
                if (!IsActive)
                    if (bean.isFree()) {
//                        ToastUtils.showToastSHORT(mContext, "该资源包为试看资源，如需查看所有资源需进行激活。");
                    } else {
                        startActivityForResult(TeacherActivationActivity.class, 1000);
//                        ToastUtils.showToastSHORT(mContext, "您的教师空间尚未激活，只能查看试用资源，如需查看或下载更多资源，请使用《黄冈小状元》系列教师用书上的激活码激活教师空间~ ");
                        return;
                    }
                Intent intent = new Intent(mContext, TeacherResDetailActivity.class);
                intent.putExtra("id", teacherResList.get(position).getId());
                startActivity(intent);
            }
        });
        swipeTarget.setAdapter(teacherResAdapter);
    }

    @OnClick({R.id.btn_left_title, R.id.btn_refresh_net, R.id.btn_grade, R.id.btn_shuxue, R.id.btn_yuwen, R.id.btn_yingyu, R.id.tv_Activation})
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
                    btnShuxue.setBackground(getResources().getDrawable(R.drawable.shuxue2));
                    btnYingyu.setBackground(getResources().getDrawable(R.drawable.yingyu1));
                    btnYuwen.setBackground(getResources().getDrawable(R.drawable.yuwen1));
                    onRefresh();
                }
                break;
            case R.id.btn_yuwen:
                if (!"语文".equals(mSubject)) {
                    btnShuxue.setBackground(getResources().getDrawable(R.drawable.shuxue1));
                    btnYingyu.setBackground(getResources().getDrawable(R.drawable.yingyu1));
                    btnYuwen.setBackground(getResources().getDrawable(R.drawable.yuwen2));
                    mSubject = "语文";
                    onRefresh();
                }
                break;
            case R.id.btn_yingyu:
                if (!"英语".equals(mSubject)) {
                    mSubject = "英语";
                    btnShuxue.setBackground(getResources().getDrawable(R.drawable.shuxue1));
                    btnYingyu.setBackground(getResources().getDrawable(R.drawable.yingyu2));
                    btnYuwen.setBackground(getResources().getDrawable(R.drawable.yuwen1));
                    onRefresh();
                }
                break;
            case R.id.tv_Activation:
                startActivityForResult(TeacherActivationActivity.class, 1000);
                break;
        }
    }

    private void onRefresh() {
        getData(0);
    }

    private boolean IsActive = false;

    private void getData(int page) {
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

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getteacherResourceList(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        teacherResBean = ParseUtil.parseBean(response, TeacherResBean.class);
                        boolean isFirst = "0".equals(teacherResBean.getData().getCurpage());
                        if (isFirst) {
                            IsActive = teacherResBean.getData().isIsActive();
                            teacherResAdapter.setActive(IsActive);
                            tvActivation.setVisibility(teacherResBean.getData().isIsActive() ? View.GONE : View.VISIBLE);
                        }
                        if (isFirst && teacherResBean.getData().getPackages().size() == 0) {
                            ll_search_all_no_data.setVisibility(View.VISIBLE);
                            listSearchClassLine.setVisibility(View.GONE);
                        } else {
                            if (isFirst) {
                                teacherResList.clear();
                            }
                            teacherResList.addAll(teacherResBean.getData().getPackages());
                            ll_search_all_no_data.setVisibility(View.GONE);
                            listSearchClassLine.setVisibility(View.VISIBLE);
                            teacherResAdapter.notifyDataSetChanged();
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
        if (teacherResBean != null && teacherResBean.getData().isHasNextPage()) {
            getData(Integer.parseInt(teacherResBean.getData().getCurpage()) + 1);
        } else {
            ToastUtils.showToastSHORT(mContext, "没有更多数据了");
        }
        //设置上拉加载更多结束
        swipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            IsActive = true;
            teacherResAdapter.setActive(IsActive);
            teacherResAdapter.notifyDataSetChanged();
        }
    }
}
