package com.example.administrator.digitalcredit.fragment;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.digitalcredit.Model.LoanDetail;
import com.example.administrator.digitalcredit.R;
import com.example.administrator.digitalcredit.Utils.Helper;
import com.example.administrator.digitalcredit.client.ApiClient;
import com.example.administrator.digitalcredit.dialog.DialogLoan;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RepaymentFragment extends Fragment {
    private View viewRoot;
    private TableLayout tableLayout;
    private Helper helper;
    private DialogFragment dialog;
    private ProgressDialog pd;
    public RepaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        init(inflater,container,savedInstanceState);
        try {
            pd.show();
            populateTable();

        }catch (Exception e){
            e.printStackTrace();
        }
        return viewRoot;
    }

    private void init(LayoutInflater inflater, ViewGroup container,
                      Bundle savedInstanceState){
        viewRoot=inflater.inflate(R.layout.fragment_repayment, container, false);
        tableLayout=viewRoot.findViewById(R.id.tableLayout);
        helper=new Helper();
        dialog=new DialogLoan();
        pd=helper.showDialog(getActivity(),"Loading","Please wait...");

    }

    private void populateTable(){
      try {
          ApiClient.getInstance().getLoan(helper.getSession(viewRoot.getContext()).get("user_id").toString(),"U")
                  .enqueue(new Callback<List<LoanDetail>>() {
                      @Override
                      public void onResponse(Call<List<LoanDetail>> call, Response<List<LoanDetail>> response) {
                          if(response.code()==200) {
                              for (LoanDetail loanDetail : response.body()) {
                                  TableRow row = new TableRow(viewRoot.getContext());
                                  row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                                  final TextView tvId = new TextView(viewRoot.getContext());
                                  tvId.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                  tvId.setTextSize(15f);
                                  tvId.setGravity(Gravity.CENTER_HORIZONTAL);
                                  tvId.setText(String.valueOf(loanDetail.getId()));
                                  row.addView(tvId);
                                  tvId.setClickable(true);
                                  tvId.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                                  tvId.setTextColor(Color.argb(255,0,0,255));
                                  tvId.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          //pd.show();
                                          Bundle bundle=new Bundle();
                                          bundle.putInt("loanId",Integer.parseInt(tvId.getText().toString()));
                                          dialog.setArguments(bundle);
                                          dialog.show(getFragmentManager(),"Loan");

                                      }
                                  });

                        /*TextView tvCreatedDate = new TextView(viewRoot.getContext());
                        tvCreatedDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvCreatedDate.setTextSize(15f);
                        tvCreatedDate.setGravity(Gravity.CENTER_HORIZONTAL);
                        tvCreatedDate.setText(loanDetail.getLoanCreatedDate());
                        tvCreatedDate.setWidth(100);
                        row.addView(tvCreatedDate);*/

                                  TextView tvDueDate = new TextView(viewRoot.getContext());
                                  tvDueDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                  tvDueDate.setTextSize(15f);
                                  tvDueDate.setGravity(Gravity.CENTER_HORIZONTAL);
                                  tvDueDate.setText(loanDetail.getLoanDueDate());
//                        tvDueDate.setWidth(100);
                                  row.addView(tvDueDate);

                                  TextView tvAmt = new TextView(viewRoot.getContext());
                                  tvAmt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                  tvAmt.setTextSize(15f);
                                  tvAmt.setGravity(Gravity.CENTER_HORIZONTAL);
                                  tvAmt.setText(String.valueOf(loanDetail.getAmt()));
                                  row.addView(tvAmt);

                                  TextView tvStatus = new TextView(viewRoot.getContext());
                                  tvStatus.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                  tvStatus.setTextSize(15f);
                                  tvStatus.setGravity(Gravity.CENTER_HORIZONTAL);
                                  tvStatus.setText(loanDetail.getLoanStatus());
                                  row.addView(tvStatus);

                                  row.setPadding(5,5,5,5);
                                  tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                              }
                              if(pd.isShowing()){
                                  pd.dismiss();
                              }
                          }else{
                              if (pd.isShowing()) {
                                  pd.dismiss();
                                  Toast.makeText(viewRoot.getContext(), "Service not available", Toast.LENGTH_SHORT).show();
                              }
                          }

                      }

                      @Override
                      public void onFailure(Call<List<LoanDetail>> call, Throwable t) {
                          if(pd.isShowing()){
                              pd.dismiss();
                          }

                          Toast.makeText(viewRoot.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                      }
                  });
      }catch (Exception e){
          e.printStackTrace();
      }
    }

}
