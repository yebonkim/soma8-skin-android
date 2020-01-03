package com.soma.skinbutler.camera;

import java.io.File;

public interface OnImageSentListener {
    void goToResultActivity(int skinId);
    void refreshGallery(File file);
}
