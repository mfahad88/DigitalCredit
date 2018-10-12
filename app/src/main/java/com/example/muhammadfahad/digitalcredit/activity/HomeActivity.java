package com.example.muhammadfahad.digitalcredit.activity;

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

import com.example.muhammadfahad.digitalcredit.Model.CustomerDetail;
import com.example.muhammadfahad.digitalcredit.R;
import com.example.muhammadfahad.digitalcredit.Utils.Helper;
import com.example.muhammadfahad.digitalcredit.client.ApiClient;
import com.example.muhammadfahad.digitalcredit.fragment.AvailLoanFragment;
import com.example.muhammadfahad.digitalcredit.fragment.DashboardFragment;
import com.example.muhammadfahad.digitalcredit.fragment.HistoryFragment;
import com.example.muhammadfahad.digitalcredit.fragment.RepaymentFragment;

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
    ProgressDialog dialog;

    private int counter=0;
    private Helper helper;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

        if(dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    public void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");
        dialog=new ProgressDialog(getApplicationContext());
        setSupportActionBar(toolbar);
        layout=findViewById(R.id.view_container);
        helper=Helper.getInstance();
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
                        if(response.code()==200 && response.isSuccessful()){
                            tvName.setText(response.body().getUserName());
                            tvCnic.setText(response.body().getUserCnic());
                        }
                    }

                    @Override
                    public void onFailure(Call<CustomerDetail> call, Throwable t) {

                    }
                });


        fragment=new DashboardFragment();
        //fragment.setArguments(bundle);
        ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.view_container,fragment);
        ft.commit();


    }

    @Override
    public void onBackPressed() {
        counter++;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
                if(counter>=2) {
                    if(helper.clearSession(this)) {
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

            Intent intent=new Intent(this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("clear","1");
            startActivity(intent);

        }
        ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.view_container,fragment);
        ft.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
