package software.ecenter.study.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.R;

@Deprecated //没用
public class MainActivity extends BaseActivity {

    @BindView(R.id.button)
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    public void onViewClicked() {

        Intent intent = new Intent(this, FileDisplayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("path", "http://www.hrssgz.gov.cn/bgxz/sydwrybgxz/201101/P020110110748901718161.doc");
        //bundle.putSerializable("path", "http://pdf.dfcfw.com/pdf/H2_AN201804231128160631_1.pdf");
        //bundle.putSerializable("path", "http://img.mp.itc.cn/upload/20161023/6540928566c848569137ae9bc3bdad45.gif");

        //bundle.putSerializable("path", "http://img.zcool.cn/community/0142135541fe180000019ae9b8cf86.jpg@1280w_1l_2o_100sh.png");
        intent.putExtras(bundle);
        startActivity(intent);
 /*       Map<String, String> paramMap = new HashMap<String, String>();
        new RetroFactory(this, RetroFactory.getRetroFactory(this).test(paramMap))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showToastSHORT(MainActivity.this,"aaaa");
                        Log.d(TAG,response);
                    }

                    @Override
                    public void onFail(int type, String msg) {

                    }
                });*/
    }
}
