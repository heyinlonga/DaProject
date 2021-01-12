package software.ecenter.study.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;

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
import software.ecenter.study.Adapter.AutoTaoXiAdapter;
import software.ecenter.study.Adapter.ChapterListAdapter;
import software.ecenter.study.Adapter.TipKechengAdapter;
import software.ecenter.study.Adapter.ZiyuanAdapter;
import software.ecenter.study.Interface.OnItemListener;
import software.ecenter.study.R;
import software.ecenter.study.View.AutoPollRecyclerView;
import software.ecenter.study.View.ExpandableTextView;
import software.ecenter.study.View.MyDialog;
import software.ecenter.study.View.treelist.Node;
import software.ecenter.study.bean.BindBookBean;
import software.ecenter.study.bean.HomeBean.BookDetailBean;
import software.ecenter.study.bean.HomeBean.ChapterItemBean;
import software.ecenter.study.bean.HomeBean.CurriculumBean;
import software.ecenter.study.bean.HomeBean.PackageBean;
import software.ecenter.study.bean.HomeBean.ResourceBean;
import software.ecenter.study.bean.HomeBean.SecurityCodeBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.NetworkUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.PermissionUtils;
import software.ecenter.study.utils.ScreenUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class BookDetailActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.img_book)
    ImageView imgBook;
    @BindView(R.id.text_book_name)
    TextView textBookName;
    @BindView(R.id.text_book_jiage)
    TextView textBookJiage;
    @BindView(R.id.ll_youhui)
    LinearLayout ll_youhui;
    @BindView(R.id.tv_youProce)
    TextView tv_youProce;
    @BindView(R.id.tv_youProce_text)
    TextView tv_youProce_text;
    @BindView(R.id.ll_zbProce)
    LinearLayout ll_zbProce;
    @BindView(R.id.tv_zbProce)
    TextView tv_zbProce;
    @BindView(R.id.ll_zbyouProce)
    LinearLayout ll_zbyouProce;
    @BindView(R.id.tv_zbyouProce)
    TextView tv_zbyouProce;
    @BindView(R.id.tv_youhui)
    TextView tv_youhui;
    @BindView(R.id.tv_dyj)
    TextView tv_dyj;
    @BindView(R.id.tv_jifen)
    TextView tv_jifen;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.text_book_youhuijia)
    TextView textBookYouHuiJia;
    @BindView(R.id.btn_buy)
    TextView btnBuy;
    @BindView(R.id.btn_buyIn)
    TextView btnBuyIn;
    @BindView(R.id.tip_temp)
    RelativeLayout tipTemp;
    @BindView(R.id.id_source_textview)
    TextView idSourceTextview;
    @BindView(R.id.id_expand_textview)
    TextView idExpandTextview;
    @BindView(R.id.expandable_text)
    ExpandableTextView expandableText;
    @BindView(R.id.list_three)
    AutoPollRecyclerView listThree;
    @BindView(R.id.list_chapter)
    RecyclerView listChapter;
    @BindView(R.id.list_kecheng)
    RecyclerView listKecheng;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;

    List<ChapterItemBean> ChapterItemBeanList;
    ChapterListAdapter mChapterListAdapter;
    ZiyuanAdapter ziyuanAdapter;

    List<PackageBean> packageData;
    List<CurriculumBean> CurriculumBeanData;
    List<ResourceBean> resourceBeanData;

    private String bookId;
    //扫一扫
    private int REQUEST_CODE_SCAN = 112;

    private BookDetailBean mBookDetailBean;
    private boolean isShowCategory = true;
    private String youhuijia;
    private String bookname;
    private boolean isInput;
    private boolean discount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);

        listThree.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        listChapter.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        listKecheng.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        bookId = getIntent().getStringExtra("bookId");
        if (TextUtils.isEmpty(bookId)) {
            Bundle bundle = getIntent().getBundleExtra("package");
            if (bundle != null)
                bookId = (String) bundle.get("bookId");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Util.isOnMainThread()) {
            Glide.get(this).clearMemory();
        }
    }

    /**
     * 书的详情
     */
    public void getData(boolean isShow) {
        if (isShow) {
            if (!showNetWaitLoding()) {
                return;
            }
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("bookId", bookId);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).homeBookDetail(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.GONE);
                        final BookDetailBean bean = ParseUtil.parseBean(response, BookDetailBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setResponseView(bean);
                            }
                        });
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    //设置数据
    public void setResponseView(BookDetailBean bean) {
        if (bean == null) return;
        mBookDetailBean = bean;
        if (Util.isOnMainThread()) {
            if (!this.isFinishing())
                Glide.with(mContext).load(bean.getData().getBookImage()).error(R.drawable.morenshu).into(imgBook);
        }
        bookname = bean.getData().getBookName();
        textBookName.setText(bean.getData().getBookName());


        String yuanjia;
        if (bean.getData().isHasSecurityCode())
            yuanjia = "原价：" + bean.getData().getBookPrice() + " " + "元宝";
        else
            yuanjia = "价格：" + bean.getData().getBookPrice() + " " + "元宝";


        if (bean.getData().isFreeBook()) {
            yuanjia = "价格：免费";
            textBookYouHuiJia.setVisibility(View.GONE);
            btnBuyIn.setVisibility(View.VISIBLE);
            btnBuy.setVisibility(View.INVISIBLE);
        } else {
            if (bean.getData().getBookSalePrice().equals("0")) {
                youhuijia = "正版：" + "免费";
            } else {
                youhuijia = "正版：" + bean.getData().getBookSalePrice() + " " + "元宝";
            }
            SpannableStringBuilder builder = new SpannableStringBuilder(youhuijia);
            ForegroundColorSpan blueColor = new ForegroundColorSpan(getResources().getColor(R.color.textColor));
            builder.setSpan(blueColor, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textBookYouHuiJia.setText(builder);
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(yuanjia);
        ForegroundColorSpan blueColor = new ForegroundColorSpan(getResources().getColor(R.color.textColor));
        builder.setSpan(blueColor, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textBookJiage.setText(builder);

        if (!bean.getData().isHasSecurityCode()) {
            textBookYouHuiJia.setVisibility(View.GONE);
        }

        titleContent.setText(bean.getData().getBookName());
        expandableText = findViewById(R.id.expandable_text);
        expandableText.setText(bean.getData().getBookIntroduction());


        //假数据 推荐套系
        packageData = bean.getData().getPackageList();

        if (packageData != null) {
            AutoTaoXiAdapter adapterOne = new AutoTaoXiAdapter(mContext, packageData);
            adapterOne.setItemClickListener(new AutoTaoXiAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(mContext, TaoXiDetailActivity.class);
                    intent.putExtra("packageId", packageData.get(position).getPackageId());
                    startActivity(intent);
                }
            });
            listThree.setAdapter(adapterOne);
            if (packageData.size() > 1) {
                listThree.start();
            }
        }

        //章节
        isShowCategory = bean.getData().isShowCategory();
        //是否显示目录
        if (isShowCategory) setChapterList(bean);
        else setResourceList(bean);

        //推荐课程
        CurriculumBeanData = bean.getData().getCurriculumList();

        if (CurriculumBeanData != null) {
            TipKechengAdapter adapterTwo = new TipKechengAdapter(mContext, CurriculumBeanData);
            adapterTwo.setItemClickListener(new TipKechengAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(mContext, KengChengDetailActivity.class);
                    intent.putExtra("curriculumId", CurriculumBeanData.get(position).getCurriculumId());
                    startActivity(intent);
                }
            });
            listKecheng.setAdapter(adapterTwo);
        }
        setBtnYouHui(bean);
        //设置购买按钮显示内容以及是否可以点击
        setBtnBuyProPerty(bean);
        setBtnInProPerty(bean);
    }

    //设置优惠价
    private void setBtnYouHui(BookDetailBean bean) {
        BookDetailBean.Data data = bean.getData();
        if (data != null && !bean.getData().isBuy()) {
            discount = data.isDiscount();
            int coinDiscount = data.getCoinDiscount();
            int couponDiscount = data.getCouponDiscount();
            int bonusDiscount = data.getBonusDiscount();
            if (discount) {//有优惠
                ll_youhui.setVisibility(View.VISIBLE);
                textBookJiage.setVisibility(View.INVISIBLE);
                textBookYouHuiJia.setVisibility(View.INVISIBLE);
                tv_youProce_text.setText("原价：");
                tv_youProce.setText(data.getBookPrice() + "元宝");
                String bookSalePrice = bean.getData().getBookSalePrice();
                ll_zbProce.setVisibility(View.VISIBLE);
                tv_zbProce.setText("免费");
                //是正版
                if (!TextUtils.isEmpty(bookSalePrice) && !bookSalePrice.equals("0")) {
                    ll_zbProce.setVisibility(View.VISIBLE);
                    tv_zbProce.setText(bookSalePrice + "元宝");
                    if (coinDiscount > 0) {
                        ll_zbyouProce.setVisibility(View.VISIBLE);
                        tv_zbProce.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
                        String text = coinDiscount + "元宝";
                        SpannableStringBuilder builder = new SpannableStringBuilder(text);
                        ForegroundColorSpan blueColor = new ForegroundColorSpan(getResources().getColor(R.color.color_F1916E));
                        builder.setSpan(blueColor, 0, text.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builder.setSpan(new AbsoluteSizeSpan(16, true), 0, text.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tv_zbyouProce.setText(builder);
                    }
                } else if (coinDiscount > 0) {//优惠价
                    tv_youProce.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
                    tv_youhui.setVisibility(View.VISIBLE);
                    String text = "优惠价：" + coinDiscount + "元宝";
                    SpannableStringBuilder builder = new SpannableStringBuilder(text);
                    ForegroundColorSpan startColor = new ForegroundColorSpan(getResources().getColor(R.color.color_C9AB79));
                    builder.setSpan(startColor, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ForegroundColorSpan blueColor = new ForegroundColorSpan(getResources().getColor(R.color.color_F1916E));
                    builder.setSpan(blueColor, 4, text.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.setSpan(new AbsoluteSizeSpan(16, true), 4, text.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_youhui.setText(builder);
                }
                if (couponDiscount > 0) {//答疑卷
                    tv_dyj.setVisibility(View.VISIBLE);
                    String text = "正版赠送：" + couponDiscount + "张答疑劵";
                    SpannableStringBuilder builder = new SpannableStringBuilder(text);
                    ForegroundColorSpan startColor = new ForegroundColorSpan(getResources().getColor(R.color.color_C9AB79));
                    builder.setSpan(startColor, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ForegroundColorSpan blueColor = new ForegroundColorSpan(getResources().getColor(R.color.color_F1916E));
                    builder.setSpan(blueColor, 5, text.length() - 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.setSpan(new AbsoluteSizeSpan(16, true), 5, text.length() - 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_dyj.setText(builder);
                }
                if (bonusDiscount > 0) {//积分
                    tv_jifen.setVisibility(View.VISIBLE);
                    String text = "正版赠送：" + bonusDiscount + "积分";
                    SpannableStringBuilder builder = new SpannableStringBuilder(text);
                    ForegroundColorSpan startColor = new ForegroundColorSpan(getResources().getColor(R.color.color_C9AB79));
                    builder.setSpan(startColor, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ForegroundColorSpan blueColor = new ForegroundColorSpan(getResources().getColor(R.color.color_F1916E));
                    builder.setSpan(blueColor, 5, text.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.setSpan(new AbsoluteSizeSpan(16, true), 5, text.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_jifen.setText(builder);
                }
                new CountDownTimer(data.getEndTime() * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tv_time.setText("" + ToolUtil.getTime(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        tv_time.setText("已结束");
                    }
                }.start();
            }
        }
    }

    /**
     * 设置购买按钮的属性
     */
    private void setBtnBuyProPerty(BookDetailBean bean) {
        if (bean.getData().getBookSalePrice().equals("0")) {
            btnBuy.setText("获取");
        } else {
            btnBuy.setText("购买");
        }
        if (bean.getData().isHasSecurityCode()) {
            if (bean.getData().isBuy() || (bean.getData().getBookSalePrice().equals("0") && bean.getData().isBind())) {
                if (bean.getData().getBookSalePrice().equals("0")) {
                    btnBuy.setText("已获取");
                } else {
                    btnBuy.setText("已购买");
                }
                ll_youhui.setVisibility(View.GONE);
                btnBuy.setEnabled(false);
                btnBuy.setBackground(getResources().getDrawable(R.drawable.btn_sign_shape_circle_unclick));
            }
        } else if (bean.getData().isBuy()) {
            if (bean.getData().getBookSalePrice().equals("0")) {
                btnBuy.setText("已获取");
            } else {
                btnBuy.setText("已购买");
            }
            ll_youhui.setVisibility(View.GONE);
            btnBuy.setEnabled(false);
            btnBuy.setBackground(getResources().getDrawable(R.drawable.btn_sign_shape_circle_unclick));
        }
    }

    /**
     * 设置加入按钮的属性
     */
    private void setBtnInProPerty(BookDetailBean bean) {
        if (bean.getData().isBuy()) {
            btnBuyIn.setText("已加入书包");
            btnBuyIn.setEnabled(false);
            btnBuyIn.setBackground(getResources().getDrawable(R.drawable.btn_sign_shape_circle_unclick));

            //加入过书包 不需要价格
            textBookYouHuiJia.setVisibility(View.INVISIBLE);
            textBookJiage.setVisibility(View.INVISIBLE);
            ll_youhui.setVisibility(View.GONE);

        } else {
            btnBuyIn.setText("加入书包");
            btnBuyIn.setEnabled(true);
            btnBuyIn.setBackground(getResources().getDrawable(R.drawable.btn_sign_shape_circle_unclick));
        }
    }

    /**
     * 设置资源
     */
    private void setResourceList(BookDetailBean bean) {
        resourceBeanData = bean.getData().getResourceBeanList();
        if (resourceBeanData == null)
            return;
        ziyuanAdapter = new ZiyuanAdapter(mContext, resourceBeanData);
        listChapter.setAdapter(ziyuanAdapter);
        ziyuanAdapter.setItemClickListener(new ZiyuanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                if (resourceBeanData.get(position).isHaveResourceFile()) {
                    Intent intent = new Intent(mContext, SeeResourcesVideoActivity.class);
                    intent.putExtra("resourceId", resourceBeanData.get(position).getResourceId());
                    intent.putExtra("data", mBookDetailBean);
                    startActivity(intent);
                } else
                    ToastUtils.showToastLONG(mContext, "资源即将上传");

            }
        });
    }

    /**
     * 设置目录
     *
     * @param bean 图书实体类
     */
    private void setChapterList(BookDetailBean bean) {
        ChapterItemBeanList = bean.getData().getChapterList();
        //第一个参数  RecyclerView
        //第二个参数  上下文
        //第三个参数  数据集
        //第四个参数  默认展开层级数 0为不展开
        //第五个参数  展开的图标
        //第六个参数  闭合的图标
        if (ChapterItemBeanList != null) {
            try {
                mChapterListAdapter = new ChapterListAdapter(listChapter, this,
                        ChapterItemBeanList, 0);

                mChapterListAdapter.setOnTreeNodeClickListener(new ChapterListAdapter.OnTreeNodeClickListener() {
                    @Override
                    public void onClick(Node node, int position, boolean isJump) {
                        ChapterItemBean chapterItemBean = null;
                        for (ChapterItemBean chapterItemBean1 : ChapterItemBeanList) {
                            if (chapterItemBean1.getChapterId().equals(node.getId() + "")) {
                                chapterItemBean = chapterItemBean1;
                            }
                        }
                        if (chapterItemBean == null) {
                            ToastUtils.showToastSHORT(mContext, "该目录下暂无资源！");
                            return;
                        }
                        if (chapterItemBean.getIsExpand() == 1) {
                            mChapterListAdapter.expandOrCollapse(position);
                            return;
                        }

                        if (chapterItemBean.getIsExpand() == 0) {
                            if (chapterItemBean.getResourceCount() > 0) {
                                Intent intent = new Intent(mContext, ChapterDetailsActivity.class);
                                intent.putExtra("chapterId", "" + node.getId());
                                intent.putExtra("chapterName", "" + node.getName());
                                startActivity(intent);
                                return;
                            } else {
                                ToastUtils.showToastSHORT(mContext, "该目录下暂无资源！");
                                return;
                            }

                        }
                        if (node.isLeaf() || isJump) {
                     /*   Toast.makeText(getApplicationContext(), node.getName(),
                                Toast.LENGTH_SHORT).show();*/

                            if (chapterItemBean.getResourceCount() > 0) {
                                Intent intent = new Intent(mContext, ChapterDetailsActivity.class);
                                intent.putExtra("chapterId", "" + node.getId());
                                intent.putExtra("chapterName", "" + node.getName());
                                startActivity(intent);
                            } else if (chapterItemBean.getIsExpand() == 0) {
                                ToastUtils.showToastSHORT(mContext, "该目录下暂无资源！");
                            }
                        }

                    }

                });

                listChapter.setAdapter(mChapterListAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * 防伪码验证成功，绑定图书
     *
     * @param code
     * @param bookId
     */
    public void toBind(String code, final String bookId) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("bookId", bookId);
            json.put("code", code);
            json.put("isInput", isInput);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).bindSecurityCode(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
//                        ToastUtils.showToastSHORT(mContext, "绑定成功");
                        mBookDetailBean.getData().setHasSecurityCode(false);
                        BindBookBean bookBean = ParseUtil.parseBean(response, BindBookBean.class);
                        if (bookBean != null && bookBean.getData() != null) {
                            mBookDetailBean.getData().setIsBuy(bookBean.getData().isBuy());
                            mBookDetailBean.getData().setHasSecurityCode(bookBean.getData().isHasSecurityCode());
                        }
                        if (!mBookDetailBean.getData().isBuy()) {
                            Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                            intent.putExtra("Id", bookId);
                            intent.putExtra("discount", discount);
                            intent.putExtra("buyType", "1"); //1、图书 2、课程 3、套系 4、章节 5、资源
                            startActivity(intent);
                        }
                        setResponseView(mBookDetailBean);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(ScanActivity.CODED_CONTENT);
                if (content.contains("securityQrcode")) { //防伪码绑定
                    String code = content.substring(content.lastIndexOf("code=") + 5);
                    isInput = false;
                    checkSecurityCode(code);
                    //toBind(code, bookId);
                } else if (content.contains("fw=")) { //新防伪码绑定
                    String code = content.substring(content.lastIndexOf("fw=") + 3);
                    isInput = false;
                    checkNewSecurityCode(code);
                    //toBind(code, bookId);
                } else if (content.contains("bookQrcode") || content.contains("resourceQrcode") || content.contains("contentQrcode") || content.contains("curriculumQrcode")) { //图书跳转
//                    ToastUtils.showToastLONG(mContext, "非防伪码，请在我的课堂进行扫一扫");
                    ToastUtils.showToastLONG(mContext, "防伪码验证未通过！\n" +
                            "请重新扫描正确的防伪码");
                } else {   //二维码错误
                    ToastUtils.showToastLONG(mContext, "防伪码验证未通过！\n" +
                            "请重新扫描正确的防伪码");
                }

            }
        }
    }

    /**
     * 验证防伪码
     *
     * @param code 防伪码
     */
    private void checkSecurityCode(final String code) {

        if (!showNetWaitLoding()) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
            json.put("type", 1);
            json.put("isInput", isInput);
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
                            if (securityCodeBean.getData().isIsCorrect() && securityCodeBean.getData().isIsCanBind()) {
                                //验证防伪码成功，显示是否绑定
                                showtoBindDialog(code, bookId);
                            } else {
//                                Intent intent = new Intent(mContext, ResourceBuyActivity.class);
//                                intent.putExtra("Id", bookId);
//                                intent.putExtra("buyType", "1"); //1、图书 2、课程 3、套系 4、章节 5、资源
//                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    /**
     * 新验证防伪码
     *
     * @param code 防伪码
     */
    private void checkNewSecurityCode(final String code) {

        if (!showNetWaitLoding()) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
            json.put("type", 1);
            json.put("isInput", isInput);
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
                            if (securityCodeBean.getData().isIsCorrect() && securityCodeBean.getData().isIsCanBind()) {
                                //验证防伪码成功，显示是否绑定
                                showtoBindDialog(code, bookId);
                            } else {
//                                Intent intent = new Intent(mContext, ResourceBuyActivity.class);
//                                intent.putExtra("Id", bookId);
//                                intent.putExtra("buyType", "1"); //1、图书 2、课程 3、套系 4、章节 5、资源
//                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
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
//
//        //如果不传 ZxingConfig的话，两行代码就能搞定了
//        Intent intent = new Intent(mContext, CaptureActivity.class);
//        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
//        startActivityForResult(intent, REQUEST_CODE_SCAN);

        //这里的上下文是activity  所以回调的应该在 fragment所在的activity的onActivityResult
        IntentIntegrator integrator = new IntentIntegrator(this);
        // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setCaptureActivity(ScanActivity.class); //设置打开摄像头的Activity
        integrator.setPrompt("将二维码放入框内将自动扫描"); //底部的提示文字，设为""可以置空
        integrator.setCameraId(0); //前置或者后置摄像头
        integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    /**
     * 验证防伪码dialog
     */
    public void showBindDialog() {
        ToolUtil.showYanCodeDialog(this, bookname, new OnItemListener() {
            @Override
            public void onPay() {//直接购买
                Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                intent.putExtra("Id", bookId);
                intent.putExtra("discount", discount);
                intent.putExtra("buyType", "1"); //1、图书 2、课程 3、套系 4、章节 5、资源
                startActivity(intent);
            }

            @Override
            public void onCancle() {//扫码
                PermissionUtils.newIntence().requestPerssion(BookDetailActivity.this, ToolUtil.PERSSION_PHOTO, 101, new PermissionUtils.IPermission() {
                    @Override
                    public void success(int code) {
                        StartZxing(); //扫码
                    }
                });
            }

            @Override
            public void onConfig() {//手动绑码
                isInput = true;
                ToolUtil.showBindCodeDialog(BookDetailActivity.this, new OnItemListener<String>() {
                    @Override
                    public void onConfig(String s) {
                        if (s.length() == 18) {//16是新码 18是旧码
                            checkSecurityCode(s);
                        } else {
                            checkNewSecurityCode(s);
                        }
                    }
                });
            }
        });
//        MyDialog.Builder builder = new MyDialog.Builder(mContext);
//        builder.setTitle("验证防伪码");
//        builder.setMessage("验证并绑定本书防伪码后，购买整书资源可享受正版优惠价格、赠送答疑券等多项福利，是否验证？");
//
//        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                //设置你的操作事项
//                Intent intent = new Intent(mContext, ResourceBuyActivity.class);
//                intent.putExtra("Id", bookId);
//                intent.putExtra("buyType", "1"); //1、图书 2、课程 3、套系 4、章节 5、资源
//                startActivity(intent);
//            }
//        });
//
//        builder.setNegativeButton("确定",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        //进行扫一扫
//                        StartZxing();
//                        dialog.dismiss();
//                    }
//                });
//
//        builder.create().show();

    }

    /**
     * 验证防伪码成功后，显示，是否绑定dialog
     *
     * @param code
     * @param bookId
     */
    public void showtoBindDialog(final String code, final String bookId) {

        MyDialog.Builder builder = new MyDialog.Builder(mContext);
        builder.setTitle("防伪码验证");
        builder.setMessage("现在绑定，购买整书享优惠。");

        builder.setPositiveButton("不绑定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                intent.putExtra("Id", bookId);
                intent.putExtra("discount", discount);
                intent.putExtra("buyType", "1"); //1、图书 2、课程 3、套系 4、章节 5、资源
                startActivity(intent);
            }
        });

        builder.setNegativeButton("绑定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        toBind(code, bookId);
                        dialog.dismiss();
                    }
                });

        builder.create().show();

    }


    @OnClick({R.id.btn_left_title, R.id.btn_refresh_net, R.id.btn_buy, R.id.btn_buyIn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_refresh_net:
                getData(true);
                break;
            case R.id.btn_buy://购买
                if (!NetworkUtil.isConnected(mContext)) {
                    ToastUtils.showToastLONG(mContext, "网络未连接");
                    return;
                }
                buyBook();
                break;
            case R.id.btn_buyIn://加入书包
                if (!NetworkUtil.isConnected(mContext)) {
                    ToastUtils.showToastLONG(mContext, "网络未连接");
                    return;
                }
                joinShuBao();
                break;
        }
    }

    /**
     * 加入书包
     */
    private void joinShuBao() {
        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("bookId", bookId);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).homeBookAddBag(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.GONE);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getData(true);
                            }
                        });
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    /**
     * 点击购买图书按钮
     */
    private void buyBook() {
        if (mBookDetailBean != null) {
            if (mBookDetailBean.getData().isBuy()) {
                ToastUtils.showToastSHORT(mContext, mContext.getResources().getString(R.string.resources_have_been_purchased));
                return;
            }

            if (!mBookDetailBean.getData().isBookHaveFile()) {
                showIsHaveFileBook();
            } else {
                checkHasSecurityCode();
            }
        }
    }

    /**
     * 根据是否有安全码判断是否显示扫码绑定
     */
    private void checkHasSecurityCode() {
        if (!mBookDetailBean.getData().isHasSecurityCode()) {
            Intent intent = new Intent(mContext, ResourceBuyActivity.class);
            intent.putExtra("Id", bookId);
            intent.putExtra("discount", discount);
            intent.putExtra("buyType", "1"); //1、图书 2、课程 3、套系 4、章节 5、资源
            startActivity(intent);
        } else if (!mBookDetailBean.getData().isBind()) {
            showBindDialog();
        } else {
            Intent intent = new Intent(mContext, ResourceBuyActivity.class);
            intent.putExtra("Id", bookId);
            intent.putExtra("discount", discount);
            intent.putExtra("buyType", "1"); //1、图书 2、课程 3、套系 4、章节 5、资源
            startActivity(intent);
        }
    }

    /**
     * 显示
     */
    private void showIsHaveFileBook() {
        MyDialog.Builder builder = new MyDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("部分资源尚未上传，上传后会自动为您开通使用权限，是否整书购买？");

        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
            }
        });

        builder.setNegativeButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        checkHasSecurityCode();
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }


}
