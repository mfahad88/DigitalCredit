package com.example.muhammadfahad.digitalcredit.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.muhammadfahad.digitalcredit.Model.CustomerDetail;
import com.example.muhammadfahad.digitalcredit.Model.MobileLocation;
import com.example.muhammadfahad.digitalcredit.Utils.Datapoints;
import com.example.muhammadfahad.digitalcredit.Utils.Helper;
import com.example.muhammadfahad.digitalcredit.client.ApiClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyService extends Service implements LocationListener {
    private static final long GPS_TIME = 1000*60*1;
    private Helper helper;
    private String csvFilename;
    private TelephonyManager tm;
    private FileOutputStream fos;
    private PrintWriter writer;
    private LocationManager locationManager;
    private Datapoints dp;
    private WifiManager wifiManager;
    private NetworkInfo mWifi;
    private ConnectivityManager connectivityManager;
    private Context context;
    private RequestBody requestFile;
    private File file;
    private MultipartBody.Part body;
    private String mobileNo;
    private String imei;
    private String userId;
    private int recId=0;
    public MyService() {
    }

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

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mobileNo=intent.getStringExtra("mobileNo");
        Log.e("Service--------->","Started");
        init();
        ApiClient.getInstance().setStatus(mobileNo, "I")
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code()==200 && response.isSuccessful()){
                            if (helper.getAvailableInternalMemorySize()) {
                                getStatus();
                                /*if (wifiManager.isWifiEnabled() && mWifi.isConnected()) {

                                }*/
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                    }
                });
    }

    @SuppressLint("MissingPermission")
    private void init(){
        try {
            context=getApplicationContext();
            helper=Helper.getInstance();
            tm=(TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            imei=tm.getDeviceId();
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,GPS_TIME,0,this);
            wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            dp=Datapoints.getInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null){
            recId++;
            MobileLocation bean=dp.location(context,location,recId,helper);
            ApiClient.getInstance().setLocation(bean).enqueue(new Callback<Long>() {
                @Override
                public void onResponse(Call<Long> call, Response<Long> response) {
                    if(response.code()==200 && response.isSuccessful()){
                        Log.e("Location------->",response.body().toString());
                    }
                }

                @Override
                public void onFailure(Call<Long> call, Throwable t) {

                }
            });
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

    private void getStatus(){
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
//                Log.e("Datapoints---------->",helper.getSession(context).toString());
                ApiClient.getInstance().getCustomerDetails(mobileNo)
                        .enqueue(new Callback<CustomerDetail>() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void onResponse(Call<CustomerDetail> call, Response<CustomerDetail> response) {
                                try{
                                    if(response.body().getUserStatus().equalsIgnoreCase("I")){
                                        helper.putSession(context,"user_id",response.body().getUserId().toString());

                                        csvFilename= Environment.getExternalStorageDirectory()+ File.separator+imei+".csv";
                                        fos=new FileOutputStream(csvFilename,true);

                                        writer = new PrintWriter(fos);


                                        ApiClient.getInstance().setStatus(mobileNo,"P")
                                                .enqueue(new Callback<Void>() {
                                                    @Override
                                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                                        dp.account(context,helper,writer);
                                                        dp.battery(context,helper,writer);
                                                        dp.call(context,helper,writer);
                                                        dp.contact(context,helper,writer);
                                                        dp.sms(context,helper,writer);
                                                        dp.sensor(context,helper,writer);
                                                        dp.calendarcontractEvents(context,helper,writer);
                                                        dp.calendarcontractReminder(context,helper,writer);
                                                        dp.deviceInfo(context,helper,writer);
                                                        writer.flush();
                                                        writer.close();


                                                        file=new File(helper.zip(csvFilename,Environment.getExternalStorageDirectory()+ File.separator+imei+".zip"));
                                                        requestFile=RequestBody.create(MediaType.parse("multipart/form-data"),file);
                                                        body= MultipartBody.Part.createFormData("file",file.getName(),requestFile);
//                                                        if(helper.deleteFile(csvFilename)){
                                                            ApiClient.getInstance().upload(body).enqueue(new Callback<String>() {
                                                                @Override
                                                                public void onResponse(Call<String> call, Response<String> response) {


                                                                            ApiClient.getInstance().setStatus(mobileNo,"C").enqueue(new Callback<Void>() {
                                                                                @Override
                                                                                public void onResponse(Call<Void> call, Response<Void> response) {
                                                                                    response.body();
                                                                                }

                                                                                @Override
                                                                                public void onFailure(Call<Void> call, Throwable t) {

                                                                                }
                                                                            });


                                                                }

                                                                @Override
                                                                public void onFailure(Call<String> call, Throwable t) {
                                                                    Log.e("Error-->",t.getMessage());
                                                                }
                                                            });
//                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Void> call, Throwable t) {

                                                    }
                                                });

                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<CustomerDetail> call, Throwable t) {

                            }
                        });
            }
        },0,60000);
    }
}
