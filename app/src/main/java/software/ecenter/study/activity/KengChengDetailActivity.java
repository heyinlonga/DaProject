package software.ecenter.study.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
import software.ecenter.study.Adapter.ZiyuanAdapter;
import software.ecenter.study.Interface.OnItemListener;
import software.ecenter.study.R;
import software.ecenter.study.View.AutoPollRecyclerView;
import software.ecenter.study.View.ExpandableTextView;
import software.ecenter.study.View.MyDialog;
import software.ecenter.study.View.treelist.Node;
import software.ecenter.study.bean.HomeBean.BookDetailBean;
import software.ecenter.study.bean.HomeBean.ChapterItemBean;
import software.ecenter.study.bean.HomeBean.CurriculumDetailBean;
import software.ecenter.study.bean.HomeBean.PackageBean;
import software.ecenter.study.bean.HomeBean.ResourceBean;
import software.ecenter.study.bean.HomeBean.SecondBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.NetworkUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;


/***
 * dec 课程详情
 * */
public class KengChengDetailActivity extends BaseActivity {

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
    @BindView(R.id.btn_buy)
    TextView btnBuy;
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
    @BindView(R.id.list_kecheng)
    RecyclerView listKecheng;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
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
    @BindView(R.id.tv_time)
    TextView tv_time;
    private String curriculumId;
    private List<PackageBean> packageData;
    private List<ResourceBean> ResourceData;
    private CurriculumDetailBean curriculumDetailBean;
    private List<ChapterItemBean> chapterItemBeanList;
    private ChapterListAdapter mChapterListAdapter;
    private boolean discount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        curriculumId = getIntent().getStringExtra("curriculumId");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(false);
    }

    private void initView() {
        setContentView(R.layout.activity_kengcheng_detail);
        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManagerOne = new LinearLayoutManager(mContext);
        linearLayoutManagerOne.setOrientation(LinearLayoutManager.VERTICAL);
        listThree.setLayoutManager(linearLayoutManagerOne);

        listKecheng.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

    }

    public void setResponseView(CurriculumDetailBean bean) {
        if (bean == null) return;
        if (bean.getData() == null) return;

        textBookJiage.setVisibility(View.VISIBLE);
        ll_youhui.setVisibility(View.GONE);

        if (mContext != null)
            Glide.with(mContext).load(bean.getData().getCurriculumImage()).into(imgBook);
        textBookName.setText(bean.getData().getCurriculumName() + "");
        if (!TextUtils.isEmpty(bean.getData().getCurriculumPrice()) && bean.getData().getCurriculumPrice().equals("0")) {
            textBookJiage.setText("价格：免费");
        } else {
            textBookJiage.setText("价格：" + bean.getData().getCurriculumPrice() + "元宝");
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(textBookJiage.getText().toString());
        ForegroundColorSpan blueColor = new ForegroundColorSpan(getResources().getColor(R.color.textColor));
        builder.setSpan(blueColor, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textBookJiage.setText(builder);
        setBtnYouHui(bean);
        //购买状态
        int batchBuyStatus = bean.getData().getBatchBuyStatus();
        if (batchBuyStatus == 3) {
            if (!TextUtils.isEmpty(bean.getData().getCurriculumPrice()) && bean.getData().getCurriculumPrice().equals("0")) {
                btnBuy.setText("获取");
            } else {
                btnBuy.setText("追加购买");
            }
            btnBuy.setEnabled(true);
            btnBuy.setBackground(getResources().getDrawable(R.drawable.btn_sign_shape_circle_click));
        } else if (bean.getData().isBuy()) {
            if (!TextUtils.isEmpty(bean.getData().getCurriculumPrice()) && bean.getData().getCurriculumPrice().equals("0")) {
                btnBuy.setText("已获取");
            } else {
                btnBuy.setText("已购买");
            }
            btnBuy.setEnabled(false);
            btnBuy.setBackground(getResources().getDrawable(R.drawable.btn_sign_shape_circle_unclick));

            //已获取 不显示价格
            textBookJiage.setVisibility(View.INVISIBLE);
            ll_youhui.setVisibility(View.INVISIBLE);
        } else {
            if (!TextUtils.isEmpty(bean.getData().getCurriculumPrice()) && bean.getData().getCurriculumPrice().equals("0")) {
                btnBuy.setText("获取");
            } else {
                btnBuy.setText("购买");
            }
        }


        titleContent.setText(bean.getData().getCurriculumName());


        expandableText = (ExpandableTextView) findViewById(R.id.expandable_text);
        expandableText.setText(bean.getData().getCurriculumIntroduction());


        //假数据 套系
        packageData = bean.getData().getPackageList();
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
        boolean showCategory = bean.getData().isShowCategory();
        if (showCategory) {
            setChapterList(bean);
        } else {
            setResourceList(bean);
        }

    }

    //设置优惠价
    private void setBtnYouHui(CurriculumDetailBean bean) {
        CurriculumDetailBean.Data data = bean.getData();
        if (data != null && !bean.getData().isBuy()) {
            discount = data.isDiscount();
            int coinDiscount = data.getCoinDiscount();
            int couponDiscount = data.getCouponDiscount();
            int bonusDiscount = data.getBonusDiscount();
            if (discount) {//有优惠
                ll_youhui.setVisibility(View.VISIBLE);
                textBookJiage.setVisibility(View.INVISIBLE);
                tv_youProce.setText(data.getCurriculumPrice() + "元宝");
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


    //章节列表
    private void setChapterList(CurriculumDetailBean bean) {
        chapterItemBeanList = bean.getData().getChapterList();
        //第一个参数  RecyclerView
        //第二个参数  上下文
        //第三个参数  数据集
        //第四个参数  默认展开层级数 0为不展开
        //第五个参数  展开的图标
        //第六个参数  闭合的图标
        if (chapterItemBeanList != null) {
            try {
                mChapterListAdapter = new ChapterListAdapter(listKecheng, this,
                        chapterItemBeanList, 0);

                mChapterListAdapter.setOnTreeNodeClickListener(new ChapterListAdapter.OnTreeNodeClickListener() {
                    @Override
                    public void onClick(Node node, int position, boolean isJump) {
                        ChapterItemBean chapterItemBean = null;
                        for (ChapterItemBean chapterItemBean1 : chapterItemBeanList) {
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
                                intent.putExtra("newBatch", node.isNewBatch() ? 1 : 0);
                                intent.putExtra("type", 1);
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
                                intent.putExtra("type", 1);
                                intent.putExtra("newBatch", node.isNewBatch() ? 1 : 0);
                                startActivity(intent);
                            } else if (chapterItemBean.getIsExpand() == 0) {
                                ToastUtils.showToastSHORT(mContext, "该目录下暂无资源！");
                            }
                        }

                    }

                });

                listKecheng.setAdapter(mChapterListAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    //资源列表
    private void setResourceList(CurriculumDetailBean bean) {
        ResourceData = bean.getData().getResourceList();

        ZiyuanAdapter ziyuanAdapter = new ZiyuanAdapter(mContext, ResourceData);
        ziyuanAdapter.setItemClickListener(new ZiyuanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (!ResourceData.get(position).isHaveResourceFile()) {
                    ToastUtils.showToastLONG(mContext, "资源即将上传");
                    return;
                }
                Intent intent = new Intent(mContext, SeeResourcesVideoActivity.class);
                intent.putExtra("resourceId", ResourceData.get(position).getResourceId());
                startActivity(intent);

            }
        });
        listKecheng.setAdapter(ziyuanAdapter);
    }


    public void getData(boolean isShow) {
        if (isShow) {
            if (!showNetWaitLoding()) {
                return;
            }
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("curriculumId", curriculumId);

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).homeCurriculumDetail(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.GONE);
                        CurriculumDetailBean bean = ParseUtil.parseBean(response, CurriculumDetailBean.class);
                        curriculumDetailBean = bean;
                        setResponseView(bean);
                        setBuySensond(bean);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    //第2批次购买
    private void setBuySensond(CurriculumDetailBean bean) {
        if (bean != null) {
            CurriculumDetailBean.Data data = bean.getData();
            if (data != null) {
                boolean batchNotice = data.isBatchNotice();
                if (batchNotice) {//需要弹出购买
                    CurriculumDetailBean.Data.SecondBatch secondBatch = data.getSecondBatch();
                    if (secondBatch != null) {
                        getSecondData(secondBatch.getId());
                    }
                }
            }
        }
    }

    //第2批次通知
    public void getSecondData(String secondBatchId) {
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("curriculumId", curriculumId);
        map.put("secondBatchId", secondBatchId);
        JSONObject json = new JSONObject();
        try {
            json.put("curriculumId", curriculumId);
            json.put("secondBatchId", secondBatchId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getCurriculumSecondNofice(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        SecondBean bean = ParseUtil.parseBean(response, SecondBean.class);
                        if (bean != null && bean.getData() != null) {
                            ToolUtil.showBuySensondDialog(KengChengDetailActivity.this,
                                    ToolUtil.getString(bean.getData().getBatchName()), bean.getData().getCoinPrice() + "",
                                    ToolUtil.getString(bean.getData().getResourceCount()), new OnItemListener() {
                                        @Override
                                        public void onConfig() {
                                            getBuySecond();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    @OnClick({R.id.btn_left_title, R.id.btn_buy, R.id.btn_refresh_net})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_buy://购买
                if (!NetworkUtil.isConnected(mContext)) {
                    ToastUtils.showToastLONG(mContext, "网络未连接");
                    return;
                }

                //课程不绑定防伪码
                if (curriculumId != null && curriculumDetailBean != null) {
                    int batchBuyStatus = curriculumDetailBean.getData().getBatchBuyStatus();
                    if (batchBuyStatus == 3) {//追加购买
                        getBuySecond();
                    } else if (!curriculumDetailBean.getData().isBuy()) {
                        if (!curriculumDetailBean.getData().isHaveFile()) {
                            showIsHaveFileBook();
                            return;
                        }
                        Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                        intent.putExtra("Id", curriculumId);
                        intent.putExtra("discount", discount);
                        intent.putExtra("buyType", "2"); //1、图书 2、课程 3、套系 4、章节 5、资源
                        startActivity(intent);

                    } else {
                        ToastUtils.showToastSHORT(mContext, getResources().getString(R.string.resources_have_been_purchased));
                    }
                }
                break;
            case R.id.btn_refresh_net:
                getData(true);
                break;
        }
    }

    //第2批次购买
    private void getBuySecond() {
        if (curriculumDetailBean != null) {
            Intent intent = new Intent(this, ResourceBuyActivity.class);
            intent.putExtra("Id", curriculumId);
            intent.putExtra("isAppendBuy", true);
            intent.putExtra("secondBatchId", curriculumDetailBean.getData().getSecondBatch().getId());
            intent.putExtra("buyType", "2"); //1、图书 2、课程 3、套系 4、章节 5、资源12、课程批次
            startActivity(intent);
        }
    }

    /**
     * 显示
     */
    private void showIsHaveFileBook() {
        MyDialog.Builder builder = new MyDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("部分资源尚未上传，上传后会自动为您开通使用权限，是否购买课程?");

        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
            }
        });

        builder.setNegativeButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                        intent.putExtra("Id", curriculumId);
                        intent.putExtra("discount", discount);
                        intent.putExtra("buyType", "2"); //1、图书 2、课程 3、套系 4、章节 5、资源
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

}
