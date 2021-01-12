package software.ecenter.study.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import software.ecenter.study.bean.MineBean.MineDetailBean;
import software.ecenter.study.bean.MineBean.PersonDetailBean;

public class AccountUtil {

    //key
    private static final String APP_SETTING_PREFER = "APP_account_prefer"; //APP设置
    public static final String YSSTATE = "ys_state"; //隐私
    private static final String TOKEN = "token_index"; //token
    private static final String ACCOUNT_ID = "account_id_index";//用户id
    private static final String MOBILE = "mobile_index"; //用户手机号
    private static final String RElMOBILE = "mobile_index"; //用户手机号
    private static final String HEADIMAGE = "headImage_index"; //头像
    private static final String USERNAME = "userName_index"; //用户名
    private static final String GRADE = "grade_index"; //年级
    private static final String REAL_GRADE = "real_grade"; //年级
    private static final String WIFITIP = "wifi_ip"; //非WIFI提醒
    private static final String MEG_PUSH = "msgpush_index"; //消息推送
    private static final String MEG_TIP = "wifi_index"; //消息提示音
    private static final String ACCOUNT_MOBILE = "account_mobile"; //登录账号
    private static final String TEACKER_CHECKED = "teacker_checked"; //登录账号
    private static final String NICK_NAME = "nick_name";//用户昵称
    private static final String IS_TEACHER_CHECKED = "is_teacher_checked";//用户昵称
    private static final String IS_FRIST_LOGIN_AUTH= "is_frist_login_auth";//设置是否登录进入授权页面
    private static final String IS_FRIST_OPEN_HOME = "is_frist_open_home";//是否第一次打开home界面
    private static final String IS_FIRST_OPEN_BOOK_PACKAGE = "is_first_open_book_package";//是否第一次打开书包
    private static final String IS_FIRST_OPEN_MESSAGE = "is_first_open_message";//是否第一次打开消息界面
    private static final String IS_FIRST_OPEN_MONEY = "is_first_open_money"; //是否第一次打开积分和元宝
    private static final String IS_FIRST_OPEN_APP = "is_first_open_app"; //是否第一次打开应用
    private static final String SPEED_NUM = "speed_num"; //播放速度
    public static final String[] PURCHASEMODE = {"微信","支付宝","元宝","积分","答疑券","正版验证免费"}; //购买方式
    public static final String[] PURCHASETYPE = {"整书","精品课程","套系组合","章节购买","课时资源","答疑","精品课程目录","讲座","讲座目录","讲座资源"}; //购买类型

    /**
     * 获得编辑器
     *
     * @param context
     * @param name
     * @return
     */
    @SuppressLint("CommitPrefEdits")
    private static Editor createEditor(Context context, String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        return editor;
    }

    /**
     * 获取设置SharedPreferences
     *
     * @param context
     * @return
     */
    private static SharedPreferences getSettingSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_SETTING_PREFER, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static void saveUserInfo(Context context, PersonDetailBean personDetailBean) {
        PersonDetailBean.Data data = personDetailBean.getData();
        saveNickName(context, data.getNickname());
        saveIsTeacherChecked(context, data.getIsTeacherChecked());
        saveRealGrade(context, data.getRealGrade());
        saveMobile(context, data.getPhoneNumber());
        saveRelMobile(context, data.getRealPhone());
        saveRealGrade(context, data.getRealGrade());
        saveHeadImage(context, data.getHeadImage());
    }

    /**
     * 获得token
     *
     * @param context
     * @return
     */
    public static String getToken(Context context) {
        return getSettingSharedPreferences(context).getString(TOKEN, "");
    }

    /**
     * 设置token
     *
     * @param
     */
    public static void saveToken(Context context, String token) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putString(TOKEN, token);
        editor.commit();
    }

    /**
     * 获得id
     *
     * @param context
     * @return
     */
    public static String getAccountId(Context context) {
        return getSettingSharedPreferences(context).getString(ACCOUNT_ID, null);
    }

    /**
     * 设置id
     *
     * @param context
     * @param accountId
     */
    public static void saveAccountId(Context context, String accountId) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putString(ACCOUNT_ID, accountId);
        editor.commit();
    }
    /**
     * 获得播放速度
     *
     * @param context
     * @return
     */
    public static float getSpeednum(Context context) {
        return getSettingSharedPreferences(context).getFloat(SPEED_NUM, 1);
    }

    /**
     * 设置播放速度
     *
     * @param context
     * @param speed
     */
    public static void saveSpeednum(Context context, float speed) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putFloat(SPEED_NUM, speed);
        editor.commit();
    }

    public static String getNickName(Context context) {
        return getSettingSharedPreferences(context).getString(NICK_NAME, "");
    }

    public static void saveNickName(Context context, String nickName) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putString(NICK_NAME, nickName);
        editor.commit();
    }

    /**
     * 获得MOBILE
     *
     * @param context
     * @return
     */
    public static String getMobile(Context context) {
        return getSettingSharedPreferences(context).getString(MOBILE, null);
    }

    /**
     * 设置MOBILE
     *
     * @param context
     * @param accountId
     */
    public static void saveMobile(Context context, String accountId) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putString(MOBILE, accountId);
        editor.commit();
    }
    /**
     * 获得真正的手机
     *
     * @param context
     * @return
     */
    public static String getRelMobile(Context context) {
        return getSettingSharedPreferences(context).getString(RElMOBILE, null);
    }

    /**
     * 设置真正的手机
     *
     * @param context
     * @param accountId
     */
    public static void saveRelMobile(Context context, String accountId) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putString(RElMOBILE, accountId);
        editor.commit();
    }

    /**
     * 获得HEADIMAGE
     *
     * @param context
     * @return
     */
    public static String getHeadImage(Context context) {
        return getSettingSharedPreferences(context).getString(HEADIMAGE, null);
    }

    /**
     * 设置HEADIMAGE
     *
     * @param context
     * @param accountId
     */
    public static void saveHeadImage(Context context, String accountId) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putString(HEADIMAGE, accountId);
        editor.commit();
    }

    /**
     * 获得USERNAME
     *
     * @param context
     * @return
     */
    public static String getUserName(Context context) {
        return getSettingSharedPreferences(context).getString(USERNAME, null);
    }

    /**
     * 设置USERNAME
     *
     * @param context
     * @param accountId
     */
    public static void saveUserName(Context context, String accountId) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putString(USERNAME, accountId);
        editor.commit();
    }

    /**
     * 清除账户信息
     *
     * @param context
     */
    public static void clearAccountInfo(Context context) {
//        Editor editor = createEditor(context, APP_SETTING_PREFER);
//        editor.clear();
//        editor.commit();
        boolean openHome = getFirstOpenHome(context);
        boolean openBookPackage = getFirstOpenBookPackage(context);
        boolean openMessage = getFirstOpenMessage(context);
        boolean openMoney = getFirstOpenMoney(context);
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.clear();
        editor.commit();
        saveFirstOpenBookPackage(context, openBookPackage);
        saveFirstOpenHome(context, openHome);
        saveFirstOpenMessage(context, openMessage);
        saveFirstOpenMoney(context, openMoney);
        saveFirstOpenApp(context, false);
    }

    public static void clearAccountToken(Context context) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putString(TOKEN, "");
        editor.commit();
    }

    /**
     * 获得年级
     *
     * @param context
     * @return
     */
    public static String getGrade(Context context) {
        return getSettingSharedPreferences(context).getString(GRADE, "一年级下");
    }

    /**
     * 获取是否已通过教师认证
     *
     * @param context
     * @return
     */
    public static int getIsTeacherChecked(Context context) {
        return getSettingSharedPreferences(context).getInt(TEACKER_CHECKED, 0);
    }

    public static void saveIsTeacherChecked(Context context, int isTeacherChecked) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putInt(TEACKER_CHECKED, isTeacherChecked);
        editor.commit();
    }

    /**
     * 设置年级
     *
     * @param
     */
    public static void saveGrade(Context context, String grade) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putString(GRADE, grade);
        editor.commit();
    }

    /**
     * 设置带上下的年级
     *
     * @param context
     * @return
     */
    public static void saveRealGrade(Context context, String realGrade) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putString(REAL_GRADE, realGrade);
        editor.commit();
    }

    public static String getRealGrade(Context context) {
        return getSettingSharedPreferences(context).getString(REAL_GRADE, "");
    }

    public static boolean getWifiTipSet(Context context) {
        return getSettingSharedPreferences(context).getBoolean(WIFITIP, true);
    }

    public static void saveWifiTipSet(Context context, boolean value) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putBoolean(WIFITIP, value);
        editor.commit();
    }


    public static boolean getMsgPush(Context context) {
        return getSettingSharedPreferences(context).getBoolean(MEG_PUSH, true);
    }

    public static void saveMsgPush(Context context, boolean value) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putBoolean(MEG_PUSH, value);
        editor.commit();
    }


    public static boolean getMsgTip(Context context) {
        return getSettingSharedPreferences(context).getBoolean(MEG_TIP, true);
    }

    public static void saveMsgTip(Context context, boolean value) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putBoolean(MEG_TIP, value);
        editor.commit();
    }

    public static void saveAccount_phone(Context context, String account_phone) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putString(ACCOUNT_MOBILE, account_phone);
        editor.commit();
    }

    public static String getAccountMobile(Context context) {
        return getSettingSharedPreferences(context).getString(ACCOUNT_MOBILE, "");
    }

    /**
     * 设置是否为第一次打开首页显示二维码引导
     */
    public static boolean getFirstOpenHome(Context context) {
        return getSettingSharedPreferences(context).getBoolean(IS_FRIST_OPEN_HOME, true);
    }

    public static void saveFirstOpenHome(Context context, boolean isOpenHome) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putBoolean(IS_FRIST_OPEN_HOME, isOpenHome);
        editor.commit();
    }
    /**
     * 设置是否登录进入授权页面
     */
    public static boolean getLoginToAuth(Context context) {
        return getSettingSharedPreferences(context).getBoolean(IS_FRIST_LOGIN_AUTH, true);
    }

    public static void saveLoginToAuth(Context context, boolean isAuthe) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putBoolean(IS_FRIST_LOGIN_AUTH, isAuthe);
        editor.commit();
    }

    /**
     * 设置是否第一次打开首页显示书包引导
     */
    public static boolean getFirstOpenBookPackage(Context context) {
        return getSettingSharedPreferences(context).getBoolean(IS_FIRST_OPEN_BOOK_PACKAGE, true);
    }

    public static void saveFirstOpenBookPackage(Context context, boolean isOpenBookPackage) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putBoolean(IS_FIRST_OPEN_BOOK_PACKAGE, isOpenBookPackage);
        editor.commit();
    }

    /**
     * 设置是否第一次打开我的消息界面
     */
    public static boolean getFirstOpenMessage(Context context) {
        return getSettingSharedPreferences(context).getBoolean(IS_FIRST_OPEN_MESSAGE, true);
    }

    public static void saveFirstOpenMessage(Context context, boolean isOpenMessage) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putBoolean(IS_FIRST_OPEN_MESSAGE, isOpenMessage);
        editor.commit();
    }

    /**
     * 设置是否第一次打开积分和元宝界面
     */
    public static boolean getFirstOpenMoney(Context context) {
        return getSettingSharedPreferences(context).getBoolean(IS_FIRST_OPEN_MONEY, true);
    }

    public static void saveFirstOpenMoney(Context context, boolean isOpenMney) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putBoolean(IS_FIRST_OPEN_MONEY, isOpenMney);
        editor.commit();
    }

    public static boolean getFirstApp(Context context) {
        return getSettingSharedPreferences(context).getBoolean(IS_FIRST_OPEN_APP, true);
    }

    public static void saveFirstOpenApp(Context context, boolean isOpenApp) {
        Editor editor = createEditor(context, APP_SETTING_PREFER);
        editor.putBoolean(IS_FIRST_OPEN_APP, isOpenApp);
        editor.commit();
    }

}
