package com.soma.skinbutler.camera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.soma.skinbutler.R;
import com.soma.skinbutler.common.IntentExtra;
import com.soma.skinbutler.common.util.ActionBarManager;
import com.soma.skinbutler.webview.WebViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultActivity extends AppCompatActivity  {
    private static final int[] SKIN_NAMES = {R.string.light_spring, R.string.spring_bright, R.string.summer_light, R.string.summer_mute, R.string.deep_autumn, R.string.fall_mute, R.string.winter_deep, R.string.winter_bright};
    private static final int[] COLOR_IMGS = {R.drawable.light_spring, R.drawable.spring_bright, R.drawable.summer_light, R.drawable.summer_mute, R.drawable.deep_autumn, R.drawable.fall_mute, R.drawable.winter_deep, R.drawable.winter_bright};
    private static final int[] SKIN_INFOS = {R.string.light_spring_info, R.string.spring_bright_info, R.string.summer_light_info, R.string.summer_mute_info, R.string.deep_autumn_info, R.string.fall_mute_info, R.string.winter_deep_info, R.string.winter_bright_info};

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_skin_name)
    TextView skinName;
    @BindView(R.id.image_color)
    ImageView colorImage;
    @BindView(R.id.text_info)
    TextView info;

    int mSkinId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        mSkinId = getIntent().getIntExtra(IntentExtra.SKIN_ID, 1);
        mSkinId -= 1;
        ActionBarManager.initBackArrowActionbar(this, toolbar, getString(SKIN_NAMES[mSkinId]));
        setData();
    }

    protected void setData() {
        skinName.setText(getString(SKIN_NAMES[mSkinId]));
        colorImage.setBackground(getDrawable(COLOR_IMGS[mSkinId]));
        info.setText(getString(SKIN_INFOS[mSkinId]));
    }

    protected void goToCameraActivity() {
        startActivity(new Intent(this, CameraActivity.class));
        finish();
    }

    @OnClick(R.id.btn_webview)
    public void goToWebView() {
        startActivity(new Intent(this, WebViewActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home || id == android.R.id.home) {
            goToCameraActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToCameraActivity();
    }
}