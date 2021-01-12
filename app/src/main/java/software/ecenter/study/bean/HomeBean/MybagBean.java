package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mike on 2018/5/10.
 */

public class MybagBean implements Serializable {

    private static final long serialVersionUID = 2493557756084427294L;
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

        public List<BookBean> bookList;
        public List<CurriculumBean> curriculumList;
        public List<QualityEducationBean> qualityEducationList;

        public List<QualityEducationBean> getQualityEducationList() {
            return qualityEducationList;
        }

        public void setQualityEducationList(List<QualityEducationBean> qualityEducationList) {
            this.qualityEducationList = qualityEducationList;
        }

        public List<BookBean> getBookList() {
            return bookList;
        }

        public void setBookList(List<BookBean> bookList) {
            this.bookList = bookList;
        }

        public List<CurriculumBean> getCurriculumList() {
            return curriculumList;
        }

        public void setCurriculumList(List<CurriculumBean> curriculumList) {
            this.curriculumList = curriculumList;
        }
    }


}