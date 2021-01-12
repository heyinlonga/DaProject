package software.ecenter.study.View;

import java.util.List;

/**
 * Message
 * Create by Administrator
 * Create by 2019/10/31
 */
public class RankManBean {

    /**
     * data : [{"bonusPoints":500,"prizeName":"特等奖","users":[{"isSelf":false,"name":"Hdh"}]},{"bonusPoints":300,"prizeName":"一等奖","users":[{"isSelf":false,"name":"哈哈哈"},{"isSelf":true,"name":"137****1734"}]}]
     * message :
     * status : 1
     */

    private String message;
    private int status;
    private List<DataBean> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * bonusPoints : 500
         * prizeName : 特等奖
         * users : [{"isSelf":false,"name":"Hdh"}]
         */

        private int bonusPoints;
        private String prizeName;
        private List<UsersBean> users;

        public int getBonusPoints() {
            return bonusPoints;
        }

        public void setBonusPoints(int bonusPoints) {
            this.bonusPoints = bonusPoints;
        }

        public String getPrizeName() {
            return prizeName;
        }

        public void setPrizeName(String prizeName) {
            this.prizeName = prizeName;
        }

        public List<UsersBean> getUsers() {
            return users;
        }

        public void setUsers(List<UsersBean> users) {
            this.users = users;
        }

        public static class UsersBean {
            /**
             * isSelf : false
             * name : Hdh
             */

            private boolean isSelf;
            private String name;

            public boolean isIsSelf() {
                return isSelf;
            }

            public void setIsSelf(boolean isSelf) {
                this.isSelf = isSelf;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
