package software.ecenter.study.bean;

import java.util.List;

/**
 * Message
 * Create by Administrator
 * Create by 2019/11/20
 */
public class PinLishi {

    /**
     * status : 1
     * message :
     * data : {"curpage":"0","hasNextPage":false,"pinduExercises":[]}
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
         * curpage : 0
         * hasNextPage : false
         * pinduExercises : []
         */

        private String curpage;
        private boolean hasNextPage;
        private List<Pindu> pinduExercises;

        public String getCurpage() {
            return curpage;
        }

        public void setCurpage(String curpage) {
            this.curpage = curpage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public List<Pindu> getPinduExercises() {
            return pinduExercises;
        }

        public void setPinduExercises(List<Pindu> pinduExercises) {
            this.pinduExercises = pinduExercises;
        }
        public class Pindu{
            private String id;
            private String exerciseDate;
            private String audioCount;
            private String resourceName;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getExerciseDate() {
                return exerciseDate;
            }

            public void setExerciseDate(String exerciseDate) {
                this.exerciseDate = exerciseDate;
            }

            public String getAudioCount() {
                return audioCount;
            }

            public void setAudioCount(String audioCount) {
                this.audioCount = audioCount;
            }

            public String getResourceName() {
                return resourceName;
            }

            public void setResourceName(String resourceName) {
                this.resourceName = resourceName;
            }
        }
    }
}
