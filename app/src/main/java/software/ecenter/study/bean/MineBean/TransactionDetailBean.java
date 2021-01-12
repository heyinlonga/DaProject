package software.ecenter.study.bean.MineBean;

import java.util.List;

/**
 * Created by Mike on 2018/5/15.
 */

public class TransactionDetailBean {
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

    public class  Data{
        List<TransactionBean> data;

        public List<TransactionBean> getData() {
            return data;
        }

        public void setData(List<TransactionBean> data) {
            this.data = data;
        }
    }

}
