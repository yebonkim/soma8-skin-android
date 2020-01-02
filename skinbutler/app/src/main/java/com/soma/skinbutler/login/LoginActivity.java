package com.soma.skinbutler.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.soma.skinbutler.R;
import com.soma.skinbutler.signup.SignUpActivity;
import com.soma.skinbutler.webview.WebViewActivity;
import com.soma.skinbutler.common.util.SimpleDialogBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContract.View{
    private static final String TAG = "LoginActivity";
    @BindView(R.id.idET)
    EditText idET;
    @BindView(R.id.pwET)
    EditText pwET;

    LoginPresenter presenter;

    private ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        presenter = new LoginPresenter();
        presenter.setView(this, this);
        presenter.getPermission();
    }

    @OnClick(R.id.loginBtn)
    public void login() {
        String userId = idET.getText().toString();
        String password = pwET.getText().toString();

        presenter.onLoginClick(userId, password);
    }

    @Override
    @OnClick(R.id.goSignUpBtn)
    public void goToSignUpActivity() {
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void showErrorDialog(String errorMsg) {
        SimpleDialogBuilder.makeCustomOneButtonDialogAndShow(this, errorMsg, getLayoutInflater());
    }

    @Override
    public void showLoadingDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.wait));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void stopLoadingDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToWebViewActivity(int userId) {
        Intent i = new Intent(this, WebViewActivity.class);
        i.putExtra("userId", userId);
        startActivity(i);
        finish();
    }

}