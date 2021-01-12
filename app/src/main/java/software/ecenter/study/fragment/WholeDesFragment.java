package software.ecenter.study.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import software.ecenter.study.R;

/**
 * Message 视频讲座简介
 * Create by Administrator
 * Create by 2019/8/15
 */
public class WholeDesFragment extends BaseFragment {

    private WebView webview;
    public static WholeDesFragment newIntence(Bundle bundle){
        WholeDesFragment fragment = new WholeDesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_wholedes, container, false);
        webview = inflate.findViewById(R.id.webview);
        webview.setBackgroundColor(0);
        Bundle arguments = getArguments();
        if (arguments!=null){
            String text = arguments.getString("text");
            if (TextUtils.isEmpty(text)){
                text = "暂无简介";
            }
            String html = "<html><header>" + css + "</header>" + text + "</html>";
            webview.loadDataWithBaseURL(null,html,"text/html","utf-8",null);
        }
        return inflate;
    }
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
}
