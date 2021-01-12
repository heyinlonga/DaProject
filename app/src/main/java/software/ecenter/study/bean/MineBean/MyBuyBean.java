package software.ecenter.study.bean.MineBean;

import java.io.Serializable;

/**
 * Created by Mike on 2018/4/25.
 */

public class MyBuyBean implements Serializable  {
    private static final long serialVersionUID = -6953361436212641422L;
    private String purchaseId;
    private String purchaseTitle;
    private int purchaseMode; //购买方式1、微信 2、支付宝 3、学习币 4、积分 5、答疑券
    private String purchaseDate;
    private int purchaseType; //1、整书 2、组合 3、课时 4、答疑
    private String purchaseResourceType;
    private String purchaseShowType;
    private String mainTitle;
    private String subTitle;
    private String resourceId;
    private int categoryShowType;

    public String getPurchaseShowType() {
        return purchaseShowType;
    }

    public void setPurchaseShowType(String purchaseShowType) {
        this.purchaseShowType = purchaseShowType;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getCategoryShowType() {
        return categoryShowType;
    }

    public void setCategoryShowType(int categoryShowType) {
        this.categoryShowType = categoryShowType;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getPurchaseTitle() {
        return purchaseTitle;
    }

    public void setPurchaseTitle(String purchaseTitle) {
        this.purchaseTitle = purchaseTitle;
    }

    public int getPurchaseMode() {
        return purchaseMode;
    }

    public void setPurchaseMode(int purchaseMode) {
        this.purchaseMode = purchaseMode;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public int getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(int purchaseType) {
        this.purchaseType = purchaseType;
    }

    public String getPurchaseResourceType() {
        return purchaseResourceType;
    }

    public void setPurchaseResourceType(String purchaseResourceType) {
        this.purchaseResourceType = purchaseResourceType;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
