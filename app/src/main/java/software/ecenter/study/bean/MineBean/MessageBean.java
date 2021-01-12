package software.ecenter.study.bean.MineBean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/4/25.
 */

public class MessageBean implements Serializable {
    private static final long serialVersionUID = -6953361436212641422L;
    private String messageId;
    private String messageType;
    private String messageTitle;
    private String messageDate;


    private boolean isCheck;//选中
    private int isReader;//已阅读
    private boolean isCheckMode;//先择模式
    private int type; //0:评论通过审核，1：反馈回复、其他推送，2：答疑回复，3: 上传资源过审
    private int relatedId;//type=0，对应资源id；type=1，空；type=2，提问id；type=3，空

    public int getIsReader() {
        return isReader;
    }

    public void setIsReader(int isReader) {
        this.isReader = isReader;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(int relatedId) {
        this.relatedId = relatedId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getIsRead() {
        return isReader;
    }

    public void setIsRead(int isReader) {
        this.isReader = isReader;
    }

    public boolean isCheckMode() {
        return isCheckMode;
    }

    public void setCheckMode(boolean checkMode) {
        isCheckMode = checkMode;
    }
}
