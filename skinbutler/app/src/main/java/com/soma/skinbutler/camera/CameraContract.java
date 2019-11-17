package com.soma.skinbutler.camera;

import android.app.Activity;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.util.List;

/**
 * Created by yebonkim on 2017. 10. 28..
 */

public interface CameraContract {
    interface View {
        void goToResultActivity();
        int getScreenWidth();
        void changeLayoutAndPreviewSize(Camera.Parameters cameraParams);
        void startCamera();
        void showLoadingDialog();
        void dismissLoadingDialog();
    }

    interface Presenter {
        void setView(Activity activity, View view);
        void focusing();
        void swapCamera();
        Camera getCamera();
        void releaseCamera();
        void resetCamera(SurfaceHolder cameraHolder);
        void initPath();
        int getPictureWidth();
        int getPictureHeight();
        void capture();
        Camera.Size getOptimalPreviewSize(int width, int height, Camera.Parameters params);
        void setCanvasGuideLine(Canvas canvas);
    }
}
