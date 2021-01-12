package software.ecenter.study.bean.WxBean;

import com.google.gson.annotations.SerializedName;

/**
 * 创建时间: 2018/5/28.
 * 编写人: wxy
 * 功能描述:
 */

public class WxPayBean {

    /**
     * status : 1
     * message :
     * data : {"appid":"wxb29f269838646062","noncestr":"hfzjn43HlgG8ljJ3","package":"Sign=WXPay","partnerid":"1503090391","prepayid":"wx311754074208919a420691464167995117","result_code":"SUCCESS","return_code":"SUCCESS","return_msg":"OK","sign":"96F16F0A708DAA196E39F85AD3405E45","timestamp":1527760447,"trade_type":"APP"}
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
         * appid : wxb29f269838646062
         * noncestr : hfzjn43HlgG8ljJ3
         * package : Sign=WXPay
         * partnerid : 1503090391
         * prepayid : wx311754074208919a420691464167995117
         * result_code : SUCCESS
         * return_code : SUCCESS
         * return_msg : OK
         * sign : 96F16F0A708DAA196E39F85AD3405E45
         * timestamp : 1527760447
         * trade_type : APP
         */

        private String appid;
        private String noncestr;
        @SerializedName("package")
        private String packageX;
        private String partnerid;
        private String prepayid;
        private String result_code;
        private String return_code;
        private String return_msg;
        private String sign;
        private int timestamp;
        private String trade_type;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getResult_code() {
            return result_code;
        }

        public void setResult_code(String result_code) {
            this.result_code = result_code;
        }

        public String getReturn_code() {
            return return_code;
        }

        public void setReturn_code(String return_code) {
            this.return_code = return_code;
        }

        public String getReturn_msg() {
            return return_msg;
        }

        public void setReturn_msg(String return_msg) {
            this.return_msg = return_msg;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public String getTrade_type() {
            return trade_type;
        }

        public void setTrade_type(String trade_type) {
            this.trade_type = trade_type;
        }
    }
}
