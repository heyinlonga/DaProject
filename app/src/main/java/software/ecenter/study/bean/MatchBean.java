package software.ecenter.study.bean;

import java.util.List;

/**
 * Message
 * Create by Administrator
 * Create by 2019/10/28
 */
public class MatchBean {
    private int status;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean{
        private List<MatchList> inProgressList;
        private List<MatchList> beginSoonList;
        private List<MatchList> endList;

        public List<MatchList> getInProgressList() {
            return inProgressList;
        }

        public void setInProgressList(List<MatchList> inProgressList) {
            this.inProgressList = inProgressList;
        }

        public List<MatchList> getBeginSoonList() {
            return beginSoonList;
        }

        public void setBeginSoonList(List<MatchList> beginSoonList) {
            this.beginSoonList = beginSoonList;
        }

        public List<MatchList> getEndList() {
            return endList;
        }

        public void setEndList(List<MatchList> endList) {
            this.endList = endList;
        }
    }
}
