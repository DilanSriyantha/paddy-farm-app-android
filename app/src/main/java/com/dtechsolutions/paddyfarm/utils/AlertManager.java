package com.dtechsolutions.paddyfarm.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.data.models.DiseaseDetectionResult;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AlertManager {

    public static void showError(
            Context context,
            String message
    ) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.custom_alert_layout, null);

        ImageView imgIcon = view.findViewById(R.id.imgIcon);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView txtMessage = view.findViewById(R.id.txtMessage);
        Button btnPositive = view.findViewById(R.id.btnPositive);
        Button btnNegative = view.findViewById(R.id.btnNegative);

        imgIcon.setImageResource(R.drawable.baseline_error_outline_24);
        txtTitle.setText(context.getString(R.string.error));
        txtMessage.setText(message);
        btnPositive.setVisibility(View.GONE);
        btnNegative.setVisibility(View.VISIBLE);

        final AlertDialog dialog = new MaterialAlertDialogBuilder(context)
                .setView(view)
                .create();

        btnNegative.setOnClickListener(v -> { dialog.dismiss(); });

        dialog.show();
    }

    public static void showSuccess(
            Context context,
            String title,
            String message,
            DialogInterface.OnClickListener onConfirm
    ) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.custom_alert_layout, null);

        ImageView imgIcon = view.findViewById(R.id.imgIcon);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView txtMessage = view.findViewById(R.id.txtMessage);
        Button btnPositive = view.findViewById(R.id.btnPositive);
        Button btnNegative = view.findViewById(R.id.btnNegative);

        imgIcon.setImageResource(R.drawable.baseline_check_circle_outline_24);
        txtTitle.setText(title);
        txtMessage.setText(message);
        btnPositive.setVisibility(View.VISIBLE);
        btnNegative.setVisibility(View.VISIBLE);

        final AlertDialog dialog = new MaterialAlertDialogBuilder(context)
                .setView(view)
                .setCancelable(false)
                .create();

        btnPositive.setOnClickListener(v -> {
            if(onConfirm != null) {
                onConfirm.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
            }
        });

        btnNegative.setOnClickListener(v -> { dialog.dismiss(); });

        dialog.show();
    }

//    public static void showConfirmation(
//            Context context,
//            String title,
//            String message,
//            String positiveText,
//            String negativeText,
//            DialogInterface.OnClickListener onPositive,
//            DialogInterface.OnClickListener onNegative
//    ) {
//        new MaterialAlertDialogBuilder(context)
//                .setTitle(title)
//                .setMessage(message)
//                .setPositiveButton(positiveText, onPositive)
//                .setNegativeButton(negativeText, onNegative)
//                .show();
//    }

    public static void showCustomAlert(Context context, View view) {
        new MaterialAlertDialogBuilder(context)
                .setView(view)
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.close), ((dialog, which) -> dialog.dismiss()))
                .show();
    }
}
