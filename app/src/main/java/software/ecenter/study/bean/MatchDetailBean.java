package software.ecenter.study.bean;

import java.util.List;

/**
 * Message
 * Create by Administrator
 * Create by 2019/11/1
 */
public class MatchDetailBean {

    /**
     * data : {"id":14,"questionList":[{"id":19,"optionImgUrl":"http://zygxkt.oss-cn-beijing.aliyuncs.com/BookImg/b71b9de0e56f4a0eabe50535a57cc64f","options":["A:<p>大名<\/p>","B:<p>王五<\/p>"],"question":"<p>小明的父亲叫什么名字<\/p>"},{"id":21,"optionImgUrl":"http://zygxkt.oss-cn-beijing.aliyuncs.com/BookImg/b71b9de0e56f4a0eabe50535a57cc64f","options":["A:<p>小青<\/p>","B:<p>小白<\/p>","C:<p>小黑<\/p>","D:<p>李青<\/p>"],"question":"<p>小明的母亲叫什么名字<\/p>"},{"id":22,"optionImgUrl":"http://zygxkt.oss-cn-beijing.aliyuncs.com/BookImg/b71b9de0e56f4a0eabe50535a57cc64f","options":["A:<p>夏天<\/p>","B:<p>冬天<\/p>","C:<p>秋天<\/p>","D:<p>花儿<\/p>"],"question":"<p>小明的姐姐叫什么名字<\/p>"},{"id":23,"optionImgUrl":"http://zygxkt.oss-cn-beijing.aliyuncs.com/BookImg/b71b9de0e56f4a0eabe50535a57cc64f","options":["A:驱蚊器群翁二全市非是否第三隔热个人艾丝凡大哥哥认为燃气管","B:12312435融合发哦很费钱必胜客大部分","C:额外IQhi是覅和反馈情况情况看开发就是佛为","D:奥山东省佛往期活佛弄死你东区哦哦为契机"],"question":"今天是时候的爱哦和我汉代还让我of那你快独孤皇后去温柔齐家网如临其境老师傅了阿斯顿海口市金凤凰是读后感肯定是赶快去任务号平均分路上大概你看我监护人可能焚枯食淡吧卡机打个卡很勤快？"}],"timeLimit":3600,"totalQuestion":4}
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
         * id : 14
         * questionList : [{"id":19,"optionImgUrl":"http://zygxkt.oss-cn-beijing.aliyuncs.com/BookImg/b71b9de0e56f4a0eabe50535a57cc64f","options":["A:<p>大名<\/p>","B:<p>王五<\/p>"],"question":"<p>小明的父亲叫什么名字<\/p>"},{"id":21,"optionImgUrl":"http://zygxkt.oss-cn-beijing.aliyuncs.com/BookImg/b71b9de0e56f4a0eabe50535a57cc64f","options":["A:<p>小青<\/p>","B:<p>小白<\/p>","C:<p>小黑<\/p>","D:<p>李青<\/p>"],"question":"<p>小明的母亲叫什么名字<\/p>"},{"id":22,"optionImgUrl":"http://zygxkt.oss-cn-beijing.aliyuncs.com/BookImg/b71b9de0e56f4a0eabe50535a57cc64f","options":["A:<p>夏天<\/p>","B:<p>冬天<\/p>","C:<p>秋天<\/p>","D:<p>花儿<\/p>"],"question":"<p>小明的姐姐叫什么名字<\/p>"},{"id":23,"optionImgUrl":"http://zygxkt.oss-cn-beijing.aliyuncs.com/BookImg/b71b9de0e56f4a0eabe50535a57cc64f","options":["A:驱蚊器群翁二全市非是否第三隔热个人艾丝凡大哥哥认为燃气管","B:12312435融合发哦很费钱必胜客大部分","C:额外IQhi是覅和反馈情况情况看开发就是佛为","D:奥山东省佛往期活佛弄死你东区哦哦为契机"],"question":"今天是时候的爱哦和我汉代还让我of那你快独孤皇后去温柔齐家网如临其境老师傅了阿斯顿海口市金凤凰是读后感肯定是赶快去任务号平均分路上大概你看我监护人可能焚枯食淡吧卡机打个卡很勤快？"}]
         * timeLimit : 3600
         * totalQuestion : 4
         */

        private int id;
        private int timeLimit;
        private int totalQuestion;
        private List<TiBean> questionList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getTimeLimit() {
            return timeLimit;
        }

        public void setTimeLimit(int timeLimit) {
            this.timeLimit = timeLimit;
        }

        public int getTotalQuestion() {
            return totalQuestion;
        }

        public void setTotalQuestion(int totalQuestion) {
            this.totalQuestion = totalQuestion;
        }

        public List<TiBean> getQuestionList() {
            return questionList;
        }

        public void setQuestionList(List<TiBean> questionList) {
            this.questionList = questionList;
        }
    }
}
