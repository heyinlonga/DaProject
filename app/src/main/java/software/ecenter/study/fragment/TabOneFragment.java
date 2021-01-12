package software.ecenter.study.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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


import com.google.zxing.integration.android.IntentIntegrator;
import com.youth.banner.Banner;


import org.json.JSONException;
import org.json.JSONObject;

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
import software.ecenter.study.Adapter.BookAdapter;
import software.ecenter.study.Adapter.KechengAdapter;
import software.ecenter.study.Adapter.PackageAdapter;
import software.ecenter.study.Interface.ConstantData;
import software.ecenter.study.R;
import software.ecenter.study.View.SpinnerPopWindow;
import software.ecenter.study.activity.BannerDetailActivity;
import software.ecenter.study.activity.BookDetailActivity;
import software.ecenter.study.activity.ChapterDetailsActivity;
import software.ecenter.study.activity.KengChengDetailActivity;
import software.ecenter.study.activity.MyBagActivity;
import software.ecenter.study.activity.ScanActivity;
import software.ecenter.study.activity.SearchDActivity;
import software.ecenter.study.activity.SeeResourcesVideoActivity;
import software.ecenter.study.activity.TaoXiDetailActivity;
import software.ecenter.study.bean.HomeBean.BannerBean;
import software.ecenter.study.bean.HomeBean.BookBean;
import software.ecenter.study.bean.HomeBean.CurriculumBean;
import software.ecenter.study.bean.HomeBean.HomeBean;
import software.ecenter.study.bean.HomeBean.PackageBean;
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
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

/**
 * dec 课堂首页
 * Created by Mike on 2018/4/25.
 */

public class TabOneFragment extends BaseFragment implements ConstantData {
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

    private SpinnerPopWindow spinnerPopWindow;


    private BookAdapter adapterOne;
    private KechengAdapter adapterTwo;
    private PackageAdapter adapterThree;

    private List<BannerBean> listBanner;
    private List<BookBean> ListDataOne;
    private List<CurriculumBean> ListDataTwo;
    private List<PackageBean> ListDataThree;
    private HomeBean mHomeBean;

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
    private boolean isLoad;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_one, null);
        unbinder = ButterKnife.bind(this, mRootView);

        LinearLayoutManager linearLayoutManagerOne = new LinearLayoutManager(mContext);
        linearLayoutManagerOne.setOrientation(LinearLayoutManager.HORIZONTAL);
        listOne.setLayoutManager(linearLayoutManagerOne);

        LinearLayoutManager linearLayoutManagerTwo = new LinearLayoutManager(mContext);
        linearLayoutManagerTwo.setOrientation(LinearLayoutManager.HORIZONTAL);
        listTwo.setLayoutManager(linearLayoutManagerTwo);

        LinearLayoutManager linearLayoutManagerThree = new LinearLayoutManager(mContext);
        linearLayoutManagerThree.setOrientation(LinearLayoutManager.HORIZONTAL);
        listThree.setLayoutManager(linearLayoutManagerThree);
        getUserInfo();
        setGrade();

        btnShuxue.setBackground(getResources().getDrawable(R.drawable.shuxue2));
        mSubject = "数学";
        getAllData(gradeText.getText().toString(), mSubject);
        boolean isOpenFirstOpenHome = AccountUtil.getFirstOpenHome(mContext);
       /* if (isOpenFirstOpenHome) {
            // TODO: 2018/7/7 显示扫码引导页
            homeRlYinDao.setVisibility(View.VISIBLE);
            homeIvYinDao.setImageDrawable(getResources().getDrawable(R.drawable.yindao1));
            homeRlYinDao.setBackgroundColor(getResources().getColor(R.color.black));
            homeRlYinDao.getBackground().setAlpha(120);
        } else if (AccountUtil.getFirstOpenBookPackage(mContext)) {
            homeRlYinDao.setVisibility(View.VISIBLE);
            homeIvYinDao2.setImageDrawable(getResources().getDrawable(R.drawable.yindao2));
            homeRlYinDao.setBackgroundColor(getResources().getColor(R.color.black));
            homeRlYinDao.getBackground().setAlpha(120);

        }*/

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

    public void setResponseView(HomeBean homeBean) {
        mHomeBean = homeBean;

        //假数据广告 https://www.jianshu.com/p/d229a647e705
        listBanner = mHomeBean.getData().getBannerList();
        String[] images = new String[listBanner.size()];
        for (int i = 0; i < images.length; i++) {
            images[i] = listBanner.get(i).getBannerImage();
        }

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
                Intent intent = new Intent(mContext, BannerDetailActivity.class);
                intent.putExtra("id", listBanner.get(position - 1).getBannerId());
                startActivity(intent);
            }
        });

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

        //图书 列表
        ListDataOne = mHomeBean.getData().getBookList();
        if (ListDataOne.size() == 0) {
            bookw.setVisibility(View.VISIBLE);
        } else {
            bookw.setVisibility(View.GONE);
        }
        adapterOne = new BookAdapter(mContext, ListDataOne);
        adapterOne.setItemClickListener(new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(mContext, BookDetailActivity.class);
                intent.putExtra("bookId", ListDataOne.get(position).getBookId());
                startActivity(intent);
//                LucklyPopopWindow mLucklyPopopWindow = new LucklyPopopWindow(mContext);
//                mLucklyPopopWindow.setViewMargin(false, 1, 0, 0, 0);
//                mLucklyPopopWindow.setViewPadding(0, 1, 1, 1);
//                DataBeans add=new DataBeans();
//                add.setData("增加");
//                List<DataBeans> list=new ArrayList<>();
//                list.add(add);
//                //必须设置宽度
//                mLucklyPopopWindow.setWidth(ScreenUtils.dp2px(mContext,200));
//                //添加分割线(可选)
//                mLucklyPopopWindow.addItemDecoration(LucklyPopopWindow.VERTICAL, Color.GRAY, 1);
//                mLucklyPopopWindow.showAtLocation(getActivity().getWindow().getDecorView(), v);

            }
        });
        listOne.setAdapter(adapterOne);
        listOne.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("infoo", "newState:" + newState + "---" + recyclerView.computeHorizontalScrollExtent() + "----" + recyclerView.computeHorizontalScrollOffset() + "--" + recyclerView.computeHorizontalScrollRange());
                int se = recyclerView.computeHorizontalScrollExtent();
                int so = recyclerView.computeHorizontalScrollOffset();
                int sr = recyclerView.computeHorizontalScrollRange();
                if (newState == 0 && se + so == sr) {
                    if (mHomeBean.getData().isHasBookNextPage()&&!isLoad) {
                        isLoad = true;
                        getMoreBook(gradeText.getText().toString(), mSubject, "" + (mHomeBean.getData().getBookCurPage() + 1));
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        // 课程 列表
        ListDataTwo = mHomeBean.getData().getCurriculumList();
        if (ListDataTwo.size() == 0) {
            booktwo.setVisibility(View.VISIBLE);
        } else {
            booktwo.setVisibility(View.GONE);
        }
        adapterTwo = new KechengAdapter(mContext, ListDataTwo);
        adapterTwo.setItemClickListener(new KechengAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent intent = new Intent(mContext, KengChengDetailActivity.class);
                intent.putExtra("curriculumId", ListDataTwo.get(position).getCurriculumId());
                startActivity(intent);
            }
        });
        listTwo.setAdapter(adapterTwo);
        listTwo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int se = recyclerView.computeHorizontalScrollExtent();
                int so = recyclerView.computeHorizontalScrollOffset();
                int sr = recyclerView.computeHorizontalScrollRange();
                if (newState == 0 && se + so == sr) {
                    //加载更多
                    if (mHomeBean.getData().isHasCurriculumNextPage()&&!isLoad) {
                        isLoad = true;
                        getMoreCurriculum(gradeText.getText().toString(), mSubject, "" + (mHomeBean.getData().getCurriculumCurPage() + 1));
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        //套系 列表
        ListDataThree = mHomeBean.getData().getPackageList();
        if (ListDataThree.size() == 0) {
            bookthree.setVisibility(View.VISIBLE);
        } else {
            bookthree.setVisibility(View.GONE);
        }
        adapterThree = new PackageAdapter(mContext, ListDataThree);
        adapterThree.setItemClickListener(new PackageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent intent = new Intent(mContext, TaoXiDetailActivity.class);
                intent.putExtra("packageId", ListDataThree.get(position).getPackageId());
                startActivity(intent);
            }
        });
        listThree.setAdapter(adapterThree);

        listThree.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int se = recyclerView.computeHorizontalScrollExtent();
                int so = recyclerView.computeHorizontalScrollOffset();
                int sr = recyclerView.computeHorizontalScrollRange();
                if (newState == 0 && se + so == sr) {
                    //加载更多
                    if (mHomeBean.getData().isHasPackageNextPage()) {
                        getMorePackage(gradeText.getText().toString(), mSubject, "" + (mHomeBean.getData().getPackageCurPage() + 1));
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            if (gradeText != null)
//                getAllData(gradeText.getText().toString(), mSubject);
//            String curGrade = AccountUtil.getRealGrade(mContext);
//            if (TextUtils.isEmpty(userGrade) || !userGrade.contains(curGrade)) {
//                setGrade();
//                getAllData(gradeText.getText().toString().trim(), mSubject);
//            }
//        }
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        String curGrade = AccountUtil.getRealGrade(mContext);
//
//        if (TextUtils.isEmpty(userGrade) || !userGrade.contains(curGrade)) {
//            setGrade();
//            getAllData(gradeText.getText().toString().trim(), mSubject);
//            return;
//        }
//        getAllData(gradeText.getText().toString(), mSubject);
//    }

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
            R.id.btn_grade, R.id.btn_refresh_net,
            R.id.home_rl_yin_dao})
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
                startActivity(new Intent(mContext, MyBagActivity.class)); //进入我的书包界面
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
        }
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
//
//        //如果不传 ZxingConfig的话，两行代码就能搞定了
//        Intent intent = new Intent(mContext, CaptureActivity.class);
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
            if (data != null) {

                String content = data.getStringExtra(ScanActivity.CODED_CONTENT);
                String id = content.substring(content.lastIndexOf("rid=") + 4);
                boolean isOldUrl = false;
                isOldUrl = content.contains("resourceurl.jspx");
                try {
                    if (content.contains(TYPE_BOOK_QRCODE)) { //图书跳转
                        getRealIdByrid("", TYPE_BOOK_QRCODE, id, isOldUrl);
                    } else if (content.contains(TYPE_RESOURCE_QRCODE) || isOldUrl) { //资源跳转
                        String type = "";
                        if (content.lastIndexOf("type=") != -1 && content.lastIndexOf("&") != -1)
                            type = content.substring(content.lastIndexOf("type=") + 5, content.lastIndexOf("&"));
                        getRealIdByrid(type, TYPE_RESOURCE_QRCODE, id, isOldUrl);
                    } else if (content.contains(TYPE_CONTENT_QRCODE)) { //章节跳转
                        getRealIdByrid("", TYPE_CONTENT_QRCODE, id, isOldUrl);
                    } else if (content.contains(TYPE_CURRICULUM_QRCODE)) { //课程跳转
                        getRealIdByrid("", TYPE_CURRICULUM_QRCODE, id, isOldUrl);
                    } else if (content.contains(TYPE_SECURITY_QRCODE)) { //防伪码绑定
//                        ToastUtils.showToastLONG(mContext, "请在相关资源页面进行防伪码绑定");
                        String code = content.substring(content.lastIndexOf("code=") + 5);
                        toCheckSecurityCode(code);
                    } else  if (content.contains("fw=")) { //新防伪码绑定
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
            json.put("pageNum", pageNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).home(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.GONE);
                        HomeBean bean = ParseUtil.parseBean(response, HomeBean.class);
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

    public void setMoreBookResponseView(HomeBean homeBean) {
        mHomeBean.getData().setBookCurPage(homeBean.getData().getCurpage());
        mHomeBean.getData().setHasBookNextPage(homeBean.getData().isHasBookNextPage());
        //图书 列表
        ListDataOne.addAll(homeBean.getData().getBookList());
        adapterOne.refreshData(ListDataOne);
    }

    public void setMoreCurriculumResponseView(HomeBean homeBean) {
        mHomeBean.getData().setCurriculumCurPage(homeBean.getData().getCurpage());
        mHomeBean.getData().setHasCurriculumNextPage(homeBean.getData().isHasCurriculumNextPage());
        //课程 列表
        ListDataTwo.addAll(homeBean.getData().getCurriculumList());
        adapterTwo.refreshData(ListDataTwo);

    }

    public void setMorePackageResponseView(HomeBean homeBean) {
        mHomeBean.getData().setPackageCurPage(homeBean.getData().getCurpage());
        mHomeBean.getData().setHasPackageNextPage(homeBean.getData().isHasPackageNextPage());
        //套系 列表
        ListDataThree.addAll(homeBean.getData().getPackageList());
        adapterThree.refreshData(ListDataThree);

    }
    //更多图书
    public void getMoreBook(String grade, String subject, String curpage) {
        if (!showNetWaitLoding()) {
            isLoad = false;
            return;
        }

        JSONObject json = new JSONObject();
        try {

            json.put("grade", grade);
            json.put("subject", subject);
            json.put("pageNum", pageNum);
            json.put("curpage", curpage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).homeBook(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        // HomeBean临时变量，只含有book数据
                        HomeBean bean = ParseUtil.parseBean(response, HomeBean.class);
                        setMoreBookResponseView(bean);
                        isLoad = false;
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                        isLoad = false;
                    }

                });
    }
    //跟多课程
    public void getMoreCurriculum(String grade, String subject, String curpage) {
        if (!showNetWaitLoding()) {
            isLoad = false;
            return;
        }

        JSONObject json = new JSONObject();
        try {

            json.put("grade", grade);
            json.put("subject", subject);
            json.put("pageNum", pageNum);
            json.put("curpage", curpage);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).homeCurriculum(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        HomeBean bean = ParseUtil.parseBean(response, HomeBean.class);
                        setMoreCurriculumResponseView(bean);
                        isLoad = false;
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                        isLoad = false;
                    }

                });
    }

    public void getMorePackage(String grade, String subject, String curpage) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {

            json.put("grade", grade);
            json.put("subject", subject);
            json.put("pageNum", pageNum);
            json.put("curpage", curpage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).homepackage(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        HomeBean bean = ParseUtil.parseBean(response, HomeBean.class);
                        setMorePackageResponseView(bean);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    private void getRealIdByrid(String upResouceType, final String resouceType, String rid, boolean isOldUrl) throws Exception {
        int type = 0;
        switch (resouceType) {
            case TYPE_BOOK_QRCODE:
                type = 0;
                break;
            case TYPE_CONTENT_QRCODE:
                type = 1;
                break;
            case TYPE_RESOURCE_QRCODE:
                type = 2;
                break;
            case TYPE_CURRICULUM_QRCODE:
                type = 3;
                break;
        }
        final int realId = Integer.parseInt(rid);
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
