package software.ecenter.study.OSSSImple;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;

import software.ecenter.study.bean.OssTokenBean;


/**
 * 当前类的注释:OSS配置文件
 * 作者：WangLiJian on 2017/8/16.
 * 邮箱：wanglijian1214@gmail.com
 */

public class OSSConfig {
    // 运行sample前需要配置以下字段为有效的值
//    public static final String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    public static final String endpoint = OssTokenBean.getInstance().getData().getEndPoint();
//    public static final String OSSPATH="http://fengyangts.oss-cn-beijing.aliyuncs.com/";
    public static final String OSSPATH= OssTokenBean.getInstance().getData().getUploadUrl()+"/";


//    public static final String testBucket = "fengyangts";
    public static final String testBucket = OssTokenBean.getInstance().getData().getBucket();
    public static final String uploadVideoObject = "androidTest/0816video.mp4";
    public static final String downloadVideoObject = "androidTest/0816video.mp4";

    public static final String uploadVoiceObject = "0816voice.amr";
    public static final String downloadVoiceObject = "0816voice.amr";

    private final String DIR_NAME = "oss";
    private final String FILE_NAME = "caifang.m4a";

//    private final static String testAccessKeyId = "oJksg1EpWe1GLx1V";
    private final static String testAccessKeyId = OssTokenBean.getInstance().getData().getAccessKeyId();
//    private final static String testAccessKeySecret = "bYi1pysQXQp2KhICofVvVoOo6wT27u";
    private final static String testAccessKeySecret = OssTokenBean.getInstance().getData().getAccessKeySecret();
//    private static final String testSecretToken = "eyJleHBpcmF0aW9uIjoiMjAxNy0wOC0xNFQxMDoxMTo1NC4yMDdaIiwiY29uZGl0aW9ucyI6W1siY29udGVudC1sZW5ndGgtcmFuZ2UiLDAsMTA0ODU3NjAwMF0sWyJzdGFydHMtd2l0aCIsIiRrZXkiLCIyMDE3MDgxNCJdXX0=";
    private static final String testSecretToken = OssTokenBean.getInstance().getData().getSecurityToken();
    private static OSS oss;
    public static final String ossnoteFolder = OssTokenBean.getInstance().getData().getUploadFilePath(); //图片文件夹

    public static OSS getInstance(Context context) {
        if (oss == null) {
            OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(testAccessKeyId, testAccessKeySecret, testSecretToken);
            ClientConfiguration conf = new ClientConfiguration();
            conf.setConnectionTimeout(30 * 1000); // 连接超时，默认15秒
            conf.setSocketTimeout(30 * 1000); // socket超时，默认15秒
            conf.setMaxConcurrentRequest(9); // 最大并发请求书，默认5个
            conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
            OSSLog.enableLog();
            oss = new OSSClient(context, endpoint, credentialProvider, conf);
            Log.d("111111uploadData","testAccessKeyId"+testAccessKeyId);
            Log.d("111111uploadData","testAccessKeySecret"+testAccessKeySecret);
            Log.d("111111uploadData","endpoint"+endpoint);
            Log.d("111111uploadData","testBucket"+testBucket);
            return oss;
        } else {
            return oss;
        }
    }

}
