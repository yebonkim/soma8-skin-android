package com.soma.skinbutler.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by yebonkim on 2017. 10. 28..
 */

public interface LoginContract {
    interface View {
        void showErrorDialog(String errorMsg);
        void showLoadingDialog();
        void showToast(String msg);
        void goToWebViewActivity(int userId);
        void goToSignUpActivity();
        void stopLoadingDialog();
    }

    interface Presenter {
        void setView(Activity activity, View view);
        void getPermission();
        void onLoginClick(String userId, String password);
    }
}
