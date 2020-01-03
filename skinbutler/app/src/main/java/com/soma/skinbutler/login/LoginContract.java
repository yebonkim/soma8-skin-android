package com.soma.skinbutler.login;

/**
 * Created by yebonkim on 2017. 10. 28..
 */

public interface LoginContract {
    interface View {
        void showErrorDialog(int errorMsgId);
        void showLoadingDialog();
        void goToWebViewActivity(int userId);
        void stopLoadingDialog();
    }

    interface Presenter {
        void setView(View view);
        void onLoginClick(String userId, String password);
    }
}
