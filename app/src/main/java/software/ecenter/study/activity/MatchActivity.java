package software.ecenter.study.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.bean.AnswerBody;
import software.ecenter.study.Adapter.MatchTiAdapter;
import software.ecenter.study.bean.TiBean;
import software.ecenter.study.Interface.OnItemListener;
import software.ecenter.study.R;
import software.ecenter.study.bean.Bean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.TimeUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message 比赛
 * Create by Administrator
 * Create by 2019/11/1
 */
@SuppressLint("SetTextI18n")
public class MatchActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_up)
    TextView tv_up;
    @BindView(R.id.tv_next)
    TextView tv_next;
    @BindView(R.id.tv_config)
    TextView tv_config;
    @BindView(R.id.tv_currentTime)
    TextView tv_currentTime;
    @BindView(R.id.tv_totalTime)
    TextView tv_totalTime;
    @BindView(R.id.tv_tiNum)
    TextView tv_tiNum;
    @BindView(R.id.tv_ti)
    TextView tv_ti;
    @BindView(R.id.iv_icon)
    ImageView iv_icon;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    private int postion;
    private String id;
    private int time;
    private List<TiBean> dataList;
    private MatchTiAdapter adapter;
    private long beginTime;
    private int currentTIme;//用时
    private TiBean tiBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        ButterKnife.bind(this);
        rv_list.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        adapter = new MatchTiAdapter(mContext, new OnItemListener() {
            @Override
            public void onItemClick(int poc) {
                adapter.setChose(poc);
                setDataChange(adapter.getChose());
            }
        });
        rv_list.setAdapter(adapter);
        id = getIntent().getStringExtra("id");
        time = getIntent().getIntExtra("time", 0);
        dataList = (List<TiBean>) getIntent().getSerializableExtra("data");
        tv_totalTime.setText(TimeUtil.getTimeMS(time * 1000));
        tv_currentTime.setText(TimeUtil.getTimeMS(time * 1000));
        beginTime = System.currentTimeMillis();
        handler.sendEmptyMessageDelayed(1, 1000);
        showCurrentTi();
    }

    @OnClick({R.id.iv_back, R.id.tv_up, R.id.tv_next, R.id.tv_config, R.id.iv_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_up://上一题
                postion--;
                showCurrentTi();
                break;
            case R.id.tv_next://下一题
                postion++;
                showCurrentTi();
                break;
            case R.id.tv_config://提交
                clickConfig();
                break;
            case R.id.iv_icon:
                if (tiBean != null) {
                    PictureSelector.create(this)
                            .externalPicturePreview(0, "study", ToolUtil.getPicData(ToolUtil.getString(tiBean.getOptionImgUrl())));
                }
                break;
        }
    }

    //提交
    private void clickConfig() {
        handler.removeMessages(1);
        int noAnswerNum = getNoAnswerNum();
        if (noAnswerNum <= 0) {
            config();
            return;
        }
        ToolUtil.showMatchConfigDialog(this, noAnswerNum, new OnItemListener() {
            @Override
            public void onConfig() {
                //提交
                config();
            }

            @Override
            public void onCancle() {
                handler.sendEmptyMessageDelayed(1, 1000);
            }
        });
    }

    //获取未答数量
    private int getNoAnswerNum() {
        int num = 0;
        if (ToolUtil.isList(dataList)) {
            for (int i = 0; i < dataList.size(); i++) {
                String myAnswer = dataList.get(i).getMyAnswer();
                if (TextUtils.isEmpty(myAnswer)) {
                    num++;
                }
            }
        }
        return num;
    }

    //提价答案
    private void config() {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        if (!showNetWaitLoding()) {
            return;
        }
        long endTime = System.currentTimeMillis();
        AnswerBody answerBody = new AnswerBody();
        answerBody.setId(id);
        answerBody.setBeginTime(TimeUtil.getTime(beginTime, TimeUtil.NORMAL_PATTERN));
        answerBody.setEndTime(TimeUtil.getTime(endTime, TimeUtil.NORMAL_PATTERN));
        answerBody.setIsOverTime(currentTIme - time >= 0);
        answerBody.setTimeCost(currentTIme);
        answerBody.setAnswerList(getAnswerList());
        Log.d("1e12dqw", new Gson().toJson(answerBody) + "");
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(answerBody));

        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getMatchSubmit(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        Bean bean = ParseUtil.parseBean(response, Bean.class);
                        if (bean != null) {
                            ToastUtils.showToastSHORT(mContext, ToolUtil.getString(bean.getMessage()));
                            startActivity(new Intent(mContext, MeResultActivity.class).putExtra("id", id).putExtra("type", 1));
                            setResult(101, new Intent());
                            finish();
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    //获取比赛答案列表
    private List<AnswerBody.AnswerBean> getAnswerList() {
        List<AnswerBody.AnswerBean> list = new ArrayList<>();
        if (ToolUtil.isList(dataList)) {
            for (int i = 0; i < dataList.size(); i++) {
                AnswerBody.AnswerBean answerBody = new AnswerBody.AnswerBean();
                answerBody.setId(ToolUtil.getString(dataList.get(i).getId()));
                answerBody.setMyAnswer(ToolUtil.getString(dataList.get(i).getMyAnswer()));
                list.add(answerBody);
            }
        }
        return list;
    }

    //改变答案
    private void setDataChange(String answer) {
        if (ToolUtil.isList(dataList) && postion < dataList.size()) {
            dataList.get(postion).setMyAnswer(answer);
            setTiName();
        }
    }

    //显示当前题目数据
    public void showCurrentTi() {
        if (ToolUtil.isList(dataList) && postion < dataList.size()) {
            tiBean = dataList.get(postion);
            if (tiBean != null) {
                if (!TextUtils.isEmpty(tiBean.getOptionImgUrl())) {
                    iv_icon.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(tiBean.getOptionImgUrl()).into(iv_icon);
                } else {
                    iv_icon.setImageResource(0);
                    iv_icon.setVisibility(View.GONE);
                }
                adapter.setData(tiBean.getMyAnswer(), tiBean.getOptions());
            }
            setTiName();
            showButtonState();
            setTiNum();
        }
    }

    //设置题目名称及答案
    private void setTiName() {
        if (ToolUtil.isList(dataList) && postion < dataList.size()) {
            TiBean bean = dataList.get(postion);
            if (bean != null) {
                tv_ti.setText((postion + 1) + "." + ToolUtil.getString(bean.getQuestion()) + "【 " + ToolUtil.getString(bean.getMyAnswer()) + " 】");
            }
        }
    }

    //底部按钮的状态
    private void showButtonState() {
        if (ToolUtil.isList(dataList)) {
            if (postion == 0) {
                tv_up.setVisibility(View.VISIBLE);
                tv_next.setVisibility(View.VISIBLE);
                tv_config.setVisibility(View.GONE);
                tv_next.setSelected(true);
                tv_up.setSelected(false);
                tv_up.setClickable(false);
                //只有一题
                if (postion == dataList.size() - 1) {
                    tv_up.setVisibility(View.GONE);
                    tv_next.setVisibility(View.GONE);
                    tv_config.setVisibility(View.VISIBLE);
                }
            } else if (postion == dataList.size() - 1) {
                tv_up.setVisibility(View.VISIBLE);
                tv_next.setVisibility(View.GONE);
                tv_config.setVisibility(View.VISIBLE);
                tv_next.setSelected(true);
                tv_up.setSelected(true);
                tv_up.setClickable(true);
            } else {
                tv_up.setVisibility(View.VISIBLE);
                tv_next.setVisibility(View.VISIBLE);
                tv_config.setVisibility(View.GONE);
                tv_next.setSelected(true);
                tv_up.setSelected(true);
                tv_up.setClickable(true);
            }
        }
    }

    //设置题目数量
    private void setTiNum() {
        if (ToolUtil.isList(dataList)) {
            tv_tiNum.setText((postion + 1) + "/" + dataList.size());
        }
    }

    //倒计时
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            currentTIme++;
            tv_currentTime.setText(TimeUtil.getTimeMS((time - currentTIme > 0 ? time - currentTIme : 0) * 1000));
            if ((time - currentTIme) == 30) {//30秒倒计时提示
                ToolUtil.showMatchMessDialog(MatchActivity.this, new OnItemListener() {
                    @Override
                    public void onConfig() {
                        clickConfig();
                    }

                    @Override
                    public void onCancle() {
                        handler.sendEmptyMessageDelayed(1, 1000);
                    }
                });
            } else {
                handler.sendEmptyMessageDelayed(1, 1000);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
