package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mike on 2018/4/25.
 */

public class BookDetailBean implements Serializable {

    private static final long serialVersionUID = -4452050444296800995L;
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

    public class Data implements Serializable{
        private boolean hasSecurityCode;            //图书是否需要防伪验证，true:显示两个价格，false：显示一个价格
        private String bookImage;                   //图书图片
        private String bookName;                    //图书名称
        private String bookIntroduction;            //图书简介
        private String bookPrice;                   //图书价格
        private String bookSalePrice;               //正版优惠价
        private List<PackageBean> packageList;      //套系集合
        private List<ChapterItemBean> chapterList;  //章节目录
        private List<CurriculumBean> curriculumList;//课程集合
        private boolean isBuy;                      //是否已经获取
        private boolean isShowCategory;             //是否显示目录
        private List<ResourceBean> resourceList;    //
        private boolean isFreeBook;                 //是否免费图书
        private boolean isBookHaveFile;             //是否上传了物理资源
        private boolean isBind;                     //用户是否绑定，hasSecurityCode为true时，true：不提示验证绑定，false：提示验证绑定
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

        public boolean isBind() {
            return isBind;
        }

        public void setBind(boolean bind) {
            isBind = bind;
        }

        public List<ResourceBean> getResourceList() {
            return resourceList;
        }

        public void setResourceList(List<ResourceBean> resourceList) {
            this.resourceList = resourceList;
        }

        public boolean isFreeBook() {
            return isFreeBook;
        }

        public void setFreeBook(boolean freeBook) {
            isFreeBook = freeBook;
        }

        public boolean isBookHaveFile() {
            return isBookHaveFile;
        }

        public void setBookHaveFile(boolean bookHaveFile) {
            isBookHaveFile = bookHaveFile;
        }

        public boolean isShowCategory() {
            return isShowCategory;
        }

        public void setShowCategory(boolean showCategory) {
            isShowCategory = showCategory;
        }

        public List<ResourceBean> getResourceBeanList() {
            return resourceList;
        }

        public void setResourceBeanList(List<ResourceBean> resourceList) {
            this.resourceList = resourceList;
        }

        public String getBookSalePrice() {
            return bookSalePrice;
        }

        public void setBookSalePrice(String bookSalePrice) {
            this.bookSalePrice = bookSalePrice;
        }

        public void setBuy(boolean buy) {
            isBuy = buy;
        }

        public boolean isBuy() {
            return isBuy;
        }

        public void setIsBuy(boolean isBuy) {
            this.isBuy = isBuy;
        }

        public boolean isHasSecurityCode() {
            return hasSecurityCode;
        }

        public void setHasSecurityCode(boolean hasSecurityCode) {
            this.hasSecurityCode = hasSecurityCode;
        }

        public String getBookImage() {
            return bookImage;
        }

        public void setBookImage(String bookImage) {
            this.bookImage = bookImage;
        }

        public String getBookName() {
            return bookName;
        }

        public void setBookName(String bookName) {
            this.bookName = bookName;
        }

        public String getBookIntroduction() {
            return bookIntroduction;
        }

        public void setBookIntroduction(String bookIntroduction) {
            this.bookIntroduction = bookIntroduction;
        }

        public String getBookPrice() {
            return bookPrice;
        }

        public void setBookPrice(String bookPrice) {
            this.bookPrice = bookPrice;
        }

        public List<PackageBean> getPackageList() {
            return packageList;
        }

        public void setPackageList(List<PackageBean> packageList) {
            this.packageList = packageList;
        }

        public List<ChapterItemBean> getChapterList() {
            return chapterList;
        }

        public void setChapterList(List<ChapterItemBean> chapterList) {
            this.chapterList = chapterList;
        }

        public List<CurriculumBean> getCurriculumList() {
            return curriculumList;
        }

        public void setCurriculumList(List<CurriculumBean> curriculumList) {
            this.curriculumList = curriculumList;
        }
    }


}
