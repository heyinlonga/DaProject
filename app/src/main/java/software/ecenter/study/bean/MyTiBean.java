package software.ecenter.study.bean;

import java.util.List;

import software.ecenter.study.bean.TiBean;

/**
 * Message
 * Create by Administrator
 * Create by 2019/10/31
 */
public class MyTiBean {

    /**
     * data : {"content":[{"isCorrect":false,"myAnswer":"A","optionImgUrl":"http://zygxkt.oss-cn-beijing.aliyuncs.com/BookImg/aeff861d31264334ae0883f2b0c401e4","options":["A:<p>王五<\/p>","B:<p>小明<\/p>"],"question":"<p>你是谁<\/p>"}],"curpage":0,"hasNextPage":false}
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
         * content : [{"isCorrect":false,"myAnswer":"A","optionImgUrl":"http://zygxkt.oss-cn-beijing.aliyuncs.com/BookImg/aeff861d31264334ae0883f2b0c401e4","options":["A:<p>王五<\/p>","B:<p>小明<\/p>"],"question":"<p>你是谁<\/p>"}]
         * curpage : 0
         * hasNextPage : false
         */

        private int curpage;
        private boolean hasNextPage;
        private List<TiBean> content;

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

        public List<TiBean> getContent() {
            return content;
        }

        public void setContent(List<TiBean> content) {
            this.content = content;
        }

    }
}
