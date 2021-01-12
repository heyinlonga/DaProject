package software.ecenter.study.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPluginPlatformInterface;
import cn.jpush.android.api.JPushInterface;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import software.ecenter.study.R;
import software.ecenter.study.bean.HaveNewMessageBean;
import software.ecenter.study.bean.HomeBean.ResourceBean;
import software.ecenter.study.bean.HomeBean.VersionBean;
import software.ecenter.study.bean.MineBean.MineDetailBean;
import software.ecenter.study.bean.MineBean.PersonDetailBean;
import software.ecenter.study.fragment.TabFourFragment;
import software.ecenter.study.fragment.TabOneNewFragment;
import software.ecenter.study.fragment.TabThreeFragment;
import software.ecenter.study.fragment.TabThreeNewFragment;
import software.ecenter.study.fragment.TabTwoFragment;
import software.ecenter.study.net.LoadFileModel;
import software.ecenter.study.net.RetroFactory;
import software.ecenter.study.service.DownLoadIntentService;
import software.ecenter.study.service.UpLoadIntentService;
import software.ecenter.study.tool.FileManager;
import software.ecenter.study.utils.AccountUtil;
import software.ecenter.study.utils.ActivityControl;
import software.ecenter.study.utils.JpushUtil;
import software.ecenter.study.utils.Logger;
import software.ecenter.study.utils.NetworkUtil;
import software.ecenter.study.utils.ParseUtil;
import software.ecenter.study.utils.PermissionUtils;
import software.ecenter.study.utils.ToastUtils;
import software.ecenter.study.utils.ToolUtil;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;
import static com.shuyu.gsyvideoplayer.utils.CommonUtil.getStatusBarHeight;

/**
 * 首页 MainActivity
 */
public class HomeActivity extends BaseActivity {
    @BindView(R.id.ll_home_container)
    FrameLayout llHomeContainer;
    @BindView(R.id.iv_home_ic_normal)
    ImageView ivHomeIcNormal;
    @BindView(R.id.iv_home_ic_select)
    ImageView ivHomeIcSelect;
    @BindView(R.id.tv_home_tv)
    TextView tvHomeTv;
    @BindView(R.id.fl_tab_home)
    FrameLayout flTabHome;
    @BindView(R.id.iv_finance_ic_normal)
    ImageView ivFinanceIcNormal;
    @BindView(R.id.iv_finance_ic_select)
    ImageView ivFinanceIcSelect;
    @BindView(R.id.tv_finance_tv)
    TextView tvFinanceTv;
    @BindView(R.id.fl_tab_finance)
    FrameLayout flTabFinance;
    @BindView(R.id.iv_find_ic_normal)
    ImageView ivFindIcNormal;
    @BindView(R.id.iv_find_ic_select)
    ImageView ivFindIcSelect;
    @BindView(R.id.tv_find_tv)
    TextView tvFindTv;
    @BindView(R.id.fl_tab_find)
    FrameLayout flTabFind;
    @BindView(R.id.iv_myaccount_ic_normal)
    ImageView ivMyaccountIcNormal;
    @BindView(R.id.iv_myaccount_ic_select)
    ImageView ivMyaccountIcSelect;
    @BindView(R.id.tv_myaccount_tv)
    TextView tvMyaccountTv;
    @BindView(R.id.fl_tab_myaccount)
    FrameLayout flTabMyaccount;
    //显示引导页
    @BindView(R.id.home_iv_yin_dao1)
    ImageView homeIvYinDao;
    @BindView(R.id.home_iv_yin_dao2)
    ImageView homeIvYinDao2;
    @BindView(R.id.home_rl_yin_dao)
    RelativeLayout homeRlYinDao;
    @BindView(R.id.me_rl_yin_dao)
    RelativeLayout meRlYinDao;
    @BindView(R.id.me_iv_yin_dao3)
    ImageView meIvYinDao3;
    @BindView(R.id.me_iv_yin_dao4)
    ImageView meIvYinDao4;
    @BindView(R.id.view_pint)
    View view_pint;


    private static final String TAG = "JIGUANG-Example";
    /**
     * 消息Id
     **/
    private static final String KEY_MSGID = "msg_id";
    /**
     * 该通知的下发通道
     **/
    private static final String KEY_WHICH_PUSH_SDK = "rom_type";
    /**
     * 通知标题
     **/
    private static final String KEY_TITLE = "n_title";
    /**
     * 通知内容
     **/
    private static final String KEY_CONTENT = "n_content";
    /**
     * 通知附加字段
     **/
    private static final String KEY_EXTRASS = "n_extras";


    /**
     * 课堂
     */
    private TabOneNewFragment homeFragment;
    /**
     * 答疑
     */
    public TabTwoFragment AnsweringFragment;
    /**
     * 活动
     */
    private TabThreeNewFragment findFragment;
    /**
     * 我的
     */
    private TabFourFragment myAccountFragment;
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private int source = 0;
    private String resourceId = "";

    private HaveNewMessageBean haveNewMessageBean;


    /**
     * Fragment的TAG 用于解决app内存被回收之后导致的fragment重叠问题
     */
    private static final String[] FRAGMENT_TAG = {"homeTag", "financeTag", "findTag", "myaccountTag"};
    private static final String PRV_SELINDEX = "PRV_SELINDX";
    private int selindex = 0;

    private MyBroadcastReceiver mBroadcastReceiver;
    private JPluginPlatformInterface jPluginPlatformInterface;

    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;

    static HomeActivity context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        jPluginPlatformInterface = new JPluginPlatformInterface(this);
        context = this;
        handleOpenClick();
        if (isAuthLogin()) {
            AccountUtil.saveLoginToAuth(mContext, true);
            startActivity(AuthLoginActivity.class);
        }
        getUserInfo();
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            //读取上一次界面Save的时候tab选中的状态(这里暂时未保存)
            selindex = savedInstanceState.getInt(PRV_SELINDEX, selindex);
            homeFragment = (TabOneNewFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG[0]);
            AnsweringFragment = (TabTwoFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG[1]);
            findFragment = (TabThreeNewFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG[2]);
            myAccountFragment = (TabFourFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG[3]);
        }


        if (homeFragment == null) {
            homeFragment = new TabOneNewFragment();
        }
        if (AnsweringFragment == null) {
            AnsweringFragment = new TabTwoFragment();
        }
        if (findFragment == null) {
            findFragment = new TabThreeNewFragment();
        }
        if (myAccountFragment == null) {
            myAccountFragment = new TabFourFragment();
        }
        // initWindows();
        String tab = getIntent().getStringExtra("TAB");
        source = getIntent().getIntExtra("source", 0);
        resourceId = getIntent().getStringExtra("resourceId");
        if ("2".equals(tab)) {
            Bundle bundle = new Bundle();
            bundle.putInt("source", source);
            bundle.putString("resourceId", resourceId == null ? "" : resourceId);
            AnsweringFragment.setArguments(bundle);
            //答疑
            onViewClicked(flTabFinance);
        } else {
            //首页
            onViewClicked(flTabHome);
            showHomeYinDao();
        }
        delFile();
        getDeviceInfo(mContext);
        getUpdatVersion();

        //客户端XML外部实体注入
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setExpandEntityReferences(false);
    }

    /**
     * 极光厂商渠道 通知处理
     */
    private void handleOpenClick() {
        Log.d(TAG, "HomeActivity ----- 用户点击打开了通知");
        String data = null;
        //获取华为平台附带的jpush信息
        if (getIntent().getData() != null) {
            data = getIntent().getData().toString();
        }
        Log.w(TAG, "HomeActivity ----- msg content is 1111111   <-------->   " + String.valueOf(data));
        //获取fcm、oppo、小米、vivo平台附带的jpush信息
        if (TextUtils.isEmpty(data) && getIntent().getExtras() != null) {
            data = getIntent().getExtras().getString("JMessageExtra");
        }
        Log.w(TAG, "HomeActivity ----- msg content is 2222222   <--------->  " + String.valueOf(data));

        if (TextUtils.isEmpty(data)) return;
        try {
            JSONObject jsonObject = new JSONObject(data);
            String msgId = jsonObject.optString(KEY_MSGID);
            byte whichPushSDK = (byte) jsonObject.optInt(KEY_WHICH_PUSH_SDK);
            String title = jsonObject.optString(KEY_TITLE);
            String content = jsonObject.optString(KEY_CONTENT);
            String extras = jsonObject.optString(KEY_EXTRASS);
            Bundle bundle = getIntent().getExtras();
            JSONObject jsonObjectEx = new JSONObject(extras);
            int id = jsonObjectEx.getInt("id");
            int type = jsonObjectEx.getInt("type");
            Intent intentJump = new Intent();
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


//            StringBuilder sb = new StringBuilder();
//            sb.append("msgId:");
//            sb.append(String.valueOf(msgId));
//            sb.append("\n");
//            sb.append("title:");
//            sb.append(String.valueOf(title));
//            sb.append("\n");
//            sb.append("content:");
//            sb.append(String.valueOf(content));
//            sb.append("\n");
//            sb.append("extras:");
//            sb.append(String.valueOf(extras));
//            sb.append("\n");
//            sb.append("platform:");
//            sb.append(getPushSDKName(whichPushSDK));
//            Logger.d("主页获取得推送数据",sb.toString());

            //上报点击事件
            JPushInterface.reportNotificationOpened(this, msgId, whichPushSDK);
        } catch (JSONException e) {
            Log.w(TAG, "HomeActivity ----- parse notification error");
        }
    }

//    private String getPushSDKName(byte whichPushSDK) {
//        String name;
//        switch (whichPushSDK){
//            case 0:
//                name = "jpush";
//                break;
//            case 1:
//                name = "xiaomi";
//                break;
//            case 2:
//                name = "huawei";
//                break;
//            case 3:
//                name = "meizu";
//                break;
//            case 4:
//                name= "oppo";
//                break;
//            case 5:
//                name = "vivo";
//                break;
//            case 8:
//                name = "fcm";
//                break;
//            default:
//                name = "jpush";
//        }
//        return name;
//    }


    //删除上次跟新的apk
    private void delFile() {
        File file = new File(FileManager.getInstance(this).getLoadapkDir());
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file1 : files) {
                    if (file1.exists()) {
                        file1.delete();
                    }
                }
            }
        }
    }

    /**
     * 是否需要更新
     */
    private void getUpdatVersion() {
        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, Object> map = new HashMap<>();
        map.put("type", 0);
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).upDataVewsion(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        VersionBean bean = ParseUtil.parseBean(response, VersionBean.class);
                        if (bean != null) {
                            VersionBean.Data data = bean.getData();
                            if (data != null) {
                                String version = data.getVersion();
                                //版本号不同  更新
                                if (ToolUtil.isUp(version, getVersionName())) {
                                    showVersion(data.isCanClose(), bean);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });

    }


    /**
     * 更新弹框
     *
     * @param canClose
     */
    private void showVersion(boolean canClose, final VersionBean bean) {
        final Dialog normalDialog = new Dialog(this, R.style.dialog);
        View inflate = LayoutInflater.from(this).inflate(R.layout.view_version, null);
        ImageView ver_cancle = inflate.findViewById(R.id.ver_cancle);
        ImageView ver_gone = inflate.findViewById(R.id.ver_gone);
        if (!canClose) {
            ver_cancle.setVisibility(View.GONE);
        }
        ver_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalDialog.dismiss();
            }
        });
        ver_gone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtils.newIntence().requestPerssion(HomeActivity.this, ToolUtil.PERSSION_WRITE, 1, new PermissionUtils.IPermission() {
                    @Override
                    public void success(int code) {
                        //去升级
                        UpLoadIntentService.startActionUpload(mContext, bean);
                        normalDialog.dismiss();
                        showPro();
                    }
                });
            }
        });
        normalDialog.setCanceledOnTouchOutside(false);
        normalDialog.setContentView(inflate);
        normalDialog.show();
    }

    private ProgressDialog pd1;

    private void showPro() {
        pd1 = new ProgressDialog(this);
        pd1.setTitle(getString(R.string.app_name));
        pd1.setIcon(R.mipmap.ic_launcher);
        pd1.setMessage("正在下载...");
        pd1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd1.setCancelable(true);
        pd1.setIndeterminate(false);
        pd1.setMax(100);
        pd1.show();
    }

    //获取版本号
    private String getVersionName() {
        String verName = "";
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            verName = packInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = getMac(context);

            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMac(Context context) {
        String mac = "";
        if (context == null) {
            return mac;
        }
        if (Build.VERSION.SDK_INT < 23) {
            mac = getMacBySystemInterface(context);
        } else {
            mac = getMacByJavaAPI();
            if (TextUtils.isEmpty(mac)) {
                mac = getMacBySystemInterface(context);
            }
        }
        return mac;

    }

    @TargetApi(9)
    private static String getMacByJavaAPI() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface netInterface = interfaces.nextElement();
                if ("wlan0".equals(netInterface.getName()) || "eth0".equals(netInterface.getName())) {
                    byte[] addr = netInterface.getHardwareAddress();
                    if (addr == null || addr.length == 0) {
                        return null;
                    }
                    StringBuilder buf = new StringBuilder();
                    for (byte b : addr) {
                        buf.append(String.format("%02X:", b));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    return buf.toString().toLowerCase(Locale.getDefault());
                }
            }
        } catch (Throwable e) {
        }
        return null;
    }

    private static String getMacBySystemInterface(Context context) {
        if (context == null) {
            return "";
        }
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
                WifiInfo info = wifi.getConnectionInfo();
                return info.getMacAddress();
            } else {
                return "";
            }
        } catch (Throwable e) {
            return "";
        }
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (context == null) {
            return result;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Throwable e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    private void initWindows() {
        Window window = getWindow();
        int color = getResources().getColor(R.color.background);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);
            //设置导航栏颜色
            window.setNavigationBarColor(color);
            ViewGroup contentView = ((ViewGroup) findViewById(android.R.id.content));
            View childAt = contentView.getChildAt(0);
            if (childAt != null) {
                childAt.setFitsSystemWindows(true);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //设置contentview为fitsSystemWindows
            ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);
            View childAt = contentView.getChildAt(0);
            if (childAt != null) {
                childAt.setFitsSystemWindows(true);
            }
            //给statusbar着色
            View view = new View(this);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(this)));
            view.setBackgroundColor(color);
            contentView.addView(view);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //保存tab选中的状态
        outState.putInt(PRV_SELINDEX, selindex);
        super.onSaveInstanceState(outState);
    }

    /**
     * tab选择效果(无切换刷新)
     *
     * @param home
     * @param finance
     * @param myaccount
     */
    private void setTabSelectNoRefresh(boolean home, boolean finance, boolean find, boolean myaccount) {
        if (home) {
            ivHomeIcNormal.setVisibility(View.INVISIBLE);
            ivHomeIcSelect.setVisibility(View.VISIBLE);
            showHomeYinDao();
        } else {
            ivHomeIcNormal.setVisibility(View.VISIBLE);
            ivHomeIcSelect.setVisibility(View.INVISIBLE);
        }
        tvHomeTv.setSelected(home);
        if (finance) {
            ivFinanceIcNormal.setVisibility(View.INVISIBLE);
            ivFinanceIcSelect.setVisibility(View.VISIBLE);
        } else {
            ivFinanceIcNormal.setVisibility(View.VISIBLE);
            ivFinanceIcSelect.setVisibility(View.INVISIBLE);
        }
        tvFinanceTv.setSelected(finance);
        if (find) {
            ivFindIcNormal.setVisibility(View.INVISIBLE);
            ivFindIcSelect.setVisibility(View.VISIBLE);
        } else {
            ivFindIcNormal.setVisibility(View.VISIBLE);
            ivFindIcSelect.setVisibility(View.INVISIBLE);
        }
        tvFindTv.setSelected(find);
        if (myaccount) {
            ivMyaccountIcNormal.setVisibility(View.INVISIBLE);
            ivMyaccountIcSelect.setVisibility(View.VISIBLE);
            showMeYinDao();
        } else {
            ivMyaccountIcNormal.setVisibility(View.VISIBLE);
            ivMyaccountIcSelect.setVisibility(View.INVISIBLE);
        }
        tvMyaccountTv.setSelected(myaccount);

    }

    @OnClick({R.id.fl_tab_home, R.id.fl_tab_finance, R.id.fl_tab_find, R.id.fl_tab_myaccount, R.id.home_rl_yin_dao,
            R.id.me_rl_yin_dao})
    public void onViewClicked(View view) {
        //二.在监听器里每一次切换创建一个新事务和隐藏所有fragment
        transaction = fragmentManager
                .beginTransaction();
        if (homeFragment.isAdded()) {
            transaction.hide(homeFragment);
            showHomeYinDao();
        }
        if (AnsweringFragment.isAdded()) {
            transaction.hide(AnsweringFragment);
        }
        if (findFragment.isAdded()) {
            transaction.hide(findFragment);
        }
        if (myAccountFragment.isAdded()) {
            transaction.hide(myAccountFragment);
            showMeYinDao();
        }
        switch (view.getId()) {
            case R.id.fl_tab_home:
                //tab1
                //三.切换时判断如果fragment没有被添加过就添加并展示>>提交
                if (!homeFragment.isAdded()) {
                    // 隐藏当前的fragment，add下一个到Activity中
                    transaction.add(R.id.ll_home_container, homeFragment, FRAGMENT_TAG[0]);
                    transaction.show(homeFragment);//这行可以不需要?
                    transaction.commitAllowingStateLoss();
                } else {
                    //添加过了就只显示fragment>>提交
                    // 隐藏当前的fragment，显示下一个
                    transaction.show(homeFragment).commitAllowingStateLoss();

                }
                setTabSelectNoRefresh(true, false, false, false);

                break;
            case R.id.fl_tab_finance:
                //tab2
                if (!AnsweringFragment.isAdded()) {
                    // 隐藏当前的fragment，add下一个到Activity中
                    transaction.add(R.id.ll_home_container, AnsweringFragment, FRAGMENT_TAG[1]);
                    transaction.show(AnsweringFragment);
                    transaction.commitAllowingStateLoss();
                } else {
                    // 隐藏当前的fragment，显示下一个
                    transaction.show(AnsweringFragment).commitAllowingStateLoss();
                }
                setTabSelectNoRefresh(false, true, false, false);

                break;
            case R.id.fl_tab_find:
                //tab3
                if (!findFragment.isAdded()) {
                    // 隐藏当前的fragment，add下一个到Activity中
                    transaction.add(R.id.ll_home_container, findFragment, FRAGMENT_TAG[2]);
                    transaction.show(findFragment);
                    transaction.commitAllowingStateLoss();
                } else {
                    // 隐藏当前的fragment，显示下一个
                    transaction.show(findFragment).commitAllowingStateLoss();
                    findFragment.getData();
                }
                setTabSelectNoRefresh(false, false, true, false);

                break;
            case R.id.fl_tab_myaccount:
/*                updateToken();
                //tab4
                //判断是否是切换到我的账户tab且非登录状态;
                if (TextUtils.isEmpty(token)) {
                    //跳转登录界面
                    ActivityUtil.startNewLoginActivity(this, ConstUtils.LOGIN_MYACCOUNT);
                    return;
                } else {*/
                if (!myAccountFragment.isAdded()) {
                    // 隐藏当前的fragment，add下一个到Activity中
                    transaction.add(R.id.ll_home_container, myAccountFragment, FRAGMENT_TAG[3]);
                    transaction.show(myAccountFragment);
                    transaction.commitAllowingStateLoss();
                } else {
                    // 隐藏当前的fragment，显示下一个
                    transaction.show(myAccountFragment).commitAllowingStateLoss();

                }
                setTabSelectNoRefresh(false, false, false, true);

                break;
            // }
            case R.id.home_rl_yin_dao:
                if (AccountUtil.getFirstOpenHome(mContext)) {
                    AccountUtil.saveFirstOpenHome(mContext, false);
                    if (AccountUtil.getFirstOpenBookPackage(mContext)) {
                        homeIvYinDao2.setImageDrawable(getResources().getDrawable(R.drawable.yindao2));
                        homeIvYinDao.setVisibility(View.GONE);
//                        homeIvYinDao.
                    }
                } else {
                    AccountUtil.saveFirstOpenBookPackage(mContext, false);
                    homeRlYinDao.setVisibility(View.GONE);
                }
                break;
            case R.id.me_rl_yin_dao:
                if (AccountUtil.getFirstOpenMessage(mContext)) {
                    AccountUtil.saveFirstOpenMessage(mContext, false);
                    if (AccountUtil.getFirstOpenMoney(mContext)) {
                        meIvYinDao4.setImageDrawable(getResources().getDrawable(R.drawable.yindao4));
                        meIvYinDao3.setVisibility(View.GONE);

                    }
                } else {
                    AccountUtil.saveFirstOpenMoney(mContext, false);
                    meRlYinDao.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void showMeYinDao() {
        boolean isOpenFirstOpenMessage = AccountUtil.getFirstOpenMessage(mContext);
        if (isOpenFirstOpenMessage) {
            // TODO: 2018/7/7 显示扫码引导页
            meRlYinDao.setVisibility(View.VISIBLE);
            meIvYinDao3.setImageDrawable(getResources().getDrawable(R.drawable.yidao3));
            meRlYinDao.setBackgroundColor(getResources().getColor(R.color.black));
            meRlYinDao.getBackground().setAlpha(220);
        } else if (AccountUtil.getFirstOpenMoney(mContext)) {
            meRlYinDao.setVisibility(View.VISIBLE);
            meIvYinDao4.setImageDrawable(getResources().getDrawable(R.drawable.yindao4));
            meRlYinDao.setBackgroundColor(getResources().getColor(R.color.black));
            meRlYinDao.getBackground().setAlpha(220);

        }
    }

    private void showHomeYinDao() {
        boolean isOpenFirstOpenHome = AccountUtil.getFirstOpenHome(mContext);
        if (isOpenFirstOpenHome) {
            // TODO: 2018/7/7 显示扫码引导页
            homeRlYinDao.setVisibility(View.VISIBLE);
            homeIvYinDao.setImageDrawable(getResources().getDrawable(R.drawable.yindao1));
            homeRlYinDao.setBackgroundColor(getResources().getColor(R.color.black));
            homeRlYinDao.getBackground().setAlpha(220);
        } else if (AccountUtil.getFirstOpenBookPackage(mContext)) {
            homeRlYinDao.setVisibility(View.VISIBLE);
            homeIvYinDao2.setImageDrawable(getResources().getDrawable(R.drawable.yindao2));
            homeRlYinDao.setBackgroundColor(getResources().getColor(R.color.black));
            homeRlYinDao.getBackground().setAlpha(220);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerMyReceiver();
        getUserInfo();
        getHaveNewMessage();
        LoadFileModel.ProgressHelper.setProgressHandler(new LoadFileModel.ProgressBean.ProgressHandler() {
            @Override
            protected void onProgress(long progress, long total, long speed, boolean done, String resouceId) {
                Log.e("onProgress", String.format("%d%% done\n", progress * 100 / total) + "   resouceId-->" + resouceId);
//                ToastUtils.showToastSHORT(mContext, "已下载" + progress * 100 / total + "%");
                if (pd1 != null) {
                    double value = progress / (double) total;
                    if (value <= 0) {
                        value = 0;
                    }
                    if (value >= 1) {
                        value = 1;
                    }
                    Log.e("onProgress", "------" + (int) (value * 100) + "----------" + value);
                    int pro = (int) (value * 100);
                    if (pro >= 100) {
                        pro = 100;
                        pd1.setMessage("正在安装...");
                    }
                    pd1.setProgress(pro);
                }
            }
        });
    }

    private void registerMyReceiver() {
        if (mBroadcastReceiver == null) {
            //注册广播
            mBroadcastReceiver = new MyBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(DownLoadIntentService.RESULT_DOWN);
            intentFilter.addAction(DownLoadIntentService.RESULT_DELETE);
            intentFilter.addAction(DownLoadIntentService.RESULT_DOWN_ONE);
            registerReceiver(mBroadcastReceiver, intentFilter);
        }
    }

    /**
     * 检查是否有新消息
     */
    public void getHaveNewMessage() {
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getHaveNewMessage())
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        haveNewMessageBean = new HaveNewMessageBean();
                        haveNewMessageBean = ParseUtil.parseBean(response, HaveNewMessageBean.class);
                        setHaveNewMessage(haveNewMessageBean.getData().isHaveNewMessage());


                    }

                    @Override
                    public void onFail(int type, String msg) {

                    }
                });
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownLoadIntentService.RESULT_DOWN)) {//下载全部
                String num = intent.getStringExtra("down_num");
                String errorNum = intent.getStringExtra("down_error_num");
                ToastUtils.showToastLONG(context, "下载完成，共" + num + "个资源" + ",失败" + errorNum + "资源");
            }
            if (intent.getAction().equals(DownLoadIntentService.RESULT_DELETE)) {//删除全部选中
                List<ResourceBean> list = (List<ResourceBean>) intent.getSerializableExtra("data");
                //根据服务器是否有记录删除
                if (list != null && list.size() > 0) {
                    String ids = "";
                    for (int i = 0; i < list.size(); i++) {
                        ResourceBean resourceBean = list.get(i);
                        if (resourceBean != null) {
                            //服务器是否有记录
                            if (resourceBean.isAnwer()) {
                                if (i == list.size() - 1) {
                                    ids = ids + resourceBean.getResourceId();
                                } else {
                                    ids = ids + resourceBean.getResourceId() + ",";
                                }
                            }
                        }
                    }
                    getdel(ids);
                }
            }
            if (intent.getAction().equals(DownLoadIntentService.RESULT_DOWN_ONE)) {//下载完一个
                ResourceBean data = (ResourceBean) intent.getSerializableExtra("data");
                // 上传下载记录
                getUpData(data);
            }
        }
    }

    /**
     * 保存记录
     *
     * @param
     */
    public void getUpData(ResourceBean data) {
        if (!NetworkUtil.isConnected(mContext)) {
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("resourceId", data.getResourceId());
            json.put("resourceName", data.getResourceTitle());
            if (data.getResourceTime() != null)
                json.put("duration", data.getResourceTime());
            json.put("size", data.getResourceSize());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).savemyDownloads(body))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("resource up onSuccess", "---" + response);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        Log.d("resource  up onFail", "---" + msg);
                    }
                });
    }

    /**
     * 服务器是否有记录
     *
     * @param resourceId
     */
    public void getdel(final String resourceId) {
        if (!NetworkUtil.isConnected(mContext)) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("resourceIds", resourceId);
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).deletemyDownloads(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("resource del onSuccess", "resourceId---" + resourceId);
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        Log.d("resource  del onFail", "resourceId---" + resourceId + "----" + msg);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
        mBroadcastReceiver = null;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                ToastUtils.showToastSHORT(mContext, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo() {
//        if (!showNetWaitLoding()) {
//            return;
//        }

        //接口入参，如果只有一个参数用键值对，多个参数用json
        Map<String, String> map = new HashMap<>();
        new RetroFactory(mContext, RetroFactory.getRetroFactory(mContext).getUserInfo(map))
                .handleResponse(new RetroFactory.ResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        dismissNetWaitLoging();
                        PersonDetailBean personDetailBean = ParseUtil.parseBean(response, PersonDetailBean.class);
                        if (personDetailBean != null || personDetailBean.getData() != null) {
                            AccountUtil.saveAccount_phone(mContext, personDetailBean.getData().getPhoneNumber());
                            AccountUtil.saveUserInfo(mContext, personDetailBean);
                            JpushUtil.setJpushMsgTipAudio(mContext);
                            JPushInterface.resumePush(mContext);
                        }
                    }

                    @Override
                    public void onFail(int type, String msg) {
                        dismissNetWaitLoging();
                        ToastUtils.showToastLONG(mContext, msg);
                    }

                });

    }

    private void setHaveNewMessage(boolean haveNewMessage) {
        if (haveNewMessage) {
            view_pint.setVisibility(View.VISIBLE);
//            ivMyaccountIcNormal.setImageDrawable(getResources().getDrawable(R.drawable.wode_uncheck));
//            ivMyaccountIcSelect.setImageDrawable(getResources().getDrawable(R.drawable.wode_check));
        } else {
            view_pint.setVisibility(View.GONE);
//            ivMyaccountIcNormal.setImageDrawable(getResources().getDrawable(R.drawable.wode_uncheck));
//            ivMyaccountIcSelect.setImageDrawable(getResources().getDrawable(R.drawable.wode_check));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        jPluginPlatformInterface.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        jPluginPlatformInterface.onStop(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (homeFragment != null) {
                homeFragment.showScan(data);
            }
        }
    }
}
