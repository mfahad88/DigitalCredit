package com.example.muhammadfahad.digitalcredit.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muhammadfahad.digitalcredit.Model.CustomerDetail;

import com.example.muhammadfahad.digitalcredit.Model.SessionBean;
import com.example.muhammadfahad.digitalcredit.R;
import com.example.muhammadfahad.digitalcredit.Utils.Helper;
import com.example.muhammadfahad.digitalcredit.client.ApiClient;
import com.example.muhammadfahad.digitalcredit.service.MyService;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn;
    private EditText edtName;
    private EditText edtCnic;
    private EditText edtMobile;
    private List<SessionBean> list;
    private Intent intent;
    private Call<String> api,api_userId;
    private CustomerDetail detail;
    private boolean isGranted=false;
    private SessionBean sessionBean;
    private Helper helper;
    private ProgressDialog pd;
    private View viewRoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        requestStoragePermission();
            btn.setOnClickListener(RegisterActivity.this);


    }

    private void init(){
        btn=findViewById(R.id.button);
        edtName=findViewById(R.id.editTextFullname);
        edtCnic=findViewById(R.id.editTextCnic);
        edtMobile=findViewById(R.id.editTextMobileno);
        detail=new CustomerDetail();
        list=new ArrayList<>();
        sessionBean=SessionBean.getInstance();
        helper=Helper.getInstance();
        pd=helper.showDialog(this,"Loading","Please wait...");
        viewRoot=View.inflate(this,R.layout.activity_login,null);


    }

    @Override
    public void onClick(View view) {
        try {
            if(view.getId()==R.id.button){
                if(TextUtils.isEmpty(edtName.getText().toString().trim())){
                    edtName.setError("Please provide Name");
                }if(TextUtils.isEmpty(edtCnic.getText().toString().trim())){
                    edtCnic.setError("Please provide Cnic");
                }if(TextUtils.isEmpty(edtMobile.getText().toString().trim())){
                    edtMobile.setError("Please provide Mobile Number");
                }if(!helper.isValidMobileNo(edtMobile.getText().toString())){
                    edtMobile.setError("Please enter valid mobile number");
                }
                if(!TextUtils.isEmpty(edtName.getText().toString().trim()) && (!TextUtils.isEmpty(edtCnic.getText().toString().trim()))
                        && (!TextUtils.isEmpty(edtMobile.getText().toString().trim())) && helper.isValidMobileNo(edtMobile.getText().toString())) {
                    pd.show();
                    detail.setUserName(edtName.getText().toString());
                    detail.setUserMobileNo(edtMobile.getText().toString());
                    detail.setUserCnic(edtCnic.getText().toString());
                    detail.setUserChannelId("Test");
                    detail.setUserStatus("I");

                    intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    api=ApiClient.getInstance().insertRecord(detail);
                    api.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.code()==200 && response.isSuccessful()){
                               try{
                                   if(response.body().equalsIgnoreCase("409")){
                                       if(pd.isShowing()){
                                           pd.dismiss();
                                       }
                                       Toast.makeText(RegisterActivity.this, "User already exists...", Toast.LENGTH_SHORT).show();
                                   }else {
                                       helper.putSession(RegisterActivity.this,"user_id",response.body());
                                       helper.writeTxtFile(RegisterActivity.this,response.body());
                                       Log.e("MobileNo---------->",edtMobile.getText().toString());
                                       Intent i=new Intent(getApplicationContext(), MyService.class);
                                       i.putExtra("mobileNo",edtMobile.getText().toString());
//                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                       getApplicationContext().startService(i);
                                       startActivity(intent);
                                       if(pd.isShowing()){
                                           pd.dismiss();
                                       }
                                   }

                               }catch (Exception e){
                                   e.printStackTrace();
                               }
                            }else{
                                try{
                                    if(pd.isShowing()){
                                        pd.dismiss();
                                    }
                                    Log.e("Error",response.body().trim());
                                    // Toast.makeText(RegisterActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            if(pd.isShowing()){
                                pd.dismiss();
                            }
                            Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void requestStoragePermission() {
        try{
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.GET_ACCOUNTS,
                            Manifest.permission.READ_CALENDAR,
                            Manifest.permission.BATTERY_STATS,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.ACCESS_WIFI_STATE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                helper.showMesage(viewRoot,"Permission Granted...");
                            }

                          /*  // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // show alert dialog navigating to Settings
                                Toast.makeText(RegisterActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                            }*/
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
