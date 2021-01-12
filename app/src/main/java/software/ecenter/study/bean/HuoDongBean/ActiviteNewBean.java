package software.ecenter.study.bean.HuoDongBean;

import java.util.List;

/**
 * auther: zzm
 * Date: 2019/8/13
 * Description:
 */
public class ActiviteNewBean {

    /**
     * status : 1
     * message :
     * data : {"compositionId":47,"matchList":[{"name":"阅读大赛","imgUrl":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/ActivityImg/81fdba6ed5af4e40895d4e6b4f2d202d"}],"oldMatchList":[],"activityList":[{"id":27,"activityName":"测试第三方","imgUrl":null},{"id":30,"activityName":"测试20190104","imgUrl":null},{"id":31,"activityName":"腾讯111","imgUrl":null},{"id":32,"activityName":"优酷222","imgUrl":null},{"id":35,"activityName":"测试爱奇艺","imgUrl":null},{"id":26,"activityName":"作文提升课","imgUrl":null},{"id":37,"activityName":"测试wuxin","imgUrl":null}],"compositionImgUrl":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/ActivityImg/53ec306af81245b1a7bd11e1d4330b1b"}
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
         * compositionId : 47
         * matchList : [{"name":"阅读大赛","imgUrl":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/ActivityImg/81fdba6ed5af4e40895d4e6b4f2d202d"}]
         * oldMatchList : []
         * activityList : [{"id":27,"activityName":"测试第三方","imgUrl":null},{"id":30,"activityName":"测试20190104","imgUrl":null},{"id":31,"activityName":"腾讯111","imgUrl":null},{"id":32,"activityName":"优酷222","imgUrl":null},{"id":35,"activityName":"测试爱奇艺","imgUrl":null},{"id":26,"activityName":"作文提升课","imgUrl":null},{"id":37,"activityName":"测试wuxin","imgUrl":null}]
         * compositionImgUrl : http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/ActivityImg/53ec306af81245b1a7bd11e1d4330b1b
         */

        private int compositionId;
        private String compositionImgUrl;
        private List<MatchListBean> matchList;
        private List<ActivityListBean> oldMatchList;
        private List<ActivityListBean> activityList;

        public int getCompositionId() {
            return compositionId;
        }

        public void setCompositionId(int compositionId) {
            this.compositionId = compositionId;
        }

        public String getCompositionImgUrl() {
            return compositionImgUrl;
        }

        public void setCompositionImgUrl(String compositionImgUrl) {
            this.compositionImgUrl = compositionImgUrl;
        }

        public List<MatchListBean> getMatchList() {
            return matchList;
        }

        public void setMatchList(List<MatchListBean> matchList) {
            this.matchList = matchList;
        }

        public List<ActivityListBean> getOldMatchList() {
            return oldMatchList;
        }

        public void setOldMatchList(List<ActivityListBean> oldMatchList) {
            this.oldMatchList = oldMatchList;
        }

        public List<ActivityListBean> getActivityList() {
            return activityList;
        }

        public void setActivityList(List<ActivityListBean> activityList) {
            this.activityList = activityList;
        }

        public static class MatchListBean {
            /**
             * name : 阅读大赛
             * imgUrl : http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/ActivityImg/81fdba6ed5af4e40895d4e6b4f2d202d
             */
            private boolean isCanEnter;
            private String name;
            private String imgUrl;

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
        }

        public static class ActivityListBean {
            /**
             * id : 27
             * activityName : 测试第三方
             * imgUrl : null
             */

            private String id;
            private String name;
            private String activityName;
            private String imgUrl;
            private String activityType;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getActivityType() {
                return activityType == null ? "" : activityType;
            }

            public void setActivityType(String activityType) {
                this.activityType = activityType;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getActivityName() {
                return activityName;
            }

            public void setActivityName(String activityName) {
                this.activityName = activityName;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }
        }
    }
}
