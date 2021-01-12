package software.ecenter.study.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import software.ecenter.study.Adapter.PySpellAdapter;
import software.ecenter.study.Interface.OnItemListener;
import software.ecenter.study.R;
import software.ecenter.study.bean.CommType;
import software.ecenter.study.bean.PinLishiBean;
import software.ecenter.study.bean.PinTiBean;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.tool.mp3recorder.AudioPlayerManager;
import software.ecenter.study.tool.mp3recorder.PlayerCallback;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.PlayHelperUtil;
import software.ecenter.study.utils.TimeUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message 拼音拼读列表
 * Create by Administrator
 * Create by 2019/11/8
 */
public class PinyinSpellListActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_lishi)
    TextView tv_lishi;
    @BindView(R.id.tv_xuan)
    TextView tv_xuan;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;
    private PySpellAdapter adapter;
    private int playPoc;
    private String id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinyinspelllist);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        initListener();
        adapter = new PySpellAdapter(mContext, new OnItemListener() {
            @Override
            public void onItemClick(int type,int poc) {
                if (type == 0) {
                    startActivity(new Intent(mContext, PinyinDetailActivity.class));
                } else {
                    if (TextUtils.isEmpty(adapter.getPath(poc))) {
                        ToastUtils.showToastSHORT(mContext, "播放路径错误");
                        return;
                    }
                    if (PlayHelperUtil.newInrence().playStatus()) {
                        PlayHelperUtil.newInrence().stopPlay();
                        if (poc != playPoc){
                            PlayHelperUtil.newInrence().startPlay(adapter.getPath(poc));
                        }
                    } else {
                        PlayHelperUtil.newInrence().startPlay(adapter.getPath(poc));
                    }
                    adapter.setPlay(poc,PlayHelperUtil.newInrence().playStatus());
                }
                playPoc = poc;
            }
        });
        rv_list.setAdapter(adapter);
        getData();
    }
    //获取数据
    private void getData() {
        if (TextUtils.isEmpty(id) || !showNetWaitLoding()) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getPinduDetail(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        PinLishiBean bean = ParseUtil.parseBean(response, PinLishiBean.class);
                        if (bean!=null){
                            PinLishiBean.DataBean data = bean.getData();
                            if (data!=null){
                                String resourceName = data.getResourceName();
                                tv_title.setText(ToolUtil.getString(resourceName));
                                List<PinLishiBean.DataBean.AudioListBean> audioList = data.getAudioList();
                                adapter.setData(audioList);
                            }
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }
                });
    }
    @OnClick({R.id.iv_back, R.id.tv_lishi, R.id.tv_xuan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_lishi://历史
                startActivity(new Intent(mContext, PinyinLishiListActivity.class));
                break;
            case R.id.tv_xuan://炫耀
                startActivity(new Intent(mContext, SharePinActivity.class).putExtra("score",adapter.getScore()).putExtra("month",TimeUtil.getCurrentDate()));
                break;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        PlayHelperUtil.newInrence().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PlayHelperUtil.newInrence().onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PlayHelperUtil.newInrence().onDestroy();
    }
    private void initListener() {
        PlayHelperUtil.newInrence().listener(new PlayerCallback() {
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
                adapter.setPlay(playPoc,PlayHelperUtil.newInrence().playStatus());
            }

            @Override
            public void onStop(Object dataSource, AudioPlayerManager manager) {

            }

            @Override
            public void onError(Object dataSource, AudioPlayerManager manager) {
                adapter.setPlay(playPoc,PlayHelperUtil.newInrence().playStatus());
            }

            @Override
            public void onProgress(int progress, Object dataSource, AudioPlayerManager manager) {

            }

            @Override
            public void onSeeking(Object dataSource, AudioPlayerManager manager) {

            }

            @Override
            public void onBufferingUpdate(int percent, AudioPlayerManager manager) {

            }

            @Override
            public void onGetMaxDuration(int maxDuration) {

            }
        });
    }
}
