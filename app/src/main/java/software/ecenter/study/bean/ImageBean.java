package software.ecenter.study.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Mike on 2018/5/7.
 */

public class ImageBean  implements Serializable{
    private static final long serialVersionUID = -1459967661638095389L;
    private Bitmap ThumBitmap;
    private String targetPicPath;
    private boolean isAddImage; //添加图片的显示

    public Bitmap getThumBitmap() {
        return ThumBitmap;
    }

    public void setThumBitmap(Bitmap thumBitmap) {
        ThumBitmap = thumBitmap;
    }

    public String getTargetPicPath() {
        return targetPicPath;
    }

    public void setTargetPicPath(String targetPicPath) {
        this.targetPicPath = targetPicPath;
    }

    public boolean isAddImage() {
        return isAddImage;
    }

    public void setAddImage(boolean addImage) {
        isAddImage = addImage;
    }
}
