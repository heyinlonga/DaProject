package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;

/**
 * Message
 * Create by Administrator
 * Create by 2019/6/22
 */
public class RecommendCurriculumBean implements Serializable {
    private String recommendCurriculumId;
    private String recommendCurriculum;
    private String recommendCurriculumProice;
    private String discountPrice;

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getRecommendCurriculumId() {
        return recommendCurriculumId;
    }

    public void setRecommendCurriculumId(String recommendCurriculumId) {
        this.recommendCurriculumId = recommendCurriculumId;
    }

    public String getRecommendCurriculum() {
        return recommendCurriculum;
    }

    public void setRecommendCurriculum(String recommendCurriculum) {
        this.recommendCurriculum = recommendCurriculum;
    }

    public String getRecommendCurriculumProice() {
        return recommendCurriculumProice;
    }

    public void setRecommendCurriculumProice(String recommendCurriculumProice) {
        this.recommendCurriculumProice = recommendCurriculumProice;
    }
}
