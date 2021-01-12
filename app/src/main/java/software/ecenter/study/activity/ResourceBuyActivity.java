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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.Adapter.PayTypeAdapter;
import software.ecenter.study.R;
import software.ecenter.study.alipay.PayResult;
import software.ecenter.study.bean.AlipayOrderInfo;
import software.ecenter.study.bean.HomeBean.BuyDetailBean;
import software.ecenter.study.bean.HomeBean.payTypeBean;
import software.ecenter.study.bean.WxBean.WxPayBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ConstantValue;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

import static software.ecenter.study.alipay.AlipayConstants.SDK_PAY_FLAG;

/**
 * dec 资源购买
 */
public class ResourceBuyActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.resoure_name_tip)
    TextView resoureNameTip;
    @BindView(R.id.resoure_name)
    TextView resoureName;
    @BindView(R.id.resoure_youxiao_tip)
    TextView resoureYouxiaoTip;
    @BindView(R.id.resoure_youxiao_name)
    TextView resoureYouxiaoName;
    @BindView(R.id.resoure_payall_tip)
    TextView resourePayallTip;
    @BindView(R.id.resoure_payall_price)
    TextView resourePayallPrice;
    @BindView(R.id.resoure_payall_img)
    ImageView resourePayallImg;
    @BindView(R.id.resoure_paytaoxi_tip)
    TextView resourePaytaoxiTip;
    @BindView(R.id.resoure_payjingpin_tip)
    TextView resourePayjingpinTip;
    @BindView(R.id.resoure_paytaoxi_price)
    TextView resourePaytaoxiPrice;
    @BindView(R.id.resoure_payjingpin_price)
    TextView resourePayjingpinPrice;
    @BindView(R.id.resoure_paytaoxi_img)
    ImageView resourePaytaoxiImg;
    @BindView(R.id.list_pay)
    RecyclerView listPay;
    @BindView(R.id.pay_price_tip)
    TextView payPriceTip;
    @BindView(R.id.pay_price)
    TextView payPrice;
    @BindView(R.id.btn_buy)
    Button btnBuy;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.cv_tuijian)
    CardView cvTuiJian;
    @BindView(R.id.rl_resoure_payall_tip)
    RelativeLayout rlResourePayAllTip;
    @BindView(R.id.rl_resoure_paytaoxi_tip)
    RelativeLayout rlSourePayTaoXiTip;
    @BindView(R.id.rl_resoure_payjingpin_tip)
    RelativeLayout rlSourePayingpinTip;
    private String payType = "";//支付类型
    private PayTypeAdapter payTypeAdapter;
    private List<payTypeBean> listData = new ArrayList<>();

    private int positio;
    private int type = 2;
    private String amount;
    private String Id;
    private int buyType;
    private String secondBatchId;
    private BuyDetailBean mBuyDetailBean;
    private IWXAPI api;
    private boolean isDiscount;
    private boolean isAppendBuy;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                        dismissNetWaitLoging();
                        setResult(1, new Intent());
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                        dismissNetWaitLoging();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_buy);
        ButterKnife.bind(this);
        api = WXAPIFactory.createWXAPI(this, ConstantValue.APP_ID);
        Id = getIntent().getStringExtra("Id");
        isDiscount = getIntent().getBooleanExtra("discount", false);
        isAppendBuy = getIntent().getBooleanExtra("isAppendBuy", false);
        buyType = Integer.parseInt(getIntent().getStringExtra("buyType"));
        secondBatchId = getIntent().getStringExtra("secondBatchId");
        listPay.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        getBuyMess();

    }

    //获取购买信息
    private void getBuyMess() {
        if (buyType == 11) {//学情报告
            getBuyReport();
        } else {
            getBuyResource();
        }
    }

    public void setResponseView(BuyDetailBean bean) {
        mBuyDetailBean = bean;
        isDiscount = bean.getData().isDiscount();
        titleContent.setText(bean.getData().getName());
        resoureName.setText(bean.getData().getName());
        resoureYouxiaoName.setText(bean.getData().getValidity());
        resoureYouxiaoName.setVisibility(View.GONE);
        if ((bean.getData().getRecommendBookList() == null || bean.getData().getRecommendBookList().size() <= 0) &&
                (bean.getData().getRecommendPackageList() == null || bean.getData().getRecommendPackageList().size() <= 0) &&
                (bean.getData().getRecommendCurriculumList() == null || bean.getData().getRecommendCurriculumList().size() <= 0)
                )
            cvTuiJian.setVisibility(View.GONE);
        else {
            cvTuiJian.setVisibility(View.VISIBLE);
            if (bean.getData().getRecommendBookList() != null && bean.getData().getRecommendBookList().size() > 0) {
                rlResourePayAllTip.setVisibility(View.VISIBLE);
                resourePayallTip.setText("【整书购买】" + bean.getData().getRecommendBookList().get(0).getRecommendBook());
                String discountPrice = bean.getData().getRecommendBookList().get(0).getDiscountPrice();
                if (!TextUtils.isEmpty(discountPrice) && !discountPrice.equals("0")) {
                    resourePayallPrice.setText(bean.getData().getRecommendBookList().get(0).getDiscountPrice());
                } else {
                    resourePayallPrice.setText(bean.getData().getRecommendBookList().get(0).getRecommendBookProice());
                }
            } else {
                rlResourePayAllTip.setVisibility(View.GONE);
            }

//            if (bean.getData().getRecommendPackageList() != null && bean.getData().getRecommendPackageList().size() > 0) {
//                rlSourePayTaoXiTip.setVisibility(View.VISIBLE);
//                resourePaytaoxiTip.setText("【套系购买】" + bean.getData().getRecommendPackageList().get(0).getRecommendPackage());
//                resourePaytaoxiPrice.setText(bean.getData().getRecommendPackageList().get(0).getRecommendPackageProice());
//            } else {
//                rlSourePayTaoXiTip.setVisibility(View.GONE);
//            }
            if (bean.getData().getRecommendCurriculumList() != null && bean.getData().getRecommendCurriculumList().size() > 0) {
                rlSourePayingpinTip.setVisibility(View.VISIBLE);
                resourePayjingpinTip.setText("【全课购买，超值优惠！】");
//                resourePayjingpinTip.setText("【精品课程】" + bean.getData().getRecommendCurriculumList().get(0).getRecommendCurriculum());
                String discountPrice = bean.getData().getRecommendCurriculumList().get(0).getDiscountPrice();
                if (!TextUtils.isEmpty(discountPrice) && !discountPrice.equals("0")) {
                    resourePayjingpinPrice.setText(bean.getData().getRecommendCurriculumList().get(0).getDiscountPrice());
                } else {
                    resourePayjingpinPrice.setText(bean.getData().getRecommendCurriculumList().get(0).getRecommendCurriculumProice());
                }
            } else {
                rlSourePayingpinTip.setVisibility(View.GONE);
            }
        }

        //支付
        listData = bean.getData().getPayTypeData();
        payTypeAdapter = new PayTypeAdapter(mContext, listData, isDiscount);
        if (listData != null)
            CheckItem(0);
        payTypeAdapter.setItemClickListener(new PayTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                CheckItem(position);
            }
        });
        listPay.setAdapter(payTypeAdapter);
    }

    public void CheckItem(int position) {
        for (payTypeBean item : listData) { //全部置为未选中
            item.setCheck(false);
        }
        listData.get(position).setCheck(listData.get(position).isCheck() ? false : true);
        positio = position;

        payType = listData.get(position).getPayType();
        String price;
        if (isDiscount) {
            if (TextUtils.isEmpty(listData.get(position).getDiscountNum())) {
                price = listData.get(position).getPayNum();
            } else {
                price = listData.get(position).getDiscountNum();
            }
        } else {
            price = listData.get(position).getPayNum();
        }
        //底部动态变化
        if ("元".equals(listData.get(position).getPayUnit())) {
            payPrice.setText("￥" + price + listData.get(position).getPayUnit());
        } else {
            payPrice.setText(price + listData.get(position).getPayUnit());
        }
        amount = price;
        payTypeAdapter.notifyDataSetChanged();
    }

    //购买资源信息
    public void getBuyResource() {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("id", Id);
            if (buyType == 1 || buyType == 2 || buyType - 1 == 7)
                json.put("isDiscount", isDiscount);
            json.put("buyType", buyType >= 7 ? buyType - 1 : buyType);//精品课程后都要减1  6被答疑用了
            if (buyType == 2) {//追加购买
                json.put("isAppendBuy",isAppendBuy);
                json.put("secondBatchId",secondBatchId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getBuyResource(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.GONE);
                        BuyDetailBean bean = ParseUtil.parseBean(response, BuyDetailBean.class);
                        if (bean != null)
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

    //购买学情报告信息
    public void getBuyReport() {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("id", Id);
            json.put("isDiscount", isDiscount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getBuyReport(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.GONE);
                        BuyDetailBean bean = ParseUtil.parseBean(response, BuyDetailBean.class);
                        if (bean != null)
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


    @OnClick({R.id.btn_left_title, R.id.btn_buy, R.id.btn_refresh_net, R.id.rl_resoure_payall_tip, R.id.rl_resoure_paytaoxi_tip, R.id.rl_resoure_payjingpin_tip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                finish();
                break;
            case R.id.btn_refresh_net:
                getBuyMess();
                break;
            case R.id.btn_buy://精品课程目录  传7
                if (payType.equals("1")) {
                    //微信
                    wechatBuy(type, amount, buyType, Id);
                }
                if (payType.equals("2")) {
                    //支付宝
                    aliPayBuy(type, amount, buyType, Id);
                }
                if (payType.equals("3")) {
                    //学习币
                    studycoinBuy(type, amount, buyType, Id);
                }
                if (payType.equals("4")) {
                    //积分
                    integralBuy(type, amount, buyType, Id);
                }
                if (payType.equals("5")) {
                    //答疑
                    questionBuy(type, amount, buyType, Id);
                }
                break;
            case R.id.rl_resoure_payall_tip:
                if (mBuyDetailBean.getData().getRecommendBookList().size() > 0) {
                    Intent intent = new Intent(ResourceBuyActivity.this, BookDetailActivity.class);
                    intent.putExtra(" bookId", mBuyDetailBean.getData().getRecommendBookList().get(0).getRecommendBookId() + "");
                    String bookId = "";
                    bookId = mBuyDetailBean.getData().getRecommendBookList().get(0).getRecommendBookId();

                    Bundle bundle = new Bundle();
                    bundle.putString("bookId", bookId);
                    intent.putExtra("package", bundle);

                    startActivity(intent);
                }
                break;
            case R.id.rl_resoure_paytaoxi_tip:
                if (mBuyDetailBean.getData().getRecommendPackageList().size() > 0) {
                    Intent intentTaoXi = new Intent(ResourceBuyActivity.this, TaoXiDetailActivity.class);
                    String packageId = "";
                    packageId = mBuyDetailBean.getData().getRecommendPackageList().get(0).getRecommendPackageId();
                    Bundle bundle = new Bundle();
                    bundle.putString("packageId", packageId);
                    intentTaoXi.putExtra("package", bundle);
                    startActivity(intentTaoXi);
                }
                break;
            case R.id.rl_resoure_payjingpin_tip:
                if (mBuyDetailBean.getData().getRecommendCurriculumList().size() > 0) {
                    Intent intentTaoXi = new Intent(ResourceBuyActivity.this, KengChengDetailActivity.class);
                    String packageId = "";
                    packageId = mBuyDetailBean.getData().getRecommendCurriculumList().get(0).getRecommendCurriculumId();
                    intentTaoXi.putExtra("curriculumId", packageId);
                    startActivity(intentTaoXi);
                }
                break;
        }
    }

    //多一个参数
    private void setIsDic(JSONObject json) {
        try {
            if (buyType == 1 || buyType == 2 || buyType == 8 || buyType == 11)
                json.put("isDiscount", isDiscount);
            if (buyType == 2) {//追加购买
                json.put("isAppendBuy",isAppendBuy);
                json.put("secondBatchId",secondBatchId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //微信购买
    private void wechatBuy(int type, String amount, int buyType, String Id) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
            json.put("amount", amount);
            json.put("buyType", buyType);
            json.put("buyId", Id);
            setIsDic(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).accountRecharge(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        WxPayBean wxPayBean = ParseUtil.parseBean(response, WxPayBean.class);
                        requestWChatPay(wxPayBean.getData());
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    //积分购买
    private void integralBuy(int type, String amount, int buyType, String Id) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
            json.put("amount", amount);
            json.put("buyType", buyType);
            json.put("buyId", Id);
            setIsDic(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).bonusPay(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, "购买成功");
                        setResult(1, new Intent());
                        finish();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    //学习币购买
    private void studycoinBuy(int type, String amount, int buyType, String Id) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
            json.put("amount", amount);
            json.put("buyType", buyType);
            json.put("buyId", Id);
            setIsDic(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("JSONException", json.toString() + "");
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).coinPay(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, "购买成功");
                        setResult(1, new Intent());
                        finish();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    //支付宝
    private void aliPayBuy(int type, String amount, int buyType, String Id) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
            json.put("amount", amount);
            json.put("buyType", buyType);
            json.put("buyId", Id);
            setIsDic(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getAlipayOrder(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        AlipayOrderInfo orderInfo = ParseUtil.parseBean(response, AlipayOrderInfo.class);
                        if (orderInfo != null)
                            payV2(orderInfo.getData().getOrderString());
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    /**
     * 支付宝支付业务
     *
     * @param
     */
    public void payV2(String orderInfos) {
        final String orderInfo = orderInfos;
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(ResourceBuyActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 调用微信支付
     *
     * @param dataBean
     */
    private void requestWChatPay(WxPayBean.DataBean dataBean) {
        PayReq req = new PayReq();
        req.appId = dataBean.getAppid();
        req.partnerId = dataBean.getPartnerid();
        req.prepayId = dataBean.getPrepayid();
        req.nonceStr = dataBean.getNoncestr();
        req.timeStamp = String.valueOf(dataBean.getTimestamp());
        req.packageValue = dataBean.getPackageX();
        req.sign = dataBean.getSign();
        req.extData = "fromBuy";
        api.sendReq(req);
        api.handleIntent(getIntent(), new IWXAPIEventHandler() {
            @Override
            public void onReq(BaseReq baseReq) {

            }

            @Override
            public void onResp(BaseResp baseResp) {

            }
        });
    }

    //答疑卷购买
    private void questionBuy(int type, String amount, int buyType, String Id) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
            json.put("amount", amount);
            json.put("buyType", buyType);
            json.put("buyId", Id);
            setIsDic(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).couponPay(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, "购买成功");
                        setResult(1, new Intent());
                        finish();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String data = "";
        data = intent.getStringExtra("extData");
        if (!TextUtils.isEmpty(data)) {
            if (data.equals("fromBuy")) {
                ToastUtils.showToastLONG(mContext, "购买成功");
                setResult(1, new Intent());
                finish();
            }
        }

    }
}
