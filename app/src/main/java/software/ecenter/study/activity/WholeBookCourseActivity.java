package software.ecenter.study.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.Adapter.WholePageAdapter;
import software.ecenter.study.R;
import software.ecenter.study.View.ExpandableTextView;
import software.ecenter.study.bean.WholeBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ToastUtils;

/**
 * Message整本书阅读  视频讲座 目录列表
 * Create by Administrator
 * Create by 2019/8/15
 */
public class WholeBookCourseActivity extends BaseActivity implements View.OnClickListener {
    private ExpandableTextView expandableTextView;
    private LinearLayout ll_oldPric;
    private ImageView iv_back;
    private TextView tv_title;
    private ImageView iv_icon;
    private TextView tv_name;
    private TextView tv_gold;
    private TextView tv_pay;
    private SlidingTabLayout st_tab;
    private ViewPager vp_pager;
    private boolean isBuy;
    private String id;
    private int type;//0其他1从章节列表跳进来的

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholebookcourse);
        id = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type", 0);
        initView();
        if (type == 1) {
            getData();
        } else {
            getChapter();
        }
    }

    //获取章节数据
    private void getChapter() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getlectureChapter(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        showData(response);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    //获取课程数据
    private void getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getlectureCurse(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        showData(response);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });

    }

    //设置数据
    private void showData(String response) {
        WholeBean bean = new Gson().fromJson(response, WholeBean.class);
        if (bean != null) {
            WholeBean.DataBean data = bean.getData();
            if (data != null) {
                if (Util.isOnMainThread()) {
                    if (!isFinishing())
                        Glide.with(mContext).load(data.getImgUrl()).error(R.drawable.morenshu).into(iv_icon);
                }
                tv_title.setText(data.getName());
                tv_name.setText(data.getName());
                tv_gold.setText(data.getCoinPrice() + "元宝");
                isBuy = data.isIsBuy();
                showBuyLay();
                expandableTextView.setText(data.getDescription());

                vp_pager.setAdapter(new WholePageAdapter(getSupportFragmentManager(), bean));
                st_tab.setViewPager(vp_pager);
                vp_pager.setOffscreenPageLimit(2);
                st_tab.getTitleView(0).getPaint().setFakeBoldText(true);
                st_tab.getTitleView(1).getPaint().setFakeBoldText(true);
            }
        }
    }
    //购买按钮
    private void showBuyLay() {
        if (isBuy){
            tv_pay.setText("已购买");
            tv_pay.setEnabled(false);
            tv_pay.setBackground(getResources().getDrawable(R.drawable.btn_sign_shape_circle_unclick));
            ll_oldPric.setVisibility(View.INVISIBLE);
        }
    }
    //初始化控件
    private void initView() {
        ll_oldPric = findViewById(R.id.ll_oldPric);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        iv_icon = findViewById(R.id.iv_icon);
        tv_name = findViewById(R.id.tv_name);
        tv_gold = findViewById(R.id.tv_gold);
        tv_pay = findViewById(R.id.tv_pay);
        expandableTextView = findViewById(R.id.expandable_text);

        st_tab = findViewById(R.id.st_tab);
        vp_pager = findViewById(R.id.vp_pager);
        iv_back.setOnClickListener(this);
        tv_pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.tv_pay://购买
                Intent intent = new Intent(mContext, ResourceBuyActivity.class);
                intent.putExtra("Id", String.valueOf(id));
                if (type == 1) {//章节
                    intent.putExtra("buyType", "9"); //9讲座章节
                } else if (type == 0) {//讲座
                    intent.putExtra("buyType", "8"); //8讲座
                }
                startActivityForResult(intent, 1);
                break;
        }
    }

    //购买成功
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == 1 && resultCode == 1) {
            isBuy = true;
            showBuyLay();
        }
    }
}
