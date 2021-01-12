package software.ecenter.study.bean.QuestionBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mike on 2018/5/14.
 */

public class ChapterDetailBean implements Serializable {

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
        private List<chapterBean> chapterList;

        public List<chapterBean> getChapterList() {
            return chapterList;
        }

        public void setChapterList(List<chapterBean> chapterList) {
            this.chapterList = chapterList;
        }
    }

}
