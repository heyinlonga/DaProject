package software.ecenter.study.bean.QuestionBean;

import java.io.Serializable;
import java.util.List;

import software.ecenter.study.bean.HomeBean.payTypeBean;
import software.ecenter.study.bean.PayBean;

/**
 * Created by Mike on 2018/5/14.
 */

public class QuestionPayDetailBean implements Serializable {
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

    public class Data implements Serializable{

        private static final long serialVersionUID = -8125305268607960329L;
        private String hasBuy;
        private String questionId;
        private String resourceId;
        private List<payTypeBean> payTypeData;

        public String getResourceId() {
            return resourceId;
        }

        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }

        public String getHasBuy() {
            return hasBuy;
        }

        public void setHasBuy(String hasBuy) {
            this.hasBuy = hasBuy;
        }

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public List<payTypeBean> getPayTypeData() {
            return payTypeData;
        }

        public void setPayTypeData(List<payTypeBean> payTypeData) {
            this.payTypeData = payTypeData;
        }
    }

}
