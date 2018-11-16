package com.example.administrator.digitalcredit.fragment;



import android.app.ProgressDialog;
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

import com.example.administrator.digitalcredit.Model.CustomerDetail;
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
    private DialogFragment dialog;
    private Fragment prev;
    private String userId;
    private ProgressDialog pd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot=inflater.inflate(R.layout.fragment_dashboard, container, false);
        try {
            init();
            pd.show();
            ApiClient.getInstance().getCustomerDetails(helper.getSession(viewRoot.getContext()).get("mobileNo").toString())
                    .enqueue(new Callback<CustomerDetail>() {
                        @Override
                        public void onResponse(Call<CustomerDetail> call, Response<CustomerDetail> response) {
                            if(response.isSuccessful() && response.code()==200){

                                helper.putSession(viewRoot.getContext(),"user_id",response.body().getUserId().toString());
                                helper.putSession(viewRoot.getContext(),"user_mobile_no",response.body().getUserMobileNo().toString());
                                helper.putSession(viewRoot.getContext(),"user_cnic",response.body().getUserCnic().toString());
                                helper.putSession(viewRoot.getContext(),"user_channel_id",response.body().getUserChannelId().toString());
                                helper.putSession(viewRoot.getContext(),"income",response.body().getIncome().toString());
                                helper.putSession(viewRoot.getContext(),"user_status",response.body().getUserStatus().toString());
                                helper.putSession(viewRoot.getContext(),"available_Balance",response.body().getAvailableBalance().toString());
                                helper.putSession(viewRoot.getContext(),"base_scrore",response.body().getBaseScrore().toString());
                                helper.putSession(viewRoot.getContext(),"behavior_score",response.body().getBehaviorScore().toString());
                                helper.putSession(viewRoot.getContext(),"base_score_flag",response.body().getBaseScoreFlag().toString());
                                helper.putSession(viewRoot.getContext(),"behavior_score_flag",response.body().getBehaviorScoreFlag().toString());
                                helper.putSession(viewRoot.getContext(),"assigned_amount_Limit",response.body().getAssignedAmountLimit().toString());
                                helper.putSession(viewRoot.getContext(),"consumed_Limit",response.body().getConsumedLimit().toString());
                                helper.putSession(viewRoot.getContext(),"available_Amount_Limit",response.body().getAvailableAmountLimit().toString());
                                helper.putSession(viewRoot.getContext(),"dbr_value",response.body().getDbrValue().toString());
                                helper.putSession(viewRoot.getContext(),"dbr_value_flag",response.body().getDbrValueFlag().toString());
                                //startActivity(intent);
                                tvBase.setText(helper.getSession(viewRoot.getContext()).get("base_scrore").toString());
                                tvBehavior.setText(helper.getSession(viewRoot.getContext()).get("behavior_score").toString());
                                tvAggregation.setText(String.valueOf(Integer.parseInt(helper.getSession(viewRoot.getContext()).get("base_scrore").toString())+Integer.parseInt(helper.getSession(viewRoot.getContext()).get("behavior_score").toString())));
                                Log.e("Helper----->",helper.CashFormatter(response.body().getAvailableBalance().toString()));
                                if(response.body().getAvailLoan()>0.00){
                                    tvRemainingAmt.setText("Rs. "+helper.CashFormatter(String.valueOf(response.body().getAvailLoan())));
                                }else{
                                    tvRemainingAmt.setText("Rs. "+String.valueOf(response.body().getAvailLoan()));
                                }
                                if(Integer.parseInt(helper.getSession(viewRoot.getContext()).get("available_Amount_Limit").toString())>0) {
                                    tvLimit.setText("Rs. " + helper.CashFormatter(helper.getSession(viewRoot.getContext()).get("available_Amount_Limit").toString()));
                                }else{
                                    if(Integer.parseInt(helper.getSession(viewRoot.getContext()).get("available_Amount_Limit").toString())<0){
                                        tvLimit.setText("Rs. 0.00");
                                    }else {
                                        tvLimit.setText("Rs. " + helper.getSession(viewRoot.getContext()).get("available_Amount_Limit").toString());
                                    }
                                }

                               try{
                                   ApiClient.getInstance().getLoan(helper.getSession(viewRoot.getContext()).get("user_id").toString(),"U")
                                           .enqueue(new Callback<List<LoanDetail>>() {
                                               @Override
                                               public void onResponse(Call<List<LoanDetail>> call, Response<List<LoanDetail>> response) {
                                                   if(response.code()==200) {
                                                       populateTable(response.body());
                                                   }
                                                   if(pd.isShowing()){
                                                       pd.dismiss();
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

                        @Override
                        public void onFailure(Call<CustomerDetail> call, Throwable t) {
                            if(pd.isShowing()){
                                pd.dismiss();
                            }
                            Toast.makeText(viewRoot.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }catch (Exception e){
            e.printStackTrace();
        }



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
        pd=helper.showDialog(viewRoot.getContext(),"Loading","Please wait...");

    }

    public void populateTable(List<LoanDetail> list){
       try {
           for (LoanDetail loanDetail : list) {
               TableRow row = new TableRow(viewRoot.getContext());
               row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

               final TextView tvId = new TextView(viewRoot.getContext());
               tvId.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
               tvId.setTextSize(15f);
               tvId.setGravity(Gravity.CENTER_HORIZONTAL);
               tvId.setText(String.valueOf(loanDetail.getId()));
               row.addView(tvId);
               tvId.setClickable(true);
            /*tvId.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
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
*/
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
//            tvDueDate.setWidth(100);
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
       }catch (Exception e){
           e.printStackTrace();
       }
    }

}
