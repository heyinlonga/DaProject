package software.ecenter.study.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * Message:  对一些需要集中销毁的Activity进行销毁
 * Created by  ChenLong.
 * Created by Time on 2017/12/16.
 */

public class ActivityControl {
    private static Stack<Activity> activityStack;

    private ActivityControl() {
    }

    private static ActivityControl instance;

    public static ActivityControl getScreenManager() {
        if (instance == null) {
            instance = new ActivityControl();
        }
        return instance;
    }

    //退出栈顶Activity
    public static void popActivity(Activity activity) {
        if (activity != null) {
            //在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    //获得当前栈顶Activity
    public static Activity currentActivity() {
        Activity activity = null;
        if (!activityStack.empty())
            activity = activityStack.lastElement();
        return activity;
    }
    //获得当前栈地Activity
    public static Activity firstActivity() {
        Activity activity = null;
        if (!activityStack.empty())
            activity = activityStack.firstElement();
        return activity;
    }

    //将当前Activity推入栈中
    public static void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);

    }

    //退出栈中所有Activity
    public static void popAllActivityOne() {
        if (activityStack != null) {
            while (!activityStack.empty()) {
                Activity activity = currentActivity();
                popActivity(activity);
            }
        }
    }
}
