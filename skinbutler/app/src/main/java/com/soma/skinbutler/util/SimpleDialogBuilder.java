package com.soma.skinbutler.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Kimyebon on 2017. 10. 28..
 */

public class SimpleDialogBuilder {

    public static void makePositiveDialogAndShow(Context context, String title, String description,
                                                 String okMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(description)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton(okMessage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

}