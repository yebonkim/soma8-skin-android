package com.soma.skinbutler.model;


import android.support.annotation.IntDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

/**
 * Created by yebonkim on 2017. 11. 24..
 */

public class User {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SkinType.DRYNESS, SkinType.NEUTRAL, SkinType.OILLY, SkinType.COMPOUND, SkinType.SENSITIVITY})
    public @interface SkinType {
        int DRYNESS = 1;
        int NEUTRAL = 2;
        int OILLY = 3;
        int COMPOUND = 4;
        int SENSITIVITY = 5;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Gender.MALE, Gender.FEMALE})
    public @interface Gender {
        int MALE = 0;
        int FEMALE = 1;
    }

    @SerializedName("user_id")
    int userId;
    String email;
    String name;
    int sex;
    Date birthday;
    @SerializedName("contact_agree")
    int contactAgree;
    @SerializedName("profile_img")
    String profileImg;
    @SerializedName("skin_type_id")
    int skinTypeId;

    public User(int userId, String email, String name, int sex, Date birthday, int contactAgree, String profileImg, int skinTypeId) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.contactAgree = contactAgree;
        this.profileImg = profileImg;
        this.skinTypeId = skinTypeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getContactAgree() {
        return contactAgree;
    }

    public void setContactAgree(int contactAgree) {
        this.contactAgree = contactAgree;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public int getSkinTypeId() {
        return skinTypeId;
    }

    public void setSkinTypeId(int skinTypeId) {
        this.skinTypeId = skinTypeId;
    }
}
