package software.ecenter.study.bean;

/**
 * Created by zyt on 2018/7/9.
 */

public class BindBookBean {

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

        private boolean isBuy;
        private String msg;
        private boolean hasSecurityCode;

        public boolean isBuy() {
            return isBuy;
        }

        public void setBuy(boolean buy) {
            isBuy = buy;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public boolean isHasSecurityCode() {
            return hasSecurityCode;
        }

        public void setHasSecurityCode(boolean hasSecurityCode) {
            this.hasSecurityCode = hasSecurityCode;
        }
    }
}
