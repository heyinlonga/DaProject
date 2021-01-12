package software.ecenter.study.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.Adapter.ErrorTiAdapter;
import software.ecenter.study.Adapter.ErrorTiBean;
import software.ecenter.study.R;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * Message 错题解析
 * Create by Administrator
 * Create by 2019/10/31
 */
public class ErrorTiActivity extends BaseActivity{
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_errorNum)
    TextView tv_errorNum;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    private String id;
    private ErrorTiAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_errorti);
        ButterKnife.bind(this);
        rv_list.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new ErrorTiAdapter(mContext);
        rv_list.setAdapter(adapter);
        id = getIntent().getStringExtra("id");
        getData();
    }
    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
    //错题
    private void getData() {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("curpage", curpage);
            json.put("pageNum", 1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getWrongQuestionList(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ErrorTiBean bean = ParseUtil.parseBean(response, ErrorTiBean.class);
                        if (bean != null) {
                            ErrorTiBean.DataBean data = bean.getData();
                            if (data != null) {
                                tv_errorNum.setText(data.getTotal()+"");
                                adapter.setData(data.getContent());
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
}
