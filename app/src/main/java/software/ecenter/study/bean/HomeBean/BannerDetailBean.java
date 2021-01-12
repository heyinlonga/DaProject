package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mike on 2018/4/25.
 */

public class BannerDetailBean implements Serializable  {

    private static final long serialVersionUID = -1919231584494447324L;
    private int status;
    private String message;
    private Data data;



    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private String bannerImage;
        private String bannerContent;
        private String bannerTitle;

        public String getBannerImage() {
            return bannerImage;
        }

        public void setBannerImage(String bannerImage) {
            this.bannerImage = bannerImage;
        }

        public String getBannerContent() {
            return bannerContent;
        }

        public void setBannerContent(String bannerContent) {
            this.bannerContent = bannerContent;
        }

        public String getBannerTitle() {
            return bannerTitle;
        }

        public void setBannerTitle(String bannerTitle) {
            this.bannerTitle = bannerTitle;
        }
    }


}
