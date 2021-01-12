package software.ecenter.study.bean.QuestionBean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/5/14.
 */

public class chapterBean implements Serializable {
    private static final long serialVersionUID = 3063574140587795660L;

    private String chapterId;
    private String chapterName;
    private int hasSection;

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

    public int getHasSection() {
        return hasSection;
    }

    public void setHasSection(int hasSection) {
        this.hasSection = hasSection;
    }
}
