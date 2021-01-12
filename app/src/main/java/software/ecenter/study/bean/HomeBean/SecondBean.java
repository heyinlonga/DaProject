package software.ecenter.study.bean.HomeBean;

/**
 * Message:
 * Author: 陈龙
 * Date: 2020/5/28 14:23
 */
public class SecondBean {
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

    public class Data {
        private String batchId;
        private String batchName;
        private String resourceCount;
        private int coinPrice;

        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
        }

        public String getBatchName() {
            return batchName;
        }

        public void setBatchName(String batchName) {
            this.batchName = batchName;
        }

        public String getResourceCount() {
            return resourceCount;
        }

        public void setResourceCount(String resourceCount) {
            this.resourceCount = resourceCount;
        }

        public int getCoinPrice() {
            return coinPrice;
        }

        public void setCoinPrice(int coinPrice) {
            this.coinPrice = coinPrice;
        }
    }
}
