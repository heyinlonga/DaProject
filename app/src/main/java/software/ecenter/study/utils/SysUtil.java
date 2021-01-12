package software.ecenter.study.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;


import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.KEYGUARD_SERVICE;

/**
 * Message:  系统相关
 * Created by  ChenLong.
 * Created by Time on 2018/9/18.
 */

public class SysUtil {
    //获取包名
    public static String getPackageName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String packageName = "自己项目的包名";
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            packageName = packageInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageName;
    }

    //获取版本号
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        int versionCode = 1;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    //获取版本名称
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String versionName = "1.0";
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 打开市场
     */
    public static void goToMarket(Context context, String packageName) {
//        String packageName = getPackageName(context);
        if (!isHaveMarket(context)) {
//            Uri uri = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=" + packageName);
//            Intent it = new Intent(Intent.ACTION_VIEW, uri);
//            context.startActivity(it);
            Intent intent = new Intent(); //客户要先跳网页
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(packageName);
            intent.setData(content_url);
            context.startActivity(intent);
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + packageName));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    /**
     * 是否安装市场
     */
    private static boolean isHaveMarket(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.APP_MARKET");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        return infos.size() > 0;
    }

    //打开应用市场列表  com.tencent.android.qqdownloader 应用宝
    public static boolean toMarket(Context context) {
        String packageName = getPackageName(context);
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (marketPkg != null) {// 如果没给市场的包名，则系统会弹出市场的列表让你进行选择。
//            intent.setPackage(marketPkg);
//        }
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            //跳转应用宝
            Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=" + packageName));
            context.startActivity(it);
            return false;
        }
    }

    //是否安装了应用
    public static boolean checkAppInstalled(Context context, String pkgName) {
        if (pkgName == null || pkgName.isEmpty()) {
            return false;
        }
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> info = packageManager.getInstalledPackages(0);
        if (info == null || info.isEmpty())
            return false;
        for (int i = 0; i < info.size(); i++) {
            if (pkgName.equals(info.get(i).packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取系统亮度
     *
     * @param activity
     */
    public static int getSysBrightness(Activity activity) {
        int du = 0;
        try {
            du = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return du;
    }

    /**
     * 设置系统亮度
     *
     * @param activity
     */
    public static void setSysBrightness(Activity activity, int progress) {
        Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, progress);
    }

    /**
     * 获取设备标示
     *
     * @param activity
     */
    public static String getAndroidId(Activity activity) {
        String android_id;
        android_id = Settings.System.getString(activity.getContentResolver(), Settings.System.ANDROID_ID);
        return android_id;
    }


    //获取sha1安全码
    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


}
