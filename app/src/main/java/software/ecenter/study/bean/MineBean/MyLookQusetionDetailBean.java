package software.ecenter.study.bean.MineBean;

import java.util.List;

/**
 * Created by Mike on 2018/5/16.
 */

public class MyLookQusetionDetailBean {
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

    public class Data {
        private List<QusetionLookBean> questionList;
        private boolean isInquiry;
        private boolean isOver;
        private String location;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public List<QusetionLookBean> getQuestionList() {
            return questionList;
        }

        public void setQuestionList(List<QusetionLookBean> questionList) {
            this.questionList = questionList;
        }

        public boolean isOver() {
            return isOver;
        }

        public void setOver(boolean isOver) {
            this.isOver = isOver;
        }

        public boolean isInquiry() {
            return isInquiry;
        }

        public void setInquiry(boolean inquiry) {
            isInquiry = inquiry;
        }
    }
}
