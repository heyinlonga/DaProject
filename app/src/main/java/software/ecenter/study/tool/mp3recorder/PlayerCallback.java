package software.ecenter.study.tool.mp3recorder;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public interface PlayerCallback {


    void onPreparing(Object dataSource, AudioPlayerManager manager);
    void onPlaying(Object dataSource, AudioPlayerManager manager);
    void onPause(Object dataSource, AudioPlayerManager manager);
    void onFinished(Object dataSource, AudioPlayerManager manager);
    void onStop(Object dataSource, AudioPlayerManager manager);
    void onError(Object dataSource, AudioPlayerManager manager);
    void onProgress(int progress, Object dataSource, AudioPlayerManager manager);
    void onSeeking(Object dataSource, AudioPlayerManager manager);
    void onBufferingUpdate(int percent, AudioPlayerManager manager);
    void onGetMaxDuration(int maxDuration);



}
