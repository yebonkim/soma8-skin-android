package com.soma.skinbutler.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.soma.skinbutler.R;
import com.soma.skinbutler.common.IntentExtra;
import com.soma.skinbutler.signup.SignUpActivity;
import com.soma.skinbutler.webview.WebViewActivity;
import com.soma.skinbutler.common.util.SimpleDialogBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContract.View{
    @BindView(R.id.edit_id)
    EditText idEdit;
    @BindView(R.id.edit_pw)
    EditText pwEdit;

    private LoginPresenter mPresenter;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mPresenter = new LoginPresenter();
        mPresenter.setView(this, this);
        mPresenter.getPermission();
        setLoadingDialog();
    }

    @OnClick(R.id.btn_login)
    public void login() {
        String userId = idEdit.getText().toString();
        String password = pwEdit.getText().toString();

        mPresenter.onLoginClick(userId, password);
    }

    @Override
    @OnClick(R.id.btn_sign_up)
    public void goToSignUpActivity() {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    @Override
    public void showErrorDialog(String errorMsg) {
        SimpleDialogBuilder.makeCustomOneButtonDialogAndShow(this, errorMsg, getLayoutInflater());
    }

    @Override
    public void showLoadingDialog() {
        mProgressDialog.show();
    }

    @Override
    public void stopLoadingDialog() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToWebViewActivity(int userId) {
        Intent i = new Intent(this, WebViewActivity.class);
        i.putExtra(IntentExtra.USER_ID, userId);
        startActivity(i);
        finish();
    }

    private void setLoadingDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(getString(R.string.wait));
        mProgressDialog.setCancelable(false);
    }

}