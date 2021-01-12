package software.ecenter.study.net;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @desc: 初始化Retrofit配置
 * @auth: 哎哎
 * @time: 2016/9/12
 * @fixer:
 * @fixtime:
 * @fixdesc:
 * @remarks:
 */
public class RetroInit {

    private StudyAPI studyAPI;
    private StudyAPI authStudyAPI;
    private final RequestInterceptor requestInterceptor;

    RetroInit(Context context, boolean hasToken) {
        //缓存拦截器
        //        Cache cache=initCache();
        //        CacheInterceptor cacheInterceptor=new CacheInterceptor();

        //请求拦截器
        requestInterceptor = new RequestInterceptor(context, hasToken);

        OkHttpClient.Builder okhttpClient = new OkHttpClient.Builder();
        //设置https本地证书
        /*if(BuildConfig.HttpsCheck){
            okhttpClient.sslSocketFactory(QMCAplicationLike.sslParams.sSLSocketFactory,QMCAplicationLike.sslParams.trustManager).build();//添加https证书验证
        }*/
        okhttpClient
                .addInterceptor(requestInterceptor)//请求url打印拦截器
                .connectTimeout(30, TimeUnit.SECONDS)//public static final int TIME_OUT=30;-->静态代码块里引用静态变量无效
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl(RetroFactory.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .client(okhttpClient.build())
                        .build();
        studyAPI = retrofit.create(StudyAPI.class);
    }


    RetroInit(Context context, boolean hasAuthentication, boolean hasToken) {
        //缓存拦截器
        //        Cache cache=initCache();
        //        CacheInterceptor cacheInterceptor=new CacheInterceptor();


        //请求拦截器
        requestInterceptor = new RequestInterceptor(context,hasToken);

        OkHttpClient.Builder okhttpClient = new OkHttpClient.Builder();
        //设置https本地证书
        /*if(BuildConfig.HttpsCheck){
            okhttpClient.sslSocketFactory(QMCAplicationLike.sslParams.sSLSocketFactory,QMCAplicationLike.sslParams.trustManager).build();//添加https证书验证
        }*/
        okhttpClient
                .addInterceptor(requestInterceptor)//请求url打印拦截器
                .connectTimeout(20, TimeUnit.SECONDS)//public static final int TIME_OUT=30;-->静态代码块里引用静态变量无效
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
        ;
        if (hasAuthentication) {
            okhttpClient.authenticator(new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    String credential = Credentials.basic("app", "app@sec");
                    Log.d("lsy", "Authorization: " + credential);
                    return response.request().newBuilder()
                            .header("Authorization", credential)
                            .build();
                }
            });
        }

        Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl(RetroFactory.AUTH_BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .client(okhttpClient.build())
                        .build();
        authStudyAPI = retrofit.create(StudyAPI.class);

    }

    /**
     * 传递当前界面的context给请求拦截器
     *
     * @param context
     */
    public void setContext(Context context) {
        if (requestInterceptor != null) {
            requestInterceptor.setContext(context);
        }
    }
    public void setContext(Context context,boolean hasToken) {

        if (requestInterceptor != null) {
            requestInterceptor.setContext(context,hasToken);
        }
    }

    public StudyAPI getApi() {

        return studyAPI;
    }

    public StudyAPI getAuthStudyAPI() {
        return authStudyAPI;
    }
/* public static Cache initCache(){

        //缓存路径-->关于缓存,ohttp默认没有缓存,且没有默认缓存路径
        //QMCAplicationLike.getInstance().getCacheDir().getAbsolutePath()
        File cacheFile = new File(QMCAplicationLike.getInstance().getCacheDir().getAbsolutePath(), "qmc_HttpResponseCache");
        Cache cache = null;
        try {
            cache = new Cache(cacheFile, 10 * 1024 * 1024);//缓存文件为10MB
        } catch (Exception e) {
            Log.e("OKHttp", "Could not create http cache", e);
        }
        return cache;
        //设置缓存

    }*/

}
