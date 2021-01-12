package software.ecenter.study.bean.MineBean;

import com.bumptech.glide.load.engine.Resource;

import java.util.List;

import software.ecenter.study.bean.HomeBean.ResourceBean;

/**
 * Created by Mike on 2018/5/16.
 */

public class MyCollectionDetailBean {

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
        private List<ResourceBean> data;

        public List<ResourceBean> getData() {
            return data;
        }

        public void setData(List<ResourceBean> data) {
            this.data = data;
        }
    }

}
