package software.ecenter.study.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import software.ecenter.study.R;
import software.ecenter.study.View.SuperFileView2;
import software.ecenter.study.net.LoadFileModel;
import software.ecenter.study.tool.FileAccessor;
import software.ecenter.study.tool.FileManager;
import software.ecenter.study.tool.Md5Tool;

/***
 * dsc 文件展示   课程详情pdf 、doc、excel等等
 *
 * */
public class FileDisplayActivity extends BaseActivity {


    @BindView(R.id.mSuperFileView)
    SuperFileView2 mSuperFileView;

    private String filePath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_display);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        showNetWaitLoding();
        mSuperFileView = (SuperFileView2) findViewById(R.id.mSuperFileView);
        mSuperFileView.setOnGetFilePathListener(new SuperFileView2.OnGetFilePathListener() {
            @Override
            public void onGetFilePath(SuperFileView2 mSuperFileView2) {
                getFilePathAndShowFile(mSuperFileView2);
            }
        });

        Intent intent = this.getIntent();
        String path = (String) intent.getSerializableExtra("path");

        if (!TextUtils.isEmpty(path)) {
            Log.d(TAG, "文件path:" + path);
            setFilePath(path);
        }
        mSuperFileView.show();

    }

    /**
     * 获取需要展示的文件，并进行展示
     *
     * @param mSuperFileView2
     */
    private void getFilePathAndShowFile(SuperFileView2 mSuperFileView2) {


        if (getFilePath().contains("http")) {//网络地址要先下载
            downLoadFromNet(getFilePath(), mSuperFileView2);
        } else {
            mSuperFileView2.displayFile(new File(getFilePath()));
        }
    }

    public void setFilePath(String fileUrl) {
        this.filePath = fileUrl;
    }

    private String getFilePath() {
        return filePath;
    }


    private void downLoadFromNet(final String url, final SuperFileView2 mSuperFileView2) {
        Log.d(TAG, "创建缓存文件： " + url);
        //1.网络下载、存储路径、
        File cacheFile = getCacheFile(url);
        boolean exists = cacheFile.exists();
        if (cacheFile.exists()) {
            if (cacheFile.length() <= 0) {
                Log.d(TAG, "删除空文件！！");
                cacheFile.delete();
                return;
            } else {
                File fileN = getCacheFile(url);//new File(getCacheDir(url), getFileName(url))
                mSuperFileView2.displayFile(fileN);
                return;
            }
        }

        LoadFileModel.loadPdfFile(url, url, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "下载文件-->onResponse");
                boolean flag;
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    ResponseBody responseBody = response.body();
                    is = responseBody.byteStream();
                    long total = responseBody.contentLength();

                    File file1 = getCacheDir(url);
                    if (!file1.exists()) {
                        file1.mkdirs();
                        Log.d(TAG, "创建缓存目录： " + file1.toString());
                    }


                    //fileN : /storage/emulated/0/pdf/kauibao20170821040512.pdf
                    File fileN = getCacheFile(url);//new File(getCacheDir(url), getFileName(url))

                    Log.d(TAG, "创建缓存文件： " + fileN.toString());
                    if (!fileN.exists()) {
                        boolean mkdir = fileN.createNewFile();
                    }
                    fos = new FileOutputStream(fileN);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Log.d(TAG, "写入缓存文件" + fileN.getName() + "进度: " + progress);
                    }
                    fos.flush();
                    Log.d(TAG, "文件下载成功,准备展示文件。");
                    dismissNetWaitLoging();
                    //2.ACache记录文件的有效期
                    mSuperFileView2.displayFile(fileN);
                } catch (Exception e) {
                    Log.d(TAG, "文件下载异常 = " + e.toString());
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
                Log.d(TAG, "文件下载失败");
                File file = getCacheFile(url);
                if (!file.exists()) {
                    Log.d(TAG, "删除下载失败文件");
                    file.delete();
                }
            }
        });
    }

    /***
     * 获取缓存目录
     *
     * @param url
     * @return
     */
    private File getCacheDir(String url) {

        return new File(FileManager.getInstance(mContext).getTempDir() + File.separator + "documentTempDir");

    }

    /***
     * 绝对路径获取缓存文件
     *
     * @param url
     * @return
     */
    private File getCacheFile(String url) {
        String tex = "";
        if (!TextUtils.isEmpty(url)&&url.contains("?")){
            String[] split = url.split("\\?");
             tex = split[0];
        }
        if (TextUtils.isEmpty(tex)){
            tex = url;
        }
        File cacheFile = new File(FileManager.getInstance(mContext).getTempDir() + File.separator + "documentTempDir" + File.separator
                + getFileName(tex));
        Log.d(TAG, "缓存文件 = " + cacheFile.toString());
        return cacheFile;
    }

    /***
     * 根据链接获取文件名（带类型的），具有唯一性
     *
     * @param url
     * @return
     */
    private String getFileName(String url) {
        String fileName = Md5Tool.hashKey(url) + "." + getFileType(url);
        return fileName;
    }

    /***
     * 获取文件类型
     *
     * @param paramString
     * @return
     */
    private String getFileType(String paramString) {
        String str = "";
        String strdock = "";

        if (TextUtils.isEmpty(paramString)) {
            Log.d(TAG, "paramString---->null");
            return str;
        }
        Log.d(TAG, "paramString:" + paramString);
//        int j = paramString.lastIndexOf("?");
//
//        if (j <= -1) {
//            Log.d(TAG, "j <= -1");
//            return str;
//        }
//        strdock = paramString.substring(0, j);
        int i = paramString.lastIndexOf(".");
        if (i <= -1) {
            Log.d(TAG, "i <= -1");
            return str;
        }
        str = paramString.substring(i + 1);
//        str = paramString.substring(i + 1, j);
        Log.d(TAG, "paramString.substring(i + 1,j+1)------>" + str);
        return str;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSuperFileView != null) {
            mSuperFileView.onStopDisplay();
        }
        //清空缓存
        //   FileAccessor.deleteDir(FileManager.getInstance(mContext).getTempDir() + File.separator + "documentTempDir");

    }
}
