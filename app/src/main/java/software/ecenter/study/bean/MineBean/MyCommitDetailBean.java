package software.ecenter.study.bean.MineBean;

import java.util.List;

/**
 * Created by Mike on 2018/5/16.
 */

public class MyCommitDetailBean {
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
        private List<MyCommitBean> data;

        public List<MyCommitBean> getData() {
            return data;
        }

        public void setData(List<MyCommitBean> data) {
            this.data = data;
        }
    }
}
