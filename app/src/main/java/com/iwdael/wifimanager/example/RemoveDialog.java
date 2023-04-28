package com.iwdael.wifimanager.example;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

public class RemoveDialog {

    public static void showTipDialog(Context context, String title, String content, final View.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title)
                .setMessage(content)
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("确认 ", (dialog, which) -> {
                    dialog.dismiss();
                    if (null != listener) {
                        listener.onClick(null);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        btnNegative.setTextColor(Color.parseColor("#191F25"));

        Button btnPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btnPositive.setTextColor(Color.parseColor("#B03180"));
    }

}
