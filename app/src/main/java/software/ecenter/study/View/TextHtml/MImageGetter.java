package software.ecenter.study.View.TextHtml;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.text.Html.ImageGetter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * @author gaozhen
 *         重新的imagegetter类
 */
public class MImageGetter implements ImageGetter {
    Context c;
    TextView container;

    public MImageGetter(TextView text, Context c) {
        this.c = c;
        this.container = text;
    }

    public Drawable getDrawable(String source) {
        final LevelListDrawable drawable = new LevelListDrawable();
        Glide.with(c).load(source).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (resource != null) {
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(resource);
                    Bitmap bm = bitmapDrawable.getBitmap();
                    drawable.addLevel(1, 1, bitmapDrawable);
                    int width = container.getWidth();
                    double multiple =(double) bm.getWidth() / bm.getHeight();

                    drawable.setBounds(0, 0, width, (int)(width / multiple) );
                    drawable.setLevel(1);
                    container.invalidate();
                    container.setText(container.getText()); // 解决图文重叠
                }
            }
        });


        return drawable;
    }

}
