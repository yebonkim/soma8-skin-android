package com.soma.skinbutler.camera;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.soma.skinbutler.R;
import com.soma.skinbutler.common.util.PixelCalculator;
import com.soma.skinbutler.common.util.SimpleDialogBuilder;
import com.soma.skinbutler.view.CameraPreView;
import com.soma.skinbutler.webview.WebViewActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback, CameraContract.View {
    CameraPreView preview;

    @Bind(R.id.cameraView)
    SurfaceView cameraView;
    @Bind(R.id.guideView)
    SurfaceView guideView;
    @Bind(R.id.frameLayout)
    FrameLayout frameLayout;

    SurfaceHolder cameraHolder, guideHolder;

    CameraPresenter presenter;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        presenter = new CameraPresenter();
        presenter.setView(this, this);
        presenter.initPath();

        setCameraHolder();
        setGuideHolder();
        startCamera();
    }

    protected void setCameraHolder() {
        cameraHolder = cameraView.getHolder();
        cameraHolder.addCallback(this);
        cameraView.setSecure(true);
    }

    protected void setGuideHolder() {
        guideHolder = guideView.getHolder();
        guideHolder.addCallback(this);
        guideHolder.setFormat(PixelFormat.TRANSLUCENT);
        guideView.setZOrderMediaOverlay(true);
    }

    @OnClick(R.id.swapBtn)
    public void swapCamera() {
        presenter.swapCamera();
    }

    @OnClick(R.id.cancelBtn)
    public void onCancel() {
        SimpleDialogBuilder.makeCustomTwoButtonDialogAndShow(this, getString(R.string.skipAlert),
                getLayoutInflater(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToResultActivity();
            }
        });
    }

    @OnClick(R.id.captureBtn)
    public void capture() {
        presenter.capture();
    }

    @OnClick(R.id.frameLayout)
    public void focusing() {
        presenter.focusing();
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
        int pictureWidth = presenter.getPictureWidth();
        int pictureHeight = presenter.getPictureHeight();
        double ratio = (double) pictureWidth / pictureHeight;
        int adjustHeight = (int) (screenWidth * ratio);

        android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(screenWidth, adjustHeight);
        frameLayout.setLayoutParams(params);

        Camera.Size optimalSize = presenter.getOptimalPreviewSize(screenWidth, adjustHeight, cameraParams);

        cameraParams.setPreviewSize(optimalSize.width, optimalSize.height);
    }

    protected void setPreview() {
        if (preview == null) {
            preview = new CameraPreView(this, cameraView);
            preview.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
            ((FrameLayout) findViewById(R.id.frameLayout)).addView(preview);
            preview.setKeepScreenOn(true);
        }

        preview.setCamera(null);
    }

    @Override
    public void startCamera() {
        setPreview();
        preview.setCamera(presenter.getCamera());
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
//        presenter.releaseCamera();
    }

    protected void draw() {
        Canvas canvas = guideHolder.lockCanvas(null);
        presenter.setCanvasGuideLine(canvas);
        guideHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //show Dialog
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
    public void dismissLoadingDialog() {
        progressDialog.dismiss();
    }
}