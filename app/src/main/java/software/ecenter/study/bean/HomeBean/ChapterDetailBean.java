package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mike on 2018/4/25.
 */

public class ChapterDetailBean implements Serializable {

    private static final long serialVersionUID = 2677349948372272866L;
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
        private boolean isBuy;
        private boolean hasSecurityCode;
        private String bookId;
        private boolean isSingleBuy;
        private boolean isHaveFile;
        private boolean isHaveSpecial;
        private List<ResourceBean> resourceList;
        private boolean isBind;

        public boolean isBind() {
            return isBind;
        }

        public void setBind(boolean bind) {
            isBind = bind;
        }

        public boolean isHaveSpecial() {
            return isHaveSpecial;
        }

        public void setHaveSpecial(boolean haveSpecial) {
            isHaveSpecial = haveSpecial;
        }

        public boolean isHaveFile() {
            return isHaveFile;
        }

        public void setHaveFile(boolean haveFile) {
            isHaveFile = haveFile;
        }

        public boolean isSingleBuy() {
            return isSingleBuy;
        }

        public void setSingleBuy(boolean singleBuy) {
            isSingleBuy = singleBuy;
        }

        public boolean isBuy() {
            return isBuy;
        }

        public void setBuy(boolean buy) {
            isBuy = buy;
        }

        public boolean isHasSecurityCode() {
            return hasSecurityCode;
        }

        public void setHasSecurityCode(boolean hasSecurityCode) {
            this.hasSecurityCode = hasSecurityCode;
        }

        public String getBookId() {
            return bookId;
        }

        public void setBookId(String bookId) {
            this.bookId = bookId;
        }

        public List<ResourceBean> getResourceList() {
            return resourceList;
        }

        public void setResourceList(List<ResourceBean> resourceList) {
            this.resourceList = resourceList;
        }
    }


}
