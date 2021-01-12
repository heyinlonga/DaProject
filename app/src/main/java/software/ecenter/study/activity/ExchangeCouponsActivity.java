package software.ecenter.study.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.R;
import software.ecenter.study.bean.MineBean.ExchangeCouponDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

import static software.ecenter.study.net.RetroFactory.RESULT_MESSAGE;

/**
 * dec 兑换优惠券
 */
public class ExchangeCouponsActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.coupons_edit)
    EditText couponsEdit;
    @BindView(R.id.coupons_need_integral)
    TextView couponsNeedIntegral;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.btn_submission)
    Button btnSubmission;

    private ExchangeCouponDetailBean mExchangeCouponDetailBean;
    private int currentIntegral;
    private int proportion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_coupons);
        ButterKnife.bind(this);
        integralExchangeRules();
        currentIntegral = ToolUtil.getIntValue(getIntent().getStringExtra("currentIntegral"));
    }

    public void integralExchangeRules() {
        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).integralExchangeRules(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.GONE);
                        mExchangeCouponDetailBean = ParseUtil.parseBean(response, ExchangeCouponDetailBean.class);
                        initView();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.INVISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });

    }


    public void initView() {
        if (mExchangeCouponDetailBean == null) {
            return;
        }
        proportion = mExchangeCouponDetailBean.getData().getProportion();
        couponsEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().startsWith("0")) {
                    couponsEdit.setText("");
                    return;
                }
                if (TextUtils.isEmpty(s.toString())) {
                    couponsNeedIntegral.setText("");
                    return;
                }
                if (ToolUtil.getIntValue(s.toString()) > 1000) {
                    ToastUtils.showToastSHORT(mContext,"单次最多兑换1000张");
                }
                if (proportion > 0) {
                    couponsNeedIntegral.setText(String.valueOf(ToolUtil.getIntValue(s.toString()) * proportion));
                }
            }
        });
    }

    @OnClick({R.id.btn_left_title, R.id.btn_refresh_net, R.id.btn_submission})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_refresh_net:
                integralExchangeRules();
                break;
            case R.id.btn_submission:
                String s = couponsEdit.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    ToastUtils.showToastSHORT(mContext, "请输入兑换的数量");
                } else {
                    if (ToolUtil.getIntValue(s) > 1000) {
                            ToastUtils.showToastSHORT(mContext,"单次最多兑换1000张");
                    } else {
                        if (ToolUtil.getIntValue(s) * proportion > currentIntegral) {
                            ToastUtils.showToastSHORT(mContext, "你没有那么多的积分");
                        } else {
                            integralExchange();
                        }
                    }
                }
                break;
        }
    }

    public void integralExchange() {
        if (!showNetWaitLoding()) {
            return;
        }

        final JSONObject json = new JSONObject();
        try {
            json.put("exchangeNum", Integer.parseInt(couponsEdit.getText().toString()));
            json.put("couponType", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).integralExchange(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, "已成功兑换" + couponsEdit.getText().toString() + "张答疑券");
                        Intent intent = getIntent();
                        intent.putExtra("level", Integer.parseInt(couponsNeedIntegral.getText().toString().trim()));
                        setResult(Activity.RESULT_OK, intent);
                        finish();

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        JSONObject jsonObject = null;
                        String rescode = "";
                        try {
                            jsonObject = new JSONObject(msg);
                            rescode = jsonObject.getString(RESULT_MESSAGE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtils.showToastSHORT(mContext, msg);
                        }

                        ToastUtils.showToastLONG(mContext, rescode);
                    }

                });


    }


}
