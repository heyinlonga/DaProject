package software.ecenter.study.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Message:  屏幕尺寸相关
 * Created by  ChenLong.
 * Created by Time on 2018/9/18.
 */

public class ScreenUtil {
    // 将px值转换为dip或dp值
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    // 将dp或dip值转换为px值
    public static int dip2px(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue * scale + 0.5f);
    }

    //动态设置高度
    public static void getViewHeight(Activity activity, View view, int with, int height) {
        //获取布局参数
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.height = getScreenWidth(activity) * height / with;
        view.setLayoutParams(lp);
    }

    //设置宽高一样
    public static void getHeightSame(Activity activity, View view) {
        //获取布局参数
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.height = getScreenWidth(activity);
        view.setLayoutParams(lp);
    }

    //获取屏幕的宽度
    public static int getScreenWidth(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getWidth();
    }

    //获取屏幕的高度
    public static int getScreenHeight(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getHeight();
    }

    //获取屏幕的宽度
    public static int getScreenWidth(Context activity) {
        return activity.getResources().getDisplayMetrics().widthPixels;
    }

    //获取屏幕的高度
    public static int getScreenHeight(Context activity) {
        return activity.getResources().getDisplayMetrics().heightPixels;
    }
}
