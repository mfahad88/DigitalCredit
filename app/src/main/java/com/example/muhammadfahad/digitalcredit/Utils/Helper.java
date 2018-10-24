package com.example.muhammadfahad.digitalcredit.Utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.design.widget.Snackbar;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.muhammadfahad.digitalcredit.Model.MobileBean;
import com.example.muhammadfahad.digitalcredit.Model.SessionBean;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static android.support.constraint.Constraints.TAG;

public class Helper {
    public static final String MY_PREFS_NAME = "MyPrefs";
    public static Helper helper;
    private static List<SessionBean> list;
    private static SessionBean bean;
    private static final int BUFFER = 1024*10;
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

    public void OpenCsv(List<MobileBean> list, PrintWriter writer){
        for(MobileBean bean:list){
            writer.println(bean.getUser_id()+"<<>>"+bean.getCat_id()+"<<>>"+bean.getRec_id()+"<<>>"+bean.getName()+"<<>>"+bean.getValue());
        }
    }

    public boolean isValidMobileNo(String mobileNo){
        String regex="^((\\+92)|(0092))-{0,1}\\d{3}-{0,1}\\d{7}$|^\\d{11}$|^\\d{4}-\\d{7}$";
        return mobileNo.matches(regex);
    }

    public boolean isValidName(String name){
        String regex="^\\p{Lu}\\p{M}*+(?:\\p{L}\\p{M}*+|[,.'-])++(?: (?:\\p{L}\\p{M}*+|[,.'-])++)*+$";
        return name.matches(regex);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        if (((availableBlocks * blockSize) / 1024) / 1024 > 10) {
            return true;
        }
        return false;
    }

    public boolean deleteFile(String filename) {
        File file = new File(filename);
        if (file.delete()) {
            Log.e("Deleting....", file.getName());
            return true;
        }
        return false;
    }

    public String zip(String zipFilePath,String output) {
        try {
            Log.e("zip------------>",zipFilePath);
            BufferedInputStream origin;
            FileOutputStream dest = new FileOutputStream(output);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            byte data[] = new byte[BUFFER];

            FileInputStream fi = new FileInputStream(zipFilePath);
            origin = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(zipFilePath.substring(zipFilePath.lastIndexOf("/") + 1));
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public void writeTxtFile(Context context,String txt){
        try{
            File myDirectory = new File(Environment.getExternalStorageDirectory(), "DC");
            if(myDirectory.exists()){
                deleteFile(myDirectory+"config.txt");
            }
            if(!myDirectory.exists()) {
                myDirectory.mkdirs();
            }
            File gpxfile = new File(myDirectory, "config.txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(txt);
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String readtxtFile(String filename){
        File dir = Environment.getExternalStorageDirectory();
        //File yourFile = new File(dir, "path/to/the/file/inside/the/sdcard.ext");

        //Get the text file
        File file = new File(dir,"/DC/"+filename);
        // i have kept text.txt in the sd-card


        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }
        //Set the text
        return  text.toString();

    }

    public void generateKeyHash(Context context){


        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.example.muhammadfahad.digitalcredit",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:---------->", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    public void facebookLog(Context context, String eventName, Bundle parameters){
        try{
            FacebookSdk.sdkInitialize(context);
            AppEventsLogger.activateApp(context);
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
            AppEventsLogger logger=AppEventsLogger.newLogger(context);
            if(!eventName.equals(null)) {
                logger.logEvent(eventName, parameters);
            }else {
                TelephonyManager tm=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
                logger.logEvent(tm.getDeviceId(),parameters);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ProgressDialog showDialog(Context context,String title,String message){
        ProgressDialog pd=new ProgressDialog(context);
        pd.setTitle(title);
        pd.setMessage(message);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return pd;
    }

    public void showMesage(View view, String message){

        if(android.os.Build.VERSION.SDK_INT<=android.os.Build.VERSION_CODES.LOLLIPOP){
            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
        }else{
            Snackbar snackbar=Snackbar.make(view,message,Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }


    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public  void logcat(Context context) {

        File filename = new File(Environment.getExternalStorageDirectory() + "/mylog.txt");
        try {
            Runtime.getRuntime().exec("logcat -c");
            filename.createNewFile();
            String cmd = "logcat -d -f" + filename.getAbsolutePath();
            Runtime.getRuntime().exec(cmd);
            File file = new File(Environment.getExternalStorageDirectory(),"/mylog.txt");
            // i have kept text.txt in the sd-card


            //Read text from file
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                }
            }
            catch (IOException e) {
                //You'll need to add proper error handling here
            }
            //Set the text
            Bundle bundle=new Bundle();
            bundle.putString("Debug",text.toString());
            facebookLog(context,"LOGS",bundle);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
