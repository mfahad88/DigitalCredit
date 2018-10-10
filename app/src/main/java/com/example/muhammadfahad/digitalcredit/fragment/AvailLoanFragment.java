package com.example.muhammadfahad.digitalcredit.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadfahad.digitalcredit.Interface.ApiInterface;
import com.example.muhammadfahad.digitalcredit.MainActivity;
import com.example.muhammadfahad.digitalcredit.Model.CustomerDetail;
import com.example.muhammadfahad.digitalcredit.Model.LoanDetail;
import com.example.muhammadfahad.digitalcredit.Model.TenureDetail;
import com.example.muhammadfahad.digitalcredit.R;
import com.example.muhammadfahad.digitalcredit.Utils.Helper;
import com.example.muhammadfahad.digitalcredit.client.ApiClient;

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
    private Integer remaining_bal,availableAmt,consumedAmt;
    private Button btn;
    private LoanDetail loanDetail;
    private Date date;
    private SimpleDateFormat sdf;
    private Calendar calendar;
    private Fragment fragmentDashboard;
    boolean isAvailed=false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_avail_loan, container, false);
        init();
        detail=ApiClient.getInstance().getCustomerDetails(helper.getSession(viewRoot.getContext()).get("mobileNo").toString());

        detail.clone().enqueue(new Callback<CustomerDetail>() {
            @Override
            public void onResponse(Call<CustomerDetail> call, Response<CustomerDetail> response) {
                if(response.code()==200){
                    detail.enqueue(new Callback<CustomerDetail>() {
                        @Override
                        public void onResponse(Call<CustomerDetail> call, Response<CustomerDetail> response) {
                            availableAmt=response.body().getAvailableAmountLimit();
                            if(availableAmt>0) {
                                tvAmt.setText("Rs. " + helper.CashFormatter(availableAmt.toString()));
                            }else{
                                Toast.makeText(viewRoot.getContext(), "Insufficient Limit", Toast.LENGTH_SHORT).show();
                                tvAmt.setText("Rs. " + availableAmt.toString());
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
                if(editable.length()>0 && (!TextUtils.isEmpty(editable.toString()))){
                    consumedAmt=Integer.parseInt(editable.toString());
                    ApiClient.getInstance().getProcessingFee(weeks, Integer.parseInt(editable.toString())).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if(response.code()==200){
                                tvProcessingFee.setText("Rs. "+helper.CashFormatter(response.body().toString()));
                                remaining_bal= availableAmt-(consumedAmt+response.body());
                                tvRemaining.setText("Rs. "+helper.CashFormatter(String.valueOf(remaining_bal)));
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {

                        }
                    });
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                    date = new Date();
                    calendar.add(Calendar.WEEK_OF_YEAR,weeks);
                    loanDetail.setAmt(consumedAmt);
                    loanDetail.setCustomerId(Integer.valueOf(helper.getSession(view.getContext()).get("user_id").toString()));
                    loanDetail.setLoanCreatedDate(sdf.format(date));
                    loanDetail.setLoanDueDate(sdf.format(calendar.getTime()));
                    loanDetail.setAmtPaid(0);
                    loanDetail.setLoanStatus("U");
                    loanDetail.setRemainingAmt(remaining_bal);
                    ApiClient.getInstance().CustomerLoan(loanDetail).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if(response.body()==200){


                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {

                        }
                    });




                Toast.makeText(viewRoot.getContext(), "Successful...", Toast.LENGTH_SHORT).show();
                fragmentDashboard=new DashboardFragment();
                getFragmentManager().beginTransaction().replace(R.id.view_container,fragmentDashboard).commit();

            }
        });
        return viewRoot;
    }

    private void init(){

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
        /*retrofit= ApiClient.getInstance();
        service=retrofit.create(ApiInterface.class);*/
    }
}
