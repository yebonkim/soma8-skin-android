package com.soma.skinbutler.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by yebonkim on 2017. 11. 24..
 */

public class StdSkin {
    @SerializedName("std_skin_color_id")
    int stdSkinColorId;
    int code;
    String name;

    public StdSkin(int stdSkinColorId, int code, String name) {
        this.stdSkinColorId = stdSkinColorId;
        this.code = code;
        this.name = name;
    }

    public int getStdSkinColorId() {
        return stdSkinColorId;
    }

    public void setStdSkinColorId(int stdSkinColorId) {
        this.stdSkinColorId = stdSkinColorId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
