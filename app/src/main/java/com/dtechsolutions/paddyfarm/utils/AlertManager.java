package com.dtechsolutions.paddyfarm.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dtechsolutions.paddyfarm.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AlertManager {

    private static View createCustomLayout(Context context, int imgResourceId, String title, String message) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_alert_layout, null);

        ImageView imgIcon = view.findViewById(R.id.imgIcon);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView txtMessage = view.findViewById(R.id.txtMessage);

        imgIcon.setImageResource(imgResourceId);
        txtTitle.setText(title);
        txtMessage.setText(message);

        return view;
    }

    public static void showError(Context context, String message) {
        View view = createCustomLayout(context, R.drawable.baseline_error_outline_24, "Error", message);

        new MaterialAlertDialogBuilder(context)
                .setView(view)
                .setPositiveButton("OK", null)
                .show();
    }

    public static void showSuccess(Context context, String message, String title, DialogInterface.OnClickListener onConfirm) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setCancelable(false)
                .show();
    }

    public static void showConfirmation(
            Context context,
            String title,
            String message,
            String positiveText,
            String negativeText,
            DialogInterface.OnClickListener onPositive,
            DialogInterface.OnClickListener onNegative
    ) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, onPositive)
                .setNegativeButton(negativeText, onNegative)
                .show();
    }
}
