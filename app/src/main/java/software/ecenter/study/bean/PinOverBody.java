package software.ecenter.study.bean;

import java.util.List;

/**
 * Message
 * Create by Administrator
 * Create by 2019/11/21
 */
public class PinOverBody {
    private String resourceId;
    private List<PinTiBean> exerciseResults;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public List<PinTiBean> getExerciseResults() {
        return exerciseResults;
    }

    public void setExerciseResults(List<PinTiBean> exerciseResults) {
        this.exerciseResults = exerciseResults;
    }
}
