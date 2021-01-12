package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mike on 2018/5/11.
 */

public class ExerciseDetailBean implements Serializable {


    private static final long serialVersionUID = -1308031470814759855L;
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

    public class Data implements Serializable {
        private static final long serialVersionUID = 4858925667095281537L;
        private String resourceName;
        private List<ExerciseBean> exerciseList;
        private long beginTime;

        public long getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(long beginTime) {
            this.beginTime = beginTime;
        }

        public String getResourceName() {
            return resourceName;
        }

        public void setResourceName(String resourceName) {
            this.resourceName = resourceName;
        }

        public List<ExerciseBean> getExerciseList() {
            return exerciseList;
        }

        public void setExerciseList(List<ExerciseBean> exerciseList) {
            this.exerciseList = exerciseList;
        }
    }


}
