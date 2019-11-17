package com.soma.skinbutler.camera;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Camera;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.soma.skinbutler.R;
import com.soma.skinbutler.common.PermissionList;
import com.soma.skinbutler.common.util.ImageUtil;
import com.soma.skinbutler.common.util.PixelCalculator;
import com.soma.skinbutler.login.LoginContract;
import com.soma.skinbutler.serverinterface.ServerQuery;
import com.soma.skinbutler.serverinterface.request.SkinRequest;
import com.soma.skinbutler.serverinterface.response.StdSkinResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by yebonkim on 2017. 10. 30..
 */

public class CameraPresenter implements CameraContract.Presenter {
    private static final String TAG = "CameraActivity";
    private CameraContract.View view;

    Activity activity;

    Path[] paths;
    //pixel unit
    private final int interval = 800;
    private final int stdX = 1000;
    private final int stdY = 3000;
    private static int CAMERA_FACING = Camera.CameraInfo.CAMERA_FACING_FRONT;

    Camera camera;

    @Override
    public void setView(Activity activity, CameraContract.View view) {
        this.activity = activity;
        this.view = view;
    }

    @Override
    public void swapCamera() {
        if (CAMERA_FACING == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            CAMERA_FACING = Camera.CameraInfo.CAMERA_FACING_BACK;
        } else {
            CAMERA_FACING = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
        view.startCamera();
    }

    @Override
    public void initPath() {

        paths = new Path[9];
        int dpInterval = (int) PixelCalculator.pxToDp(activity, interval);
        int dpStdX = (int) PixelCalculator.pxToDp(activity, stdX);
        int dpStdY = (int) PixelCalculator.pxToDp(activity, stdY);
        float[][] stdPoint = new float[9][2];

        Log.d("Yebon", dpInterval + "/" + dpStdX + "/" + dpStdY + "path");
        //228/285/857
        //228.57/285.71/857.14

        int idx = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                stdPoint[idx++] = new float[]{dpStdX + dpInterval * i, dpStdY + dpInterval * j};
            }
        }

        float x, y;
        for (int i = 0; i < paths.length; i++) {
            paths[i] = new Path();
            x = stdPoint[i][0];
            y = stdPoint[i][1];
            paths[i].moveTo(x, y);
            paths[i].lineTo(x, y);
            paths[i].lineTo(x + dpInterval, y);
            paths[i].lineTo(x + dpInterval, y + dpInterval);
            paths[i].lineTo(x, y + dpInterval);
            paths[i].lineTo(x, y);
        }
    }

    @Override
    public void focusing() {
        camera.autoFocus(new Camera.AutoFocusCallback() {
            public void onAutoFocus(boolean success, Camera camera) {
            }
        });
    }

    @Override
    public int getPictureHeight() {
        if (camera == null)
            return 0;

        return camera.getParameters().getPictureSize().height;
    }

    @Override
    public int getPictureWidth() {
        if (camera == null)
            return 0;

        return camera.getParameters().getPictureSize().width;
    }

    @Override
    public void capture() {
        camera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }

    @Override
    public Camera getCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }

        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {
                camera = Camera.open(CAMERA_FACING);
                camera.setDisplayOrientation(setCameraDisplayOrientation(CAMERA_FACING,
                        camera));
                Camera.Parameters params = camera.getParameters();

                params.setRotation(setCameraDisplayOrientation(CAMERA_FACING, camera));
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                view.changeLayoutAndPreviewSize(params);
                camera.setParameters(params);
                camera.startPreview();
            } catch (RuntimeException ex) {
                Toast.makeText(activity, "camera_not_found " + ex.getMessage().toString(),
                        Toast.LENGTH_LONG).show();
                Log.d(TAG, "camera_not_found " + ex.getMessage().toString());
            }
        }

        return camera;
    }

    @Override
    public void releaseCamera() {
        camera.release();
    }

    @Override
    public void resetCamera(SurfaceHolder cameraHolder) {
        try {
            camera.stopPreview();
            camera.setPreviewDisplay(cameraHolder);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            Log.d(TAG, "onShutter'd");
        }
    };

    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d(TAG, "onPictureTaken - raw");
        }
    };

    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            File[] outFiles = new File[9];

            int orientation = setCameraDisplayOrientation(CAMERA_FACING, camera);

            orientation += 90;
            orientation %= 360;

            //byte array를 bitmap으로 변환
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

            //이미지를 디바이스 방향으로 회전
            Matrix matrix = new Matrix();
            //좌우반전
            matrix.setScale(-1, 1);
            matrix.postRotate(orientation);
            //2448/3264 -> bitmap
            //1440/1920 -> frameLayout
            //256/144 -> preview
            //px
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            double ratio = (double) 1440 / 144;
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
            Log.d("Yebon", "bitmap" + bitmapWidth + "/" + bitmapHeight);
            Log.d("Yebon", view.getScreenWidth() + "screen");

            Bitmap croppedBmp;

            int dpInterval = 25;
            int dpStdX = 30;
            int dpStdY = 85;


            Log.d("Yebon", dpInterval + "ggdfsfg");

            //470 / 588 / 1764

            Log.d("Yebon", dpInterval + "/" + dpStdX + "/" + dpStdY);


            int idx = 0;
            byte[][] imgData = new byte[9][];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    croppedBmp = Bitmap.createBitmap(bitmap, dpStdX + dpInterval * i, dpStdY + dpInterval * j, dpInterval, dpInterval);

                    //bitmap을 byte array로 변환
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    croppedBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    imgData[idx++] = stream.toByteArray();
                }
            }
            //파일로 저장
            view.showLoadingDialog();
            new SaveImageTask(activity).execute(imgData[0], imgData[1], imgData[2], imgData[3], imgData[4], imgData[5], imgData[6], imgData[7], imgData[8]);

            view.startCamera();
            view.dismissLoadingDialog();
            Log.d(TAG, "onPictureTaken - jpeg");
        }
    };

    protected void saveImage(byte[][] imgData) {

    }



    @Override
    public Camera.Size getOptimalPreviewSize(int width, int height, Camera.Parameters params) {
        //오차
        final double TOLERANCE = 0.1;
        double targetRatio = (double) height / width;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = height;

        List<Camera.Size> sizes = params.getSupportedPreviewSizes();

        if (sizes == null) return optimalSize;


        //tolerance 적용
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        //tolerance 미적용
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        return optimalSize;
    }

    @Override
    public void setCanvasGuideLine(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(ContextCompat.getColor(activity, R.color.guideLine));
        paint.setStrokeWidth(3);

        for (int i = 0; i < paths.length; i++) {
            canvas.drawPath(paths[i], paint);
        }
    }

    protected int setCameraDisplayOrientation(int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }

}
