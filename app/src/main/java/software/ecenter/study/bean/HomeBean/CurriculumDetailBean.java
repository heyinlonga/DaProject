package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mike on 2018/4/25.
 */

public class CurriculumDetailBean implements Serializable  {

    private static final long serialVersionUID = 5685074250941327011L;
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
        private boolean hasSecurityCode;
        private String curriculumImage;
        private String curriculumName;
        private String curriculumIntroduction;
        private String curriculumPrice;
        private List<PackageBean> packageList;
        private List<ResourceBean> resourceList;
        private List<ChapterItemBean> chapterList;  //章节目录
        private int batchBuyStatus;
        private boolean batchNotice;
        private boolean isBuy;
        private boolean isHaveFile;
        private boolean isShowCategory;
        private boolean isDiscount;                     //是否有优惠
        private String discountType;                     //优惠类别1：积分赠送，2：价格优惠，3：答疑券赠送
        private String discountDetail;                     //具体优惠数值 积分、答疑券、优惠价
        private int bonusDiscount;                     //积分
        private int coinDiscount;                     //优惠价
        private int couponDiscount;                     //答疑券
        private long endTime;                     //时间  秒
        private SecondBatch secondBatch;                     //时间  秒

        public SecondBatch getSecondBatch() {
            return secondBatch;
        }

        public void setSecondBatch(SecondBatch secondBatch) {
            this.secondBatch = secondBatch;
        }

        public class SecondBatch{
            private String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
        public int getBatchBuyStatus() {
            return batchBuyStatus;
        }

        public void setBatchBuyStatus(int batchBuyStatus) {
            this.batchBuyStatus = batchBuyStatus;
        }

        public boolean isBatchNotice() {
            return batchNotice;
        }

        public void setBatchNotice(boolean batchNotice) {
            this.batchNotice = batchNotice;
        }

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

        public List<ChapterItemBean> getChapterList() {
            return chapterList;
        }

        public void setChapterList(List<ChapterItemBean> chapterList) {
            this.chapterList = chapterList;
        }

        public boolean isShowCategory() {
            return isShowCategory;
        }

        public void setShowCategory(boolean showCategory) {
            isShowCategory = showCategory;
        }

        public void setBuy(boolean buy) {
            isBuy = buy;
        }

        public boolean isHaveFile() {
            return isHaveFile;
        }

        public void setHaveFile(boolean haveFile) {
            isHaveFile = haveFile;
        }

        public boolean isBuy(){
            return  isBuy;
        }
        public void setIsBuy(boolean isBuy){
            this.isBuy = isBuy;
        }

        public boolean isHasSecurityCode() {
            return hasSecurityCode;
        }

        public void setHasSecurityCode(boolean hasSecurityCode) {
            this.hasSecurityCode = hasSecurityCode;
        }

        public String getCurriculumImage() {
            return curriculumImage;
        }

        public void setCurriculumImage(String curriculumImage) {
            this.curriculumImage = curriculumImage;
        }

        public String getCurriculumName() {
            return curriculumName;
        }

        public void setCurriculumName(String curriculumName) {
            this.curriculumName = curriculumName;
        }

        public String getCurriculumIntroduction() {
            return curriculumIntroduction;
        }

        public void setCurriculumIntroduction(String curriculumIntroduction) {
            this.curriculumIntroduction = curriculumIntroduction;
        }

        public String getCurriculumPrice() {
            return curriculumPrice;
        }

        public void setCurriculumPrice(String curriculumPrice) {
            this.curriculumPrice = curriculumPrice;
        }

        public List<PackageBean> getPackageList() {
            return packageList;
        }

        public void setPackageList(List<PackageBean> packageList) {
            this.packageList = packageList;
        }

        public List<ResourceBean> getResourceList() {
            return resourceList;
        }

        public void setResourceList(List<ResourceBean> resourceList) {
            this.resourceList = resourceList;
        }
    }


}
