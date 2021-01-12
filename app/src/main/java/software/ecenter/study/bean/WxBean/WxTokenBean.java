package software.ecenter.study.bean.WxBean;

/**
 * 创建时间: 2018/5/24
 * 编写人: wxy
 * 功能描述: 微信成功的返回的id
 */

public class WxTokenBean {

    /**
     * access_token : 8_80PnlJjeqo5t0xKOJRKU8pOqdLR2c81qf_ycJSSg4nUiRMmjGTDmw3-94Ohmlau00myPFafBblSYZ9DNJj1kyhUDnCVbVOuYawK0C-U4Rn4
     * expires_in : 7200
     * refresh_token : 8_CtdIEdkTlF8NxNg1XZAx3u2yUWjjSCSu_0dq3uCBP60VWonWrTePOCCYQs5DgoosPxkY6sS6Hf7lYFKAQ8ebdSwjQxP4o16JphAVvyJw4_4
     * openid : oIPVowSPaT5_ELIaCaijeLsYKp9w
     * scope : snsapi_userinfo
     * unionid : ogBKhwzUbpkW7UkKvgXB5nZNMMBg
     */

    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
