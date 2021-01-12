package software.ecenter.study.utils;

import android.text.TextUtils;

import com.bumptech.glide.load.model.GlideUrl;


/**
 * Message
 * Create by Administrator
 * Create by 2019/10/17
 */
public class MyGlideUrl extends GlideUrl {
    private String mUrl;

    public MyGlideUrl(String url) {
        super(url);
        mUrl = url;
    }

    @Override
    public String getCacheKey() {
        if (!TextUtils.isEmpty(mUrl) && mUrl.contains("?")) {
            return mUrl.substring(0, mUrl.indexOf("?"));
        }
        return mUrl;
    }
}
