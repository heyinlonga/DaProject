package software.ecenter.study.activity;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.czt.mp3recorder.AudioNoPermissionEvent;
import com.czt.mp3recorder.Mp3Recorder;
import com.czt.mp3recorder.Mp3RecorderUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import software.ecenter.study.Adapter.ImageAdapter;
import software.ecenter.study.Interface.ConstantData;
import software.ecenter.study.View.LoadingDialog;
import software.ecenter.study.View.SpinnerPopWindow;
import software.ecenter.study.bean.ImageBean;
import software.ecenter.study.tool.FileAccessor;
import software.ecenter.study.tool.FileManager;
import software.ecenter.study.tool.ImageTool;
import software.ecenter.study.tool.mp3recorder.AudioPlayerManager;
import software.ecenter.study.tool.mp3recorder.PlayerCallback;
import software.ecenter.study.tool.mp3recorder.ProxyTools;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;


/**
 * 需要拍照的要继承此父类   抽象类
 */
public abstract class BasePicActivity extends BaseActivity implements ConstantData {

    public SpinnerPopWindow spinnerPopWindow;
    private List<String> spinnerList;

    //相机
    public final int REQUEST_PIC_CARMER = 1;
    //相册
    public final int REQUEST_PHOTO_PHOTO = 2;

    //图片处理完毕
    public final int MSG_IAMGE_FINISHED = 21;
    public LoadingDialog mWaitDialog;

    protected String myTempFilePath;//随机图片名称

    public static List<ImageBean> listImageData;
    public ImageAdapter imageAdapter;
    public int MAX_IMAGE_COUNT = 5;


    //音频
    public Mp3Recorder mRecorder;
    public String tempRecorderPath;
    public boolean canRecorder;//是否有录音权限
    public AudioPlayerManager audioPlayerManager;//音频播放

    public boolean hasRecording;//有录音
    public boolean isPlaying;//播放
    public boolean isThouchModee;//触摸模式 解决 onclick 跟onthouch 冲突
    public int recordingSecond;//录音时长
    public final int minRecordingSecond = 1;//最小时长
    public final int MSG_AUDIO_FINISHED = 22;
    public final int MSG_AUDIO_STOP = 23;
    public boolean isRecording = false;//是否正在录制中
    public boolean isPause = false; //是否暂停录制

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_IAMGE_FINISHED:
                    thumImageOK(msg.arg1, String.valueOf(msg.obj));
                    break;

                case MSG_AUDIO_FINISHED:
                    StopProcess();
                    break;
                case MSG_AUDIO_STOP:
                    stopRecording();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listImageData = new ArrayList<>();
        ImageBean item = new ImageBean();
        item.setAddImage(true);
        listImageData.add(item);
        //宽高
  /*      WindowManager manager = getActivity().getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
*/
        // 照片
        spinnerList = ToolUtil.StringToArray(PhonePhoto, ",");
        spinnerPopWindow = new SpinnerPopWindow(mContext);
        //spinnerPopWindow.setHeight(outMetrics.heightPixels * 4/10);
        spinnerPopWindow.refreshData(spinnerList);
        spinnerPopWindow.setPopTitle("照片");
        spinnerPopWindow.setItemSelectListener(new SpinnerPopWindow.MItemSelectListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0: //相机
                        startCamera();
                        break;

                    case 1: //相册
                        startPhoto();
                        break;
                }

            }
        });

        mWaitDialog = new LoadingDialog(mContext);
        mWaitDialog.setCancelable(false);
    }


    public void showWaitLoding() {
        if (!mWaitDialog.isShowing()) {
            mWaitDialog.show();
        }
    }

    public void dismissWaitLoging() {
        if (mWaitDialog.isShowing()) {
            if (!this.isFinishing())
                mWaitDialog.dismiss();
        }
    }

    //相机
    protected void startCamera() {
        if (spinnerPopWindow.isShowing()) {
            spinnerPopWindow.dismiss();
        }
        // 指定相机拍摄照片保存地址

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent();
            // 指定开启系统相机的Action
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            if (!outDir.exists()) {
                outDir.mkdirs();
            }

            //如果文件目录存在，那么就指定拍照路径
            File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
            myTempFilePath = outFile.getAbsolutePath();

            // 把文件地址转换成Uri格式
            Uri uri = ToolUtil.parUri(mContext,outFile);

            // 设置系统相机拍摄照片完成后图片文件的存放地址
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            // 此值在最低质量最小文件尺寸时是0，在最高质量最大文件尺寸时是１
            //intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            //Log.d(TAG, "指定了路径:" + myPicFilePath);

            startActivityForResult(intent, REQUEST_PIC_CARMER);

        } else {
            ToastUtils.showToastSHORT(mContext, "请确认已经插入SD卡");
        }

    }

    //相册选择
    protected void startPhoto() {
        if (spinnerPopWindow.isShowing()) {
            spinnerPopWindow.dismiss();
        }

		/*		Intent intent = new Intent();
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, CurRequestPhoto);*/
        Intent openphotoIntent = new Intent(Intent.ACTION_PICK);
        openphotoIntent.setType("image/*");
        startActivityForResult(openphotoIntent, REQUEST_PHOTO_PHOTO);
    }

    public void thumImageOK(int requestCode, String targetPicPath) {
        dismissWaitLoging();
        Bitmap mCurThumBitmap = null; //缩略图（用来展示图片）
        //拍照
        if (requestCode == REQUEST_PIC_CARMER) {
            if (targetPicPath != null) {
                mCurThumBitmap = ImageTool.decodeBitmap(targetPicPath);
            }
            if (mCurThumBitmap == null) {
                dismissWaitLoging();
                ToastUtils.showToastSHORT(mContext, "照片获取失败");
                return;
            }
            ImageBean item = new ImageBean();
            item.setTargetPicPath(targetPicPath);
            item.setThumBitmap(null);
            item.setAddImage(false);
            if (listImageData.size() > 0) {
                listImageData.add(listImageData.size() - 1, item);//添加到默认添加图的前一位
            }
            if (listImageData.size() == MAX_IMAGE_COUNT + 1) { //最多5个图片，如果够了，就去掉添加按钮图片
                listImageData.remove(MAX_IMAGE_COUNT);
            }
            imageAdapter.refreshData(listImageData);
            dismissWaitLoging();
            //相册
        } else if (requestCode == REQUEST_PHOTO_PHOTO) {
            if (targetPicPath != null) {
                mCurThumBitmap = ImageTool.decodeBitmap(targetPicPath);
            }
            if (mCurThumBitmap == null) {
                dismissWaitLoging();
                ToastUtils.showToastSHORT(mContext, "照片获取失败");
                return;
            }

            ImageBean item = new ImageBean();
            item.setTargetPicPath(targetPicPath);
            item.setThumBitmap(null);
            item.setAddImage(false);
            if (listImageData.size() > 0) {
                listImageData.add(listImageData.size() - 1, item);//添加到默认添加图的前一位
            }
            if (listImageData.size() == MAX_IMAGE_COUNT + 1) { //最多5个图片，如果够了，就去掉添加按钮图片
                listImageData.remove(MAX_IMAGE_COUNT);
            }
            imageAdapter.refreshData(listImageData);
            dismissWaitLoging();

        }
    }

    public void clearImageData() {
        listImageData.clear();
        ImageBean item = new ImageBean();
        item.setAddImage(true);
        listImageData.add(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //指定路径拍照的话，返回 intent 为null
            //开启线程处理 图片结果，防止应用无响应
            showWaitLoding();
            //ToastUtils.showToastSHORT(mContext,"正在处理图片");
            MyThumRunnable run = new MyThumRunnable(requestCode, data);
            new Thread(run).start();

        }
    }

    /**
     * 用来生成压缩图片的耗时操作
     */
    class MyThumRunnable implements Runnable {
        private int requestCode;
        private Intent data;

        public MyThumRunnable(int requestCode, Intent data) {
            this.requestCode = requestCode;
            this.data = data;
        }

        @Override
        public void run() {

            String TargetPicPath = null;
            Message msg = mHandler.obtainMessage();

            //拍照
            if (requestCode == REQUEST_PIC_CARMER) {
                TargetPicPath = ImageTool.getThumPicPath(mContext, myTempFilePath, null);

                //相册
            } else if (requestCode == REQUEST_PHOTO_PHOTO) {
                try {
                    if (data != null) {
                        Uri uri = data.getData();
                        String absolutePath = getAbsolutePath(mContext, uri);

                        TargetPicPath = ImageTool.getThumPicPath(mContext, absolutePath, myTempFilePath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            msg.what = MSG_IAMGE_FINISHED;
            msg.arg1 = requestCode;
            msg.obj = TargetPicPath;
            mHandler.sendMessage(msg);

        }
    }

    public String getAbsolutePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    //音频 -------------------------------------------
    public void startRecording() {
        askPermission();
        if (canRecorder) {
            hasRecording = false;
            isRecording = true;
            mRecorder.start();
        }
    }

    public void pauseRecording() {
        if (isRecording) {
            isPause = true;
            mRecorder.pause();
        }
    }

    public void resumeRecording() {
        if (isRecording) {
            isPause = false;
            mRecorder.resume();
        }
    }


    public void stopActionRecording() {
        showWaitLoding();//防止连续点击
        mHandler.sendEmptyMessageDelayed(MSG_AUDIO_STOP, 1000); //防止快速点击，起码录制一秒
        hasRecording = true;
    }

    public void stopRecording() {
        mRecorder.stop(Mp3Recorder.ACTION_STOP_ONLY);

    }

    public void startplayRecording() {
        if (audioPlayerManager == null) {
            initPlayer(tempRecorderPath);
        } else {
            audioPlayerManager.release();
        }

        isPlaying = true;
        audioPlayerManager.start();
    }

    public void stopPlayRecording() {
        isPlaying = false;
        audioPlayerManager.stop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AudioNoPermissionEvent event) {
        ToastUtils.showToastSHORT(mContext, "没有权限,赶紧去设置吧");
    }

    private void askPermission() {
        new RxPermissions(this)
                .request(Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            canRecorder = true;
                            initRecorder();
                        } else {
                            canRecorder = false;
                            ToastUtils.showToastSHORT(mContext, "权限被拒绝了,无法录音");

                        }
                    }
                });
    }

    private void initRecorder() {
        Mp3RecorderUtil.init(mContext, true);
        mRecorder = new Mp3Recorder();
        tempRecorderPath = FileManager.getInstance(mContext).createTempRecordingPath();
        mRecorder.setOutputFile(tempRecorderPath)
                .setMaxDuration(59)//60s
                .setCallback(new Mp3Recorder.Callback() {
                    @Override
                    public void onRecording(double duration, double volume) {
                     /*   String str = "";
                        str = String.format("duration:\n" + "%d分%d秒", (int) (duration / 1000 / 60), (int) (duration / 1000 % 60)) + "---" + duration + "\n"
                                + "分贝值:\n" + volume;*/
                        if (!hasRecording) { //因为stop后此方法还会调用，会覆盖掉stop中的 recordingFinished的文字
                            isRecording = true;
                            recordingSecond = (int) (duration / 1000 % 60);
                            curRecordingSecond(recordingSecond);
                        }
                    }

                    @Override
                    public void onStart() {
                        // ToastUtils.showToastSHORT(mContext, "开始录音");

                    }

                    @Override
                    public void onPause() {
                        //ToastUtils.showToastSHORT(mContext,"暂停了....");
                    }

                    @Override
                    public void onResume() {
                        // ToastUtils.showToastSHORT(mContext,"恢复....");

                    }

                    @Override
                    public void onStop(int action) {
                        //ToastUtils.showToastSHORT(mContext,"onStop....");
                        mHandler.sendEmptyMessageDelayed(MSG_AUDIO_FINISHED, 500);//延迟500毫秒是因为DOWN事件会在UP后再执行一次
                    }

                    @Override
                    public void onReset() {
                        //ToastUtils.showToastSHORT(mContext,"onReset....");

                    }

                    @Override
                    public void onMaxDurationReached() {
                        // ToastUtils.showToastSHORT(mContext,"onMaxDurationReached....");
                        isRecording = false;
                        recordingFinished();
                        ToastUtils.showToastSHORT(mContext, "录音结束....");
                    }
                });

        //mRecorder.start();
    }

    //录音完成后处理
    public void StopProcess() {
        if (recordingSecond < minRecordingSecond) {
            try {
                isRecording = false;
                FileAccessor.deleteFile(tempRecorderPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            recordingFinishedTooMin();
            tempRecorderPath = null;
            recordingSecond = 0;
            hasRecording = false;
            isRecording = false;
            isThouchModee = false;


            ToastUtils.showToastSHORT(mContext, "录音时间过短,未到1秒....");
        } else {
//            ToastUtils.showToastSHORT(mContext, "录音结束....");
            isRecording = false;
            recordingFinished();
        }
        dismissWaitLoging();
    }

    public void clearAudio() {
        try {
            if (tempRecorderPath != null) {
                FileAccessor.deleteFile(tempRecorderPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        recordingFinishedTooMin();
        tempRecorderPath = null;
        recordingSecond = 0;
        hasRecording = false;
        isThouchModee = false;


    }

    private void initPlayer(String path) {
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
                        playFinished();
                    }

                    @Override
                    public void onStop(Object dataSource, AudioPlayerManager manager) {
                        playFinished();
                    }

                    @Override
                    public void onError(Object dataSource, AudioPlayerManager manager) {

                    }

                    @Override
                    public void onProgress(int progress, Object dataSource, AudioPlayerManager manager) {

                        curPlaySecond((int) (progress / 1000 % 60));
                        // String.format("%d:%02d",progress/1000/60,progress/1000));

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


    public void curRecordingSecond(int second) {
    }

    ;

    public void recordingFinished() {

    }

    ;

    public void recordingFinishedTooMin() {
    }

    ;

    public void curPlaySecond(int second) {
    }

    ;

    public void playFinished() {
    }

    ;

    @Override
    public void onDestroy() {
        if (audioPlayerManager != null) {
            audioPlayerManager.release();
        }
        if (mRecorder != null) {
            mRecorder.stop(Mp3Recorder.ACTION_STOP_ONLY);
            mRecorder = null;
        }
        super.onDestroy();
    }

}
