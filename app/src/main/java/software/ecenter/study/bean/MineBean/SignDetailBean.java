package software.ecenter.study.bean.MineBean;

/**
 * Created by Mike on 2018/5/15.
 */

public class SignDetailBean {
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
        private int isSign;
        private String signList;
        private String  signDay;
        private String signIntegral;

        public int getIsSign() {
            return isSign;
        }

        public void setIsSign(int isSign) {
            this.isSign = isSign;
        }

        public String getSignList() {
            return signList;
        }

        public void setSignList(String signList) {
            this.signList = signList;
        }

        public String getSignDay() {
            return signDay;
        }

        public void setSignDay(String signDay) {
            this.signDay = signDay;
        }

        public String getSignIntegral() {
            return signIntegral;
        }

        public void setSignIntegral(String signIntegral) {
            this.signIntegral = signIntegral;
        }
    }
}
