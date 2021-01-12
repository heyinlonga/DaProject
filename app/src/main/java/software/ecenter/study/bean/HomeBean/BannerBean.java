package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/4/25.
 * 广告
 */

public class BannerBean implements Serializable  {

    private static final long serialVersionUID = -6085388279080989327L;
    private String bannerId;
    private String bannerImage;

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }
}
