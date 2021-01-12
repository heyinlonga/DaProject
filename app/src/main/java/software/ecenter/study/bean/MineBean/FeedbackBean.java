package software.ecenter.study.bean.MineBean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/4/25.
 */

public class FeedbackBean implements Serializable  {
    private static final long serialVersionUID = -6953361436212641422L;
    private String feedbackId;
    private String feedbackTitle;
    private String feedbackContext;
    private boolean isCheck;//选中

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getFeedbackTitle() {
        return feedbackTitle;
    }

    public void setFeedbackTitle(String feedbackTitle) {
        this.feedbackTitle = feedbackTitle;
    }

    public String getFeedbackContext() {
        return feedbackContext;
    }

    public void setFeedbackContext(String feedbackContext) {
        this.feedbackContext = feedbackContext;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
