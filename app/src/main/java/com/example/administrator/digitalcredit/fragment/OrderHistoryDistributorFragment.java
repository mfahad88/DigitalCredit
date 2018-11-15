package com.example.administrator.digitalcredit.fragment;


import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.administrator.digitalcredit.Model.OrderHistoryResponse;
import com.example.administrator.digitalcredit.Utils.Helper;
import com.example.administrator.digitalcredit.R;
import com.example.administrator.digitalcredit.client.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderHistoryDistributorFragment extends Fragment {
    private TableLayout tableLayout;
    private View viewRoot;
    private Helper helper;
    private ProgressBar bar;
    private CardView cardView;

    public OrderHistoryDistributorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot=inflater.inflate(R.layout.fragment_order_history_distributor, container, false);
        init();
        populateTable(helper.getSession(viewRoot.getContext()).get("user_id").toString());
        return viewRoot;
    }

    public void init(){
        helper=Helper.getInstance();
        tableLayout=viewRoot.findViewById(R.id.tableLayout);
        bar=viewRoot.findViewById(R.id.progress_bar);
        cardView=viewRoot.findViewById(R.id.cardView);
    }

    private void populateTable(String Id){
       try{
           ApiClient.getInstance().orderHistory("fk_distributer_id="+Id)
                   .enqueue(new Callback<List<OrderHistoryResponse>>() {
                       @Override
                       public void onResponse(Call<List<OrderHistoryResponse>> call, Response<List<OrderHistoryResponse>> response) {
                           if(response.code()==200 || response.isSuccessful()){
                               for(final OrderHistoryResponse historyResponse:response.body()){
                                   bar.setVisibility(View.GONE);
                                   cardView.setVisibility(View.VISIBLE);
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
                               helper.showMesage(getActivity().getWindow().getDecorView(),"Something went wrong...");
                           }
                       }

                       @Override
                       public void onFailure(Call<List<OrderHistoryResponse>> call, Throwable t) {
                           t.printStackTrace();
                           helper.showMesage(getActivity().getWindow().getDecorView(),t.getMessage());
                       }
                   });
       }catch (Exception e){
           e.printStackTrace();
       }
    }
}
