package software.ecenter.study.utils;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by zyt on 2018/6/21.
 */

public class DateUtils {
    public String formetFileSize(double fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "K";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "M";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "G";
        }
        if (fileSizeString.indexOf(".") == 0) {
            fileSizeString = "0" + fileSizeString;
        }
        return fileSizeString;
    }
}

