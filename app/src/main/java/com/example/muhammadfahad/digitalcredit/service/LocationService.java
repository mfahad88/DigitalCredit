package com.example.muhammadfahad.digitalcredit.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.muhammadfahad.digitalcredit.BuildConfig;
import com.example.muhammadfahad.digitalcredit.Model.MobileLocation;
import com.example.muhammadfahad.digitalcredit.Utils.Datapoints;
import com.example.muhammadfahad.digitalcredit.Utils.Helper;
import com.example.muhammadfahad.digitalcredit.client.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationService  extends Service implements LocationListener {
    private LocationManager locationManager;
    private static final long GPS_TIME = 1000*60*1;
    private Datapoints dp;
    private int recId=0;
    private Helper helper;
    private Criteria criteria;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent restartService = new Intent("RestartService");
        sendBroadcast(restartService);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        dp=Datapoints.getInstance();
        helper=Helper.getInstance();
        Log.e("LocationService--->","started");
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        criteria=new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider=locationManager.getBestProvider(criteria,true);
        Log.e("Provider--->", String.valueOf(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)));
//        this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        openSettings();
      /*  if(!locationManager.isProviderEnabled(provider)){
            this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }*/
        locationManager.requestLocationUpdates(provider,GPS_TIME,0,this);
    }
    @Override
    public void onLocationChanged(final Location location) {
        if(location!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        recId++;
                        dp.location(getApplicationContext(),location,recId, Integer.parseInt(helper.readtxtFile("config.txt")),helper);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
