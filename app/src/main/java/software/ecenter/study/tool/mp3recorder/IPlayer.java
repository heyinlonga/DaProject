package software.ecenter.study.tool.mp3recorder;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public interface IPlayer {



    //void init(Uri uri);
    void start();
    void start(Object dataSource);
    void pause();
    void resume();
    void stop();
    void seekTo(int duration);
    void release();

}
