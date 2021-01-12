package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/5/14.
 */

public class payTypeBean implements Serializable{
    private static final long serialVersionUID = 3595350641956227071L;
    private String payType;
    private String payNum;
    private String discountNum;
    private String payUnit;
    private boolean check;

    public String getDiscountNum() {
        return discountNum;
    }

    public void setDiscountNum(String discountNum) {
        this.discountNum = discountNum;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayNum() {
        return payNum;
    }

    public void setPayNum(String payNum) {
        this.payNum = payNum;
    }

    public String getPayUnit() {
        return payUnit;
    }

    public void setPayUnit(String payUnit) {
        this.payUnit = payUnit;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
