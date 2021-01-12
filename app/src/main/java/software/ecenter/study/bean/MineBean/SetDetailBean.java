package software.ecenter.study.bean.MineBean;

import java.io.Serializable;
import java.util.List;

import software.ecenter.study.bean.QuestionBean.chapterBean;

/**
 * Created by Mike on 2018/5/14.
 */

public class SetDetailBean implements Serializable {

    private static final long serialVersionUID = -4633233610215092302L;

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
        private boolean MessageTipOpen;

        public String AppVersion;

        public boolean isMessageTipOpen() {
            return MessageTipOpen;
        }

        public void setMessageTipOpen(boolean messageTipOpen) {
            MessageTipOpen = messageTipOpen;
        }

        public String getAppVersion() {
            return AppVersion;
        }

        public void setAppVersion(String appVersion) {
            AppVersion = appVersion;
        }
    }

}
