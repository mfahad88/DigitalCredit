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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.digitalcredit.Model.OrderDetail;
import com.example.administrator.digitalcredit.Model.OrderDetailResponse;
import com.example.administrator.digitalcredit.R;
import com.example.administrator.digitalcredit.dialog.DialogPay;


public class CartFragment extends Fragment implements View.OnClickListener ,RadioGroup.OnCheckedChangeListener {
    private View viewRoot;
    private TableLayout tableLayout;
    private OrderDetailResponse list;
    private TextView tvItems,tvTotal;
    private RadioGroup radioGroup;
    private RadioButton radioCash,radioLoan;
    private Button btnPay;
    private int radioStatus=1;
    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        viewRoot=inflater.inflate(R.layout.fragment_cart, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Cart Details");
        init();

         populateTable(list);
         tvItems.setText(String.valueOf(list.getOrder().getTotalItem()));
         tvTotal.setText(String.valueOf(list.getOrder().getTotalAmt()));
         btnPay.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);

        //Toast.makeText(viewRoot.getContext(), list.toString(), Toast.LENGTH_SHORT).show();
        return viewRoot;
    }


    public void getMessage(OrderDetailResponse orderDetailResponse){
        Log.e(this.getClass().getName(),orderDetailResponse.toString());
        list=orderDetailResponse;
       // populateTable(orderDetailResponse);
    }


    private void init(){
        tableLayout=viewRoot.findViewById(R.id.tableLayout);
        radioGroup=viewRoot.findViewById(R.id.radioGroup);
        radioCash=viewRoot.findViewById(R.id.radioCash);
        radioLoan=viewRoot.findViewById(R.id.radioLoan);
        btnPay=viewRoot.findViewById(R.id.payBtn);
        radioCash.setChecked(true);
        tvItems=viewRoot.findViewById(R.id.textViewItems);
        tvTotal=viewRoot.findViewById(R.id.textViewTotalAmount);
    }

    private void populateTable(OrderDetailResponse orderDetailResponse){
        for (OrderDetail orderDetail : orderDetailResponse.getOrderDetail()) {
            TableRow row = new TableRow(viewRoot.getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

            final TextView tvId = new TextView(viewRoot.getContext());
            tvId.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvId.setTextSize(15f);
            tvId.setGravity(Gravity.START);
            tvId.setText(String.valueOf(orderDetail.getOrderDetailId()));
            row.addView(tvId);



            TextView tvName = new TextView(viewRoot.getContext());
            tvName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvName.setTextSize(15f);
            tvName.setGravity(Gravity.CENTER_HORIZONTAL);
            tvName.setText(orderDetail.getProductName());
            tvName.setPadding(10,0,10,0);
            tvName.setWidth(100);
            row.addView(tvName);

            TextView tvUnitPrice = new TextView(viewRoot.getContext());
            tvUnitPrice.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvUnitPrice.setTextSize(15f);
            tvUnitPrice.setGravity(Gravity.CENTER_HORIZONTAL);
            tvUnitPrice.setText(orderDetail.getPrice().toString());
            tvUnitPrice.setWidth(100);
            row.addView(tvUnitPrice);
            row.setPadding(10,10,10,10);

            TextView tvQty = new TextView(viewRoot.getContext());
            tvQty.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvQty.setTextSize(15f);
            tvQty.setGravity(Gravity.CENTER_HORIZONTAL);
            tvQty.setWidth(80);
            tvQty.setText(String.valueOf(orderDetail.getQty()));
            row.addView(tvQty);

            TextView tvAmt = new TextView(viewRoot.getContext());
            tvAmt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvAmt.setTextSize(15f);
            tvAmt.setGravity(Gravity.CENTER_HORIZONTAL);
            tvAmt.setText(String.valueOf(orderDetail.getQty()*orderDetail.getPrice()));
            row.addView(tvAmt);


            tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
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
