package com.soma.skinbutler.serverinterface.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yebonkim on 2017. 11. 25..
 */

public class SkinRequest {
    @SerializedName("spring_bright_img")
    private String springBrightImg;
    @SerializedName("spring_light_img")
    private String springLightImg;
    @SerializedName("summer_light_img")
    private String summerLightImg;
    @SerializedName("summer_mute_img")
    private String summerMuteImg;
    @SerializedName("fall_deep_img")
    private String fallDeepImg;
    @SerializedName("fall_mute_img")
    private String fallMuteImg;
    @SerializedName("winter_deep_img")
    private String winterDeepImg;
    @SerializedName("winter_bright_img")
    private String winterBrightImg;
    @SerializedName("user_skin_img")
    private String userSkinImg;
    @SerializedName("user_id")
    private int userId;


    public SkinRequest() {

    }

    public SkinRequest(String springBrightImg, String springLightImg, String summerLightImg, String summerMuteImg, String fallDeepImg, String fallMuteImg, String winterDeepImg, String winterBrightImg, String userSkinImg, int userId) {
        this.springBrightImg = springBrightImg;
        this.springLightImg = springLightImg;
        this.summerLightImg = summerLightImg;
        this.summerMuteImg = summerMuteImg;
        this.fallDeepImg = fallDeepImg;
        this.fallMuteImg = fallMuteImg;
        this.winterDeepImg = winterDeepImg;
        this.winterBrightImg = winterBrightImg;
        this.userSkinImg = userSkinImg;
        this.userId = userId;
    }

    public String getSpringBrightImg() {
        return springBrightImg;
    }

    public void setSpringBrightImg(String springBrightImg) {
        this.springBrightImg = springBrightImg;
    }

    public String getSpringLightImg() {
        return springLightImg;
    }

    public void setSpringLightImg(String springLightImg) {
        this.springLightImg = springLightImg;
    }

    public String getSummerLightImg() {
        return summerLightImg;
    }

    public void setSummerLightImg(String summerLightImg) {
        this.summerLightImg = summerLightImg;
    }

    public String getSummerMuteImg() {
        return summerMuteImg;
    }

    public void setSummerMuteImg(String summerMuteImg) {
        this.summerMuteImg = summerMuteImg;
    }

    public String getFallDeepImg() {
        return fallDeepImg;
    }

    public void setFallDeepImg(String fallDeepImg) {
        this.fallDeepImg = fallDeepImg;
    }

    public String getFallMuteImg() {
        return fallMuteImg;
    }

    public void setFallMuteImg(String fallMuteImg) {
        this.fallMuteImg = fallMuteImg;
    }

    public String getWinterDeepImg() {
        return winterDeepImg;
    }

    public void setWinterDeepImg(String winterDeepImg) {
        this.winterDeepImg = winterDeepImg;
    }

    public String getWinterBrightImg() {
        return winterBrightImg;
    }

    public void setWinterBrightImg(String winterBrightImg) {
        this.winterBrightImg = winterBrightImg;
    }

    public String getUserSkinImg() {
        return userSkinImg;
    }

    public void setUserSkinImg(String userSkinImg) {
        this.userSkinImg = userSkinImg;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
