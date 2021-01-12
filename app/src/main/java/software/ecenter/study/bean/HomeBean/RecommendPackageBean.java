package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;

/**
 * Created by zyt on 2018/6/22.
 */

public class RecommendPackageBean implements Serializable {
    /**
     * recommendPackageId : 4
     * recommendPackage : 套系组合4
     * recommendPackageProice : 4学习币/8积分
     */

    private String recommendPackageId;
    private String recommendPackage;
    private String recommendPackageProice;

    public String getRecommendPackageId() {
        return recommendPackageId;
    }

    public void setRecommendPackageId(String recommendPackageId) {
        this.recommendPackageId = recommendPackageId;
    }

    public String getRecommendPackage() {
        return recommendPackage;
    }

    public void setRecommendPackage(String recommendPackage) {
        this.recommendPackage = recommendPackage;
    }

    public String getRecommendPackageProice() {
        return recommendPackageProice;
    }

    public void setRecommendPackageProice(String recommendPackageProice) {
        this.recommendPackageProice = recommendPackageProice;
    }

}
