package com.hack.blackhawk.raktadaanam.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by alenave on 25/11/15.
 */
public class InternetConnection {

    public static boolean isInternetConnected(Context context, boolean... toastFlag) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
             if(toastFlag.length==0||(toastFlag.length>0&&toastFlag[0]==true)) {
                Toast.makeText(context, "Network error.\nPlease check your internet connection and retry.", Toast.LENGTH_SHORT).show();
            }
            return false;
        } else
            return true;
    }
}
