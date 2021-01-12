package software.ecenter.study.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
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
import software.ecenter.study.Adapter.Rechargedapter;
import software.ecenter.study.R;
import software.ecenter.study.alipay.PayResult;
import software.ecenter.study.bean.AlipayOrderInfo;
import software.ecenter.study.bean.RechargeBean;
import software.ecenter.study.bean.WxBean.WxPayBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ConstantValue;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

import static software.ecenter.study.alipay.AlipayConstants.SDK_PAY_FLAG;

/***
 * dec 充值
 * */
public class RechargeCouponActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.list_account_detail)
    RecyclerView listAccountDetail;
    @BindView(R.id.recharge_weixin_img)
    ImageView rechargeWeixinImg;
    @BindView(R.id.weixin_check_img)
    ImageView weixinCheckImg;
    @BindView(R.id.btn_weixin)
    RelativeLayout btnWeixin;
    @BindView(R.id.recharge_zhifubao_img)
    ImageView rechargeZhifubaoImg;
    @BindView(R.id.zhifubao_check_img)
    ImageView zhifubaoCheckImg;
    @BindView(R.id.btn_zhifubao)
    RelativeLayout btnZhifubao;

    private TextView paypricenum;
    private Rechargedapter mAdapter;
    private List<RechargeBean> listData = new ArrayList<>();
    private IWXAPI api;
    private int type = 1;
    private String money;
    private int rechargeAmount;
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
        setContentView(R.layout.activity_recharge_coupon);
        ButterKnife.bind(this);
        paypricenum = findViewById(R.id.pay_price_num);
        api = WXAPIFactory.createWXAPI(this, ConstantValue.APP_ID);
        GridLayoutManager manager = new GridLayoutManager(mContext, 3);
        listAccountDetail.setLayoutManager(manager);
        initView();
    }

    public void initView() {
        for (int i = 1; i < 7; i++) {
            RechargeBean item = new RechargeBean();
            if (i < 5) {
                item.setRechargeBi((50 * i) + "元宝");
                item.setRechargeMoney((50 * i) + "元");
            } else if (i == 5) {
                item.setRechargeBi(50 * 6 + "元宝");
                item.setRechargeMoney(50 * 6 + "元");
            } else if (i == 6) {
                item.setRechargeBi(50 * 10 + "元宝");
                item.setRechargeMoney(50 * 10 + "元");
            }

            if (i == 1) {
                item.setCheck(true);
                money = "50";
                rechargeAmount = 50;
            } else {
                item.setCheck(false);
            }
            switch (i) {
                case 1:
                    item.setRechargeJiFen("（赠送200积分）");
                    break;
                case 2:
                    item.setRechargeJiFen("（赠送500积分）");
                    break;
                case 3:
                    item.setRechargeJiFen("（赠送800积分）");
                    break;
                case 4:
                    item.setRechargeJiFen("（赠送1100积分）");
                    break;
                case 5:
                    item.setRechargeJiFen("（赠送1800积分）");
                    break;
                case 6:
                    item.setRechargeJiFen("（赠送3500积分）");
                    break;
            }
            listData.add(item);
        }

        mAdapter = new Rechargedapter(mContext, listData);
        mAdapter.setItemClickListener(new Rechargedapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                CheckItem(position);
                if (position == 0) {
                    paypricenum.setText("￥50.0元");
                    money = "50";
                    rechargeAmount = 50;
                } else if (position == 1) {
                    paypricenum.setText("￥100.0元");
                    money = "100";
                    rechargeAmount = 100;
                } else if (position == 2) {
                    paypricenum.setText("￥150.0元");
                    money = "150";
                    rechargeAmount = 150;
                } else if (position == 3) {
                    paypricenum.setText("￥200.0元");
                    money = "200";
                    rechargeAmount = 200;
                } else if (position == 4) {
                    paypricenum.setText("￥300.0元");
                    money = "300";
                    rechargeAmount = 300;
                } else if (position == 5) {
                    paypricenum.setText("￥500.0元");
                    money = "500";
                    rechargeAmount = 500;
                }
            }
        });
        listAccountDetail.setAdapter(mAdapter);
        CheckItem(0);
    }


    public void accountRecharge(int type, String amount, int rechargeAmount) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
            json.put("amount", amount);
            json.put("rechargeAmount", rechargeAmount);
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

    public void CheckItem(int position) {
        for (RechargeBean item : listData) { //全部置为未选中
            item.setCheck(false);
        }
        listData.get(position).setCheck(listData.get(position).isCheck() ? false : true);
        mAdapter.notifyDataSetChanged();
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
        req.extData = "geology";
        api.sendReq(req);
    }


    @OnClick({R.id.btn_left_title, R.id.btn_weixin, R.id.btn_zhifubao, R.id.pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_weixin:
                zhifubaoCheckImg.setImageResource(R.drawable.xuan_r1);
                weixinCheckImg.setImageResource(R.drawable.xuan_r2);
                //TODO 对接支付
                type = 1;
                break;
            case R.id.btn_zhifubao:
                zhifubaoCheckImg.setImageResource(R.drawable.xuan_r2);
                weixinCheckImg.setImageResource(R.drawable.xuan_r1);
                //TODO 对接支付
                type = 2;
                break;
            case R.id.pay:
                if (type == 1) {
                    accountRecharge(type, money, rechargeAmount);
                } else {
                    aliPayBuy(1, money, rechargeAmount);
                }
                break;
        }
    }

    //支付宝
    private void aliPayBuy(int type, String amount, int rechargeAmount) {
        if (!showNetWaitLoding()) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
            json.put("amount", amount);
            json.put("rechargeAmount", rechargeAmount);
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
                PayTask alipay = new PayTask(RechargeCouponActivity.this);
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


}
