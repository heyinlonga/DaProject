package software.ecenter.study.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import software.ecenter.study.R;
import software.ecenter.study.utils.AccountUtil;

/**
 * 轮播图
 */
public class WelcomeGuidActivity extends BaseActivity {
    @BindView(R.id.in_viewpager)
    ViewPager inViewPager;
    LinearLayout llView;
    // 记录当前选中位置
    private int currentIndex;

    private static final int[] pics = {R.layout.guid_view1,
            R.layout.guid_view2, R.layout.guid_view3};
    private List<View> viewList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        LayoutInflater lf = getLayoutInflater().from(this);
        if (viewList == null) viewList = new ArrayList<>();
        for (int viewId : pics) {
            viewList.add(lf.inflate(viewId, null));
        }
        llView = viewList.get(viewList.size() - 1).findViewById(R.id.ll_guid_view3);
        llView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(LogingActivity.class);
                AccountUtil.saveFirstOpenApp(mContext, false);
                finish();
            }
        });
        inViewPager.setAdapter(new ViewPagerAdapter(viewList));
        inViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    class ViewPagerAdapter extends PagerAdapter {
        List<View> views;


        public ViewPagerAdapter(List<View> mViewList) {
            this.views = mViewList;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }
}
