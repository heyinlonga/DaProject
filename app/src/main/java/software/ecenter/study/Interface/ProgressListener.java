package software.ecenter.study.Interface;

/**
 * progress表示当前已经下载的文件大小
 * total表示文件大小
 * speed表示下载速度
 * done表示是否下载完成
 * Created by 1013369768 on 2017/10/20.
 */
public interface ProgressListener {
    void onProgress(long progress,long total,long speed,boolean done,String resourceId);
}