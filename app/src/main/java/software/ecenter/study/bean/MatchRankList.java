package software.ecenter.study.bean;

import java.util.List;

/**
 * Message
 * Create by Administrator
 * Create by 2019/10/30
 */
public class MatchRankList {

    /**
     * data : {"content":[{"correctPercent":"100%","isSelf":false,"nickname":"Hdh","timeCost":"120"},{"correctPercent":"0%","isSelf":false,"nickname":"哈哈哈","timeCost":"200"},{"correctPercent":"0%","isSelf":true,"nickname":"137****1734","timeCost":"2000"}],"curpage":0,"hasNextPage":false}
     * message :
     * status : 1
     */

    private DataBean data;
    private String message;
    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

    public static class DataBean {
        /**
         * content : [{"correctPercent":"100%","isSelf":false,"nickname":"Hdh","timeCost":"120"},{"correctPercent":"0%","isSelf":false,"nickname":"哈哈哈","timeCost":"200"},{"correctPercent":"0%","isSelf":true,"nickname":"137****1734","timeCost":"2000"}]
         * curpage : 0
         * hasNextPage : false
         */

        private int curpage;
        private boolean hasNextPage;
        private List<ContentBean> content;

        public int getCurpage() {
            return curpage;
        }

        public void setCurpage(int curpage) {
            this.curpage = curpage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public List<ContentBean> getContent() {
            return content;
        }

        public void setContent(List<ContentBean> content) {
            this.content = content;
        }

        public static class ContentBean {
            /**
             * correctPercent : 100%
             * isSelf : false
             * nickname : Hdh
             * timeCost : 120
             */

            private String correctPercent;
            private boolean isSelf;
            private String nickname;
            private String timeCost;

            public String getCorrectPercent() {
                return correctPercent;
            }

            public void setCorrectPercent(String correctPercent) {
                this.correctPercent = correctPercent;
            }

            public boolean isIsSelf() {
                return isSelf;
            }

            public void setIsSelf(boolean isSelf) {
                this.isSelf = isSelf;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getTimeCost() {
                return timeCost;
            }

            public void setTimeCost(String timeCost) {
                this.timeCost = timeCost;
            }
        }
    }
}
