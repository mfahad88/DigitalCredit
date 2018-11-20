package com.example.administrator.digitalcredit.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.digitalcredit.Model.CreateProduct;
import com.example.administrator.digitalcredit.Model.LoanDetail;
import com.example.administrator.digitalcredit.Model.Product;
import com.example.administrator.digitalcredit.R;
import com.example.administrator.digitalcredit.Utils.DecimalDigitsInputFilter;
import com.example.administrator.digitalcredit.Utils.Helper;
import com.example.administrator.digitalcredit.client.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends Fragment implements View.OnClickListener {
    private View viewRoot;
    private EditText edtTextName,edtTextPrice,edtTextDesc;
    private Button btn_add;
    private Helper helper;
    private ProgressBar bar;
    private TableLayout tableLayout;
    private LinearLayout layout;
    public ProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot=inflater.inflate(R.layout.fragment_product, container, false);
        init();
        populateTable();
//        pd=helper.showDialog(viewRoot.getContext(),"Loading...","Please wait while product creation is in progress");
        edtTextPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        btn_add.setOnClickListener(this);
        return viewRoot;

    }

    @Override
    public void onClick(View v) {
        try {
            if(v.getId()==R.id.buttonAdd){
                btn_add.setEnabled(false);
                if((!TextUtils.isEmpty(edtTextName.getText().toString())) && (!TextUtils.isEmpty(edtTextPrice.getText().toString())) && (!TextUtils.isEmpty(edtTextDesc.getText().toString()))){
                    bar.setVisibility(View.VISIBLE);
                    CreateProduct product=new CreateProduct();
                    product.setProduct_name(edtTextName.getText().toString());
                    product.setPrice(Float.parseFloat(edtTextPrice.getText().toString()));
                    product.setDesc(edtTextDesc.getText().toString());

                    ApiClient.getInstance().createProduct(product)
                            .enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                    if(response.code()==200 || response.isSuccessful()){
                                        btn_add.setEnabled(true);
                                        helper.showMesage(getActivity().getWindow().getDecorView(),"Product Created...");
                                        edtTextName.setText("");
                                        edtTextPrice.setText("");
                                        edtTextDesc.setText("");
                                        populateTable();
                                    }else{
                                        helper.showMesage(getActivity().getWindow().getDecorView(),"Something went wrong...");
                                        if(bar.isShown()){
                                            bar.setVisibility(View.GONE);
                                        }
                                        edtTextName.setText("");
                                        edtTextPrice.setText("");
                                        edtTextDesc.setText("");
                                        btn_add.setEnabled(true);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {
                                    btn_add.setEnabled(true);
                                    if(bar.isShown()){
                                        bar.setVisibility(View.GONE);

                                    }
                                    t.printStackTrace();
                                    helper.showMesage(getActivity().getWindow().getDecorView(),t.getMessage());
                                }
                            });


                }else{
                    helper.showMesage(getActivity().getWindow().getDecorView(),"Empty fields not allowed...");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void init(){
        edtTextName=viewRoot.findViewById(R.id.editTextName);
        edtTextPrice=viewRoot.findViewById(R.id.editTextPrice);
        edtTextDesc=viewRoot.findViewById(R.id.editTextdesc);
        btn_add=viewRoot.findViewById(R.id.buttonAdd);
        bar=viewRoot.findViewById(R.id.progress_bar);
        tableLayout=viewRoot.findViewById(R.id.tableLayout);
        layout=viewRoot.findViewById(R.id.linearProduct);
        helper=Helper.getInstance();
    }

    private void populateTable(){
        try {
            bar.setVisibility(View.VISIBLE);
            helper.cleanTable(tableLayout);
            ApiClient.getInstance().getProduct().enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    if(response.code()==200 || response.isSuccessful()){
                        for(Product product:response.body()){
                            TableRow row = new TableRow(viewRoot.getContext());
                            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                            TextView tvId=new TextView(viewRoot.getContext());
                            tvId.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvId.setTextSize(15f);
                            tvId.setGravity(Gravity.CENTER_HORIZONTAL);
                            tvId.setText(String.valueOf(product.getProductId()));
                            row.addView(tvId);

                            TextView tvName=new TextView(viewRoot.getContext());
                            tvName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvName.setTextSize(15f);
                            tvName.setWidth(330);
                            tvName.setGravity(Gravity.START);
                            tvName.setText(product.getProductName());
                            row.addView(tvName);

                            TextView tvRate=new TextView(viewRoot.getContext());
                            tvRate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvRate.setTextSize(15f);
                            tvRate.setGravity(Gravity.END);
                            tvRate.setText(String.valueOf(product.getPrice()));
                            row.addView(tvRate);
                            row.setPadding(5,5,5,5);
                            tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                        }
                    }else{
                        helper.showMesage(getActivity().getWindow().getDecorView(),"Something went wrong...");
                    }
                    if(bar.isShown()){
                        bar.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    t.printStackTrace();
                    helper.showMesage(getActivity().getWindow().getDecorView(),t.getMessage());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
