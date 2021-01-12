package software.ecenter.study.net;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import software.ecenter.study.activity.LogingActivity;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.ToastUtils;


/**类
 * @desc: 网络请求封装
 */
public class RetroFactory {

    public final String TAG = "okhttp";

    //根网址
    public static final String BASE_URL = "https://xzykt.longmenshuju.com/";//客户正式线上
//    public static final String BASE_URL = "http://47.95.1.161:81/";//
//    public static final String BASE_URL = "http://47.95.1.161:9016/";//测试服务器  13800138006
//    public static final String BASE_URL = "http://47.94.235.55:8080/";//伪正式线上  15175243021   123qwe

    //auth
    public static final String AUTH_BASE_URL = "http://101.200.43.126:8401/";

    public static final String RESULT_CODE = "status";
    public static final String RESULT_MESSAGE = "message";
    public static final String RESULT_CODE_OK = "1";
    public static final String RESULT_CODE_ERROR = "-1";

    public static final int ERROR_1 = 0;//正常服务器返回错误
    public static final int ERROR_2 = 1;//服务器返回值错误
    public static final int ERROR_3 = 2;//服务器 404 500
    public static final int ERROR_4 = 3;//当发生网络异常时，调用服务器或意外时调用

    private static StudyAPI studyAPI = null;
    private static StudyAPI authStudyAPI = null;
    protected static final Object monitor = new Object();

    private Call<String> mCall;

    private Context mContext;
    private static Dialog dialog;
    private static RetroInit retroInit;
    private static RetroInit authInit;


    public static StudyAPI getRetroFactory(Context context) {
        return getRetroFactory(context, true);
    }

    public static StudyAPI  getRetroFactory(Context context, boolean hasToken) {
        synchronized (monitor) {
            if (retroInit == null) {
                retroInit = new RetroInit(context, hasToken);
            } else {
                retroInit.setContext(context, hasToken);

            }
            if (studyAPI == null) {
                studyAPI = retroInit.getApi();
            }
            return studyAPI;
        }
    }

    public static StudyAPI getAuthRetroFactory(Context context) {
        synchronized (monitor) {
            if (authInit == null) {
                authInit = new RetroInit(context, true, false);
            } else {
                authInit.setContext(context);
            }
            if (authStudyAPI == null) {
                authStudyAPI = authInit.getAuthStudyAPI();
            }
            return authStudyAPI;
        }

    }

    /**
     * 回调封装
     */
    public RetroFactory(Context context, Call call) {
//        Log.d("okhttp", "Bearer " + AccountUtil.getToken(context));
        mCall = call;
        mContext = context;
    }

    public interface ResponseListener<String> {
        void onSuccess(String response);

        void onFail(int type, String msg);
    }

    public Call<String> handleResponse(final ResponseListener listener) {

        mCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful() && response.errorBody() == null) {
                    try {
                        Log.i(TAG, response.body());
                        JSONObject jsonObject = new JSONObject(response.body());
                        String rescode = jsonObject.getString(RESULT_CODE);
                        if (RESULT_CODE_OK.equals(rescode)) {
                            //请求成功
                            listener.onSuccess(response.body());
                        } else if (RESULT_CODE_ERROR.equals(rescode)) {
                            String resmsg = jsonObject.getString(RESULT_MESSAGE);
                            listener.onFail(ERROR_1, resmsg);
                        } else {
                            String resmsg = jsonObject.getString(RESULT_MESSAGE);
                            //请求成功,错误信息
                            ToastUtils.showToastSHORT(mContext, resmsg);
                            //其他状态码
                            listener.onFail(ERROR_1, response.body());
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                        Log.i(TAG, "error : JSONException");
                        listener.onFail(ERROR_2, "解析错误");
                    }
                } else {
                    //服务器原因错误
                    Log.i(TAG, "error message:" + response.code() + response.message());

                    //显示错误提示布局，不吐司
//                        Toast.makeText(mContext, "网络请求返回异常！", Toast.LENGTH_LONG).show();

                    if (response.code() == 401){
                        Intent intent = new Intent(mContext,LogingActivity.class);
                        AccountUtil.clearAccountInfo(mContext);
                        AccountUtil.clearAccountToken(mContext);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
                    listener.onFail(ERROR_3, "错误" + response.code());//不管是服务器还是本地原因,如果是在下拉或加载更多时候,都要关闭刷新和下拉
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (call.isCanceled()) {
                    Log.i(TAG, "request:isCanceled");
                } else {
                    Log.i(TAG, "neterror:" + t.getMessage());
//                    listener.onFail(ERROR_4, t.getMessage());
                    listener.onFail(ERROR_4, "似乎已断开与互联网的链接。");
                }

            }

        });

        return mCall;
    }

    public Call<String> handleAuthResponse(final ResponseListener listener) {

        mCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful() && response.errorBody() == null) {
                    //请求成功
                    Log.i(TAG, response.body());
                    listener.onSuccess(response.body());

                } else {
                    //服务器原因错误
                    Log.i(TAG, "error message:" + response.code() + response.message());

                    //显示错误提示布局，不吐司
//                        Toast.makeText(mContext, "网络请求返回异常！", Toast.LENGTH_LONG).show();

                    listener.onFail(ERROR_3, "错误" + response.code());//不管是服务器还是本地原因,如果是在下拉或加载更多时候,都要关闭刷新和下拉
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (call.isCanceled()) {
                    Log.i(TAG, "request:isCanceled");
                } else {
                    Log.i(TAG, "neterror:" + t.getMessage());
                    listener.onFail(ERROR_4, t.getMessage());
                }

            }

        });

        return mCall;
    }

    /**
     * 取消请求
     */
    public void cancleRequest() {
        if (mCall != null && mCall.isExecuted()) {
            mCall.cancel();
        }
    }


    /**
     * 上传文件需要的方法
     */

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    /**
     * 创建文件描述
     *
     * @param descriptionString
     * @return
     */
    @NonNull
    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

    /**
     * 创建上传的文件类型
     *
     * @param partName
     * @param file
     * @return
     */
    @NonNull
    public static MultipartBody.Part prepareFilePart(String partName, File file) {

        // 为file建立RequestBody实例
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);

        // MultipartBody.Part借助文件名完成最终的上传
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

}
