package software.ecenter.study.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.R;
import software.ecenter.study.bean.TeacherBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * 教师空间
 */
public class TeacherSpaceActivity extends BaseActivity {

    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.tvTeacherActivation)
    TextView tvTeacherActivation;
    @BindView(R.id.tv_look)
    TextView tvLook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_space);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getTeacherIsActivated(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        btnRefreshNet.setVisibility(View.GONE);
                        dismissNetWaitLoging();
                        if (!TextUtils.isEmpty(response)) {
                            TeacherBean teacherBean = ParseUtil.parseBean(response, TeacherBean.class);
                            boolean isActivated = teacherBean.getData().isActive();
                            tvTeacherActivation.setEnabled(!isActivated);
                            tvTeacherActivation.setText(isActivated ? "教师资源已激活" : "教师资源激活");
                            tvLook.setVisibility(isActivated ? View.VISIBLE : View.GONE);
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });

    }

    @OnClick({R.id.btn_left_title, R.id.tvTeacher, R.id.tvTeacherActivation, R.id.btn_refresh_net, R.id.tv_look})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                finish();
                break;
            case R.id.tvTeacher:
                startActivity(new Intent(mContext, TeacherActivity.class));
                break;
            case R.id.tvTeacherActivation:
                startActivityForResult(new Intent(mContext, TeacherActivationActivity.class), 1000);
                break;
            case R.id.btn_refresh_net:
                initData();
                break;
            case R.id.tv_look://教师资源列表
                startActivity(new Intent(mContext, TeacherResourcesActivity.class));
                break;

        }
    }

}
