package software.ecenter.study.bean.QuestionBean;

import java.io.Serializable;
import java.util.List;

import software.ecenter.study.bean.HomeBean.QuestionListBean;
import software.ecenter.study.bean.HomeBean.ResourceBean;

/**
 * Created by Mike on 2018/5/14.
 */

public class TopicDetailBean implements Serializable {
    private static final long serialVersionUID = -6588864499024775443L;
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
        private List<topicBean> topicList;
        private List<ResourceBean> resourceList;
        private List<QuestionListBean> questionList;

        public List<QuestionListBean> getQuestionList() {
            return questionList;
        }

        public void setQuestionList(List<QuestionListBean> questionList) {
            this.questionList = questionList;
        }

        public List<topicBean> getTopicList() {
            return topicList;
        }

        public void setTopicList(List<topicBean> topicList) {
            this.topicList = topicList;
        }

        public List<ResourceBean> getResourceList() {
            return resourceList;
        }

        public void setResourceList(List<ResourceBean> resourceList) {
            this.resourceList = resourceList;
        }
    }


}
