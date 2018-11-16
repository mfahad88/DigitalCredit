package com.example.administrator.digitalcredit.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

import com.example.administrator.digitalcredit.Model.LoanDetail;
import com.example.administrator.digitalcredit.R;
import com.example.administrator.digitalcredit.Utils.Helper;
import com.example.administrator.digitalcredit.client.ApiClient;
import com.example.administrator.digitalcredit.dialog.DialogHistory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {
    private View viewRoot;
    private Helper helper;
    private TableLayout tableLayout;
    private DialogFragment dialog;
    private ProgressDialog pd;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot=inflater.inflate(R.layout.fragment_history, container, false);
        try {
            init();
            pd.show();
            Log.e("User_id------->",helper.getSession(viewRoot.getContext()).get("user_id").toString());
            ApiClient.getInstance().getLoanAll(helper.getSession(viewRoot.getContext()).get("user_id").toString())
                    .enqueue(new Callback<List<LoanDetail>>() {
                        @SuppressLint("NewApi")
                        @Override
                        public void onResponse(Call<List<LoanDetail>> call, Response<List<LoanDetail>> response) {
                            if(response.code()==200) {
                                if(pd.isShowing()){
                                    pd.dismiss();
                                }
                                for (LoanDetail loanDetail : response.body()) {
                                    TableRow.LayoutParams params=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                                    TableRow row = new TableRow(viewRoot.getContext());

                                    row.setLayoutParams(params);

                                    final TextView tvId = new TextView(viewRoot.getContext());
                                    tvId.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tvId.setTextSize(15f);
                                    tvId.setGravity(Gravity.START);
                                    tvId.setText(String.valueOf(loanDetail.getId()));
                                    tvId.setPadding(2,2,2,2);
                                    row.addView(tvId);
                                    tvId.setClickable(true);
                                    tvId.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                                    tvId.setTextColor(Color.argb(255,0,0,255));
                                    tvId.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            tvId.setEnabled(false);
                                            //pd.show();
                                            Bundle bundle=new Bundle();
                                            bundle.putInt("loanId",Integer.parseInt(tvId.getText().toString()));
                                            dialog.setArguments(bundle);
                                            dialog.setCancelable(false);
                                            dialog.show(getFragmentManager(),"Loan");
                                            tvId.setEnabled(true);
                                        }
                                    });



                                    TextView tvDueDate = new TextView(viewRoot.getContext());
                                    tvDueDate.setLayoutParams(params);
                                    tvDueDate.setTextSize(15f);
                                    tvDueDate.setGravity(Gravity.START);
//                                    tvDueDate.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                                    tvDueDate.setText(loanDetail.getLoanDueDate());
                                    tvDueDate.setWidth(300);
                                    tvDueDate.setPadding(2,2,2,2);
                                    row.addView(tvDueDate);

                                    TextView tvAmt = new TextView(viewRoot.getContext());
                                    tvAmt.setLayoutParams(params);
                                    tvAmt.setTextSize(15f);
                                    tvAmt.setGravity(Gravity.END);
                                    tvAmt.setText(String.valueOf(loanDetail.getAmt()));
                                    tvAmt.setPadding(2,2,2,2);
                                    row.addView(tvAmt);

                                    TextView tvStatus = new TextView(viewRoot.getContext());
                                    tvStatus.setLayoutParams(params);
                                    tvStatus.setTextSize(15f);
                                    tvStatus.setGravity(Gravity.CENTER_HORIZONTAL);
                                    tvStatus.setText(loanDetail.getLoanStatus());
                                    tvStatus.setPadding(2,2,2,2);
                                    row.addView(tvStatus);

                                    TextView tvLoan = new TextView(viewRoot.getContext());
                                    tvLoan.setLayoutParams(params);
                                    tvLoan.setTextSize(15f);
                                    tvLoan.setGravity(Gravity.START);
                                    tvLoan.setText(String.valueOf(loanDetail.getLoanFees()));
                                    tvLoan.setPadding(2,2,2,2);
                                    row.addView(tvLoan);


                                    TableLayout.LayoutParams table_params=new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                                    table_params.setMargins(7,0,0,0);
                                    tableLayout.addView(row, table_params);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<LoanDetail>> call, Throwable t) {
                            Toast.makeText(viewRoot.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
        return viewRoot;
    }
    private void init(){
        tableLayout=viewRoot.findViewById(R.id.tableLayout);
        helper=Helper.getInstance();
        dialog=new DialogHistory();
        pd=helper.showDialog(viewRoot.getContext(),"Loading","Please wait...");
    }
}
