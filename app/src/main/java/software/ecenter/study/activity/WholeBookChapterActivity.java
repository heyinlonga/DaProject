package software.ecenter.study.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import software.ecenter.study.Adapter.WholeBookOneAdapter;
import software.ecenter.study.Interface.OnItemListener;
import software.ecenter.study.R;
import software.ecenter.study.View.ExpandableTextView;
import software.ecenter.study.bean.CateBean;
import software.ecenter.study.bean.HomeBean.BookDetailBean;
import software.ecenter.study.bean.WholeBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message 整本书阅读  视频讲座章节列表
 * Create by Administrator
 * Create by 2019/8/15
 */
public class WholeBookChapterActivity extends BaseActivity implements View.OnClickListener {

    private ExpandableTextView expandableTextView;
    private RecyclerView rv_list;
    private LinearLayout ll_oldPric;
    private ImageView iv_back;
    private TextView tv_title;
    private ImageView iv_icon;
    private TextView tv_name;
    private TextView tv_gold;
    private TextView tv_pay;
    LinearLayout ll_youhui;
    TextView tv_youProce;
    TextView tv_youhui;
    TextView tv_dyj;
    TextView tv_jifen;
    TextView tv_time;
    private String id;
    private boolean isBuy;
    private int categoryShowType;//0：不显示目录，1：显示到章，2：显示到节
    private WholeBookOneAdapter wholeBookOneAdapter;
    private boolean discount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholebookchapter);
        id = getIntent().getStringExtra("id");
        initView();
        getData();
    }

    //初始化控件
    private void initView() {
        ll_oldPric = findViewById(R.id.ll_oldPric);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        iv_icon = findViewById(R.id.iv_icon);
        tv_name = findViewById(R.id.tv_name);
        tv_gold = findViewById(R.id.tv_gold);
        tv_pay = findViewById(R.id.tv_pay);
        ll_youhui = findViewById(R.id.ll_youhui);
        tv_youProce = findViewById(R.id.tv_youProce);
        tv_youhui = findViewById(R.id.tv_youhui);
        tv_dyj = findViewById(R.id.tv_dyj);
        tv_jifen = findViewById(R.id.tv_jifen);
        tv_time = findViewById(R.id.tv_time);
        expandableTextView = findViewById(R.id.expandable_text);
        rv_list = findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        wholeBookOneAdapter = new WholeBookOneAdapter(mContext, new OnItemListener() {
            //章点击事件
            @Override
            public void onItemClick(int poc) {
                CateBean choseData = wholeBookOneAdapter.getChoseData(poc);
                if (choseData != null) {
                    if (choseData.isNoFile()) {
                        ToastUtils.showToastSHORT(mContext, "资源即将上传，敬请期待");
                        return;
                    }
                    startActivity(new Intent(mContext, WholeBookCourseActivity.class)
                            .putExtra("type", 1)
                            .putExtra("id", String.valueOf(choseData.getId())));
                }
            }

            //节点击事件
            @Override
            public void onChildItemClick(int parentPoc, int poc) {
                CateBean choseData = wholeBookOneAdapter.getChoseData(parentPoc);
                if (choseData != null) {
                    List<CateBean> children = choseData.getChildren();
                    if (ToolUtil.isList(children) && poc < children.size()) {
                        CateBean cateBean = children.get(poc);
                        if (cateBean != null) {
                            if (cateBean.isNoFile()) {
                                ToastUtils.showToastSHORT(mContext, "资源即将上传");
                                return;
                            }
                            startActivity(new Intent(mContext, WholeBookCourseActivity.class)
                                    .putExtra("type", 1)
                                    .putExtra("id", String.valueOf(cateBean.getId())));
                        }
                    }
                }
            }
        });
        rv_list.setAdapter(wholeBookOneAdapter);
        iv_back.setOnClickListener(this);
        tv_pay.setOnClickListener(this);

    }

    //获取数据
    private void getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getlectureChapter(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        WholeBean bean = new Gson().fromJson(response, WholeBean.class);
                        if (bean != null) {
                            WholeBean.DataBean data = bean.getData();
                            if (data != null) {
                                if (Util.isOnMainThread()) {
                                    if (!isFinishing())
                                        Glide.with(mContext).load(data.getImgUrl()).error(R.drawable.morenshu).into(iv_icon);
                                }
                                tv_title.setText(data.getName());
                                tv_name.setText(data.getName());
                                tv_gold.setText(data.getCoinPrice() + "元宝");
                                isBuy = data.isIsBuy();
                                categoryShowType = data.getCategoryShowType();
                                expandableTextView.setText(data.getDescription());
                                wholeBookOneAdapter.setType(categoryShowType);
                                wholeBookOneAdapter.setData(data.getCategoryList());
                                setBtnYouHui(data);
                                showBuyLay();
                            }
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });

    }

    //购买按钮
    private void showBuyLay() {
        if (isBuy) {
            tv_pay.setText("已购买");
            tv_pay.setEnabled(false);
            tv_pay.setBackground(getResources().getDrawable(R.drawable.btn_sign_shape_circle_unclick));
            ll_youhui.setVisibility(View.GONE);
            ll_oldPric.setVisibility(View.INVISIBLE);
        }
    }

    //设置优惠价
    private void setBtnYouHui(WholeBean.DataBean data) {
        if (data != null&&!data.isIsBuy()) {
            discount = data.isDiscount();
            int coinDiscount = data.getCoinDiscount();
            int couponDiscount = data.getCouponDiscount();
            int bonusDiscount = data.getBonusDiscount();
            if (discount) {//有优惠
                ll_youhui.setVisibility(View.VISIBLE);
                ll_oldPric.setVisibility(View.INVISIBLE);
                tv_youProce.setText(data.getCoinPrice() + "元宝");
                if (coinDiscount > 0) {//优惠价
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
                    String text = "赠送：" + couponDiscount + "张答疑劵";
                    SpannableStringBuilder builder = new SpannableStringBuilder(text);
                    ForegroundColorSpan startColor = new ForegroundColorSpan(getResources().getColor(R.color.color_C9AB79));
                    builder.setSpan(startColor, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ForegroundColorSpan blueColor = new ForegroundColorSpan(getResources().getColor(R.color.color_F1916E));
                    builder.setSpan(blueColor, 3, text.length() - 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.setSpan(new AbsoluteSizeSpan(16, true), 3, text.length() - 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_dyj.setText(builder);
                }
                if (bonusDiscount > 0) {//积分
                    tv_jifen.setVisibility(View.VISIBLE);
                    String text = "赠送：" + bonusDiscount + "积分";
                    SpannableStringBuilder builder = new SpannableStringBuilder(text);
                    ForegroundColorSpan startColor = new ForegroundColorSpan(getResources().getColor(R.color.color_C9AB79));
                    builder.setSpan(startColor, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ForegroundColorSpan blueColor = new ForegroundColorSpan(getResources().getColor(R.color.color_F1916E));
                    builder.setSpan(blueColor, 3, text.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.setSpan(new AbsoluteSizeSpan(16, true), 3, text.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.tv_pay://购买
                Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                intent.putExtra("Id", String.valueOf(id));
                intent.putExtra("buyType", "8"); //8讲座
                intent.putExtra("discount", discount);
                startActivityForResult(intent, 1);
                break;
        }
    }

    //购买成功
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == 1 && resultCode == 1) {
            isBuy = true;
            showBuyLay();
        }
    }
}
