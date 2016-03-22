package com.example.android.mymoiveapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.example.android.mymoiveapp.R;

/**
 * Created by weny on 3/21/16.
 */
public class Utility {


    public static boolean isInternetConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activityNetWork = cm.getActiveNetworkInfo();

        boolean isConnected = activityNetWork != null && activityNetWork.isConnectedOrConnecting();
        return isConnected;

    }




    public static String getPreferredSort(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_type_key),context.getString(R.string.sort_type_default));

    }


}
