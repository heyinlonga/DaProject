package software.ecenter.study.Jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import software.ecenter.study.activity.BookDetailActivity;
import software.ecenter.study.activity.HomeActivity;
import software.ecenter.study.activity.MatchDetailActivity;
import software.ecenter.study.activity.MessageDetailActivity;
import software.ecenter.study.activity.MyUpdataActivity;
import software.ecenter.study.activity.QuestionDetailActivity;
import software.ecenter.study.activity.SeeResourcesVideoActivity;
import software.ecenter.study.activity.TestActivity;
import software.ecenter.study.utils.ExampleUtil;
import software.ecenter.study.utils.Logger;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-Example";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Logger.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Logger.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                processCustomMessage(context, bundle);
//                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//                JSONObject jsonObjectTest = new JSONObject(extras);
//                int id = jsonObjectTest.getInt("id");
//                int type = jsonObjectTest.getInt("type");
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");
                int type = -1;
                int id = 0;
                Intent intentJump = new Intent();
                intentJump.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                String a = (String) bundle.get(JPushInterface.EXTRA_EXTRA);
                JSONObject json = new JSONObject(a);
                id = json.getInt("id");
                type = json.getInt("type");
                Logger.d(TAG, "[MyReceiver] id: " + id);
                Logger.d(TAG, "[MyReceiver] type: " + type);
                switch (type) {
                    case 0: //评论通过审核  ---- 跳转到查看资源页面
                        intentJump.putExtra("resourceId", id + "");
                        intentJump.setClass(context, SeeResourcesVideoActivity.class);
                        context.startActivity(intentJump);
                        break;
                    case 1: //反馈回复、上传资源通过审核、其他推送  -----跳转到消息详情界面
                        intentJump.putExtra("messageId", id + "");
                        intentJump.setClass(context, MessageDetailActivity.class);
                        context.startActivity(intentJump);
                        break;
                    case 2: //答疑回复    -----跳转到提问详情页面
                        intentJump.putExtra("questionId", id + "");
                        intentJump.setClass(context, QuestionDetailActivity.class);
                        context.startActivity(intentJump);
                        break;
                    case 3: // 群发         ------跳转到消息详情页面，接口中是否群发参数为true
                        intentJump.putExtra("messageId", id + "");
                        intentJump.putExtra("isall", true);
                        intentJump.setClass(context, MessageDetailActivity.class);
                        context.startActivity(intentJump);
                        break;
                    case 4://上传资源审核通过     -----跳转到 我的上传列表
                        intentJump.setClass(context, MyUpdataActivity.class);
                        context.startActivity(intentJump);
                        break;
                    case 5: //大赛详情页
                        intentJump.setClass(context, MatchDetailActivity.class);
                        intentJump.putExtra("id", id + "");
                        context.startActivity(intentJump);
                        break;
                    default:
                        //打开自定义的Activity
                        Intent i = new Intent(context, HomeActivity.class);
                        i.putExtras(bundle);
                        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                        break;

                }

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Logger.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {
            Logger.d(TAG,"出错了：   " + e.toString());
            e.printStackTrace();
        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Logger.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Logger.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        if (HomeActivity.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Intent msgIntent = new Intent(HomeActivity.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(HomeActivity.KEY_MESSAGE, message);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (extraJson.length() > 0) {
                        msgIntent.putExtra(HomeActivity.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {

                }

            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
        }
    }
}
