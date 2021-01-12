package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;

/**
 * Created by zyt on 2018/6/22.
 */

public class RecommendBookListBean implements Serializable {
    /**
     * recommendPackageId : 4
     * recommendPackage : 套系组合4
     * recommendPackageProice : 4学习币/8积分
     */

    private String recommendBookId;
    private String recommendBook;
    private String recommendBookProice;
    private String discountPrice;

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getRecommendBookId() {
        return recommendBookId;
    }

    public void setRecommendBookId(String recommendBookId) {
        this.recommendBookId = recommendBookId;
    }

    public String getRecommendBook() {
        return recommendBook;
    }

    public void setRecommendBook(String recommendBook) {
        this.recommendBook = recommendBook;
    }

    public String getRecommendBookProice() {
        return recommendBookProice;
    }

    public void setRecommendBookProice(String recommendBookProice) {
        this.recommendBookProice = recommendBookProice;
    }
}
