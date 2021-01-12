package software.ecenter.study.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Message: 保存本地
 * Created by  ChenLong.
 * Created by Time on 2017/12/16.
 */

public class StorageUtil {
    public static final String SETTING="setting";
    public static final String MAP = "map"; //地图选择
    public static final String SCREEN = "screen"; //屏幕常亮
    public static final String MAP_ENGLISH = "map_English"; //英文地图
    public static final String LOGIN_NUMBER = "login_number"; //账号
    public static final String LOGIN_PSW = "login_psw"; //密码
    public static final String LOGIN_STATE = "login_state"; //记住密码状态

    /**
     * 保存设置
     * @param context 调用页面的context
     * @param key 保存的key
     * @param value 保存的value
     * @return 是否存储成功
     */
    public static boolean saveSetting(Context context, String key, String value){
        SharedPreferences sp=context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(key,value);
        return editor.commit();
    }

    /**
     * 获取本地的设置
     * @param context 调用页面的context
     * @param key 要获取值的key
     * @return 要获取的设置值
     */
    public static String getSetting(Context context, String key){
        SharedPreferences sp=context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }
}
