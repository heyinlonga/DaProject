package software.ecenter.study.utils;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Message: 微信分享
 * Created by  ChenLong.
 * Created by Time on 2018/2/27.
 */

public class Share implements Serializable {
    String url;//分享链接
    String title;//分享标题
    String contant;//分享内容
    String img;//qq分享的logo
    Bitmap thumb;//缩略图
    int pic;//微信的分享的logo
    int type;//好友与圈子

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContant() {
        return contant;
    }

    public void setContant(String contant) {
        this.contant = contant;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
