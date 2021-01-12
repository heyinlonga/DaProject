package software.ecenter.study.bean;

/**
 * Created by zyt on 2018/6/7.
 */

public class NetBaseBean {


    /**
     * status : 1
     * message :
     * data : {"access_token":"07e059d8-f8c8-449d-9caa-f609272f3f17","token_type":"bearer","refresh_token":"42ead591-351c-4127-9adf-7138e2f61960","expires_in":2483344,"scope":"ui","role":1}
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
         * access_token : 07e059d8-f8c8-449d-9caa-f609272f3f17
         * token_type : bearer
         * refresh_token : 42ead591-351c-4127-9adf-7138e2f61960
         * expires_in : 2483344
         * scope : ui
         * role : 1
         */

        private String access_token;
        private String token_type;
        private String refresh_token;
        private int expires_in;
        private String scope;
        private int role;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }
    }
}
