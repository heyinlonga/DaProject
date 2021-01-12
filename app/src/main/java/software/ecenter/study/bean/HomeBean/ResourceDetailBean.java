package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mike on 2018/5/10.
 */

public class ResourceDetailBean implements Serializable {

    private static final long serialVersionUID = -7142751940667837681L;
    private int status;
    private String message;
    private Data data;

    public void init() {
        data = new Data();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        private boolean isNeedBuy;
        private int isSingleBuy; //否可单独购买0：否，1：是，为0时提示，需该资源不可单独购买，请打包购买
        private boolean hasSecurityCode;
        private String resourceId;
        private String resourceUrl;
        private String resourceName;
        public String bookName;
        private String authorId;
        private String authorName;
        private String authorImg;
        private String resourceType; //资源类型:jpg、png、gif、MP3、MP4、doc、pdf、ppt、txt
        private int thumbUpNum;
        private int commentNum;
        private String bookId;
        private String headImage;
        private int role;//身份( 1、学生  2、老师)
        private List<ResourceBean> resourceList;
        private String LocalPathDir;//本地路径
        private String type;
        private String content;
        private String resourceSize;
        private String resourceTime;
        private boolean isThumbUp;
        private boolean isCollection;
        private boolean isCheck;
        private int sourceType; //资源所属分类 1：讲座资源，2：素质教育资源，0：其他
        private int coinPrice;  //学习币价格
        private boolean isHaveExercise; //是否有习题 ，true是 ，false 否
        private boolean haveEasy ; //是否有简单题
        private boolean haveNormal; //是否有适中题
        private boolean haveDifficulty; //是否有困难题
        private boolean isHaveAfterClassExercise; //是否有课后突破题
        private boolean isHaveKSExercises; //是否有口算练习
        private boolean havePinduExercise; //是否有拼音拼读

        public boolean isHaveKSExercises() {
            return isHaveKSExercises;
        }

        public void setHaveKSExercises(boolean haveKSExercises) {
            isHaveKSExercises = haveKSExercises;
        }

        public boolean isHavePinduExercise() {
            return havePinduExercise;
        }

        public void setHavePinduExercise(boolean havePinduExercise) {
            this.havePinduExercise = havePinduExercise;
        }

        public boolean isHaveAfterClassExercise() {
            return isHaveAfterClassExercise;
        }

        public void setHaveAfterClassExercise(boolean haveAfterClassExercise) {
            isHaveAfterClassExercise = haveAfterClassExercise;
        }

        public int getSourceType() {
            return sourceType;
        }

        public void setSourceType(int sourceType) {
            this.sourceType = sourceType;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getAuthorImg() {
            return authorImg;
        }

        public void setAuthorImg(String authorImg) {
            this.authorImg = authorImg;
        }

        public String getAuthorId() {
            return authorId;
        }

        public void setAuthorId(String authorId) {
            this.authorId = authorId;
        }

        public boolean isHaveExercise() {
            return isHaveExercise;
        }

        public void setHaveExercise(boolean haveExercise) {
            isHaveExercise = haveExercise;
        }

        public void setIsSingleBuy(int isSingleBuy) {
            this.isSingleBuy = isSingleBuy;
        }

        public int getCoinPrice() {
            return coinPrice;
        }

        public void setCoinPrice(int coinPrice) {
            this.coinPrice = coinPrice;
        }

        public boolean isHaveEasy() {
            return haveEasy;
        }

        public void setHaveEasy(boolean haveEasy) {
            this.haveEasy = haveEasy;
        }

        public boolean isHaveNormal() {
            return haveNormal;
        }

        public void setHaveNormal(boolean haveNormal) {
            this.haveNormal = haveNormal;
        }

        public boolean isHaveDifficulty() {
            return haveDifficulty;
        }

        public void setHaveDifficulty(boolean haveDifficulty) {
            this.haveDifficulty = haveDifficulty;
        }

        public int getIsSingleBuy() {
            return isSingleBuy;
        }

        public void setSingleBuy(int singleBuy) {
            isSingleBuy = singleBuy;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public boolean getThumbUp() {
            return isThumbUp;
        }

        public void setThumbUp(boolean thumbUp) {
            isThumbUp = thumbUp;
        }

        public boolean getCollection() {
            return isCollection;
        }

        public void setCollection(boolean collection) {
            isCollection = collection;
        }

        public void setResourceSize(String resourceSize) {
            this.resourceId = resourceId;
        }

        public String getResourceSize() {
            return resourceSize;
        }

        public void setResourceTime(String resourceTime) {
            this.resourceTime = resourceTime;
        }

        public String getResourceTime() {
            return resourceTime;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public boolean isNeedBuy() {
            return isNeedBuy;
        }

        public void setNeedBuy(boolean needBuy) {
            isNeedBuy = needBuy;
        }

        public boolean isHasSecurityCode() {
            return hasSecurityCode;
        }

        public void setHasSecurityCode(boolean hasSecurityCode) {
            this.hasSecurityCode = hasSecurityCode;
        }

        public String getResourceId() {
            return resourceId;
        }

        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }

        public String getResourceUrl() {
            return resourceUrl;
        }

        public void setResourceUrl(String resourceUrl) {
            this.resourceUrl = resourceUrl;
        }

        public String getResourceName() {
            return resourceName;
        }

        public void setResourceName(String resourceName) {
            this.resourceName = resourceName;
        }

        public String getResourceType() {
            return resourceType;
        }

        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }

        public int getThumbUpNum() {
            return thumbUpNum;
        }

        public void setThumbUpNum(int thumbUpNum) {
            this.thumbUpNum = thumbUpNum;
        }

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public String getBookId() {
            return bookId;
        }

        public void setBookId(String bookId) {
            this.bookId = bookId;
        }

        public List<ResourceBean> getResourceList() {
            return resourceList;
        }

        public void setResourceList(List<ResourceBean> resourceList) {
            this.resourceList = resourceList;
        }

        public String getHeadImage() {
            return headImage;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public String getLocalPathDir() {
            return LocalPathDir;
        }

        public void setLocalPathDir(String localPathDir) {
            LocalPathDir = localPathDir;
        }
    }


}
