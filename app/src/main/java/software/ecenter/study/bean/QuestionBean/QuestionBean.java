package software.ecenter.study.bean.QuestionBean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/4/25.
 */

public class QuestionBean implements Serializable {

    private static final long serialVersionUID = -414476732838099495L;
    private String questionId;
    private String questionName;
    private int questionStatus;
    private String questionTitle;
    private String questionDate;
    private String audioDuration;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public int getQuestionStatus() {
        return questionStatus;
    }

    public void setQuestionStatus(int questionStatus) {
        this.questionStatus = questionStatus;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionDate() {
        return questionDate;
    }

    public void setQuestionDate(String questionDate) {
        this.questionDate = questionDate;
    }
}
