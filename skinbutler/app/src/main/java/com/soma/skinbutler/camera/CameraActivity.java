package com.soma.skinbutler.camera;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.soma.skinbutler.R;
import com.soma.skinbutler.view.CameraPreView;
import com.soma.skinbutler.util.PixelCalculator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private static final String TAG = "CameraActivity";
    CameraPreView preview;
    Camera camera;
    Context ctx;

    private final static int PERMISSIONS_REQUEST_CODE = 100;
    private final static int CAMERA_FACING = Camera.CameraInfo.CAMERA_FACING_FRONT;

    private AppCompatActivity mActivity;

    @Bind(R.id.cameraView)
    SurfaceView cameraView;
    @Bind(R.id.guideView)
    SurfaceView guideView;
    @Bind(R.id.frameLayout)
    FrameLayout frameLayout;

    SurfaceHolder cameraHolder, guideHolder;

    Path[] paths;

    Canvas canvas;

    int interval, stdX, stdY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideTitleBar();
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

        ctx = this;
        mActivity = this;


        initPath();

        cameraHolder = cameraView.getHolder();
        cameraHolder.addCallback(this);
        cameraView.setSecure(true);

        guideHolder = guideView.getHolder();
        guideHolder.addCallback(this);
        guideHolder.setFormat(PixelFormat.TRANSLUCENT);
        guideView.setZOrderMediaOverlay(true);


        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            //퍼미션 요청
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int hasCameraPermission = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA);
                int hasWriteExternalStoragePermission =
                        ContextCompat.checkSelfPermission(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (! (hasCameraPermission == PackageManager.PERMISSION_GRANTED
                        && hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED)) {
                    //퍼미션 요청
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSIONS_REQUEST_CODE);
                }
            }
        } else {
            Toast.makeText(CameraActivity.this, "Camera not supported",
                    Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.captureBtn)
    public void capture() {
        camera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }

    @OnClick(R.id.frameLayout)
    public void focusing() {
        camera.autoFocus(new Camera.AutoFocusCallback() {
            public void onAutoFocus(boolean success, Camera camera) {
            }
        });
    }

    private void hideTitleBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.widthPixels;
    }

    private int getPictureWidth() {
        if(camera==null)
            return 0;

        return camera.getParameters().getPictureSize().width;
    }

    private int getPictureHeight() {
        if(camera==null)
            return 0;

        return camera.getParameters().getPictureSize().height;
    }

    private static void doRestart(Context context) {
        int mPendingIntentId = 223344;

        try {
            if (context != null) {
                PackageManager pm = context.getPackageManager();
                if (pm != null) {
                    Intent intent = pm.getLaunchIntentForPackage(context.getPackageName());
                    if (intent != null) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent mPendingIntent = PendingIntent
                                .getActivity(context, mPendingIntentId, intent,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr =
                                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        System.exit(0);
                    } else {
                        Log.e(TAG, "Was not able to restart application, " +
                                "intent null");
                    }
                } else {
                    Log.e(TAG, "Was not able to restart application, PM null");
                }
            } else {
                Log.e(TAG, "Was not able to restart application, Context null");
            }
        } catch (Exception ex) {
            Log.e(TAG, "Was not able to restart application");
        }
    }

    private void changePreviewSize(List<Camera.Size> sizes, int width, int height, Camera.Parameters cameraParams) {
        //오차
        final double TOLERANCE = 0.1;
        double targetRatio = (double) height / width;

        if (sizes == null) return;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = height;

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

        cameraParams.setPreviewSize(optimalSize.width, optimalSize.height);
    }

    private void changeFrameLayoutSize(Camera.Parameters cameraParams) {
        int screenWidth = getScreenWidth();
        double ratio = (double) getPictureWidth() / getPictureHeight();
        int adjustHeight = (int) ((findViewById(R.id.frameLayout)).getWidth() * ratio);

        android.widget.RelativeLayout.LayoutParams params = new android.widget.RelativeLayout.LayoutParams(screenWidth, adjustHeight);
        frameLayout.setLayoutParams(params);

        changePreviewSize(cameraParams.getSupportedPreviewSizes(), screenWidth, adjustHeight, cameraParams);
    }

    private void startCamera() {
        if (preview == null) {
            preview = new CameraPreView(this, cameraView);
            preview.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
            ((FrameLayout) findViewById(R.id.frameLayout)).addView(preview);
            preview.setKeepScreenOn(true);
        }
        preview.setCamera(null);

        if (camera != null) {
            camera.release();
            camera = null;
        }

        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {
                camera = Camera.open(CAMERA_FACING);
                camera.setDisplayOrientation(setCameraDisplayOrientation(this, CAMERA_FACING,
                        camera));
                Camera.Parameters params = camera.getParameters();

                params.setRotation(setCameraDisplayOrientation(this, CAMERA_FACING, camera));
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                changeFrameLayoutSize(params);
                camera.setParameters(params);
                camera.startPreview();
            } catch (RuntimeException ex) {
                Toast.makeText(ctx, "camera_not_found " + ex.getMessage().toString(),
                        Toast.LENGTH_LONG).show();
                Log.d(TAG, "camera_not_found " + ex.getMessage().toString());
            }
        }

        preview.setCamera(camera);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCamera();
    }

    private void resetCam() {
        startCamera();
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
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


            int orientation = setCameraDisplayOrientation(CameraActivity.this,
                    CAMERA_FACING, camera);

            orientation+=180;
            orientation%=360;

            //byte array를 bitmap으로 변환
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

            //이미지를 디바이스 방향으로 회전
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);
            //1440/1920 -> 화면크기 3:4
            //4032/3024 -> 사진크기 4:3 ->  bitmap 크기
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);

            Bitmap croppedBmp;

            stdX = 800;
            stdY = 1450;
            interval = 400;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    croppedBmp = Bitmap.createBitmap(bitmap, stdX + interval * i, stdY + interval * j, interval, interval);

                    //bitmap을 byte array로 변환
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    croppedBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] currentData = stream.toByteArray();

                    //파일로 저장
                    new SaveImageTask().execute(currentData);
                }
            }
            resetCam();
            Log.d(TAG, "onPictureTaken - jpeg");
        }
    };

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
        if (cameraHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            camera.setPreviewDisplay(cameraHolder);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.release();
    }

    private void initPath() {
        paths = new Path[9];
        interval = PixelCalculator.pxToDp(800);
        stdX = PixelCalculator.pxToDp(1000);
        stdY = PixelCalculator.pxToDp(3000);
        int[][] stdPoint = new int[9][2];

        int idx = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                stdPoint[idx++] = new int[]{stdX + interval * i, stdY + interval * j};
            }
        }

        int x, y;
        for (int i = 0; i < paths.length; i++) {
            paths[i] = new Path();
            x = stdPoint[i][0];
            y = stdPoint[i][1];
            paths[i].moveTo(x, y);
            paths[i].lineTo(x, y);
            paths[i].lineTo(x + interval, y);
            paths[i].lineTo(x + interval, y + interval);
            paths[i].lineTo(x, y + interval);
            paths[i].lineTo(x, y);
        }
    }

    private void draw() {
        canvas = guideHolder.lockCanvas(null);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(3);

        for (int i = 0; i < paths.length; i++) {
            canvas.drawPath(paths[i], paint);
        }

        guideHolder.unlockCanvasAndPost(canvas);
    }

    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;

            // Write to SD Card
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/camtest");
                dir.mkdirs();

                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);

                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();

                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to "
                        + outFile.getAbsolutePath());

                refreshGallery(outFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }

    }

    /**
     * @param activity
     * @param cameraId Camera.CameraInfo.CAMERA_FACING_FRONT,
     *                 Camera.CameraInfo.CAMERA_FACING_BACK
     * @param camera   Camera Orientation
     */
    public static int setCameraDisplayOrientation(Activity activity,
                                                  int cameraId, android.hardware.Camera camera) {
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


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (requestCode == PERMISSIONS_REQUEST_CODE && grandResults.length > 0) {

            int hasCameraPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA);
            int hasWriteExternalStoragePermission =
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (hasCameraPermission == PackageManager.PERMISSION_GRANTED
                    && hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {

                //이미 퍼미션을 가지고 있음
                doRestart(this);
            } else {
                checkPermissions();
            }
        }

    }


    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        int hasCameraPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int hasWriteExternalStoragePermission =
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

        boolean cameraRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA);
        boolean writeExternalStorageRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if ((hasCameraPermission == PackageManager.PERMISSION_DENIED && cameraRationale)
                || (hasWriteExternalStoragePermission == PackageManager.PERMISSION_DENIED
                && writeExternalStorageRationale))
            showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");

        else if ((hasCameraPermission == PackageManager.PERMISSION_DENIED && !cameraRationale)
                || (hasWriteExternalStoragePermission == PackageManager.PERMISSION_DENIED
                && !writeExternalStorageRationale))
            showDialogForPermissionSetting("퍼미션 거부 + Don't ask again(다시 묻지 않음) " +
                    "체크 박스를 설정한 경우로 설정에서 퍼미션 허가해야합니다.");

        else if (hasCameraPermission == PackageManager.PERMISSION_GRANTED
                || hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
            doRestart(this);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //퍼미션 요청
                ActivityCompat.requestPermissions(CameraActivity.this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_CODE);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }

    private void showDialogForPermissionSetting(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + mActivity.getPackageName()));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(myAppSettings);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }


}