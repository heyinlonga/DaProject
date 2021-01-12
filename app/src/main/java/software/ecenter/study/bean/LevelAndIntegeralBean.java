package software.ecenter.study.bean;

import java.util.List;

/**
 * Created by zyt on 2018/6/6.
 */

public class LevelAndIntegeralBean {

    /**
     * status : 1
     * message :
     * data : {"data":[{"title":"每日签到送积分","value":"1分"},{"title":"学生上传资源送积分","value":"5分"},{"title":"教师优质资源送积分","value":"10分"},{"title":"教师一般资源送积分","value":"5分"},{"title":"教师低级资源送积分","value":"1分"}]}
     */

    private int status;
    private String message;
    private DataBeanX data;

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

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * title : 每日签到送积分
             * value : 1分
             */

            private String title;
            private String value;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
