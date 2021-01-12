package software.ecenter.study.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Message
 * Create by Administrator
 * Create by 2019/11/5
 */
public class ReportList implements Serializable {

    /**
     * data : [{"isBuy":false,"isFree":false,"reportMonth":9,"reportYear":2019,"title":"2019年9月份学情报告"},{"isBuy":false,"isFree":false,"reportMonth":10,"reportYear":2019,"title":"2019年10月份学情报告"},{"isBuy":false,"isFree":false,"reportMonth":11,"reportYear":2019,"title":"2019年11月份学情报告"}]
     * message :
     * status : 1
     */

    private String message;
    private int status;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * isBuy : false
         * isFree : false
         * reportMonth : 9
         * reportYear : 2019
         * title : 2019年9月份学情报告
         */

        private boolean isBuy;
        private boolean isFree;
        private int reportMonth;
        private int reportYear;
        private String id;
        private String title;
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isBuy() {
            return isBuy;
        }

        public void setBuy(boolean buy) {
            isBuy = buy;
        }

        public boolean isFree() {
            return isFree;
        }

        public void setFree(boolean free) {
            isFree = free;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isIsBuy() {
            return isBuy;
        }

        public void setIsBuy(boolean isBuy) {
            this.isBuy = isBuy;
        }

        public boolean isIsFree() {
            return isFree;
        }

        public void setIsFree(boolean isFree) {
            this.isFree = isFree;
        }

        public int getReportMonth() {
            return reportMonth;
        }

        public void setReportMonth(int reportMonth) {
            this.reportMonth = reportMonth;
        }

        public int getReportYear() {
            return reportYear;
        }

        public void setReportYear(int reportYear) {
            this.reportYear = reportYear;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
