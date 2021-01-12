package software.ecenter.study.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.Adapter.BookAdapter;
import software.ecenter.study.Adapter.KechengAdapter;
import software.ecenter.study.Adapter.QualityEducationAdapter;
import software.ecenter.study.Interface.ConstantData;
import software.ecenter.study.R;
import software.ecenter.study.View.SpinnerPopWindow;
import software.ecenter.study.bean.HomeBean.BookBean;
import software.ecenter.study.bean.HomeBean.CurriculumBean;
import software.ecenter.study.bean.HomeBean.MybagBean;
import software.ecenter.study.bean.HomeBean.QualityEducationBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.NetworkUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * dec 我的书包
 */
public class MyBagActivity extends BaseActivity implements ConstantData {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.btn_grade)
    LinearLayout btnGrade;
    @BindView(R.id.btn_shuxue)
    ImageButton btnShuxue;
    @BindView(R.id.btn_yuwen)
    ImageButton btnYuwen;
    @BindView(R.id.btn_yingyu)
    ImageButton btnYingyu;
    @BindView(R.id.list_one)
    RecyclerView listOne;
    @BindView(R.id.list_two)
    RecyclerView listTwo;
    @BindView(R.id.list_three)
    RecyclerView listThree;
    @BindView(R.id.grade_text)
    TextView gradeText;
    @BindView(R.id.grade_tip)
    ImageView gradeTip;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.ll_search_all_no_data)
    LinearLayout ll_search_all_no_data;
    @BindView(R.id.rl_book)
    RelativeLayout rlBook;
    @BindView(R.id.rl_kechen)
    RelativeLayout rlKechen;
    @BindView(R.id.rl_shuzhi)
    RelativeLayout rlShuzhi;

    private SpinnerPopWindow spinnerPopWindow;

    private List<BookBean> ListDataOne;
    private List<CurriculumBean> ListDataTwo;
    private List<QualityEducationBean> ListDataThree;


    private BookAdapter adapterOne;
    private KechengAdapter adapterTwo;
    private QualityEducationAdapter adapterThree;
    private String grade;
    private String subject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bag);
        ButterKnife.bind(this);
        listOne.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        listTwo.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        listThree.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        subject = getIntent().getStringExtra("mSubject");
        grade = getIntent().getStringExtra("grade");
        btnShuxue.setBackground(getResources().getDrawable(R.drawable.shuxue2));
        gradeText.setText(grade);
        showSubject();
    }

    public void getData() {

        if (NetworkUtil.isConnected(mContext)) {
            if (!mNetWatiDialog.isShowing()) {
                mNetWatiDialog.show();
            }
        } else {
            ToastUtils.showToastLONG(mContext, "此功能需在联网状态下使用，已下载资源可在“我的下载”中查看");
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        JSONObject json = new JSONObject();
        try {
            json.put("grade", grade);
            json.put("subject", subject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getUserBag(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.GONE);
                        MybagBean bean = ParseUtil.parseBean(response, MybagBean.class);
                        setResponseView(bean);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }


    public void setResponseView(MybagBean bean) {


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
                getData();

            }
        });
        spinnerPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                gradeTip.setImageResource(R.drawable.grade_tip1);
            }
        });

        // 图书
        ListDataOne = bean.getData().getBookList();
        if (ListDataOne.size() == 0) {
            rlBook.setVisibility(View.GONE);
        } else {
            rlBook.setVisibility(View.VISIBLE);
        }

        adapterOne = new BookAdapter(mContext, ListDataOne);
        adapterOne.setItemClickListener(new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(mContext, BookDetailActivity.class);
                intent.putExtra("bookId", ListDataOne.get(position).getBookId());
                startActivity(intent);
            }
        });
        listOne.setAdapter(adapterOne);

        // 课程
        ListDataTwo = bean.getData().getCurriculumList();
        if (ListDataTwo.size() == 0) {
            rlKechen.setVisibility(View.GONE);
        } else {
            rlKechen.setVisibility(View.VISIBLE);
        }

        adapterTwo = new KechengAdapter(mContext, ListDataTwo);
        adapterTwo.setType(1);
        adapterTwo.setItemClickListener(new KechengAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                Intent intent = new Intent(mContext, KengChengDetailActivity.class);
//                intent.putExtra("curriculumId", ListDataTwo.get(position).getCurriculumId());
//                startActivity(intent);
                if ("CURRICULUM".equals(ListDataTwo.get(position).getType())) {
                    Intent intent = new Intent(mContext, KengChengDetailActivity.class);
                    intent.putExtra("curriculumId", ListDataTwo.get(position).getCurriculumId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, WholeBookChapterActivity.class);
                    int categoryShowType = ListDataTwo.get(position).getCategoryShowType();
                    intent.putExtra("id", ListDataTwo.get(position).getCurriculumId());
                    if (categoryShowType == 0) {//课程列表
                        intent.setClass(mContext, WholeBookCourseActivity.class);
                    } else {//章节列表
                        intent.setClass(mContext, WholeBookChapterActivity.class);
                    }
                    startActivity(intent);
                }
            }
        });
        listTwo.setAdapter(adapterTwo);

        // 素质教育
        ListDataThree = bean.getData().getQualityEducationList();
        if (ListDataThree.size() == 0) {
            rlShuzhi.setVisibility(View.GONE);
        } else {
            rlShuzhi.setVisibility(View.VISIBLE);
        }

        adapterThree = new QualityEducationAdapter(mContext, ListDataThree);
        adapterThree.setItemClickListener(new QualityEducationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (3 == ListDataThree.get(position).getType()) {
                    Intent intent = new Intent(mContext, KengChengDetailActivity.class);
                    intent.putExtra("curriculumId", ListDataThree.get(position).getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, WebActivity.class);
                    intent.putExtra("title", ListDataThree.get(position).getName());
                    intent.putExtra("id", ListDataThree.get(position).getId());
                    intent.putExtra("type", 1);
                    startActivity(intent);
                }
            }
        });
        listThree.setAdapter(adapterThree);

        if (ListDataOne.size() == 0&&ListDataTwo.size() == 0&&ListDataThree.size() == 0) {
            ll_search_all_no_data.setVisibility(View.VISIBLE);
        } else {
            ll_search_all_no_data.setVisibility(View.GONE);
        }

    }


    @OnClick({R.id.btn_left_title, R.id.btn_grade, R.id.btn_shuxue, R.id.btn_yuwen, R.id.btn_yingyu, R.id.btn_refresh_net})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_grade:
                spinnerPopWindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                gradeTip.setImageResource(R.drawable.grade_tip2);
                break;
            case R.id.btn_shuxue:
                if (!"数学".equals(subject)) {
                    subject = "数学";
                    showSubject();
                }
                break;
            case R.id.btn_yuwen:
                if (!"语文".equals(subject)) {
                    subject = "语文";
                    showSubject();
                }
                break;
            case R.id.btn_yingyu:
                if (!"英语".equals(subject)) {
                    subject = "英语";
                    showSubject();
                }
                break;
            case R.id.btn_refresh_net:
                getData();
                break;
        }
    }
    private void showSubject() {
        switch (subject) {
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
        getData();
    }
}
