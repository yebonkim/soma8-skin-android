package com.soma.skinbutler.login;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.soma.skinbutler.R;
import com.soma.skinbutler.common.PermissionList;
import com.soma.skinbutler.serverinterface.ServerQuery;
import com.soma.skinbutler.serverinterface.request.LoginRequest;
import com.soma.skinbutler.serverinterface.response.UserResponse;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by yebonkim on 2017. 10. 28..
 */

public class LoginPresenter implements LoginContract.Presenter{
    private LoginContract.View mView;

    @Override
    public void setView(LoginContract.View view) {
        this.mView = view;
    }

    @Override
    public void onLoginClick(String userId, String password) {
        if (userId.equals("")) {
            mView.showErrorDialog(R.string.inputId);
        } else if (password.equals("")) {
            mView.showErrorDialog(R.string.inputPw);
        } else {
            mView.showLoadingDialog();
            login(userId, password);
        }
    }

    private void login(final String userId, String password) {
        LoginRequest request = new LoginRequest(userId, password);
        ServerQuery.login(request, new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                UserResponse userResponse = (UserResponse)response.body();

                if (response.code() == 200) {
                    mView.goToWebViewActivity(userResponse.getUser().getUserId());
                } else if (response.code() == 403) {
                    mView.showErrorDialog(R.string.confirmPassword);
                } else if (response.code() == 404) {
                    mView.showErrorDialog(R.string.confirmId);
                }

                mView.stopLoadingDialog();
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
