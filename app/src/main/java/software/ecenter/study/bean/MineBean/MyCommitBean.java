package software.ecenter.study.bean.MineBean;

/**
 * Created by Mike on 2018/5/16.
 */

public class MyCommitBean {
    private String resourceId;
    private String resourceTitle;
    private String commentId;
    private String commentContent;
    private String commentDate;
    private String resourceType;
    private int commentState;

    private boolean isCheck;//选中
    private boolean isCheckMode;//先择模式

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

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public int getCommentState() {
        return commentState;
    }

    public void setCommentState(int commentState) {
        this.commentState = commentState;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isCheckMode() {
        return isCheckMode;
    }

    public void setCheckMode(boolean checkMode) {
        isCheckMode = checkMode;
    }
}
