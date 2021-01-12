package software.ecenter.study.bean.HuoDongBean;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Mike on 2018/5/14.
 */

public class knowledgeDetailPoint implements Serializable {
    private static final long serialVersionUID = -8411233944906229191L;
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

    public class Data {
        private List<knowledgePoint> knowledgePointList;
        private String subject;
        private String grade;

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

        public List<knowledgePoint> getKnowledgePointList() {
            return knowledgePointList;
        }

        public void setKnowledgePointList(List<knowledgePoint> knowledgePointList) {
            this.knowledgePointList = knowledgePointList;
        }
    }

}
