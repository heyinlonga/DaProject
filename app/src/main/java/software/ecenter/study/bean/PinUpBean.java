package software.ecenter.study.bean;

/**
 * Message
 * Create by Administrator
 * Create by 2019/12/28
 */
public class PinUpBean {

    /**
     * status : 1
     * message :
     * data : {"pinduRecordId":39}
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
         * pinduRecordId : 39
         */

        private int pinduRecordId;

        public int getPinduRecordId() {
            return pinduRecordId;
        }

        public void setPinduRecordId(int pinduRecordId) {
            this.pinduRecordId = pinduRecordId;
        }
    }
}
