package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/5/11.
 */

public class ExerciseAnswerBean implements Serializable {
    private static final long serialVersionUID = 120812763625831546L;
    private String answerContent;
    private boolean answerIsRight;
    private String answerId;

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getAnswerId() {
        return answerId;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public boolean isAnswerIsRight() {
        return answerIsRight;
    }

    public void setAnswerIsRight(boolean answerIsRight) {
        this.answerIsRight = answerIsRight;
    }
}
