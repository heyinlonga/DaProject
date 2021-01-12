package software.ecenter.study.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import software.ecenter.study.activity.AnswerBuyActivity;
import software.ecenter.study.activity.ResourceBuyActivity;
import software.ecenter.study.utils.ConstantValue;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * 创建时间: 2018/5/21.
 * 编写人: wxy
 * 功能描述:集成微信支付sdk的接口
 */

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, ConstantValue.APP_ID, true);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.errCode == 0) {        //成功
            Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
            boolean isPaySuccess = ((PayResp) baseResp).extData.equals("fromBuy");
            if (isPaySuccess) {
                Intent intent = new Intent(WXPayEntryActivity.this, ResourceBuyActivity.class);
                intent.putExtra("extData", "fromBuy");
                startActivity(intent);
                finish();
                return;
            }
            if (((PayResp) baseResp).extData.equals("fromAnswerBuy")) {
                Intent intent = new Intent(WXPayEntryActivity.this, AnswerBuyActivity.class);
                intent.putExtra("extData", "fromBuy");
                startActivity(intent);
                finish();
                return;
            }
            finish();
        } else if (baseResp.errCode == -1) { //失败
            Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
            finish();
        } else if (baseResp.errCode == -2) { //取消
            Toast.makeText(this, "取消支付", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
