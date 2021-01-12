package software.ecenter.study.bean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/4/25.
 */

public class ZuheBookBean implements Serializable {
    private static final long serialVersionUID = -6953361436212641422L;
    private String id;
    private String Image;
    private String Name;
    private int type; //1、图书 2、课程
    private boolean isHaveFile;

    public boolean isHaveFile() {
        return isHaveFile;
    }

    public void setHaveFile(boolean haveFile) {
        isHaveFile = haveFile;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
