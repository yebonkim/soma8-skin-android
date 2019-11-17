package com.soma.skinbutler.common.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.soma.skinbutler.R;

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

    public static void showDialog(Context context, String title, String description, String okMsg,
                                  String cancelMsg, DialogInterface.OnClickListener okFunc,
                                  DialogInterface.OnClickListener cancelFunc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(description)
                .setTitle(title)
                .setCancelable(true)
                .setPositiveButton(okMsg, okFunc)
                .setNegativeButton(cancelMsg, cancelFunc);
        builder.show();
    }

    public static void makeCustomTwoButtonDialogAndShow(Context context, String titleTxt, LayoutInflater inflater,
                                                        View.OnClickListener okFunc) {
        View alertLayout = inflater.inflate(R.layout.dialog_default_two_button, null);
        final Button cancelBtn = (Button)alertLayout.findViewById(R.id.cancelBtn);
        final Button okBtn = (Button)alertLayout.findViewById(R.id.okBtn);
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        if(titleTxt!=null) {
            TextView title = (TextView)alertLayout.findViewById(R.id.titleTV);
            title.setText(titleTxt);
        }

        dialogBuilder.setView(alertLayout);
        dialogBuilder.setCancelable(false);

        final AlertDialog dialog = dialogBuilder.show();

        okBtn.setOnClickListener(okFunc);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public static void makeCustomOneButtonDialogAndShow(Context context, String titleTxt, LayoutInflater inflater) {
        View alertLayout = inflater.inflate(R.layout.dialog_default_one_button, null);
        final Button okBtn = (Button)alertLayout.findViewById(R.id.okBtn);
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        if(titleTxt!=null) {
            TextView title = (TextView)alertLayout.findViewById(R.id.titleTV);
            title.setText(titleTxt);
        }

        dialogBuilder.setView(alertLayout);
        dialogBuilder.setCancelable(false);

        final AlertDialog dialog = dialogBuilder.show();

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}