package software.ecenter.study.bean;

import java.util.List;

/**
 * Message
 * Create by Administrator
 * Create by 2019/11/5
 */
public class ReportDetailBean {

    /**
     * data : {"askCount":0,"compositionCount":0,"exerciseCount":0,"id":4,"matchCount":0,"nickname":"13757141734","practiceCount":0,"price":4,"reportDate":"2019-9","resourceCount":0}
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
         * askCount : 0
         * compositionCount : 0
         * exerciseCount : 0
         * id : 4
         * matchCount : 0
         * nickname : 13757141734
         * practiceCount : 0
         * price : 4
         * reportDate : 2019-9
         * resourceCount : 0
         */

        private int askCount;
        private int compositionCount;
        private int exerciseCount;
        private String id;
        private int matchCount;
        private int chineseExerciseCount;
        private int mathExerciseCount;
        private int englishExerciseCount;
        private int matchExerciseCount;
        private String nickname;
        private int practiceCount;
        private int price;
        private boolean isBuy;
        private boolean isFree;
        private String status;
        private String reportDate;
        private List<String> pieChartName;
        private List<String> pieColourNum;
        private List<Integer> pieChartNum;
        private int resourceCount;
        private int bonusDiscount;
        private int couponDiscount;
        private int coinDiscount;
        private long endTime;
        private boolean isDiscount;

        public List<String> getPieChartName() {
            return pieChartName;
        }

        public void setPieChartName(List<String> pieChartName) {
            this.pieChartName = pieChartName;
        }

        public List<String> getPieColourNum() {
            return pieColourNum;
        }

        public void setPieColourNum(List<String> pieColourNum) {
            this.pieColourNum = pieColourNum;
        }

        public List<Integer> getPieChartNum() {
            return pieChartNum;
        }

        public void setPieChartNum(List<Integer> pieChartNum) {
            this.pieChartNum = pieChartNum;
        }

        public int getBonusDiscount() {
            return bonusDiscount;
        }

        public void setBonusDiscount(int bonusDiscount) {
            this.bonusDiscount = bonusDiscount;
        }

        public int getCouponDiscount() {
            return couponDiscount;
        }

        public void setCouponDiscount(int couponDiscount) {
            this.couponDiscount = couponDiscount;
        }

        public int getCoinDiscount() {
            return coinDiscount;
        }

        public void setCoinDiscount(int coinDiscount) {
            this.coinDiscount = coinDiscount;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public boolean isDiscount() {
            return isDiscount;
        }

        public void setDiscount(boolean discount) {
            isDiscount = discount;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getMatchExerciseCount() {
            return matchExerciseCount;
        }

        public void setMatchExerciseCount(int matchExerciseCount) {
            this.matchExerciseCount = matchExerciseCount;
        }

        public int getEnglishExerciseCount() {
            return englishExerciseCount;
        }

        public void setEnglishExerciseCount(int englishExerciseCount) {
            this.englishExerciseCount = englishExerciseCount;
        }

        public int getMathExerciseCount() {
            return mathExerciseCount;
        }

        public void setMathExerciseCount(int mathExerciseCount) {
            this.mathExerciseCount = mathExerciseCount;
        }

        public int getChineseExerciseCount() {
            return chineseExerciseCount;
        }

        public void setChineseExerciseCount(int chineseExerciseCount) {
            this.chineseExerciseCount = chineseExerciseCount;
        }

        public int getAskCount() {
            return askCount;
        }

        public void setAskCount(int askCount) {
            this.askCount = askCount;
        }

        public int getCompositionCount() {
            return compositionCount;
        }

        public void setCompositionCount(int compositionCount) {
            this.compositionCount = compositionCount;
        }

        public int getExerciseCount() {
            return exerciseCount;
        }

        public void setExerciseCount(int exerciseCount) {
            this.exerciseCount = exerciseCount;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getMatchCount() {
            return matchCount;
        }

        public void setMatchCount(int matchCount) {
            this.matchCount = matchCount;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getPracticeCount() {
            return practiceCount;
        }

        public void setPracticeCount(int practiceCount) {
            this.practiceCount = practiceCount;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getReportDate() {
            return reportDate;
        }

        public void setReportDate(String reportDate) {
            this.reportDate = reportDate;
        }

        public int getResourceCount() {
            return resourceCount;
        }

        public void setResourceCount(int resourceCount) {
            this.resourceCount = resourceCount;
        }
    }
}
