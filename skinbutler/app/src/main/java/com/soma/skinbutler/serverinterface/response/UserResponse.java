package com.soma.skinbutler.serverinterface.response;

import com.google.gson.annotations.SerializedName;
import com.soma.skinbutler.model.User;

/**
 * Created by yebonkim on 2017. 11. 24..
 */

public class UserResponse {
    @SerializedName("results")
    User user;

    public User getUser() {
        return user;
    }
}
