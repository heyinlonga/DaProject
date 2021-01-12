package software.ecenter.study.bean.HomeBean;

import java.util.List;

/**
 * auther: zzm
 * Date: 2019/8/14
 * Description:
 */
public class HomeMoreBooksBean {

    /**
     * status : 1
     * message : 
     * data : {"curpage":"0","hasBookNextPage":false,"bookList":[{"bookImage":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/BookImg/a9cf8013398d4033b8f2cea65762244c","bookName":"测试图书38-勿删","bookId":"38"},{"bookImage":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/BookImg/77c6049f23e24b62889c9caf4addcf43","bookName":"测试图书39-勿删","bookId":"39"},{"bookImage":"https://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/BookImg/18q_xzydbj_6nj_sx_R 拷贝.png","bookName":"测试图书40-勿删","bookId":"40"},{"bookImage":"https://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/BookImg/18q_xzydbj_6nj_yy_RP 拷贝.png","bookName":"测试图书41-勿删","bookId":"41"},{"bookImage":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/BookImg/b73f6109dcc64137862428a012fb1d92","bookName":"测试011104","bookId":"104"}]}
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
         * hasBookNextPage : false
         * bookList : [{"bookImage":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/BookImg/a9cf8013398d4033b8f2cea65762244c","bookName":"测试图书38-勿删","bookId":"38"},{"bookImage":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/BookImg/77c6049f23e24b62889c9caf4addcf43","bookName":"测试图书39-勿删","bookId":"39"},{"bookImage":"https://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/BookImg/18q_xzydbj_6nj_sx_R 拷贝.png","bookName":"测试图书40-勿删","bookId":"40"},{"bookImage":"https://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/BookImg/18q_xzydbj_6nj_yy_RP 拷贝.png","bookName":"测试图书41-勿删","bookId":"41"},{"bookImage":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/BookImg/b73f6109dcc64137862428a012fb1d92","bookName":"测试011104","bookId":"104"}]
         */

        private String curpage;
        private boolean hasBookNextPage;
        private List<BookListBean> bookList;

        public String getCurpage() {
            return curpage;
        }

        public void setCurpage(String curpage) {
            this.curpage = curpage;
        }

        public boolean isHasBookNextPage() {
            return hasBookNextPage;
        }

        public void setHasBookNextPage(boolean hasBookNextPage) {
            this.hasBookNextPage = hasBookNextPage;
        }

        public List<BookListBean> getBookList() {
            return bookList;
        }

        public void setBookList(List<BookListBean> bookList) {
            this.bookList = bookList;
        }

        public static class BookListBean {
            /**
             * bookImage : http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/BookImg/a9cf8013398d4033b8f2cea65762244c
             * bookName : 测试图书38-勿删
             * bookId : 38
             */

            private String bookImage;
            private String bookName;
            private String bookId;
            private boolean isDiscount;
            private int bookImgType;

            public int getBookImgType() {
                return bookImgType;
            }

            public void setBookImgType(int bookImgType) {
                this.bookImgType = bookImgType;
            }
            public boolean isDiscount() {
                return isDiscount;
            }

            public void setDiscount(boolean discount) {
                isDiscount = discount;
            }
            public String getBookImage() {
                return bookImage;
            }

            public void setBookImage(String bookImage) {
                this.bookImage = bookImage;
            }

            public String getBookName() {
                return bookName;
            }

            public void setBookName(String bookName) {
                this.bookName = bookName;
            }

            public String getBookId() {
                return bookId;
            }

            public void setBookId(String bookId) {
                this.bookId = bookId;
            }
        }
    }
}
