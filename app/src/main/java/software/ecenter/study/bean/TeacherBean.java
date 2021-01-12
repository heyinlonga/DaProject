package software.ecenter.study.bean;


public class TeacherBean {

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

        private boolean isActive;

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean activated) {
            isActive = activated;
        }
    }
}
