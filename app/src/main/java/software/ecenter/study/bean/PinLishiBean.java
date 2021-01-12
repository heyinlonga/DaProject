package software.ecenter.study.bean;

import java.util.List;

/**
 * Message
 * Create by Administrator
 * Create by 2019/11/21
 */
public class PinLishiBean {

    /**
     * status : 1
     * message :
     * data : {"resourceName":"易混声母","audioCount":"2","audioList":[{"audioUrl":"/storage/emulated/0/studyRecore/1574325730232_study.mp3","duration":"3771","score":"41"},{"audioUrl":"/storage/emulated/0/studyRecore/1574325740935_study.mp3","duration":"7542","score":"50"}]}
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
         * resourceName : 易混声母
         * audioCount : 2
         * audioList : [{"audioUrl":"/storage/emulated/0/studyRecore/1574325730232_study.mp3","duration":"3771","score":"41"},{"audioUrl":"/storage/emulated/0/studyRecore/1574325740935_study.mp3","duration":"7542","score":"50"}]
         */

        private String resourceName;
        private String audioCount;
        private String exerciseDate;
        private List<AudioListBean> audioList;

        public String getExerciseDate() {
            return exerciseDate;
        }

        public void setExerciseDate(String exerciseDate) {
            this.exerciseDate = exerciseDate;
        }

        public String getResourceName() {
            return resourceName;
        }

        public void setResourceName(String resourceName) {
            this.resourceName = resourceName;
        }

        public String getAudioCount() {
            return audioCount;
        }

        public void setAudioCount(String audioCount) {
            this.audioCount = audioCount;
        }

        public List<AudioListBean> getAudioList() {
            return audioList;
        }

        public void setAudioList(List<AudioListBean> audioList) {
            this.audioList = audioList;
        }

        public static class AudioListBean {
            /**
             * audioUrl : /storage/emulated/0/studyRecore/1574325730232_study.mp3
             * duration : 3771
             * score : 41
             */

            private String audioUrl;
            private String duration;
            private String score;
            private String title;
            private boolean select;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public boolean isSelect() {
                return select;
            }

            public void setSelect(boolean select) {
                this.select = select;
            }

            public String getAudioUrl() {
                return audioUrl;
            }

            public void setAudioUrl(String audioUrl) {
                this.audioUrl = audioUrl;
            }

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }
        }
    }
}
