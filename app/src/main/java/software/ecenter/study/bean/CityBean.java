package software.ecenter.study.bean;

import java.util.List;

/**
 * Created by Mike on 2018/5/7.
 */

public class CityBean {
    private String name;
    private List<CityBean> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityBean> getChildren() {
        return children;
    }

    public void setChildren(List<CityBean> children) {
        this.children = children;
    }
}
