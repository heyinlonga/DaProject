package software.ecenter.study.bean.MineBean;

/**
 * Created by zyt on 2018/6/23.
 */

public class CheckTeacherBean {

    /**
     * status : 1
     * message :
     * data : {"auditContent":null,"school":"学校","subject":"语文","sex":1,"id":23,"state":1,"title":"初级","qualityPicturesUrl":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/Users/18519200834/TeacherCheck/1529747639_photo0.jpg?Expires=1529752830&OSSAccessKeyId=STS.NHd5uXWPzGxTQj7tQPwCGkp4i&Signature=kIC4YG4fpVHMo394UIYmHECpKDU%3D&security-token=CAISnAN1q6Ft5B2yfSjIr4vRfs%2Fsuo9b8Lq%2FU0yGkFEFe8xrhLWfizz2IH1KenFsBO0WsfU%2FmW1Q7vgclqB6T55OSAmcNZIof2DleKnlMeT7oMWQweEuAvTHcDHhv3eZsebWZ%2BLmNjm0GJOEYEzFkSle2KbzcS7YMXWuLZyOj%2BxlDLEQRRLqQjdaI91UKwB%2ByqodLmCDDeuxFRToj2HMbjJvoREupW575Liy5cee5xHC7jj90fRHg4XqPqCtddV3Xud4SMzn9eZxbLbkzSpM6gBD7rtLlKhD8Dul2daGGAt14g6aFODW%2F9ZzN3VfBM4AFrVDseL3mNBhp%2BXXjP6X8RtWOvxPWCmtHuLQycDfSuSyLYR7J%2FSpO3vdwMHKKpTur0RmAwwSPxgYfME6eD0iS04sRSHIO%2Bq79UvWQH%2F6E%2FXegftpjsQqlgSxpYbTHTXVHeXFixR%2FE4QnckYlOyQR2WHcaaIce2ROCQg6WejNEdguPE8P%2Bf615VWMCDcTx3VWruD4YOjNpqccZIPwU5RLy4MBY45ctG8nX71DmStsM8B9GoABI7LdsOzxAwaqGEOdt9DzvGM1MWlwEVjtG3cQDJq3fI1HXwPVG2nPvyHdEdaXDFphA%2BByCcOaO7cHL8Bb06krAuxSpQXjXaQwq5mI8dsr%2F5w%2BiZrJiBGpmM5eVtpRyaLHAWrxvqSS0dCcejKNmEeGBWCJEs9w9hEQvJj%2FZKTFFSE%3D","isChecked":true,"seniority":11}
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
         * auditContent : null
         * school : 学校
         * subject : 语文
         * sex : 1
         * id : 23
         * state : 1
         * title : 初级
         * qualityPicturesUrl : http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/Users/18519200834/TeacherCheck/1529747639_photo0.jpg?Expires=1529752830&OSSAccessKeyId=STS.NHd5uXWPzGxTQj7tQPwCGkp4i&Signature=kIC4YG4fpVHMo394UIYmHECpKDU%3D&security-token=CAISnAN1q6Ft5B2yfSjIr4vRfs%2Fsuo9b8Lq%2FU0yGkFEFe8xrhLWfizz2IH1KenFsBO0WsfU%2FmW1Q7vgclqB6T55OSAmcNZIof2DleKnlMeT7oMWQweEuAvTHcDHhv3eZsebWZ%2BLmNjm0GJOEYEzFkSle2KbzcS7YMXWuLZyOj%2BxlDLEQRRLqQjdaI91UKwB%2ByqodLmCDDeuxFRToj2HMbjJvoREupW575Liy5cee5xHC7jj90fRHg4XqPqCtddV3Xud4SMzn9eZxbLbkzSpM6gBD7rtLlKhD8Dul2daGGAt14g6aFODW%2F9ZzN3VfBM4AFrVDseL3mNBhp%2BXXjP6X8RtWOvxPWCmtHuLQycDfSuSyLYR7J%2FSpO3vdwMHKKpTur0RmAwwSPxgYfME6eD0iS04sRSHIO%2Bq79UvWQH%2F6E%2FXegftpjsQqlgSxpYbTHTXVHeXFixR%2FE4QnckYlOyQR2WHcaaIce2ROCQg6WejNEdguPE8P%2Bf615VWMCDcTx3VWruD4YOjNpqccZIPwU5RLy4MBY45ctG8nX71DmStsM8B9GoABI7LdsOzxAwaqGEOdt9DzvGM1MWlwEVjtG3cQDJq3fI1HXwPVG2nPvyHdEdaXDFphA%2BByCcOaO7cHL8Bb06krAuxSpQXjXaQwq5mI8dsr%2F5w%2BiZrJiBGpmM5eVtpRyaLHAWrxvqSS0dCcejKNmEeGBWCJEs9w9hEQvJj%2FZKTFFSE%3D
         * isChecked : true
         * seniority : 11
         */
        private boolean isChecked;
        private int state;
        private String subject;
        private int sex;
        private String school;
        private String title;
        private int seniority;
        private String qualityPicturesUrl;
        private String auditContent;
        private int id;

        public String getAuditContent() {
            return auditContent;
        }

        public void setAuditContent(String auditContent) {
            this.auditContent = auditContent;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getQualityPicturesUrl() {
            return qualityPicturesUrl;
        }

        public void setQualityPicturesUrl(String qualityPicturesUrl) {
            this.qualityPicturesUrl = qualityPicturesUrl;
        }

        public boolean isIsChecked() {
            return isChecked;
        }

        public void setIsChecked(boolean isChecked) {
            this.isChecked = isChecked;
        }

        public int getSeniority() {
            return seniority;
        }

        public void setSeniority(int seniority) {
            this.seniority = seniority;
        }
    }
}
