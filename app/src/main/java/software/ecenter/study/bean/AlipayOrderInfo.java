package software.ecenter.study.bean;

/**
 * Created by zyt on 2018/6/5.
 */

public class AlipayOrderInfo {

    /**
     * status : 1
     * message :
     * data : {"orderString":"alipay_sdk=alipay-sdk-java-3.0.52.ALL&app_id=2018051460115498&biz_content=%7B%22body%22%3A%22%E9%BB%84%E5%86%88%E5%B0%8F%E7%8A%B6%E5%85%83-%22%2C%22out_trade_no%22%3A%22135201259331528177692891399895%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Flongmen.secdps.com%2Fpay%2FgetAlipayCallback&sign=ELsZGT7V%2B%2BEg6YZyAJOQa568EWmZBF8Dr7etiFXBdbdVg7F8LbIU2voHTFWpXTz6RfZMmEbe4MrH55wg8mwZcouxiwNSqMm12jreGtcT%2BLd1kovJOMZNbJ4%2FMIHr8n%2BtiD0uO1e7zdYUW9M7V9Ou%2FRWDRJAfax3AZVeC4cRK7Ixhvw6YGugXuYuYJYfols8HkIhVxsix%2FIhMrB46SuOEP0yZFBdN2P1YuQ4cecOIsgCQF%2FyMcKKPOe5yVH1VX1Uj8OK1AasVLykpFxhlPZM9Z0nxfWcDaw4Npv4VxIc445EQ8TQHhBY%2BXwdabC1UWU9YmvfSzIOIL9dHTRaVaR%2BhOw%3D%3D&sign_type=RSA2&timestamp=2018-06-05+13%3A48%3A12&version=1.0"}
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
         * orderString : alipay_sdk=alipay-sdk-java-3.0.52.ALL&app_id=2018051460115498&biz_content=%7B%22body%22%3A%22%E9%BB%84%E5%86%88%E5%B0%8F%E7%8A%B6%E5%85%83-%22%2C%22out_trade_no%22%3A%22135201259331528177692891399895%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Flongmen.secdps.com%2Fpay%2FgetAlipayCallback&sign=ELsZGT7V%2B%2BEg6YZyAJOQa568EWmZBF8Dr7etiFXBdbdVg7F8LbIU2voHTFWpXTz6RfZMmEbe4MrH55wg8mwZcouxiwNSqMm12jreGtcT%2BLd1kovJOMZNbJ4%2FMIHr8n%2BtiD0uO1e7zdYUW9M7V9Ou%2FRWDRJAfax3AZVeC4cRK7Ixhvw6YGugXuYuYJYfols8HkIhVxsix%2FIhMrB46SuOEP0yZFBdN2P1YuQ4cecOIsgCQF%2FyMcKKPOe5yVH1VX1Uj8OK1AasVLykpFxhlPZM9Z0nxfWcDaw4Npv4VxIc445EQ8TQHhBY%2BXwdabC1UWU9YmvfSzIOIL9dHTRaVaR%2BhOw%3D%3D&sign_type=RSA2&timestamp=2018-06-05+13%3A48%3A12&version=1.0
         */

        private String orderString;

        public String getOrderString() {
            return orderString;
        }

        public void setOrderString(String orderString) {
            this.orderString = orderString;
        }
    }
}
