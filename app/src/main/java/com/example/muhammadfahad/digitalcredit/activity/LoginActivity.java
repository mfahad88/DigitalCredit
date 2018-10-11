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
    private Intent intent;
    private Helper helper;
    private List<SessionBean> list;
    private SessionBean bean;
    private ProgressDialog dialog;


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
                ApiClient.getInstance().getCustomerDetails(edtTextMobileNo.getText().toString())
                        .enqueue(new Callback<CustomerDetail>() {
                            @Override
                            public void onResponse(Call<CustomerDetail> call, Response<CustomerDetail> response) {
                                if(response.isSuccessful() && response.code()==200){
                                    helper.putSession(LoginActivity.this,"user_id",response.body().getUserId().toString());
                                    helper.putSession(LoginActivity.this,"user_mobile_no",response.body().getUserMobileNo().toString());
                                    helper.putSession(LoginActivity.this,"user_cnic",response.body().getUserCnic().toString());
                                    helper.putSession(LoginActivity.this,"user_channel_id",response.body().getUserChannelId().toString());
                                    helper.putSession(LoginActivity.this,"income",response.body().getIncome().toString());
                                    helper.putSession(LoginActivity.this,"user_status",response.body().getUserStatus().toString());
                                    helper.putSession(LoginActivity.this,"available_Balance",response.body().getAvailableBalance().toString());
                                    helper.putSession(LoginActivity.this,"base_scrore",response.body().getBaseScrore().toString());
                                    helper.putSession(LoginActivity.this,"behavior_score",response.body().getBehaviorScore().toString());
                                    helper.putSession(LoginActivity.this,"base_score_flag",response.body().getBaseScoreFlag().toString());
                                    helper.putSession(LoginActivity.this,"behavior_score_flag",response.body().getBehaviorScoreFlag().toString());
                                    helper.putSession(LoginActivity.this,"assigned_amount_Limit",response.body().getAssignedAmountLimit().toString());
                                    helper.putSession(LoginActivity.this,"consumed_Limit",response.body().getConsumedLimit().toString());
                                    helper.putSession(LoginActivity.this,"available_Amount_Limit",response.body().getAvailableAmountLimit().toString());
                                    helper.putSession(LoginActivity.this,"dbr_value",response.body().getDbrValue().toString());
                                    helper.putSession(LoginActivity.this,"dbr_value_flag",response.body().getDbrValueFlag().toString());
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onFailure(Call<CustomerDetail> call, Throwable t) {
                                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
