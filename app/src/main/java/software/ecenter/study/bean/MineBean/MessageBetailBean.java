package software.ecenter.study.bean.MineBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mike on 2018/5/15.
 */

public class MessageBetailBean implements Serializable {
    private static final long serialVersionUID = 3795061781072109641L;

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
        private List<MessageBean> data;

        public List<MessageBean> getData() {
            return data;
        }

        public void setData(List<MessageBean> data) {
            this.data = data;
        }
    }
}
