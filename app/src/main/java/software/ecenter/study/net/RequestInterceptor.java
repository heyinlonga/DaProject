package software.ecenter.study.net;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import software.ecenter.study.utils.AccountUtil;

/**
 * @desc: okhttp的请求拦截器
 */
public class RequestInterceptor implements Interceptor {
//    公共参数
/*    public static final String PUBLIC_ACCOUNT_ID = "accessMode";
    public static final String PUBLIC_TOKEN= "APP-ANDROID";*/


    private Context context;
    private boolean hasToken = true;

    public RequestInterceptor(Context context, boolean hasToken) {
        this.context = context;
        this.hasToken = hasToken;

    }

    public RequestInterceptor(Context context) {
        new RequestInterceptor(context, false);
    }
    public void setContext(Context context) {
        this.context = context;
    }
    public void setContext(Context context,boolean hastoken) {
        this.context = context;
        this.hasToken = hastoken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.d("okhttp",chain.request().url()+"");
        Log.d("okhttp",AccountUtil.getToken(context)+"    --Token");

        Request.Builder builder = chain.request().newBuilder();
        if (hasToken) {
            builder.addHeader("Authorization", "Bearer " + AccountUtil.getToken(context));//"87179b32-39ad-438a-b559-313ca76f6baa")
        }
//公共参加到头
        Request request = builder.build();

//post公共参数加到不同参数
   /*
         Request   request = chain.request()
                    .newBuilder()
                    .build();

       if (request.method().equals("POST")) {
            if (request.body() instanceof FormBody) {

                FormBody.Builder bodyBuilder = new FormBody.Builder();
                FormBody formBody = (FormBody) request.body();


                //添加公共参数

                formBody = bodyBuilder
                        .addEncoded(ConstUtils.USERID_KEY, userId)
                        .addEncoded(ConstUtils.TOKEN_KEY, token)
                        .addEncoded(ConstUtils.VERSION_KEY, version)
                        .build();


                request = request.newBuilder().post(formBody).build();
            }
        }
*/
        Response response = chain.proceed(request);

//        LogUtil.i("response:" + response.body());
        //注意:response.body().string()的string方法只可以调用一次,不然会报错!!!

        return response;
    }


    /* //加全局请求参数token
        HttpUrl httpUrl = request.url().newBuilder()
                .addQueryParameter("token", "tokenValue")
                .build();
        request = request.newBuilder().url(httpUrl).build();
        return chain.proceed(request);
        */
}
