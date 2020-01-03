package com.soma.skinbutler.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.soma.skinbutler.R;
import com.soma.skinbutler.common.IntentExtra;
import com.soma.skinbutler.common.PermissionList;
import com.soma.skinbutler.signup.SignUpActivity;
import com.soma.skinbutler.webview.WebViewActivity;
import com.soma.skinbutler.common.util.SimpleDialogBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
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
        mPresenter.setView(this);
        getPermission();
        setLoadingDialog();
    }

    protected void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result;

            String[] permissionList = PermissionList.list;

            for (int i = 0; i < permissionList.length; i++) {
                result = ContextCompat.checkSelfPermission(getApplicationContext(), permissionList[i]);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissionList, 1);
                    break;
                }
            }
        }
    }

    @OnClick(R.id.btn_login)
    public void login() {
        String userId = idEdit.getText().toString();
        String password = pwEdit.getText().toString();

        mPresenter.onLoginClick(userId, password);
    }

    @OnClick(R.id.btn_sign_up)
    public void goToSignUpActivity() {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    @Override
    public void showErrorDialog(int errorMsgId) {
        SimpleDialogBuilder.makeCustomOneButtonDialogAndShow(this, getString(errorMsgId),
                getLayoutInflater());
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