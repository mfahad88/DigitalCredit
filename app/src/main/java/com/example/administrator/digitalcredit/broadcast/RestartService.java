package com.example.administrator.digitalcredit.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import com.example.administrator.digitalcredit.service.LocService;
import com.example.administrator.digitalcredit.service.LocationService;
import com.example.administrator.digitalcredit.service.MyService;


public class RestartService extends BroadcastReceiver {
    private LocationManager locationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
       try {
           Log.e("Service","Restart");
           locationManager=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
           context.startService(new Intent(context,MyService.class));
           context.startService(new Intent(context, LocService.class));
       }catch (Exception e){
           e.printStackTrace();
       }
    }
}