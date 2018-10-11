package com.example.muhammadfahad.digitalcredit.fragment;


import android.os.Bundle;
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

import com.example.muhammadfahad.digitalcredit.Model.LoanDetail;
import com.example.muhammadfahad.digitalcredit.R;
import com.example.muhammadfahad.digitalcredit.Utils.Helper;
import com.example.muhammadfahad.digitalcredit.client.ApiClient;

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

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot=inflater.inflate(R.layout.fragment_history, container, false);
        init();
        Log.e("User_id------->",helper.getSession(viewRoot.getContext()).get("user_id").toString());
        ApiClient.getInstance().getLoan(helper.getSession(viewRoot.getContext()).get("user_id").toString(),"P")
        .enqueue(new Callback<List<LoanDetail>>() {
            @Override
            public void onResponse(Call<List<LoanDetail>> call, Response<List<LoanDetail>> response) {
                if(response.code()==200) {

                    for (LoanDetail loanDetail : response.body()) {
                        TableRow row = new TableRow(viewRoot.getContext());
                        Toast.makeText(viewRoot.getContext(), loanDetail.getLastPaidDate(), Toast.LENGTH_SHORT).show();
                        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                        final TextView tvId = new TextView(viewRoot.getContext());
                        tvId.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvId.setTextSize(15f);
                        tvId.setGravity(Gravity.CENTER_HORIZONTAL);
                        tvId.setText(String.valueOf(loanDetail.getId()));
                        tvId.setPadding(2,2,2,2);
                        row.addView(tvId);
                        tvId.setClickable(true);
                       /* tvId.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(viewRoot.getContext(), tvId.getText().toString(), Toast.LENGTH_SHORT).show();
                                Bundle bundle=new Bundle();
                                bundle.putInt("loanId",Integer.parseInt(tvId.getText().toString()));
                                dialog.setArguments(bundle);
                                dialog.show(getFragmentManager(),"Loan");
                            }
                        });*/

                        TextView tvPaidDate = new TextView(viewRoot.getContext());
                        tvPaidDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvPaidDate.setTextSize(15f);
                        tvPaidDate.setGravity(Gravity.CENTER_HORIZONTAL);
                        tvPaidDate.setText(loanDetail.getLastPaidDate());
                        tvPaidDate.setPadding(2,2,2,2);
                        row.addView(tvPaidDate);

                        TextView tvDueDate = new TextView(viewRoot.getContext());
                        tvDueDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvDueDate.setTextSize(15f);
                        tvDueDate.setGravity(Gravity.CENTER_HORIZONTAL);
                        tvDueDate.setText(loanDetail.getLoanDueDate());
                        tvDueDate.setPadding(2,2,2,2);
                        row.addView(tvDueDate);

                        TextView tvAmt = new TextView(viewRoot.getContext());
                        tvAmt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvAmt.setTextSize(15f);
                        tvAmt.setGravity(Gravity.CENTER_HORIZONTAL);
                        tvAmt.setText(String.valueOf(loanDetail.getAmt()));
                        tvAmt.setPadding(2,2,2,2);
                        row.addView(tvAmt);

                        TextView tvStatus = new TextView(viewRoot.getContext());
                        tvStatus.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvStatus.setTextSize(15f);
                        tvStatus.setGravity(Gravity.CENTER_HORIZONTAL);
                        tvStatus.setText(loanDetail.getLoanStatus());
                        tvStatus.setPadding(2,2,2,2);
                        row.addView(tvStatus);


                        tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<LoanDetail>> call, Throwable t) {
                Toast.makeText(viewRoot.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return viewRoot;
    }
    private void init(){
        tableLayout=viewRoot.findViewById(R.id.tableLayout);
        helper=Helper.getInstance();
    }
}
