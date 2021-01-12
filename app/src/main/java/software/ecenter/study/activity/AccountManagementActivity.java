package software.ecenter.study.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.R;
import software.ecenter.study.bean.LevelAndIntegeralBean;
import software.ecenter.study.bean.MineBean.AccountManagementDetaiBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * dec 账户管理
 */
public class AccountManagementActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.account_cur_text)
    TextView accountCurText;
    @BindView(R.id.btn_account_detail)
    Button btnAccountDetail;
    @BindView(R.id.btn_buy_account)
    Button btnBuyAccount;
    @BindView(R.id.account_img)
    ImageView accountImg;
    @BindView(R.id.account_total_text)
    TextView accountTotalText;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.btn_integral_detail_mx)
    Button btnIntegralDetailmx;
    @BindView(R.id.btn_buy_youhuquan_dh)
    Button btnBuyYouhuquandh;
    @BindView(R.id.btn_buy_inter)
    Button btnBuyinter;
    @BindView(R.id.typeText)
    TextView typeText;
    @BindView(R.id.integral_cur_text)
    TextView integralCurText;
    @BindView(R.id.integral_all_text)
    TextView integralAllText;

    private AccountManagementDetaiBean mAccountManagementDetaiBean;
    private LevelAndIntegeralBean mLevelAndIntegeralBean;

    private String currentIntegral;
    private String totalIntegral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);
        ButterKnife.bind(this);
        currentIntegral = getIntent().getStringExtra("currentIntegral");
        totalIntegral = getIntent().getStringExtra("totalIntegral");
    }


    @Override
    protected void onResume() {
        super.onResume();
        userAccountManagement();
//        getIntegralDetail();
    }

    /**
     * 获取账户 余额、积分、优惠卷信息
     */
    public void userAccountManagement() {
//        if (!showNetWaitLoding()) {
//            return;
//        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).userAccountManagement(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        mAccountManagementDetaiBean = ParseUtil.parseBean(response, AccountManagementDetaiBean.class);
                        btnRefreshNet.setVisibility(View.GONE);
                        initView();

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });

    }

    public void initView() {
        if (mAccountManagementDetaiBean == null||mAccountManagementDetaiBean.getData() == null) {
            return;
        }
        accountCurText.setText(mAccountManagementDetaiBean.getData().getBalance());
        typeText.setText("答疑劵");
        accountTotalText.setText(mAccountManagementDetaiBean.getData().getCouponNum());

        integralCurText.setText(mAccountManagementDetaiBean.getData().getCurrentIntegral());
        integralAllText.setText("累积:" + mAccountManagementDetaiBean.getData().getTotalIntegral()+"分");

    }


    @OnClick({R.id.btn_left_title, R.id.btn_account_detail, R.id.btn_buy_account,
            R.id.btn_integral_detail_mx, R.id.btn_buy_youhuquan_dh, R.id.btn_refresh_net, R.id.btn_buy_inter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_account_detail: //查看账户明细
                startActivity(AccountManagementDetailActivity.class);
                break;
            case R.id.btn_buy_account: //充值
                startActivity(RechargeCouponActivity.class);
            case R.id.btn_refresh_net:
                userAccountManagement();
                break;
            case R.id.btn_integral_detail_mx://积分明细
                startActivity(IntegralDetailActivity.class);
                break;
            case R.id.btn_buy_inter://积分规则
                startActivity(LevelIntegralActivity.class);
                break;
            case R.id.btn_buy_youhuquan_dh: //兑换优惠券
//                startActivityForResult(ExchangeCouponsActivity.class, 110);
                startActivityForResult(new Intent(this, ExchangeCouponsActivity.class).putExtra("currentIntegral", currentIntegral), 110);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 110) {
            int integral = data.getIntExtra("level", 0);
            int a = 0;
            a = (Integer.parseInt(currentIntegral)) - integral;
            currentIntegral = a + "";
        }
    }

}
