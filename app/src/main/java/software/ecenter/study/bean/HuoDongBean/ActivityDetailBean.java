package software.ecenter.study.bean.HuoDongBean;


import java.util.List;

/**
 * Created by Mike on 2018/5/14.
 */

public class ActivityDetailBean {

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
        List<ActivityBean> hotActivityList;
        List<ActivityBean> oldActivityList;

        public List<ActivityBean> getHotActivityList() {
            return hotActivityList;
        }

        public void setHotActivityList(List<ActivityBean> hotActivityList) {
            this.hotActivityList = hotActivityList;
        }

        public List<ActivityBean> getOldActivityList() {
            return oldActivityList;
        }

        public void setOldActivityList(List<ActivityBean> oldActivityList) {
            this.oldActivityList = oldActivityList;
        }
    }
}
