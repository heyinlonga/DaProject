package software.ecenter.study.bean;

/**
 * Created by zyt on 2018/7/6.
 */

public class HaveNewMessageBean {

    /**
     * status : 1
     * message :
     * data : {"haveNewMessage":true}
     */

    private int status;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * haveNewMessage : true
         */

        private boolean haveNewMessage;

        public boolean isHaveNewMessage() {
            return haveNewMessage;
        }

        public void setHaveNewMessage(boolean haveNewMessage) {
            this.haveNewMessage = haveNewMessage;
        }
    }
}
