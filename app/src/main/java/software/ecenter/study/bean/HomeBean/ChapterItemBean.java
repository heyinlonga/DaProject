package software.ecenter.study.bean.HomeBean;


import java.io.Serializable;

import software.ecenter.study.View.treelist.TreeNodeHasFile;
import software.ecenter.study.View.treelist.TreeNodeId;
import software.ecenter.study.View.treelist.TreeNodeIsExpand;
import software.ecenter.study.View.treelist.TreeNodeLabel;
import software.ecenter.study.View.treelist.TreeNodeNewBatch;
import software.ecenter.study.View.treelist.TreeNodePid;

/**
 * Created by Mike on 2018/4/28.
 */

public class ChapterItemBean implements Serializable {
    private static final long serialVersionUID = 5314877800861487481L;
    @TreeNodePid
    private String parentId;
    @TreeNodeId
    private String chapterId;
    @TreeNodeLabel
    private String chapterName;
    private boolean canBuyGroup;
    @TreeNodeIsExpand
    private int isExpand;
    private int resourceCount;
    @TreeNodeHasFile
    private int isHaveFile;
    @TreeNodeNewBatch
    private boolean newBatch;

    public ChapterItemBean(String parentId, String chapterId, String chapterName, int isHaveFile, boolean newBatch) {

        this.parentId = parentId;
        this.chapterId = chapterId;
        this.chapterName = chapterName;
        this.isHaveFile = isHaveFile;
        this.newBatch = newBatch;
    }

    public int getIsHaveFile() {
        return isHaveFile;
    }

    public void setIsHaveFile(int isHaveFile) {
        this.isHaveFile = isHaveFile;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public boolean getCanBuyGroup() {
        return canBuyGroup;
    }

    public void setCanBuyGroup(boolean canBuyGroup) {
        this.canBuyGroup = canBuyGroup;
    }

    public void setIsExpand(int isExpand) {

    }

    public int getIsExpand() {
        return isExpand;
    }

    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }

    public int getResourceCount() {
        return resourceCount;
    }
}
