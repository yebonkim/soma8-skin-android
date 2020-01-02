package com.soma.skinbutler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.soma.skinbutler.R;
import com.soma.skinbutler.camera.CameraActivity;
import com.soma.skinbutler.common.util.ActionBarManager;
import com.soma.skinbutler.common.util.SimpleDialogBuilder;
import com.soma.skinbutler.webview.WebViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideActivity extends AppCompatActivity  {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        ActionBarManager.initBackArrowActionbar(this, toolbar, getString(R.string.guideTitle));
    }

    @OnClick(R.id.btn_skip)
    public void skip() {
        SimpleDialogBuilder.makeCustomTwoButtonDialogAndShow(this, getString(R.string.skipAlert),
                getLayoutInflater(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToWebViewActivity();
            }
        });
    }

    @OnClick(R.id.btn_start)
    public void goToCameraActivity() {
        startActivity(new Intent(this, CameraActivity.class));
        finish();
    }

    protected void goToWebViewActivity() {
        startActivity(new Intent(this, WebViewActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home || id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}