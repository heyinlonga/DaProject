package software.ecenter.study.bean;

import java.util.List;

/**
 * Message
 * Create by Administrator
 * Create by 2019/8/15
 */
public class WholeBean {

    /**
     * data : {"canBuyGroup":false,"categoryShowType":0,"coinPrice":10,"description":"<p>1<\/p>","detail":"<p>1888<\/p>","id":5,"imgUrl":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/CurriculumImg/b19c213e414b4645a42d86ea90123b04","isBuy":false,"name":"1","resourceList":[]}
     * message :
     * status : 1
     */

    private DataBean data;
    private String message;
    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * canBuyGroup : false
         * categoryShowType : 0
         * coinPrice : 10
         * description : <p>1</p>
         * detail : <p>1888</p>
         * id : 5
         * imgUrl : http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/CurriculumImg/b19c213e414b4645a42d86ea90123b04
         * isBuy : false
         * name : 1
         * resourceList : []
         */

        private boolean canBuyGroup;
        private int categoryShowType;
        private int coinPrice;
        private List<CateBean> categoryList;
        private String description;
        private String detail;
        private int id;
        private String imgUrl;
        private boolean isBuy;
        private String name;
        private List<CourseBean> resourceList;
        private boolean isDiscount;                     //是否有优惠
        private String discountType;                     //优惠类别1：积分赠送，2：价格优惠，3：答疑券赠送
        private String discountDetail;                     //具体优惠数值 积分、答疑券、优惠价
        private long endTime;                     //时间  秒
        private int bonusDiscount;                     //积分
        private int coinDiscount;                     //优惠价
        private int couponDiscount;                     //答疑券

        public int getBonusDiscount() {
            return bonusDiscount;
        }

        public void setBonusDiscount(int bonusDiscount) {
            this.bonusDiscount = bonusDiscount;
        }

        public int getCoinDiscount() {
            return coinDiscount;
        }

        public void setCoinDiscount(int coinDiscount) {
            this.coinDiscount = coinDiscount;
        }

        public int getCouponDiscount() {
            return couponDiscount;
        }

        public void setCouponDiscount(int couponDiscount) {
            this.couponDiscount = couponDiscount;
        }

        public boolean isDiscount() {
            return isDiscount;
        }

        public void setDiscount(boolean discount) {
            isDiscount = discount;
        }

        public String getDiscountType() {
            return discountType;
        }

        public void setDiscountType(String discountType) {
            this.discountType = discountType;
        }

        public String getDiscountDetail() {
            return discountDetail;
        }

        public void setDiscountDetail(String discountDetail) {
            this.discountDetail = discountDetail;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public List<CateBean> getCategoryList() {
            return categoryList;
        }

        public void setCategoryList(List<CateBean> categoryList) {
            this.categoryList = categoryList;
        }

        public boolean isBuy() {
            return isBuy;
        }

        public void setBuy(boolean buy) {
            isBuy = buy;
        }

        public boolean isCanBuyGroup() {
            return canBuyGroup;
        }

        public void setCanBuyGroup(boolean canBuyGroup) {
            this.canBuyGroup = canBuyGroup;
        }

        public int getCategoryShowType() {
            return categoryShowType;
        }

        public void setCategoryShowType(int categoryShowType) {
            this.categoryShowType = categoryShowType;
        }

        public int getCoinPrice() {
            return coinPrice;
        }

        public void setCoinPrice(int coinPrice) {
            this.coinPrice = coinPrice;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public boolean isIsBuy() {
            return isBuy;
        }

        public void setIsBuy(boolean isBuy) {
            this.isBuy = isBuy;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<CourseBean> getResourceList() {
            return resourceList;
        }

        public void setResourceList(List<CourseBean> resourceList) {
            this.resourceList = resourceList;
        }
    }
}
