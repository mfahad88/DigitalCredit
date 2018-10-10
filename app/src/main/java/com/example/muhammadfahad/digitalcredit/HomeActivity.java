package com.example.muhammadfahad.digitalcredit;

import android.app.ProgressDialog;
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

import com.example.muhammadfahad.digitalcredit.Model.InfoBean;
import com.example.muhammadfahad.digitalcredit.Utils.Helper;
import com.example.muhammadfahad.digitalcredit.fragment.AvailLoanFragment;
import com.example.muhammadfahad.digitalcredit.fragment.DashboardFragment;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FrameLayout layout;
    private FragmentTransaction ft;
    private Fragment fragment;
    private Toolbar toolbar;
    private TextView tvName;
    private TextView tvCnic;
    private Bundle extras;
    private View header;
    ProgressDialog dialog;
    InfoBean bean;
    private int counter=0;
    private Helper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

        if(dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        helper.clearSession(this);
        super.onDestroy();

    }

    public void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");
        dialog=new ProgressDialog(getApplicationContext());
        bean=InfoBean.getInstance();
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

        if(extras!=null){
            tvName.setText(extras.getString("name"));
            tvCnic.setText(extras.getString("cnic"));
        }
        fragment=new DashboardFragment();
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
        }
        ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.view_container,fragment);
        ft.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
