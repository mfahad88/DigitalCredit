package com.example.muhammadfahad.digitalcredit.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadfahad.digitalcredit.Interface.ApiInterface;
import com.example.muhammadfahad.digitalcredit.MainActivity;
import com.example.muhammadfahad.digitalcredit.Model.CustomerDetail;
import com.example.muhammadfahad.digitalcredit.Model.TenureDetail;
import com.example.muhammadfahad.digitalcredit.R;
import com.example.muhammadfahad.digitalcredit.client.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.muhammadfahad.digitalcredit.MainActivity.MY_PREFS_NAME;



/**
 * A simple {@link Fragment} subclass.
 */
public class AvailLoanFragment extends Fragment {
    public static AvailLoanFragment fragment;
    private SharedPreferences preferences;
    private TextView tvAmt;
    private Retrofit retrofit;
    private ApiInterface service;
    private Spinner spinner;
    private Call<CustomerDetail> detail;
    private Call<List<TenureDetail>> tenure;
    private View view;
    public static AvailLoanFragment getInstance() {
        if(fragment==null){
            Log.e("AvailLoan","Object Created...");
            fragment=new AvailLoanFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_avail_loan, container, false);
        init();
        detail=service.getScore(preferences.getString("mobile",""));

        detail.clone().enqueue(new Callback<CustomerDetail>() {
            @Override
            public void onResponse(Call<CustomerDetail> call, Response<CustomerDetail> response) {
                if(response.code()==200){
                    detail.enqueue(new Callback<CustomerDetail>() {
                        @Override
                        public void onResponse(Call<CustomerDetail> call, Response<CustomerDetail> response) {
                            tvAmt.setText("Rs. "+response.body().getAvailableAmountLimit());
                        }

                        @Override
                        public void onFailure(Call<CustomerDetail> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<CustomerDetail> call, Throwable t) {
                Toast.makeText(view.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        tenure=service.getTenure();
        tenure.enqueue(new Callback<List<TenureDetail>>() {
            @Override
            public void onResponse(Call<List<TenureDetail>> call, Response<List<TenureDetail>> response) {
                if(response.code()==200){
                    List<String> weeks=new ArrayList<>();
                    for(int i=0;i<response.body().size();i++){
                        weeks.add(response.body().get(i).getTenureTime()+" weeks");
                    }
                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1,weeks);
                    spinner.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<TenureDetail>> call, Throwable t) {

            }
        });
        return view;
    }

    private void init(){

        preferences=view.getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        tvAmt=view.findViewById(R.id.textViewAvailablelimit);
        spinner=view.findViewById(R.id.spinnerTenure);
        retrofit= ApiClient.getInstance();
        service=retrofit.create(ApiInterface.class);



    }
}
