package software.ecenter.study.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.R;
import software.ecenter.study.View.ReportFormatter;
import software.ecenter.study.View.SchoolPopWindow;
import software.ecenter.study.View.SharePopWindow;
import software.ecenter.study.bean.ReportDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.BitmapUtils;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.Share;
import software.ecenter.study.utils.ShareUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message  学情报告详情
 * Create by Administrator
 * Create by 2019/11/5
 */
public class ReportDetailActivity extends BaseActivity implements OnChartValueSelectedListener, SchoolPopWindow.MItemSelectListener {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.ll_buy)
    LinearLayout ll_buy;
    @BindView(R.id.ll_noBuy)
    LinearLayout ll_noBuy;
    @BindView(R.id.tv_buy)
    TextView tv_buy;
    @BindView(R.id.pc_chart)
    PieChart pc_chart;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_tiNum)
    TextView tv_tiNum;
    @BindView(R.id.tv_zyNum)
    TextView tv_zyNum;
    @BindView(R.id.tv_lxNum)
    TextView tv_lxNum;
    @BindView(R.id.tv_dyNum)
    TextView tv_dyNum;
    @BindView(R.id.tv_zwNum)
    TextView tv_zwNum;
    @BindView(R.id.tv_yddsNum)
    TextView tv_yddsNum;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.ll_price)
    LinearLayout ll_price;
    @BindView(R.id.tv_pText)
    TextView tv_pText;
    @BindView(R.id.tv_ywNum)
    TextView tv_ywNum;
    @BindView(R.id.tv_sxNum)
    TextView tv_sxNum;
    @BindView(R.id.tv_yyNum)
    TextView tv_yyNum;
    @BindView(R.id.tv_ydNum)
    TextView tv_ydNum;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ll_nodis)
    LinearLayout ll_nodis;
    @BindView(R.id.ll_youhui)
    LinearLayout ll_youhui;
    @BindView(R.id.tv_youProce)
    TextView tv_youProce;
    @BindView(R.id.tv_youhui)
    TextView tv_youhui;
    @BindView(R.id.tv_dyj)
    TextView tv_dyj;
    @BindView(R.id.tv_jifen)
    TextView tv_jifen;
    @BindView(R.id.tv_youtime)
    TextView tv_youtime;
    @BindView(R.id.ll_share)
    LinearLayout ll_share;
    @BindView(R.id.ll_erweima)
    LinearLayout ll_erweima;
    @BindView(R.id.ll_fenxiang)
    LinearLayout ll_fenxiang;
    @BindView(R.id.scroll_search)
    ScrollView scroll_search;
    @BindView(R.id.tv_share_text)
    TextView tv_share_text;

    private SharePopWindow sharePopWindow;
    private String id;
    private int reportYear;
    private int reportMonth;
    private boolean isFree;
    protected final String[] titles = new String[]{"查看资源", "练习", "答疑", "作文投稿", "阅读大赛"};
    protected final String[] cols = new String[]{"F9C900", "00D6B7", "14BBF2", "A071EA", "EE7C55"};
    //    protected final int[] cols = new int[]{R.color.color_F9C900, R.color.color_00D6B7, R.color.color_14BBF2, R.color.color_A071EA, R.color.color_EE7C55};
    private boolean isBuy;
    private int coinDiscount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportdetail);
        ButterKnife.bind(this);
        reportYear = getIntent().getIntExtra("reportYear", 0);
        reportMonth = getIntent().getIntExtra("reportMonth", 0);
        isBuy = getIntent().getBooleanExtra("isBuy", false);
        int type = getIntent().getIntExtra("type", 0);
        showChart();
        if (type == 1) {
            tv_title.setText("报告样例");
            pc_chart.setVisibility(View.VISIBLE);
            ll_buy.setVisibility(View.VISIBLE);
            ll_noBuy.setVisibility(View.GONE);
            ll_share.setVisibility(View.VISIBLE);
            setData(Arrays.asList(titles), getTestData(), Arrays.asList(cols));
            tv_share_text.setText("状元共享课堂能生成学情报告？\n看着不错，要不要来试试？");
        } else {
            if (isBuy) {
                getBuyData();
            } else {
                getNotBuyData();
            }
        }
    }

    //初始化图表
    private void showChart() {
        pc_chart.setUsePercentValues(false);
        pc_chart.getDescription().setEnabled(false);
        pc_chart.setExtraOffsets(5, 10, 5, 5);
        pc_chart.setDragDecelerationFrictionCoef(0.95f);
        pc_chart.setCenterText("");

        pc_chart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        pc_chart.setDrawHoleEnabled(true);
        pc_chart.setHoleColor(Color.parseColor("#fffdec"));//中间的背景颜色

        pc_chart.setTransparentCircleColor(Color.parseColor("#ffffff"));
        pc_chart.setTransparentCircleAlpha(110);

        pc_chart.setHoleRadius(40f);
        pc_chart.setTransparentCircleRadius(44f);

        pc_chart.setDrawCenterText(true);

        pc_chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pc_chart.setRotationEnabled(true);
        pc_chart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        pc_chart.setOnChartValueSelectedListener(this);
        pc_chart.setEntryLabelColor(Color.parseColor("#6A3906"));

        pc_chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = pc_chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);

        sharePopWindow = new SharePopWindow(mContext);
        sharePopWindow.setItemSelectListener(this);
        sharePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ll_erweima.setVisibility(View.GONE);
                ll_fenxiang.setVisibility(View.GONE);
            }
        });
    }

    //设置图表数据
    private void setData(List<String> pieChartName, List<Integer> count, List<String> pieColourNum) {
        try {
            if (ToolUtil.isList(pieChartName) && ToolUtil.isList(count) && ToolUtil.isList(pieColourNum)
                    && pieColourNum.size() == count.size() && pieColourNum.size() == pieChartName.size()) {
                ArrayList<PieEntry> entries = new ArrayList<>();
                ArrayList<Integer> colors = new ArrayList<>();
                // NOTE: The order of the entries when being added to the entries array determines their position around the center of
                // the chart.
                for (int i = 0; i < pieChartName.size(); i++) {
                    entries.add(new PieEntry(count.get(i) / 1f, pieChartName.get(i)));
                    colors.add(Color.parseColor("#" + pieColourNum.get(i)));
//                colors.add(getResources().getColor());
                }

                PieDataSet dataSet = new PieDataSet(entries, "Election Results");
                dataSet.setSliceSpace(1f);
                dataSet.setSelectionShift(5f);
                dataSet.setColors(colors);
                dataSet.setValueLinePart1OffsetPercentage(80.f);
                dataSet.setValueLinePart1Length(0.4f);
                dataSet.setValueLinePart2Length(0.6f);
                dataSet.setValueLineColor(Color.parseColor("#6A3906"));

                dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

                PieData data = new PieData(dataSet);
                data.setValueFormatter(new ReportFormatter(new DecimalFormat("#.#")));
                data.setDrawValues(true);
                data.setValueTextSize(12f);
                data.setValueTextColor(Color.parseColor("#6A3906"));
                pc_chart.setData(data);
//        if (bean != null && isOut(bean)) {
//            for (IDataSet<?> set : pc_chart.getData().getDataSets())
//                set.setDrawValues(false);
//            pc_chart.setDrawEntryLabels(false);
//        }
                // undo all highlights
                pc_chart.highlightValues(null);
                pc_chart.invalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private void getNotBuyData() {
        if (reportMonth == 0 || reportYear == 0) return;
        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        JSONObject json = new JSONObject();
        try {
            json.put("reportYear", reportYear);
            json.put("reportMonth", reportMonth);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getReportNotBuy(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ReportDetailBean bean = ParseUtil.parseBean(response, ReportDetailBean.class);
                        setView(bean);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    //学情报告详情
    private void getBuyData() {
        if (reportMonth == 0 || reportYear == 0) return;
        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        JSONObject json = new JSONObject();
        try {
            json.put("reportYear", reportYear);
            json.put("reportMonth", reportMonth);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getReportBuy(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ReportDetailBean bean = ParseUtil.parseBean(response, ReportDetailBean.class);
                        setView(bean);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    public String getStringByEnter(String string) throws Exception {
        for (int i = 1; i <= string.length(); i++) {
//            string.substring(0, i).getBytes("GBK").length
            String substring = string.substring(0, i);
            if (substring.length() > 6) {
                return string.substring(0, i - 1) + "\n" +
                        getStringByEnter(string.substring(i - 1));
            }
        }
        return string;
    }

    //设置数据
    @SuppressLint("SetTextI18n")
    private void setView(ReportDetailBean bean) {
        if (bean != null) {
            ReportDetailBean.DataBean data = bean.getData();
            if (data != null) {
                id = data.getId();
                try {
                    boolean phoneNumber = ToolUtil.isPhoneNumber(ToolUtil.getString(data.getNickname()));
                    if (phoneNumber) {
                        tv_name.setText((ToolUtil.getString(data.getNickname())));
                    } else {
                        tv_name.setText(getStringByEnter(ToolUtil.getString(data.getNickname())));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tv_time.setText(ToolUtil.getString(data.getReportDate()));
                tv_tiNum.setText("做题：" + data.getExerciseCount() + "道");
                tv_zyNum.setText("查看资源：" + data.getResourceCount() + "次");
                tv_lxNum.setText("练习：" + data.getPracticeCount() + "次");
                tv_dyNum.setText("答疑：" + data.getAskCount() + "次");
                tv_zwNum.setText("作文投稿：" + data.getCompositionCount() + "次");
                tv_yddsNum.setText("阅读大赛：" + data.getMatchCount() + "次");
                tv_price.setText(data.getPrice() + "");
                tv_ywNum.setText(data.getChineseExerciseCount() + "");
                tv_sxNum.setText(data.getMathExerciseCount() + "");
                tv_yyNum.setText(data.getEnglishExerciseCount() + "");
                tv_ydNum.setText(data.getMatchExerciseCount() + "");
                showLay(data);
                List<Integer> list = new ArrayList<>();
                list.add(data.getResourceCount());
                list.add(data.getPracticeCount());
                list.add(data.getAskCount());
                list.add(data.getCompositionCount());
                list.add(data.getMatchCount());
                if (data.getResourceCount() > 0 || data.getPracticeCount() > 0 || data.getAskCount() > 0 ||
                        data.getCompositionCount() > 0 || data.getMatchCount() > 0) {
                    setData(data.getPieChartName(), data.getPieChartNum(), data.getPieColourNum());
                }
                if (data.getExerciseCount() == 0 && data.getResourceCount() == 0 && data.getPracticeCount() == 0 && data.getAskCount() == 0
                        && data.getCompositionCount() == 0 && data.getMatchCount() == 0) {
                    pc_chart.setVisibility(View.GONE);
                }
            }
        }
    }

    private boolean isOut(ReportDetailBean.DataBean data) {
        int all = data.getResourceCount() + data.getPracticeCount() + data.getAskCount()
                + data.getCompositionCount() + data.getMatchCount();
        if (data.getResourceCount() == 0 || data.getResourceCount() * 1f / all > 0.8f) {
            return true;
        }
        if (data.getPracticeCount() == 0 || data.getPracticeCount() * 1f / all > 0.8f) {
            return true;
        }
        if (data.getAskCount() == 0 || data.getAskCount() * 1f / all > 0.8f) {
            return true;
        }
        if (data.getCompositionCount() == 0 || data.getCompositionCount() * 1f / all > 0.8f) {
            return true;
        }
        if (data.getMatchCount() == 0 || data.getMatchCount() * 1f / all > 0.8f) {
            return true;
        }
        return false;
    }

    //控件显示
    private void showLay(ReportDetailBean.DataBean data) {
        if (data != null) {
            boolean isBuy = data.isBuy();
            boolean isDiscount = data.isDiscount();
            isFree = data.getPrice() <= 0;
            boolean isStatus = ToolUtil.getString(data.getStatus()).equals("1");

            if (isBuy) {//已购买
                if (isStatus) {//是否审核通过
                    pc_chart.setVisibility(View.VISIBLE);
                    ll_buy.setVisibility(View.VISIBLE);
                    ll_noBuy.setVisibility(View.GONE);
                    ll_share.setVisibility(View.VISIBLE);
                } else {
                    pc_chart.setVisibility(View.GONE);
                    ll_buy.setVisibility(View.GONE);
                    ll_noBuy.setVisibility(View.VISIBLE);
                    ll_share.setVisibility(View.GONE);
                    ll_price.setVisibility(View.GONE);
                    ll_youhui.setVisibility(View.GONE);
                    ll_nodis.setVisibility(View.GONE);
                    tv_buy.setText("已购买");
                    tv_buy.setSelected(true);
                    tv_buy.setClickable(false);
                    if (isFree) {//免费
                        tv_price.setText("0");
                        tv_pText.setText("元宝");
                    }
                }
            } else {
                if (isFree) {//免费
                    tv_price.setText("0");
                    tv_pText.setText("元宝");
                } else if (isDiscount) {//是否有优惠
                    coinDiscount = data.getCoinDiscount();
                    int couponDiscount = data.getCouponDiscount();
                    int bonusDiscount = data.getBonusDiscount();
                    ll_youhui.setVisibility(View.VISIBLE);
                    ll_nodis.setVisibility(View.GONE);
                    tv_youProce.setText(data.getPrice() + "元宝");
                    if (coinDiscount > 0) {//优惠价
                        tv_youProce.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
                        tv_youhui.setVisibility(View.VISIBLE);
                        String text = "优惠价：" + coinDiscount + "元宝";
                        SpannableStringBuilder builder = new SpannableStringBuilder(text);
                        ForegroundColorSpan startColor = new ForegroundColorSpan(getResources().getColor(R.color.color_6a3906));
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
                        ForegroundColorSpan startColor = new ForegroundColorSpan(getResources().getColor(R.color.color_6a3906));
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
                        ForegroundColorSpan startColor = new ForegroundColorSpan(getResources().getColor(R.color.color_6a3906));
                        builder.setSpan(startColor, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ForegroundColorSpan blueColor = new ForegroundColorSpan(getResources().getColor(R.color.color_F1916E));
                        builder.setSpan(blueColor, 3, text.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builder.setSpan(new AbsoluteSizeSpan(16, true), 3, text.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tv_jifen.setText(builder);
                    }
                    if (data.getEndTime() > 0) {
                        long endTime = data.getEndTime() * 1000;
                        new CountDownTimer(endTime, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                tv_youtime.setText("" + ToolUtil.getTime(millisUntilFinished));
                            }

                            @Override
                            public void onFinish() {
                                tv_youtime.setText("已结束");
                                getNotBuyData();
                            }
                        }.start();
                    }
                }
                pc_chart.setVisibility(View.GONE);
                ll_buy.setVisibility(View.GONE);
                ll_noBuy.setVisibility(View.VISIBLE);
                ll_share.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_buy, R.id.ll_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_buy://购买
                if (isFree) {
                    getBuyFree();
                } else {
                    startActivityForResult(new Intent(mContext, ResourceBuyActivity.class).putExtra("Id", id).putExtra("discount", coinDiscount).putExtra("buyType", "11"), 1);
                }
                break;
            case R.id.ll_share: //学情报告分享
                ll_erweima.setVisibility(View.VISIBLE);
                ll_fenxiang.setVisibility(View.VISIBLE);
                sharePopWindow.showAtLocation(ll_share, Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    String localPath;

    @Override
    public void onItemClick(int position) {
        Share share = new Share();
        localPath = BitmapUtils.saveBitmap(this, BitmapUtils.shotScrollView(scroll_search), System.currentTimeMillis() + ".png");
        share.setUrl(localPath);
        switch (position) {
            case 1://朋友圈
                share.setType(1);
                ShareUtil.imageShare(this, share);
                break;
            case 2://微信好友
                share.setType(2);
                ShareUtil.imageShare(this, share);
                break;
            case 3://qq
                ShareUtil.shareImgToQQ(this, share);
                break;
            case 4://qq空间
                ShareUtil.shareImgToQQzone(this, share);
                break;
        }
    }

    //学情报告 免费购买
    private void getBuyFree() {
        if (TextUtils.isEmpty(id)) return;
        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getBuyFree(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showToastLONG(mContext, "购买成功");
                        dismissNetWaitLoging();
                        getBuyData();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == 1 && resultCode == 1) {
            tv_buy.setText("已购买");
            tv_buy.setSelected(true);
            tv_buy.setClickable(false);
            getBuyData();
        }
    }

    private List<Integer> getTestData() {
        List<Integer> list = new ArrayList<>();
        list.add(15);
        list.add(15);
        list.add(7);
        list.add(5);
        list.add(3);
        return list;
    }


}
