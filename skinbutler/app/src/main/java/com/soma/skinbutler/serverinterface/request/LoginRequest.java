package com.soma.skinbutler.serverinterface.request;

/**
 * Created by yebonkim on 2017. 11. 24..
 */

public class LoginRequest {
    private String name;
    private String password;

    public LoginRequest(String name, String password) {
        this.name = name;
        this.password = password;
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
}
