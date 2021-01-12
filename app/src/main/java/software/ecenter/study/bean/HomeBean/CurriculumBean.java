package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/4/25.
 */

public class CurriculumBean implements Serializable  {

    private static final long serialVersionUID = -9205812963650755281L;
    private String curriculumId;
    private String curriculumImage;
    private String curriculumPrice;
    private String curriculumName;
    private boolean isHaveFile;
    private String type;
    private int categoryShowType;

    public int getCategoryShowType() {
        return categoryShowType;
    }

    public void setCategoryShowType(int categoryShowType) {
        this.categoryShowType = categoryShowType;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isHaveFile() {
        return isHaveFile;
    }

    public void setHaveFile(boolean haveFile) {
        isHaveFile = haveFile;
    }

    public String getCurriculumId() {
        return curriculumId;
    }

    public void setCurriculumId(String curriculumId) {
        this.curriculumId = curriculumId;
    }

    public String getCurriculumImage() {
        return curriculumImage;
    }

    public void setCurriculumImage(String curriculumImage) {
        this.curriculumImage = curriculumImage;
    }

    public String getCurriculumPrice() {
        return curriculumPrice;
    }

    public void setCurriculumPrice(String curriculumPrice) {
        this.curriculumPrice = curriculumPrice;
    }

    public String getCurriculumName() {
        return curriculumName;
    }

    public void setCurriculumName(String curriculumName) {
        this.curriculumName = curriculumName;
    }

}
