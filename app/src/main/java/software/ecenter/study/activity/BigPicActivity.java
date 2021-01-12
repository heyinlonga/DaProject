package software.ecenter.study.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.bumptech.glide.Glide;

import software.ecenter.study.R;
import software.ecenter.study.View.MatrixImageView;
import software.ecenter.study.tool.ImageCacheManager;
/***
 * dsc 大图查看
 *
 * */
public class BigPicActivity extends BaseActivity {
    private MatrixImageView mImageView = null;
    private String mImageUrl = null;
    private String mImagePath = null;
    private ImageCacheManager mImageCacheManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_pic);

        mImageView = (MatrixImageView) findViewById(R.id.img_photo);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);

        int maxWidth = metrics.widthPixels;
        int maxHeight = metrics.heightPixels;

        Intent intent = getIntent();

        mImagePath = intent.getStringExtra("ImagePath");
        if (mImagePath == null) {
            mImageUrl = intent.getStringExtra("ImageUrl");
            if (mImageUrl != null) {
         /*       mImageCacheManager = new ImageCacheManager(this, R.drawable.morentu, false);
                //按屏幕的宽高来，防止加载原图内存溢出
                mImageCacheManager.setImage(mImageUrl, mImageView, maxWidth, maxHeight);*/
                Glide.with(mContext).load(mImageUrl).into(mImageView);//用户头像
            }
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(mImagePath);
            if (bitmap != null) {
                mImageView.setImageBitmap(bitmap);
            }
        }

    }


}
