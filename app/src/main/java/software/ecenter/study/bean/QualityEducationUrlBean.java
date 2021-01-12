package software.ecenter.study.bean;

import java.util.List;

/**
 * auther: zzm
 * Date: 2019/8/14
 * Description:
 */
public class QualityEducationUrlBean {

    /**
     * status : 1
     * message :
     * data : {"curpage":"0","hasNextPage":false,"qualityEducationList":[{"imgUrl":"imgUrl","coinPrice":"0","name":"test0719新建","id":"114","type":4},{"imgUrl":"","coinPrice":"13","name":"test0719导入","id":"113","type":4},{"imgUrl":"","coinPrice":"1","name":"素质教育h5测试","id":"116","type":4}]}
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
         * qualityEducationList : [{"imgUrl":"imgUrl","coinPrice":"0","name":"test0719新建","id":"114","type":4},{"imgUrl":"","coinPrice":"13","name":"test0719导入","id":"113","type":4},{"imgUrl":"","coinPrice":"1","name":"素质教育h5测试","id":"116","type":4}]
         */

        private String url;

        public String getUrl() {
            return url == null ? "" : url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
