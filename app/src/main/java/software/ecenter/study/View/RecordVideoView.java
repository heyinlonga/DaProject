package software.ecenter.study.View;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.czt.mp3recorder.Mp3Recorder;
import com.czt.mp3recorder.Mp3RecorderUtil;
import com.shuyu.gsyvideoplayer.utils.FileUtils;

import software.ecenter.study.Adapter.PopAdapter;
import software.ecenter.study.Interface.OnClickItemListener;
import software.ecenter.study.Interface.OnItemListener;
import software.ecenter.study.R;
import software.ecenter.study.tool.mp3recorder.AudioPlayerManager;
import software.ecenter.study.tool.mp3recorder.PlayerCallback;
import software.ecenter.study.utils.PermissionUtils;
import software.ecenter.study.utils.TimeUtil;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

/**
 * Message: 录制
 * Author: 陈龙
 * Date: 2019/8/1 11:40
 */
public class RecordVideoView extends LinearLayout implements View.OnClickListener {
    /**
     * 录音按钮
     */
    private ImageView iv_luyin;
    /**
     * 声音
     */
    private ImageView iv_shenyin;
    /**
     * 播放按钮
     */
    private ImageView iv_play;
    /**
     * 语音时间
     */
    private TextView tv_time;
    /**
     * 当前录音状态
     */
    private RecordState curretnRecordState = RecordState.NORMAL;
    /**
     * 录音控件
     */
    private Mp3Recorder mp3Recorder;
    /**
     * 录音文件地址
     */
    private String recordPath;
    /**
     * 录音文件夹名称
     */
    private String dirName;
    /**
     * 最小录制时间
     */
    private int MIN_RECORD_TIME = 1000;
    /**
     * 最大录制时间  秒
     */
    private int MAX_RECORD_TIME = 60;
    /**
     * 当前录制时长
     */
    private int currentrecordTime;
    /**
     * 语音播放
     */
    private AudioPlayerManager audioPlayerManager;
    /**
     * 上下文
     */
    private Activity activity;
    private OnClickItemListener listener;

    public void setActivity(Activity activity, String dir, OnClickItemListener listener) {
        this.activity = activity;
        this.listener = listener;
        if (TextUtils.isEmpty(dir)) {
//            dirName = activity.getString(R.string.app_name);
            dirName = "study";
        } else {
            this.dirName = dir;
        }
    }

    public void setCurrentrecordTime(int currentrecordTime) {
        this.currentrecordTime = currentrecordTime;
    }

    /**
     * 回显录音
     *
     * @param path
     */
    public void setVideoPath(String path) {
        recordPath = path;
        showLunState(RecordState.RECORDING);
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(recordPath);
            int duration = mediaPlayer.getDuration();
            tv_time.setText(duration / 1000 + "s");
        } catch (Exception e) {
            tv_time.setText("0s");
            e.printStackTrace();
        }
    }

    /**
     * 录音状态
     * NORMAL  未录音  RECORDING  录音中 PAUSE 暂停 OVER 录音完成
     */
    public enum RecordState {
        NORMAL,
        RECORDING,
        OVER
    }

    /**
     * 是否正在播放语音
     */
    boolean isPlaying;

    public RecordVideoView(Context context) {
        this(context, null);
    }

    public RecordVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    //初始化
    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_record, this, true);
        iv_luyin = findViewById(R.id.iv_luyin);
        iv_shenyin = findViewById(R.id.iv_shenyin);
        iv_play = findViewById(R.id.iv_play);
        tv_time = findViewById(R.id.tv_time);

        iv_luyin.setOnClickListener(this);
        iv_play.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_luyin://录音按钮
                PermissionUtils.newIntence().requestPerssion(activity, ToolUtil.PERSSION_RECORD, 1, new PermissionUtils.IPermission() {
                    @Override
                    public void success(int code) {
                        if (curretnRecordState == RecordState.OVER) {
                            if (listener != null) {
                                listener.onCancle();
                            }
                        } else {
                            if (listener != null) {
                                listener.onPay();
                            }
                        }
                        showLunState(curretnRecordState);
                    }
                });
                break;
            case R.id.iv_play://播放
                if (isPlaying) {
                    stopPlay();
                } else {
                    startPlay();
                }
                break;
        }
    }

    //初始化播放
    private void initPlayer(String path) {
        audioPlayerManager = new AudioPlayerManager();
        audioPlayerManager.setDataSource(path)
                .setCallback(new PlayerCallback() {
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
                        iv_play.setImageResource(R.drawable.bofangxiao);
                        if (curretnRecordState != RecordState.NORMAL) {
                            String timeReord = TimeUtil.getTimeReord(currentrecordTime);
                            tv_time.setText(timeReord);
                        }
                    }

                    @Override
                    public void onStop(Object dataSource, AudioPlayerManager manager) {

                    }

                    @Override
                    public void onError(Object dataSource, AudioPlayerManager manager) {

                    }

                    @Override
                    public void onProgress(int progress, Object dataSource, AudioPlayerManager manager) {
                        String timeReord = TimeUtil.getTimeReord(progress);
                        tv_time.setText(timeReord);
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

    //开始播放
    private void startPlay() {
        if (audioPlayerManager == null) {
            initPlayer(recordPath);
        } else {
            audioPlayerManager.release();
        }
        isPlaying = true;
        audioPlayerManager.setDataSource(recordPath);
        audioPlayerManager.start();
        iv_play.setImageResource(R.drawable.zanting);
        tv_time.setText("");
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    //停止播放
    public void stopPlay() {
        isPlaying = false;
        if (audioPlayerManager != null) {
            audioPlayerManager.stop();
        }
        iv_play.setImageResource(R.drawable.bofangxiao);
        String timeReord = TimeUtil.getTimeReord(currentrecordTime);
        tv_time.setText(timeReord);
    }

    //初始化录音
    private void initRecorder() {
        Mp3RecorderUtil.init(activity, true);
        mp3Recorder = new Mp3Recorder();
        recordPath = ToolUtil.createFile(activity, System.currentTimeMillis() + "_study.mp3", dirName);
        mp3Recorder.setOutputFile(recordPath)
                .setMaxDuration(MAX_RECORD_TIME)
                .setCallback(new Mp3Recorder.Callback() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onPause() {

                    }

                    @Override
                    public void onResume() {

                    }

                    @Override
                    public void onStop(int i) {   //录制结束
                        if (currentrecordTime < MIN_RECORD_TIME) {
                            try {
                                ToolUtil.deleteDir(recordPath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            showLunState(RecordState.OVER);
                            ToastUtils.showToastSHORT(activity, "录制时间过短，未到1秒");
                        } else {
                            if (curretnRecordState == RecordState.OVER) {
                                if (listener != null) {
                                    listener.onConfig(recordPath);
                                }
                            }
                        }
                    }

                    @Override
                    public void onReset() {

                    }

                    @Override
                    public void onRecording(double v, double v1) {
                        currentrecordTime = (int) v;
                        String timeReord = TimeUtil.getTimeReord((int) v);
                        tv_time.setText(timeReord);
                    }

                    @Override
                    public void onMaxDurationReached() {
                        showLunState(RecordState.RECORDING);
                    }
                });
    }

    //获取文件路径
    public String getRecordPath() {
        return recordPath;
    }

    public RecordState getCurretnRecordState() {
        return curretnRecordState;
    }

    //当前录制时长
    public int getCurrentrecordTime() {
        return currentrecordTime;
    }

    //取消
    private void cancle() {
        curretnRecordState = RecordState.NORMAL;
        iv_luyin.setImageResource(R.drawable.luyin);
        iv_shenyin.setVisibility(VISIBLE);
        iv_play.setVisibility(GONE);
        iv_play.setImageResource(R.drawable.bofangxiao);
        try {
            ToolUtil.deleteDir(recordPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mp3Recorder != null) {
            mp3Recorder = null;
        }
        isPlaying = false;
        if (audioPlayerManager != null) {
            audioPlayerManager.stop();
        }
        tv_time.setText("");
    }

    public void onPause() {
        if (audioPlayerManager != null && isPlaying) {
            audioPlayerManager.pause();
        }
    }

    public void onResume() {
        if (audioPlayerManager != null && isPlaying) {
            audioPlayerManager.resume();
        }
    }

    //释放资源
    public void onDestroy() {
        if (audioPlayerManager != null) {
            audioPlayerManager.release();
            audioPlayerManager = null;
        }
        if (mp3Recorder != null) {
            mp3Recorder.stop(Mp3Recorder.ACTION_STOP_ONLY);
            mp3Recorder = null;
        }
    }

    /**
     * 录音按钮状态
     */
    public void showLunState(RecordState type) {
        if (type == RecordState.NORMAL) {//未录音
            curretnRecordState = RecordState.RECORDING;
            iv_luyin.setImageResource(R.drawable.zanting1);
            iv_shenyin.setVisibility(VISIBLE);
            iv_play.setVisibility(GONE);
            tv_time.setText("");
            initRecorder();
            if (mp3Recorder != null) {
                mp3Recorder.start();
            }
        } else if (type == RecordState.RECORDING) {//录音中
            curretnRecordState = RecordState.OVER;
            iv_luyin.setImageResource(R.drawable.shuangxin);
            iv_shenyin.setVisibility(GONE);
            iv_play.setVisibility(VISIBLE);
            iv_play.setImageResource(R.drawable.bofangxiao);
            if (mp3Recorder != null) {
                mp3Recorder.stop(Mp3Recorder.ACTION_STOP_ONLY);
            }
        } else if (type == RecordState.OVER) {//已完成
            curretnRecordState = RecordState.NORMAL;
            if (isPlaying) {
                stopPlay();
            }
            iv_luyin.setImageResource(R.drawable.luyin1);
            iv_shenyin.setVisibility(VISIBLE);
            iv_play.setVisibility(GONE);
            tv_time.setText("");
            recordPath = "";
        }
    }
}
