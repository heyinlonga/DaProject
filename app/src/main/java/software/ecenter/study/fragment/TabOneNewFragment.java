package software.ecenter.study.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.zxing.integration.android.IntentIntegrator;
import com.youth.banner.Banner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.Adapter.BookNewAdapter;
import software.ecenter.study.Adapter.HomeHotAdapter;
import software.ecenter.study.Adapter.KechengNewAdapter;
import software.ecenter.study.Adapter.QualityEducationNewAdapter;
import software.ecenter.study.Interface.ConstantData;
import software.ecenter.study.R;
import software.ecenter.study.View.SpinnerPopWindow;
import software.ecenter.study.activity.BannerDetailActivity;
import software.ecenter.study.activity.BookDetailActivity;
import software.ecenter.study.activity.ChapterDetailsActivity;
import software.ecenter.study.activity.HomeMoreActivity;
import software.ecenter.study.activity.KengChengDetailActivity;
import software.ecenter.study.activity.MyBagActivity;
import software.ecenter.study.activity.ScanActivity;
import software.ecenter.study.activity.SearchDActivity;
import software.ecenter.study.activity.SeeResourcesVideoActivity;
import software.ecenter.study.activity.TeacherResourcesActivity;
import software.ecenter.study.activity.WebActivity;
import software.ecenter.study.activity.WholeBookChapterActivity;
import software.ecenter.study.activity.WholeBookCourseActivity;
import software.ecenter.study.activity.WholeCourseDetailActivity;
import software.ecenter.study.bean.HomeBean.HomeNewBean;
import software.ecenter.study.bean.HomeBean.ResourceIdBean;
import software.ecenter.study.bean.HomeBean.SecurityCodeBean;
import software.ecenter.study.bean.MineBean.PersonDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.DataUtils;
import software.ecenter.study.utils.JpushUtil;
import software.ecenter.study.utils.Logger;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.PermissionUtils;
import software.ecenter.study.utils.ScreenUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

/**
 * dec 课堂首页
 * Created by Mike on 2018/4/25.
 */

public class TabOneNewFragment extends BaseFragment implements ConstantData {
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.title_left)
    ImageView titleLeft;
    @BindView(R.id.seach_img)
    ImageView seachImg;
    @BindView(R.id.seach_btn)
    LinearLayout seachBtn;
    @BindView(R.id.title_right)
    ImageView titleRight;
    @BindView(R.id.top)
    RelativeLayout top;
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
    Unbinder unbinder;
    @BindView(R.id.grade_text)
    TextView gradeText;
    @BindView(R.id.grade_tip)
    ImageView gradeTip;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.book_w)
    TextView bookw;
    @BindView(R.id.book_two)
    TextView booktwo;
    @BindView(R.id.book_three)
    TextView bookthree;
    @BindView(R.id.home_iv_yin_dao1)
    ImageView homeIvYinDao;
    @BindView(R.id.home_iv_yin_dao2)
    ImageView homeIvYinDao2;
    @BindView(R.id.home_rl_yin_dao)
    RelativeLayout homeRlYinDao;
    @BindView(R.id.rc_hot)
    RecyclerView rcHot;
    @BindView(R.id.tvTwoMore)
    TextView tvTwoMore;
    @BindView(R.id.tvThreeMore)
    TextView tvThreeMore;
    @BindView(R.id.ll_hot)
    LinearLayout ll_hot;
    @BindView(R.id.rl_book)
    RelativeLayout rl_book;
    @BindView(R.id.rl_kechen)
    RelativeLayout rl_kechen;
    @BindView(R.id.rl_shuzhi)
    RelativeLayout rl_shuzhi;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;


    private SpinnerPopWindow spinnerPopWindow;

    private HomeHotAdapter homeHotAdapter;
    private BookNewAdapter adapterOne;
    private KechengNewAdapter adapterTwo;
    private QualityEducationNewAdapter adapterThree;
    private List<HomeNewBean.DataBean.IndexHotListBean> indexHotListBeans;
    private List<HomeNewBean.DataBean.BannerListBean> listBanner;
    private List<HomeNewBean.DataBean.BookListBean> ListDataOne;
    private List<HomeNewBean.DataBean.CurriculumListBean> ListDataTwo;
    private List<HomeNewBean.DataBean.QualityEducationListBean> ListDataThree;
    private HomeNewBean mHomeBean;

    private String mSubject;
    private String grade;
    private String userGrade;

    //扫一扫
    private int REQUEST_CODE_SCAN = 111;
    //扫一扫对应资源类型
    private static final String TYPE_BOOK_QRCODE = "bookQrcode"; //图书资源
    private static final String TYPE_RESOURCE_QRCODE = "resourceQrcode";  //资源详情跳转
    private static final String TYPE_CONTENT_QRCODE = "contentQrcode";   //章节跳转
    private static final String TYPE_CURRICULUM_QRCODE = "curriculumQrcode"; //课程跳转
    private static final String TYPE_SECURITY_QRCODE = "securityQrcode"; //课程跳转
    private static final String TYPE_LECTURE_QRCODE = "lectureQrcode"; //讲座
    private static final String TYPE_LECTURE_CATEGORY_QRCODE = "lectureCategoryQrcode"; //讲座目录
    private static final String TYPE_LECTURE_RESOURCE_QRCODE = "lectureResourceQrcode"; //讲座资源
    private static final String TYPE_BOOK_VIDEO = "qrbookvideo"; //图书视频资源
    private boolean isLoad;

    private HashMap<Integer, Integer> abnormalQrcodeMap;
    private int abnormalQrcodeValue = 615210;
    private int abnormalQrcodeKeyMin = 6190;
    private int abnormalQrcodeKeyMax = 6205;

    private int abnormalQrcodeKeyMin2 = 2834;
    private int abnormalQrcodeKeyMax2 = 2865;

    private HashMap<Integer, String> abnormalTitleMap;

    private String[] errTitles = new String[]{
            "模块一 数学文化",
            "模块二 数与代数",
            "模块三 应用专题",
            "模块四 图形与几何",
            "模块五 实践综合",
            "模块一 数学文化",
            "模块二 数与代数",
            "模块三 图形几何",
            "模块四 应用专题",
            "模块五 实践综合",
            "模块一 数学文化",
            "模块二 数与代数",
            "模块三 图形几何",
            "模块四 应用专题",
            "模块五 实践综合",
            "模块一 数学文化",
            "模块二 数与代数",
            "模块三 图形与几何",
            "模块四 应用专题",
            "模块五 概率统计",
            "模块六 实践综合",
            "模块一 数学文化",
            "模块二 数与代数",
            "模块三 应用专题",
            "模块四 图形与几何",
            "模块五 实践综合",
            "模块一 数学文化",
            "模块二 数与代数",
            "模块三 图形与几何",
            "模块四 应用专题",
            "模块五 概率统计",
            "模块六 实践综合"
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        abnormalQrcodeMap = new HashMap<>();
        for (int i = abnormalQrcodeKeyMin; i <= abnormalQrcodeKeyMax; i++){
            abnormalQrcodeMap.put(i, abnormalQrcodeValue);
            abnormalQrcodeValue++;
        }

        abnormalTitleMap = new HashMap<>();
        abnormalQrcodeValue = 764427;
        for (int i = abnormalQrcodeKeyMin2; i <= abnormalQrcodeKeyMax2; i++){
            abnormalQrcodeMap.put(i, abnormalQrcodeValue);
            abnormalTitleMap.put(i, errTitles[i - abnormalQrcodeKeyMin2]);

            if (abnormalQrcodeValue == 764431){
                abnormalQrcodeValue = 764827;
            }

            if (abnormalQrcodeValue == 764832){
                abnormalQrcodeValue = 765222;
            }

            if (abnormalQrcodeValue == 765227){
                abnormalQrcodeValue = 765631;
            }

            if (abnormalQrcodeValue == 765637){
                abnormalQrcodeValue = 766012;
            }

            if (abnormalQrcodeValue == 766017){
                abnormalQrcodeValue = 766430;
            }

            abnormalQrcodeValue++;
        }

        mRootView = inflater.inflate(R.layout.fragment_one_new, null);
        unbinder = ButterKnife.bind(this, mRootView);

        refresh.setColorSchemeResources(R.color.background);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllData(gradeText.getText().toString(), mSubject);
            }
        });

        LinearLayoutManager linearLayoutManagerOne = new LinearLayoutManager(mContext);
        linearLayoutManagerOne.setOrientation(LinearLayoutManager.HORIZONTAL);
        listOne.setLayoutManager(linearLayoutManagerOne);

        LinearLayoutManager linearLayoutManagerTwo = new LinearLayoutManager(mContext);
        linearLayoutManagerTwo.setOrientation(LinearLayoutManager.HORIZONTAL);
        listTwo.setLayoutManager(linearLayoutManagerTwo);

        LinearLayoutManager linearLayoutManagerThree = new LinearLayoutManager(mContext);
        linearLayoutManagerThree.setOrientation(LinearLayoutManager.HORIZONTAL);
        listThree.setLayoutManager(linearLayoutManagerThree);

        LinearLayoutManager linearLayoutManagerHote = new LinearLayoutManager(mContext);
        linearLayoutManagerHote.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcHot.setLayoutManager(linearLayoutManagerHote);
        getUserInfo();
        setGrade();

        btnShuxue.setBackground(getResources().getDrawable(R.drawable.shuxue2));
        mSubject = "数学";
        getAllData(gradeText.getText().toString(), mSubject);
        boolean isOpenFirstOpenHome = AccountUtil.getFirstOpenHome(mContext);

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
                getAllData(gradeText.getText().toString(), mSubject);
            }
        });
        spinnerPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                gradeTip.setImageResource(R.drawable.grade_tip1);
            }
        });
        return mRootView;
    }


    private void setGrade() {
        //设置默认年级
        DataUtils getData = new DataUtils().invoke();
        int mMonth = getData.getmMonth();
        int mDay = getData.getmDay();
        String curGrade = AccountUtil.getRealGrade(mContext);
        if (TextUtils.isEmpty(userGrade) || !userGrade.contains(curGrade)) {
            grade = curGrade;
            userGrade = curGrade;
        }
        gradeText.setText(grade);
    }

    public void setResponseView(HomeNewBean homeBean) {
        mHomeBean = homeBean;

        //假数据广告 https://www.jianshu.com/p/d229a647e705
        listBanner = mHomeBean.getData().getBannerList();
        String[] images = new String[listBanner.size()];
        for (int i = 0; i < images.length; i++) {
            images[i] = listBanner.get(i).getImgUrl();
        }
        ViewGroup.LayoutParams layoutParams = banner.getLayoutParams();
        layoutParams.height = ScreenUtil.getScreenWidth(mContext) * 270 / 710;
        banner.setLayoutParams(layoutParams);
        //设置图片标题:自动对应
        //String[] titles = new String[]{"全场2折起", "十大星级品牌联盟", "嗨购5折不要停", "12趁现在", "嗨购5折不要停，12.12趁现在", "实打实大顶顶顶顶"};
        banner.setBannerStyle(Banner.CIRCLE_INDICATOR_TITLE);//显示圆形指示器和标题
        //设置轮播要显示的标题和图片对应（如果不传默认不显示标题）
        //banner.setBannerTitle(titles);
        banner.setIndicatorGravity(Banner.CENTER);//指示器居中
        //设置是否自动轮播（不设置则默认自动）
        banner.isAutoPlay(true);
        //设置轮播图片间隔时间（不设置默认为2000）
        banner.setDelayTime(5000);
        //设置图片资源:可选图片网址/资源文件，默认用Glide加载,也可自定义图片的加载框架
        //所有设置参数方法都放在此方法之前执行
        banner.setImages(images);

        //设置点击事件，下标是从1开始
        banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {//设置点击事件
            @Override
            public void OnBannerClick(View view, int position) {
                String type = listBanner.get(position - 1).getType();
                int categoryShowType = listBanner.get(position - 1).getCategoryShowType();
                String relatedId = listBanner.get(position - 1).getRelatedId();
                Intent intent = new Intent();
                switch (type) {
                    case "BOOK"://图书
                        intent.setClass(mContext, BookDetailActivity.class);
                        intent.putExtra("bookId", relatedId);
                        break;
                    case "CURRICULUM"://课程
                        intent.setClass(mContext, KengChengDetailActivity.class);
                        intent.putExtra("curriculumId", relatedId);
                        break;
                    case "LECTURE"://讲座
                        if (categoryShowType == 0) {//课程列表
                            intent.setClass(mContext, WholeBookCourseActivity.class);
                        } else {//章节列表
                            intent.setClass(mContext, WholeBookChapterActivity.class);
                        }
                        intent.putExtra("id", relatedId);
                        break;
                    case "RICHTEXT"://富文本
                        intent.setClass(mContext, BannerDetailActivity.class);
                        intent.putExtra("id", listBanner.get(position - 1).getId());
                        break;
                }
                startActivity(intent);
            }
        });


        //todo 获取数据设置rc数据 如果数据空不显示这一栏
        indexHotListBeans = mHomeBean.getData().getIndexHotList();
        if (indexHotListBeans.size() > 0) {
            ll_hot.setVisibility(View.VISIBLE);
            homeHotAdapter = new HomeHotAdapter(mContext, indexHotListBeans);
            homeHotAdapter.setItemClickListener(new HomeHotAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
//                   素质教育: CURRICULUM，精品课程: EDUCATION  ，视频讲座：VIDEO_LECTURE，阅读大赛：READ_MATCH，
//                    教师资源：TEACHER_RESOURCE，图书：BOOK
                    Intent intent = new Intent();
                    switch (indexHotListBeans.get(position).getType()) {
                        case "EDUCATION":
                        case "CURRICULUM":
                            intent.setClass(mContext, KengChengDetailActivity.class);
                            intent.putExtra("curriculumId", indexHotListBeans.get(position).getRelatedId());
                            break;
                        case "VIDEO_LECTURE":
                            int categoryShowType = indexHotListBeans.get(position).getCategoryShowType();
                            intent.putExtra("id", indexHotListBeans.get(position).getRelatedId());
                            if (categoryShowType == 0) {//课程列表
                                intent.setClass(mContext, WholeBookCourseActivity.class);
                            } else {//章节列表
                                intent.setClass(mContext, WholeBookChapterActivity.class);
                            }
                            break;
                        case "READ_MATCH":
                            ToastUtils.showToastSHORT(mContext, "阅读大赛");
                            break;
                        case "TEACHER_RESOURCE":
                            intent.setClass(mContext, TeacherResourcesActivity.class);
                            intent.putExtra("grade", grade);
                            break;
                        case "BOOK":
                            intent.setClass(mContext, BookDetailActivity.class);
                            intent.putExtra("bookId", indexHotListBeans.get(position).getRelatedId());
                            break;
                    }
                    startActivity(intent);
                }
            });
            rcHot.setAdapter(homeHotAdapter);
        } else {
            ll_hot.setVisibility(View.GONE);
        }

        //图书 列表
        ListDataOne = mHomeBean.getData().getBookList();
        if (ListDataOne.size() == 0) {
            rl_book.setVisibility(View.GONE);
            bookw.setVisibility(View.VISIBLE);
        } else {
            rl_book.setVisibility(View.VISIBLE);
            bookw.setVisibility(View.GONE);
        }
        adapterOne = new BookNewAdapter(mContext, ListDataOne);
        adapterOne.setItemClickListener(new BookNewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(mContext, BookDetailActivity.class);
                intent.putExtra("bookId", ListDataOne.get(position).getBookId());
                startActivity(intent);

            }
        });
        listOne.setAdapter(adapterOne);
        // 课程 列表
        ListDataTwo = mHomeBean.getData().getCurriculumList();
        if (ListDataTwo.size() == 0) {
            rl_kechen.setVisibility(View.GONE);
            booktwo.setVisibility(View.VISIBLE);
        } else {
            rl_kechen.setVisibility(View.VISIBLE);
            booktwo.setVisibility(View.GONE);
        }
        adapterTwo = new KechengNewAdapter(mContext, ListDataTwo);
        adapterTwo.setItemClickListener(new KechengNewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if ("CURRICULUM".equals(ListDataTwo.get(position).getType())) {
                    Intent intent = new Intent(mContext, KengChengDetailActivity.class);
                    intent.putExtra("curriculumId", ListDataTwo.get(position).getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    int categoryShowType = ListDataTwo.get(position).getCategoryShowType();
                    intent.putExtra("id", ListDataTwo.get(position).getId());
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

        //素质 列表
        ListDataThree = mHomeBean.getData().getQualityEducationList();
        if (ListDataThree.size() == 0) {
            rl_shuzhi.setVisibility(View.GONE);
            bookthree.setVisibility(View.VISIBLE);
        } else {
            rl_shuzhi.setVisibility(View.VISIBLE);
            bookthree.setVisibility(View.GONE);
        }
        adapterThree = new QualityEducationNewAdapter(mContext, ListDataThree);
        adapterThree.setItemClickListener(new QualityEducationNewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (3 == ListDataThree.get(position).getType()) {
                    Intent intent = new Intent(mContext, KengChengDetailActivity.class);
                    intent.putExtra("curriculumId", ListDataThree.get(position).getId());
                    startActivity(intent);
//                    Intent intent = new Intent(mContext, BookDetailActivity.class);
//                    intent.putExtra("bookId", ListDataThree.get(position).getId());
//                    startActivity(intent);
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

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //图片请求的权限
    public static String[] PERSSION_PHOTO = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    @OnClick({R.id.title_left, R.id.seach_btn,
            R.id.title_right, R.id.btn_shuxue,
            R.id.btn_yuwen, R.id.btn_yingyu,
            R.id.btn_grade, R.id.btn_refresh_net, R.id.iv_one, R.id.iv_two, R.id.iv_three,
            R.id.home_rl_yin_dao, R.id.tvOneMore, R.id.tvTwoMore, R.id.tvThreeMore})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_left://扫描权限
                PermissionUtils.newIntence().requestPerssion(getActivity(), PERSSION_PHOTO, 101, new PermissionUtils.IPermission() {
                    @Override
                    public void success(int code) {
                        StartZxing(); //扫码
                    }
                });
                break;
            case R.id.seach_btn:
                startActivity(new Intent(mContext, SearchDActivity.class)); //进入搜索界面
//                startActivity(new Intent(mContext, SearchActivity.class)); //进入搜索界面
                break;
            case R.id.title_right:
                startActivity(new Intent(mContext, MyBagActivity.class).putExtra("mSubject", mSubject).putExtra("grade", grade)); //进入我的书包界面
                break;
            case R.id.btn_shuxue:
                mSubject = "数学";
                btnShuxue.setBackground(getResources().getDrawable(R.drawable.shuxue2));
                btnYingyu.setBackground(getResources().getDrawable(R.drawable.yingyu1));
                btnYuwen.setBackground(getResources().getDrawable(R.drawable.yuwen1));
                getAllData(gradeText.getText().toString(), mSubject);
                break;
            case R.id.btn_yuwen:
                btnShuxue.setBackground(getResources().getDrawable(R.drawable.shuxue1));
                btnYingyu.setBackground(getResources().getDrawable(R.drawable.yingyu1));
                btnYuwen.setBackground(getResources().getDrawable(R.drawable.yuwen2));
                mSubject = "语文";
                getAllData(gradeText.getText().toString(), mSubject);
                break;
            case R.id.btn_yingyu:
                mSubject = "英语";
                btnShuxue.setBackground(getResources().getDrawable(R.drawable.shuxue1));
                btnYingyu.setBackground(getResources().getDrawable(R.drawable.yingyu2));
                btnYuwen.setBackground(getResources().getDrawable(R.drawable.yuwen1));
                getAllData(gradeText.getText().toString(), mSubject);
                break;
            case R.id.btn_grade:
                spinnerPopWindow.showAtLocation(mRootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                gradeTip.setImageResource(R.drawable.grade_tip2);
                break;
            case R.id.btn_refresh_net:
                getAllData(gradeText.getText().toString(), mSubject);
                break;
            case R.id.home_rl_yin_dao:
                if (AccountUtil.getFirstOpenHome(mContext)) {
                    AccountUtil.saveFirstOpenHome(mContext, false);
                    if (AccountUtil.getFirstOpenBookPackage(mContext)) {
                        homeIvYinDao2.setImageDrawable(getResources().getDrawable(R.drawable.yindao2));
                        homeIvYinDao.setVisibility(View.GONE);
//                        homeIvYinDao.
                    }
                } else {
                    AccountUtil.saveFirstOpenBookPackage(mContext, false);
                    homeRlYinDao.setVisibility(View.GONE);
                }
                break;
            case R.id.tvOneMore:
            case R.id.iv_one:
                //todo 图书资源更多
                jumpHomeMore(0);
                break;
            case R.id.tvTwoMore:
            case R.id.iv_two:
                //todo 精品课程更多
                jumpHomeMore(1);
                break;
            case R.id.tvThreeMore:
            case R.id.iv_three:
                //todo 素质教育更多
                jumpHomeMore(2);
                break;
        }
    }

    private void jumpHomeMore(int type) {
        Intent intent = new Intent(mContext, HomeMoreActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("mSubject", mSubject);
        intent.putExtra("grade", grade);
        startActivity(intent);
    }

    //扫一扫
    public void StartZxing() {
        /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
         * 也可以不传这个参数
         * 不传的话  默认都为默认不震动  其他都为true
         * */
//        ZxingConfig config = new ZxingConfig();
//        config.setShowbottomLayout(false);//底部布局（包括闪光灯和相册）
//        config.setPlayBeep(true);//是否播放提示音
//        config.setShake(false);//是否震动
//        config.setShowAlbum(false);//是否显示相册
//        config.setShowFlashLight(false);//是否显示闪光灯

        //如果不传 ZxingConfig的话，两行代码就能搞定了
//        Intent intent = new Intent(mContext, `.class);
//        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
//        startActivityForResult(intent, REQUEST_CODE_SCAN);

        //这里的上下文是activity  所以回调的应该在 fragment所在的activity的onActivityResult
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setCaptureActivity(ScanActivity.class); //设置打开摄像头的Activity
        integrator.setPrompt("将二维码放入框内将自动扫描"); //底部的提示文字，设为""可以置空
        integrator.setCameraId(0); //前置或者后置摄像头
        integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            showScan(data);
        }
    }

    public void showScan(Intent data) {
        if (data != null) {
            String content = data.getStringExtra(ScanActivity.CODED_CONTENT);
            String id = content.substring(content.lastIndexOf("rid=") + 4);
            boolean isOldUrl = false;
            isOldUrl = content.contains("resourceurl.jspx");
            try {
                if (content.contains(TYPE_BOOK_QRCODE)) { //图书跳转
                    getRealIdByrid("", TYPE_BOOK_QRCODE, id, isOldUrl);
                } else if (content.contains(TYPE_RESOURCE_QRCODE) || isOldUrl) { //资源跳转
                    if (id.equals("5137")) { //2月24日 二维码修正 写死的判断
                        Intent intent2 = new Intent(mContext, ChapterDetailsActivity.class);
                        intent2.putExtra("chapterId", "592750");
                        startActivity(intent2);
                        return;
                    }
                    String type = "";
                    if (content.lastIndexOf("type=") != -1 && content.lastIndexOf("&") != -1)
                        type = content.substring(content.lastIndexOf("type=") + 5, content.lastIndexOf("&"));
                    getRealIdByrid(type, TYPE_RESOURCE_QRCODE, id, isOldUrl);
                } else if (content.contains(TYPE_CONTENT_QRCODE)) { //章节跳转
                    getRealIdByrid("", TYPE_CONTENT_QRCODE, id, isOldUrl);
                } else if (content.contains(TYPE_CURRICULUM_QRCODE)) { //课程跳转
                    String[] ids = {"56", "57", "58", "59", "60", "61"};
                    if (Arrays.asList(ids).contains(id)) {//讲座
                        Intent intent3 = new Intent(mContext, WholeBookChapterActivity.class);
                        intent3.putExtra("id", isWholeId(id));
                        startActivity(intent3);
                    } else {
                        getRealIdByrid("", TYPE_CURRICULUM_QRCODE, id, isOldUrl);
                    }
                } else if (content.contains(TYPE_SECURITY_QRCODE)) { //防伪码绑定
//                        ToastUtils.showToastLONG(mContext, "请在相关资源页面进行防伪码绑定");
                    String code = content.substring(content.lastIndexOf("code=") + 5);
                    toCheckSecurityCode(code);
                } else if (content.contains(TYPE_LECTURE_QRCODE)) {  //讲座
                    String[] ids = {"1", "2", "3", "4", "5", "6"};
                    if (Arrays.asList(ids).contains(id)) {//精品课程
                        Intent intent3 = new Intent(mContext, KengChengDetailActivity.class);
                        switch (id) {
                            case "1":
                                intent3.putExtra("curriculumId", "177");
                                break;
                            case "2":
                                intent3.putExtra("curriculumId", "178");
                                break;
                            case "3":
                                intent3.putExtra("curriculumId", "179");
                                break;
                            case "4":
                                intent3.putExtra("curriculumId", "180");
                                break;
                            case "5":
                                intent3.putExtra("curriculumId", "181");
                                break;
                            case "6":
                                intent3.putExtra("curriculumId", "182");
                                break;
                        }
                        startActivity(intent3);
                    } else {
                        getRealIdByrid("", TYPE_LECTURE_QRCODE, id, isOldUrl);
                    }
                } else if (content.contains(TYPE_LECTURE_CATEGORY_QRCODE)) {  //讲座目录
                    getRealIdByrid("", TYPE_LECTURE_CATEGORY_QRCODE, id, isOldUrl);
                } else if (content.contains(TYPE_LECTURE_RESOURCE_QRCODE)) {  //讲座资源
                    getRealIdByrid("", TYPE_LECTURE_RESOURCE_QRCODE, id, isOldUrl);
                } else if (content.contains("fw=")) { //新防伪码绑定
                    String code = content.substring(content.lastIndexOf("fw=") + 3);
                    checkNewSecurityCode(code);
                } else {   //二维码错误
                    ToastUtils.showToastLONG(mContext, "无效的二维码");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //ToastUtils.showToastSHORT(mContext, content);
        }
    }

    //转换二维码讲座id
    private String isWholeId(String id) {
        if (!TextUtils.isEmpty(id)) {
            if (id.equals("56")) return "1";
            if (id.equals("57")) return "2";
            if (id.equals("58")) return "3";
            if (id.equals("59")) return "4";
            if (id.equals("60")) return "5";
            if (id.equals("61")) return "6";
        }
        return id;
    }

    /**
     * 验证防伪码
     *
     * @param code 根据扫出来的防伪码验证是否为正确的防伪码
     */
    private void toCheckSecurityCode(String code) {
        if (!showNetWaitLoding()) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
            json.put("type", 0);
            json.put("isInput", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).checkSecurityCode(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        SecurityCodeBean securityCodeBean = ParseUtil.parseBean(response, SecurityCodeBean.class);
                        if (securityCodeBean.getStatus() == 1) {
                            ToastUtils.showToastLONG(mContext, securityCodeBean.getData().getMsg());
                        } else {
                            ToastUtils.showToastSHORT(mContext, "防伪码验证失败");
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
     * 新验证防伪码
     *
     * @param code 根据扫出来的防伪码验证是否为正确的防伪码
     */
    private void checkNewSecurityCode(String code) {
        if (!showNetWaitLoding()) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
            json.put("type", 0);
            json.put("isInput", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).checkNewSecurityCode(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        SecurityCodeBean securityCodeBean = ParseUtil.parseBean(response, SecurityCodeBean.class);
                        if (securityCodeBean.getStatus() == 1) {
                            ToastUtils.showToastLONG(mContext, securityCodeBean.getData().getMsg());
                        } else {
                            ToastUtils.showToastSHORT(mContext, "防伪码验证失败");
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

//网路获取数据 --------------------

    public void getAllData(String grade, String subject) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("grade", grade);
            json.put("subject", subject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).classIndex(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        refresh.setRefreshing(false);
                        btnRefreshNet.setVisibility(View.GONE);
                        HomeNewBean bean = ParseUtil.parseBean(response, HomeNewBean.class);
                        setResponseView(bean);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        refresh.setRefreshing(false);
                        btnRefreshNet.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }


    private void getRealIdByrid(String upResouceType, final String resouceType, final String rid, boolean isOldUrl) throws Exception {
        int type = 0;
        switch (resouceType) {
            case TYPE_BOOK_QRCODE:
                type = 0;
                break;
            case TYPE_CONTENT_QRCODE: //目录
                type = 1;
                break;
            case TYPE_RESOURCE_QRCODE:
                type = 2;
                break;
            case TYPE_CURRICULUM_QRCODE:
                type = 3;
                break;
            case TYPE_LECTURE_QRCODE:
                type = 5;
                break;
            case TYPE_LECTURE_CATEGORY_QRCODE:
                type = 6;
                break;
            case TYPE_LECTURE_RESOURCE_QRCODE:
                type = 7;
                break;
        }
        final int realId = Integer.parseInt(rid);

        if (abnormalQrcodeKeyMin <= realId && realId <= abnormalQrcodeKeyMax){
            // 615210~615225为有问题的16个章节
            Intent intent1 = new Intent(mContext, ChapterDetailsActivity.class);
            intent1.putExtra("chapterId", String.valueOf(abnormalQrcodeMap.get(realId)));
            intent1.putExtra("chapterName", "扫码听口算");
            startActivity(intent1);

            return;
        }

        if (abnormalQrcodeKeyMin2 <= realId && realId <= abnormalQrcodeKeyMax2 && upResouceType != null && upResouceType.equals(TYPE_BOOK_VIDEO)){
            // 后加的问题章节
            Intent intent1 = new Intent(mContext, ChapterDetailsActivity.class);
            intent1.putExtra("chapterId", String.valueOf(abnormalQrcodeMap.get(realId)));
            intent1.putExtra("chapterName", abnormalTitleMap.get(realId));
            startActivity(intent1);

            return;
        }



        boolean isOldUrls = isOldUrl;

        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("rid", realId);
        json.put("isOldUrl", isOldUrls);

        if (type == 2)
            json.put("resourceType", upResouceType);
        else
            json.put("resourceType", "");

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext)
                .getRealId(body)).handleResponse(new RetroFactory.ResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                Logger.i(TAG, "get realId  is successful     " + response.toString());
                ResourceIdBean resourceIdBean = new ResourceIdBean();
                resourceIdBean = ParseUtil.parseBean(response, ResourceIdBean.class);
                if (resourceIdBean != null && TextUtils.isEmpty(resourceIdBean.getMessage()) && resourceIdBean.getStatus() == 1) {
                    switch (resouceType) {
                        case TYPE_BOOK_QRCODE:  //图书
                            Intent intent = new Intent(mContext, BookDetailActivity.class);
                            intent.putExtra("bookId", String.valueOf(resourceIdBean.getData().getId()));
                            startActivity(intent);
                            break;
                        case TYPE_RESOURCE_QRCODE: //资源
                            Intent intent1 = new Intent(mContext, SeeResourcesVideoActivity.class);
                            intent1.putExtra("resourceId", String.valueOf(resourceIdBean.getData().getId()));
                            startActivity(intent1);
                            break;
                        case TYPE_CONTENT_QRCODE: //章节目录
                            Intent intent2 = new Intent(mContext, ChapterDetailsActivity.class);
                            intent2.putExtra("chapterId", String.valueOf(resourceIdBean.getData().getId()));
                            startActivity(intent2);
                            break;
                        case TYPE_CURRICULUM_QRCODE:  //精品课程
                            Intent intent3 = new Intent(mContext, KengChengDetailActivity.class);
                            intent3.putExtra("curriculumId", String.valueOf(resourceIdBean.getData().getId()));
                            startActivity(intent3);
                            break;
                        case TYPE_LECTURE_QRCODE: //讲座
                            Intent intent4 = new Intent(mContext, WholeBookChapterActivity.class);
                            intent4.putExtra("id", String.valueOf(resourceIdBean.getData().getId()));
                            startActivity(intent4);
                            break;
                        case TYPE_LECTURE_CATEGORY_QRCODE: //讲座目录
                            Intent intent5 = new Intent(mContext, WholeBookCourseActivity.class);
                            intent5.putExtra("id", String.valueOf(resourceIdBean.getData().getId()));
                            intent5.putExtra("type", 1);
                            startActivity(intent5);
                            break;
                        case TYPE_LECTURE_RESOURCE_QRCODE: //讲座资源
                            Intent intent6 = new Intent(mContext, WholeCourseDetailActivity.class);
                            intent6.putExtra("id", String.valueOf(resourceIdBean.getData().getId()));
                            startActivity(intent6);
                            break;
                    }
                } else {
                    if (resourceIdBean != null && resourceIdBean.getMessage() != null) {
                        ToastUtils.showToastLONG(mContext, resourceIdBean.getMessage());
                    }
                }
            }

            @Override
            public void onFail(int type, String msg) {
                Logger.e(TAG, "get realId  is fail    " + msg.toString());
                ToastUtils.showToastSHORT(mContext, msg);
            }
        });

    }

    public void getUserInfo() {
//        if (!showNetWaitLoding()) {
//            return;
//        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getUserInfo(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        PersonDetailBean personDetailBean = ParseUtil.parseBean(response, PersonDetailBean.class);
                        if (personDetailBean != null || personDetailBean.getData() != null) {
                            AccountUtil.saveUserInfo(mContext, personDetailBean);
                            JpushUtil.setJpushMsgTipAudio(mContext);
                            JPushInterface.resumePush(mContext);
                            setGrade();
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }


}
