package com.soma.skinbutler.serverinterface.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yebonkim on 2017. 11. 25..
 */

public class SignUpRequest {
    private String email;
    private String name;
    private String password;
    private int sex;
    private String birthday;
    @SerializedName("profile_img")
    private String profileImg;
    @SerializedName("skin_type_id")
    private int skinTypeId;

    public SignUpRequest() {

    }

    public SignUpRequest(String email, String name, String password, int sex, String birthday, String profileImg, int skinTypeId) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.sex = sex;
        this.birthday = birthday;
        this.profileImg = profileImg;
        this.skinTypeId = skinTypeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getSkinTypeId() {
        return skinTypeId;
    }

    public void setSkinTypeId(int skinTypeId) {
        this.skinTypeId = skinTypeId;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}
