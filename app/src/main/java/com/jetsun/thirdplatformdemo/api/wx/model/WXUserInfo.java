package com.jetsun.thirdplatformdemo.api.wx.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cai.jia on 2017/6/9 0009
 */

public class WXUserInfo {

    public static final String MAN = "1";
    public static final String WOMAN = "2";
    /**
     * 普通用户的标识，对当前开发者帐号唯一
     */
    private String openid;
    /**
     * 普通用户昵称
     */
    private String nickname;
    /**
     * 普通用户性别，1为男性，2为女性
     */
    private String sex;
    /**
     * 普通用户个人资料填写的省份
     */
    private String province;
    private String language;
    /**
     * 普通用户个人资料填写的城市
     */
    private String city;
    /**
     * 国家，如中国为CN
     */
    private String country;
    /**
     * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），
     * 用户没有头像时该项为空
     */
    private String headimgurl;
    /**
     * 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
     */
    private String unionid;

    public static WXUserInfo fromJson(String json) {
        WXUserInfo info = new WXUserInfo();
        try {
            JSONObject jo = new JSONObject(json);
            String openId = jo.optString("openid");
            String nickname = jo.optString("nickname");
            String sex = jo.optString("sex");
            String language = jo.optString("language");
            String city = jo.optString("city");
            String province = jo.optString("province");
            String country = jo.optString("country");
            String headimgurl = jo.optString("headimgurl");
            String unionid = jo.optString("unionid");
            info.setAll(openId, nickname, sex, language, city, province, country,
                    headimgurl, unionid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
    }

    private void setAll(String openId, String nickname, String sex, String language,
                        String city, String province, String country, String headimgurl,
                        String unionid) {
        this.openid = openId;
        this.nickname = nickname;
        this.sex = sex;
        this.province = province;
        this.city = city;
        this.country = country;
        this.language = language;
        this.headimgurl = headimgurl;
        this.unionid = unionid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "WXUserInfo{" +
                "openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", province='" + province + '\'' +
                ", language='" + language + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }
}
