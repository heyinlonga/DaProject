package software.ecenter.study.bean.MineBean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/4/25.
 */

public class UpdataBean implements Serializable  {
    private static final long serialVersionUID = -6953361436212641422L;
    private String uploadId;
    private String uploadTitle;
    private String mainTitle;
    private String subTitle;
    private String uploadStatus;
    private String uploadIntegral;
    private String uploadTime;

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUploadTime() {

        return uploadTime;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getUploadTitle() {
        return uploadTitle;
    }

    public void setUploadTitle(String uploadTitle) {
        this.uploadTitle = uploadTitle;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getUploadIntegral() {
        return uploadIntegral;
    }

    public void setUploadIntegral(String uploadIntegral) {
        this.uploadIntegral = uploadIntegral;
    }
}
