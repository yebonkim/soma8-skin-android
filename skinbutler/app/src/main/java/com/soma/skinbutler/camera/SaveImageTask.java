package com.soma.skinbutler.camera;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.soma.skinbutler.common.util.ImageUtil;
import com.soma.skinbutler.serverinterface.ServerQuery;
import com.soma.skinbutler.serverinterface.request.SkinRequest;
import com.soma.skinbutler.serverinterface.response.StdSkinResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by yebonkim on 2017. 10. 30..
 */

public class SaveImageTask extends AsyncTask<byte[], Void, Void> {
    private final static String TAG = SaveImageTask.class.getSimpleName();
    private final static String FOLDER_NAME = "/skinbutler";
    private final static String FILE_NAME_FORMAT = "%d_%d.jpg";

    private OnImageSentListener mOnImageSentListener;

    public SaveImageTask(OnImageSentListener onImageSentListener) {
        mOnImageSentListener = onImageSentListener;
    }

    @Override
    protected Void doInBackground(byte[]... data) {
        FileOutputStream outStream = null;
        File outFile = null;
        File[] files = new File[9];

        // Write to SD Card
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + FOLDER_NAME);
            dir.mkdirs();

            for(int i = 0; i < 9; i++) {
                String fileName = String.format(FILE_NAME_FORMAT, System.currentTimeMillis(), i);
                outFile = new File(dir, fileName);

                outStream = new FileOutputStream(outFile);
                outStream.write(data[i]);
                outStream.flush();
                outStream.close();
                mOnImageSentListener.refreshGallery(outFile);

                files[i] = outFile;

                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to "
                        + outFile.getAbsolutePath());
            }

            sendSkin(files);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    private void sendSkin(File[] files) {
        SkinRequest request = new SkinRequest();
        request.setSpringLightImg(ImageUtil.imageToString(files[0]));
        request.setFallMuteImg(ImageUtil.imageToString(files[1]));
        request.setFallDeepImg(ImageUtil.imageToString(files[2]));
        request.setSpringBrightImg(ImageUtil.imageToString(files[3]));
        request.setUserSkinImg(ImageUtil.imageToString(files[4]));
        request.setWinterBrightImg(ImageUtil.imageToString(files[5]));
        request.setSummerLightImg(ImageUtil.imageToString(files[6]));
        request.setSummerMuteImg(ImageUtil.imageToString(files[7]));
        request.setWinterDeepImg(ImageUtil.imageToString(files[8]));
        request.setUserId(1);

        ServerQuery.sendSkin(request, new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                StdSkinResponse stdSkinResponse = (StdSkinResponse)response.body();

                if (stdSkinResponse != null) {
                    mOnImageSentListener.goToResultActivity(stdSkinResponse.getStdSkin().getStdSkinColorId());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }
}