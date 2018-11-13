package com.example.administrator.digitalcredit.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.digitalcredit.Model.OrderDetail;
import com.example.administrator.digitalcredit.Model.OrderDetailResponse;
import com.example.administrator.digitalcredit.Model.Transaction;
import com.example.administrator.digitalcredit.Model.TransactionRequest;
import com.example.administrator.digitalcredit.R;
import com.example.administrator.digitalcredit.Utils.Helper;
import com.example.administrator.digitalcredit.activity.DistributorActivity;
import com.example.administrator.digitalcredit.client.ApiClient;
import com.example.administrator.digitalcredit.dialog.DialogPayment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DistributorFragment extends Fragment implements View.OnClickListener {
    private Button btnInquiry,buttonCollect;
    private EditText edtTextOrderId;
    private TextView textViewItems,textViewTotalAmount;
    private TableLayout tableLayout;
    private Helper helper;
    private ProgressBar bar;
    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout;
    private View viewRoot;
    public DistributorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot=inflater.inflate(R.layout.fragment_distributor, container, false);
        init();
        btnInquiry.setOnClickListener(this);
        buttonCollect.setOnClickListener(this);
        return viewRoot;
    }

    private void init(){
        btnInquiry=viewRoot.findViewById(R.id.buttonInquiry);
        edtTextOrderId=viewRoot.findViewById(R.id.editTextOrderId);
        textViewItems=viewRoot.findViewById(R.id.textViewItems);
        textViewTotalAmount=viewRoot.findViewById(R.id.textViewTotalAmount);
        buttonCollect=viewRoot.findViewById(R.id.buttonCollect);
        tableLayout=viewRoot.findViewById(R.id.tableLayout);
        helper=Helper.getInstance();
        bar=viewRoot.findViewById(R.id.progress_bar);
        relativeLayout=viewRoot.findViewById(R.id.relativeBody);
        linearLayout=viewRoot.findViewById(R.id.linearFooter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonInquiry){
            helper.cleanTable(tableLayout);
            if(relativeLayout.isShown()){
                relativeLayout.setVisibility(View.GONE);
            }
            if(linearLayout.isShown()){
                linearLayout.setVisibility(View.GONE);
            }
            if(buttonCollect.isShown()){
                buttonCollect.setVisibility(View.GONE);
            }
            bar.setVisibility(View.VISIBLE);

            ApiClient.getInstance().orderInquiry(edtTextOrderId.getText().toString())
                    .enqueue(new Callback<OrderDetailResponse>() {
                        @Override
                        public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                            if(response.code()==200 || response.isSuccessful()){
                                populateTable(edtTextOrderId.getText().toString());
                                textViewItems.setText(String.valueOf(response.body().getOrder().getTotalItem()));
                                textViewTotalAmount.setText(String.valueOf(response.body().getOrder().getTotalAmt()));
                                linearLayout.setVisibility(View.VISIBLE);
                                buttonCollect.setVisibility(View.VISIBLE);

                            }else{
                                Toast.makeText(viewRoot.getContext(), "OrderId not found...", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                            Toast.makeText(viewRoot.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            TransactionRequest request=new TransactionRequest();
            Transaction transaction=new Transaction();
            transaction.setDebit_user_id(Integer.parseInt(helper.getSession(viewRoot.getContext()).get("user_id").toString()));
            transaction.setAmt(Float.parseFloat(textViewTotalAmount.getText().toString()));
            request.setTraction(transaction);
            ApiClient.getInstance().trasactionCash(request)
                    .enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if(response.code()==200 || response.isSuccessful()){
                                DialogPayment dialogPayment=new DialogPayment();
                                dialogPayment.show(getFragmentManager(),"PaymentDialog");
                                helper.cleanTable(tableLayout);
                                edtTextOrderId.setText("");
                                textViewItems.setText("");
                                textViewTotalAmount.setText("");

                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Toast.makeText(viewRoot.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void populateTable(String Id){
        ApiClient.getInstance().orderInquiry(Id)
                .enqueue(new Callback<OrderDetailResponse>() {
                    @Override
                    public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                        if(response.code()==200 || response.isSuccessful()){
                            for(OrderDetail detail:response.body().getOrderDetail()){
                                bar.setVisibility(View.GONE);
                                relativeLayout.setVisibility(View.VISIBLE);
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


                        }
                    }

                    @Override
                    public void onFailure(Call<OrderDetailResponse> call, Throwable t) {

                    }
                });
    }
}
