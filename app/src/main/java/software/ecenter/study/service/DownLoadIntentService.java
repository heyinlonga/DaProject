package software.ecenter.study.service;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import software.ecenter.study.bean.HomeBean.ResourceBean;
import software.ecenter.study.net.LoadFileModel;
import software.ecenter.study.tool.FileAccessor;
import software.ecenter.study.tool.FileManager;
import software.ecenter.study.tool.JsonTool;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class DownLoadIntentService extends IntentService {
    private static final String ACTION_DOWN = "software.ecenter.study.service.action.down";
    public static final String RESULT_DOWN = "software.ecenter.study.service.result.down";
    public static final String RESULT_DOWNING = "software.ecenter.study.service.result.downing";
    public static final String RESULT_DOWN_ONE = "software.ecenter.study.service.result.downOne";
    private static final String EXTRA_PARAM = "software.ecenter.study.service.extra.listresource";

    private static final String ACTION_DELETE = "software.ecenter.study.service.action.delete";
    public static final String RESULT_DELETE = "software.ecenter.study.service.result.delete";

    // Task任务集合
    public static List<ResourceBean> allTask = new ArrayList<ResourceBean>();


    public int numSize;
    public int errorSize;
    public ArrayList<String> errId;
    public ResourceBean mCurTask;
    //public boolean suspend; //暂停下载
    public Call curCall;

    public DownLoadIntentService() {
        super("DownLoadIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionDown(Context context, List<ResourceBean> list) {
        Intent intent = new Intent(context, DownLoadIntentService.class);
        intent.setAction(ACTION_DOWN);
        intent.putExtra(EXTRA_PARAM, (Serializable) list);
        context.startService(intent);
    }

    public static void startActionDelete(Context context, List<ResourceBean> list) {
        Intent intent = new Intent(context, DownLoadIntentService.class);
        intent.setAction(ACTION_DELETE);
        intent.putExtra(EXTRA_PARAM, (Serializable) list);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWN.equals(action)) {
                List<ResourceBean> list = (List<ResourceBean>) intent.getSerializableExtra(EXTRA_PARAM);
                handleActionFoo(list);
            } else if (ACTION_DELETE.equals(action)) {
                List<ResourceBean> list = (List<ResourceBean>) intent.getSerializableExtra(EXTRA_PARAM);
                handleActionDelete(list);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(List<ResourceBean> list) {
        //资源归属的路径
        for (ResourceBean bean : list) {
            bean.setLocalPathDir(FileManager.getInstance(this).getDocumentDir() + File.separator + bean.getResourceId());
            allTask.add(bean);
        }
        numSize = allTask.size();
        errorSize = 0;
        errId = new ArrayList<>();
        //开始下载
        Log.e(DownLoadIntentService.class.getSimpleName(), "onStart");
        if (mCurTask == null)
            toStart();
        else if (allTask.size() == 1)
            toStart();
    }

    public void toStart() {
        if (allTask.size() > 0) {
            //做任务
            mCurTask = allTask.get(0);
            if (mCurTask.isWillDelete()) {
                allTask.remove(mCurTask);
                toStart();
            } else {
                doTask();
            }
        } else {
            downloadAllOk();
        }

    }
    //下载全部成功
    public void downloadAllOk() {
        Intent intent = new Intent(RESULT_DOWN);
        // 此处传送的数据的是集合类型，也可以有其他的类型：intent.put
        intent.putExtra("down_num", "" + numSize);
        intent.putExtra("down_error_num", "" + errorSize);
        intent.putStringArrayListExtra("error_id", errId);
        sendBroadcast(intent);
        numSize = 0;
        errorSize = 0;
        LoadFileModel.setResouceId("");
    }

    //下载一个成功
    public void downloadOneOk(String resourcesId, ResourceBean mCurTask) {
        Intent intent = new Intent(RESULT_DOWN_ONE);
        intent.putExtra("resourcesId", "" + resourcesId);
        intent.putExtra("data", mCurTask);
        sendBroadcast(intent);
    }


    public void doTask() {
        if (mCurTask == null) {
            return;
        }
        Log.e(DownLoadIntentService.class.getSimpleName(), "download_curCall-->");
        curCall = LoadFileModel.loadPdfFile(mCurTask.getResourceId(), mCurTask.getResourceUrl(), new Callback<ResponseBody>() {
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

                    File file1 = new File(mCurTask.getLocalPathDir());
                    if (!file1.exists()) {
                        file1.mkdirs();
                    }


                    //下载文件
                    File fileN = new File(mCurTask.getLocalPathDir(), FileAccessor.getFileName(mCurTask.getResourceId()));

                    //创建文件
                    if (fileN.exists()) {
                        fileN.delete();//删除文件
                        fileN.createNewFile();
                    }
                    fos = new FileOutputStream(fileN);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        //写入缓存文件
                        fos.write(buf, 0, len);
                        sum += len;
                        //任务正在执行中，要删除，跳出写入  （标注一）
                        if (mCurTask.isWillDelete()) {
                            break;
                        }
                    }
                    fos.flush();

                    //第二步，创建信息
                    mCurTask.setDownloadOk(true); //标记下载完成
                    String resourceInfo = mCurTask.getLocalPathDir() + File.separator + "resource.info";
                    if (!FileAccessor.exists(resourceInfo)) {//不存在
                        FileAccessor.WriteStringToFile(resourceInfo, JsonTool.resourceBeanToJson(mCurTask));
                    }
                    if (mCurTask.isWillDelete()) { //任务正在执行中，上面（标注一）已经停止写入，这里删除文件
                        if (fileN.exists()) {
                            //删除资源文件夹;
                            FileAccessor.deleteDir(mCurTask.getLocalPathDir());
                        }
                    }
                    allTask.remove(mCurTask);//任务完成，移除队列
                    downloadOneOk(mCurTask.getResourceId(),mCurTask);
                    toStart();//继续下一个任务
                } catch (Exception e) {
                    //文件下载异常

                    File file = new File(mCurTask.getLocalPathDir());
                    if (file.exists()) {
                        //删除资源文件夹;
                        FileAccessor.deleteDir(mCurTask.getLocalPathDir());
                    }
                    allTask.remove(mCurTask);//任务失败，移除队列
                    curCall.cancel();
                    toStart();//继续下一个任务
                    errorSize++;
                    errId.add(mCurTask.getResourceId());
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
                File file = new File(mCurTask.getLocalPathDir());
                if (file.exists()) {
                    //删除资源文件夹;
                    FileAccessor.deleteDir(mCurTask.getLocalPathDir());
                }
                allTask.remove(mCurTask);//任务失败，移除队列
                errorSize++;
                errId.add(mCurTask.getResourceId());
                toStart();//继续下一个任务
            }
        });
        if (curCall == null){
            allTask.remove(mCurTask);//任务失败，移除队列
            errorSize++;
            errId.add(mCurTask.getResourceId());
            toStart();//继续下一个任务
        }
    }


    private void handleActionDelete(List<ResourceBean> list) {
        for (ResourceBean item : list) {//删除本地资源
            for (ResourceBean task : allTask) {//准备移除队列
                if (task.getResourceId().equals(item.getResourceId())) {
                    task.setWillDelete(true);
                }
            }

            if (mCurTask != null) {
                if (mCurTask.getResourceId().equals(item.getResourceId())) {//取消当前任务
                    curCall.cancel(); //去掉后，会回调 onFailure
                }
            }

            for (ResourceBean loacl : FileManager.LocalResourceList) {//下载好的本地删除
                if (loacl.getResourceId().equals(item.getResourceId())) {
                    FileAccessor.deleteDir(loacl.getLocalPathDir());

                    if (mCurTask != null && mCurTask.getResourceId().equals(item.getResourceId()))
                        mCurTask = null;
                }
            }
        }
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (allTask.size() == 0) {
                break;
            }
            boolean delete = false;
            for (ResourceBean task : allTask) {
                if (task.isWillDelete()) {
                    delete = task.isWillDelete();//有任何一个要删除的
                }
            }
            if (!delete) {
                break;  //全部都删除了，跳出循环等待，发送消息
            }
        }
        Intent intent = new Intent(RESULT_DELETE);
        // 此处传送的数据的是集合类型，也可以有其他的类型：intent.put
        intent.putExtra("delete_num", "" + list.size());
        intent.putExtra("data", (Serializable) list);
        sendBroadcast(intent);
    }

}

