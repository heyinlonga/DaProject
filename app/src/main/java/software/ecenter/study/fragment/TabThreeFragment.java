package software.ecenter.study.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import software.ecenter.study.Adapter.HuoDongBottomAdapter;
import software.ecenter.study.Adapter.HuoDongTopAdapter;
import software.ecenter.study.R;
import software.ecenter.study.activity.HuoDongDetailActivity;
import software.ecenter.study.activity.WebActivity;
import software.ecenter.study.bean.HuoDongBean.ActivityBean;
import software.ecenter.study.bean.HuoDongBean.ActivityDetailBean;
import software.ecenter.study.bean.MineBean.PersonDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * dec 活动
 * Created by Mike on 2018/4/25.
 */

public class TabThreeFragment extends BaseFragment {

    @BindView(R.id.list_top)
    RecyclerView listTop;
    @BindView(R.id.list_bottom)
    RecyclerView listBottom;
    Unbinder unbinder;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;


    private ActivityDetailBean mActivityDetailBean;
    private List<ActivityBean> listTopData;
    private List<ActivityBean> listBottomData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_three, null);
        unbinder = ButterKnife.bind(this, mRootView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listTop.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManagerOne = new LinearLayoutManager(mContext);
        linearLayoutManagerOne.setOrientation(LinearLayoutManager.VERTICAL);
        listBottom.setLayoutManager(linearLayoutManagerOne);

        getActivityList(true);
        return mRootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            getActivityList(false);
            getUserInfo();
        }
    }

    private void getUserInfo() {

//        if (!showNetWaitLoding()) {
//            return;
//        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getUserInfo(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        PersonDetailBean personDetailBean = ParseUtil.parseBean(response, PersonDetailBean.class);
                        if (personDetailBean != null || personDetailBean.getData() != null) {
                            AccountUtil.saveUserInfo(mContext, personDetailBean);
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    public void initView() {
        if (mActivityDetailBean == null) {
            return;
        }


        listTopData = mActivityDetailBean.getData().getHotActivityList();
        HuoDongTopAdapter adapter = new HuoDongTopAdapter(mContext, listTopData);
        adapter.setItemClickListener(new HuoDongTopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

        /*        listTopData.get(position).setActivityType("5");
                listTopData.get(position).setActivityContent("https://www.baidu.com/");*/

                //listTopData.get(position).setActivityType("4");
                if ("5".equals(listTopData.get(position).getActivityType())) {
                    Intent intent = new Intent(mContext, WebActivity.class);
//                    intent.putExtra("url", listTopData.get(position).getActivityContent());
                    intent.putExtra("activityId", listTopData.get(position).getActivityId());
                    startActivity(intent);
                } else {
                    if ("2".equals(listTopData.get(position).getActivityType())) {
                        if (AccountUtil.getIsTeacherChecked(mContext) != 1) {
                            ToastUtils.showToastLONG(mContext, "请先前往“我的”栏目，进行教师资格认证");
                            return;
                        }
                    }
                    Intent intent = new Intent(mContext, HuoDongDetailActivity.class);
                    intent.putExtra("activityId", listTopData.get(position).getActivityId());
                    intent.putExtra("activityType", listTopData.get(position).getActivityType());
                    startActivity(intent);
                }

            }
        });
        listTop.setAdapter(adapter);


        listBottomData = mActivityDetailBean.getData().

                getOldActivityList();

        HuoDongBottomAdapter adapterOne = new HuoDongBottomAdapter(mContext, listBottomData);
        adapterOne.setItemClickListener(new HuoDongBottomAdapter.OnItemClickListener()

        {
            @Override
            public void onItemClick(int position) {

                if ("5".equals(listBottomData.get(position).getActivityType())) {
                    Intent intent = new Intent(mContext, WebActivity.class);
//                    intent.putExtra("url", listBottomData.get(position).getActivityContent());
                    intent.putExtra("activityId", listBottomData.get(position).getActivityId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, HuoDongDetailActivity.class);
                    intent.putExtra("activityId", listBottomData.get(position).getActivityId());
                    intent.putExtra("activityType", listBottomData.get(position).getActivityType());
                    startActivity(intent);
                }
                //startActivity(new Intent(mContext, CompositionSubmissionActivity.class));
            }
        });
        listBottom.setAdapter(adapterOne);


    }

    //节获取接口

    public void getActivityList(boolean isShow) {
        if (isShow) {
            if (!showNetWaitLoding()) {
                return;
            }
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getActivityList(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        // HomeBean临时变量，只含有book数据
                        mActivityDetailBean = ParseUtil.parseBean(response, ActivityDetailBean.class);
                        btnRefreshNet.setVisibility(View.GONE);
                        initView();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.VISIBLE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_refresh_net)
    public void onViewClicked() {
        getActivityList(true);
    }
}
