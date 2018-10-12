package com.example.muhammadfahad.digitalcredit.dialog;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogHistory extends DialogFragment {
    private View viewRoot;
    private TextView tvLoanId;
    private TextView tvUserId;
    private TextView tvAmount;

    private TextView tvDueDate;
    private TextView tvAmountPaid;
    private TextView tvStatus;
    private TextView tvPaidDate;
    private TextView tvRemainingAmount;
    private TextView tvLoanFees;
    private Helper helper;
    private LoanDetail bean;
    private Button btnClose;
    private int loanId;
    private Bundle bundle;
    private Date date;
    private SimpleDateFormat sdf;

    public DialogHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_dialog_history, container, false);
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
                                tvLoanId.setText(String.valueOf(bean.getId()));
//                                tvUserId.setText(bean.getCustomerId().toString());
                                tvAmount.setText(String.valueOf(bean.getAmt()));
                                tvPaidDate.setText(bean.getLastPaidDate());
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
                    }

                    @Override
                    public void onFailure(Call<LoanDetail> call, Throwable t) {
                        Toast.makeText(viewRoot.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        return viewRoot;

    }

    private void init(){
        tvLoanId=viewRoot.findViewById(R.id.textViewLoanId);
//        tvUserId=viewRoot.findViewById(R.id.textViewUserId);
        tvAmount=viewRoot.findViewById(R.id.textViewAmt);
        tvPaidDate=viewRoot.findViewById(R.id.textViewLoanPaidDate);
        tvDueDate=viewRoot.findViewById(R.id.textViewLoanDueDate);
        tvAmountPaid=viewRoot.findViewById(R.id.textViewAmtPaid);
        tvStatus=viewRoot.findViewById(R.id.textViewLoanStatus);
//        tvPaidDate=viewRoot.findViewById(R.id.textViewLastPaidDate);
//        tvRemainingAmount=viewRoot.findViewById(R.id.textViewRemainingAmt);
        tvLoanFees=viewRoot.findViewById(R.id.textViewLoanFees);
        btnClose=viewRoot.findViewById(R.id.buttonClose);
        helper=new Helper();
        getDialog().setTitle("Test");
        date=new Date();
        sdf=new SimpleDateFormat("YYYY-MM-dd HH:MM:SS");
    }

}
