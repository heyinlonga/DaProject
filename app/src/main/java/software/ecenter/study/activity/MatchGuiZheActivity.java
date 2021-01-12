package software.ecenter.study.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.Adapter.GuiZheAdapter;
import software.ecenter.study.R;
import software.ecenter.study.bean.GuiZheBean;
import software.ecenter.study.bean.MatchDetail;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * Message 比赛规则
 * Create by Administrator
 * Create by 2019/10/28
 */
public class MatchGuiZheActivity extends BaseActivity{

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;
    private GuiZheAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchguizhe);
        ButterKnife.bind(this);
        rv_list.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new GuiZheAdapter(mContext);
        rv_list.setAdapter(adapter);
        getData();
    }
    //获取数据
    private void getData() {
        if (!showNetWaitLoding()) {
            return;
        }
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getMatchGuiZhe())
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        GuiZheBean bean = ParseUtil.parseBean(response, GuiZheBean.class);
                        if (bean!=null){
                            List<GuiZheBean.DataBean> data = bean.getData();
                            adapter.setData(data);
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }
    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
