package software.ecenter.study.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import software.ecenter.study.Adapter.ImageAdapter;
import software.ecenter.study.Adapter.PayTypeAdapter;
import software.ecenter.study.R;
import software.ecenter.study.alipay.PayResult;
import software.ecenter.study.bean.AlipayOrderInfo;
import software.ecenter.study.bean.HomeBean.payTypeBean;
import software.ecenter.study.bean.ImageBean;
import software.ecenter.study.bean.QuestionBean.QuestionPayDetailBean;
import software.ecenter.study.bean.WxBean.WxPayBean;
import software.ecenter.study.fragment.TabTwoFragment;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ConstantValue;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

import static software.ecenter.study.alipay.AlipayConstants.SDK_PAY_FLAG;

/**
 * dec 答疑支付
 */
public class AnswerBuyActivity extends BasePicActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.question_miaoshu)
    TextView questionMiaoshu;
    @BindView(R.id.tip_ly)
    ImageButton tipLy;
    @BindView(R.id.btn_lv_yin)
    RelativeLayout btnLvYin;
    @BindView(R.id.list_pay)
    RecyclerView listPay;
    @BindView(R.id.pay_price_tip)
    TextView payPriceTip;
    @BindView(R.id.pay_price)
    TextView payPrice;
    @BindView(R.id.btn_buy)
    Button btnBuy;
    @BindView(R.id.list_image)
    RecyclerView listImage;
    @BindView(R.id.lv_text)
    TextView lvText;


    private IWXAPI api;
    private PayTypeAdapter payTypeAdapter;
    private List<payTypeBean> listData = new ArrayList<>();

    private QuestionPayDetailBean mQuestionPayDetailBean;
    private String describe;
    private String clazz = "";

    private int buyType;
    private int positio;
    private String amount;
    private String Id;
    private int type;
    Intent intent = null;
    private String payType = "";//支付类型
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
                    // 判断resultStatus 为9000则代表提交成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实提交成功，需要依赖服务端的异步通知。
                        Toast.makeText(mContext, "提交成功", Toast.LENGTH_SHORT).show();
                        dismissNetWaitLoging();
//                        if (type == 3)
                        setResult(111, intent);
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(mContext, "提交失败", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_answer_buy);
        ButterKnife.bind(this);
        api = WXAPIFactory.createWXAPI(this, ConstantValue.APP_ID);

        intent = getIntent();
        if (intent != null) {
            mQuestionPayDetailBean = (QuestionPayDetailBean) intent.getSerializableExtra("mQuestionPayDetailBean");
            describe = intent.getStringExtra("describe");
            clazz = intent.getStringExtra("class");
            buyType = intent.getIntExtra("buyType", 0);
            type = intent.getIntExtra("type", 0);
            Id = getIntent().getStringExtra("buyId");
            tempRecorderPath = intent.getStringExtra("audioPath");
            recordingSecond = intent.getIntExtra("recordingSecond", 0);
            if (!TextUtils.isEmpty(tempRecorderPath)) {
                hasRecording = true;
            }

            listImageData = new ArrayList<>();
            if (TextUtils.isEmpty(clazz) && TabTwoFragment.listImageData != null)
                for (ImageBean item : TabTwoFragment.listImageData) {
                    if (!item.isAddImage()) {
                        ImageBean imge = new ImageBean();
                        imge.setThumBitmap(item.getThumBitmap());
                        imge.setTargetPicPath(item.getTargetPicPath());
                        imge.setAddImage(false);
                        listImageData.add(imge);
                    }
                }
            if (!TextUtils.isEmpty(clazz) && QuestionCloselyActivity.imageBeanList != null) {
                for (ImageBean item : QuestionCloselyActivity.imageBeanList) {
                    if (!item.isAddImage()) {
                        ImageBean imge = new ImageBean();
                        imge.setThumBitmap(item.getThumBitmap());
                        imge.setAddImage(false);
                        imge.setTargetPicPath(item.getTargetPicPath());
                        listImageData.add(imge);
                    }
                }
            }
        }

        initView();
    }

    public void initView() {
        //添加图片
        if (!TextUtils.isEmpty(describe)) {
            questionMiaoshu.setText(describe);
        } else {
            questionMiaoshu.setText("没有问题描述");
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        listImage.setLayoutManager(gridLayoutManager);
        imageAdapter = new ImageAdapter(mContext, listImageData);
        imageAdapter.setCannotDelete(true);
        listImage.setAdapter(imageAdapter);

        if (hasRecording) {
            lvText.setText("点击播放(" + recordingSecond + " s)");
            btnLvYin.setVisibility(View.VISIBLE);

        }

        listData = mQuestionPayDetailBean.getData().getPayTypeData();
//        LinearLayoutManager manager = new LinearLayoutManager(mContext);
//        manager.setOrientation(LinearLayoutManager.VERTICAL);
        listPay.setLayoutManager(new LinearLayoutManager(mContext){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        payTypeAdapter = new PayTypeAdapter(mContext, listData,false);
        payTypeAdapter.setItemClickListener(new PayTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                CheckItem(position);

            }
        });
        listPay.setAdapter(payTypeAdapter);
        CheckItem(0);
    }

    public void CheckItem(int position) {
        for (payTypeBean item : listData) { //全部置为未选中
            item.setCheck(false);
        }
        listData.get(position).setCheck(listData.get(position).isCheck() ? false : true);
        positio = position;

        payType = listData.get(position).getPayType();

        //底部动态变化
        if ("元".equals(listData.get(position).getPayUnit())) {
            payPrice.setText("￥" + listData.get(position).getPayNum() + listData.get(position).getPayUnit());
        } else {
            payPrice.setText(listData.get(position).getPayNum() + listData.get(position).getPayUnit());
        }
        amount = listData.get(position).getPayNum();
        payTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public void curPlaySecond(int second) {
        lvText.setText("正在播放(" + second + " s)");
    }

    @Override
    public void playFinished() {
        lvText.setText("点击播放(" + recordingSecond + " s)");
    }


    @OnClick({R.id.btn_left_title, R.id.btn_lv_yin, R.id.btn_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                finish();
                break;
            case R.id.btn_lv_yin:
                if (hasRecording) {
                    if (!isPlaying) {
                        startplayRecording();//播放
                    } else {
                        stopPlayRecording();//停止播放
                    }
                }
                break;
            case R.id.btn_buy:
                //TODO 支付
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
        }
    }

    //微信获取
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

    //积分获取
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).bonusPay(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, "提交成功");
                        setResult(111, intent);
                        finish();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    //学习币获取
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).coinPay(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, "提交成功");
                        setResult(111, intent);
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
                PayTask alipay = new PayTask(AnswerBuyActivity.this);
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

    //答疑卷获取
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).couponPay(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, "提交成功");
                        setResult(111, intent);
                        finish();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
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
        req.extData = "fromAnswerBuy";
        api.sendReq(req);
    }

    @Override
    protected void onNewIntent(Intent intents) {
        super.onNewIntent(intents);
        String data = "";
        data = intents.getStringExtra("extData");
        if (!TextUtils.isEmpty(data)) {
            if (data.equals("fromBuy")) {

                if (type == 3) {
                    if (intent != null)
                        setResult(111, intent);
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
