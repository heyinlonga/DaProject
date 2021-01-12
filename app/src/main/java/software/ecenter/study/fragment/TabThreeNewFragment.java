package software.ecenter.study.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import software.ecenter.study.Adapter.HistoryHuoDongNewAdapter;
import software.ecenter.study.Adapter.HuoDongNewAdapter;
import software.ecenter.study.R;
import software.ecenter.study.activity.HuoDongDetailActivity;
import software.ecenter.study.activity.MatchDetailActivity;
import software.ecenter.study.activity.PinyinDetailActivity;
import software.ecenter.study.activity.ReadBookListActivity;
import software.ecenter.study.activity.WebActivity;
import software.ecenter.study.bean.HuoDongBean.ActiviteNewBean;
import software.ecenter.study.bean.MineBean.PersonDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.SysUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * dec 活动
 * Created by Mike on 2018/4/25.
 */

public class TabThreeNewFragment extends BaseFragment {

    @BindView(R.id.list_activite)
    RecyclerView listTop;
    @BindView(R.id.list_history)
    RecyclerView listBottom;
    Unbinder unbinder;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.iv_top)
    ImageView ivTop;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.iv_right)
    ImageView ivRight;

    private boolean kousuan, yuedu;
    private ActiviteNewBean mActivityDetailBean;
    private List<ActiviteNewBean.DataBean.ActivityListBean> listTopData;
    private List<ActiviteNewBean.DataBean.ActivityListBean> listBottomData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_three_new, null);
        unbinder = ButterKnife.bind(this, mRootView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listTop.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManagerOne = new LinearLayoutManager(mContext);
        linearLayoutManagerOne.setOrientation(LinearLayoutManager.VERTICAL);
        listBottom.setLayoutManager(linearLayoutManagerOne);

        getActivityList(true);
        return mRootView;
    }

    public void getData(){
        getActivityList(true);
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
        ActiviteNewBean.DataBean data = mActivityDetailBean.getData();
        if (data != null) {
            String compositionImgUrl = data.getCompositionImgUrl();
            Glide.with(getActivity()).load(compositionImgUrl).into(ivTop);
            List<ActiviteNewBean.DataBean.MatchListBean> matchList = data.getMatchList();
            if (matchList != null) {
                for (int i = 0; i < matchList.size(); i++) {
                    ActiviteNewBean.DataBean.MatchListBean bean = matchList.get(i);
                    if (bean != null) {
                        String name = bean.getName();
                        String imgUrl = bean.getImgUrl();
                        if (name.contains("口算")) {
                            kousuan = bean.isCanEnter();
                            Glide.with(getActivity()).load(imgUrl).into(ivLeft);
                        }
                        if (name.contains("阅读")) {
                            yuedu = bean.isCanEnter();
                            Glide.with(getActivity()).load(imgUrl).into(ivRight);
                        }
                    }
                }
            }
        }
        listTopData = mActivityDetailBean.getData().getActivityList();
        HuoDongNewAdapter adapter = new HuoDongNewAdapter(mContext, listTopData);
        adapter.setItemClickListener(new HuoDongNewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

        /*        listTopData.get(position).setActivityType("5");
                listTopData.get(position).setActivityContent("https://www.baidu.com/");*/

                //listTopData.get(position).setActivityType("4");
                if ("5".equals(listTopData.get(position).getActivityType())) {
                    Intent intent = new Intent(mContext, WebActivity.class);
//                    intent.putExtra("url", listTopData.get(position).getActivityContent());
                    intent.putExtra("activityId", listTopData.get(position).getId() + "");
                    startActivity(intent);
                } else {
                    if ("2".equals(listTopData.get(position).getActivityType())) {
                        if (AccountUtil.getIsTeacherChecked(mContext) != 1) {
                            ToastUtils.showToastLONG(mContext, "请先前往“我的”栏目，进行教师资格认证");
                            return;
                        }
                    }
                    Intent intent = new Intent(mContext, HuoDongDetailActivity.class);
                    intent.putExtra("activityId", listTopData.get(position).getId() + "");
                    intent.putExtra("activityType", listTopData.get(position).getActivityType());
                    startActivity(intent);
                }
            }
        });
        listTop.setAdapter(adapter);
        listBottomData = mActivityDetailBean.getData().getOldMatchList();
        HistoryHuoDongNewAdapter adapterOne = new HistoryHuoDongNewAdapter(mContext, listBottomData);
        adapterOne.setItemClickListener(new HistoryHuoDongNewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //todo 跳转历史阅读大赛
                Intent intent = new Intent(mContext, MatchDetailActivity.class);
                intent.putExtra("id", listBottomData.get(position).getId());
                startActivity(intent);
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

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getActivityListNew(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        // HomeBean临时变量，只含有book数据
                        mActivityDetailBean = ParseUtil.parseBean(response, ActiviteNewBean.class);
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

    @OnClick({R.id.iv_top, R.id.iv_left, R.id.iv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_top:
                if (mActivityDetailBean.getData().getCompositionId() != 0) {
                    Intent intent = new Intent(mContext, HuoDongDetailActivity.class);
                    intent.putExtra("activityId", mActivityDetailBean.getData().getCompositionId() + "");
                    intent.putExtra("activityType", "4");
                    startActivity(intent);
                }
                break;
            case R.id.iv_left:
                if (kousuan) {//口算大赛//todo---------
                    //没有安装应用
                    if (!SysUtil.checkAppInstalled(mContext, "com.study.talkcompute")) {
                        SysUtil.goToMarket(mContext, "http://xzykt.longmenshuju.com/pc-api/qrcode/ksQrcode");
                        return;
                    }
                    //安装了应用
                    Intent intent = new Intent();
                    ComponentName componentName = new ComponentName("com.study.talkcompute", "com.study.talkcompute.ui.SplashActivity");
                    intent.putExtra("joinType", 1);
                    int grade = getGrade();
                    intent.putExtra("joinGrade", grade);
                    intent.setComponent(componentName);
                    startActivity(intent);
                }
                break;
            case R.id.iv_right://阅读大赛
                if (yuedu) {
                    startActivity(new Intent(mContext, ReadBookListActivity.class));
                }
                break;
        }
    }

    //获得年级
    private int getGrade() {
        int grade = 1;
        String realGrade = AccountUtil.getRealGrade(getActivity());
        switch (realGrade) {
            case "一年级上":
                grade = 1;
                break;
            case "一年级下":
                grade = 2;
                break;
            case "二年级上":
                grade = 3;
                break;
            case "二年级下":
                grade = 4;
                break;
            case "三年级上":
                grade = 5;
                break;
            case "三年级下":
                grade = 6;
                break;
            case "四年级上":
                grade = 7;
                break;
            case "四年级下":
                grade = 8;
                break;
            case "五年级上":
                grade = 9;
                break;
            case "五年级下":
                grade = 10;
                break;
            case "六年级上":
                grade = 11;
                break;
            case "六年级下":
                grade = 12;
                break;
        }
        return grade;
    }
}
