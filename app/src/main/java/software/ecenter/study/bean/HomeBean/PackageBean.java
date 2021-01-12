package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/4/25.
 */

public class PackageBean implements Serializable  {

    private static final long serialVersionUID = -3153049223782130315L;
    private String packageId;
    private String packagePrice;
    private String packageImage;
    private String packageName;



    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
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

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }


}
