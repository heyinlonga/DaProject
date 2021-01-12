package software.ecenter.study.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.Adapter.QuestionDetailAdapter;
import software.ecenter.study.R;
import software.ecenter.study.View.MyRatingDialog;
import software.ecenter.study.bean.MineBean.MyLookQusetionDetailBean;
import software.ecenter.study.bean.MineBean.QusetionLookBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.tool.mp3recorder.AudioPlayerManager;
import software.ecenter.study.tool.mp3recorder.PlayerCallback;
import software.ecenter.study.tool.mp3recorder.ProxyTools;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.ToastUtils;

/**
 * 我的提问详情
 */
@SuppressLint("SetTextI18n")
public class QuestionDetailActivity extends BaseActivity {

    @BindView(R.id.btn_left_title)
    ImageView btnLeftTitle;
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.title_temp)
    RelativeLayout titleTemp;
    @BindView(R.id.list_question)
    RecyclerView listQuestion;
    @BindView(R.id.btn_pinglun)
    Button btnPinglun;
    @BindView(R.id.btn_tiwen)
    Button btnTiwen;
    @BindView(R.id.btn_refresh_net)
    LinearLayout btnRefreshNet;
    @BindView(R.id.bottom_line)
    LinearLayout bottomLine;
    @BindView(R.id.tv_question_detail_dingwei)
    TextView tvQuestionDetailDingWei;

    private QuestionDetailAdapter mAdapter;
    private List<QusetionLookBean> listData = new ArrayList<>();

    private String questionId;
    private MyLookQusetionDetailBean mMyLookQusetionDetailBean;

    public boolean isPlaying;//播放
    public AudioPlayerManager audioPlayerManager;//音频播放
    private QusetionLookBean curBean;
    private int pos;//当前播放的索引


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        ButterKnife.bind(this);
        listQuestion.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        questionId = getIntent().getStringExtra("questionId");

        mAdapter = new QuestionDetailAdapter(mContext, listData);
        mAdapter.setmItemBtnAddClickListener(new QuestionDetailAdapter.OnItemBtnAddClickListener() {
            @Override
            public void onItemBtnAddClick(View v, int position, String audioUrl) {
                if (TextUtils.isEmpty(audioUrl)) {
                    ToastUtils.showToastSHORT(mContext, "播放路径错误");
                    return;
                }
                //正在播放
                if (listData.get(position).isPlaying()) {
                    stopPlayRecording();
                    listData.get(position).setPlaying(false);
                    listData.get(position).setCurDuration(0);
                } else {//没有播放
                    if (isPlaying) {//有在播放其他的语音
                        stopPlayRecording();
                        listData.get(pos).setPlaying(false);
                        listData.get(pos).setCurDuration(0);
                    }
                    startplayRecording(position, audioUrl);
                    listData.get(position).setPlaying(true);
                }
                pos = position;
                mAdapter.refreshData(listData);
//                if (!listData.get(position).isPlaying()) {
//                    if (isPlaying) {
//                        listData.get(position).setCurDuration(0);
//                        stopPlayRecording();
//                        if (curBean != null) {
//                            curBean.setPlaying(false);
//                        }
//                    }
//                    startplayRecording(position, audioUrl);
//                    listData.get(position).setPlaying(true);
//                    curBean = listData.get(position);
//                } else {
//                    stopPlayRecording();
//                    listData.get(position).setPlaying(false);
//                }
            }
        });
        listQuestion.setAdapter(mAdapter);
        getQuestionDetail();
    }

    //获取提问详情
    public void getQuestionDetail() {
        if (!showNetWaitLoding()) {
            return;
        }
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        map.put("questionId", questionId);
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getQuestionDetail(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        mMyLookQusetionDetailBean = ParseUtil.parseBean(response, MyLookQusetionDetailBean.class);
                        if (mMyLookQusetionDetailBean != null && mMyLookQusetionDetailBean.getData() != null) {
                            if (mMyLookQusetionDetailBean.getData().isOver()) {
                                bottomLine.setVisibility(View.GONE);
                            } else {
                                bottomLine.setVisibility(View.VISIBLE);
                            }
                            btnRefreshNet.setVisibility(View.GONE);
                            tvQuestionDetailDingWei.setText(mMyLookQusetionDetailBean.getData().getLocation() + "");
                            listData = mMyLookQusetionDetailBean.getData().getQuestionList();
                            mAdapter.refreshData(listData);
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        btnRefreshNet.setVisibility(View.VISIBLE);
                        bottomLine.setVisibility(View.GONE);
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }

    //开始播放
    public void startplayRecording(int position, String audioUrl) {
        if (audioPlayerManager == null) {
            initPlayer(position, audioUrl);
        } else {
            audioPlayerManager.release();
            audioPlayerManager.setDataSource(audioUrl);
        }
        isPlaying = true;
        audioPlayerManager.start();
    }

    //停止播放
    public void stopPlayRecording() {
        isPlaying = false;
        if (audioPlayerManager != null) {
            audioPlayerManager.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioPlayerManager !=null) {
            audioPlayerManager.stop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (audioPlayerManager !=null&&isPlaying) {
            audioPlayerManager.pause();
            isPlaying = false;
            listData.get(pos).setPlaying(false);
            listData.get(pos).setCurDuration(0);
            mAdapter.refreshData(listData);
        }
    }

    private void initPlayer(final int position, String path) {
        audioPlayerManager = AudioPlayerManager.get();
        audioPlayerManager.setDataSource(path)
                .setCallback(ProxyTools.getShowMethodInfoProxy(new PlayerCallback() {
                    @Override
                    public void onPreparing(Object dataSource, AudioPlayerManager manager) {

                    }

                    @Override
                    public void onPlaying(Object dataSource, AudioPlayerManager manager) {

                    }

                    @Override
                    public void onPause(Object dataSource, AudioPlayerManager manager) {

                    }

                    @Override
                    public void onFinished(Object dataSource, AudioPlayerManager manager) {
                        isPlaying = false;
                        listData.get(position).setPlaying(false);
                        listData.get(position).setCurDuration(0);
                        mAdapter.refreshData(listData);
                    }

                    @Override
                    public void onStop(Object dataSource, AudioPlayerManager manager) {
                        isPlaying = false;
                        listData.get(position).setPlaying(false);
                        listData.get(position).setCurDuration(0);
                        mAdapter.refreshData(listData);
                    }

                    @Override
                    public void onError(Object dataSource, AudioPlayerManager manager) {
                        isPlaying = false;
                        listData.get(position).setPlaying(false);
                        listData.get(position).setCurDuration(0);
                        mAdapter.refreshData(listData);
                    }

                    @Override
                    public void onProgress(int progress, Object dataSource, AudioPlayerManager manager) {
                        int recordingSecond = ((int) (progress / 1000 / 60) * 60) + ((int) (progress / 1000 % 60));
                        listData.get(position).setCurDuration(recordingSecond);
                        mAdapter.refreshData(listData);
                    }

                    @Override
                    public void onSeeking(Object dataSource, AudioPlayerManager manager) {

                    }

                    @Override
                    public void onBufferingUpdate(int percent, AudioPlayerManager manager) {

                    }

                    @Override
                    public void onGetMaxDuration(int maxDuration) {
                        // String.format("%d:%02d",maxDuration/1000/60,maxDuration/1000);
                    }
                }));

    }


    @OnClick({R.id.btn_left_title, R.id.btn_pinglun, R.id.btn_tiwen, R.id.btn_refresh_net})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_title://返回
                onBackPressed();
                break;
            case R.id.btn_pinglun://结束评论
                showRatingDialog();
                break;
            case R.id.btn_tiwen://追问
                if (mMyLookQusetionDetailBean.getData().isInquiry()) {
                    Intent intent = new Intent(mContext, QuestionCloselyActivity.class);
                    intent.putExtra("questionId", questionId);
                    startActivityForResult(intent, 111);
                } else {
                    ToastUtils.showToastLONG(mContext, "老师还没有回复，不能追问");
                }
                break;
            case R.id.btn_refresh_net://刷新
                getQuestionDetail();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == 111 && resultCode == 111) {
            getQuestionDetail();
        }
    }

    //结束评论
    public void showRatingDialog() {
        MyRatingDialog.Builder builder = new MyRatingDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setStar(3);
        builder.setPositiveButton("提交评价", new MyRatingDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which, float star) {
                dialog.dismiss();
                //设置你的操作事项
                submitQuestionScore("" + star);

            }
        });
        builder.create().show();
    }

    //结束评论
    public void submitQuestionScore(String star) {
        if (!showNetWaitLoding()) {
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("star", star);
            json.put("questionId", questionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).submitQuestionScore(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, "评价成功");
                        onBackPressed();
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }
}
