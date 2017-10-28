package com.soma.skinbutler.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by yebonkim on 2017. 10. 28..
 */

public interface LoginContract {
    interface View {
        void showDialog(String title, String description);
        void showToast(String msg);
        void goToWebViewActivity();
        void goToSignUpActivity();
    }

    interface Presenter {
        void setView(Activity activity, View view);
        void getPermission();
        void login(String userId, String password);
        void onLoginClick(String userId, String password);
    }
}