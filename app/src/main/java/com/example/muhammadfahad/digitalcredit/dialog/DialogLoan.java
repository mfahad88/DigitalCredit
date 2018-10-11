package com.example.muhammadfahad.digitalcredit.dialog;




import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadfahad.digitalcredit.Model.LoanDetail;
import com.example.muhammadfahad.digitalcredit.R;
import com.example.muhammadfahad.digitalcredit.Utils.Helper;
import com.example.muhammadfahad.digitalcredit.client.ApiClient;

import java.text.SimpleDateFormat;
import java.util.Date;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogLoan extends DialogFragment {
    private View viewRoot;
    private TextView tvLoanId;
    private TextView tvUserId;
    private TextView tvAmount;
    private TextView tvCreatedDate;
    private TextView tvDueDate;
    private TextView tvAmountPaid;
    private TextView tvStatus;
    private TextView tvPaidDate;
    private TextView tvRemainingAmount;
    private TextView tvLoanFees;
    private Helper helper;
    private LoanDetail bean;
    private Button btnPay;
    private int loanId;
    private Bundle bundle;
    private Date date;
    private SimpleDateFormat sdf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewRoot = inflater.inflate(R.layout.dialog_fragment, container, false);
        init();
        bundle=getArguments();
        loanId=bundle.getInt("loanId");
        ApiClient.getInstance().SingleLoan(helper.getSession(viewRoot.getContext()).get("user_id").toString(), String.valueOf(loanId))
                .enqueue(new Callback<LoanDetail>() {
                    @Override
                    public void onResponse(Call<LoanDetail> call, Response<LoanDetail> response) {
                        if(response.isSuccessful() && response.code()==200){
                            try {
                                bean=response.body();
                                tvLoanId.setText(bean.getId().toString());
//                                tvUserId.setText(bean.getCustomerId().toString());
                                tvAmount.setText(bean.getAmt().toString());
                                tvCreatedDate.setText(bean.getLoanCreatedDate());
                                tvDueDate.setText(bean.getLoanDueDate());
                                tvAmountPaid.setText(bean.getAmtPaid().toString());
                                tvStatus.setText(bean.getLoanStatus());
                                tvPaidDate.setText(bean.getLastPaidDate());
//                                tvRemainingAmount.setText(String.valueOf(bean.getRemainingAmt()));
                                tvLoanFees.setText(String.valueOf(bean.getLoanFees()));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoanDetail> call, Throwable t) {
                        Toast.makeText(viewRoot.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    bean.setLoanStatus("P");
                    bean.setAmtPaid(Integer.valueOf(bean.getAmt().toString()));
                    date=new Date();
                    sdf=new SimpleDateFormat("YYYY-MM-dd HH:MM:SS");
                    bean.setLastPaidDate(sdf.format(date));

                  ApiClient.getInstance().updateLoanStatus(bean).enqueue(new Callback<Integer>() {
                      @Override
                      public void onResponse(Call<Integer> call, Response<Integer> response) {
                          if(response.isSuccessful() && response.code()==200){
                              Toast.makeText(viewRoot.getContext(), "Loan Paid...", Toast.LENGTH_SHORT).show();
                          }
                      }

                      @Override
                      public void onFailure(Call<Integer> call, Throwable t) {
//                          Toast.makeText(viewRoot.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                      }
                  });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        return viewRoot;
    }

    private void init(){
        tvLoanId=viewRoot.findViewById(R.id.textViewLoanId);
//        tvUserId=viewRoot.findViewById(R.id.textViewUserId);
        tvAmount=viewRoot.findViewById(R.id.textViewAmt);
        tvCreatedDate=viewRoot.findViewById(R.id.textViewLoanCreatedDate);
        tvDueDate=viewRoot.findViewById(R.id.textViewLoanDueDate);
        tvAmountPaid=viewRoot.findViewById(R.id.textViewAmtPaid);
        tvStatus=viewRoot.findViewById(R.id.textViewLoanStatus);
        tvPaidDate=viewRoot.findViewById(R.id.textViewLastPaidDate);
//        tvRemainingAmount=viewRoot.findViewById(R.id.textViewRemainingAmt);
        tvLoanFees=viewRoot.findViewById(R.id.textViewLoanFees);
        btnPay=viewRoot.findViewById(R.id.buttonPay);
        helper=new Helper();
//        date=new Date();
//        sdf=new SimpleDateFormat("YYYY-MM-dd");
    }
}
