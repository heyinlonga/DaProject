package software.ecenter.study.bean;

/**
 * Message
 * Create by Administrator
 * Create by 2019/8/15
 */
public class CourseResBean {
    /**
     * data : {"canBuyGroup":false,"categoryShowType":0,"coinPrice":10,"description":"<p>1<\/p>","detail":"<p>1888<\/p>","id":5,"imgUrl":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/CurriculumImg/b19c213e414b4645a42d86ea90123b04","isBuy":false,"name":"1","resourceList":[]}
     * message :
     * status : 1
     */

    private DataBean data;
    private String message;
    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class DataBean {


        private long id;
        private String name;
        private String imgUrl;
        private boolean isBuy;
        private int coinPrice;
        private String description;
        private String teacherPhoto;
        private String teacherDescription;
        private boolean isHaveFile;
        private long resourceId;

        public boolean isHaveFile() {
            return isHaveFile;
        }

        public void setHaveFile(boolean haveFile) {
            isHaveFile = haveFile;
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

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public boolean isBuy() {
            return isBuy;
        }

        public void setBuy(boolean buy) {
            isBuy = buy;
        }

        public int getCoinPrice() {
            return coinPrice;
        }

        public void setCoinPrice(int coinPrice) {
            this.coinPrice = coinPrice;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTeacherPhoto() {
            return teacherPhoto;
        }

        public void setTeacherPhoto(String teacherPhoto) {
            this.teacherPhoto = teacherPhoto;
        }

        public String getTeacherDescription() {
            return teacherDescription;
        }

        public void setTeacherDescription(String teacherDescription) {
            this.teacherDescription = teacherDescription;
        }

        public long getResourceId() {
            return resourceId;
        }

        public void setResourceId(long resourceId) {
            this.resourceId = resourceId;
        }
    }
}
