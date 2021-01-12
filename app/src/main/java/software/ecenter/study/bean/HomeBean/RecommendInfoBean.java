package software.ecenter.study.bean.HomeBean;

/**
 * Created by zyt on 2018/6/23.
 */

public class RecommendInfoBean {

    /**
     * status : 1
     * message :
     * data : {"sectionName":"第二节","charterName":"第一章","subject":"数学","grade":"一年级下","sectionId":11,"bookName":"测试图书1","charterId":8,"isCurriculum":false,"bookId":2}
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
        /**
         * sectionName : 第二节
         * charterName : 第一章
         * subject : 数学
         * grade : 一年级下
         * sectionId : 11
         * bookName : 测试图书1
         * charterId : 8
         * isCurriculum : false
         * bookId : 2
         */


        private String subject;
        private String grade;
        private int bookId;
        private int charterId;
        private int sectionId;
        private int topicId;
        private boolean isCurriculum;
        private int curriculumId;
        private String bookName;
        private String sectionName;
        private String charterName;
        private String topicName;
        private String curriculumName;

        public int getTopicId() {
            return topicId;
        }

        public void setTopicId(int topicId) {
            this.topicId = topicId;
        }

        public boolean isCurriculum() {
            return isCurriculum;
        }

        public void setCurriculum(boolean curriculum) {
            isCurriculum = curriculum;
        }

        public int getCurriculumId() {
            return curriculumId;
        }

        public void setCurriculumId(int curriculumId) {
            this.curriculumId = curriculumId;
        }

        public String getTopicName() {
            return topicName;
        }

        public void setTopicName(String topicName) {
            this.topicName = topicName;
        }

        public String getCurriculumName() {
            return curriculumName;
        }

        public void setCurriculumName(String curriculumName) {
            this.curriculumName = curriculumName;
        }

        public String getSectionName() {
            return sectionName;
        }

        public void setSectionName(String sectionName) {
            this.sectionName = sectionName;
        }

        public String getCharterName() {
            return charterName;
        }

        public void setCharterName(String charterName) {
            this.charterName = charterName;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public int getSectionId() {
            return sectionId;
        }

        public void setSectionId(int sectionId) {
            this.sectionId = sectionId;
        }

        public String getBookName() {
            return bookName;
        }

        public void setBookName(String bookName) {
            this.bookName = bookName;
        }

        public int getCharterId() {
            return charterId;
        }

        public void setCharterId(int charterId) {
            this.charterId = charterId;
        }

        public boolean isIsCurriculum() {
            return isCurriculum;
        }

        public void setIsCurriculum(boolean isCurriculum) {
            this.isCurriculum = isCurriculum;
        }

        public int getBookId() {
            return bookId;
        }

        public void setBookId(int bookId) {
            this.bookId = bookId;
        }
    }
}
