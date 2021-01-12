package software.ecenter.study.bean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/4/25.
 */

public class BaseBean implements Serializable  {

    private static final long serialVersionUID = 917343955585490540L;
    private int code;
    private int status;
    private String iamge;
    private String text;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIamge() {
        return iamge;
    }

    public void setIamge(String iamge) {
        this.iamge = iamge;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
