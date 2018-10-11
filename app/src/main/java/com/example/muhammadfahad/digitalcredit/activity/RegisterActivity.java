package com.example.muhammadfahad.digitalcredit.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn;
    private EditText edtName;
    private EditText edtCnic;
    private EditText edtMobile;
    private List<SessionBean> list;
    private Intent intent;
    private Call<String> api,api_userId;
    private CustomerDetail detail;
    private ProgressDialog dialog;

    private SessionBean sessionBean;
    private Helper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        btn.setOnClickListener(this);


    }

    private void init(){
        btn=findViewById(R.id.button);

        edtName=findViewById(R.id.editTextFullname);
        edtCnic=findViewById(R.id.editTextCnic);
        edtMobile=findViewById(R.id.editTextMobileno);
        dialog=new ProgressDialog(RegisterActivity.this);
        dialog.setTitle("Loading...");
        dialog.setMessage("Please Wait...");
        detail=new CustomerDetail();
        list=new ArrayList<>();
        sessionBean=SessionBean.getInstance();
        helper=Helper.getInstance();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.button){
            if(TextUtils.isEmpty(edtName.getText().toString().trim())){
                edtName.setError("Please provide Name");
            }if(TextUtils.isEmpty(edtCnic.getText().toString().trim())){
                edtCnic.setError("Please provide Cnic");
            }if(TextUtils.isEmpty(edtMobile.getText().toString().trim())){
                edtMobile.setError("Please provide Mobile Number");
            }
            if(!TextUtils.isEmpty(edtName.getText().toString().trim()) && (!TextUtils.isEmpty(edtCnic.getText().toString().trim()))
                    && (!TextUtils.isEmpty(edtMobile.getText().toString().trim()))) {
                dialog.show();

                detail.setUserMobileNo(edtMobile.getText().toString());
                detail.setUserCnic(edtCnic.getText().toString());
                detail.setUserChannelId("Test");
                detail.setUserStatus("i");

                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                api=ApiClient.getInstance().insertRecord(detail);
                api.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.code()==200){
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        if(dialog.isShowing()){
                            dialog.dismiss();
                            Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
    }
}
