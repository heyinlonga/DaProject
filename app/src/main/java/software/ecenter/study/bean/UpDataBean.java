package software.ecenter.study.bean;

import java.util.List;

import software.ecenter.study.bean.HomeBean.ResourceBean;

/**
 * Message
 * Create by Administrator
 * Create by 2019/1/8
 */
public class UpDataBean {

    /**
     * status : 1
     * message :
     * data : {"hasNextPage":false,"resourceList":[{"resourceId":5,"resourceName":null,"resourceSize":18,"resourceTime":null,"resourceUrl":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/ContentResource/38/%E5%9C%A8%E7%BA%BF%E9%A2%98%E5%9E%8B.doc?Expires=1546921144&OSSAccessKeyId=STS.NHLBptemT65bGA8k2oXyWcJdb&Signature=7l0fFdkjMRurQUdyo5D6KsrYA6g%3D&security-token=CAISnAN1q6Ft5B2yfSjIr4v5CcrAiLJ1gfeJRWeJjzI6VPZ7jI%2FPgDz2IH1KenFsBO0WsfU%2FmW1Q7vgclqB6T55OSAmcNZIoXwunRenkMeT7oMWQweEuAvTHcDHhv3eZsebWZ%2BLmNjm0GJOEYEzFkSle2KbzcS7YMXWuLZyOj%2BxlDLEQRRLqQjdaI91UKwB%2ByqodLmCDDeuxFRToj2HMbjJvoREupW575Liy5cee5xHC7jj90fRHg4XqPqCtddV3Xud4SMzn9eZxbLbkzSpM6gBD7rtLlKhD8Dul2daGGAt14g6aFODW%2F9ZzN3VfBM4AFrVDseL3mNBhp%2BXXjP6X8RtWOvxPWCmtHuLQycDfSuSyLYR7J%2FSpO3vdwMHKKpTur0RmAwwSPxgYfME6eD0iS04sRSHIO%2Bq79UvWQH%2F6E%2FXegftpjsQqlgSxpYbTHTXVHeXFixR%2FE4QnckYlOyQR2WHcaaIce2ROCQg6WejNEdguPE8P%2Bf615VWMCDcTx3VWruD4YOjNpqccZIPwU5RLy4MBY45ctG8nX71DmStsM8B9GoABr1Nj8dtNQ1cHKs3TJOHFI1prbA2Y8ZURBO4Y8BNqIV8Te9zr9uBDkmM7fxW95Ubh7j8yQnw6lE1DMsglte8ntg2ShIrP8q0lhZmghMXKeS4WT%2F%2BomW0Pxphz0%2F0uWYQtmdbr95w63n6hWUQKqjdqrHYu83xpJlGpSfbQlG1rrGk%3D","isNeedUpdate":false}]}
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
         * hasNextPage : false
         * resourceList : [{"resourceId":5,"resourceName":null,"resourceSize":18,"resourceTime":null,"resourceUrl":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/ContentResource/38/%E5%9C%A8%E7%BA%BF%E9%A2%98%E5%9E%8B.doc?Expires=1546921144&OSSAccessKeyId=STS.NHLBptemT65bGA8k2oXyWcJdb&Signature=7l0fFdkjMRurQUdyo5D6KsrYA6g%3D&security-token=CAISnAN1q6Ft5B2yfSjIr4v5CcrAiLJ1gfeJRWeJjzI6VPZ7jI%2FPgDz2IH1KenFsBO0WsfU%2FmW1Q7vgclqB6T55OSAmcNZIoXwunRenkMeT7oMWQweEuAvTHcDHhv3eZsebWZ%2BLmNjm0GJOEYEzFkSle2KbzcS7YMXWuLZyOj%2BxlDLEQRRLqQjdaI91UKwB%2ByqodLmCDDeuxFRToj2HMbjJvoREupW575Liy5cee5xHC7jj90fRHg4XqPqCtddV3Xud4SMzn9eZxbLbkzSpM6gBD7rtLlKhD8Dul2daGGAt14g6aFODW%2F9ZzN3VfBM4AFrVDseL3mNBhp%2BXXjP6X8RtWOvxPWCmtHuLQycDfSuSyLYR7J%2FSpO3vdwMHKKpTur0RmAwwSPxgYfME6eD0iS04sRSHIO%2Bq79UvWQH%2F6E%2FXegftpjsQqlgSxpYbTHTXVHeXFixR%2FE4QnckYlOyQR2WHcaaIce2ROCQg6WejNEdguPE8P%2Bf615VWMCDcTx3VWruD4YOjNpqccZIPwU5RLy4MBY45ctG8nX71DmStsM8B9GoABr1Nj8dtNQ1cHKs3TJOHFI1prbA2Y8ZURBO4Y8BNqIV8Te9zr9uBDkmM7fxW95Ubh7j8yQnw6lE1DMsglte8ntg2ShIrP8q0lhZmghMXKeS4WT%2F%2BomW0Pxphz0%2F0uWYQtmdbr95w63n6hWUQKqjdqrHYu83xpJlGpSfbQlG1rrGk%3D","isNeedUpdate":false}]
         */

        private boolean hasNextPage;
        private List<ResourceBean> resourceList;

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public List<ResourceBean> getResourceList() {
            return resourceList;
        }

        public void setResourceList(List<ResourceBean> resourceList) {
            this.resourceList = resourceList;
        }
    }
}
