package software.ecenter.study.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.R;
import software.ecenter.study.View.CapterView.CaptureView;
import software.ecenter.study.bean.MineBean.SignDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * dec 签到
 */
public class SignActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.captureView)
    CaptureView captureView;
    @BindView(R.id.btn_sign)
    Button btnSign;
    @BindView(R.id.has_sign_line)
    LinearLayout hasSignLine;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.has_sign_line_text)
    TextView hasSignLineText;

    private SignDetailBean mSignDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        ButterKnife.bind(this);

        Calendar time = Calendar.getInstance();
        int select_year = time.get(Calendar.YEAR);
        int select_month = time.get(Calendar.MONTH)+1;
        captureView.setTitle(select_year + "年" + select_month + "月签到日历");
        //假数据
        captureView.install();
        getSignInfo();
    }

    public void initView() {
        if (mSignDetailBean == null) {
            return;
        }
        if (mSignDetailBean.getData().getIsSign() == 1) { //已签到
            btnSign.setVisibility(View.GONE);
            hasSignLineText.setText(mSignDetailBean.getData().getSignIntegral()+"积分");
            hasSignLine.setVisibility(View.VISIBLE);
        } else {
            btnSign.setVisibility(View.VISIBLE);
            hasSignLine.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(mSignDetailBean.getData().getSignList())) {
            List<String> listdata = ToolUtil.StringToArray(mSignDetailBean.getData().getSignList(), ",");

            try {
                captureView.refresh(listdata);
            } catch (ParseException e) {
                e.printStackTrace();
                ToastUtils.showToastSHORT(mContext, "日期错误");
            }
        }

    }

    //获取签到信息
    public void getSignInfo() {
        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getSignInfo(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        mSignDetailBean = ParseUtil.parseBean(response, SignDetailBean.class);
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

    //进行签到
    public void userSign() {
        if (!showNetWaitLoding()) {
            return;
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).userSign(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        mSignDetailBean = ParseUtil.parseBean(response, SignDetailBean.class);
                        initView();
                        ToastUtils.showToastSHORT(mContext,"签到成功");

                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });

    }


    @OnClick({R.id.btn_left_title, R.id.btn_sign, R.id.btn_refresh_net})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                onBackPressed();
                break;
            case R.id.btn_sign:
                userSign();
                break;
            case R.id.btn_refresh_net:
                getSignInfo();
                break;
        }
    }

}
