package software.ecenter.study.bean.QuestionBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mike on 2018/5/14.
 */

public class sectionDetailBean implements Serializable {
    private static final long serialVersionUID = -6247951445273348424L;

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
        private List<sectionBean> sectionList;

        public List<sectionBean> getSectionList() {
            return sectionList;
        }

        public void setSectionList(List<sectionBean> sectionList) {
            this.sectionList = sectionList;
        }
    }


}
