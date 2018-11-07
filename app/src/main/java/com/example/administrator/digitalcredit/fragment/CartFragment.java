package com.example.administrator.digitalcredit.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import com.example.administrator.digitalcredit.Model.OrderDetail;
import com.example.administrator.digitalcredit.Model.OrderDetailResponse;
import com.example.administrator.digitalcredit.R;
import com.example.administrator.digitalcredit.Utils.Helper;
import com.example.administrator.digitalcredit.client.ApiClient;
import com.example.administrator.digitalcredit.dialog.DialogHistory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment {
    private ViewGroup viewRoot;
    private Helper helper;
    private TableLayout tableLayout;
    private DialogFragment dialog;
    private ProgressDialog pd;
    private OrderDetailResponse list;
    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        helper=Helper.getInstance();
        viewRoot=(ViewGroup) inflater.inflate(R.layout.fragment_cart, container, false);
        init();

        return viewRoot;
    }

    // TODO: Rename method, update argument and hook method into UI event




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public void getMessage(OrderDetailResponse orderDetailResponse){
        Log.e(this.getClass().getName(),orderDetailResponse.toString());
       // populateTable(orderDetailResponse);
    }


    private void init(){
        tableLayout=viewRoot.findViewById(R.id.tableLayout);
        helper=Helper.getInstance();
        dialog=new DialogHistory();
        pd=helper.showDialog(viewRoot.getContext(),"Loading","Please wait...");
    }

    private void populateTable(OrderDetailResponse orderDetailResponse){
        for (OrderDetail orderDetail : orderDetailResponse.getOrderDetail()) {
            TableRow row = new TableRow(viewRoot.getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

            final TextView tvId = new TextView(viewRoot.getContext());
            tvId.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvId.setTextSize(15f);
            tvId.setGravity(Gravity.CENTER_HORIZONTAL);
            tvId.setText(String.valueOf(orderDetail.getOrderDetailId()));
            row.addView(tvId);



            TextView tvName = new TextView(viewRoot.getContext());
            tvName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvName.setTextSize(15f);
            tvName.setGravity(Gravity.CENTER_HORIZONTAL);
            tvName.setText(orderDetail.getProductName());
//                        tvDueDate.setWidth(100);
            row.addView(tvName);

            TextView tvQty = new TextView(viewRoot.getContext());
            tvQty.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvQty.setTextSize(15f);
            tvQty.setGravity(Gravity.CENTER_HORIZONTAL);
            tvQty.setText(String.valueOf(orderDetail.getQty()));
            row.addView(tvQty);

            TextView tvAmt = new TextView(viewRoot.getContext());
            tvAmt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvAmt.setTextSize(15f);
            tvAmt.setGravity(Gravity.CENTER_HORIZONTAL);
            tvAmt.setText(String.valueOf(orderDetail.getQty()*orderDetail.getPrice()));
            row.addView(tvAmt);

            TextView tvDate = new TextView(viewRoot.getContext());
            tvDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvDate.setTextSize(15f);
            tvDate.setGravity(Gravity.CENTER_HORIZONTAL);
            tvDate.setText(orderDetail.getCreatedAt());
            row.addView(tvDate);
            row.setPadding(5,5,5,5);
            tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }
}
