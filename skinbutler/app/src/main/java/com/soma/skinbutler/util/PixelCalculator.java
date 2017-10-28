package com.soma.skinbutler.util;

import android.content.res.Resources;

/**
 * Created by Kimyebon on 2017. 10. 26.
 */

public class PixelCalculator {

    public static int pxToDp(int px) {
        if(px==0)
            return 0;

        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
