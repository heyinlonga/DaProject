package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;


/**
 * Created by Mike on 2018/4/25.
 *   是否更新
 */

public class VersionBean implements Serializable  {

    private static final long serialVersionUID = -8035051123181398678L;
    private int status;
    private String message;
    private Data data;

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

    public class Data implements Serializable{
        private String version;
        private String url;
        private boolean isCanClose;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isCanClose() {
            return isCanClose;
        }

        public void setCanClose(boolean canClose) {
            isCanClose = canClose;
        }
    }
}
