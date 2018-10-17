package com.example.muhammadfahad.digitalcredit.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadfahad.digitalcredit.Model.CustomerDetail;
import com.example.muhammadfahad.digitalcredit.Model.SessionBean;
import com.example.muhammadfahad.digitalcredit.R;
import com.example.muhammadfahad.digitalcredit.Utils.Helper;
import com.example.muhammadfahad.digitalcredit.client.ApiClient;
import com.example.muhammadfahad.digitalcredit.service.LocationService;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtTextMobileNo,edtTextPassword;
    private TextView tvSignUp;
    private Button btnSign;
    private Intent intent,i;
    private Helper helper;
    private List<SessionBean> list;
    private SessionBean bean;
    private ProgressDialog dialog;
    private Integer userId=0;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishAffinity();
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        requestStoragePermission();



        if(helper.getSession(getApplicationContext())!=null){
            helper.clearSession(getApplicationContext());
        }

        tvSignUp.setOnClickListener(LoginActivity.this);

    }

    private void init(){
        edtTextMobileNo=findViewById(R.id.editTextMobileno);
        edtTextPassword=findViewById(R.id.editTextPassword);
        tvSignUp=findViewById(R.id.textViewSignup);
        btnSign=findViewById(R.id.buttonSignin);
        helper=Helper.getInstance();
        list=new ArrayList<>();
        bean=SessionBean.getInstance();
        dialog=new ProgressDialog(this);
    }

    @Override
    public void onClick(View view) {

        try {
            if(view.getId()==R.id.buttonSignin){
                if(!TextUtils.isEmpty(edtTextMobileNo.getText().toString().trim()) && (!TextUtils.isEmpty(edtTextPassword.getText().toString().trim()))){
                    helper.putSession(this,"mobileNo",edtTextMobileNo.getText().toString());
                    helper.putSession(this,"password",edtTextPassword.getText().toString());
                    intent=new Intent(this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ApiClient.getInstance().loginUser(edtTextMobileNo.getText().toString())
                            .enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                    if(response.isSuccessful() && response.code()==200){
                                        helper.putSession(getApplicationContext(),"user_id",response.body().toString());
                                        if(response.body()==-1){
                                            Toast.makeText(LoginActivity.this, "Invalid user...", Toast.LENGTH_SHORT).show();
                                        }else{
                                            startActivity(intent);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {

                                }
                            });

                }else{
                    edtTextMobileNo.setError("Please provide Mobile Number");
                    edtTextPassword.setError("Please provide Password");
                }
            }else if(view.getId()==R.id.textViewSignup){
                intent=new Intent(this,RegisterActivity.class);
                startActivity(intent);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void requestStoragePermission() {
       try{
           Dexter.withActivity(this)
                   .withPermissions(
                           Manifest.permission.INTERNET,
                           Manifest.permission.ACCESS_FINE_LOCATION,
                           Manifest.permission.ACCESS_COARSE_LOCATION)
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
                                   btnSign.setOnClickListener(LoginActivity.this);
                                   startService(new Intent(getApplicationContext(), LocationService.class));
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
