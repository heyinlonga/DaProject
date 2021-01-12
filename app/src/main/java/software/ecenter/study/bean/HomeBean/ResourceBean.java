package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/5/10.
 */

public class ResourceBean implements Serializable {
    private static final long serialVersionUID = 5452341758125646495L;
    private String userId;
    private String resourceId;
    private String resourceTitle;
    private String resourceName;
    private String resourceSize;
    private String resourceTime;
    private String resourceType;
    private String resourceUrl;
    private String LocalPathDir;//本地路径
    private String resourceDownloadSize; //當前下載進度
    private boolean isHaveResourceFile;
    private boolean isTeacherResource;
    private String TeacherResourceType;
    private String id;
    private boolean isAnwer;//服务器是否有记录
    private boolean isCheck;//选中
    private boolean isDownload;//下载中
    private boolean isDownloadOk;//下载完成
    private boolean isWillDelete;//准备删除
    private boolean isCheckMode;//先择模式
    private boolean isCanDownload;//是否可以下载
    private String type; //文档类型
    private boolean isSpecial; //是否特殊资源
    private boolean isBuy;

    private String questionId;
    private String questionName;
    private boolean newBatch;

    public boolean isNewBatch() {
        return newBatch;
    }

    public void setNewBatch(boolean newBatch) {
        this.newBatch = newBatch;
    }

    private boolean isNeedUpdate;//是否更新

    public boolean isTeacherResource() {
        return isTeacherResource;
    }

    public void setTeacherResource(boolean teacherResource) {
        isTeacherResource = teacherResource;
    }

    public String getTeacherResourceType() {
        return TeacherResourceType == null ? "" : TeacherResourceType;
    }

    public void setTeacherResourceType(String teacherResourceType) {
        TeacherResourceType = teacherResourceType;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isNeedUpdate() {
        return isNeedUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        isNeedUpdate = needUpdate;
    }

    public boolean isAnwer() {
        return isAnwer;
    }

    public void setAnwer(boolean anwer) {
        isAnwer = anwer;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }

    public boolean isHaveResourceFile() {
        return isHaveResourceFile;
    }

    public void setHaveResourceFile(boolean haveResourceFile) {
        isHaveResourceFile = haveResourceFile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isCanDownload() {
        return isCanDownload;
    }

    public void setCanDownload(boolean canDownload) {
        isCanDownload = canDownload;
    }

    public void setResourceDownloadSize(String resourceDownloadSize) {
        this.resourceDownloadSize = resourceDownloadSize;
    }

    public String getResourceDownloadSize() {
        return resourceDownloadSize;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceTitle() {
        return resourceTitle;
    }

    public void setResourceTitle(String resourceTitle) {
        this.resourceTitle = resourceTitle;
    }

    public String getResourceSize() {
        return resourceSize;
    }

    public void setResourceSize(String resourceSize) {
        this.resourceSize = resourceSize;
    }

    public String getResourceTime() {
        return resourceTime;
    }

    public void setResourceTime(String resourceTime) {
        this.resourceTime = resourceTime;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public boolean isCheckMode() {
        return isCheckMode;
    }

    public void setCheckMode(boolean checkMode) {
        isCheckMode = checkMode;
    }

    public String getLocalPathDir() {
        return LocalPathDir;
    }

    public void setLocalPathDir(String localPathDir) {
        LocalPathDir = localPathDir;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public boolean isDownloadOk() {
        return isDownloadOk;
    }

    public void setDownloadOk(boolean downloadOk) {
        isDownloadOk = downloadOk;
    }

    public boolean isWillDelete() {
        return isWillDelete;
    }

    public void setWillDelete(boolean willDelete) {
        isWillDelete = willDelete;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}
