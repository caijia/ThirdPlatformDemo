package com.jetsun.thirdPlatform.model;

import com.jetsun.thirdPlatform.parser.UserInfoParser;

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

    @Override
    public String toString() {
        return "UserInfo{" +
                "gender=" + gender +
                ", genderStr='" + genderStr + '\'' +
                ", nickName='" + nickName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
