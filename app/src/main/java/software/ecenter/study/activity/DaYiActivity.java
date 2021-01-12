package software.ecenter.study.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.R;
import software.ecenter.study.fragment.TabTwoFragment;

/**
 * dec 答疑
 * Created by fxl on 2018/7/6.
 */

public class DaYiActivity extends BasePicActivity {
    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    TabTwoFragment tabTwoFragment;
    private int source = 0;
    private String resourceId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dayi);
        ButterKnife.bind(this);
        source = getIntent().getIntExtra("source", 0);
        resourceId = getIntent().getStringExtra("resourceId");
        tabTwoFragment = new TabTwoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("source", source);
        bundle.putString("resourceId", resourceId == null ? "" : resourceId);
        tabTwoFragment.setArguments(bundle);
        //必需继承FragmentActivity,嵌套fragment只需要这行代码
        getSupportFragmentManager().beginTransaction().replace(R.id.content, tabTwoFragment).commitAllowingStateLoss();
    }

    @OnClick({R.id.btn_left_title})
    public void onViewClicked(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_left_title:
                finish();
                break;

        }
    }

}
