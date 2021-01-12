package software.ecenter.study.bean.HomeBean;

import java.util.List;

/**
 * auther: zzm
 * Date: 2019/8/15
 * Description:
 */
public class TeacherResBean {

    /**
     * status : 1
     * message : 
     * data : {"curpage":"0","hasNextPage":false,"packages":[{"id":"2","imgUrl":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/TeacherPackageImg/bff785e84ae14c559c08bf134c0241c1","name":"导入06201"},{"id":"3","imgUrl":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/TeacherPackageImg/b62dd71d536d425396f337ca2f92df21","name":"导入06202"}],"isActive":true}
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
         * curpage : 0
         * hasNextPage : false
         * packages : [{"id":"2","imgUrl":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/TeacherPackageImg/bff785e84ae14c559c08bf134c0241c1","name":"导入06201"},{"id":"3","imgUrl":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/TeacherPackageImg/b62dd71d536d425396f337ca2f92df21","name":"导入06202"}]
         * isActive : true
         */

        private String curpage;
        private boolean hasNextPage;
        private boolean isActive;
        private List<PackagesBean> packages;

        public String getCurpage() {
            return curpage;
        }

        public void setCurpage(String curpage) {
            this.curpage = curpage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public boolean isIsActive() {
            return isActive;
        }

        public void setIsActive(boolean isActive) {
            this.isActive = isActive;
        }

        public List<PackagesBean> getPackages() {
            return packages;
        }

        public void setPackages(List<PackagesBean> packages) {
            this.packages = packages;
        }

        public static class PackagesBean {
            /**
             * id : 2
             * imgUrl : http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/TeacherPackageImg/bff785e84ae14c559c08bf134c0241c1
             * name : 导入06201
             */

            private String id;
            private String imgUrl;
            private String name;
            private boolean isFree;

            public boolean isFree() {
                return isFree;
            }

            public void setFree(boolean free) {
                isFree = free;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
