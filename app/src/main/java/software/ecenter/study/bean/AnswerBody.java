package software.ecenter.study.bean;

import java.util.List;

/**
 * Message
 * Create by Administrator
 * Create by 2019/11/4
 */
public class AnswerBody {


    /**
     * answerList : [{"id":"","myAnswer":""}]
     * beginTime :
     * endTime :
     * isOverTime : true
     * timeCost :
     */
    private String id;
    private String beginTime;
    private String endTime;
    private boolean isOverTime;
    private long timeCost;
    private List<AnswerBean> answerList;

    public static class AnswerBean {
        private String id;
        private String myAnswer;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMyAnswer() {
            return myAnswer;
        }

        public void setMyAnswer(String myAnswer) {
            this.myAnswer = myAnswer;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isOverTime() {
        return isOverTime;
    }

    public void setOverTime(boolean overTime) {
        isOverTime = overTime;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isIsOverTime() {
        return isOverTime;
    }

    public void setIsOverTime(boolean isOverTime) {
        this.isOverTime = isOverTime;
    }

    public long getTimeCost() {
        return timeCost;
    }

    public void setTimeCost(long timeCost) {
        this.timeCost = timeCost;
    }

    public List<AnswerBean> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<AnswerBean> answerList) {
        this.answerList = answerList;
    }
}
