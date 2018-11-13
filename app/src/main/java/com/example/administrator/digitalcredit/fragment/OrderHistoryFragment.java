package com.example.administrator.digitalcredit.fragment;


import android.graphics.Color;
import android.graphics.Paint;
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

import com.example.administrator.digitalcredit.Model.LoanDetail;
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
public class OrderHistoryFragment extends Fragment {
    private TableLayout tableLayout;
    private View viewRoot;
    private Helper helper;
    public OrderHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot=inflater.inflate(R.layout.fragment_order_history, container, false);
        helper=Helper.getInstance();
        tableLayout=viewRoot.findViewById(R.id.tableLayout);
        populateTable(helper.getSession(viewRoot.getContext()).get("user_id").toString());
        Log.e("ORderHIstory--->",helper.getSession(viewRoot.getContext()).get("user_id").toString());
        return viewRoot;
    }


    private void populateTable(String Id){
        ApiClient.getInstance().orderHistory("user_id="+Id)
              .enqueue(new Callback<List<OrderHistoryResponse>>() {
                  @Override
                  public void onResponse(Call<List<OrderHistoryResponse>> call, Response<List<OrderHistoryResponse>> response) {
                      if(response.code()==200 || response.isSuccessful()){
                          for(final OrderHistoryResponse historyResponse:response.body()){
                              TableRow row = new TableRow(viewRoot.getContext());
                              row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                              final TextView tvId = new TextView(viewRoot.getContext());
                              tvId.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                              tvId.setTextSize(15f);
                              tvId.setGravity(Gravity.CENTER_HORIZONTAL);
                              tvId.setText(String.valueOf(historyResponse.getOrderId()));
                              row.addView(tvId);
                              tvId.setClickable(true);
                              tvId.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                              tvId.setTextColor(Color.argb(255,0,0,255));
                              tvId.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View view) {
                                    Bundle bundle=new Bundle();
                                    bundle.putInt("OrderId",historyResponse.getOrderId());
                                    OrderHistoryDetailFragment fragment=new OrderHistoryDetailFragment();
                                    fragment.setArguments(bundle);
                                    getFragmentManager().beginTransaction().replace(R.id.view_container,fragment).commit();
                                  }
                              });


                              TextView tvCreatedDate = new TextView(viewRoot.getContext());
                              tvCreatedDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                              tvCreatedDate.setTextSize(15f);
                              tvCreatedDate.setGravity(Gravity.CENTER_HORIZONTAL);
                              tvCreatedDate.setText(historyResponse.getCreatedAt());
//                        tvDueDate.setWidth(100);
                              row.addView(tvCreatedDate);

                              TextView tvItems = new TextView(viewRoot.getContext());
                              tvItems.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                              tvItems.setTextSize(15f);
                              tvItems.setGravity(Gravity.CENTER_HORIZONTAL);
                              tvItems.setText(String.valueOf(historyResponse.getTotalItem()));
                              row.addView(tvItems);

                              TextView tvAmt = new TextView(viewRoot.getContext());
                              tvAmt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                              tvAmt.setTextSize(15f);
                              tvAmt.setGravity(Gravity.CENTER_HORIZONTAL);
                              tvAmt.setText(String.valueOf(historyResponse.getTotalAmt()));
                              row.addView(tvAmt);

                              row.setPadding(5,5,5,5);
                              tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                          }

                      }else {
                          helper.showMesage(viewRoot.getRootView(),"Something went wrong...");
                      }
                  }

                  @Override
                  public void onFailure(Call<List<OrderHistoryResponse>> call, Throwable t) {

                  }
              });
    }
}
