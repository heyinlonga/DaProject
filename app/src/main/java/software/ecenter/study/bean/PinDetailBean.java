package software.ecenter.study.bean;

import java.util.List;

/**
 * Message
 * Create by Administrator
 * Create by 2019/11/21
 */
public class PinDetailBean {

    /**
     * status : 1
     * message :
     * data : [{"id":"1","title":null,"audioUrl":null},{"id":"2","title":null,"audioUrl":null}]
     */

    private int status;
    private String message;
    private List<PinTiBean> data;

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

    public List<PinTiBean> getData() {
        return data;
    }

    public void setData(List<PinTiBean> data) {
        this.data = data;
    }

}
