package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mike on 2018/4/25.
 */

public class HomeBean implements Serializable  {

    private static final long serialVersionUID = 6407007585848874274L;
    private int status;
    private String message;
    private Data data;



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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        public List<BannerBean> bannerList;
        public List<PackageBean> packageList;
        public List<BookBean> bookList;
        public List<CurriculumBean> curriculumList;

        private boolean hasBookNextPage;
        private boolean hasCurriculumNextPage;
        private boolean hasPackageNextPage;
        private int curpage;
        private int bookCurPage;
        private int curriculumCurPage;
        private int packageCurPage;

        public List<BannerBean> getBannerList() {
            return bannerList;
        }

        public void setBannerList(List<BannerBean> bannerList) {
            this.bannerList = bannerList;
        }

        public List<PackageBean> getPackageList() {
            return packageList;
        }

        public void setPackageList(List<PackageBean> packageList) {
            this.packageList = packageList;
        }

        public List<BookBean> getBookList() {
            return bookList;
        }

        public void setBookList(List<BookBean> bookList) {
            this.bookList = bookList;
        }

        public List<CurriculumBean> getCurriculumList() {
            return curriculumList;
        }

        public void setCurriculumList(List<CurriculumBean> curriculumList) {
            this.curriculumList = curriculumList;
        }

        public boolean isHasBookNextPage() {
            return hasBookNextPage;
        }

        public void setHasBookNextPage(boolean hasBookNextPage) {
            this.hasBookNextPage = hasBookNextPage;
        }

        public boolean isHasCurriculumNextPage() {
            return hasCurriculumNextPage;
        }

        public void setHasCurriculumNextPage(boolean hasCurriculumNextPage) {
            this.hasCurriculumNextPage = hasCurriculumNextPage;
        }

        public boolean isHasPackageNextPage() {
            return hasPackageNextPage;
        }

        public void setHasPackageNextPage(boolean hasPackageNextPage) {
            this.hasPackageNextPage = hasPackageNextPage;
        }

        public int getBookCurPage() {
            return bookCurPage;
        }

        public void setBookCurPage(int bookCurPage) {
            this.bookCurPage = bookCurPage;
        }

        public int getCurriculumCurPage() {
            return curriculumCurPage;
        }

        public void setCurriculumCurPage(int curriculumCurPage) {
            this.curriculumCurPage = curriculumCurPage;
        }

        public int getPackageCurPage() {
            return packageCurPage;
        }

        public void setPackageCurPage(int packageCurPage) {
            this.packageCurPage = packageCurPage;
        }

        public int getCurpage() {
            return curpage;
        }

        public void setCurpage(int curpage) {
            this.curpage = curpage;
        }

    }


}
