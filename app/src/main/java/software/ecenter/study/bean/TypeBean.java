package software.ecenter.study.bean;

import java.util.List;

/**
 * Message
 * Create by Administrator
 * Create by 2019/6/22
 */
public class TypeBean {
    private boolean isSelect;
    private String name;

    public TypeBean( String name,boolean isSelect) {
        this.isSelect = isSelect;
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
