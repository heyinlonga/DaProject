package software.ecenter.study.bean;

/**
 * Message
 * Create by Administrator
 * Create by 2019/12/9
 */
public class AfterBean {

    /**
     * status : 1
     * message :
     * data : {"afterClassId":41}
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
         * afterClassId : 41
         */

        private int afterClassId;

        public int getAfterClassId() {
            return afterClassId;
        }

        public void setAfterClassId(int afterClassId) {
            this.afterClassId = afterClassId;
        }
    }
}
