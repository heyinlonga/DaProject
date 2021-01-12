package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mike on 2018/4/25.
 */

public class PackageDetailBean implements Serializable {

    private static final long serialVersionUID = 4003990973568516560L;
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
        private boolean hasSecurityCode;
        private String packageId;
        private String packageImage;
        private String packageName;
        private String packagePrice;
        private String packageSalePrice;
        private String packageIntroduction;
        private String bookId;
        private String bookImage;
        private String bookName;
        private List<CurriculumBean> curriculumList;
        private List<BookBean> bookList;
        private boolean isBuy;
        private boolean isBind;
        private boolean isHaveFile;

        public boolean isHaveFile() {
            return isHaveFile;
        }

        public void setHaveFile(boolean haveFile) {
            isHaveFile = haveFile;
        }

        public boolean isBind() {
            return isBind;
        }

        public void setBind(boolean bind) {
            isBind = bind;
        }

        public List<BookBean> getBookBeanList() {
            return bookList;
        }

        public void setBookBeanList(List<BookBean> bookBeanList) {
            this.bookList = bookBeanList;
        }

        public String getPackageSalePrice() {
            return packageSalePrice;
        }

        public void setPackageSalePrice(String packageSalePrice) {
            this.packageSalePrice = packageSalePrice;
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

        public String getPackageId() {
            return packageId;
        }

        public void setPackageId(String packageId) {
            this.packageId = packageId;
        }

        public String getPackageImage() {
            return packageImage;
        }

        public void setPackageImage(String packageImage) {
            this.packageImage = packageImage;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getPackagePrice() {
            return packagePrice;
        }

        public void setPackagePrice(String packagePrice) {
            this.packagePrice = packagePrice;
        }

        public String getPackageIntroduction() {
            return packageIntroduction;
        }

        public void setPackageIntroduction(String packageIntroduction) {
            this.packageIntroduction = packageIntroduction;
        }

        public String getBookId() {
            return bookId;
        }

        public void setBookId(String bookId) {
            this.bookId = bookId;
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

        public List<CurriculumBean> getCurriculumList() {
            return curriculumList;
        }

        public void setCurriculumList(List<CurriculumBean> curriculumList) {
            this.curriculumList = curriculumList;
        }
    }


}
