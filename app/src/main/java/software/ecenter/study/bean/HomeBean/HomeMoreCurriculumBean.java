package software.ecenter.study.bean.HomeBean;

import java.util.List;

/**
 * auther: zzm
 * Date: 2019/8/14
 * Description:
 */
public class HomeMoreCurriculumBean {

    /**
     * status : 1
     * message : 
     * data : {"curpage":"0","hasCurriculumNextPage":false,"curriculumList":[{"curriculumName":"test06131","curriculumPrice":"10","curriculumImage":"","curriculumId":"44"},{"curriculumName":"test06132","curriculumPrice":"10","curriculumImage":"","curriculumId":"45"},{"curriculumName":"1","curriculumPrice":"1","curriculumImage":"","curriculumId":"6"}]}
     */

    private int status;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * curpage : 0
         * hasCurriculumNextPage : false
         * curriculumList : [{"curriculumName":"test06131","curriculumPrice":"10","curriculumImage":"","curriculumId":"44"},{"curriculumName":"test06132","curriculumPrice":"10","curriculumImage":"","curriculumId":"45"},{"curriculumName":"1","curriculumPrice":"1","curriculumImage":"","curriculumId":"6"}]
         */

        private String curpage;
        private boolean hasCurriculumNextPage;
        private List<CurriculumListBean> curriculumList;

        public String getCurpage() {
            return curpage;
        }

        public void setCurpage(String curpage) {
            this.curpage = curpage;
        }

        public boolean isHasCurriculumNextPage() {
            return hasCurriculumNextPage;
        }

        public void setHasCurriculumNextPage(boolean hasCurriculumNextPage) {
            this.hasCurriculumNextPage = hasCurriculumNextPage;
        }

        public List<CurriculumListBean> getCurriculumList() {
            return curriculumList;
        }

        public void setCurriculumList(List<CurriculumListBean> curriculumList) {
            this.curriculumList = curriculumList;
        }

        public static class CurriculumListBean {

            /**
             * imgUrl : 
             * coinPrice : 10
             * name : test06131
             * id : 44
             * type : CURRICULUM
             */

            private String imgUrl;
            private String coinPrice;
            private String name;
            private String id;
            private String type;
            private int categoryShowType;
            private boolean isDiscount;
            private String salePrice;

            public String getSalePrice() {
                return salePrice;
            }

            public void setSalePrice(String salePrice) {
                this.salePrice = salePrice;
            }
            public boolean isDiscount() {
                return isDiscount;
            }

            public void setDiscount(boolean discount) {
                isDiscount = discount;
            }
            public int getCategoryShowType() {
                return categoryShowType;
            }

            public void setCategoryShowType(int categoryShowType) {
                this.categoryShowType = categoryShowType;
            }

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

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
