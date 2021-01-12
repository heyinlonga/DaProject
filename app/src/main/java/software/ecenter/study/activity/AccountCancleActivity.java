package software.ecenter.study.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import software.ecenter.study.Interface.OnItemListener;
import software.ecenter.study.R;
import software.ecenter.study.bean.SafeBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message 注销账户
 * Create by Administrator
 * Create by 2020/1/6
 */
public class AccountCancleActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private ImageView btn_left_title;
    private CheckBox cb_view;
    private TextView tv_help;
    private TextView tv_zhu;
    private boolean isState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountcancle);
        btn_left_title = findViewById(R.id.btn_left_title);
        tv_help = findViewById(R.id.tv_help);
        cb_view = findViewById(R.id.cb_view);
        tv_zhu = findViewById(R.id.tv_zhu);
        cb_view.setOnCheckedChangeListener(this);
        tv_help.setOnClickListener(this);
        tv_zhu.setOnClickListener(this);
        btn_left_title.setOnClickListener(this);
        getData();
    }

    //获取数据
    private void getData() {
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getZXState(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        SafeBean bean = ParseUtil.parseBean(response, SafeBean.class);
                        if (bean != null) {
                            SafeBean.DataBean data = bean.getData();
                            if (data != null) {
                                isState = data.getState() == 1;
                                if (data.getState() == 1) {
                                    cb_view.setClickable(false);
                                    cb_view.setChecked(true);
                                    tv_zhu.setText("已申请注销");
                                }
                            }
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isState) {
            tv_zhu.setSelected(isChecked);
            tv_zhu.setTextColor(isChecked ? getResources().getColor(R.color.white) : getResources().getColor(R.color.textHoldColor));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left_title:
                finish();
                break;
            case R.id.tv_help://反馈
                startActivity(HelpUpDataActivity.class);
                break;
            case R.id.tv_zhu://注销
                if (tv_zhu.isSelected()) {
                    getZhuXiao();
                }
                break;
        }
    }

    //注销
    private void getZhuXiao() {
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getZhuXiao(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        tv_zhu.setSelected(false);
                        tv_zhu.setText("已申请注销");
                        cb_view.setClickable(false);
                        ToolUtil.showZXDialog(AccountCancleActivity.this, new OnItemListener());
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }
}
