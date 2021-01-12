package software.ecenter.study.bean.HomeBean;

import java.util.List;

/**
 * auther: zzm
 * Date: 2019/8/13
 * Description:
 */
public class HomeNewBean {

    /**
     * status : 1
     * message : 
     * data : {"bannerList":[{"name":"123","imgUrl":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/ActivityImg/95773f3afd2c4e87a636987bdfa2e39c","type":"BOOK","relatedId":"24","id":"14"},{"name":"123","imgUrl":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/ActivityImg/dd42a437ceb14361ae64baa6ba7c76fd","type":"CURRICULUM","relatedId":"46","id":"13"}],"indexHotList":[{"name":"title","imgUrl":"imgUrl","type":"BOOK","relatedId":"113"}],"bookList":[{"bookId":"38","bookImage":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/BookImg/a9cf8013398d4033b8f2cea65762244c","bookName":"测试图书38-勿删","isManyEdition":false},{"bookId":"39","bookImage":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/BookImg/77c6049f23e24b62889c9caf4addcf43","bookName":"测试图书39-勿删","isManyEdition":false},{"bookId":"40","bookImage":"https://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/BookImg/18q_xzydbj_6nj_sx_R 拷贝.png","bookName":"测试图书40-勿删","isManyEdition":false},{"bookId":"41","bookImage":"https://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/BookImg/18q_xzydbj_6nj_yy_RP 拷贝.png","bookName":"测试图书41-勿删","isManyEdition":false},{"bookId":"104","bookImage":"http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/BookImg/b73f6109dcc64137862428a012fb1d92","bookName":"测试011104","isManyEdition":false}],"curriculumList":[{"id":"44","name":"test06131","coinPrice":"10","imgUrl":"","type":"COMMON"},{"id":"45","name":"test06132","coinPrice":"10","imgUrl":"","type":"COMMON"},{"id":"6","name":"1","coinPrice":"1","imgUrl":"","type":"COMMON"}],"qualityEducationList":[{"id":"114","name":"test0719新建","coinPrice":"0","imgUrl":"imgUrl","type":4},{"id":"113","name":"test0719导入","coinPrice":"13","imgUrl":"","type":4},{"id":"116","name":"素质教育h5测试","coinPrice":"1","imgUrl":"","type":4}],"packageList":[{"packageId":"1","packageName":"套系组合1","packagePrice":"2","packageImage":""}]}
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
        private List<BannerListBean> bannerList;
        private List<IndexHotListBean> indexHotList;
        private List<BookListBean> bookList;
        private List<CurriculumListBean> curriculumList;
        private List<QualityEducationListBean> qualityEducationList;
        private List<PackageListBean> packageList;

        public List<BannerListBean> getBannerList() {
            return bannerList;
        }

        public void setBannerList(List<BannerListBean> bannerList) {
            this.bannerList = bannerList;
        }

        public List<IndexHotListBean> getIndexHotList() {
            return indexHotList;
        }

        public void setIndexHotList(List<IndexHotListBean> indexHotList) {
            this.indexHotList = indexHotList;
        }

        public List<BookListBean> getBookList() {
            return bookList;
        }

        public void setBookList(List<BookListBean> bookList) {
            this.bookList = bookList;
        }

        public List<CurriculumListBean> getCurriculumList() {
            return curriculumList;
        }

        public void setCurriculumList(List<CurriculumListBean> curriculumList) {
            this.curriculumList = curriculumList;
        }

        public List<QualityEducationListBean> getQualityEducationList() {
            return qualityEducationList;
        }

        public void setQualityEducationList(List<QualityEducationListBean> qualityEducationList) {
            this.qualityEducationList = qualityEducationList;
        }

        public List<PackageListBean> getPackageList() {
            return packageList;
        }

        public void setPackageList(List<PackageListBean> packageList) {
            this.packageList = packageList;
        }

        public static class BannerListBean {
            /**
             * name : 123
             * imgUrl : http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/ActivityImg/95773f3afd2c4e87a636987bdfa2e39c
             * type : BOOK
             * relatedId : 24
             * id : 14
             */

            private String name;
            private String imgUrl;
            private String type;
            private int categoryShowType;
            private String relatedId;
            private String id;

            public int getCategoryShowType() {
                return categoryShowType;
            }

            public void setCategoryShowType(int categoryShowType) {
                this.categoryShowType = categoryShowType;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getRelatedId() {
                return relatedId;
            }

            public void setRelatedId(String relatedId) {
                this.relatedId = relatedId;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        public static class IndexHotListBean {
            /**
             * name : title
             * imgUrl : imgUrl
             * type : BOOK
             * relatedId : 113
             */

            private String name;
            private String imgUrl;
            private String type;
            private String relatedId;
            private int categoryShowType;
            private boolean isDiscount;

            public boolean isDiscount() {
                return isDiscount;
            }

            public void setDiscount(boolean discount) {
                isDiscount = discount;
            }

            public int getCategoryShowType() {
                return categoryShowType;
            }

            public void setCategoryShowType(int categoryShowType) {
                this.categoryShowType = categoryShowType;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getRelatedId() {
                return relatedId;
            }

            public void setRelatedId(String relatedId) {
                this.relatedId = relatedId;
            }
        }

        public static class BookListBean {
            /**
             * bookId : 38
             * bookImage : http://hgxzy-test.oss-cn-hangzhou.aliyuncs.com/BookImg/a9cf8013398d4033b8f2cea65762244c
             * bookName : 测试图书38-勿删
             * isManyEdition : false
             */

            private String bookId;
            private String bookImage;
            private String bookName;
            private boolean isManyEdition;
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

            public String getBookId() {
                return bookId;
            }

            public void setBookId(String bookId) {
                this.bookId = bookId;
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

            public boolean isIsManyEdition() {
                return isManyEdition;
            }

            public void setIsManyEdition(boolean isManyEdition) {
                this.isManyEdition = isManyEdition;
            }
        }

        public static class CurriculumListBean {
            /**
             * id : 44
             * name : test06131
             * coinPrice : 10
             * imgUrl :
             * type : COMMON
             */

            private String id;
            private String name;
            private String coinPrice;
            private String imgUrl;
            private String type;
            private int categoryShowType;
            private boolean isDiscount;
            private String salePrice;

            public String getSalePrice() {
                return salePrice;
            }

            public void setSalePrice(String salePrice) {
                this.salePrice = salePrice;
            }

            public boolean isDiscount() {
                return isDiscount;
            }

            public void setDiscount(boolean discount) {
                isDiscount = discount;
            }
            public int getCategoryShowType() {
                return categoryShowType;
            }

            public void setCategoryShowType(int categoryShowType) {
                this.categoryShowType = categoryShowType;
            }

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

            public String getCoinPrice() {
                return coinPrice;
            }

            public void setCoinPrice(String coinPrice) {
                this.coinPrice = coinPrice;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public static class QualityEducationListBean {
            /**
             * id : 114
             * name : test0719新建
             * coinPrice : 0
             * imgUrl : imgUrl
             * type : 4
             */

            private String id;
            private String name;
            private String coinPrice;
            private String imgUrl;
            private int type;
            private boolean isDiscount;
            private String salePrice;

            public String getSalePrice() {
                return salePrice;
            }

            public void setSalePrice(String salePrice) {
                this.salePrice = salePrice;
            }
            public boolean isDiscount() {
                return isDiscount;
            }

            public void setDiscount(boolean discount) {
                isDiscount = discount;
            }
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

            public String getCoinPrice() {
                return coinPrice;
            }

            public void setCoinPrice(String coinPrice) {
                this.coinPrice = coinPrice;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }

        public static class PackageListBean {
            /**
             * packageId : 1
             * packageName : 套系组合1
             * packagePrice : 2
             * packageImage : 
             */

            private String packageId;
            private String packageName;
            private String packagePrice;
            private String packageImage;

            public String getPackageId() {
                return packageId;
            }

            public void setPackageId(String packageId) {
                this.packageId = packageId;
            }

            public String getPackageName() {
                return packageName;
            }

            public void setPackageName(String packageName) {
                this.packageName = packageName;
            }

            public String getPackagePrice() {
                return packagePrice;
            }

            public void setPackagePrice(String packagePrice) {
                this.packagePrice = packagePrice;
            }

            public String getPackageImage() {
                return packageImage;
            }

            public void setPackageImage(String packageImage) {
                this.packageImage = packageImage;
            }
        }
    }
}
