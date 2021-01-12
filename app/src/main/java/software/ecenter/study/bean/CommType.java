package software.ecenter.study.bean;

/**
 * Message
 * Create by Administrator
 * Create by 2019/9/3
 */
public class CommType {
    private String name;
    private String id;
    private int resId;
    private boolean select;

    public CommType() {

    }

    public CommType(String name, boolean select) {
        this.name = name;
        this.select = select;
    }

    public CommType(String name, String id, int resId) {
        this.name = name;
        this.id = id;
        this.resId = resId;
    }
    public CommType(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
