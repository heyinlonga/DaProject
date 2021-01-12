package software.ecenter.study.bean;

/**
 * Message
 * Create by Administrator
 * Create by 2019/10/28
 */
public class MatchList {

    /**
     * name : 口算
     * imgUrl : http://zygxkt.oss-cn-beijing.aliyuncs.com/ActivityImg/b7e3ce435ab64fdf8bcf60d1cf26691a
     * isCanEnter : false
     */

    private String id;
    private String name;
    private String matchBeginDate;
    private String matchEndDate;
    private String enrollEndDate;
    private boolean isEnroll;
    private boolean isEndEnroll;
    private boolean isMatch;
    private String enrollCount;
    private String imgUrl;
    private boolean isCanEnter;

    public boolean isEndEnroll() {
        return isEndEnroll;
    }

    public void setEndEnroll(boolean endEnroll) {
        isEndEnroll = endEnroll;
    }

    public boolean isMatch() {
        return isMatch;
    }

    public void setMatch(boolean match) {
        isMatch = match;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatchBeginDate() {
        return matchBeginDate;
    }

    public void setMatchBeginDate(String matchBeginDate) {
        this.matchBeginDate = matchBeginDate;
    }

    public String getMatchEndDate() {
        return matchEndDate;
    }

    public void setMatchEndDate(String matchEndDate) {
        this.matchEndDate = matchEndDate;
    }

    public String getEnrollEndDate() {
        return enrollEndDate;
    }

    public void setEnrollEndDate(String enrollEndDate) {
        this.enrollEndDate = enrollEndDate;
    }

    public boolean isEnroll() {
        return isEnroll;
    }

    public void setEnroll(boolean enroll) {
        isEnroll = enroll;
    }

    public String getEnrollCount() {
        return enrollCount;
    }

    public void setEnrollCount(String enrollCount) {
        this.enrollCount = enrollCount;
    }

    public boolean isCanEnter() {
        return isCanEnter;
    }

    public void setCanEnter(boolean canEnter) {
        isCanEnter = canEnter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isIsCanEnter() {
        return isCanEnter;
    }

    public void setIsCanEnter(boolean isCanEnter) {
        this.isCanEnter = isCanEnter;
    }
}
