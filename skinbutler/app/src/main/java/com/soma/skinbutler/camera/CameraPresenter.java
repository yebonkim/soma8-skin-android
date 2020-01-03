package com.soma.skinbutler.camera;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Camera;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;

import com.soma.skinbutler.R;
import com.soma.skinbutler.common.util.PixelCalculator;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by yebonkim on 2017. 10. 30..
 */

public class CameraPresenter implements CameraContract.Presenter {
    private static final String TAG = "CameraActivity";

    private final static int INTERVAL_PX = 800;
    private final static int STD_X_PX = 1000;
    private final static int STD_Y_PX = 3000;

    private int mIntervalDp;
    private int mStdXDp;
    private int mStdYDp;

    private CameraContract.View mView;

    private Path[] mPaths;

    private int mCameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;

    private Camera mCamera;
    private SaveImageTask mSaveImageTask;

    public CameraPresenter(Context context, OnImageSentListener onImageSentListener) {
        mSaveImageTask = new SaveImageTask(onImageSentListener);
        mIntervalDp = (int) PixelCalculator.pxToDp(context, INTERVAL_PX);
        mStdXDp = (int) PixelCalculator.pxToDp(context, STD_X_PX);
        mStdYDp = (int) PixelCalculator.pxToDp(context, STD_Y_PX);

        initPath();
    }

    @Override
    public void setView(CameraContract.View view) {
        this.mView = view;
    }

    @Override
    public void swapCamera() {
        if (mCameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mCameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
        } else {
            mCameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
        mView.startCamera();
    }

    @Override
    public void focusing() {
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            public void onAutoFocus(boolean success, Camera camera) {
            }
        });
    }

    @Override
    public int getPictureHeight() {
        if (mCamera == null) {
            return 0;
        }

        return mCamera.getParameters().getPictureSize().height;
    }

    @Override
    public int getPictureWidth() {
        if (mCamera == null) {
            return 0;
        }

        return mCamera.getParameters().getPictureSize().width;
    }

    @Override
    public void capture() {
        mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }

    @Override
    public Camera getCamera() {
        releaseCamera();

        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {
                mCamera = Camera.open(mCameraFacing);
                mCamera.setDisplayOrientation(getCameraDisplayOrientation(mCameraFacing));
                Camera.Parameters params = mCamera.getParameters();

                params.setRotation(getCameraDisplayOrientation(mCameraFacing));
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                mView.changeLayoutAndPreviewSize(params);
                mCamera.setParameters(params);
                mCamera.startPreview();
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }
        }

        return mCamera;
    }

    @Override
    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void resetCamera(SurfaceHolder cameraHolder) {
        try {
            mCamera.stopPreview();
            mCamera.setPreviewDisplay(cameraHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            Log.d(TAG, "onShutter");
        }
    };

    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d(TAG, "onPictureTaken - raw");
        }
    };

    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            int orientation = getCameraDisplayOrientation(mCameraFacing);

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

            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);

            Bitmap croppedBmp;

            int idx = 0;
            byte[][] imgData = new byte[9][];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    croppedBmp = Bitmap.createBitmap(bitmap, mStdXDp + mIntervalDp * i, mStdYDp + mIntervalDp * j, mIntervalDp, mIntervalDp);

                    //bitmap을 byte array로 변환
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    croppedBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    imgData[idx++] = stream.toByteArray();
                }
            }
            //파일로 저장
            mView.showLoadingDialog();
            mSaveImageTask.execute(imgData[0], imgData[1], imgData[2], imgData[3], imgData[4], imgData[5], imgData[6], imgData[7], imgData[8]);

            mView.startCamera();
            mView.dismissLoadingDialog();
            Log.d(TAG, "onPictureTaken - jpeg");
        }
    };

    @Override
    public Camera.Size getOptimalPreviewSize(int width, int height, Camera.Parameters params) {
        //오차
        final double TOLERANCE = 0.1;
        double targetRatio = (double) height / width;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = height;

        List<Camera.Size> sizes = params.getSupportedPreviewSizes();

        //tolerance 적용
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        return optimalSize;
    }

    @Override
    public void setCanvasGuideLine(Context context, Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(ContextCompat.getColor(context, R.color.guideLine));
        paint.setStrokeWidth(3);

        for (int i = 0; i < mPaths.length; i++) {
            canvas.drawPath(mPaths[i], paint);
        }
    }

    protected int getCameraDisplayOrientation(int cameraId) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int degrees = mView.getCameraRotation() * 90;

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }

    private void initPath() {
        mPaths = new Path[9];
        // left & top point of each cell.
        float[][] stdPoint = new float[9][2];

        int idx = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                stdPoint[idx++] = new float[]{mStdXDp + mIntervalDp * i, mStdYDp + mIntervalDp * j};
            }
        }

        float x, y;
        for (int i = 0; i < mPaths.length; i++) {
            mPaths[i] = new Path();
            x = stdPoint[i][0];
            y = stdPoint[i][1];
            mPaths[i].moveTo(x, y);
            mPaths[i].lineTo(x, y);
            mPaths[i].lineTo(x + mIntervalDp, y);
            mPaths[i].lineTo(x + mIntervalDp, y + mIntervalDp);
            mPaths[i].lineTo(x, y + mIntervalDp);
            mPaths[i].lineTo(x, y);
        }
    }

}
