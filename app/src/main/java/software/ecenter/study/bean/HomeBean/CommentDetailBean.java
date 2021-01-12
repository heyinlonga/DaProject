package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mike on 2018/4/25.
 */

public class CommentDetailBean implements Serializable  {

    private static final long serialVersionUID = 1256159771719990427L;
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
        private int hasNextpage; //1 有 0 没有
        private int curpage;
        private List<CommentBean> commentsList;

        public int getHasNextpage() {
            return hasNextpage;
        }

        public void setHasNextpage(int hasNextpage) {
            this.hasNextpage = hasNextpage;
        }

        public int getCurpage() {
            return curpage;
        }

        public void setCurpage(int curpage) {
            this.curpage = curpage;
        }

        public List<CommentBean> getCommentsList() {
            return commentsList;
        }

        public void setCommentsList(List<CommentBean> commentsList) {
            this.commentsList = commentsList;
        }
    }


}
