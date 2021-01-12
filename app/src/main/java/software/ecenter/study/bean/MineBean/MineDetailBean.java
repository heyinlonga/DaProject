package software.ecenter.study.bean.MineBean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/5/15.
 */

public class MineDetailBean implements Serializable {
    private static final long serialVersionUID = 1806591641878481206L;
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
        private String headImage;
        private String nickname;
        private String currentIntegral;
        private String totalIntegral;
        private String grade;
        private String realGrade;
        private String learnCoin;
        private String realPhone;
        private String role;
        private int isTeacherChecked;

        public String getRealPhone() {
            return realPhone;
        }

        public void setRealPhone(String realPhone) {
            this.realPhone = realPhone;
        }

        public int getIsTeacherChecked() {
            return isTeacherChecked;
        }

        public void setIsTeacherChecked(int isTeacherChecked) {
            this.isTeacherChecked = isTeacherChecked;
        }

        public String getRealGrade() {
            return realGrade;
        }

        public void setRealGrade(String realGrade) {
            this.realGrade = realGrade;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getHeadImage() {
            return headImage;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getCurrentIntegral() {
            return currentIntegral;
        }

        public void setCurrentIntegral(String currentIntegral) {
            this.currentIntegral = currentIntegral;
        }

        public String getTotalIntegral() {
            return totalIntegral;
        }

        public void setTotalIntegral(String totalIntegral) {
            this.totalIntegral = totalIntegral;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getLearnCoin() {
            return learnCoin;
        }

        public void setLearnCoin(String learnCoin) {
            this.learnCoin = learnCoin;
        }
    }
}
