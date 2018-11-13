package com.example.administrator.digitalcredit.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.digitalcredit.Model.OrderDetail;
import com.example.administrator.digitalcredit.Model.OrderDetailResponse;
import com.example.administrator.digitalcredit.R;
import com.example.administrator.digitalcredit.Utils.Helper;
import com.example.administrator.digitalcredit.client.ApiClient;
import com.example.administrator.digitalcredit.dialog.DialogPay;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartDetailFragment extends Fragment implements View.OnClickListener ,RadioGroup.OnCheckedChangeListener{
    private View viewRoot;
    private TableLayout tableLayout;
    private Helper helper;
    private ProgressBar bar;
    private CardView cardView;
    private RadioGroup radioGroup;
    private RadioButton radioCash,radioLoan;
    private Button btnPay;
    private int radioStatus=1;
    private OrderDetailResponse list;
    public CartDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot=inflater.inflate(R.layout.fragment_cart_detail, container, false);
        tableLayout=viewRoot.findViewById(R.id.tableLayout);
        helper=Helper.getInstance();
        bar=viewRoot.findViewById(R.id.progress_bar);
        cardView=viewRoot.findViewById(R.id.cardView);
        radioGroup=viewRoot.findViewById(R.id.radioGroup);
        radioCash=viewRoot.findViewById(R.id.radioCash);
        radioLoan=viewRoot.findViewById(R.id.radioLoan);
        btnPay=viewRoot.findViewById(R.id.payBtn);
        if(getArguments()!=null){
            populateTable(String.valueOf(getArguments().getInt("OrderId")));
        }
        btnPay.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        return viewRoot;
    }

    private void populateTable(String Id){
        ApiClient.getInstance().orderInquiry(Id)
                .enqueue(new Callback<OrderDetailResponse>() {
                    @Override
                    public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                        if(response.code()==200 || response.isSuccessful()){
                            list=response.body();
                            for(OrderDetail detail:response.body().getOrderDetail()){
                                bar.setVisibility(View.GONE);
                                cardView.setVisibility(View.VISIBLE);
                                Log.e("OrderDetail---->",detail.getProductName());
                                TableRow row = new TableRow(viewRoot.getContext());
                                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                final TextView tvName = new TextView(viewRoot.getContext());
                                tvName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                tvName.setTextSize(15f);
                                tvName.setGravity(Gravity.CENTER_HORIZONTAL);
                                tvName.setText(String.valueOf(detail.getProductName()));
                                row.addView(tvName);



                                TextView tvQty = new TextView(viewRoot.getContext());
                                tvQty.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                tvQty.setTextSize(15f);
                                tvQty.setGravity(Gravity.CENTER_HORIZONTAL);
                                tvQty.setText(String.valueOf(detail.getQty()));
//                        tvDueDate.setWidth(100);
                                row.addView(tvQty);

                                TextView tvPrice = new TextView(viewRoot.getContext());
                                tvPrice.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                tvPrice.setTextSize(15f);
                                tvPrice.setGravity(Gravity.CENTER_HORIZONTAL);
                                tvPrice.setText(String.valueOf(detail.getPrice()));
                                row.addView(tvPrice);



                                row.setPadding(5,5,5,5);
                                tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                            }


                        }else {
                            helper.showMesage(getActivity().getWindow().getDecorView(),"Something went wrong...");
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                        t.printStackTrace();
                        helper.showMesage(getActivity().getWindow().getDecorView(),t.getMessage());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.payBtn){

            if(radioStatus==1){
                DialogFragment dialog=new DialogPay();
                Bundle bundle=new Bundle();

                bundle.putInt("OrderId",list.getOrder().getOrderId());
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(),"PayByCash");

//              Toast.makeText(viewRoot.getContext(), "Pay by Cash", Toast.LENGTH_SHORT).show();
            }else{
                AvailLoanFragment availLoanFragment=new AvailLoanFragment();
                Bundle bundle=new Bundle();
                bundle.putFloat("totalAmount",list.getOrder().getTotalAmt());
                bundle.putInt("orderId",list.getOrder().getOrderId());
                availLoanFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.view_container,availLoanFragment).commit();
                // Toast.makeText(viewRoot.getContext(), "Pay by Loan", Toast.LENGTH_SHORT).show();
            }


        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId==R.id.radioCash){
            radioStatus=1;
        }else{
            radioStatus=2;
        }
    }
}
