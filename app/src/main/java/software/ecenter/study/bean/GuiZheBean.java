package software.ecenter.study.bean;

import java.util.List;

/**
 * Message
 * Create by Administrator
 * Create by 2019/10/28
 */
public class GuiZheBean {

    /**
     * status : 1
     * message :
     * data : [{"name":"参赛方法","content":"需提前报名参赛，并在开赛时间段内参赛\r\n只有一次参赛机会，点击开始比赛按钮后，中途退出视为自动放弃比赛\r\n。"},{"name":"成绩及排名","content":"在规定时间内提交答卷，超时提交，成绩不计入大赛排行榜\r\n按正确率高低、答题快慢排名，成绩相同时早提交成绩的排名在前。\r\n"},{"name":"奖品设置规则","content":"获得实物奖品的需要留下有效的快递地址和联系方式，由于个人原因导致无法接收奖品的，后果自负。"},{"name":"其他规则","content":"解释权归举办方所有。"}]
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
         * name : 参赛方法
         * content : 需提前报名参赛，并在开赛时间段内参赛
         只有一次参赛机会，点击开始比赛按钮后，中途退出视为自动放弃比赛
         。
         */

        private String name;
        private String content;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
