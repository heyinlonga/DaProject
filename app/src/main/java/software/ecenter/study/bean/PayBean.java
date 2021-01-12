package software.ecenter.study.bean;

/**
 * Created by Mike on 2018/5/2.
 */

public class PayBean {
    private int  pay;  //1、微信 2、支付宝 3、学习币 4、积分
    private String paynum;
    private String payunity;
    private boolean check;

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public String getPaynum() {
        return paynum;
    }

    public void setPaynum(String paynum) {
        this.paynum = paynum;
    }

    public String getPayunity() {
        return payunity;
    }

    public void setPayunity(String payunity) {
        this.payunity = payunity;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
