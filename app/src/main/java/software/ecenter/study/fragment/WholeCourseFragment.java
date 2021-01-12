package software.ecenter.study.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import software.ecenter.study.Adapter.WholeCourseAdapter;
import software.ecenter.study.Interface.OnItemListener;
import software.ecenter.study.R;
import software.ecenter.study.activity.WholeCourseDetailActivity;
import software.ecenter.study.bean.CourseBean;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message 视频讲座课程列表
 * Create by Administrator
 * Create by 2019/8/15
 */
public class WholeCourseFragment extends BaseFragment {

    private LinearLayout ll_nor;
    private RecyclerView rv_list;
    private WholeCourseAdapter adapter;

    public static WholeCourseFragment newIntence(Bundle bundle) {
        WholeCourseFragment fragment = new WholeCourseFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_wholecourse, container, false);
        ll_nor = inflate.findViewById(R.id.ll_nor);
        rv_list = inflate.findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new WholeCourseAdapter(getActivity(), new OnItemListener() {
            @Override
            public void onItemClick(int poc) {
                startActivity(new Intent(getActivity(), WholeCourseDetailActivity.class)
                        .putExtra("id", adapter.getChoseData(poc).getId() + ""));
            }
        });
        rv_list.setAdapter(adapter);
        Bundle arguments = getArguments();
        if (arguments != null) {
            List<CourseBean> list = (List<CourseBean>) arguments.getSerializable("list");
            adapter.setData(list);
            if (ToolUtil.isList(list)){
                ll_nor.setVisibility(View.GONE);
            }else {
                ll_nor.setVisibility(View.VISIBLE);
            }
        }
        return inflate;
    }
}
