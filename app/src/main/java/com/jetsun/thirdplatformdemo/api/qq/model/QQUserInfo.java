package com.jetsun.thirdplatformdemo.api.qq.model;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * {
 * "ret":0,
 * "msg":"",
 * "nickname":"Peter",
 * "figureurl":"http://qzapp.qlogo.cn/qzapp/111111/942FEA70050EEAFBD4DCE2C1FC775E56/30",
 * "figureurl_1":"http://qzapp.qlogo.cn/qzapp/111111/942FEA70050EEAFBD4DCE2C1FC775E56/50",
 * "figureurl_2":"http://qzapp.qlogo.cn/qzapp/111111/942FEA70050EEAFBD4DCE2C1FC775E56/100",
 * "figureurl_qq_1":"http://q.qlogo.cn/qqapp/100312990/DE1931D5330620DBD07FB4A5422917B6/40",
 * "figureurl_qq_2":"http://q.qlogo.cn/qqapp/100312990/DE1931D5330620DBD07FB4A5422917B6/100",
 * "gender":"男",
 * "is_yellow_vip":"1",
 * "vip":"1",
 * "yellow_vip_level":"7",
 * "level":"7",
 * "is_yellow_year_vip":"1"
 * Created by cai.jia on 2017/6/11.
 */

public class QQUserInfo {

    public static final int MAN = 1;
    public static final int WOMAN = 0;

    public static QQUserInfo fromJson(String json) {
        QQUserInfo qqUserInfo = new QQUserInfo();
        try {
            JSONObject jo = new JSONObject(json);
            int ret = jo.optInt("ret");
            String msg = jo.optString("msg");
            String nickname = jo.optString("nickname");
            String qzoneImg30 = jo.optString("figureurl");
            String qzoneImg50 = jo.optString("figureurl_1");
            String qzoneImg100 = jo.optString("figureurl_2");
            String qqImg40 = jo.optString("figureurl_qq_1");
            String qqImg100 = jo.optString("figureurl_qq_2");
            String gender = jo.optString("gender");
            String yellowVip = jo.optString("is_yellow_vip");
            boolean isYellowVip = TextUtils.equals("1", yellowVip);
            String vip = jo.optString("vip");
            boolean isVip = TextUtils.equals("1", vip);
            String yellowVipLevel = jo.optString("yellow_vip_level");
            String level = jo.optString("level");
            String yellowYearVip = jo.optString("is_yellow_year_vip");
            boolean isYellowYearVip = TextUtils.equals("1", yellowYearVip);
            qqUserInfo.setAll(ret, msg, nickname, qzoneImg30, qzoneImg50, qzoneImg100, qqImg40,
                    qqImg100,gender,isYellowVip,isVip,yellowVipLevel,level,isYellowYearVip);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return qqUserInfo;
    }

    private void setAll(int ret, String msg, String nickname, String qzoneImg30, String qzoneImg50,
                        String qzoneImg100, String qqImg40, String qqImg100, String gender,
                        boolean isYellowVip, boolean isVip, String yellowVipLevel, String level,
                        boolean isYellowYearVip) {
        this.ret = ret;
        this.msg = msg;
        this.nickname = nickname;
        this.qzoneImg30 = qzoneImg30;
        this.qzoneImg50 = qzoneImg50;
        this.qzoneImg100 = qzoneImg100;
        this.qqImg40 = qqImg40;
        this.qqImg100 = qqImg100;
        this.gender = TextUtils.equals("男", gender) ? 1 : 0;
        this.isYellowVip = isYellowVip;
        this.vip = isVip;
        this.yellowVipLevel = yellowVipLevel;
        this.level = level;
        this.isYellowYearVip = isYellowYearVip;
        this.qqImg = TextUtils.isEmpty(qqImg100) ? qqImg40 : qqImg100;
    }

    private int ret;
    private String msg;
    private String nickname;

    /**
     * 大小为30×30像素的QQ空间头像URL。
     */
    private String qzoneImg30;

    /**
     * 大小为50×50像素的QQ空间头像URL。
     */
    private String qzoneImg50;

    /**
     * 大小为100×100像素的QQ空间头像URL。
     */
    private String qzoneImg100;

    /**
     * 	大小为40×40像素的QQ头像URL。
     */
    private String qqImg40;

    /**
     * 大小为100×100像素的QQ头像URL。需要注意，不是所有的用户都拥有QQ的100x100的头像，但40x40像素则是一定会有。
     */
    private String qqImg100;

    private String qqImg;

    /**
     * 性别。 如果获取不到则默认返回"男"
     */
    private int gender;

    /**
     * 标识用户是否为黄钻用户（0：不是；1：是）。
     */
    private boolean isYellowVip;

    /**
     * 标识用户是否为黄钻用户（0：不是；1：是）
     */
    private boolean vip;

    /**
     * 	黄钻等级
     */
    private String yellowVipLevel;

    /**
     * 黄钻等级
     */
    private String level;

    /**
     * 识是否为年费黄钻用户（0：不是； 1：是）
     */
    private boolean isYellowYearVip;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getQzoneImg30() {
        return qzoneImg30;
    }

    public void setQzoneImg30(String qzoneImg30) {
        this.qzoneImg30 = qzoneImg30;
    }

    public String getQzoneImg50() {
        return qzoneImg50;
    }

    public void setQzoneImg50(String qzoneImg50) {
        this.qzoneImg50 = qzoneImg50;
    }

    public String getQzoneImg100() {
        return qzoneImg100;
    }

    public void setQzoneImg100(String qzoneImg100) {
        this.qzoneImg100 = qzoneImg100;
    }

    public String getQqImg() {
        return qqImg;
    }

    public void setQqImg(String qqImg) {
        this.qqImg = qqImg;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public boolean isYellowVip() {
        return isYellowVip;
    }

    public void setYellowVip(boolean yellowVip) {
        isYellowVip = yellowVip;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public String getYellowVipLevel() {
        return yellowVipLevel;
    }

    public void setYellowVipLevel(String yellowVipLevel) {
        this.yellowVipLevel = yellowVipLevel;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public boolean isYellowYearVip() {
        return isYellowYearVip;
    }

    public void setYellowYearVip(boolean yellowYearVip) {
        isYellowYearVip = yellowYearVip;
    }

    @Override
    public String toString() {
        return "QQUserInfo{" +
                "ret=" + ret +
                ", msg='" + msg + '\'' +
                ", nickname='" + nickname + '\'' +
                ", qzoneImg30='" + qzoneImg30 + '\'' +
                ", qzoneImg50='" + qzoneImg50 + '\'' +
                ", qzoneImg100='" + qzoneImg100 + '\'' +
                ", qqImg40='" + qqImg40 + '\'' +
                ", qqImg100='" + qqImg100 + '\'' +
                ", qqImg='" + qqImg + '\'' +
                ", gender=" + gender +
                ", isYellowVip=" + isYellowVip +
                ", vip=" + vip +
                ", yellowVipLevel='" + yellowVipLevel + '\'' +
                ", level='" + level + '\'' +
                ", isYellowYearVip=" + isYellowYearVip +
                '}';
    }
}
