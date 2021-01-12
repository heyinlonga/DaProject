package software.ecenter.study.bean.MineBean;

import java.util.List;

import software.ecenter.study.bean.QuestionBean.QuestionBean;

/**
 * Created by Mike on 2018/5/16.
 */

public class MyQusetionDetailBean {
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
        List<QuestionBean> data;

        public List<QuestionBean> getData() {
            return data;
        }

        public void setData(List<QuestionBean> data) {
            this.data = data;
        }
    }

}
