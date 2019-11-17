package com.soma.skinbutler.common.util;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by yebonkim on 2017. 11. 29..
 */

public class ImageUtil {

    public static String imageToString(File file) {
        byte[] buf = null;
        RandomAccessFile f = null;
        String encodeBase64String = null;

        if(file==null)
            return "";

        try {
            f = new RandomAccessFile(file, "r");
            long longLength = f.length();
            int length = (int) longLength;
            if (length != longLength)
                throw new IOException("File size >= 2 GB");
            buf = new byte[length];
            f.readFully(buf);
            encodeBase64String = Base64.encodeToString(buf, Base64.DEFAULT);
            encodeBase64String = "data:image/jpeg;base64,"+encodeBase64String;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                f.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return encodeBase64String;
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static BitmapDrawable bitmapToBitmapDrawable(Context context, Intent data) {
        BitmapDrawable result = null;
        try {
            Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
            result = new BitmapDrawable(context.getResources(), image_bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
