package com.jetsun.thirdplatformdemo.api.wx.model;

/**
 * Created by cai.jia on 2017/6/9 0009
 */

public class WxAuthResult {

    /**
     * ERR_OK = 0(用户同意)
     * ERR_AUTH_DENIED = -4（用户拒绝授权）
     * ERR_USER_CANCEL = -2（用户取消）
     */
    private int errorCode;

    /**
     * 用户换取access_token的code，仅在ErrCode为0时有效
     */
    private String code;

    /**
     * 第三方程序发送时用来标识其请求的唯一性的标志，由第三方程序调用sendReq时传入，
     * 由微信终端回传，state字符串长度不能超过1K
     */
    private String state;

    /**
     * 微信客户端当前语言
     */
    private String lang;

    /**
     * 微信用户当前国家信息
     */
    private String country;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public WxAuthResult(int errorCode, String code, String state, String lang, String country) {
        this.errorCode = errorCode;
        this.code = code;
        this.state = state;
        this.lang = lang;
        this.country = country;
    }

    @Override
    public String toString() {
        return "WxAuthResult{" +
                "errorCode=" + errorCode +
                ", code='" + code + '\'' +
                ", state='" + state + '\'' +
                ", lang='" + lang + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
