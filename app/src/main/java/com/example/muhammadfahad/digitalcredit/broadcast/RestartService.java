package com.example.muhammadfahad.digitalcredit.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.muhammadfahad.digitalcredit.service.LocationService;
import com.example.muhammadfahad.digitalcredit.service.MyService;


public class RestartService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       try {
           Log.e("Service","Restart");
           context.startService(new Intent(context, LocationService.class));
           context.startService(new Intent(context,MyService.class));
       }catch (Exception e){
           e.printStackTrace();
       }
    }
}