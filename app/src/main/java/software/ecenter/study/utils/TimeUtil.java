package software.ecenter.study.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Locale.CHINA;

/**
 * Created by Xiaoming on 2016/4/6 11:42.
 * Email:xiaoming_huo@163.com
 * <p>
 * 与时间处理相关的常用功能
 */
final public class TimeUtil {

    public static final String NORMAL_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String NORMAL_PATTERNDS = "yyyy年MM月dd日 HH:mm:ss";
    public static final String NORMAL_NIANYUE = "yyyy年MM月";
    public static final String NORMAL_PAT = "MM-dd HH:mm";
    public static final String NORMAL_PATTE = "yyyy-MM-dd HH:mm";
    public static final String NORMIAN_DATE = "yyyy/MM/dd HH:mm";
    public static final String NORMAL_DATE = "yyyy-MM-dd";
    public static final String NORMALDIAN_DATE = "yyyy.MM.dd";
    public static final String PLAINT_DATE = "yyyyMMdd";
    public static final String PLAIN_PATTERN = "yyyyMMddHHmmss";
    public static final String END_TIME = "2099-12-31 00:00";
    public static final String MONTH_DATE = "MM-dd HH:mm";
    public static final String MONTH_TIME = "HH:mm";
    public static final long day = 1000 * 60 * 60 * 24;

    private TimeUtil() {
    }

    /**
     * 获取时分
     *
     * @return
     */
    public static String getDayTime() {
        return getTime(MONTH_TIME);
    }

    /**
     * 获取当前的时间字符串
     *
     * @return 时间字符串
     */
    public static String getCurrentTime() {
        return getTime(NORMAL_PATTERN);
    }

    public static String getCurrentTime(String pattern) {
        return getTime(pattern);
    }

    public static String getEndTime(int day) {
        return getTime(NORMAL_PATTERN, day);
    }

    //获取当前的日期
    public static String getCurrentDate() {
        return getTime(NORMAL_DATE);
    }

    public static String getEndDate(int day) {
        return getTime(NORMAL_DATE, day);
    }


    /**
     * 获取当前的时间字符串
     *
     * @return 中间没有分割符的时间字符串
     */
    public static String getPlainCurrentTime() {
        return getTime(PLAIN_PATTERN);
    }

    public static String getTime(String pattern) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, CHINA);
        return dateFormat.format(date);
    }

    /**
     * 转换自己想要的日期格式
     *
     * @param time
     * @param pattern
     * @return
     */
    public static String getTime(String time, String pattern) {
        Date date = null;
        if (TextUtils.isEmpty(time)) {
            date = new Date();
        } else {
            date = parse(time, NORMAL_PATTERN);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, CHINA);
        return dateFormat.format(date);
    }

    /**
     * 转换自己想要的日期格式
     *
     * @param time
     * @param pattern
     * @return
     */
    public static String getTime(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, CHINA);
        return dateFormat.format(date);
    }

    private static String getTime(String pattern, int day) {
        Date date = new Date();
        long endTime = day * 24L * 3600L * 1000L + date.getTime();
        Date end = new Date(endTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, CHINA);
        return dateFormat.format(end);
    }

    /**
     * 设置每个阶段时间
     */
    private static final int seconds_of_1minute = 60;

    private static final int seconds_of_30minutes = 30 * 60;

    private static final int seconds_of_1hour = 60 * 60;

    private static final int seconds_of_1day = 24 * 60 * 60;

    private static final int seconds_of_15days = seconds_of_1day * 15;

    private static final int seconds_of_30days = seconds_of_1day * 30;

    private static final int seconds_of_6months = seconds_of_30days * 6;

    private static final int seconds_of_1year = seconds_of_30days * 12;

    //转换成 中文
    public static String getTimeRange(String mTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /**获取当前时间*/
        Date curDate = new Date(System.currentTimeMillis());
        String dataStrNew = sdf.format(curDate);
        Date startTime = null;
        try {
            /**将时间转化成Date*/
            curDate = sdf.parse(dataStrNew);
            if (!TextUtils.isEmpty(mTime)) {
                startTime = sdf.parse(mTime);
            } else {
                startTime = sdf.parse(sdf.format(new Date(System.currentTimeMillis() + 1)));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        /**除以1000是为了转换成秒*/
        long between = (curDate.getTime() - startTime.getTime()) / 1000;
        int elapsedTime = (int) (between);
        if (elapsedTime < seconds_of_1minute) {

            return "刚刚";
        }
        if (elapsedTime < seconds_of_1hour) {

            return elapsedTime / seconds_of_1minute + "分钟前";
        }
//        if (elapsedTime < seconds_of_1hour) {
//
//            return "半小时前";
//        }
        if (elapsedTime < seconds_of_1day) {

            return elapsedTime / seconds_of_1hour + "小时前";
        }
        if (elapsedTime < seconds_of_30days) {

            return elapsedTime / seconds_of_1day + "天前";
        }
//        if (elapsedTime < seconds_of_30days) {
//
//            return "半个月前";
//        }
        if (elapsedTime < seconds_of_1year) {

            return elapsedTime / seconds_of_30days + "月前";
        }
//        if (elapsedTime < seconds_of_1year) {
//
//            return "半年前";
//        }
        if (elapsedTime >= seconds_of_1year) {

            return elapsedTime / seconds_of_1year + "年前";
        }
        return "";
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */

    public static Date parse(String strDate, String pattern) {

        if (TextUtils.isEmpty(strDate)) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern, CHINA);
            return df.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //默认时间
    public static String getDefTime(String s) {
        if (!TextUtils.isEmpty(s)) {
            return s;
        }
        return getCurrentTime();
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */

    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern, CHINA);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    //获取当前的年份
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance(CHINA);
        return calendar.get(Calendar.YEAR);
    }

    //比较2个时间的长短
    public static long getTimeCha(String start, String end, String pattern) {
        Date startDate = TimeUtil.parse(start, pattern);
        Date endDate = TimeUtil.parse(end, pattern);
        if (startDate != null && endDate != null) {
            return endDate.getTime() - startDate.getTime();
        }
        return 0;
    }

    //比较2个时间的长短
    public static long getTimeCha(String start, String end) {
        return getTimeCha(start, end, TimeUtil.NORMAL_PATTERN);
    }

    /**
     * 获取时间 天:时:分;秒
     *
     * @return
     */
    public static String getTimeMatch(long time) {
        int d = 1000 * 60 * 60 * 24;//3600
        int h = 1000 * 60 * 60;//3600
        int m = 1000 * 60;
        long day = time / d;
        long l = time % d;
        long hour = l / h;
        long min = l % h / m;
        long sec = l % h % m / 1000;
        StringBuilder str = new StringBuilder();
        if (day > 0) {
            str.append(day);
            str.append("天");
        } else {
            str.append("0天");
        }
        if (hour > 0) {
            if (hour <= 9) {
                str.append("0");
            }
            str.append(hour);
        } else {
            str.append("00");
        }
        str.append(":");
        if (min > 0) {
            if (min <= 9) {
                str.append("0");
            }
            str.append(min);
        } else {
            str.append("00");
        }
        str.append(":");
        if (sec > 0) {
            if (sec <= 9) {
                str.append("0");
            }
            str.append(sec);
        } else {
            str.append("00");
        }
        return str.toString();
    }

    /**
     * 获取时间 天:时:分;秒
     *
     * @return
     */
    public static String getTimeMS(long time) {
        int m = 1000 * 60;
        long min = time / m;
        long sec = time % m / 1000;
        StringBuilder str = new StringBuilder();
        if (min > 0) {
            if (min <= 9) {
                str.append("0");
            }
            str.append(min);
        } else {
            str.append("00");
        }
        str.append(":");
        if (sec > 0) {
            if (sec <= 9) {
                str.append("0");
            }
            str.append(sec);
        } else {
            str.append("00");
        }
        return str.toString();
    }

    /**
     * 获取时间 天:时:分;秒
     *
     * @return
     */
    public static String getTimeShort(long time) {
        int d = 1000 * 60 * 60 * 24;//3600
        int h = 1000 * 60 * 60;//3600
        int m = 1000 * 60;
        long day = time / d;
        long l = time % d;
        long hour = l / h;
        long min = l % h / m;
        long sec = l % h % m / 1000;
        if (day > 0) {
            return day + "天" + hour + "时" + min + "分" + sec + "秒";
        }
        if (hour > 0) {
            return hour + "时" + min + "分" + sec + "秒";
        }
        if (min > 0) {
            return min + "分" + sec + "秒";
        }
        return sec + "秒";
    }

    /**
     * 获取时间 时:分;秒
     *
     * @param time 秒
     * @return
     */
    public static String getTimeLimit(long time) {
        int h = 60 * 60;
        int m = 60;
        long hour = time / h;
        long min = time % h / m;
        long sec = time % h % m;
        StringBuilder str = new StringBuilder();
        if (hour > 0) {
            str.append(hour);
            str.append("小时");
            if (min > 0) {
                str.append(min);
                if (sec > 0) {
                    str.append("分");
                } else {
                    str.append("分钟");
                }
            }
            if (sec > 0) {
                str.append(sec);
                str.append("秒");
            }
        } else if (min > 0) {
            str.append(min);
            if (sec > 0) {
                str.append("分");
                str.append(sec);
                str.append("秒");
            } else {
                str.append("分钟");
            }
        } else {
            str.append(sec);
            str.append("秒");
        }
        return str.toString();
    }

    /**
     * 获取时间 小时:分;秒
     *
     * @return
     */
    public static String getTimeAll(long time) {
        int h = 1000 * 60 * 60;//3600
        int m = 1000 * 60;
        long hour = time / h;
        long min = time % h / m;
        long sec = time % h % m / 1000;
        if (hour > 9) {
            if (min > 9) {
                if (sec > 9) {
                    return hour + "时" + min + "分" + sec + "秒";
                } else {
                    return hour + "时" + min + "分" + "0" + sec + "秒";
                }
            } else {
                if (sec > 9) {
                    return hour + "时" + "0" + min + "分" + sec + "秒";
                } else {
                    return hour + "时" + "0" + min + "分" + "0" + sec + "秒";
                }
            }
        } else {
            if (min > 9) {
                if (sec > 9) {
                    return "0" + hour + "时" + min + "分" + sec + "秒";
                } else {
                    return "0" + hour + "时" + min + "分" + "0" + sec + "秒";
                }
            } else {
                if (sec > 9) {
                    return "0" + hour + "时" + "0" + min + "分" + sec + "秒";
                } else {
                    return "0" + hour + "时" + "0" + min + "分" + "0" + sec + "秒";
                }
            }
        }
    }

    /**
     * 获取时间 天：小时:分;秒  的集合
     *
     * @return
     */
    public static List<String> getTimeList(long time) {
        List<String> str = new ArrayList<>();
        int d = 1000 * 60 * 60 * 24;//3600
        int h = 1000 * 60 * 60;//3600
        int m = 1000 * 60;
        long day = time / d;
        long l = time % d;
        long hour = l / h;
        long min = l % h / m;
        long sec = l % h % m / 1000;
        str.add(day + "");
        if (hour > 9) {
            str.add(hour + "");
        } else {
            str.add("0" + hour);
        }
        if (min > 9) {
            str.add(min + "");
        } else {
            str.add("0" + min);
        }
        if (sec > 9) {
            str.add(sec + "");
        } else {
            str.add("0" + sec);
        }
        return str;
    }

    //毫秒转秒
    public static String long2String(long time) {

        //毫秒转秒
        int sec = (int) time / 1000;
        int min = sec / 60;    //分钟
        sec = sec % 60;        //秒
        if (min < 10) {    //分钟补0
            if (sec < 10) {    //秒补0
                return "0" + min + ":0" + sec;
            } else {
                return "0" + min + ":" + sec;
            }
        } else {
            if (sec < 10) {    //秒补0
                return min + ":0" + sec;
            } else {
                return min + ":" + sec;
            }
        }

    }


    public static String getTimeReord(long time) {
        int m = 1000 * 60;
        long min = time / m;
        long sec = time % m / 1000;
        if (min > 0) {
            return min + "m" + sec + "s";
        }
        return sec + "s";
    }
}
