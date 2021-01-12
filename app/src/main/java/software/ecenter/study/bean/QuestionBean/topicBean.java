package software.ecenter.study.bean.QuestionBean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/5/14.
 */

public class topicBean implements Serializable{
    private static final long serialVersionUID = 1791068226348189531L;
    private String topicId;
    private String topicName;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
