package com.example.administrator.digitalcredit.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.administrator.digitalcredit.client.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DistributorActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnInquiry,buttonCollect;
    private EditText edtTextOrderId;
    private TextView textViewItems,textViewTotalAmount;
    private TableLayout tableLayout;
    private Helper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor);
        init();
        btnInquiry.setOnClickListener(this);
        buttonCollect.setOnClickListener(this);
    }

    private void init(){
        btnInquiry=findViewById(R.id.buttonInquiry);
        edtTextOrderId=findViewById(R.id.editTextOrderId);
        textViewItems=findViewById(R.id.textViewItems);
        textViewTotalAmount=findViewById(R.id.textViewTotalAmount);
        buttonCollect=findViewById(R.id.buttonCollect);
        tableLayout=findViewById(R.id.tableLayout);
        helper=Helper.getInstance();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonInquiry){
            ApiClient.getInstance().orderInquiry(edtTextOrderId.getText().toString())
                    .enqueue(new Callback<OrderDetailResponse>() {
                        @Override
                        public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                            if(response.code()==200 || response.isSuccessful()){
                                textViewItems.setText(String.valueOf(response.body().getOrder().getTotalItem()));
                                textViewTotalAmount.setText(String.valueOf(response.body().getOrder().getTotalAmt()));
                                populateTable(edtTextOrderId.getText().toString());
                            }else{
                                Toast.makeText(DistributorActivity.this, "OrderId not found...", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                            Toast.makeText(DistributorActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            TransactionRequest request=new TransactionRequest();
            Transaction transaction=new Transaction();
            transaction.setDebit_user_id(Integer.parseInt(helper.getSession(getApplicationContext()).get("user_id").toString()));
            transaction.setAmt(Float.parseFloat(textViewTotalAmount.getText().toString()));
            request.setTraction(transaction);
            ApiClient.getInstance().trasactionCash(request)
                    .enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if(response.code()==200 || response.isSuccessful()){
                                helper.cleanTable(tableLayout);
                                edtTextOrderId.setText("");
                                textViewItems.setText("");
                                textViewTotalAmount.setText("");
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Toast.makeText(DistributorActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Log.e("OrderDetail---->",detail.getProductName());
                                TableRow row = new TableRow(getApplicationContext());
                                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                final TextView tvName = new TextView(getApplicationContext());
                                tvName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                tvName.setTextSize(15f);
                                tvName.setGravity(Gravity.CENTER_HORIZONTAL);
                                tvName.setText(String.valueOf(detail.getProductName()));
                                row.addView(tvName);



                                TextView tvQty = new TextView(getApplicationContext());
                                tvQty.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                tvQty.setTextSize(15f);
                                tvQty.setGravity(Gravity.CENTER_HORIZONTAL);
                                tvQty.setText(String.valueOf(detail.getQty()));
//                        tvDueDate.setWidth(100);
                                row.addView(tvQty);

                                TextView tvPrice = new TextView(getApplicationContext());
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
