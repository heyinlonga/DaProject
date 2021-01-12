package software.ecenter.study.bean;

import java.util.List;

/**
 * Message
 * Create by Administrator
 * Create by 2019/8/15
 */
public class CateBean {
    /**
     * children : [{"children":[],"id":10,"name":"《孤独的小螃蟹》第一站","parentId":8},{"children":[],"id":9,"name":"《孤独的小螃蟹》第二站(《恐龙鲁鲁》)","parentId":8}]
     * id : 8
     * name : 二年级
     */
    private long parentId;
    private long id;
    private String name;
    private boolean isHide;
    private boolean isNoFile;
    private List<CateBean> children;

    public boolean isNoFile() {
        return isNoFile;
    }

    public void setNoFile(boolean noFile) {
        isNoFile = noFile;
    }

    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean hide) {
        isHide = hide;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CateBean> getChildren() {
        return children;
    }

    public void setChildren(List<CateBean> children) {
        this.children = children;
    }
}
