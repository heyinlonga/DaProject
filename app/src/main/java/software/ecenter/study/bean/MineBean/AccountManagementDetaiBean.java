package software.ecenter.study.bean.MineBean;



/**
 * Created by Mike on 2018/5/15.
 */

public class AccountManagementDetaiBean {
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
        private String balance;
        private String couponType;
        private String couponNum;
        private String currentIntegral;
        private String currentBonus;
        private String totalIntegral;
        private String totalBonus;

        public String getCurrentIntegral() {
            return currentIntegral;
        }

        public void setCurrentIntegral(String currentIntegral) {
            this.currentIntegral = currentIntegral;
        }

        public String getTotalIntegral() {
            return totalIntegral;
        }

        public void setTotalIntegral(String totalIntegral) {
            this.totalIntegral = totalIntegral;
        }

        public String getCurrentBonus() {
            return currentBonus;
        }

        public void setCurrentBonus(String currentBonus) {
            this.currentBonus = currentBonus;
        }

        public String getTotalBonus() {
            return totalBonus;
        }

        public void setTotalBonus(String totalBonus) {
            this.totalBonus = totalBonus;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getCouponType() {
            return couponType;
        }

        public void setCouponType(String couponType) {
            this.couponType = couponType;
        }

        public String getCouponNum() {
            return couponNum;
        }

        public void setCouponNum(String couponNum) {
            this.couponNum = couponNum;
        }
    }
}
