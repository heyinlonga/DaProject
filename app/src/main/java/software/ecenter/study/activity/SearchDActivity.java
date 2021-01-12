package software.ecenter.study.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;

import software.ecenter.study.R;
import software.ecenter.study.fragment.SearchFourFragment;
import software.ecenter.study.fragment.SearchOneFragment;
import software.ecenter.study.fragment.SearchThreeFragment;
import software.ecenter.study.fragment.SearchTwoFragment;
import software.ecenter.study.utils.EditUtils;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message
 * Create by Administrator
 * Create by 2019/1/25
 */
public class SearchDActivity extends BaseActivity {

    private EditText seach_edit;
    private TextView title_right;
    private SlidingTabLayout tab;
    private ViewPager vp;
    private String[] titles = {"全部", "图书", "资源", "答疑"};
    private String searchKeyword = "";
    private SearchOneFragment searchOneFragment;
    private SearchTwoFragment searchTwoFragment;
    private SearchThreeFragment searchThreeFragment;
    private SearchFourFragment searchFourFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchd);
        //返回
        findViewById(R.id.title_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolUtil.closeSoft(SearchDActivity.this);
                finish();
            }
        });
        seach_edit = findViewById(R.id.seach_edit);
        EditUtils.showEditNoEmoji(seach_edit);
        title_right = findViewById(R.id.title_right);
        tab = findViewById(R.id.tab);
        vp = findViewById(R.id.vp);
        SearchPager pager = new SearchPager(getSupportFragmentManager());
        vp.setAdapter(pager);
        vp.setOffscreenPageLimit(4);
        tab.setViewPager(vp);

        searchOneFragment = SearchOneFragment.newIntence(new Bundle());
        searchTwoFragment = SearchTwoFragment.newIntence(new Bundle());
        searchThreeFragment = SearchThreeFragment.newIntence(new Bundle());
        searchFourFragment = SearchFourFragment.newIntence(new Bundle());

        title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchKeyword = seach_edit.getText().toString();
                if (TextUtils.isEmpty(searchKeyword)||!ToolUtil.isKongGe(searchKeyword)) {
                    ToastUtils.showToastSHORT(mContext, "请输入搜索内容");
                    return;
                }

                setSearchKeyword(searchKeyword);
                tab.setVisibility(View.VISIBLE);
                vp.setVisibility(View.VISIBLE);
                int currentItem = vp.getCurrentItem();
                searchOneFragment.onSearch(currentItem == 0,searchKeyword);
                searchTwoFragment.onSearch(currentItem == 1,searchKeyword);
                searchThreeFragment.onSearch(currentItem == 2,searchKeyword);
                searchFourFragment.onSearch(currentItem == 3,searchKeyword);
            }
        });
        seach_edit.setFocusableInTouchMode(true);
        seach_edit.setFocusable(true);
        seach_edit.requestFocus();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ToolUtil.showSoft(SearchDActivity.this);
            }
        },500);
    }


    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }
    //设置当前fragment
    public void setCurrentTab(int i) {
        if (vp!=null){
            vp.setCurrentItem(i);
        }
    }

    public class SearchPager extends FragmentPagerAdapter {

        public SearchPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 1) {
                return searchTwoFragment;
            } else if (i == 2) {
                return searchThreeFragment;
            } else if (i == 3) {
                return searchFourFragment;
            }
            return searchOneFragment;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
