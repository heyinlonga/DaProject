package software.ecenter.study.utils;

import java.util.Calendar;

/**
 * Created by zyt on 2018/6/21.
 */

public class DataUtils {
    private int mMonth;
    private int mDay;

    public int getmMonth() {
        return mMonth;
    }

    public int getmDay() {
        return mDay;
    }

    public DataUtils invoke() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        int mWay = c.get(Calendar.DAY_OF_WEEK);// 获取当前日期的星期
        int mHour = c.get(Calendar.HOUR_OF_DAY);//时
        int mMinute = c.get(Calendar.MINUTE);//分
        return this;
    }

    private String to2Str(int i) {

        if (i > 9) {

            return i + "";
        } else {

            return "0" + i;
        }

    }

    public String toTimeStr(int secTotal) {

        String result = "";
        int hour = secTotal / 3600;
        int min = (secTotal ) / 60;
        int sec = (secTotal ) % 60;
        result = to2Str(min) + ":" + to2Str(sec);

        return result;
    }
}

