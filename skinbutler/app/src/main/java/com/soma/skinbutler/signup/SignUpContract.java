package com.soma.skinbutler.signup;

import android.app.Activity;

import com.soma.skinbutler.serverinterface.request.SignUpRequest;

import java.util.ArrayList;

/**
 * Created by yebonkim on 2017. 11. 25..
 */

public interface SignUpContract {
    interface View {
        void setDaySpinner(ArrayList<String> data);
        void setYearSpinner(ArrayList<String> data);
        SignUpRequest collectData(String image);
        void goToLoginActivity();
        void showErrorDialog(String errorMsg);
    }

    interface Presenter {
        void setView(SignUpContract.View view, Activity activity);
        void signUp();
        boolean validate(String name, String pw, String pwConfirm);
        void setProfile(String image);
    }
}
