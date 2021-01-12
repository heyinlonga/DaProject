package software.ecenter.study.bean.MineBean;

import java.util.List;

import software.ecenter.study.bean.HomeBean.MybagBean;

/**
 * Created by Mike on 2018/5/16.
 */

public class MyBuyDetailBean {
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
        List<MyBuyBean> data;
        public List<MyBuyBean> getData(){
            return data;
        }
        public void setData(List<MyBuyBean> data){
            this.data = data;
        }
    }
}
