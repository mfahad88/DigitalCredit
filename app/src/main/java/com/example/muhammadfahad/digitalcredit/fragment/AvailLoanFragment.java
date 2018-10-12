package com.example.muhammadfahad.digitalcredit.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadfahad.digitalcredit.Model.CustomerDetail;
import com.example.muhammadfahad.digitalcredit.Model.LoanDetail;
import com.example.muhammadfahad.digitalcredit.Model.TenureDetail;
import com.example.muhammadfahad.digitalcredit.R;
import com.example.muhammadfahad.digitalcredit.Utils.Helper;
import com.example.muhammadfahad.digitalcredit.activity.HomeActivity;
import com.example.muhammadfahad.digitalcredit.activity.LoginActivity;
import com.example.muhammadfahad.digitalcredit.client.ApiClient;

import java.io.IOException;
import java.math.BigInteger;
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
    private Spinner spinner;
    private Call<CustomerDetail> detail;
    private View viewRoot;
    private EditText editTextConsumed;
    private Integer weeks;
    private int remaining_bal,availableAmt,consumedAmt;
    private Button btn;
    private LoanDetail loanDetail;
    private Date date;
    private SimpleDateFormat sdf;
    private Calendar calendar;
    private Fragment fragmentDashboard;
    boolean isAvailed=false;
    private Intent intent;
    private int processingFee;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_avail_loan, container, false);
        init();
        Log.e("Helper---------->",helper.getSession(viewRoot.getContext()).toString());
        detail=ApiClient.getInstance().getCustomerDetails(helper.getSession(viewRoot.getContext()).get("user_mobile_no").toString());

        detail.clone().enqueue(new Callback<CustomerDetail>() {
            @Override
            public void onResponse(Call<CustomerDetail> call, Response<CustomerDetail> response) {
                if(response.code()==200){
                    detail.enqueue(new Callback<CustomerDetail>() {
                        @Override
                        public void onResponse(Call<CustomerDetail> call, Response<CustomerDetail> response) {
                            availableAmt=response.body().getAvailableAmountLimit();


                            if(availableAmt>0) {
                                tvAmt.setText("Rs. " + helper.CashFormatter(String.valueOf(availableAmt)));
                            }else{
                                Toast.makeText(viewRoot.getContext(), "Insufficient Limit", Toast.LENGTH_SHORT).show();
                                tvAmt.setText("Rs. " + String.valueOf(availableAmt));
                                editTextConsumed.setEnabled(false);
                                btn.setEnabled(false);
                            }
                        }

                        @Override
                        public void onFailure(Call<CustomerDetail> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<CustomerDetail> call, Throwable t) {
                Toast.makeText(viewRoot.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        ApiClient.getInstance().getTenure().enqueue(new Callback<List<TenureDetail>>() {
            @Override
            public void onResponse(Call<List<TenureDetail>> call, Response<List<TenureDetail>> response) {
                if(response.code()==200){
                    List<String> weeks=new ArrayList<>();
                    for(int i=0;i<response.body().size();i++){
                        weeks.add(response.body().get(i).getTenureTime()+" weeks");
                    }
                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(viewRoot.getContext(),android.R.layout.simple_list_item_1,weeks);
                    spinner.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<TenureDetail>> call, Throwable t) {
                Toast.makeText(viewRoot.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected=adapterView.getAdapter().getItem(Integer.parseInt(String.valueOf(l))).toString().trim();
                weeks= Integer.valueOf(selected.replace(" weeks",""));
                if(!TextUtils.isEmpty(editTextConsumed.getText().toString().trim()) && Integer.parseInt(editTextConsumed.getText().toString())>0){
                    ApiClient.getInstance().getProcessingFee(weeks,Integer.parseInt(editTextConsumed.getText().toString().trim()))
                            .enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                    processingFee=response.body();
                                    tvProcessingFee.setText("Rs. "+helper.CashFormatter(response.body().toString()));
                                    remaining_bal= availableAmt-(consumedAmt+processingFee);
                                    tvRemaining.setText("Rs. "+helper.CashFormatter(String.valueOf(remaining_bal)));
                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        editTextConsumed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                if(editable.length()>0 && (!TextUtils.isEmpty(editable.toString())) && Integer.parseInt(editable.toString())>0){

                    consumedAmt=Integer.parseInt(String.valueOf(editable));
                    ApiClient.getInstance().getProcessingFee(weeks, Integer.parseInt(editable.toString())).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if(response.code()==200){
                                processingFee=response.body();
                                tvProcessingFee.setText("Rs. "+helper.CashFormatter(response.body().toString()));
                                remaining_bal= availableAmt-(consumedAmt+processingFee);
                                tvRemaining.setText("Rs. "+helper.CashFormatter(String.valueOf(remaining_bal)));
                                Log.e("consumedAmt--->", ""+consumedAmt);
                                Log.e("availableAmt--->", ""+availableAmt);


                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {

                        }
                    });
                }else{
                    tvProcessingFee.setText("Rs. 0.00");
                    tvRemaining.setText("Rs. 0.00");
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                   try{
                       date = new Date();
                       calendar.add(Calendar.WEEK_OF_YEAR,weeks);
                       loanDetail.setAmt(consumedAmt);
                       loanDetail.setCustomerId(Integer.valueOf(helper.getSession(view.getContext()).get("user_id").toString()));
                       loanDetail.setLoanCreatedDate(sdf.format(date));
                       loanDetail.setLoanDueDate(sdf.format(calendar.getTime()));
                       loanDetail.setAmtPaid(0);
                       loanDetail.setLoanStatus("U");
                       loanDetail.setRemainingAmt(remaining_bal);
                       loanDetail.setLoanFees(processingFee);
                       if(Integer.parseInt(editTextConsumed.getText().toString())>availableAmt) {
                           Toast.makeText(viewRoot.getContext(), "Please enter valid amount...", Toast.LENGTH_SHORT).show();
                       }
                       if(consumedAmt>0){
                           if(Integer.parseInt(editTextConsumed.getText().toString())>0 &&
                                   Integer.parseInt(editTextConsumed.getText().toString())<=availableAmt) {
                               ApiClient.getInstance().CustomerLoan(loanDetail).enqueue(new Callback<Integer>() {
                                   @Override
                                   public void onResponse(Call<Integer> call, Response<Integer> response) {
                                        btn.setEnabled(false);
                                       if(response.code()==200 && response.isSuccessful()){
                                           Toast.makeText(getActivity(), "Successful...", Toast.LENGTH_SHORT).show();
                                           intent=new Intent(getActivity(), HomeActivity.class);

//                                           intent.putExtra("loan","A");
                                           startActivity(intent);
                                       }
                                   }

                                   @Override
                                   public void onFailure(Call<Integer> call, Throwable t) {

                                   }
                               });

                           }

                       }





             /*   try {
                    detail.request().cacheControl().noCache();
                    Log.e("API-------->",ApiClient.getInstance().getCustomerDetails(helper.getSession(viewRoot.getContext()).get("user_mobile_no").toString())
                            .execute().body().getAvailableBalance().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                       //helper.putSession(viewRoot.getContext(),"available_Amount_Limit", String.valueOf(remaining_bal));

                   }catch (Exception e){
                       e.printStackTrace();
                   }

            }
        });
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
        helper=Helper.getInstance();
        btn=viewRoot.findViewById(R.id.buttonSubmit);
        loanDetail=new LoanDetail();
        sdf = new SimpleDateFormat("YYYY-MM-dd");
        calendar=Calendar.getInstance();
        InputMethodManager imm = (InputMethodManager) viewRoot.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        /*retrofit= ApiClient.getInstance();
        service=retrofit.create(ApiInterface.class);*/
    }
}
