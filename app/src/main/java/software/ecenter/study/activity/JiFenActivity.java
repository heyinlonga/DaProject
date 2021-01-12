package software.ecenter.study.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.R;
import software.ecenter.study.bean.MineBean.MyCollectionDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

public class JiFenActivity extends BaseActivity implements View.OnClickListener {

    private EditText ed_contetn;
    private LinearLayout ll_message;
    private TextView tv_message;
    private TextView tv_config;
    private ImageView btn_left_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ji_fen);

        ed_contetn = findViewById(R.id.ed_contetn);
        ll_message = findViewById(R.id.ll_message);
        tv_message = findViewById(R.id.tv_message);
        tv_config = findViewById(R.id.tv_config);
        btn_left_title = findViewById(R.id.btn_left_title);

        btn_left_title.setOnClickListener(this);
        tv_config.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left_title:
                finish();
                break;
            case R.id.tv_config:
                config();
                break;
        }
    }

    private void config() {
        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        JSONObject json = new JSONObject();
        try {
            json.put("redeemCode", ed_contetn.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getIntegral(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        Log.e("---", response);
                        ll_message.setVisibility(View.GONE);
                        ToastUtils.showToastLONG(mContext, "兑换成功");
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ll_message.setVisibility(View.VISIBLE);
                        tv_message.setText(msg);
                    }

                });
    }
}
