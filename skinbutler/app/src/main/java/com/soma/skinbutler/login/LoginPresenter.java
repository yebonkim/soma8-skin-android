package com.soma.skinbutler.login;


import android.app.Activity;
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
    private LoginContract.View view;

    Activity activity;
    String selectedUserId;

    @Override
    public void setView(Activity activity, LoginContract.View view) {
        this.activity = activity;
        this.view = view;
    }

    @Override
    public void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //>= mashmallow
            checkAndRequestPermission();
        }
    }

    public void login(final String userId, String password) {
        LoginRequest request = new LoginRequest(userId, password);
        ServerQuery.login(request, new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                UserResponse userResponse = (UserResponse)response.body();

                if(response.code()==200)
                    view.goToWebViewActivity(userResponse.getUser().getUserId());
                else if(response.code() == 403)
                    view.showErrorDialog(activity.getString(R.string.confirmPassword));
                else if(response.code() == 404)
                    view.showErrorDialog(activity.getString(R.string.confirmId));

                view.stopLoadingDialog();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void onLoginClick(String userId, String password) {

        if (userId.equals("")) {
            view.showErrorDialog(getString(R.string.inputId));
        } else if (password.equals("")) {
            view.showErrorDialog(getString(R.string.inputPw));
        } else {
            view.showLoadingDialog();
            login(userId, password);
        }
    }

    public void checkAndRequestPermission() {
        int result;

        String[] permissionList = PermissionList.list;

        for (int i = 0; i < permissionList.length; i++) {
            result = ContextCompat.checkSelfPermission(activity, permissionList[i]);
            if (result != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, permissionList, 1);
                break;
            }
        }
    }

    private String getString(int strId) {
        return activity.getString(strId);
    }
}
