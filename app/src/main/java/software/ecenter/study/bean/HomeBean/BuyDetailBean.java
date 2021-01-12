package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mike on 2018/5/14.
 */

public class BuyDetailBean implements Serializable {
    private static final long serialVersionUID = 6953895911313841405L;
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

    public class Data {
        private String name;
        private String validity;
        private String recommendBook;
        private String recommendBookProice;
        private String recommendPackage;
        private String recommendPackageProice;
        private boolean isDiscount;
        private List<payTypeBean> payTypeData;
        private List<RecommendPackageBean> recommendPackageList;
        private List<RecommendBookListBean> recommendBookList;
        private List<RecommendCurriculumBean> recommendCurriculumList;

        public boolean isDiscount() {
            return isDiscount;
        }

        public void setDiscount(boolean discount) {
            isDiscount = discount;
        }

        public List<RecommendCurriculumBean> getRecommendCurriculumList() {
            return recommendCurriculumList;
        }

        public void setRecommendCurriculumList(List<RecommendCurriculumBean> recommendCurriculumList) {
            this.recommendCurriculumList = recommendCurriculumList;
        }

        public List<RecommendPackageBean> getRecommendPackageList() {
            return recommendPackageList;
        }

        public void setRecommendPackageList(List<RecommendPackageBean> recommendPackageList) {
            this.recommendPackageList = recommendPackageList;
        }

        public List<RecommendBookListBean> getRecommendBookList() {
            return recommendBookList;
        }

        public void setRecommendBookList(List<RecommendBookListBean> recommendBookList) {
            this.recommendBookList = recommendBookList;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValidity() {
            return validity;
        }

        public void setValidity(String validity) {
            this.validity = validity;
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

        public List<payTypeBean> getPayTypeData() {
            return payTypeData;
        }

        public void setPayTypeData(List<payTypeBean> payTypeData) {
            this.payTypeData = payTypeData;
        }
    }


}
