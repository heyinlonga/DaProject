package software.ecenter.study.bean.MineBean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/5/15.
 */

public class PersonDetailBean implements Serializable {
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

    public class Data {
        private String nickname;
        private String grade;
        private String provinceId;
        private String addressProvince;
        private String cityId;
        private String addressCity;
        private String areaId;
        private String addressArea;
        private int role;
        private String school;
        private String phoneNumber;
        private String realPhone;
        private String headImage;
        private int isBindQQ;
        private int isBindWechat;
        private String realGrade;
        private int isTeacherChecked;

        public String getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(String provinceId) {
            this.provinceId = provinceId;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getAddressArea() {
            return addressArea;
        }

        public void setAddressArea(String addressArea) {
            this.addressArea = addressArea;
        }

        public String getRealPhone() {
            return realPhone;
        }

        public void setRealPhone(String realPhone) {
            this.realPhone = realPhone;
        }

        public String getRealGrade() {
            return realGrade;
        }

        public void setRealGrade(String realGrade) {
            this.realGrade = realGrade;
        }

        public int getIsTeacherChecked() {
            return isTeacherChecked;
        }

        public void setIsTeacherChecked(int isTeacherChecked) {
            this.isTeacherChecked = isTeacherChecked;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getAddressProvince() {
            return addressProvince;
        }

        public void setAddressProvince(String addressProvince) {
            this.addressProvince = addressProvince;
        }

        public String getAddressCity() {
            return addressCity;
        }

        public void setAddressCity(String addressCity) {
            this.addressCity = addressCity;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getHeadImage() {
            return headImage;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }

        public int getIsBindQQ() {
            return isBindQQ;
        }

        public void setIsBindQQ(int isBindQQ) {
            this.isBindQQ = isBindQQ;
        }

        public int getIsBindWechat() {
            return isBindWechat;
        }

        public void setIsBindWechat(int isBindWechat) {
            this.isBindWechat = isBindWechat;
        }
    }


}
