package software.ecenter.study.bean.HomeBean;

import java.util.List;

/**
 * auther: zzm
 * Date: 2019/8/15
 * Description:
 */
public class TeacherResChapterBean {

    /**
     * status : 1
     * message : 
     * data : [{"id":"13","name":"name2","type":"PPT","fileSize":"12","duration":"112"},{"id":"15","name":"导入080901","type":"PDF","fileSize":"13197","duration":"844"},{"id":"16","name":"导入080902","type":"IMG","fileSize":"13197","duration":"844"},{"id":"14","name":"test08091","type":"MP3","fileSize":"13197","duration":"844"}]
     */

    private int status;
    private String message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 13
         * name : name2
         * type : PPT
         * fileSize : 12
         * duration : 112
         */

        private String id;
        private String name;
        private String type;
        private String fileSize;
        private String duration;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFileSize() {
            return fileSize;
        }

        public void setFileSize(String fileSize) {
            this.fileSize = fileSize;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }
    }
}
