package software.ecenter.study.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import software.ecenter.study.R;
import software.ecenter.study.View.CaptureManager;
import software.ecenter.study.View.DecoratedBarcodeView;
import software.ecenter.study.utils.ToastUtils;

/**
 * Message: ZXing扫描
 * Author: 陈龙
 * Date: 2019/3/18 13:32
 */
public class ScanActivity extends BaseActivity {
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        initView(savedInstanceState);
    }
    protected void initView(Bundle savedInstanceState) {
        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.setResultCallBack(new CaptureManager.ResultCallBack() {
            @Override
            public void callBack(int requestCode, int resultCode, Intent intent) {
                //二维码扫描结构
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                if (scanResult != null) {
                    String result = scanResult.getContents();
                    goToActivtity(result);
                } else {
                    capture.onResume();
                    capture.decode();
                }
            }
        });
        capture.decode();
        findViewById(R.id.btn_left_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public static final String CODED_CONTENT = "scan_coded_content";
    //扫码后跳转
    public void goToActivtity(String text) {
        Log.d("scanZxing", "扫描后的数据：" + text);
        Intent intent = getIntent();
        intent.putExtra(CODED_CONTENT, text);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
        capture.decode();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

}
