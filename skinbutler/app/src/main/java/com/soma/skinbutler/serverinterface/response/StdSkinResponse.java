package com.soma.skinbutler.serverinterface.response;

import com.google.gson.annotations.SerializedName;
import com.soma.skinbutler.model.StdSkin;
import com.soma.skinbutler.model.User;

/**
 * Created by yebonkim on 2017. 11. 24..
 */

public class StdSkinResponse {
    @SerializedName("results")
    StdSkin stdSkin;

    public StdSkinResponse(StdSkin stdSkin) {
        this.stdSkin = stdSkin;
    }

    public StdSkin getStdSkin() {
        return stdSkin;
    }
}
