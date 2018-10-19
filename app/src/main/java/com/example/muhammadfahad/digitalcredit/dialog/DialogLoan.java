package com.example.muhammadfahad.digitalcredit.dialog;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadfahad.digitalcredit.Model.LoanDetail;
import com.example.muhammadfahad.digitalcredit.R;
import com.example.muhammadfahad.digitalcredit.Utils.Helper;

import com.example.muhammadfahad.digitalcredit.activity.HomeActivity;
import com.example.muhammadfahad.digitalcredit.client.ApiClient;
import com.example.muhammadfahad.digitalcredit.fragment.DashboardFragment;
import com.example.muhammadfahad.digitalcredit.fragment.RepaymentFragment;


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
    private Button btnClose;
    private ProgressDialog pd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewRoot = inflater.inflate(R.layout.dialog_fragment, container, false);
        try{
            init();
            bundle=getArguments();
            loanId=bundle.getInt("loanId");
            pd.show();
            ApiClient.getInstance().SingleLoan(helper.getSession(viewRoot.getContext()).get("user_id").toString(), String.valueOf(loanId))
                    .enqueue(new Callback<LoanDetail>() {
                        @Override
                        public void onResponse(Call<LoanDetail> call, Response<LoanDetail> response) {

                            if(response.isSuccessful() && response.code()==200){
                                try {
                                    bean=response.body();
                                    tvLoanId.setText(String.valueOf(bean.getId()));
//                                tvUserId.setText(bean.getCustomerId().toString());
                                    tvAmount.setText(String.valueOf(bean.getAmt()));
                                    tvCreatedDate.setText(bean.getLoanCreatedDate());
                                    tvDueDate.setText(bean.getLoanDueDate());
                                    tvAmountPaid.setText(String.valueOf(bean.getAmtPaid()));
                                    tvStatus.setText(bean.getLoanStatus());
//                                tvPaidDate.setText(bean.getLastPaidDate());
//                                tvRemainingAmount.setText(String.valueOf(bean.getRemainingAmt()));
                                    tvLoanFees.setText(String.valueOf(bean.getLoanFees()));
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            if(pd.isShowing()){
                                pd.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoanDetail> call, Throwable t) {
                            if(pd.isShowing()){
                                pd.dismiss();
                            }
                            Toast.makeText(viewRoot.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    try{

                        bean.setLoanStatus("P");
                        bean.setAmtPaid(bean.getAmt());

                        bean.setLastPaidDate(sdf.format(date).toString());

                        ApiClient.getInstance().updateLoanStatus(bean).enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                if(response.isSuccessful() && response.code()==200){
                                    Toast.makeText(viewRoot.getContext(), "Loan Paid...", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                // Toast.makeText(viewRoot.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        dismiss();
                        Intent intent=new Intent(viewRoot.getContext(),HomeActivity.class);
                        intent.putExtra("availLoan","Yes");
                        startActivity(intent);
//                        startActivity(new Intent(viewRoot.getContext(), HomeActivity.class));

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
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
//        tvPaidDate=viewRoot.findViewById(R.id.textViewLastPaidDate);
//        tvRemainingAmount=viewRoot.findViewById(R.id.textViewRemainingAmt);
        tvLoanFees=viewRoot.findViewById(R.id.textViewLoanFees);
        btnPay=viewRoot.findViewById(R.id.buttonPay);
        helper=new Helper();
        getDialog().setTitle("Loan Details");
        date=new Date();
        sdf=new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        pd=helper.showDialog(getActivity(),"Loading","Please wait");

    }
}
