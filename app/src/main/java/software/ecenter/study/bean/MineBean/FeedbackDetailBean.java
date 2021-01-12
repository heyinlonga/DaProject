package software.ecenter.study.bean.MineBean;

import java.util.List;

/**
 * Created by Mike on 2018/5/16.
 */

public class FeedbackDetailBean {
    private int status;
    private String message;

    private List<FeedbackBean> data;

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

    public List<FeedbackBean> getData() {
        return data;
    }

    public void setData(List<FeedbackBean> data) {
        this.data = data;
    }
}
