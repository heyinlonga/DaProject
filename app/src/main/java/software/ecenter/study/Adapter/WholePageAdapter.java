package software.ecenter.study.Adapter;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.io.Serializable;

import software.ecenter.study.bean.WholeBean;
import software.ecenter.study.fragment.TabFourFragment;
import software.ecenter.study.fragment.WholeCourseFragment;
import software.ecenter.study.fragment.WholeDesFragment;

/**
 * Message
 * Create by Administrator
 * Create by 2019/8/15
 */
public class WholePageAdapter extends FragmentPagerAdapter {
    String[] titles = { "课程表","简介"};
    private WholeBean bean;

    public WholePageAdapter(FragmentManager fm, WholeBean bean) {
        super(fm);
        this.bean = bean;
    }

    @Override
    public Fragment getItem(int i) {
        Bundle bundle = new Bundle();
        if (i == 1) {
            if (bean != null) {
                WholeBean.DataBean data = bean.getData();
                if (data != null) {
                    String detail = data.getDetail();
                    bundle.putString("text", detail);
                }
            }
            return WholeDesFragment.newIntence(bundle);
        }
        if (bean != null) {
            WholeBean.DataBean data = bean.getData();
            if (data != null) {
                bundle.putSerializable("list", (Serializable) data.getResourceList());
            }
        }

        return WholeCourseFragment.newIntence(bundle);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
