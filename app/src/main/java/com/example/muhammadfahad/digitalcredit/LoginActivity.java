package com.example.muhammadfahad.digitalcredit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadfahad.digitalcredit.Model.SessionBean;
import com.example.muhammadfahad.digitalcredit.Utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtTextMobileNo,edtTextPassword;
    private TextView tvSignUp;
    private Button btnSign;
    private Intent intent;
    private Helper helper;
    private List<SessionBean> list;
    private SessionBean bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        btnSign.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
    }

    private void init(){
        edtTextMobileNo=findViewById(R.id.editTextMobileno);
        edtTextPassword=findViewById(R.id.editTextPassword);
        tvSignUp=findViewById(R.id.textViewSignup);
        btnSign=findViewById(R.id.buttonSignin);
        helper=Helper.getInstance();
        list=new ArrayList<>();
        bean=SessionBean.getInstance();
    }

    @Override
    public void onClick(View view) {


        if(view.getId()==R.id.buttonSignin){
            if(!TextUtils.isEmpty(edtTextMobileNo.getText().toString().trim()) && (!TextUtils.isEmpty(edtTextPassword.getText().toString().trim()))){
                helper.putSession(this,"mobileNo",edtTextMobileNo.getText().toString());
                helper.putSession(this,"password",edtTextPassword.getText().toString());
                intent=new Intent(this,HomeActivity.class);
                startActivity(intent);
            }else{
                edtTextMobileNo.setError("Please provide Mobile Number");
                edtTextPassword.setError("Please provide Password");
            }
        }else if(view.getId()==R.id.textViewSignup){
            intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }


}
