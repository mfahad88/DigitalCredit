package com.example.administrator.digitalcredit.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.digitalcredit.BuildConfig;
import com.example.administrator.digitalcredit.Model.AppVersion;
import com.example.administrator.digitalcredit.Model.CustomerDetail;
import com.example.administrator.digitalcredit.service.LocService;

import com.example.administrator.digitalcredit.Model.SessionBean;
import com.example.administrator.digitalcredit.R;
import com.example.administrator.digitalcredit.Utils.Helper;
import com.example.administrator.digitalcredit.client.ApiClient;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG ="LOGIN ACTIVITY" ;
    private EditText edtTextMobileNo,edtTextPassword;
    private TextView tvSignUp;
    private Button btnSign;
    private Intent intent,i;
    private Helper helper;
    private List<SessionBean> list;
    private SessionBean bean;
    private ProgressDialog pd;
    private Integer userId=0;
    private TelephonyManager tm;
    private Bundle bundle;
    private AccountManager am;
    private Account[] accounts;
    private LocationManager locationManager;
    @Override
    protected void onPostResume() {
        super.onPostResume();
        edtTextMobileNo.setText("");
        edtTextPassword.setText("");
        if (pd.isShowing()) {
            pd.dismiss();
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishAffinity();
        System.exit(0);
    }

    /*@Override
    protected void onResume() {
        GpsLocationReceiver receiver=new GpsLocationReceiver();
        this.registerReceiver(receiver,new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        super.onResume();
    }*/

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_login);
            init();


          /*  if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))){
                Intent intent=new Intent(this,LocationCheckActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }else{

            }*/
            requestStoragePermission();


            bundle.putString("imei",tm.getDeviceId());
            bundle.putString("Carrier",tm.getNetworkOperatorName());
            bundle.putString("App Lauch time",new Date().toString());
            helper.facebookLog(getApplicationContext(),"Device Info",bundle);





            ApiClient.getInstance().appVersion()
                    .enqueue(new Callback<AppVersion>() {
                        @Override
                        public void onResponse(Call<AppVersion> call, Response<AppVersion> response) {
                            if(response.code()==200 && response.isSuccessful()){
                                Log.e("App Version",response.body().getAppVersion().toString());
                                Double version= Double.valueOf(BuildConfig.VERSION_NAME);
                                Log.e("App check-->", String.valueOf(response.body().getAppVersion()< version));
                                if(response.body().getAppVersion()> version){
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(response.body().getAppUrl())));
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                }
                            }else if(response.code()==404 || response.code()==500){
                                Toast.makeText(getApplicationContext(),"Service not available",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<AppVersion> call, Throwable t) {
                            t.fillInStackTrace();
                        }
                    });
        }catch (Exception e){
            if (pd.isShowing()) {
                pd.dismiss();
            }
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private void init(){
        am=AccountManager.get(this);
        accounts = am.getAccountsByType("com.google");
        bundle = new Bundle();
        edtTextMobileNo=findViewById(R.id.editTextMobileno);
        edtTextPassword=findViewById(R.id.editTextPassword);
        tvSignUp=findViewById(R.id.textViewSignup);
        btnSign=findViewById(R.id.buttonSignin);
        helper=Helper.getInstance();
        list=new ArrayList<>();
        bean=SessionBean.getInstance();
        pd=helper.showDialog(this,"Loading","Please wait...");
        tm=(TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        helper.logcat(getApplicationContext());
        locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);

//        helper.generateKeyHash(getApplicationContext());

    }


    @Override
    public void onClick(final View view) {

        try {
            if(view.getId()==R.id.buttonSignin){
                if(!TextUtils.isEmpty(edtTextMobileNo.getText().toString().trim()) && (!TextUtils.isEmpty(edtTextPassword.getText().toString().trim()))){
                    pd.show();
                    helper.putSession(this,"mobileNo",edtTextMobileNo.getText().toString());
                    helper.putSession(this,"password",edtTextPassword.getText().toString());

                    btnSign.setEnabled(false);
                    ApiClient.getInstance().getCustomerDetails(edtTextMobileNo.getText().toString())
                            .enqueue(new Callback<CustomerDetail>() {
                                @Override
                                public void onResponse(Call<CustomerDetail> call, Response<CustomerDetail> response) {
                                    Log.e(TAG, String.valueOf(response.code()));
                                    if(response.isSuccessful() && response.code()==200){
                                        btnSign.setEnabled(true);
                                        helper.putSession(getApplicationContext(),"user_id", String.valueOf(response.body().getUserId()));

                                        if(response.body().getUserId()==0){
                                            helper.showMesage(view.getRootView(),"Invalid user and password...");
                                            //Toast.makeText(LoginActivity.this, "Invalid user...", Toast.LENGTH_SHORT).show();
                                        }else if(response.body().getUserType().intValue()==1001){
                                            helper.putSession(getApplicationContext(),"user_type",response.body().getUserType().toString());
//                                            Toast.makeText(LoginActivity.this, "This is distributor", Toast.LENGTH_SHORT).show();
                                            intent=new Intent(LoginActivity.this,HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }
                                        else if(response.body().getUserId().intValue()>0){
                                            if(response.body().getUserType().intValue()!=1001) {
                                                helper.putSession(getApplicationContext(),"user_type","");
                                                intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }
                                        }
                                    }else if(response.code()==404 || response.code()==500){
                                        helper.showMesage(view.getRootView(),"Service not available");
                                    }
                                    if (pd.isShowing()) {
                                        btnSign.setEnabled(true);
                                        pd.dismiss();

                                    }
                                }

                                @Override
                                public void onFailure(Call<CustomerDetail> call, Throwable t) {
                                    if (pd.isShowing()) {
                                        btnSign.setEnabled(true);
                                        pd.dismiss();

                                    }
                                    t.printStackTrace();
                                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                   /* ApiClient.getInstance().loginUser(edtTextMobileNo.getText().toString())
                            .enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                    Log.e(TAG, String.valueOf(response.code()));
                                    if(response.isSuccessful() && response.code()==200){
                                        btnSign.setEnabled(true);
                                        helper.putSession(getApplicationContext(),"user_id",response.body().toString());
                                        if(response.body()==-1){
                                            helper.showMesage(view.getRootView(),"Invalid user and password...");
                                            //Toast.makeText(LoginActivity.this, "Invalid user...", Toast.LENGTH_SHORT).show();
                                        }else{
                                            intent=new Intent(LoginActivity.this,HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }
                                    }else if(response.code()==404 || response.code()==500){
                                        helper.showMesage(view.getRootView(),"Service not available");
                                    }
                                    if (pd.isShowing()) {
                                        btnSign.setEnabled(true);
                                        pd.dismiss();

                                    }
                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {
                                    if (pd.isShowing()) {
                                        pd.dismiss();
                                    }
                                    helper.showMesage(view.getRootView(),t.getMessage());
//                                    btnSign.setEnabled(true);
                                }
                            });*/

                }else{
                    edtTextMobileNo.setError("Please provide Mobile Number");
                    edtTextPassword.setError("Please provide Password");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   edtTextMobileNo.setError(null);
                                   edtTextPassword.setError(null);
                               }
                           });
                        }
                    },2000);
                }
            }else if(view.getId()==R.id.textViewSignup){
                intent=new Intent(this,RegisterActivity.class);
                startActivity(intent);
            }
        }catch (Exception e){
            if (pd.isShowing()) {
                pd.dismiss();
            }
            helper.showMesage(getWindow().getDecorView(),e.getMessage());
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void requestStoragePermission() {
       try{
           Dexter.withActivity(this)
                   .withPermissions(
                           Manifest.permission.INTERNET,
                           Manifest.permission.ACCESS_FINE_LOCATION,
                           Manifest.permission.ACCESS_COARSE_LOCATION,
                           Manifest.permission.READ_PHONE_STATE,
                           Manifest.permission.GET_ACCOUNTS,
                           Manifest.permission.WRITE_EXTERNAL_STORAGE,
                           Manifest.permission.READ_EXTERNAL_STORAGE)
                   .withListener(new MultiplePermissionsListener() {
                       @Override
                       public void onPermissionsChecked(MultiplePermissionsReport report) {
                           // check if all permissions are granted

                           if (report.areAllPermissionsGranted()) {

                           }

                           // check for permanent denial of any permission
                           if (report.isAnyPermissionPermanentlyDenied()) {
                               // show alert dialog navigating to Settings
                               Toast.makeText(LoginActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                           }

                           for(int i=0;i<report.getGrantedPermissionResponses().size();i++){
                               if(report.getGrantedPermissionResponses().get(i).getPermissionName().equalsIgnoreCase("android.permission.ACCESS_FINE_LOCATION")){
                                   tvSignUp.setOnClickListener(LoginActivity.this);
                                   btnSign.setOnClickListener(LoginActivity.this);
                                  // startActivity(new Intent(getApplicationContext(),LocationCheckActivity.class));
//                                   startService(new Intent(getApplicationContext(), LocService.class));
                                   Intent intent=new Intent(getApplicationContext(),LocService.class);
                                   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                   startService(intent);
                               }
                           }
                       }

                       @Override
                       public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                           token.continuePermissionRequest();


                       }
                   }).
                   withErrorListener(new PermissionRequestErrorListener() {
                       @Override
                       public void onError(DexterError error) {
                           Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                       }
                   })
                   .onSameThread()
                   .check();
       }catch (Exception e){
           e.printStackTrace();
       }
    }

}
