package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/4/25.
 */

public class BookBean implements Serializable  {

    private static final long serialVersionUID = 7068950360627557640L;
    private String bookId;
    private String bookName;
    private String bookImage;
    private boolean isHaveFile;

    public boolean isHaveFile() {
        return isHaveFile;
    }

    public void setHaveFile(boolean haveFile) {
        isHaveFile = haveFile;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }



}
