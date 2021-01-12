package software.ecenter.study.bean.HomeBean;

import java.util.List;

/**
 * auther: zzm
 * Date: 2019/8/15
 * Description:
 */
public class SeeTeacherResBean {

    /**
     * status : 1
     * message :
     * data : {"id":13,"type":"PPT","url":"","resourceList":[{"resourceId":"16","resourceTitle":"导入06201, name1name12, 导入080902"}]}
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
         * id : 13
         * type : PPT
         * url :
         * resourceList : [{"resourceId":"16","resourceTitle":"导入06201, name1name12, 导入080902"}]
         */

        private int id;
        private String type;
        private String url;
        private boolean isUp;
        private boolean isCollect;
        private String ups;
        private String name;

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isUp() {
            return isUp;
        }

        public void setUp(boolean up) {
            isUp = up;
        }

        public boolean isCollect() {
            return isCollect;
        }

        public void setCollect(boolean collect) {
            isCollect = collect;
        }

        public String getUps() {
            return ups == null ? "" : ups;
        }

        public void setUps(String ups) {
            this.ups = ups;
        }

        private List<ResourceListBean> resourceList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<ResourceListBean> getResourceList() {
            return resourceList;
        }

        public void setResourceList(List<ResourceListBean> resourceList) {
            this.resourceList = resourceList;
        }

        public static class ResourceListBean {
            /**
             * resourceId : 16
             * resourceTitle : 导入06201, name1name12, 导入080902
             */

            private String resourceId;
            private String resourceTitle;

            public String getResourceId() {
                return resourceId;
            }

            public void setResourceId(String resourceId) {
                this.resourceId = resourceId;
            }

            public String getResourceTitle() {
                return resourceTitle;
            }

            public void setResourceTitle(String resourceTitle) {
                this.resourceTitle = resourceTitle;
            }
        }
    }
}
