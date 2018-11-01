package com.example.administrator.digitalcredit.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.digitalcredit.Model.CustomerDetail;
import com.example.administrator.digitalcredit.R;
import com.example.administrator.digitalcredit.Utils.Helper;
import com.example.administrator.digitalcredit.client.ApiClient;
import com.example.administrator.digitalcredit.fragment.AvailLoanFragment;
import com.example.administrator.digitalcredit.fragment.DashboardFragment;
import com.example.administrator.digitalcredit.fragment.HistoryFragment;
import com.example.administrator.digitalcredit.fragment.RepaymentFragment;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FrameLayout layout;
    private FragmentTransaction ft;
    private Fragment fragment;
    private Toolbar toolbar;
    private TextView tvName;
    private TextView tvCnic;
    private Bundle extras,bundle;
    private View header;
    ProgressDialog pd;

    private int counter=0;
    private Helper helper;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

        if(pd.isShowing()) {
            pd.dismiss();
        }
    }


    public void init(){
        try{
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Dashboard");

            setSupportActionBar(toolbar);
            layout=findViewById(R.id.view_container);
            helper=Helper.getInstance();
            pd=helper.showDialog(this,"Loading","Please wait...");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            extras=getIntent().getExtras();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            header=navigationView.getHeaderView(0);
            navigationView.setNavigationItemSelectedListener(this);

            tvName=header.findViewById(R.id.textViewName);
            tvCnic=header.findViewById(R.id.textViewCnic);
            ApiClient.getInstance().getCustomerDetails(helper.getSession(this).get("mobileNo").toString())
                    .enqueue(new Callback<CustomerDetail>() {
                        @Override
                        public void onResponse(Call<CustomerDetail> call, Response<CustomerDetail> response) {
                            if(pd.isShowing()) {
                                pd.dismiss();
                            }
                            if(response.code()==200 && response.isSuccessful()){

                                tvName.setText(response.body().getUserName());
                                tvCnic.setText(response.body().getUserCnic());
                            }else if(response.code()==404 || response.code()==500){
                                helper.showMesage(getWindow().getDecorView(),"Service not available");
                            }

                        }

                        @Override
                        public void onFailure(Call<CustomerDetail> call, Throwable t) {
                            if(pd.isShowing()) {
                                pd.dismiss();
                            }
                            helper.showMesage(getWindow().getDecorView(),t.getMessage());
//                            Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


            fragment=new DashboardFragment();
            //fragment.setArguments(bundle);
            ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.view_container,fragment);
            ft.commit();
            if(extras!=null) {
                if (extras.getString("availLoan").equalsIgnoreCase("Yes")) {
                    Fragment fragment = new RepaymentFragment();
                    toolbar.setTitle("Repayment");
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.view_container, fragment).commit();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        counter++;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
                if(counter>=2) {

                    if(helper.clearSession(this)) {
                        Bundle bundle=new Bundle();
                        bundle.putString("App closed time",new Date().toString());
                        helper.facebookLog(getApplicationContext(),"DeviceInfo",bundle);
                        finishAffinity();
                        System.exit(0);
                    }
                }
//            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id==R.id.home_loan){
            toolbar.setTitle("Dashboard");
            fragment=new DashboardFragment();
        }
        else if(id==R.id.avail_loan) {
            toolbar.setTitle("Avail Loan");
            fragment = new AvailLoanFragment();
        }else if(id==R.id.avail_history){
            toolbar.setTitle("History");
            fragment=new HistoryFragment();
        }else if(id==R.id.repayments){
            toolbar.setTitle("Repayments");
            fragment=new RepaymentFragment();
        }else if(id==R.id.logout){

            try {
                Intent intent=new Intent(this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("clear","1");
                startActivity(intent);
            }catch (Exception e){
                if(pd.isShowing()) {
                    pd.dismiss();
                }
                helper.showMesage(getWindow().getDecorView(),e.getMessage());
//                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
        ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.view_container,fragment);
        ft.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
