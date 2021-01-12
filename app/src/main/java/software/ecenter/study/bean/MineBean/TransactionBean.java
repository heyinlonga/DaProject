package software.ecenter.study.bean.MineBean;

/**
 * Created by Mike on 2018/5/15.
 */

public class TransactionBean {
    private String transactionType;
    private String transactionNum;
    private String transactionUnit;
    private String transactionData;

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionNum() {
        return transactionNum;
    }

    public void setTransactionNum(String transactionNum) {
        this.transactionNum = transactionNum;
    }

    public String getTransactionUnit() {
        return transactionUnit;
    }

    public void setTransactionUnit(String transactionUnit) {
        this.transactionUnit = transactionUnit;
    }

    public String getTransactionData() {
        return transactionData;
    }

    public void setTransactionData(String transactionData) {
        this.transactionData = transactionData;
    }
}
