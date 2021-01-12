package software.ecenter.study.bean.HomeBean;

import java.util.List;

/**
 * auther: zzm
 * Date: 2019/8/14
 * Description:
 */
public class HomeMoreQualityEducationBean {

    /**
     * status : 1
     * message : 
     * data : {"curpage":"0","hasNextPage":false,"qualityEducationList":[{"imgUrl":"imgUrl","coinPrice":"0","name":"test0719新建","id":"114","type":4},{"imgUrl":"","coinPrice":"13","name":"test0719导入","id":"113","type":4},{"imgUrl":"","coinPrice":"1","name":"素质教育h5测试","id":"116","type":4}]}
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
         * hasNextPage : false
         * qualityEducationList : [{"imgUrl":"imgUrl","coinPrice":"0","name":"test0719新建","id":"114","type":4},{"imgUrl":"","coinPrice":"13","name":"test0719导入","id":"113","type":4},{"imgUrl":"","coinPrice":"1","name":"素质教育h5测试","id":"116","type":4}]
         */

        private String curpage;
        private boolean hasNextPage;
        private List<QualityEducationListBean> qualityEducationList;

        public String getCurpage() {
            return curpage;
        }

        public void setCurpage(String curpage) {
            this.curpage = curpage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public List<QualityEducationListBean> getQualityEducationList() {
            return qualityEducationList;
        }

        public void setQualityEducationList(List<QualityEducationListBean> qualityEducationList) {
            this.qualityEducationList = qualityEducationList;
        }

        public static class QualityEducationListBean {
            /**
             * imgUrl : imgUrl
             * coinPrice : 0
             * name : test0719新建
             * id : 114
             * type : 4
             */

            private String imgUrl;
            private String coinPrice;
            private String name;
            private String id;
            private int type;
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
    }
}
