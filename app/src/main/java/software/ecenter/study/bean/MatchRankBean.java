package software.ecenter.study.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Message
 * Create by Administrator
 * Create by 2019/10/30
 */
public class MatchRankBean {

    /**
     * data : {"correct":0,"correctPercent":"0%","id":16,"isMatch":true,"name":"阅读大赛2019年10月27日17:20:26","rank":3,"timeCost":2000,"timeLimit":3600,"total":3,"totalQuestion":1}
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
         * isMatch : true
         * name : 阅读大赛2019年10月27日17:20:26
         * rank : 3
         * timeCost : 2000
         * timeLimit : 3600
         * total : 3
         * totalQuestion : 1
         */

        private int correct;
        private String correctPercent;
        private int id;
        private boolean isMatch;
        private String name;
        private String rank;
        private int timeCost;
        private int timeLimit;
        private int total;
        private int totalQuestion;
        private boolean isSetPrice;

        public boolean isSetPrize() {
            return isSetPrice;
        }

        public void setSetPrize(boolean setPrize) {
            isSetPrice = setPrize;
        }
        public int getCorrect() {
            return correct;
        }

        public void setCorrect(int correct) {
            this.correct = correct;
        }

        public String getCorrectPercent() {
            return correctPercent;
        }

        public void setCorrectPercent(String correctPercent) {
            this.correctPercent = correctPercent;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isIsMatch() {
            return isMatch;
        }

        public void setIsMatch(boolean isMatch) {
            this.isMatch = isMatch;
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

        public int getTimeCost() {
            return timeCost;
        }

        public void setTimeCost(int timeCost) {
            this.timeCost = timeCost;
        }

        public int getTimeLimit() {
            return timeLimit;
        }

        public void setTimeLimit(int timeLimit) {
            this.timeLimit = timeLimit;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTotalQuestion() {
            return totalQuestion;
        }

        public void setTotalQuestion(int totalQuestion) {
            this.totalQuestion = totalQuestion;
        }

    }
}
