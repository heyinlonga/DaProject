package software.ecenter.study.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzonePublish;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import software.ecenter.study.R;


/**
 * Message:  分享工具类
 * Created by  ChenLong.
 * Created by Time on 2018/4/26.
 */

public class ShareUtil {
    public static final String SHOP_URL = "http://art.yuanjiantouzi.com/store-box/store-goodsDetail/";
    public static final String PAI_URL = "http://art.yuanjiantouzi.com/auction-box/auction-goodsdetail/";

    //qq分享的显示logo
    private static String path = "https://yourdekan-1257262986.cos.ap-beijing.myqcloud.com/Android/20190919/1568893154336.png";

    /**
     * 分享纯图片到QQzone
     */
    public static void shareImgToQQzone(final Activity activity, Share share) {
        Bundle params = new Bundle();
//        params.putInt(QzonePublish.PUBLISH_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD);
//        params.putString(QzonePublish.PUBLISH_TO_QZONE_SUMMARY, "小状元分享");
        ArrayList<String> list = new ArrayList<>();
        list.add(share.getUrl());
//        params.putStringArrayList(QzonePublish.PUBLISH_TO_QZONE_IMAGE_URL,
//                list);// 图片地址ArrayList
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_APP);// 设置分享类型为纯图片分享
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);// 需要分享的本地图片URL
        // 分享操作要在主线程中完成
        Tencent.createInstance(ConstantValue.APP_ID_QQ, activity).shareToQzone(activity, params, new IUiListener() {
            @Override
            public void onCancel() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToastSHORT(activity, "分享取消");
                    }
                });
            }

            @Override
            public void onComplete(Object response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToastSHORT(activity, "分享成功");
                    }
                });

            }

            @Override
            public void onError(final UiError e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToastSHORT(activity, "分享失败：" + e.errorMessage);
                    }
                });
            }
        });
    }

    /**
     * 分享纯图片到QQ
     */
    public static void shareImgToQQ(final Activity activity, Share share) {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);// 设置分享类型为纯图片分享
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, share.getUrl());// 需要分享的本地图片URL
        // 分享操作要在主线程中完成
        Tencent.createInstance(ConstantValue.APP_ID_QQ, activity).shareToQQ(activity, params, new IUiListener() {
            @Override
            public void onCancel() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToastSHORT(activity, "分享取消");
                    }
                });
            }

            @Override
            public void onComplete(Object response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToastSHORT(activity, "分享成功");
                    }
                });

            }

            @Override
            public void onError(final UiError e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToastSHORT(activity, "分享失败：" + e.errorMessage);
                    }
                });
            }
        });
    }

    //qq空间分享1qq好友2
    public static void shareQQ(final Activity activity, Share share) {
        Bundle params = new Bundle();
        if (share != null && share.getType() == 2) {
            params.putString(QQShare.SHARE_TO_QQ_TITLE, share.getTitle());
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, share.getUrl());
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, share.getContant());
            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.getString(R.string.app_name));
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, ToolUtil.getString(share.getImg(), path));
        } else {
            params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
            params.putString(QzoneShare.SHARE_TO_QQ_TITLE, share.getTitle());
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, share.getUrl());
            params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, share.getContant());
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, ToolUtil.getString(share.getImg(), path));
        }
        Tencent.createInstance(ConstantValue.APP_ID_QQ, activity).shareToQQ(activity, params, new IUiListener() {
            @Override
            public void onCancel() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToastSHORT(activity, "分享取消");
                    }
                });
            }

            @Override
            public void onComplete(Object response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToastSHORT(activity, "分享成功");
                    }
                });

            }

            @Override
            public void onError(final UiError e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToastSHORT(activity, "分享失败：" + e.errorMessage);
                    }
                });
            }
        });
    }

    //分享 链接
    public static void shareWX(Activity activity, Share share) {
        IWXAPI api = WXAPIFactory.createWXAPI(activity, ConstantValue.APP_ID, true);
        api.registerApp(ConstantValue.APP_ID);
        // 判断是否安装了微信客户端
        if (!api.isWXAppInstalled()) {
            Toast.makeText(activity, "您还未安装微信客户端！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(activity, "正在登录微信", Toast.LENGTH_LONG).show();
        }
        WXWebpageObject wxWeb = new WXWebpageObject();
        wxWeb.webpageUrl = share.getUrl();//分享的url
        WXMediaMessage msg = new WXMediaMessage(wxWeb);
        msg.title = share.getTitle();//标题
        msg.description = share.getContant();//内容描述
        if (share.getThumb() != null) {//缩略图
            Bitmap thumb = share.getThumb();
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(thumb, 40, 40, false);
            msg.thumbData = bitmap2Bytes(scaledBitmap,32);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), share.getPic());
            msg.thumbData = bitmap2Bytes(bitmap,32);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        int type = share.getType();
        if (type == 1) {//朋友圈
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        } else if (type == 2) {//好友
            req.scene = SendMessageToWX.Req.WXSceneSession;
        }
        api.sendReq(req);
    }

    //分享图片
    public static void imageShare(Activity activity, Share share) {
        IWXAPI api = WXAPIFactory.createWXAPI(activity, ConstantValue.APP_ID, true);
        api.registerApp(ConstantValue.APP_ID);
        // 判断是否安装了微信客户端
        if (!api.isWXAppInstalled()) {
            Toast.makeText(activity, "您还未安装微信客户端！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(activity, "正在登录微信", Toast.LENGTH_LONG).show();
        }
        Bitmap bitmap = BitmapFactory.decodeFile(share.getUrl());
        WXImageObject imgObj = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        bitmap.recycle();
        msg.thumbData = bmpTobyte(thumbBmp);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        int type = share.getType();
        if (type == 1) {//朋友圈
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        } else if (type == 2) {//好友
            req.scene = SendMessageToWX.Req.WXSceneSession;
        }
        api.sendReq(req);
    }

    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static byte[] bmpTobyte(Bitmap bmp) {
        if (bmp == null) {
            return null;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        bmp.recycle();
        return stream.toByteArray();
    }

    /**
     * Bitmap转换成byte[]并且进行压缩,压缩到不大于maxkb
     *
     * @param bmp
     * @param maxkb
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bmp, int maxkb) {
        if (bmp == null) {
            return null;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int options = 100;
        bmp.compress(Bitmap.CompressFormat.PNG, options, stream);
        while (stream.toByteArray().length > maxkb && options != 10) {
            int len = stream.toByteArray().length;
            stream.reset(); //清空output
            bmp.compress(Bitmap.CompressFormat.PNG, options, stream);//这里压缩options%，把压缩后的数据存放到output中
            options -= 10;
        }
        bmp.recycle();
        return stream.toByteArray();
    }


    public static boolean checkInstallation(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    //分享到微信 朋友圈 多图
    public static void showPYQ(Activity contexts, List<String> fileList) {
        try {
            if (!checkInstallation(contexts, "com.tencent.mm")) {
                ToastUtils.showToastSHORT(contexts, "没有安装微信");
                return;
            }
            ArrayList<Uri> imageUris = new ArrayList<>();
            for (int i = 0; i < fileList.size(); i++) {
                File file = new File(fileList.get(i));//图片文件
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    imageUris.add(Uri.fromFile(file));
                } else {
                    //修复微信在7.0崩溃的问题
                    Uri uri = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(contexts.getContentResolver(), file.getAbsolutePath(), file.getName(), null));
                    imageUris.add(uri);
                }
            }
            Intent intent = new Intent();
            //com.tencent.mm.ui.tools.ShareImgUi   是分享到微信好友
            //com.tencent.mm.ui.tools.ShareToTimeLineUI  是分享到微信朋友圈
            ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
            intent.setComponent(comp);
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("image/*");

            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
//            intent.putExtra("Kdescription", "我分享成功啦");  // 这里可要可不要，这句话的意思是直接会显示在发表时候的文字
            contexts.startActivity(intent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ToastUtils.showToastSHORT(contexts, "分享失败！");
        }
    }
//    // 下载图片保存至手机
//    public void showLoadingImage(String urlPath, String fileName, int index) {
//        try {
//            URL u = new URL(urlPath);
//            String path = ToolUtils.createDir(activity, fileName, getString(R.string.directory_path));
//            byte[] buffer = new byte[1024 * 8];
//            int read;
//            int ava = 0;
//            long start = System.currentTimeMillis();
//            BufferedInputStream bin;
//            bin = new BufferedInputStream(u.openStream());
//            BufferedOutputStream bout = new BufferedOutputStream(
//                    new FileOutputStream(path));
//            while ((read = bin.read(buffer)) > -1) {
//                bout.write(buffer, 0, read);
//                ava += read;
//                long speed = ava / (System.currentTimeMillis() - start);
//            }
//            bout.flush();
//            bout.close();
////            activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path))));
//            Message message = handler.obtainMessage();
//            message.what = index;
//            message.obj = path;
//            handler.sendMessage(message);
//        } catch (IOException e) {
//            Log.d("111111file", "图片保存失败\n" + e.getMessage());
//            e.printStackTrace();
//        }
//    }
}
