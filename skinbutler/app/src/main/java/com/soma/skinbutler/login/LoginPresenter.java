package com.soma.skinbutler.login;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.soma.skinbutler.common.PermissionList;
import com.soma.skinbutler.login.LoginContract;

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
    @Override
    public void login(final String userId, String password) {

    }

    @Override
    public void onLoginClick(String userId, String password) {

//        if (userId.equals(getString(R.string.ID))) {
//            //id를 선택하지 않은 경우
//            view.showDialog(getString(R.string.error), getString(R.string.selectYourId));
//        } else if (password.equals("")) {
//            view.showDialog(getString(R.string.error), getString(R.string.inputPassword));
//        } else {
//            view.showLoadingDialog();
//            login(userId, password);
//        }
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
