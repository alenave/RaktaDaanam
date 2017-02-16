package com.hack.blackhawk.raktadaanam.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDlg {
    private static ProgressDialog progressDialog;
//    android.app.ProgressDlg progressDialog;
    private static Context context;

    public static void showProgressDialog(Context context, String Message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(Message);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    // function to hide the loading dialog box
    public static void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
