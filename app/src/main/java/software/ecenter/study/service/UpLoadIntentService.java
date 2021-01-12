package software.ecenter.study.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import software.ecenter.study.R;
import software.ecenter.study.StudyApplication;
import software.ecenter.study.activity.HomeActivity;
import software.ecenter.study.bean.HomeBean.ResourceBean;
import software.ecenter.study.bean.HomeBean.VersionBean;
import software.ecenter.study.net.LoadFileModel;
import software.ecenter.study.tool.FileAccessor;
import software.ecenter.study.tool.FileManager;
import software.ecenter.study.tool.JsonTool;

/**
 * Message  更新
 * Create by Administrator
 * Create by 2019/1/4
 */
public class UpLoadIntentService extends IntentService {
    //更新
    public static final String ACTION_UPLOAD = "uploadintentservice.action.upload";
    public static final String EXTRA_PARAM = "uploadintentservice.extra.param";
    public static final String RESULT_DOWN_PROGRESS = "uploadintentservice.result.down.progress";
    private String loadPath;//下载的文件路径
    private String version;
    private File apkFile;
    private ProgressDialog pd1;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * <p>
     * Used to name the worker thread, important only for debugging.
     */
    public UpLoadIntentService() {
        super("UpLoadIntentService");
    }

    //开启更新服务
    public static void startActionUpload(Context context, VersionBean bean) {
        Intent intent = new Intent(context, UpLoadIntentService.class);
        intent.setAction(ACTION_UPLOAD);
        intent.putExtra(EXTRA_PARAM, bean);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_UPLOAD.equals(action)) {
                VersionBean bean = (VersionBean) intent.getSerializableExtra(EXTRA_PARAM);
                VersionBean.Data data = bean.getData();
                if (data != null) {
                    String url = data.getUrl();
                    version = data.getVersion();
                    loadPath = FileManager.getInstance(this).getLoadapkDir();
                    upLoadVersion(url);
                }
            }
        }
    }
    private void showPro(){
        pd1 = new ProgressDialog(this);
        pd1.setTitle(getString(R.string.app_name));
        pd1.setIcon(R.mipmap.ic_launcher);
        pd1.setMessage("正在下载...");
        pd1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd1.setCancelable(true);
        pd1.setIndeterminate(true);
        pd1.setMax(100);
        pd1.show();
    }
    //下载
    private void upLoadVersion(String url) {

        LoadFileModel.loadPdfFile("upload", url, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                boolean flag;
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    ResponseBody responseBody = response.body();
                    is = responseBody.byteStream();
                    long total = responseBody.contentLength();
                    int count = 0;
                    File file1 = new File(loadPath);
                    if (!file1.exists()) {
                        file1.mkdirs();
                    }

                    //下载文件
                    apkFile = new File(loadPath, FileAccessor.getFileName(version) + "apk");

                    //创建文件
                    if (apkFile.exists()) {
                        apkFile.delete();//删除文件
                        apkFile.createNewFile();
                    }
                    fos = new FileOutputStream(apkFile);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        //写入缓存文件
                        fos.write(buf, 0, len);
                        sum += len;
                        float pro = sum * 1.0f / total;
                        //更新进度
                        downloadOneOk((int) (pro * 100 ));
                    }
                    fos.flush();
                    //下载成功
                    anZhuanApp();
                } catch (Exception e) {
                    //文件下载异常
                    File file = new File(loadPath);
                    if (file.exists()) {
                        //删除资源文件夹;
                        FileAccessor.deleteDir(loadPath);
                    }
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //文件下载失败
                File file = new File(loadPath);
                if (file.exists()) {
                    //删除资源文件夹;
                    FileAccessor.deleteDir(loadPath);
                }
            }
        });
    }

    public void downloadOneOk(int progress) {
        Intent intent = new Intent(RESULT_DOWN_PROGRESS);
        intent.putExtra("progress", "" + progress);
        sendBroadcast(intent);
    }

    /**
     * 生成uri
     *
     * @param cameraFile
     * @return
     */
    private Uri parUri(File cameraFile) {
        Uri imageUri;
        String authority = getPackageName() + ".provider";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(UpLoadIntentService.this, authority, cameraFile);
        } else {
            imageUri = Uri.fromFile(cameraFile);
        }
        return imageUri;
    }

    private void anZhuanApp() {
        Log.d("update", "下载成功");
        Uri uri = parUri(apkFile);

        //安装程序
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }else {
            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        installIntent.setDataAndType(uri,
                "application/vnd.android.package-archive");
        //启动安装程序
        UpLoadIntentService.this.startActivity(installIntent);
        stopSelf();

    }

    /**
     * 获取下载进度
     *
     * @param downSize
     * @param allSize
     * @return
     */
    public static String getMsgSpeed(long downSize, long allSize) {
        StringBuffer sBuf = new StringBuffer();
        sBuf.append(getSize(downSize));
        sBuf.append("/");
        sBuf.append(getSize(allSize));
        sBuf.append(" ");
        sBuf.append(getPercentSize(downSize, allSize));
        return sBuf.toString();
    }

    // BT字节参考量
    private static final float SIZE_BT = 1024L;
    // KB字节参考量
    private static final float SIZE_KB = SIZE_BT * 1024.0f;
    // MB字节参考量
    private static final float SIZE_MB = SIZE_KB * 1024.0f;

    /**
     * 获取大小
     *
     * @param size
     * @return
     */
    public static String getSize(long size) {
        if (size >= 0 && size < SIZE_BT) {
            return (double) (Math.round(size * 10) / 10.0) + "B";
        } else if (size >= SIZE_BT && size < SIZE_KB) {
            return (double) (Math.round((size / SIZE_BT) * 10) / 10.0) + "KB";
        } else if (size >= SIZE_KB && size < SIZE_MB) {
            return (double) (Math.round((size / SIZE_KB) * 10) / 10.0) + "MB";
        }
        return "";
    }

    /**
     * 获取到当前的下载百分比
     *
     * @param downSize 下载大小
     * @param allSize  总共大小
     * @return
     */
    public static String getPercentSize(long downSize, long allSize) {
        String percent = (allSize == 0 ? "0.0" : new DecimalFormat("0.0")
                .format((double) downSize / (double) allSize * 100));
        return "(" + percent + "%)";
    }

}
