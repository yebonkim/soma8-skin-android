package com.soma.skinbutler.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.soma.skinbutler.common.IntentExtra;
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
    private static final String TAG = "SaveImageTask.class";
    private Activity activity;
    private File[] files = new File[]{null, null, null, null, null, null, null, null, null};

    public SaveImageTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(byte[]... data) {
        FileOutputStream outStream = null;
        File outFile = null;
        File[] files = new File[9];

        // Write to SD Card
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/camtest");
            dir.mkdirs();

            for(int i=0; i<9; i++) {
                String fileName = String.format("%d_%d.jpg", System.currentTimeMillis(), i);
                outFile = new File(dir, fileName);

                outStream = new FileOutputStream(outFile);
                outStream.write(data[i]);
                outStream.flush();
                outStream.close();
                refreshGallery(outFile);

                Log.d("Yebon", data[0][44]+"/////////"+data[1][44]+"/////////"+data[5][44]+"/////////");
                files[i] = outFile;

                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to "
                        + outFile.getAbsolutePath());
            }

            sendSkin(files);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }


    protected void sendSkin(File[] files) {
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

                activity.startActivity(new Intent(activity, ResultActivity.class).putExtra(IntentExtra.SKIN_ID, stdSkinResponse.getStdSkin().getStdSkinColorId()));
                activity.finish();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    public void goToResultActivity(int skin_id) {
        activity.startActivity(new Intent(activity, ResultActivity.class).putExtra("mSkinId", skin_id));
        activity.finish();
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        activity.sendBroadcast(mediaScanIntent);
    }

}