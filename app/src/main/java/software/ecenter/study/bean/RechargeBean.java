package software.ecenter.study.bean;

/**
 * Created by Mike on 2018/5/2.
 */

public class RechargeBean {
    private String rechargeBi;
    private String rechargeMoney;
    private String rechargeJiFen;
    private boolean check;

    public String getRechargeJiFen() {
        return rechargeJiFen;
    }

    public void setRechargeJiFen(String rechargeJiFen) {
        this.rechargeJiFen = rechargeJiFen;
    }

    public String getRechargeBi() {
        return rechargeBi;
    }

    public void setRechargeBi(String rechargeBi) {
        this.rechargeBi = rechargeBi;
    }

    public String getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(String rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
