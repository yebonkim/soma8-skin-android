package com.soma.skinbutler.common;

import android.Manifest;

/**
 * Created by yebonkim on 2017. 10. 28..
 */

public class PermissionList {
    public static String[] list = {
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
}
