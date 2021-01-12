package software.ecenter.study.utils;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        //  html加载完成之后，调用js的方法
        imgReset(view);
        setWebImageClick(view);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    private void imgReset(WebView wvBannerContent) {
        wvBannerContent.loadUrl("javascript:(function(){"
                + "var objs = document.getElementsByTagName('img'); "
                + "for(var i=0;i<objs.length;i++)  " + "{"
                + "var img = objs[i];   "
                + "    img.style.width = 'auto';   "
                + "    img.style.max-width = '100%';   "
                + "    img.style.height = 'auto';   "
                + "}" + "})()");
    }

    /**
     * 設置網頁中圖片的點擊事件
     *
     * @param view
     */
    private void setWebImageClick(WebView view) {
        String jsCode = "javascript:(function(){" +
                "var imgs=document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<imgs.length;i++){" +
                "imgs[i].onclick=function(){" +
                "window.jsCallJavaObj.showBigImg(this.src);" +
                "}}})()";
        view.loadUrl(jsCode);
    }
}
