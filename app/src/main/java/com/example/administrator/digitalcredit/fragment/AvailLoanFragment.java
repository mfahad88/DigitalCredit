package com.example.administrator.digitalcredit.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.administrator.digitalcredit.Model.CustomerDetail;
import com.example.administrator.digitalcredit.Model.DistributorResponse;
import com.example.administrator.digitalcredit.Model.LoanDetail;
import com.example.administrator.digitalcredit.Model.TenureDetail;
import com.example.administrator.digitalcredit.R;
import com.example.administrator.digitalcredit.Utils.Helper;
import com.example.administrator.digitalcredit.activity.HomeActivity;
import com.example.administrator.digitalcredit.adapter.DistributorAdapter;
import com.example.administrator.digitalcredit.client.ApiClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




/**
 * A simple {@link Fragment} subclass.
 */
public class AvailLoanFragment extends Fragment {
    public static AvailLoanFragment fragment;
    private Helper helper;
    private TextView tvAmt;
    private TextView tvProcessingFee;
    private TextView tvRemaining;
    private TextView tvLoanDisburse;
    private Spinner spinner,spinnerDistributor;
    private Call<CustomerDetail> detail;
    private View viewRoot;
    private EditText editTextConsumed;
    private Integer weeks;
    private float remaining_bal,availableAmt,consumedAmt;
    private Button btn;
    private LoanDetail loanDetail;
    private Date date;
    private SimpleDateFormat sdf;
    private Calendar calendar;
    private Fragment fragmentDashboard;
    boolean isAvailed=false;
    private Intent intent;
    private int processingFee;
    private ProgressDialog pd;
    private int orderId;
    private TextView tvStatus;
    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_avail_loan, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Avail Loan");

       try{
           init();

           if(getArguments()!=null){
               editTextConsumed.setText(String.valueOf(getArguments().getFloat("totalAmount")));
               consumedAmt=getArguments().getFloat("totalAmount");
               orderId=getArguments().getInt("orderId");

           }

           pd.show();
           Log.e("Helper---------->",helper.getSession(viewRoot.getContext()).toString());
           detail=ApiClient.getInstance().getCustomerDetails(helper.getSession(viewRoot.getContext()).get("user_mobile_no").toString());



           detail.clone().enqueue(new Callback<CustomerDetail>() {
               @Override
               public void onResponse(Call<CustomerDetail> call, Response<CustomerDetail> response) {
                   if(response.code()==200 && response.isSuccessful()){
                       try{
                           detail.enqueue(new Callback<CustomerDetail>() {
                               @Override
                               public void onResponse(Call<CustomerDetail> call, Response<CustomerDetail> response) {
                                   availableAmt=response.body().getAvailableAmountLimit().floatValue();

                                   if(consumedAmt>availableAmt){
                                       spinner.setEnabled(false);
                                       spinnerDistributor.setEnabled(false);
                                       btn.setEnabled(false);
                                       tvStatus.setVisibility(View.VISIBLE);
                                       tvStatus.setText("Low Limit...");
                                   }

                                   if(availableAmt<0){
                                       availableAmt=0;
                                   }

                                   if(availableAmt>0) {
                                       tvAmt.setText("Rs. " + helper.CashFormatter(String.valueOf(availableAmt)));


                                       ApiClient.getInstance().getProcessingFee(1,consumedAmt)
                                               .enqueue(new Callback<Integer>() {
                                                   @Override
                                                   public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                       btn.setEnabled(true);
                                                        if(response.code()==200 || response.isSuccessful()){
                                                            spinner.setEnabled(true);
                                                            processingFee=response.body();
                                                            tvProcessingFee.setText("Rs. "+helper.CashFormatter(String.valueOf(processingFee)));
                                                            remaining_bal= availableAmt-consumedAmt-processingFee;

                                                            if(remaining_bal<0){
                                                                tvRemaining.setText("Rs. 0.00");
                                                            }else {
                                                                tvRemaining.setText("Rs. " + helper.CashFormatter(String.valueOf(remaining_bal)));
                                                            }
                                                            if(consumedAmt-processingFee>0) {
                                                                tvLoanDisburse.setText("Rs. "+helper.CashFormatter(String.valueOf(consumedAmt+processingFee)));
                                                            }else{
                                                                tvLoanDisburse.setText("Rs. "+String.valueOf(consumedAmt+processingFee));
                                                            }
                                                        }
                                                   }

                                                   @Override
                                                   public void onFailure(Call<Integer> call, Throwable t) {
                                                       btn.setEnabled(true);
                                                       if(pd.isShowing()){
                                                           pd.dismiss();
                                                       }
                                                   }
                                               });
                                   }else{
                                       Toast.makeText(viewRoot.getContext(), "Insufficient Limit", Toast.LENGTH_SHORT).show();
                                       tvAmt.setText("Rs. " + String.valueOf(availableAmt));
                                       editTextConsumed.setEnabled(false);
                                       //btn.setEnabled(false);
                                   }
                               }

                               @Override
                               public void onFailure(Call<CustomerDetail> call, Throwable t) {
                                   helper.showMesage(viewRoot.getRootView(),t.getMessage());
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




           ApiClient.getInstance().getTenure().enqueue(new Callback<List<TenureDetail>>() {
               @Override
               public void onResponse(Call<List<TenureDetail>> call, Response<List<TenureDetail>> response) {
                   if(response.code()==200 || response.isSuccessful()){
                       try{
                           List<String> weeks=new ArrayList<>();
                           for(int i=0;i<response.body().size();i++){
                               weeks.add(response.body().get(i).getTenureTime()+" weeks");
                           }
                           ArrayAdapter<String> adapter=new ArrayAdapter<String>(viewRoot.getContext(),android.R.layout.simple_list_item_1,weeks);
                           spinner.setAdapter(adapter);
                           if(pd.isShowing()){
                               pd.dismiss();
                           }
                       }catch (Exception e){
                           e.printStackTrace();
                       }

                   }
               }

               @Override
               public void onFailure(Call<List<TenureDetail>> call, Throwable t) {
                   if(pd.isShowing()){
                       pd.dismiss();
                   }
                   Toast.makeText(viewRoot.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
               }
           });





           ApiClient.getInstance().distributor()
                   .enqueue(new Callback<List<DistributorResponse>>() {
                       @Override
                       public void onResponse(Call<List<DistributorResponse>> call, Response<List<DistributorResponse>> response) {
                           btn.setEnabled(false);
                           if(response.code()==200 || response.isSuccessful()){
                            btn.setEnabled(true);
                            spinnerDistributor.setEnabled(true);
                               DistributorAdapter adapter=new DistributorAdapter(viewRoot.getContext(),R.layout.distributor_list_row,response.body());

                                    spinnerDistributor.setAdapter(adapter);
                           }else {
                               helper.showMesage(viewRoot.getRootView(),"Something went wrong...");
                           }
                       }

                       @Override
                       public void onFailure(Call<List<DistributorResponse>> call, Throwable t) {
                           t.printStackTrace();
                            helper.showMesage(viewRoot.getRootView(),t.getMessage());
                       }
                   });


           spinnerDistributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   loanDetail.setFk_user_distributerId(Integer.parseInt(String.valueOf(parent.getSelectedItemId())));
               }

               @Override
               public void onNothingSelected(AdapterView<?> parent) {

               }
           });




           spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                   try {
                       String selected=adapterView.getAdapter().getItem(Integer.parseInt(String.valueOf(l))).toString().trim();
                       weeks= Integer.valueOf(selected.replace(" weeks",""));
                       if(consumedAmt>0){
                           btn.setEnabled(false);
                           if(consumedAmt<availableAmt){
                               ApiClient.getInstance().getProcessingFee(weeks,consumedAmt)
                                       .enqueue(new Callback<Integer>() {
                                           @Override
                                           public void onResponse(Call<Integer> call, Response<Integer> response) {
                                               btn.setEnabled(true);
                                               processingFee=response.body();
                                               tvProcessingFee.setText("Rs. "+helper.CashFormatter(response.body().toString()));
                                               remaining_bal= availableAmt-consumedAmt-processingFee;
                                               if(remaining_bal<0){
                                                   tvRemaining.setText("Rs. 0.00");
                                               }else {
                                                   tvRemaining.setText("Rs. " + helper.CashFormatter(String.valueOf(remaining_bal)));
                                               }
                                               if(consumedAmt-processingFee>0) {
                                                   tvLoanDisburse.setText("Rs. "+helper.CashFormatter(String.valueOf(consumedAmt+processingFee)));
                                               }else{
                                                   tvLoanDisburse.setText("Rs. "+String.valueOf(consumedAmt+processingFee));
                                               }
                                           }

                                           @Override
                                           public void onFailure(Call<Integer> call, Throwable t) {
                                               btn.setEnabled(true);
                                               if(pd.isShowing()){
                                                   pd.dismiss();
                                               }
                                           }
                                       });
                           }
                       }
                   }catch (Exception e){
                       e.printStackTrace();
                   }
               }

               @Override
               public void onNothingSelected(AdapterView<?> adapterView) {
               }
           });



           btn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(final View view) {

                     try{
                         if(consumedAmt<availableAmt){
                             if(!TextUtils.isEmpty(editTextConsumed.getText())){
                                 btn.setEnabled(false);
                                 date = new Date();
                                 calendar.add(Calendar.WEEK_OF_YEAR,weeks);
                                 loanDetail.setAmt(consumedAmt+processingFee);
                                 loanDetail.setCustomerId(Integer.valueOf(helper.getSession(view.getContext()).get("user_id").toString()));
                                 loanDetail.setLoanCreatedDate(sdf.format(date));
                                 loanDetail.setLoanDueDate(sdf.format(calendar.getTime()));
                                 loanDetail.setAmtPaid(0);
                                 loanDetail.setLoanStatus("U");
                                 loanDetail.setRemainingAmt(remaining_bal);
                                 loanDetail.setLoanFees(processingFee);
                                 loanDetail.setOrderId(orderId);
                                 loanDetail.setOrder_status("Delivered");
                                 if(consumedAmt>availableAmt) {
                                     Toast.makeText(viewRoot.getContext(), "Please enter valid amount...", Toast.LENGTH_SHORT).show();
                                     btn.setEnabled(true);
                                 }

                                 if(consumedAmt>0){
                                     if( consumedAmt<=availableAmt /*&& (consumedAmt+processingFee)>availableAmt*/) {
                                         /*if(consumedAmt<processingFee *//*|| consumedAmt==processingFee*//*){
                                         btn.setEnabled(false);
                                         Toast.makeText(viewRoot.getContext(), "Consumed amount cannot be less than processing fee", Toast.LENGTH_SHORT).show();
                                     }else {*/
                                         ApiClient.getInstance().CustomerLoan(loanDetail).enqueue(new Callback<Integer>() {
                                             @Override
                                             public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                 btn.setEnabled(false);
                                                 if (response.code() == 200 && response.isSuccessful()) {
                                                     Toast.makeText(getActivity(), "Successful...", Toast.LENGTH_SHORT).show();
                                                     intent = new Intent(getActivity(), HomeActivity.class);

//                                           intent.putExtra("loan","A");
                                                     startActivity(intent);
                                                     btn.setEnabled(true);
                                                 }
                                             }

                                             @Override
                                             public void onFailure(Call<Integer> call, Throwable t) {
                                                 btn.setEnabled(true);
                                                 Toast.makeText(viewRoot.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                             }
                                         });
                                         //  }

                                     }
                                     else {
                                         btn.setEnabled(false);
                                         Toast.makeText(viewRoot.getContext(), "Invalid amount", Toast.LENGTH_SHORT).show();
                                     }

                                 }
                             }
                         }
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
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        tvAmt=viewRoot.findViewById(R.id.textViewAvailablelimit);
        spinner=viewRoot.findViewById(R.id.spinnerTenure);
        tvProcessingFee=viewRoot.findViewById(R.id.textViewProcessing);
        tvRemaining=viewRoot.findViewById(R.id.textViewAvailableBal);
        editTextConsumed=viewRoot.findViewById(R.id.edtTextConsumed);
        tvLoanDisburse=viewRoot.findViewById(R.id.textViewLoanDisburse);
        helper=Helper.getInstance();
        btn=viewRoot.findViewById(R.id.buttonSubmit);
        loanDetail=new LoanDetail();
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar=Calendar.getInstance();
        spinnerDistributor=viewRoot.findViewById(R.id.spinnerDistributor);
        tvStatus=viewRoot.findViewById(R.id.textViewStatus);
        pd=helper.showDialog(viewRoot.getContext(),"Loading","Please wait...");
        spinner.setEnabled(false);
        spinnerDistributor.setEnabled(false);
        btn.setEnabled(false);

    }
}
