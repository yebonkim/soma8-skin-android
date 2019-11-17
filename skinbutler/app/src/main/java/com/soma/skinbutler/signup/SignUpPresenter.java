package com.soma.skinbutler.signup;


import android.app.Activity;
import android.app.DownloadManager;
import android.util.Log;


import com.soma.skinbutler.R;
import com.soma.skinbutler.serverinterface.ServerQuery;
import com.soma.skinbutler.serverinterface.request.SignUpRequest;
import com.soma.skinbutler.serverinterface.response.UserResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by yebonkim on 2017. 11. 25..
 */

public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpContract.View view;

    Activity activity;

    String image;

    @Override
    public void setView(SignUpContract.View view, Activity activity) {
        this.activity = activity;
        this.view = view;
        view.setYearSpinner(makeYearArrayList());
        view.setDaySpinner(makeDayArrayList());
    }

    @Override
    public void setProfile(String image) {
        this.image = image;
    }

    @Override
    public boolean validate(String name, String pw, String pwConfirm) {
        if(name.equals("")) {
            view.showErrorDialog(activity.getString(R.string.inputName));
            return false;
        }

        if(pw.equals("")) {
            view.showErrorDialog(activity.getString(R.string.inputPassword));
            return false;
        }

        if(!pw.equals(pwConfirm)) {
            view.showErrorDialog(activity.getString(R.string.confirmPassword));
            return false;
        }

        return true;
    }

    protected ArrayList<String> makeDayArrayList() {
        ArrayList<String> result = new ArrayList<>();

        for(int i=1; i<=31; i++) {
            result.add(i+"");
        }

        return result;
    }

    protected ArrayList<String> makeYearArrayList() {
        ArrayList<String> result = new ArrayList<>();

        for(int i=1900; i<=2017; i++) {
            result.add(i+"");
        }

        return result;
    }


    @Override
    public void signUp() {
        SignUpRequest request = view.collectData(image);

        ServerQuery.signUp(request, new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {

                if(response.code()==200) {
                    UserResponse userResponse = (UserResponse) response.body();
                    Log.d("Yebon", "how!");
//                    view.goToLoginActivity();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }
}
