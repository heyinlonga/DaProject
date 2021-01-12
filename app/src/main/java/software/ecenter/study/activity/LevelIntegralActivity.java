package software.ecenter.study.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import software.ecenter.study.Adapter.IntegralAdapter;
import software.ecenter.study.Adapter.LeverAndIntegralAdapter;
import software.ecenter.study.R;
import software.ecenter.study.bean.LevelAndIntegeralBean;
import software.ecenter.study.bean.MineBean.IntegralBean;
import software.ecenter.study.bean.MineBean.IntegralDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * dec 等级和积分/荣誉
 */
public class LevelIntegralActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.integral_cur_text)
    TextView integralCurText;
    @BindView(R.id.integral_all_text)
    TextView integralAllText;
    @BindView(R.id.btn_integral_detail)
    Button btnIntegralDetail;
    @BindView(R.id.btn_buy_youhuquan)
    Button btnBuyYouhuquan;
    @BindView(R.id.list_Integral)
    RecyclerView listIntegral;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;

    //    private List<IntegralBean> listData;
    private List<LevelAndIntegeralBean.DataBeanX.DataBean> listData;
    private LeverAndIntegralAdapter mAdapter;
    private String currentIntegral;
    private String totalIntegral;

    private LevelAndIntegeralBean mLevelAndIntegeralBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_integral);
        ButterKnife.bind(this);
        listData = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        listIntegral.setLayoutManager(manager);
        currentIntegral = getIntent().getStringExtra("currentIntegral");
        totalIntegral = getIntent().getStringExtra("totalIntegral");

    }

    public void initView() {

        if (mLevelAndIntegeralBean == null)
            return;


        listData = mLevelAndIntegeralBean.getData().getData();
        mAdapter = new LeverAndIntegralAdapter(mContext, listData);
        listIntegral.setAdapter(mAdapter);
        integralCurText.setText(currentIntegral);
        integralAllText.setText("累计" + totalIntegral + "分");

    }

    @Override
    protected void onResume() {
        super.onResume();
        getIntegralDetail(false);
    }

    public void getIntegralDetail(boolean isShow) {
        if (isShow) {
            if (!showNetWaitLoding()) {
                return;
            }
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "");
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getLevelAndIntegral(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        mLevelAndIntegeralBean = ParseUtil.parseBean(response, LevelAndIntegeralBean.class);
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

    @OnClick({R.id.btn_left_title, R.id.btn_integral_detail, R.id.btn_buy_youhuquan, R.id.btn_refresh_net})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_integral_detail:
                startActivity(IntegralDetailActivity.class);
                break;
            case R.id.btn_buy_youhuquan:
                startActivityForResult(ExchangeCouponsActivity.class, 110);
                break;
            case R.id.btn_refresh_net:
                getIntegralDetail(true);
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
