package com.example.muhammadfahad.digitalcredit.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.muhammadfahad.digitalcredit.Model.SessionBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Helper {
    public static final String MY_PREFS_NAME = "MyPrefs";
    public static Helper helper;
    private static List<SessionBean> list;
    private static SessionBean bean;
    public Helper() {
    }

    public static Helper getInstance() {
        if(helper==null){
            helper=new Helper();
        }
        if(list==null) {
            list = new ArrayList<>();
        }
        return helper;
    }
    public boolean sessionStore(Context ctx, List<SessionBean> list){
        SharedPreferences.Editor preferences=ctx.getSharedPreferences(MY_PREFS_NAME,ctx.MODE_PRIVATE).edit();
        for(SessionBean bean:list){
            preferences.putString(bean.getKey(),bean.getValue());
        }
        preferences.apply();
        return preferences.commit();
    }

    public Map<String, ?> getSession(Context ctx){
        SharedPreferences preferences=ctx.getSharedPreferences(MY_PREFS_NAME,ctx.MODE_PRIVATE);
        return preferences.getAll();
    }

    public boolean clearSession(Context ctx){
        SharedPreferences.Editor preferences=ctx.getSharedPreferences(MY_PREFS_NAME,ctx.MODE_PRIVATE).edit();
        preferences.clear();
        return preferences.commit();
    }

    public String CashFormatter(String cash){
        double amount = Double.parseDouble(cash);
        DecimalFormat formatter = new DecimalFormat("#,###.00");

        System.out.println("Formaterr    :"+formatter.format(amount));
        return formatter.format(amount);
    }

    public void putSession(Context ctx,String key,String value){
        bean=SessionBean.getInstance();
        bean.setKey(key);
        bean.setValue(value);

        list.add(bean);
        Log.e("Session---->",list.toString());
        helper.sessionStore(ctx,list);
    }
}
