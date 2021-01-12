package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;

/**
 * Message
 * Create by Administrator
 * Create by 2019/9/18
 */
public class QualityEducationBean implements Serializable {

    /**
     * imgUrl : https://zygxkt.oss-cn-beijing.aliyuncs.com/BookPackageImg/2019_%E7%B2%BE%E5%93%81%E8%AF%BE%E7%A8%8B_%E5%AE%B6%E5%BA%AD%E7%B4%A0%E8%B4%A8%E6%95%99%E8%82%B2%E8%AF%BE.jpg
     * coinPrice : 18
     * name : 龙门书局家庭教育讲堂
     * id : 117
     * type : 3
     */

    private String imgUrl;
    private String coinPrice;
    private String name;
    private String id;
    private int type;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCoinPrice() {
        return coinPrice;
    }

    public void setCoinPrice(String coinPrice) {
        this.coinPrice = coinPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
