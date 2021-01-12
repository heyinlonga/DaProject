package software.ecenter.study.bean.HuoDongBean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/5/14.
 */

public class ActivityBean implements Serializable{
    private static final long serialVersionUID = -4440431265820494921L;

    private String activityId;
    private String activityImage;
    private String activityName;
    private String activityType; //1、普通 2、教师3、易错题 4、作文5、链接 （ 2、3、4 有投稿按钮 ； 1 2 3 4 都属于富文本 ）
    private String activityContent;
    private String activityTitle;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityContent() {
        return activityContent;
    }

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityImage() {
        return activityImage;
    }

    public void setActivityImage(String activityImage) {
        this.activityImage = activityImage;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
}
