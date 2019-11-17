package com.soma.skinbutler.common.util;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.soma.skinbutler.R;

/**
 * Created by yebonkim on 2017. 11. 25..
 */

public class ActionBarManager {

    public static void initBackArrowActionbar(AppCompatActivity activity, Toolbar toolbar, String title) {
        initDefault(activity, toolbar, title);
        toolbar.setNavigationIcon(getMenuDrawable(activity, true));
    }

    public static void initXBtnActionBar(AppCompatActivity activity, Toolbar toolbar, String title) {
        initDefault(activity, toolbar, title);
        toolbar.setNavigationIcon(getMenuDrawable(activity, false));
    }

    public static void initMainActionBar(AppCompatActivity activity, Toolbar toolbar, String title) {
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(title);
        //actionBar shadow
        activity.getSupportActionBar().setElevation(0);
    }

    protected static void initDefault(AppCompatActivity activity, Toolbar toolbar, String title) {
        activity.setSupportActionBar(toolbar);
        ((TextView)activity.findViewById(R.id.titleTV)).setText(title);
        activity.getSupportActionBar().setTitle("");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected static MaterialMenuDrawable getMenuDrawable(AppCompatActivity activity, boolean isArrow) {
        MaterialMenuDrawable drawable = new MaterialMenuDrawable(activity, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);

        if(isArrow) {
            drawable.setIconState(MaterialMenuDrawable.IconState.ARROW);
        } else {
            drawable.setIconState(MaterialMenuDrawable.IconState.X);
        }

        return drawable;
    }
}
