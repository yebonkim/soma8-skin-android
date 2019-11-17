package com.soma.skinbutler.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

/**
 * Created by Kimyebon on 2017. 10. 26.
 */

public class PixelCalculator {

    public static float pxToDp(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        float factor;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        Log.d("Yebon", metrics.densityDpi+"z");
        Log.d("Yebon", metrics.density+"a");
        //560
        //3.5

//        if (density >= 1.0f && density < 1.5f)
//        {
//            //MDPI
//            factor = 160;
//        }
//        else if (density == 1.5f)
//        {
//            // HDPI
//            factor = 240;
//        }
//        else if (density > 1.5f && density <= 2.0f)
//        {
//            // XHDPI
//            factor = 320;
//        }
//        else if (density > 2.0f && density <= 3.0f)
//        {
//            // XXHDPI
//            factor = 480;
//        } else {
//            factor = 640;
//        }

        return px / ((float)metrics.densityDpi / metrics.DENSITY_DEFAULT);
    }

    public static float dpToPx(Context context, float dp) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
    }

}
