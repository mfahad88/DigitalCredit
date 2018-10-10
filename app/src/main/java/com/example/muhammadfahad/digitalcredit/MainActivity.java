package com.example.muhammadfahad.digitalcredit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muhammadfahad.digitalcredit.Interface.ApiInterface;
import com.example.muhammadfahad.digitalcredit.Model.CustomerDetail;
import com.example.muhammadfahad.digitalcredit.Model.InfoBean;
import com.example.muhammadfahad.digitalcredit.Model.SessionBean;
import com.example.muhammadfahad.digitalcredit.Utils.Helper;
import com.example.muhammadfahad.digitalcredit.client.ApiClient;
import com.example.muhammadfahad.digitalcredit.fragment.DashboardFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn;
    private EditText edtName;
    private EditText edtCnic;
    private EditText edtMobile;
    private List<SessionBean> list;
    private Intent intent;
    private Call<String> api,api_userId;
    private CustomerDetail detail;
    private ProgressDialog dialog;
    private InfoBean bean;
    private SessionBean sessionBean;
    private Helper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        btn.setOnClickListener(this);
       /* edtCnic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editable.length()==6
            }
        });*/

    }

    private void init(){
        btn=findViewById(R.id.button);
        bean=InfoBean.getInstance();
        edtName=findViewById(R.id.editTextFullname);
        edtCnic=findViewById(R.id.editTextCnic);
        edtMobile=findViewById(R.id.editTextMobileno);
        dialog=new ProgressDialog(MainActivity.this);
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
                putSession("name",edtName.getText().toString());
                putSession("cnic",edtCnic.getText().toString());
                putSession("mobileNo",edtMobile.getText().toString());
                bean.setName(edtName.getText().toString());
                bean.setCnic(edtCnic.getText().toString());
                bean.setMobile(edtMobile.getText().toString());
                intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("name",edtName.getText().toString());
                intent.putExtra("cnic",edtCnic.getText().toString().trim());
                intent.putExtra("mobile",edtMobile.getText().toString().trim());
                api=ApiClient.getInstance().insertRecord(detail);

                api.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.code()==200){

                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        if(dialog.isShowing()){
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
    }

    public void putSession(String key,String value){
        sessionBean.setKey(key);
        sessionBean.setValue(value);
        list.add(sessionBean);
        helper.sessionStore(this,list);
    }
}
