package com.soma.skinbutler.camera;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.soma.skinbutler.R;
import com.soma.skinbutler.common.IntentExtra;
import com.soma.skinbutler.common.util.SimpleDialogBuilder;
import com.soma.skinbutler.view.CameraPreView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback, CameraContract.View {
    CameraPreView preview;

    @BindView(R.id.camera)
    SurfaceView cameraView;
    @BindView(R.id.guide)
    SurfaceView guideView;
    @BindView(R.id.layout_frame)
    FrameLayout frameLayout;

    private SurfaceHolder mCameraHolder, mGuideHolder;

    private CameraPresenter mPresenter;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        mPresenter = new CameraPresenter(getBaseContext(), onImageSentListener);
        mPresenter.setView(this);

        setCameraHolder();
        setGuideHolder();
        startCamera();
        setLoadingDialog();
    }

    private OnImageSentListener onImageSentListener = new OnImageSentListener() {
        @Override
        public void goToResultActivity(int skinId) {
            startActivity(new Intent(CameraActivity.this, ResultActivity.class)
                    .putExtra(IntentExtra.SKIN_ID, skinId));
            finish();
        }

        @Override
        public void refreshGallery(File file) {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                    .setData(Uri.fromFile(file)));
        }
    };

    protected void setCameraHolder() {
        mCameraHolder = cameraView.getHolder();
        mCameraHolder.addCallback(this);
        cameraView.setSecure(true);
    }

    protected void setGuideHolder() {
        mGuideHolder = guideView.getHolder();
        mGuideHolder.addCallback(this);
        mGuideHolder.setFormat(PixelFormat.TRANSLUCENT);
        guideView.setZOrderMediaOverlay(true);
    }

    @OnClick(R.id.btn_swap)
    public void swapCamera() {
        mPresenter.swapCamera();
    }

    @OnClick(R.id.btn_cancel)
    public void onCancel() {
        SimpleDialogBuilder.makeCustomTwoButtonDialogAndShow(this, getString(R.string.skipAlert),
                getLayoutInflater(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToResultActivity();
            }
        });
    }

    @OnClick(R.id.btn_capture)
    public void capture() {
        mPresenter.capture();
    }

    @OnClick(R.id.layout_frame)
    public void focusing() {
        mPresenter.focusing();
    }

    @Override
    public void goToResultActivity() {
        startActivity(new Intent(this, ResultActivity.class));
        finish();
    }

    @Override
    public int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.widthPixels;
    }

    @Override
    public void changeLayoutAndPreviewSize(final Camera.Parameters cameraParams) {
        int screenWidth = getScreenWidth();
        int pictureWidth = mPresenter.getPictureWidth();
        int pictureHeight = mPresenter.getPictureHeight();
        double ratio = (double) pictureWidth / pictureHeight;
        int adjustHeight = (int) (screenWidth * ratio);

        android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(screenWidth, adjustHeight);
        frameLayout.setLayoutParams(params);

        Camera.Size optimalSize = mPresenter.getOptimalPreviewSize(screenWidth, adjustHeight, cameraParams);

        cameraParams.setPreviewSize(optimalSize.width, optimalSize.height);
    }

    @Override
    public int getCameraRotation() {
        return getWindowManager().getDefaultDisplay().getRotation();
    }

    protected void setPreview() {
        if (preview == null) {
            preview = new CameraPreView(this, cameraView);
            preview.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
            ((FrameLayout) findViewById(R.id.layout_frame)).addView(preview);
            preview.setKeepScreenOn(true);
        }

        preview.setCamera(null);
    }

    @Override
    public void startCamera() {
        setPreview();
        preview.setCamera(mPresenter.getCamera());
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            synchronized (surfaceHolder) {
                draw();
            }
        } catch (Exception e) {
            Log.i("Exception", e.toString());
            return;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//        mPresenter.releaseCamera();
    }

    protected void draw() {
        Canvas canvas = mGuideHolder.lockCanvas(null);
        mPresenter.setCanvasGuideLine(getBaseContext(), canvas);
        mGuideHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //show Dialog
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.releaseCamera();
    }

    @Override
    public void showLoadingDialog() {
        mProgressDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        mProgressDialog.dismiss();
    }

    private void setLoadingDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(getString(R.string.wait));
        mProgressDialog.setCancelable(false);
    }
}