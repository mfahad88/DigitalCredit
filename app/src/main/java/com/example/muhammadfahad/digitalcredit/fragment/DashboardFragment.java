package com.example.muhammadfahad.digitalcredit.fragment;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.icu.util.MeasureUnit;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
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
import com.example.muhammadfahad.digitalcredit.Utils.Helper;
import com.example.muhammadfahad.digitalcredit.client.ApiClient;


import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    public static DashboardFragment fragment;
    private TextView tvBase,tvBehavior,tvLimit,tvAggregation;
    private View view;
    public Call<CustomerDetail> detail = null;
    private Call<List<LoanDetail>> loan;
    private TableLayout tableLayout;

    private Helper helper;

  /*  public static DashboardFragment getInstance() {
        if(fragment==null){
            fragment=new DashboardFragment();
        }
        return fragment;
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_dashboard, container, false);
        init();

        detail=ApiClient.getInstance().getCustomerDetails(helper.getSession(view.getContext()).get("mobileNo").toString());
        detail.enqueue(new Callback<CustomerDetail>() {
            @Override
            public void onResponse(Call<CustomerDetail> call, Response<CustomerDetail> response) {
                if(response.code()==200){
                    helper.putSession(view.getContext(),"user_id",response.body().getUserId().toString());
                    helper.putSession(view.getContext(),"user_mobile_no",response.body().getUserMobileNo().toString());
                    helper.putSession(view.getContext(),"user_cnic",response.body().getUserCnic().toString());
                    helper.putSession(view.getContext(),"user_channel_id",response.body().getUserChannelId().toString());
                    helper.putSession(view.getContext(),"income",response.body().getIncome().toString());
                    helper.putSession(view.getContext(),"user_status",response.body().getUserStatus().toString());
                    helper.putSession(view.getContext(),"available_Balance",response.body().getAvailableBalance().toString());
                    helper.putSession(view.getContext(),"base_scrore",response.body().getBaseScrore().toString());
                    helper.putSession(view.getContext(),"behavior_score",response.body().getBehaviorScore().toString());
                    helper.putSession(view.getContext(),"base_score_flag",response.body().getBaseScoreFlag().toString());
                    helper.putSession(view.getContext(),"behavior_score_flag",response.body().getBehaviorScoreFlag().toString());
                    helper.putSession(view.getContext(),"assigned_amount_Limit",response.body().getAssignedAmountLimit().toString());
                    helper.putSession(view.getContext(),"consumed_Limit",response.body().getConsumedLimit().toString());
                    helper.putSession(view.getContext(),"available_Amount_Limit",response.body().getAvailableAmountLimit().toString());
                    helper.putSession(view.getContext(),"dbr_value",response.body().getDbrValue().toString());
                    helper.putSession(view.getContext(),"dbr_value_flag",response.body().getDbrValueFlag().toString());

                    tvBase.setText(String.valueOf(response.body().getBaseScrore()));
                    tvBehavior.setText(String.valueOf(response.body().getBehaviorScore()));
                    tvAggregation.setText(String.valueOf(response.body().getBaseScrore()+response.body().getBehaviorScore()));
                    if(response.body().getAvailableAmountLimit()>0) {
                        tvLimit.setText("Rs. " + helper.CashFormatter(response.body().getAvailableAmountLimit().toString()));
                    }else{
                        tvLimit.setText("Rs. " + response.body().getAvailableAmountLimit().toString());
                    }
                    loan=ApiClient.getInstance().getLoan(helper.getSession(view.getContext()).get("user_id").toString(),"U");
                    loan.enqueue(new Callback<List<LoanDetail>>() {
                        @Override
                        public void onResponse(Call<List<LoanDetail>> call, Response<List<LoanDetail>> response) {
                            if(response.code()==200) {

                                for (LoanDetail loanDetail : response.body()) {
                                    TableRow row = new TableRow(view.getContext());
                                    row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                                    TextView tvId = new TextView(view.getContext());
                                    tvId.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tvId.setTextSize(15f);
                                    tvId.setGravity(Gravity.CENTER_HORIZONTAL);
                                    tvId.setText(loanDetail.getId().toString());
                                    row.addView(tvId);

                                    TextView tvCreatedDate = new TextView(view.getContext());
                                    tvCreatedDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tvCreatedDate.setTextSize(15f);
                                    tvCreatedDate.setGravity(Gravity.CENTER_HORIZONTAL);
                                    tvCreatedDate.setText(loanDetail.getLoanCreatedDate());
                                    row.addView(tvCreatedDate);

                                    TextView tvDueDate = new TextView(view.getContext());
                                    tvDueDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tvDueDate.setTextSize(15f);
                                    tvDueDate.setGravity(Gravity.CENTER_HORIZONTAL);
                                    tvDueDate.setText(loanDetail.getLoanDueDate());
                                    row.addView(tvDueDate);

                                    TextView tvAmt = new TextView(view.getContext());
                                    tvAmt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tvAmt.setTextSize(15f);
                                    tvAmt.setGravity(Gravity.CENTER_HORIZONTAL);
                                    tvAmt.setText(loanDetail.getAmt().toString());
                                    row.addView(tvAmt);

                                    TextView tvStatus = new TextView(view.getContext());
                                    tvStatus.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tvStatus.setTextSize(15f);
                                    tvStatus.setGravity(Gravity.CENTER_HORIZONTAL);
                                    tvStatus.setText(loanDetail.getLoanStatus());
                                    row.addView(tvStatus);


                                    tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<LoanDetail>> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<CustomerDetail> call, Throwable t) {
                Toast.makeText(view.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


      /*  loan=ApiClient.getInstance().getLoan(helper.getSession(view.getContext()).get("user_id").toString(),"U");

       loan.enqueue(new Callback<List<LoanDetail>>() {
           @Override
           public void onResponse(Call<List<LoanDetail>> call, Response<List<LoanDetail>> response) {
               int id=100;
                if(response.code()==200){

                        for(LoanDetail loanDetail:response.body()) {
                            TableRow row = new TableRow(view.getContext());
                            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                            TextView tvId=new TextView(view.getContext());
                            tvId.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvId.setTextSize(15f);
                            tvId.setGravity(Gravity.CENTER_HORIZONTAL);
                            tvId.setText(loanDetail.getId().toString());
                            row.addView(tvId);

                            TextView tvCreatedDate=new TextView(view.getContext());
                            tvCreatedDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvCreatedDate.setTextSize(15f);
                            tvCreatedDate.setGravity(Gravity.CENTER_HORIZONTAL);
                            tvCreatedDate.setText(loanDetail.getLoanCreatedDate());
                            row.addView(tvCreatedDate);

                            TextView tvDueDate=new TextView(view.getContext());
                            tvDueDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvDueDate.setTextSize(15f);
                            tvDueDate.setGravity(Gravity.CENTER_HORIZONTAL);
                            tvDueDate.setText(loanDetail.getLoanDueDate());
                            row.addView(tvDueDate);

                            TextView tvAmt=new TextView(view.getContext());
                            tvAmt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvAmt.setTextSize(15f);
                            tvAmt.setGravity(Gravity.CENTER_HORIZONTAL);
                            tvAmt.setText(loanDetail.getAmt().toString());
                            row.addView(tvAmt);

                            TextView tvStatus=new TextView(view.getContext());
                            tvStatus.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvStatus.setTextSize(15f);
                            tvStatus.setGravity(Gravity.CENTER_HORIZONTAL);
                            tvStatus.setText(loanDetail.getLoanStatus());
                            row.addView(tvStatus);

                          *//*  tv.setText(loanDetail.getId()+"\t"+loanDetail.getCustomerId()+"\t"+loanDetail.getAmt()+"\t"+
                                    loanDetail.getLoanCreatedDate()+"\t"+loanDetail.getLoanDueDate()+"\t"+
                                    loanDetail.getAmtPaid()+"\t"+loanDetail.getLoanStatus() +"\t"+loanDetail.getLastPaidDate());
                            row.addView(tv);*//*

                            tableLayout.addView(row,new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                        }

                }
           }

           @Override
           public void onFailure(Call<List<LoanDetail>> call, Throwable t) {

           }
       });*/
        return view;
    }

    public void init(){

        tvBase=view.findViewById(R.id.textViewBase);
        tvBehavior=view.findViewById(R.id.textViewBehavior);
        tvLimit=view.findViewById(R.id.textViewAvailablelimit);
        tvAggregation=view.findViewById(R.id.textViewAggregation);
        tableLayout=view.findViewById(R.id.tableLayout);
        helper=Helper.getInstance();

    }

}
