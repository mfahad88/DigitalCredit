package com.example.administrator.digitalcredit.fragment;


import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.administrator.digitalcredit.Model.OrderDetail;
import com.example.administrator.digitalcredit.Model.OrderDetailResponse;
import com.example.administrator.digitalcredit.R;
import com.example.administrator.digitalcredit.Utils.Helper;
import com.example.administrator.digitalcredit.client.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderHistoryDetailFragment extends Fragment {

    private View viewRoot;
    private TableLayout tableLayout;
    private Helper helper;
    private ProgressBar bar;
    private CardView cardView;
    private TextView tvOrderId,tvDistributor,tvMode,tvStatus,tvLoanId;
    private LinearLayout linearLayoutHeader,linearLayoutLoan;
    public OrderHistoryDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot=inflater.inflate(R.layout.fragment_order_history_detail, container, false);
        tableLayout=viewRoot.findViewById(R.id.tableLayout);
        helper=Helper.getInstance();
        bar=viewRoot.findViewById(R.id.progress_bar);
        cardView=viewRoot.findViewById(R.id.cardView);
        linearLayoutHeader=viewRoot.findViewById(R.id.linearHeader);
        tvOrderId=viewRoot.findViewById(R.id.textViewOrderId);
        tvDistributor=viewRoot.findViewById(R.id.textViewDistributor);
        tvMode=viewRoot.findViewById(R.id.textViewMode);
        tvStatus=viewRoot.findViewById(R.id.textViewStatus);
        tvLoanId=viewRoot.findViewById(R.id.textViewLoanId);
        linearLayoutLoan=viewRoot.findViewById(R.id.linearLoanId);
        if(getArguments()!=null){
            populateTable(String.valueOf(getArguments().getInt("OrderId")));
        }
        return viewRoot;
    }

    private void populateTable(String Id){
        ApiClient.getInstance().orderInquiry(Id)
                .enqueue(new Callback<OrderDetailResponse>() {
                    @Override
                    public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                        if(response.code()==200 || response.isSuccessful()){
                          try {

                              tvOrderId.setText(String.valueOf(response.body().getOrder().getOrderId()));
                              if(response.body().getDetail()!=null) {
                                  tvDistributor.setText(response.body().getDetail().getUserName());
                              }else{
                                  tvDistributor.setText("N/A");
                              }
                              if(response.body().getOrder().getOrderType().equalsIgnoreCase("C")){
                                  tvMode.setText("CASH");
                              }else if(response.body().getOrder().getOrderType().equalsIgnoreCase("L")){
                                  tvMode.setText("LOAN");
                              }
                              if(response.body().getOrder().getOrderStatus().equalsIgnoreCase("A")){
                                tvStatus.setText("In Cart");
                              }else{
                                  tvStatus.setText(response.body().getOrder().getOrderStatus());
                              }if(response.body().getOrder().getFkLoanId()>0){
                                  linearLayoutLoan.setVisibility(View.VISIBLE);
                                  tvLoanId.setText(String.valueOf(response.body().getOrder().getFkLoanId()));
                              }
                              /*if(response.body().getOrder().getOrderStatus().equalsIgnoreCase("U")){
                                  tvStatus.setText("UnDelivered");
                              }else if(response.body().getOrder().getOrderStatus().equalsIgnoreCase("Delivered")){
                                  tvStatus.setText("Delivered");
                              }*/
                              for(OrderDetail detail:response.body().getOrderDetail()){
                                  bar.setVisibility(View.GONE);
                                  linearLayoutHeader.setVisibility(View.VISIBLE);
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

                          }catch (Exception e){
                              e.printStackTrace();
                          }

                        }else {
                            helper.showMesage(viewRoot,"Something went wrong...");
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                        t.printStackTrace();
                        helper.showMesage(viewRoot,t.getMessage());
                    }
                });
    }
}
