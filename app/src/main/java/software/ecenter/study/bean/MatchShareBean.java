package software.ecenter.study.bean;

/**
 * Message
 * Create by Administrator
 * Create by 2019/11/1
 */
public class MatchShareBean {

    /**
     * data : {"correctNum":0,"date":"2019-10-24","matchName":"阅读大赛2019年10月27日17:20:26","timeCost":2000,"title":"状元","totalNum":1}
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
         * correctNum : 0
         * date : 2019-10-24
         * matchName : 阅读大赛2019年10月27日17:20:26
         * timeCost : 2000
         * title : 状元
         * totalNum : 1
         */

        private int correctNum;
        private String date;
        private String matchName;
        private int timeCost;
        private String title;
        private String prizeName;
        private int totalNum;

        public String getPrizeName() {
            return prizeName;
        }

        public void setPrizeName(String prizeName) {
            this.prizeName = prizeName;
        }

        public int getCorrectNum() {
            return correctNum;
        }

        public void setCorrectNum(int correctNum) {
            this.correctNum = correctNum;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getMatchName() {
            return matchName;
        }

        public void setMatchName(String matchName) {
            this.matchName = matchName;
        }

        public int getTimeCost() {
            return timeCost;
        }

        public void setTimeCost(int timeCost) {
            this.timeCost = timeCost;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }
    }
}
