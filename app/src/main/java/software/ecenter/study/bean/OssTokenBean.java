package software.ecenter.study.bean;

/**
 * Created by zyt on 2018/5/23.
 */

public class OssTokenBean {

    private static  OssTokenBean ossTokenBean;
    /**
     * status : 1
     * message :
     * data : {"StatusCode":"200","AccessKeyId":"STS.NKFX5kEwRT9PBN3UyKKGi6hko","AccessKeySecret":"BuaGmgtwJSKPF9s6sMNjpXh2kpWnvX3DZKj2kmPgRgrc","SecurityToken":"CAISnAN1q6Ft5B2yfSjIr4jzE4/fqKhz4/u7QGiCsXkeR8hF2a3AjTz2IH1KenFsBO0WsfU/mW1Q7vgclqB6T55OSAmcNZIoZ0SofdPlMeT7oMWQweEuAvTHcDHhv3eZsebWZ+LmNjm0GJOEYEzFkSle2KbzcS7YMXWuLZyOj+xlDLEQRRLqQjdaI91UKwB+yqodLmCDDeuxFRToj2HMbjJvoREupW575Liy5cee5xHC7jj90fRHg4XqPqCtddV3Xud4SMzn9eZxbLbkzSpM6gBD7rtLlKhD8Dul2daGGAt14g6aFODW/9ZzN3VfBM4AFrVDseL3mNBhp+XXjP6X8RtWOvxPWCmtHuLQycDfSuSyLYR7J/SpO3vdwMHKKpTur0RmAwwSPxgYfME6eD0iS04sRSHIO+q79UvWQH/6E/XegftpjsQqlgSxpYbTHTXVHeXFixR/E4QnckYlOyQR2WHcaaIce2ROCQg6WejNEdguPE8P+f615VWMCDcTx3VWruD4YOjNpqccZIPwU5RLy4MBY45ctG8nX71DmStsM8B9GoABMScojeIyZkYrhMTjAg0lUrR0Q3nkGcE6551ZVICeXPTL5KyB2Tiaj5+Y+Lpe3I2CyMRq8l1DPFtMLldvX99vO7iqBxv0gT1v+bQlCvACwvmPGSq2yijojyL9RUlMqOXKyAIBTSMI2YW9BRPeM2gj5GTKceEw66mM8ewBjlA2tCg=","Expiration":"2018-05-23T10:22:53Z","Bucket":"hgxzy-test","EndPoint":"https://oss-cn-hangzhou.aliyuncs.com","UploadUrl":"https://hgxzy-test.oss-cn-hangzhou.aliyuncs.com","UploadFilePath":"13520125933/photo/"}
     */

    private int status;
    private String message;
    private DataBean data;

    public static OssTokenBean getInstance (){
        if (ossTokenBean == null)
            ossTokenBean = new OssTokenBean();
        if (ossTokenBean.getData() == null)
            ossTokenBean.setData(new DataBean());
        return ossTokenBean;
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * StatusCode : 200
         * AccessKeyId : STS.NKFX5kEwRT9PBN3UyKKGi6hko
         * AccessKeySecret : BuaGmgtwJSKPF9s6sMNjpXh2kpWnvX3DZKj2kmPgRgrc
         * SecurityToken : CAISnAN1q6Ft5B2yfSjIr4jzE4/fqKhz4/u7QGiCsXkeR8hF2a3AjTz2IH1KenFsBO0WsfU/mW1Q7vgclqB6T55OSAmcNZIoZ0SofdPlMeT7oMWQweEuAvTHcDHhv3eZsebWZ+LmNjm0GJOEYEzFkSle2KbzcS7YMXWuLZyOj+xlDLEQRRLqQjdaI91UKwB+yqodLmCDDeuxFRToj2HMbjJvoREupW575Liy5cee5xHC7jj90fRHg4XqPqCtddV3Xud4SMzn9eZxbLbkzSpM6gBD7rtLlKhD8Dul2daGGAt14g6aFODW/9ZzN3VfBM4AFrVDseL3mNBhp+XXjP6X8RtWOvxPWCmtHuLQycDfSuSyLYR7J/SpO3vdwMHKKpTur0RmAwwSPxgYfME6eD0iS04sRSHIO+q79UvWQH/6E/XegftpjsQqlgSxpYbTHTXVHeXFixR/E4QnckYlOyQR2WHcaaIce2ROCQg6WejNEdguPE8P+f615VWMCDcTx3VWruD4YOjNpqccZIPwU5RLy4MBY45ctG8nX71DmStsM8B9GoABMScojeIyZkYrhMTjAg0lUrR0Q3nkGcE6551ZVICeXPTL5KyB2Tiaj5+Y+Lpe3I2CyMRq8l1DPFtMLldvX99vO7iqBxv0gT1v+bQlCvACwvmPGSq2yijojyL9RUlMqOXKyAIBTSMI2YW9BRPeM2gj5GTKceEw66mM8ewBjlA2tCg=
         * Expiration : 2018-05-23T10:22:53Z
         * Bucket : hgxzy-test
         * EndPoint : https://oss-cn-hangzhou.aliyuncs.com
         * UploadUrl : https://hgxzy-test.oss-cn-hangzhou.aliyuncs.com
         * UploadFilePath : 13520125933/photo/
         */

        private String StatusCode;
        private String AccessKeyId;
        private String AccessKeySecret;
        private String SecurityToken;
        private String Expiration;
        private String Bucket;
        private String EndPoint;
        private String UploadUrl;
        private String UploadFilePath;

        public String getStatusCode() {
            return StatusCode;
        }

        public void setStatusCode(String StatusCode) {
            this.StatusCode = StatusCode;
        }

        public String getAccessKeyId() {
            return AccessKeyId;
        }

        public void setAccessKeyId(String AccessKeyId) {
            this.AccessKeyId = AccessKeyId;
        }

        public String getAccessKeySecret() {
            return AccessKeySecret;
        }

        public void setAccessKeySecret(String AccessKeySecret) {
            this.AccessKeySecret = AccessKeySecret;
        }

        public String getSecurityToken() {
            return SecurityToken;
        }

        public void setSecurityToken(String SecurityToken) {
            this.SecurityToken = SecurityToken;
        }

        public String getExpiration() {
            return Expiration;
        }

        public void setExpiration(String Expiration) {
            this.Expiration = Expiration;
        }

        public String getBucket() {
            return Bucket;
        }

        public void setBucket(String Bucket) {
            this.Bucket = Bucket;
        }

        public String getEndPoint() {
            return EndPoint;
        }

        public void setEndPoint(String EndPoint) {
            this.EndPoint = EndPoint;
        }

        public String getUploadUrl() {
            return UploadUrl;
        }

        public void setUploadUrl(String UploadUrl) {
            this.UploadUrl = UploadUrl;
        }

        public String getUploadFilePath() {
            return UploadFilePath;
        }

        public void setUploadFilePath(String UploadFilePath) {
            this.UploadFilePath = UploadFilePath;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "Bucket='" + Bucket + '\'' +
                    ", EndPoint='" + EndPoint + '\'' +
                    ", UploadUrl='" + UploadUrl + '\'' +
                    ", UploadFilePath='" + UploadFilePath + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "OssTokenBean{" +
                ", data=" + data +
                '}';
    }
}
