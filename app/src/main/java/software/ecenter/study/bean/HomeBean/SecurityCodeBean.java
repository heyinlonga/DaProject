package software.ecenter.study.bean.HomeBean;

/**
 * Created by zyt on 2018/5/31.
 */

public class SecurityCodeBean {

    /**
     * status : 1
     * message :
     * data : {"msg":"您所查询的防伪码正确有效。","isCanBind":true,"isCorrect":true}
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
         * msg : 您所查询的防伪码正确有效。
         * isCanBind : true
         * isCorrect : true
         */

        private String msg;
        private boolean isCanBind;
        private boolean isCorrect;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public boolean isIsCanBind() {
            return isCanBind;
        }

        public void setIsCanBind(boolean isCanBind) {
            this.isCanBind = isCanBind;
        }

        public boolean isIsCorrect() {
            return isCorrect;
        }

        public void setIsCorrect(boolean isCorrect) {
            this.isCorrect = isCorrect;
        }
    }
}
