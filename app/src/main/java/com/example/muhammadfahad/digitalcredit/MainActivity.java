package com.example.muhammadfahad.digitalcredit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muhammadfahad.digitalcredit.Interface.ApiInterface;
import com.example.muhammadfahad.digitalcredit.Model.CustomerDetail;
import com.example.muhammadfahad.digitalcredit.Model.InfoBean;
import com.example.muhammadfahad.digitalcredit.client.ApiClient;
import com.example.muhammadfahad.digitalcredit.fragment.DashboardFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn;
    private EditText edtName;
    private EditText edtCnic;
    private EditText edtMobile;
    private SharedPreferences.Editor preferences;
    SharedPreferences prefs;
    public static final String MY_PREFS_NAME = "MyPrefs";
    private Intent intent;
    private Retrofit retrofit;
    private ApiInterface service;
    private CustomerDetail detail;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        btn.setOnClickListener(this);


    }

    private void init(){
        btn=findViewById(R.id.button);
        edtName=findViewById(R.id.editTextFullname);
        edtCnic=findViewById(R.id.editTextCnic);
        edtMobile=findViewById(R.id.editTextMobileno);
        dialog=new ProgressDialog(MainActivity.this);
        dialog.setTitle("Loading...");
        dialog.setMessage("Please Wait...");
        preferences=getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        edtName.setText(prefs.getString("name",""));
        edtCnic.setText(prefs.getString("cnic",""));
        edtMobile.setText(prefs.getString("mobile",""));
        retrofit= ApiClient.getInstance();
        service=retrofit.create(ApiInterface.class);
        detail=new CustomerDetail();
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
                edtMobile.getText().toString();
                detail.setUserMobileNo(edtMobile.getText().toString());
                detail.setUserCnic(edtCnic.getText().toString());
                detail.setUserChannelId("Test");
                detail.setUserStatus("i");
                preferences.putString("name",edtName.getText().toString());
                preferences.putString("cnic",edtCnic.getText().toString());
                preferences.putString("mobile",edtMobile.getText().toString());


                intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("name",edtName.getText().toString());
                intent.putExtra("cnic",edtCnic.getText().toString().trim());
                intent.putExtra("mobile",edtMobile.getText().toString().trim());
                service.getUserId(detail).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.code()==200){
                            preferences.putString("userId",response.body());
                            preferences.apply();

                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });

            }
        }
    }
}
