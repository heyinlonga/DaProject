package software.ecenter.study;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Mike on 2018/4/23.
 */

public class StudyApplication extends MultiDexApplication {
    private static StudyApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //增加这句话
        QbSdk.initX5Environment(this, null);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        if (!JPushInterface.isPushStopped(this))
//            JPushInterface.stopPush(this);
        registActivityListen();
//        CrashReport.initCrashReport(getApplicationContext(), "f83075fdbf", true);
        UMConfigure.init(this, "5ae19d8a8f4a9d52c0000207","", UMConfigure.DEVICE_TYPE_PHONE, null);
//        UMConfigure.init(this, "5ae19d8a8f4a9d52c0000207","", UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);


    }

    public static StudyApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void registActivityListen() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (JPushInterface.isPushStopped(activity.getBaseContext())) ;
                JPushInterface.resumePush(activity.getBaseContext());
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

    }

}
