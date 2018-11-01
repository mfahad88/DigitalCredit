package com.example.administrator.digitalcredit.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.digitalcredit.BuildConfig;
import com.example.administrator.digitalcredit.Utils.Datapoints;
import com.example.administrator.digitalcredit.Utils.Helper;
import com.example.administrator.digitalcredit.activity.LocationCheckActivity;
import com.example.administrator.digitalcredit.activity.LoginActivity;
import com.example.administrator.digitalcredit.broadcast.GpsLocationReceiver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class LocService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private Location location;
    private static final String TAG =LocService.class.getSimpleName() ;
    private Datapoints dp;
    private int recId=0;
    private Helper helper;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 1000*60*30, FASTEST_INTERVAL = 1000*60*30; // = 5 seconds
    private int LOCATION_SETTINGS_REQUEST=100;
    private LocationManager locationManager;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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
        Log.e("LocService------>","Inside");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        dp=Datapoints.getInstance();
        helper=Helper.getInstance();
        locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);

        // we build google api client
        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                        && (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))){
                    startActivity(new Intent(getApplicationContext(),LocationCheckActivity.class));
                }
            }
        },0,1000*60*10);

    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        startLocationUpdates();

        /*location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(location!=null) {
        Log.e("LOcation---->", location.getLatitude() + "," + location.getLongitude());

        startLocationUpdates();
    }*/
}

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(final Location location) {
        if(location!=null){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{

                        Log.e("LOcation---->",location.getLatitude()+","+location.getLongitude());
                        recId++;
                        dp.location(getApplicationContext(),location,recId, Integer.parseInt(helper.readtxtFile("config.txt")),helper);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
