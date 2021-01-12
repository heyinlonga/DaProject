package software.ecenter.study.bean.QuestionBean;

import java.io.Serializable;
import java.util.List;

import software.ecenter.study.bean.HomeBean.BookBean;

/**
 * Created by Mike on 2018/5/14.
 */

public class DayiDetailBean implements Serializable {
    private static final long serialVersionUID = 8362651758411455737L;
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
        private List<BookBean> bookList;

        public List<BookBean> getBookList() {
            return bookList;
        }

        public void setBookList(List<BookBean> bookList) {
            this.bookList = bookList;
        }
    }



}
