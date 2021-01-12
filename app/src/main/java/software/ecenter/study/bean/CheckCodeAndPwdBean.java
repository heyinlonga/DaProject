package software.ecenter.study.bean;

/**
 * Created by zyt on 2018/7/9.
 */

public class CheckCodeAndPwdBean {

    /**
     * status : 1
     * message :
     * data : {"pwdCorrect":true,"msg":"该手机号已注册","phoneCorrect":false,"codeCorrect":true}
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
         * pwdCorrect : true
         * msg : 该手机号已注册
         * phoneCorrect : false
         * codeCorrect : true
         */

        private boolean pwdCorrect;
        private String msg;
        private boolean phoneCorrect;
        private boolean codeCorrect;

        public boolean isPwdCorrect() {
            return pwdCorrect;
        }

        public void setPwdCorrect(boolean pwdCorrect) {
            this.pwdCorrect = pwdCorrect;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public boolean isPhoneCorrect() {
            return phoneCorrect;
        }

        public void setPhoneCorrect(boolean phoneCorrect) {
            this.phoneCorrect = phoneCorrect;
        }

        public boolean isCodeCorrect() {
            return codeCorrect;
        }

        public void setCodeCorrect(boolean codeCorrect) {
            this.codeCorrect = codeCorrect;
        }
    }
}
