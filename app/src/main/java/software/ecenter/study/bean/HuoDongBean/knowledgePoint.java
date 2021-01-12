package software.ecenter.study.bean.HuoDongBean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/5/14.
 */

public class knowledgePoint implements Serializable{

    private static final long serialVersionUID = 7258525194359504357L;
    private String knowledgePointId;
    private String knowledgePointName;

    public String getKnowledgePointId() {
        return knowledgePointId;
    }

    public void setKnowledgePointId(String knowledgePointId) {
        this.knowledgePointId = knowledgePointId;
    }

    public String getKnowledgePointName() {
        return knowledgePointName;
    }

    public void setKnowledgePointName(String knowledgePointName) {
        this.knowledgePointName = knowledgePointName;
    }
}
