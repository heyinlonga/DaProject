package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;
import java.util.List;

import software.ecenter.study.bean.QuestionBean.QuestionBean;

/**
 * Created by Mike on 2018/4/25.
 */

public class SearchBean implements Serializable  {

    private static final long serialVersionUID = 7543833902002564261L;
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
        public List<QuestionBean> questionList;
        public List<BookBean> bookList;
        public List<ResourceBean> resourceList;


        private boolean hasQuestionNextPage;
        private boolean hasBookNextPage;
        private boolean hasResourNextPage;

        private int curpage;
        private int bookCurPage;
        private int resourceCurPage;
        private int questionCurPage;

        public List<QuestionBean> getQuestionList() {
            return questionList;
        }

        public void setQuestionList(List<QuestionBean> questionList) {
            this.questionList = questionList;
        }

        public List<BookBean> getBookList() {
            return bookList;
        }

        public void setBookList(List<BookBean> bookList) {
            this.bookList = bookList;
        }

        public List<ResourceBean> getResourceList() {
            return resourceList;
        }

        public void setResourceList(List<ResourceBean> resourceList) {
            this.resourceList = resourceList;
        }

        public boolean isHasQuestionNextPage() {
            return hasQuestionNextPage;
        }

        public void setHasQuestionNextPage(boolean hasQuestionNextPage) {
            this.hasQuestionNextPage = hasQuestionNextPage;
        }

        public boolean isHasBookNextPage() {
            return hasBookNextPage;
        }

        public void setHasBookNextPage(boolean hasBookNextPage) {
            this.hasBookNextPage = hasBookNextPage;
        }

        public boolean isHasResourNextPage() {
            return hasResourNextPage;
        }

        public void setHasResourNextPage(boolean hasResourNextPage) {
            this.hasResourNextPage = hasResourNextPage;
        }

        public int getCurpage() {
            return curpage;
        }

        public void setCurpage(int curpage) {
            this.curpage = curpage;
        }

        public int getBookCurPage() {
            return bookCurPage;
        }

        public void setBookCurPage(int bookCurPage) {
            this.bookCurPage = bookCurPage;
        }

        public int getResourceCurPage() {
            return resourceCurPage;
        }

        public void setResourceCurPage(int resourceCurPage) {
            this.resourceCurPage = resourceCurPage;
        }

        public int getQuestionCurPage() {
            return questionCurPage;
        }

        public void setQuestionCurPage(int questionCurPage) {
            this.questionCurPage = questionCurPage;
        }
    }


}
