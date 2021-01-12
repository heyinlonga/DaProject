package software.ecenter.study.bean.QuestionBean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/5/14.
 */

public class sectionBean implements Serializable{
    private static final long serialVersionUID = -5849059020285717051L;
    private String sectionId;
    private String sectionName;
    private int hasTopic;

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public int getHasTopic() {
        return hasTopic;
    }

    public void setHasTopic(int hasTopic) {
        this.hasTopic = hasTopic;
    }
}
