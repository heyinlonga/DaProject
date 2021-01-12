package software.ecenter.study.bean.QuestionBean;

import java.util.List;

/**
 * Created by zyt on 2018/6/25.
 */

public class ListCurriculumBean {

    /**
     * status : 1
     * message :
     * data : {"curriculumList":[{"curriculumId":"1","curriculumName":"精品课程1"},{"curriculumId":"2","curriculumName":"精品课程2"},{"curriculumId":"3","curriculumName":"精品课程3"},{"curriculumId":"4","curriculumName":"精品课程4"}]}
     */

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

    public static class DataBean {
        private List<CurriculumListBean> curriculumList;

        public List<CurriculumListBean> getCurriculumList() {
            return curriculumList;
        }

        public void setCurriculumList(List<CurriculumListBean> curriculumList) {
            this.curriculumList = curriculumList;
        }

        public static class CurriculumListBean {
            /**
             * curriculumId : 1
             * curriculumName : 精品课程1
             */

            private String curriculumId;
            private String curriculumName;

            public String getCurriculumId() {
                return curriculumId;
            }

            public void setCurriculumId(String curriculumId) {
                this.curriculumId = curriculumId;
            }

            public String getCurriculumName() {
                return curriculumName;
            }

            public void setCurriculumName(String curriculumName) {
                this.curriculumName = curriculumName;
            }
        }
    }
}
