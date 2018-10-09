package com.example.muhammadfahad.digitalcredit.fragment;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.icu.util.MeasureUnit;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadfahad.digitalcredit.Interface.ApiInterface;
import com.example.muhammadfahad.digitalcredit.MainActivity;
import com.example.muhammadfahad.digitalcredit.Model.CustomerDetail;
import com.example.muhammadfahad.digitalcredit.Model.InfoBean;
import com.example.muhammadfahad.digitalcredit.Model.LoanDetail;
import com.example.muhammadfahad.digitalcredit.R;
import com.example.muhammadfahad.digitalcredit.client.ApiClient;


import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;
import static com.example.muhammadfahad.digitalcredit.MainActivity.MY_PREFS_NAME;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    public static DashboardFragment fragment;
    private TextView tvBase,tvBehavior,tvLimit,tvAggregation;
    private View view;
    private Retrofit retrofit;
    private ApiInterface service;
    private SharedPreferences prefs;
    public Call<CustomerDetail> detail = null;
    private Call<List<LoanDetail>> loan;
    private TableLayout tableLayout;
    private InfoBean bean;

    public static DashboardFragment getInstance() {
        if(fragment==null){
            fragment=new DashboardFragment();
        }
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_dashboard, container, false);
        init();
        prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Log.e("Preferences---->",prefs.getAll().toString());
        detail=service.getScore(prefs.getString("mobile",""));
        detail.enqueue(new Callback<CustomerDetail>() {
            @Override
            public void onResponse(Call<CustomerDetail> call, Response<CustomerDetail> response) {
                if(response.code()==200){
                    tvBase.setText(String.valueOf(response.body().getBaseScrore()));
                    tvBehavior.setText(String.valueOf(response.body().getBehaviorScore()));
                    tvAggregation.setText(String.valueOf(response.body().getBaseScrore()+response.body().getBehaviorScore()));
                    tvLimit.setText("Rs. "+String.valueOf(response.body().getAvailableAmountLimit()));
                }
            }

            @Override
            public void onFailure(Call<CustomerDetail> call, Throwable t) {
                Toast.makeText(view.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        loan=service.getLoan(prefs.getString("userId",""),"U");

       loan.enqueue(new Callback<List<LoanDetail>>() {
           @Override
           public void onResponse(Call<List<LoanDetail>> call, Response<List<LoanDetail>> response) {
                if(response.code()==200){

                    for(int i=0;i<response.body().size();i++) {
                        TableRow row=new TableRow(view.getContext());
                        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        TextView tv=new TextView(view.getContext());
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tv.setTextSize(15f);
                        tv.setText(response.body().get(i).getId()+"\t"+response.body().get(i).getCustomerId()+"\t"+response.body().get(i).getAmt()+"\t"+
                                response.body().get(i).getLoanCreatedDate()+"\t"+response.body().get(i).getLoanDueDate()+"\t"+
                                response.body().get(i).getAmtPaid()+"\t"+response.body().get(i).getLoanStatus() +"\t"+response.body().get(i).getLastPaidDate());
                        row.addView(tv);
                        tableLayout.addView(row,new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    }
                }
           }

           @Override
           public void onFailure(Call<List<LoanDetail>> call, Throwable t) {

           }
       });
        return view;
    }

    public void init(){

        tvBase=view.findViewById(R.id.textViewBase);
        tvBehavior=view.findViewById(R.id.textViewBehavior);
        tvLimit=view.findViewById(R.id.textViewAvailablelimit);
        tvAggregation=view.findViewById(R.id.textViewAggregation);
        tableLayout=view.findViewById(R.id.tableLayout);
        bean=InfoBean.getInstance();
        retrofit= ApiClient.getInstance();
        service=retrofit.create(ApiInterface.class);

    }

}
