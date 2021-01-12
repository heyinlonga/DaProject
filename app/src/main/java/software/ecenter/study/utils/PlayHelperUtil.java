package software.ecenter.study.utils;

import android.text.TextUtils;

import com.czt.mp3recorder.Mp3Recorder;

import software.ecenter.study.R;
import software.ecenter.study.tool.mp3recorder.AudioPlayerManager;
import software.ecenter.study.tool.mp3recorder.PlayerCallback;

/**
 * Message 播放帮助类
 * Create by Administrator
 * Create by 2019/11/15
 */
public class PlayHelperUtil {
    public static PlayHelperUtil mPlayHelperUtil;
    /**
     * 语音播放
     */
    private AudioPlayerManager audioPlayerManager;
    /**
     * 是否正在播放语音
     */
    boolean isPlaying;
    /**
     * 播放语音监听
     */
    PlayerCallback callback;
    String oldPath = "";

    public PlayHelperUtil() {
        audioPlayerManager = new AudioPlayerManager();
    }

    public static PlayHelperUtil newInrence() {
        if (mPlayHelperUtil == null) {
            mPlayHelperUtil = new PlayHelperUtil();
        }
        return mPlayHelperUtil;
    }

    public boolean playStatus() {
        return isPlaying;
    }

    //开始播放
    public void startPlay(String recordPath) {
        if (audioPlayerManager != null && !TextUtils.isEmpty(recordPath)) {
            initPlayer(recordPath);
            isPlaying = true;
            if (!TextUtils.isEmpty(oldPath)){
                if (recordPath.equals(oldPath)){
                    audioPlayerManager.release();
                    audioPlayerManager.setDataSource(recordPath);
                    audioPlayerManager.start();
                }else {
                    audioPlayerManager.release();
                    audioPlayerManager.start(recordPath);
                }
            }else {
                audioPlayerManager.setDataSource(recordPath);
                audioPlayerManager.start();
            }
            oldPath = recordPath;
        }
    }

    //停止播放
    public void stopPlay() {
        isPlaying = false;
        if (audioPlayerManager != null) {
            audioPlayerManager.stop();
        }
    }

    //继续播放
    private void resumePlay() {
        isPlaying = false;
        if (audioPlayerManager != null) {
            audioPlayerManager.resume();
        }
    }

    //暂停播放
    private void pausePlay() {
        isPlaying = false;
        if (audioPlayerManager != null) {
            audioPlayerManager.pause();
        }
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

    public void onDestroy() {
        if (audioPlayerManager != null) {
            audioPlayerManager.release();
            audioPlayerManager = null;
        }
        if (mPlayHelperUtil != null) {
            mPlayHelperUtil = null;
        }
        oldPath = "";
        callback = null;
    }
    //释放资源
    public void release() {
        if (audioPlayerManager != null) {
            audioPlayerManager.setDataSource(null);
            audioPlayerManager.release();
        }
        oldPath = "";
    }

    public void listener(PlayerCallback callback) {
        this.callback = callback;
    }

    //初始化播放
    private void initPlayer(String path) {
        audioPlayerManager.setDataSource(path)
                .setCallback(new PlayerCallback() {
                    @Override
                    public void onPreparing(Object dataSource, AudioPlayerManager manager) {
                        if (callback != null) {
                            callback.onPreparing(dataSource, manager);
                        }
                    }

                    @Override
                    public void onPlaying(Object dataSource, AudioPlayerManager manager) {
                        if (callback != null) {
                            callback.onPlaying(dataSource, manager);
                        }
                    }

                    @Override
                    public void onPause(Object dataSource, AudioPlayerManager manager) {
                        if (callback != null) {
                            callback.onPause(dataSource, manager);
                        }
                    }

                    @Override
                    public void onFinished(Object dataSource, AudioPlayerManager manager) {
                        isPlaying = false;
                        if (callback != null) {
                            callback.onFinished(dataSource, manager);
                        }
                    }

                    @Override
                    public void onStop(Object dataSource, AudioPlayerManager manager) {
                        if (callback != null) {
                            callback.onStop(dataSource, manager);
                        }
                    }

                    @Override
                    public void onError(Object dataSource, AudioPlayerManager manager) {
                        if (callback != null) {
                            callback.onError(dataSource, manager);
                        }
                    }

                    @Override
                    public void onProgress(int progress, Object dataSource, AudioPlayerManager manager) {
                        if (callback != null) {
                            callback.onProgress(progress, dataSource, manager);
                        }
                    }

                    @Override
                    public void onSeeking(Object dataSource, AudioPlayerManager manager) {
                        if (callback != null) {
                            callback.onSeeking(dataSource, manager);
                        }
                    }

                    @Override
                    public void onBufferingUpdate(int percent, AudioPlayerManager manager) {
                        if (callback != null) {
                            callback.onBufferingUpdate(percent, manager);
                        }
                    }

                    @Override
                    public void onGetMaxDuration(int maxDuration) {
                        if (callback != null) {
                            callback.onGetMaxDuration(maxDuration);
                        }
                    }
                });
    }


}
