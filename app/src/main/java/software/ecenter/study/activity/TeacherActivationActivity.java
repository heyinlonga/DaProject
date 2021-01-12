package software.ecenter.study.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;


import androidx.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.R;
import software.ecenter.study.bean.TeacherBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.PermissionUtils;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

/**
 * auther: zzm
 * Date: 2019/8/12
 * Description:  教师资源激活
 */
public class TeacherActivationActivity extends BaseActivity {
    @BindView(R.id.edCode)
    EditText edCode;
    private int REQUEST_CODE_SCAN = 111;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_activation);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_left_title, R.id.tvScan, R.id.tvSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title:
                finish();
                break;
            case R.id.tvScan:
                PermissionUtils.newIntence().requestPerssion(this, ToolUtil.PERSSION_PHOTO, 101, new PermissionUtils.IPermission() {
                    @Override
                    public void success(int code) {
                        StartZxing();
                    }
                });
                break;
            case R.id.tvSubmit:
                String code = edCode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    ToastUtils.showToastSHORT(mContext, "请输入激活码");
                    return;
                }
                submit(code);
                break;
        }
    }

    //扫一扫
    public void StartZxing() {
        /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
         * 也可以不传这个参数
         * 不传的话  默认都为默认不震动  其他都为true
         * */
//        ZxingConfig config = new ZxingConfig();
//        config.setShowbottomLayout(false);//底部布局（包括闪光灯和相册）
//        config.setPlayBeep(true);//是否播放提示音
//        config.setShake(false);//是否震动
//        config.setShowAlbum(false);//是否显示相册
//        config.setShowFlashLight(false);//是否显示闪光灯
//
//
//        //如果不传 ZxingConfig的话，两行代码就能搞定了
//        Intent intent = new Intent(mContext, CaptureActivity.class);
//        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
//        startActivityForResult(intent, REQUEST_CODE_SCAN);

        //这里的上下文是activity  所以回调的应该在 fragment所在的activity的onActivityResult
        IntentIntegrator integrator = new IntentIntegrator(this);
        // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setCaptureActivity(ScanActivity.class); //设置打开摄像头的Activity
        integrator.setPrompt("将二维码放入框内将自动扫描"); //底部的提示文字，设为""可以置空
        integrator.setCameraId(0); //前置或者后置摄像头
        integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    private void submit(String code) {
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).teacherActive(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, "激活成功，可在PC端免费下载资源");
                        setResult(Activity.RESULT_OK);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(ScanActivity.CODED_CONTENT);
                if (content.contains("fw=")) {
                    // 扫描二维码/条码回传
                    String code = content.substring(content.lastIndexOf("fw=") + 3);
                    edCode.setText(code);
                } else {
                    ToastUtils.showToastSHORT(mContext, "您扫描的二维码不正确");
                }
            }
        }
    }
}
