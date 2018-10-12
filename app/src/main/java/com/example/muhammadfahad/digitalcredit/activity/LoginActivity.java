package com.example.muhammadfahad.digitalcredit.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
        if(dialog.isShowing()){
            dialog.dismiss();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        btnSign.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        if(helper.getSession(getApplicationContext())!=null){
            helper.clearSession(getApplicationContext());
        }
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


        if(view.getId()==R.id.buttonSignin){
            if(!TextUtils.isEmpty(edtTextMobileNo.getText().toString().trim()) && (!TextUtils.isEmpty(edtTextPassword.getText().toString().trim()))){
                helper.putSession(this,"mobileNo",edtTextMobileNo.getText().toString());
                helper.putSession(this,"password",edtTextPassword.getText().toString());
                intent=new Intent(this,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
    }


}
