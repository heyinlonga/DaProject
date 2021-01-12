package software.ecenter.study.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import software.ecenter.study.activity.JiFenActivity;
import software.ecenter.study.activity.PinyinLishiListActivity;
import software.ecenter.study.bean.CommType;
import software.ecenter.study.Adapter.MineAdapter;
import software.ecenter.study.Interface.OnItemListener;
import software.ecenter.study.R;
import software.ecenter.study.View.CircleImageView;
import software.ecenter.study.activity.AccountManagementActivity;
import software.ecenter.study.activity.CollectionActivity;
import software.ecenter.study.activity.DownLoadAndUpdataActivity;
import software.ecenter.study.activity.HelpActivity;
import software.ecenter.study.activity.MessHuDongActivity;
import software.ecenter.study.activity.MessageActivity;
import software.ecenter.study.activity.MyBuyActivity;
import software.ecenter.study.activity.SetExerciseActivity;
import software.ecenter.study.activity.SettingActivity;
import software.ecenter.study.activity.SignActivity;
import software.ecenter.study.activity.StudyreportActivity;
import software.ecenter.study.activity.TeacherSpaceActivity;
import software.ecenter.study.activity.UserInfoActivity;
import software.ecenter.study.bean.HaveNewMessageBean;
import software.ecenter.study.bean.MineBean.MineDetailBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.MyGlideUrl;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * dec 我的
 * Created by Mike on 2018/4/25.
 */

public class TabFourFragment extends BaseFragment {


    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.btn_right_title)
    ImageView btnRightTitle;
    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.btn_sign)
    Button btnSign;
    @BindView(R.id.btn_integral)
    LinearLayout btnIntegral;
    @BindView(R.id.btn_bit)
    LinearLayout btnBit;
    @BindView(R.id.integral_text)
    TextView integralText;
    @BindView(R.id.learnCoin_text)
    TextView learnCoinText;
    @BindView(R.id.me_rl_yin_dao)
    RelativeLayout meRlYinDao;
    @BindView(R.id.me_iv_yin_dao3)
    ImageView meIvYinDao3;
    @BindView(R.id.me_iv_yin_dao4)
    ImageView meIvYinDao4;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    Unbinder unbinder;
    private MineDetailBean mMineDetailBean;
    private MineAdapter adapter;
    private String headImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_four, null, false);
        unbinder = ButterKnife.bind(this, mRootView);
        rvList.setLayoutManager(new GridLayoutManager(mContext, 3));
        adapter = new MineAdapter(mContext, listener);
        rvList.setAdapter(adapter);
        getData(true);
        return mRootView;
    }

    //获取个人信息与消息未读
    private void getData(boolean isShow) {
        getUserMyIndex(isShow);
        getHaveUnMessage();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        }
    }

    //点击事件
    OnItemListener listener = new OnItemListener() {
        @Override
        public void onItemClick(int poc) {
            CommType choseData = adapter.getChoseData(poc);
            String id = choseData.getId();
            switch (ToolUtil.getString(id)) {
                case "1"://我的题集
                    startActivity(new Intent(mContext, SetExerciseActivity.class));
                    break;
                case "2"://我的练习
                    startActivity(new Intent(mContext, PinyinLishiListActivity.class));
                    break;
                case "3"://我的收藏
                    startActivity(new Intent(mContext, CollectionActivity.class));
                    break;
                case "4"://上传下载
                    startActivity(new Intent(mContext, DownLoadAndUpdataActivity.class));
//                    startActivity(new Intent(mContext, DownLoadDActivity.class));
//                    startActivity(new Intent(mContext, MyUpdataActivity.class));
                    break;
                case "5"://我的购买
                    startActivity(new Intent(mContext, MyBuyActivity.class));
                    break;
                case "6"://我的钱包
                    if (mMineDetailBean == null) {
                        return;
                    }
                    Intent intent1 = new Intent(mContext, AccountManagementActivity.class);
                    intent1.putExtra("currentIntegral", mMineDetailBean.getData().getCurrentIntegral());
                    intent1.putExtra("totalIntegral", mMineDetailBean.getData().getTotalIntegral());
                    startActivity(intent1);
                    break;
                case "7"://互动
                    startActivity(new Intent(mContext, MessHuDongActivity.class));
//                    startActivity(new Intent(mContext, QuestionActivity.class));
//                    startActivity(new Intent(mContext, CommentActivity.class));
                    break;
                case "8"://学情报告
                    startActivity(new Intent(mContext, StudyreportActivity.class));
                    break;
                case "9"://教师空间
                    startActivity(new Intent(mContext, TeacherSpaceActivity.class));
                    break;
                case "10"://帮助及反馈
                    startActivity(new Intent(mContext, HelpActivity.class));
                    break;
                case "11"://积分兑换
                    startActivity(new Intent(mContext, JiFenActivity.class));
                    break;
            }
        }
    };

    @OnClick({R.id.btn_left_title, R.id.btn_right_title,
            R.id.img_head, R.id.user_name, R.id.btn_integral,
            R.id.btn_bit, R.id.btn_sign, R.id.me_rl_yin_dao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title: //设置
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.btn_right_title: //消息
                startActivity(new Intent(mContext, MessageActivity.class));
                break;
            case R.id.user_name:
            case R.id.img_head: //用户信息
                startActivity(new Intent(mContext, UserInfoActivity.class));
                break;
            case R.id.btn_integral: //我的钱包
            case R.id.btn_bit://我的钱包
                if (mMineDetailBean == null) {
                    return;
                }
                Intent intent1 = new Intent(mContext, AccountManagementActivity.class);
                intent1.putExtra("currentIntegral", mMineDetailBean.getData().getCurrentIntegral());
                intent1.putExtra("totalIntegral", mMineDetailBean.getData().getTotalIntegral());
                startActivity(intent1);
                break;
            case R.id.btn_sign: //签到
                startActivity(new Intent(mContext, SignActivity.class));
                break;
            case R.id.me_rl_yin_dao: //如果是第一次打开点击后设置以后不再显示引导信息
                if (AccountUtil.getFirstOpenMessage(mContext)) {
                    AccountUtil.saveFirstOpenMessage(mContext, false);
                    if (AccountUtil.getFirstOpenMoney(mContext)) {
                        meIvYinDao4.setImageDrawable(getResources().getDrawable(R.drawable.yindao4));
                        meIvYinDao3.setVisibility(View.GONE);
                    }
                } else {
                    AccountUtil.saveFirstOpenMoney(mContext, false);
                    meRlYinDao.setVisibility(View.GONE);
                }
                break;
        }
    }

    //获取个人信息
    public void getUserMyIndex(boolean isShow) {
        if (isShow) {
            if (!showNetWaitLoding()) {
                return;
            }
        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getUserMyIndex(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        mMineDetailBean = ParseUtil.parseBean(response, MineDetailBean.class);
                        if (mMineDetailBean == null) {
                            return;
                        }
                        adapter.setData(getDataList(mMineDetailBean.getData().getRole().contains("2")));
                        if (!TextUtils.isEmpty(mMineDetailBean.getData().getNickname())) {
                            userName.setText(mMineDetailBean.getData().getNickname());
                        } else {
                            if (!TextUtils.isEmpty(mMineDetailBean.getData().getRealPhone())) {
                                userName.setText(mMineDetailBean.getData().getRealPhone());
                            } else {
                                userName.setText(AccountUtil.getRelMobile(mContext));
                            }
                        }


                        integralText.setText(mMineDetailBean.getData().getCurrentIntegral() + "");
                        learnCoinText.setText(mMineDetailBean.getData().getLearnCoin());
                        if (!TextUtils.isEmpty(mMineDetailBean.getData().getHeadImage())) {
                            Glide.with(mContext).load(new MyGlideUrl(mMineDetailBean.getData().getHeadImage() + "")).error(R.drawable.morentx).into(imgHead);//用户头像
                        } else {
                            imgHead.setImageResource(R.drawable.morentx);
                        }
//                        Glide.with(mContext).load(mMineDetailBean.getData().getHeadImage()).transform(new CenterCrop(mContext), new GlideCircleTransform(mContext)).error(R.drawable.morentx).into(new SimpleTarget<GlideDrawable>() {
//                            @Override
//                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                                imgHead.setImageDrawable(resource);
//                            }
//                        });

                        AccountUtil.saveGrade(mContext, mMineDetailBean.getData().getGrade());
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });

    }

    private List<CommType> getDataList(boolean isTher) {
        List<CommType> list = new ArrayList<>();
        list.add(new CommType("我的题集", "1", R.drawable.wtiji));
        if (!isTher) {
            list.add(new CommType("我的练习", "2", R.drawable.wlianxi));
        }
        list.add(new CommType("我的收藏", "3", R.drawable.wshoucang));
        list.add(new CommType("上传下载", "4", R.drawable.wscxz));
        list.add(new CommType("我的购买", "5", R.drawable.wgoumai));
        list.add(new CommType("我的钱包", "6", R.drawable.wqianbao));
        list.add(new CommType("积分兑换", "11", R.drawable.duihuan1));
        list.add(new CommType("信息互动", "7", R.drawable.whudon));
        if (isTher) {
            list.add(new CommType("教师空间", "9", R.drawable.jiaoshikongjian));
        } else {
            list.add(new CommType("学情报告", "8", R.drawable.wxqbg));
        }
        list.add(new CommType("帮助及反馈", "10", R.drawable.wbangzhu));
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 获取是否有未读消息
     */
    public void getHaveUnMessage() {
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getHaveNewMessage())
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        HaveNewMessageBean haveNewMessageBean = ParseUtil.parseBean(response, HaveNewMessageBean.class);
                        if (haveNewMessageBean != null && haveNewMessageBean.getData() != null)
                            setHaveNewMessage(haveNewMessageBean.getData().isHaveNewMessage());
                    }

                    @Override
                    public void onFail(int type, String msg) {

                    }
                });
    }

    private void setHaveNewMessage(boolean haveNewMessage) {
        if (haveNewMessage)
            btnRightTitle.setImageDrawable(getResources().getDrawable(R.drawable.xiaoxi2));
        else
            btnRightTitle.setImageDrawable(getResources().getDrawable(R.drawable.xiaoxi));
    }
}
