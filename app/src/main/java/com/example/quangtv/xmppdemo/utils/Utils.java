package com.example.quangtv.xmppdemo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.quangtv.xmppdemo.DummyApplication;

/**
 * Created by QuangTV on 11/10/15.
 */
public class Utils {
    static boolean isOnline = false;


    public static boolean isOnline() {

        try {

            ConnectivityManager cm =
                    (ConnectivityManager) DummyApplication.
                            getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();


        } catch (Exception e) {
            // Handle your exceptions
            return false;
        }

    }
}
