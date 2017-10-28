package com.soma.skinbutler.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.soma.skinbutler.R;
import com.soma.skinbutler.signup.SignUpActivity;
import com.soma.skinbutler.webview.WebViewActivity;
import com.soma.skinbutler.util.SimpleDialogBuilder;

import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginContract.View{
    private static final String TAG = "LoginActivity";

    LoginPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        presenter = new LoginPresenter();
        presenter.setView(this, this);
        presenter.getPermission();
    }

    @Override
    public void showDialog(final String title,final String description) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SimpleDialogBuilder.makePositiveDialogAndShow(LoginActivity.this, title,
                        description, getString(R.string.ok));
            }
        });
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToWebViewActivity() {
        startActivity(new Intent(this, WebViewActivity.class));
        finish();
    }

    @Override
    public void goToSignUpActivity() {
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }
}