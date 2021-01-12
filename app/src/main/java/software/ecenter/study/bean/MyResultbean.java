package software.ecenter.study.bean;

/**
 * Message
 * Create by Administrator
 * Create by 2019/10/31
 */
public class MyResultbean {

    /**
     * data : {"correct":"0","correctPercent":"0%","id":"16","isHaveWrongQuestion":true,"name":"阅读大赛2019年10月27日17:20:26","rank":"3/3","submitTime":"2019-10-24 16:55:56","timeCost":"2000","timeLimit":"3600","total":"1"}
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
         * correct : 0
         * correctPercent : 0%
         * id : 16
         * isHaveWrongQuestion : true
         * name : 阅读大赛2019年10月27日17:20:26
         * rank : 3/3
         * submitTime : 2019-10-24 16:55:56
         * timeCost : 2000
         * timeLimit : 3600
         * total : 1
         */

        private String correct;
        private String correctPercent;
        private String id;
        private boolean isHaveWrongQuestion;
        private String name;
        private String rank;
        private String submitTime;
        private String timeCost;
        private String timeLimit;
        private String total;
        private boolean isSetPrize;

        public boolean isSetPrize() {
            return isSetPrize;
        }

        public void setSetPrize(boolean setPrize) {
            isSetPrize = setPrize;
        }

        public String getCorrect() {
            return correct;
        }

        public void setCorrect(String correct) {
            this.correct = correct;
        }

        public String getCorrectPercent() {
            return correctPercent;
        }

        public void setCorrectPercent(String correctPercent) {
            this.correctPercent = correctPercent;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isIsHaveWrongQuestion() {
            return isHaveWrongQuestion;
        }

        public void setIsHaveWrongQuestion(boolean isHaveWrongQuestion) {
            this.isHaveWrongQuestion = isHaveWrongQuestion;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getSubmitTime() {
            return submitTime;
        }

        public void setSubmitTime(String submitTime) {
            this.submitTime = submitTime;
        }

        public String getTimeCost() {
            return timeCost;
        }

        public void setTimeCost(String timeCost) {
            this.timeCost = timeCost;
        }

        public String getTimeLimit() {
            return timeLimit;
        }

        public void setTimeLimit(String timeLimit) {
            this.timeLimit = timeLimit;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }
}
