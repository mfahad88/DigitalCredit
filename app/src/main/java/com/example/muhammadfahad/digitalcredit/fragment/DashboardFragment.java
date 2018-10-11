package com.example.muhammadfahad.digitalcredit.fragment;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadfahad.digitalcredit.Model.CustomerDetail;
import com.example.muhammadfahad.digitalcredit.Model.LoanDetail;
import com.example.muhammadfahad.digitalcredit.R;
import com.example.muhammadfahad.digitalcredit.Utils.Helper;
import com.example.muhammadfahad.digitalcredit.client.ApiClient;
import com.example.muhammadfahad.digitalcredit.dialog.DialogLoan;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    public static DashboardFragment fragment;
    private TextView tvBase,tvBehavior,tvLimit,tvAggregation,tvRemainingAmt;
    private View viewRoot;
    public Call<CustomerDetail> detail = null;
    private Call<List<LoanDetail>> loan;
    private TableLayout tableLayout;
    private String availLoan;
    private Helper helper;
    private FragmentTransaction ft;
    private DialogLoan dialog;
    private Fragment prev;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot=inflater.inflate(R.layout.fragment_dashboard, container, false);
        init();

        tvBase.setText(helper.getSession(viewRoot.getContext()).get("base_scrore").toString());
        tvBehavior.setText(helper.getSession(viewRoot.getContext()).get("behavior_score").toString());
        tvAggregation.setText(String.valueOf(Integer.parseInt(helper.getSession(viewRoot.getContext()).get("base_scrore").toString())+Integer.parseInt(helper.getSession(viewRoot.getContext()).get("behavior_score").toString())));
        Log.e("Helper----->",helper.CashFormatter(helper.getSession(viewRoot.getContext()).get("available_Balance").toString()));
        tvRemainingAmt.setText("Rs. "+helper.CashFormatter(helper.getSession(viewRoot.getContext()).get("available_Balance").toString()));
        if(Integer.parseInt(helper.getSession(viewRoot.getContext()).get("available_Amount_Limit").toString())>0) {
            tvLimit.setText("Rs. " + helper.CashFormatter(helper.getSession(viewRoot.getContext()).get("available_Amount_Limit").toString()));
        }else{
            tvLimit.setText("Rs. " + helper.getSession(viewRoot.getContext()).get("available_Amount_Limit").toString());
        }

        loan=ApiClient.getInstance().getLoan(helper.getSession(viewRoot.getContext()).get("user_id").toString(),"U");
        loan.enqueue(new Callback<List<LoanDetail>>() {
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
                        tvId.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(viewRoot.getContext(), tvId.getText().toString(), Toast.LENGTH_SHORT).show();
                                Bundle bundle=new Bundle();
                                bundle.putInt("loanId",Integer.parseInt(tvId.getText().toString()));
                                dialog.setArguments(bundle);
                                dialog.show(getFragmentManager(),"Loan");
                            }
                        });

                        TextView tvCreatedDate = new TextView(viewRoot.getContext());
                        tvCreatedDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvCreatedDate.setTextSize(15f);
                        tvCreatedDate.setGravity(Gravity.CENTER_HORIZONTAL);
                        tvCreatedDate.setText(loanDetail.getLoanCreatedDate());
                        row.addView(tvCreatedDate);

                        TextView tvDueDate = new TextView(viewRoot.getContext());
                        tvDueDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvDueDate.setTextSize(15f);
                        tvDueDate.setGravity(Gravity.CENTER_HORIZONTAL);
                        tvDueDate.setText(loanDetail.getLoanDueDate());
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

    public void init(){

        tvBase=viewRoot.findViewById(R.id.textViewBase);
        tvBehavior=viewRoot.findViewById(R.id.textViewBehavior);
        tvLimit=viewRoot.findViewById(R.id.textViewAvailablelimit);
        tvAggregation=viewRoot.findViewById(R.id.textViewAggregation);
        tableLayout=viewRoot.findViewById(R.id.tableLayout);
        tvRemainingAmt=viewRoot.findViewById(R.id.textViewRemainingAmt);
        helper=Helper.getInstance();

        ft=this.getActivity().getSupportFragmentManager().beginTransaction();
        dialog=new DialogLoan();

    }

}
