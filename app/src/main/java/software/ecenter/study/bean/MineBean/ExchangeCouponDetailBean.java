package software.ecenter.study.bean.MineBean;

/**
 * Created by Mike on 2018/5/15.
 */

public class ExchangeCouponDetailBean {

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
        private int proportion;

        public int getProportion() {
            return proportion;
        }

        public void setProportion(int proportion) {
            this.proportion = proportion;
        }
    }
}
