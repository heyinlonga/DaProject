package software.ecenter.study.utils;

import android.app.Notification;
import android.content.Context;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by zyt on 2018/6/27.
 */

public class JpushUtil {

    public static void setJpushMsgTipAudio(Context context) {
        if (!AccountUtil.getMsgTip(context)) {
            BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
//            builder.statusBarDrawable = R.drawable.jpush_notification_icon;
            builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                    | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
            builder.notificationDefaults = Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
//             builder.notificationDefaults = Notification.DEFAULT_SOUND
//                    | Notification.DEFAULT_VIBRATE
//                    | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
            JPushInterface.setPushNotificationBuilder(1, builder);
        }else{
            BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
//            builder.statusBarDrawable = R.drawable.jpush_notification_icon;
            builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                    | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
             builder.notificationDefaults = Notification.DEFAULT_SOUND
                    | Notification.DEFAULT_VIBRATE
                    | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
            JPushInterface.setPushNotificationBuilder(1, builder);
        }
    }

}
