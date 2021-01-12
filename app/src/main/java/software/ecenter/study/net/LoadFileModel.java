package software.ecenter.study.net;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import software.ecenter.study.Interface.ProgressListener;

/**
 * Created by 12457 on 2017/8/21.
 */

public class LoadFileModel {
    private static String resouceId = "";

    public static void setResouceId(String resouceId) {
        LoadFileModel.resouceId = resouceId;
    }

    public static Call loadPdfFile(String url, Callback<ResponseBody> callback) {
        return loadPdfFile("", url, callback);
    }

    public static Call loadPdfFile(String curResourceId, String url, Callback<ResponseBody> callback) {
        if (!resouceId.equals(curResourceId))
            resouceId = curResourceId;
        else
            return null;

        Retrofit.Builder retrofit = new Retrofit.Builder()
                .baseUrl(RetroFactory.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        OkHttpClient.Builder builder = ProgressHelper.addProgress(null);

        LoadFileApi mLoadFileApi = retrofit.client(builder.build()).build().create(LoadFileApi.class);

        if (!TextUtils.isEmpty(url)) {
            Call<ResponseBody> call = mLoadFileApi.loadPdfFile(url);
            call.enqueue(callback);
            return call;
        }
        return null;
    }

    public static class ProgressResponseBody extends ResponseBody {
        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Nullable
        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    //当前读取字节数
                    long bytesRead = super.read(sink, byteCount);
                    //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    //回调，如果contentLength()不知道长度，会返回-1
                    progressListener.onProgress(totalBytesRead, responseBody.contentLength(), bytesRead, bytesRead == -1, resouceId);
                    return bytesRead;
                }
            };
        }
    }

    public static class ProgressHelper {
        private static ProgressBean progressBean = new ProgressBean();
        private static ProgressBean.ProgressHandler mProgressHandler;

        public static OkHttpClient.Builder addProgress(OkHttpClient.Builder builder) {
            if (builder == null) {
                builder = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS);
            }

            final ProgressListener progressListener = new ProgressListener() {
                @Override
                public void onProgress(long progress, long total, long speed, boolean done, String resourceId) {

                    if (mProgressHandler == null) {
                        return;
                    }

                    progressBean.setBytesRead(progress);
                    progressBean.setContentLength(total);
                    progressBean.setSpeed(speed);
                    progressBean.setDone(done);
                    mProgressHandler.sendMessage(progressBean);

                }
            };

            //添加拦截器，自定义ResponseBody，添加下载进度
            builder.networkInterceptors().add(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                    okhttp3.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder().body(
                            new ProgressResponseBody(originalResponse.body(), progressListener))
                            .build();

                }
            });
            return builder;
        }

        public static void setProgressHandler(ProgressBean.ProgressHandler progressHandler) {
            mProgressHandler = progressHandler;
        }
    }

    public static class ProgressBean {
        private long bytesRead;
        private long contentLength;
        private long speed;
        private boolean done;

        public long getSpeed() {
            return speed;
        }

        public void setSpeed(long speed) {
            this.speed = speed;
        }

        public long getBytesRead() {
            return bytesRead;
        }

        public void setBytesRead(long bytesRead) {
            this.bytesRead = bytesRead;
        }

        public long getContentLength() {
            return contentLength;
        }

        public void setContentLength(long contentLength) {
            this.contentLength = contentLength;
        }

        public boolean isDone() {
            return done;
        }

        public void setDone(boolean done) {
            this.done = done;
        }

        public static abstract class ProgressHandler {
            private static final int DOWNLOAD_PROGRESS = 1;

            protected abstract void onProgress(long progress, long total, long speed, boolean done, String resouceId);

            private final Handler mHandler = new UIHandler(Looper.getMainLooper(), this);

            protected void sendMessage(ProgressBean progressBean) {
                mHandler.obtainMessage(DOWNLOAD_PROGRESS, progressBean).sendToTarget();
            }

            static class UIHandler extends Handler {
                private final WeakReference<ProgressBean.ProgressHandler> mProgressHandler;

                public UIHandler(Looper looper, ProgressBean.ProgressHandler progressHandler) {
                    super(looper);
                    mProgressHandler = new WeakReference<ProgressBean.ProgressHandler>(progressHandler);
                }

                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case DOWNLOAD_PROGRESS:
                            ProgressBean.ProgressHandler progressHandler = mProgressHandler.get();
                            if (progressHandler != null) {
                                ProgressBean progressBean = (ProgressBean) msg.obj;
                                progressHandler.onProgress(progressBean.getBytesRead(), progressBean.getContentLength(), progressBean.getSpeed(), progressBean.isDone(), resouceId);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }


}
