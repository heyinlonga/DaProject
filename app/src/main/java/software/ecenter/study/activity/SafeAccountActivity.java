package software.ecenter.study.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import software.ecenter.study.R;

/**
 * Message 账户安全
 * Create by Administrator
 * Create by 2020/1/6
 */
public class SafeAccountActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safeaccount);
        findViewById(R.id.tv_zhuxiao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,AccountCancleActivity.class));
            }
        });
        findViewById(R.id.btn_left_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
