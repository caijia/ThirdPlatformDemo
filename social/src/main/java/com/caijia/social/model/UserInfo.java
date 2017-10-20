package com.caijia.social.model;

import com.caijia.social.parser.UserInfoParser;

/**
 * Created by cai.jia on 2017/6/12 0012
 */

public class UserInfo {

    public static final int MAN = 1;
    public static final int WOMAN = 0;
    private int gender;
    private String genderStr;
    private String nickName;
    private String imageUrl;
    private String unionid;

    public static UserInfo fromJson(String json, UserInfoParser parser) {
        return parser.fromJson(json);
    }

    public void setAll(int genderInt, String nickName, String imageUrl) {
        this.gender = genderInt;
        this.nickName = nickName;
        this.imageUrl = imageUrl;
        this.genderStr = genderInt == 1 ? "男" : "女";
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGenderStr() {
        return genderStr;
    }

    public void setGenderStr(String genderStr) {
        this.genderStr = genderStr;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "gender=" + gender +
                ", genderStr='" + genderStr + '\'' +
                ", nickName='" + nickName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }
}
