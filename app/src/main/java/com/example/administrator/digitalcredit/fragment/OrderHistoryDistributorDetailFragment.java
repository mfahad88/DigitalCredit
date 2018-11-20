package com.example.administrator.digitalcredit.fragment;


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
import com.example.administrator.digitalcredit.Utils.Helper;
import com.example.administrator.digitalcredit.R;
import com.example.administrator.digitalcredit.client.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderHistoryDistributorDetailFragment extends Fragment {
    private View viewRoot;
    private TableLayout tableLayout;
    private Helper helper;
    private ProgressBar bar;
    private CardView cardView;
    private TextView tvAgentId,tvAgentName,tvAgentCNIC,tvAgentMobile;
    private LinearLayout layout;
    public OrderHistoryDistributorDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot=inflater.inflate(R.layout.fragment_order_history_distributor_detail, container, false);
        tableLayout=viewRoot.findViewById(R.id.tableLayout);
        helper=Helper.getInstance();
        bar=viewRoot.findViewById(R.id.progress_bar);
        tvAgentId=viewRoot.findViewById(R.id.textViewAgentId);
        tvAgentName=viewRoot.findViewById(R.id.textViewAgentName);
        tvAgentCNIC=viewRoot.findViewById(R.id.textViewAgentCNIC);
        tvAgentMobile=viewRoot.findViewById(R.id.textViewAgentMobile);
        layout=viewRoot.findViewById(R.id.linearBody);
        cardView=viewRoot.findViewById(R.id.cardView);
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
                                tvAgentName.setText(response.body().getOrder().getName());
                                tvAgentCNIC.setText(response.body().getOrder().getCnic());
                                tvAgentId.setText(String.valueOf(response.body().getOrder().getUserId()));
                                tvAgentMobile.setText(response.body().getOrder().getMobile_no());
                                for(OrderDetail detail:response.body().getOrderDetail()){
                                    bar.setVisibility(View.GONE);

                                    cardView.setVisibility(View.VISIBLE);
                                    layout.setVisibility(View.VISIBLE);
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

                                    TextView tvType = new TextView(viewRoot.getContext());
                                    tvType.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tvType.setTextSize(15f);
                                    tvType.setGravity(Gravity.START);
                                    if(response.body().getOrder().getOrderType().equalsIgnoreCase("L")) {
                                        tvType.setText("Loan");
                                    }if(response.body().getOrder().getOrderType().equalsIgnoreCase("C")){
                                        tvType.setText("Cash");
                                    }
                                    row.addView(tvType);

                                    row.setPadding(5,5,5,5);
                                    tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                                }

                            }catch (Exception e){
                                e.printStackTrace();
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

}
