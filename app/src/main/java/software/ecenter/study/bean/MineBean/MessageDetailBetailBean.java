package software.ecenter.study.bean.MineBean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/5/15.
 */

public class MessageDetailBetailBean implements Serializable {
    private static final long serialVersionUID = 3795061781072109641L;

    private int status;
    private String message;
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private String messageTitle;
        private String messageContext;
        private String messageHead;
        private String type;
        private String feedbackProblems;
        private String opinion;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMessageHead() {
            return messageHead;
        }

        public void setMessageHead(String messageHead) {
            this.messageHead = messageHead;
        }

        public String getFeedbackProblems() {
            return feedbackProblems;
        }

        public void setFeedbackProblems(String feedbackProblems) {
            this.feedbackProblems = feedbackProblems;
        }

        public String getOpinion() {
            return opinion;
        }

        public void setOpinion(String opinion) {
            this.opinion = opinion;
        }

        public String getMessageTitle() {
            return messageTitle;
        }

        public void setMessageTitle(String messageTitle) {
            this.messageTitle = messageTitle;
        }

        public String getMessageContext() {
            return messageContext;
        }

        public void setMessageContext(String messageContext) {
            this.messageContext = messageContext;
        }
    }
}
