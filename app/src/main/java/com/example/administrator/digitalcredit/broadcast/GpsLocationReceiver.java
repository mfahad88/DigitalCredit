package com.example.administrator.digitalcredit.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.digitalcredit.activity.LocationCheckActivity;
import com.example.administrator.digitalcredit.activity.LoginActivity;

public class GpsLocationReceiver extends BroadcastReceiver {
    LocationManager locationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Broadcast--->","inside");

        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
           /* Toast.makeText(context, "in android.location.PROVIDERS_CHANGED",
            Toast.LENGTH_SHORT).show();*/
            locationManager=(LocationManager) context.getSystemService(Context.LOCATION_SERVICE) ;
            Log.e("GPS--------->", String.valueOf(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)));
            if(!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                Intent pushIntent = new Intent(context, LocationCheckActivity.class);
//                pushIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(pushIntent);
            }


        }
    }
}