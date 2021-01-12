package software.ecenter.study.bean;

/**
 * Message
 * Create by Administrator
 * Create by 2019/10/28
 */
public class MatchDetail {

    /**
     * status : 1
     * message :
     * data : {"id":"14","name":"阅读大赛2019年10月28日14:10:53","imgUrl":null,"enrollEndDate":"2019-10-31","matchBeginDate":"2019-11-01","matchEndDate":"2019-11-03","timeLimit":"3600","description":"<p>哈哈哈<\/p>","isEnroll":false,"isEnd":false}
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
         * id : 14
         * name : 阅读大赛2019年10月28日14:10:53
         * imgUrl : null
         * enrollEndDate : 2019-10-31
         * matchBeginDate : 2019-11-01
         * matchEndDate : 2019-11-03
         * timeLimit : 3600
         * description : <p>哈哈哈</p>
         * isEnroll : false
         * isEnd : false
         */

        private String id;
        private String name;
        private String imgUrl;
        private String enrollEndDate;
        private String matchBeginDate;
        private String matchEndDate;
        private String timeLimit;
        private String description;
        private boolean isEnroll;
        private boolean isMatch;
        private boolean isEnd;
        private boolean isSetPrize;
        private boolean isEndEnroll;

        public boolean isEndEnroll() {
            return isEndEnroll;
        }

        public void setEndEnroll(boolean endEnroll) {
            isEndEnroll = endEnroll;
        }

        public boolean isSetPrize() {
            return isSetPrize;
        }

        public void setSetPrize(boolean setPrize) {
            isSetPrize = setPrize;
        }

        public boolean isEnroll() {
            return isEnroll;
        }

        public void setEnroll(boolean enroll) {
            isEnroll = enroll;
        }

        public boolean isMatch() {
            return isMatch;
        }

        public void setMatch(boolean match) {
            isMatch = match;
        }

        public boolean isEnd() {
            return isEnd;
        }

        public void setEnd(boolean end) {
            isEnd = end;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getEnrollEndDate() {
            return enrollEndDate;
        }

        public void setEnrollEndDate(String enrollEndDate) {
            this.enrollEndDate = enrollEndDate;
        }

        public String getMatchBeginDate() {
            return matchBeginDate;
        }

        public void setMatchBeginDate(String matchBeginDate) {
            this.matchBeginDate = matchBeginDate;
        }

        public String getMatchEndDate() {
            return matchEndDate;
        }

        public void setMatchEndDate(String matchEndDate) {
            this.matchEndDate = matchEndDate;
        }

        public String getTimeLimit() {
            return timeLimit;
        }

        public void setTimeLimit(String timeLimit) {
            this.timeLimit = timeLimit;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isIsEnroll() {
            return isEnroll;
        }

        public void setIsEnroll(boolean isEnroll) {
            this.isEnroll = isEnroll;
        }

        public boolean isIsEnd() {
            return isEnd;
        }

        public void setIsEnd(boolean isEnd) {
            this.isEnd = isEnd;
        }
    }
}
