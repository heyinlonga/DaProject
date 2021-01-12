package software.ecenter.study.bean;

import java.util.List;

/**
 * Message
 * Create by Administrator
 * Create by 2019/6/22
 */
public class TypeCommentBean {
    private int status;
    private String message;
    private List<String> data;

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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
