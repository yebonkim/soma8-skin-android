package com.soma.skinbutler.signup;


import com.soma.skinbutler.R;
import com.soma.skinbutler.serverinterface.ServerQuery;
import com.soma.skinbutler.serverinterface.request.SignUpRequest;
import com.soma.skinbutler.serverinterface.response.UserResponse;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by yebonkim on 2017. 11. 25..
 */

public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpContract.View mView;

    String mImageStr;

    @Override
    public void setView(SignUpContract.View view) {
        this.mView = view;
        view.setYearSpinner(makeYearArrayList());
        view.setDaySpinner(makeDayArrayList());
    }

    @Override
    public void setProfile(String image) {
        this.mImageStr = image;
    }

    @Override
    public boolean validate(String name, String pw, String pwConfirm) {
        if (name.equals("")) {
            mView.showErrorDialog(R.string.inputName);
            return false;
        }

        if (pw.equals("")) {
            mView.showErrorDialog(R.string.inputPassword);
            return false;
        }

        if (!pw.equals(pwConfirm)) {
            mView.showErrorDialog(R.string.confirmPassword);
            return false;
        }

        return true;
    }

    protected ArrayList<String> makeDayArrayList() {
        ArrayList<String> result = new ArrayList<>();

        for (int i = 1; i <= 31; i++) {
            result.add(String.valueOf(i));
        }

        return result;
    }

    protected ArrayList<String> makeYearArrayList() {
        ArrayList<String> result = new ArrayList<>();

        for (int i = 1900; i <= 2017; i++) {
            result.add(String.valueOf(i));
        }

        return result;
    }


    @Override
    public void signUp() {
        SignUpRequest request = mView.collectData(mImageStr);

        ServerQuery.signUp(request, new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                if (response.code()==200) {
                    UserResponse userResponse = (UserResponse) response.body();
                    mView.goToLoginActivity();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
