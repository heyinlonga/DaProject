package software.ecenter.study.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.util.Util;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import software.ecenter.study.Interface.OnClickItemListener;
import software.ecenter.study.R;
import software.ecenter.study.StudyApplication;
import software.ecenter.study.activity.MainActivity;
import software.ecenter.study.activity.SettingActivity;
import software.ecenter.study.activity.WebActivity;
import software.ecenter.study.bean.AuthBean;
import software.ecenter.study.bean.HomeBean.VersionBean;
import software.ecenter.study.service.UpLoadIntentService;


/**
 * @desc:
 * @auth: 哎哎
 * @time: 2016/8/24
 * @fixer:
 * @fixtime:
 * @fixdesc:
 * @remarks:
 */
public class ToolUtil {
    public static String css = "<style type=\"text/css\"> img {" +
            "max-width: 100% !important;" +//限定图片宽度填充屏幕
            "height:auto !important;" +//限定图片高度自动
            "}" +
            "body {" +
            "margin-right:10px;" +//限定网页中的文字右边距为15px(可根据实际需要进行行管屏幕适配操作)
            "margin-left:10px;" +//限定网页中的文字左边距为15px(可根据实际需要进行行管屏幕适配操作)
            "margin-top:10px;" +//限定网页中的文字上边距为15px(可根据实际需要进行行管屏幕适配操作)
            "font-size:15px;" +//限定网页中文字的大小为40px,请务必根据各种屏幕分辨率进行适配更改
            "color:#723600;" +//限定网页中文字的大小为40px,请务必根据各种屏幕分辨率进行适配更改
            "word-wrap:break-word;" +//允许自动换行(汉字网页应该不需要这一属性,这个用来强制英文单词换行,类似于word/wps中的西文换行)
            "}" +
            "</style>";
    private double v;

    public static Context getContext() {
        return StudyApplication.getInstance();
    }

    /**
     * 获取资源
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取文字
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获取文字数组
     */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 获取dimen
     */
    public static int getDimens(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取drawable
     */
    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    /**
     * 获取颜色
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 获取颜色选择器
     */
    public static ColorStateList getColorStateList(int resId) {
        return getResources().getColorStateList(resId);
    }

    public static List<String> StringToArray(String text, String Delimiter) {
        //用逗号将字符串分开，得到字符串数组
        String[] strs = text.split(Delimiter);
        //将字符串数组转换成集合list
        List list = Arrays.asList(strs);
        return list;
    }

    //关闭软键盘
    public static void closeSoft(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    //打开软键盘
    public static void showSoft(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(1000, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 判断activity是否存在
     */
    public static boolean isActivityActive(Activity context) {
        if ((context) != null && !context.isFinishing()) {//Activity是否OK？
            return true;
        }
        return false;
    }


    /**
     * 是否是手机号
     *
     * @param value
     * @return
     */
    public static boolean isPhoneNumber(String value) {
        Pattern p = Pattern.compile("^1[0,1,2,3,4,5,6,7,8,9]{1}[0-9]{1}[0-9]{8}$");
        Matcher m = p.matcher(value);
        return m.matches();
    }

    //判断是否可以更新
    public static boolean isUp(String ves, String loaclves) {
        if (!TextUtils.isEmpty(ves) && !TextUtils.isEmpty(loaclves)) {
            String[] vsesplit = ves.split("\\.");
            String[] loaclvsesplit = loaclves.split("\\.");
            if (vsesplit.length == loaclvsesplit.length) {
                for (int i = 0; i < vsesplit.length; i++) {
                    int vesValux = ToolUtil.getIntValue(vsesplit[i]);
                    int loaclvesValux = ToolUtil.getIntValue(loaclvsesplit[i]);
                    if (vesValux > loaclvesValux) {
                        return true;
                    }
                    if (vesValux < loaclvesValux) {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public static String getString(String text) {
        if (!TextUtils.isEmpty(text)) {
            return text;
        }
        return "";
    }

    public static String getString(String text, String def) {
        if (!TextUtils.isEmpty(text)) {
            return text;
        }
        return getString(def);
    }

    public static int getIntValue(String text) {
        int value = 0;
        try {
            value = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    //是否都是空格
    public static boolean isKongGe(String text) {
        if (!TextUtils.isEmpty(text)) {
            String s = text.replaceAll(" ", "");
            if (s.length() == 0) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean isList(List<T> list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    //带边框
    public static void loadImageKuanD(Context context, Object path, int colorId, ImageView view) {
        try {

            if (context != null && path != null) {
                Glide.with(context).load(path).transform(new CenterCrop(context), new GlideCircleTransform(context, 2, colorId))
                        .error(R.drawable.morentx)
                        .into(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 生成uri
     *
     * @param cameraFile
     * @return
     */
    public static Uri parUri(Context context, File cameraFile) {
        Uri imageUri;
        String authority = context.getPackageName() + ".provider";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(context, authority, cameraFile);
        } else {
            imageUri = Uri.fromFile(cameraFile);
        }
        return imageUri;
    }

    /**
     * 文件夹下文件删除
     *
     * @param path
     * @return
     */
    public static boolean deleteDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (file != null) {
                for (File f : files) {
                    if (f.exists()) {
                        f.delete();
                    }
                }
            }
            return true;
        } else {
            file.delete();
            return true;
        }
    }

    public static String getRootPath(Context context) {
        String state = Environment.getExternalStorageState();
        File rootDir = state.equals(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory() : context.getCacheDir();
        return rootDir.getAbsolutePath();
    }

    //创建文件夹
    public static String createFile(Context context, String fileName, String fileDir) {
        String state = Environment.getExternalStorageState();
        File rootDie = state.equals(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory() : context.getCacheDir();
        File path = null;
        if (!TextUtils.isEmpty(fileDir)) {
            path = new File(rootDie.getAbsolutePath() + File.separator + fileDir);
        } else {
            path = new File(rootDie.getAbsolutePath());
        }
        if (!path.exists())
            path.mkdir();
        return path.getAbsolutePath() + File.separator + fileName;
    }

    //图片请求的权限
    public static String[] PERSSION_PHOTO = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    public static String[] PERSSION_RECORD = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
    //请求的权限
    public static String[] PERSSION_WRITE = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static String path = "https://gss1.bdstatic.com/9vo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=19da9ab8b6315c60579863bdecd8a076/b8014a90f603738d50533a8fb91bb051f819ec06.jpg";

    /**
     * 作者弹框
     */
    public static void showAuthDialog(Activity activity, AuthBean bean) {
        final Dialog normalDialog = new Dialog(activity, R.style.dialog);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.view_auth, null);
        ImageView auth_icon = inflate.findViewById(R.id.auth_icon);
        TextView auth_name = inflate.findViewById(R.id.auth_name);
        TextView auth_sex = inflate.findViewById(R.id.auth_sex);
        TextView auth_age = inflate.findViewById(R.id.auth_age);
        TextView auth_subject = inflate.findViewById(R.id.auth_subject);
        TextView auth_title = inflate.findViewById(R.id.auth_title);
        TextView auth_school = inflate.findViewById(R.id.auth_school);
        TextView auth_content = inflate.findViewById(R.id.auth_content);
        if (bean != null) {
            loadImageKuanD(activity, bean.getData().getPhotoUrl(), R.color.background, auth_icon);
            auth_name.setText(bean.getData().getName());
            auth_sex.setText("性别:" + bean.getData().getSex());
            auth_age.setText("年龄:" + bean.getData().getAge());
            auth_subject.setText("学科:" + bean.getData().getSubject());
            auth_title.setText("职称:" + bean.getData().getTitle());
            auth_school.setText("职称:" + bean.getData().getSchool());
            auth_content.setText(bean.getData().getDescription());
        }
        inflate.findViewById(R.id.auth_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalDialog.dismiss();
            }
        });
        normalDialog.setCanceledOnTouchOutside(true);
        normalDialog.setContentView(inflate);
        normalDialog.show();
    }

    //改变字体部分颜色
    public static SpannableStringBuilder setTextSpannd(String str, int strat, int end, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        builder.setSpan(new ForegroundColorSpan(color), strat, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new AbsoluteSizeSpan(ScreenUtil.dip2px(getContext(), 19)), strat, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 验证防伪码
     */
    public static void showYanCodeDialog(Activity activity, String bookname, final OnClickItemListener listener) {
        final Dialog normalDialog = new Dialog(activity, R.style.dialog);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.view_bind_code, null);
        TextView tv_text = inflate.findViewById(R.id.tv_text);
        String text = "每本书有一个独立的防伪码。绑定" + bookname + "防伪码后，可享受该书资源正版的优惠价格、获赠答疑卷福利。";
        tv_text.setText(setTextSpannd(text, 15, bookname.length() + 15, getColor(R.color.red_f00)));
        TextView tv_cancle = inflate.findViewById(R.id.tv_cancle);
        TextView tv_config = inflate.findViewById(R.id.tv_config);
        TextView tv_pay = inflate.findViewById(R.id.tv_pay);
        tv_pay.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_pay.getPaint().setAntiAlias(true);//抗锯齿
        tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPay();
                }
                normalDialog.dismiss();
            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCancle();
                }
                normalDialog.dismiss();
            }
        });
        tv_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onConfig();
                }
                normalDialog.dismiss();
            }
        });


        inflate.findViewById(R.id.auth_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalDialog.dismiss();
            }
        });
        normalDialog.setCanceledOnTouchOutside(true);
        normalDialog.setContentView(inflate);
        Window window = normalDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = ScreenUtil.getScreenWidth(activity) - ScreenUtil.dip2px(activity, 50);
        normalDialog.show();
    }

    /**
     * 第二批次购买框
     */
    public static void showBuySensondDialog(Activity activity, String name, String price, String count, final OnClickItemListener listener) {
        final Dialog normalDialog = new Dialog(activity, R.style.dialog);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.view_buy_send, null);
        TextView tv_text = inflate.findViewById(R.id.tv_text);
        TextView tv_cancle = inflate.findViewById(R.id.tv_cancle);
        TextView tv_config = inflate.findViewById(R.id.tv_config);
        TextView tv_price = inflate.findViewById(R.id.tv_price);

        tv_price.setText(price);
        String text = "该课程新增" + name + "资源" + count + "个，需另行购买，是否购买？";
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        builder.setSpan(new ForegroundColorSpan(getColor(R.color.color_EE7C55)), 5, name.length() + 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(getColor(R.color.color_EE7C55)), name.length() + 7, name.length() + 7 + count.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_text.setText(builder);

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCancle();
                }
                normalDialog.dismiss();
            }
        });
        tv_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onConfig();
                }
                normalDialog.dismiss();
            }
        });

        inflate.findViewById(R.id.iv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalDialog.dismiss();
            }
        });
        normalDialog.setCanceledOnTouchOutside(true);
        normalDialog.setContentView(inflate);
        Window window = normalDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = ScreenUtil.getScreenWidth(activity) - ScreenUtil.dip2px(activity, 50);
        normalDialog.show();
    }

    /**
     * 注销弹框
     */
    public static void showZXDialog(Activity activity, final OnClickItemListener listener) {
        final Dialog normalDialog = new Dialog(activity, R.style.dialog);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.view_zhuxiao, null);
        ImageView iv_cancle = inflate.findViewById(R.id.iv_cancle);
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCancle();
                }
                normalDialog.dismiss();
            }
        });
        normalDialog.setCanceledOnTouchOutside(true);
        normalDialog.setContentView(inflate);
        Window window = normalDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = ScreenUtil.getScreenWidth(activity) - ScreenUtil.dip2px(activity, 50);
        normalDialog.show();
    }

    /**
     * 隐私政策
     */
    public static void showYSDialog(final Activity activity, final OnClickItemListener listener) {
        final Dialog normalDialog = new Dialog(activity, R.style.dialog);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.view_yinshi, null);
        TextView tv_text = inflate.findViewById(R.id.tv_text);
        String text = "您可以查看完整版" +
                "《状元共享课堂用户协议》" + "和" + "《状元共享课堂隐私协议》" + "以便了解我们收集、使用、共享、存储信息的情况，以及对信息的保护措施。";
        tv_text.setText(getClickableSpan(activity, text));
        tv_text.setMovementMethod(LinkMovementMethod.getInstance());
        ImageView tv_cancle = inflate.findViewById(R.id.tv_cancle);
        TextView tv_config = inflate.findViewById(R.id.tv_config);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCancle();
                }
                normalDialog.dismiss();
            }
        });
        tv_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onConfig();
                }
                normalDialog.dismiss();
            }
        });

        normalDialog.setCanceledOnTouchOutside(false);
        normalDialog.setContentView(inflate);
        Window window = normalDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = ScreenUtil.getScreenWidth(activity) - ScreenUtil.dip2px(activity, 50);
        normalDialog.show();
    }

    //设置超链接文字
    private static SpannableString getClickableSpan(final Activity activity, String text) {
        SpannableString spanStr = new SpannableString(text);
        //设置下划线文字
//        spanStr.setSpan(new UnderlineSpan(), 42, 54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的单击事件
        spanStr.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
//                super.updateDrawState(ds);
            }

            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(activity, WebActivity.class);
                intent.putExtra("url", "http://xzykt.longmenshuju.com/userAgreement");
                intent.putExtra("fuwuxieyi", "用户协议");
                activity.startActivity(intent);
            }
        }, 9, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的前景色
        spanStr.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.color_EE7C55)), 8, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置下划线文字
//        spanStr.setSpan(new UnderlineSpan(), 53, 65, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的单击事件
        spanStr.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
//                super.updateDrawState(ds);
            }

            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(activity, WebActivity.class);
                intent.putExtra("url", "http://xzykt.longmenshuju.com/Privacy");
                intent.putExtra("fuwuxieyi", "隐私政策");
                activity.startActivity(intent);
            }
        }, 21, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的前景色
        spanStr.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.color_EE7C55)), 21, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    /**
     * 手动绑定防伪码
     */
    public static void showBindCodeDialog(final Activity activity, final OnClickItemListener listener) {
        final Dialog normalDialog = new Dialog(activity, R.style.dialog);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.view_shoudong_code, null);
        final EditText ed_contetn = inflate.findViewById(R.id.ed_contetn);
        ImageView iv_gif = inflate.findViewById(R.id.iv_gif);
        if (Util.isOnMainThread()) {
            if (!activity.isFinishing())
                Glide.with(activity).load(R.drawable.bindcode).into(iv_gif);
        }
        TextView tv_cancle = inflate.findViewById(R.id.tv_cancle);
        TextView tv_config = inflate.findViewById(R.id.tv_config);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCancle();
                }
                normalDialog.dismiss();
            }
        });
        tv_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = ed_contetn.getText().toString();
                if (getString(s).length() != 16 && getString(s).length() != 18) {
                    ToastUtils.showToastSHORT(activity, "请输入正确的防伪码");
                    return;
                }
                if (listener != null) {
                    listener.onConfig(s);
                }
                normalDialog.dismiss();
            }
        });
        inflate.findViewById(R.id.auth_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalDialog.dismiss();
            }
        });
        normalDialog.setCanceledOnTouchOutside(true);
        normalDialog.setContentView(inflate);
        Window window = normalDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = ScreenUtil.getScreenWidth(activity) - ScreenUtil.dip2px(activity, 50);
        normalDialog.show();
    }

    /**
     * 比赛完成
     */
    public static void showMatchConfigDialog(final Activity activity, int num, final OnClickItemListener listener) {
        final Dialog normalDialog = new Dialog(activity, R.style.dialog);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.dialog_match_config, null);
        ImageView iv_close = inflate.findViewById(R.id.iv_close);
        TextView tv_num = inflate.findViewById(R.id.tv_num);
        TextView tv_jx = inflate.findViewById(R.id.tv_jx);
        TextView tv_config = inflate.findViewById(R.id.tv_config);
        tv_num.setText(num + "");
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCancle();
                }
                normalDialog.dismiss();
            }
        });
        tv_jx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCancle();
                }
                normalDialog.dismiss();
            }
        });
        tv_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onConfig();
                }
                normalDialog.dismiss();
            }
        });


        normalDialog.setCanceledOnTouchOutside(false);
        normalDialog.setContentView(inflate);
        Window window = normalDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = ScreenUtil.getScreenWidth(activity) - ScreenUtil.dip2px(activity, 50);
        normalDialog.show();
    }

    /**
     * 比赛30秒提示
     */
    public static void showMatchMessDialog(final Activity activity, final OnClickItemListener listener) {
        final Dialog normalDialog = new Dialog(activity, R.style.dialog);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.dialog_mess_config, null);
        TextView tv_zd = inflate.findViewById(R.id.tv_zd);
        TextView tv_config = inflate.findViewById(R.id.tv_config);
        tv_zd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCancle();
                }
                normalDialog.dismiss();
            }
        });
        tv_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onConfig();
                }
                normalDialog.dismiss();
            }
        });


        normalDialog.setCanceledOnTouchOutside(false);
        normalDialog.setContentView(inflate);
        Window window = normalDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = ScreenUtil.getScreenWidth(activity) - ScreenUtil.dip2px(activity, 50);
        normalDialog.show();
    }

    public static String getTime(long time) {
        int d = 1000 * 60 * 60 * 24;
        int h = 1000 * 60 * 60;
        int m = 1000 * 60;
        StringBuilder text = new StringBuilder();
        long day = time / d;//天
        if (day > 0) {
            text.append(day);
            text.append("天 ");
        }
        long hour = time % d / h;
        if (hour > 0) {
            if (hour <= 9) {
                text.append("0");
            }
            text.append(hour);
        } else {
            text.append("00");
        }
        text.append(":");
        long min = time % d % h / m;
        if (min > 0) {
            if (min <= 9) {
                text.append("0");
            }
            text.append(min);
        } else {
            text.append("00");
        }
        text.append(":");
        long sec = time % d % h % m / 1000;
        if (sec > 0) {
            if (sec <= 9) {
                text.append("0");
            }
            text.append(sec);
        } else {
            text.append("00");
        }
        return text.toString();
    }

    public static String getImgPath(String ossPath) {
        if (!TextUtils.isEmpty(ossPath)) {
            if (ossPath.contains("?")) {
                return ossPath.substring(0, ossPath.indexOf("?"));
            } else {
                return ossPath;
            }
        }
        return "";
    }

    public static long getLongValue(String text) {
        long value = 0;
        if (!TextUtils.isEmpty(text)) {
            try {
                value = Long.parseLong(text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    //排序
    public static List<String> reciveList(List<String> list) {
        if (list != null && list.size() > 0) {
            Collections.sort(list, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    if (!TextUtils.isEmpty(o1) && !TextUtils.isEmpty(o2)) {
                        int i = getIntValue(o1.substring(o1.lastIndexOf("/") + 1, o1.lastIndexOf("/") + 2)) - getIntValue(o2.substring(o2.lastIndexOf("/") + 1, o2.lastIndexOf("/") + 2));
                        return i;
                    }
                    return 0;
                }
            });
        }
        return list;
    }

    //处理图片查看
    public static List<LocalMedia> getPicData(String s) {
        List<LocalMedia> localMedias = new ArrayList<>();
        LocalMedia localMedia = new LocalMedia();
        localMedia.setPath(s);
        localMedias.add(localMedia);
        return localMedias;
    }

    public static double getDoubleValue(String s) {
        double value = 0;
        if (!TextUtils.isEmpty(s)) {
            try {
                value = Double.parseDouble(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

}
