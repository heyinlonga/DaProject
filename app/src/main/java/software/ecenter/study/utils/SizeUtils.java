package software.ecenter.study.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;

import butterknife.internal.Utils;

/**
 * Created by zyt on 2018/7/12.
 */

public  class SizeUtils {
    /**
     * dp转px
     */
// 将px值转换为dip或dp值
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    // 将dp或dip值转换为px值
    public static int dip2px(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue * scale + 0.5f);
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Logger.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

}
