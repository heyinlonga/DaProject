package software.ecenter.study.bean;

import java.io.Serializable;

/**
 * Message
 * Create by Administrator
 * Create by 2019/8/15
 */
public class CourseBean implements Serializable {

    /**
     * id : 56
     * name : Module 1 达标卷 听力音频
     * imgUrl : http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/LectureImg/ff252797730f48e3ae6f9e369f5b4a9d
     * coinPrice : 123
     * liveDate :
     */

    private long id;
    private String name;
    private String imgUrl;
    private int coinPrice;
    private int type;
    private String liveDate;
    private boolean isHaveFile;

    public boolean isHaveFile() {
        return isHaveFile;
    }

    public void setHaveFile(boolean haveFile) {
        isHaveFile = haveFile;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getCoinPrice() {
        return coinPrice;
    }

    public void setCoinPrice(int coinPrice) {
        this.coinPrice = coinPrice;
    }

    public String getLiveDate() {
        return liveDate;
    }

    public void setLiveDate(String liveDate) {
        this.liveDate = liveDate;
    }
}
