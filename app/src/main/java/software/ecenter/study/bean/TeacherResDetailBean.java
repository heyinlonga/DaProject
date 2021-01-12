package software.ecenter.study.bean;

import java.util.List;

import software.ecenter.study.bean.HomeBean.ChapterItemBean;

/**
 * auther: zzm
 * Date: 2019/8/15
 * Description:
 */
public class TeacherResDetailBean {

    /**
     * status : 1
     * message :
     * data : {"id":2,"name":"导入06201","imgUrl":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/TeacherPackageImg/bff785e84ae14c559c08bf134c0241c1","description":"description2","isCollect":false,"categoryList":[{"chapterId":"1","chapterName":"name1","parentId":"0","isExpand":1,"resourceCount":0,"path":"","isHaveFile":2},{"chapterId":"5","chapterName":"name12","parentId":"1","isExpand":0,"resourceCount":4,"path":",1","isHaveFile":2}]}
     */

    private int status;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 2
         * name : 导入06201
         * imgUrl : http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/TeacherPackageImg/bff785e84ae14c559c08bf134c0241c1
         * description : description2
         * isCollect : false
         * categoryList : [{"chapterId":"1","chapterName":"name1","parentId":"0","isExpand":1,"resourceCount":0,"path":"","isHaveFile":2},{"chapterId":"5","chapterName":"name12","parentId":"1","isExpand":0,"resourceCount":4,"path":",1","isHaveFile":2}]
         */

        private int id;
        private String name;
        private String imgUrl;
        private String description;
        private boolean isCollect;
        private List<ChapterItemBean> categoryList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isIsCollect() {
            return isCollect;
        }

        public void setIsCollect(boolean isCollect) {
            this.isCollect = isCollect;
        }

        public List<ChapterItemBean> getCategoryList() {
            return categoryList;
        }

        public void setCategoryList(List<ChapterItemBean> categoryList) {
            this.categoryList = categoryList;
        }

    }
}
