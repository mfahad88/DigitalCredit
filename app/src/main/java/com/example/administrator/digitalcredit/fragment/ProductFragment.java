package com.example.administrator.digitalcredit.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.digitalcredit.Model.CreateProduct;
import com.example.administrator.digitalcredit.R;
import com.example.administrator.digitalcredit.Utils.Helper;
import com.example.administrator.digitalcredit.client.ApiClient;

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
    private ProgressDialog pd;
    public ProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot=inflater.inflate(R.layout.fragment_product, container, false);
        init();
        pd=helper.showDialog(viewRoot.getContext(),"Loading...","Please wait while product creation is in progress");
        btn_add.setOnClickListener(this);
        return viewRoot;

    }

    @Override
    public void onClick(View v) {
        try {
            if(v.getId()==R.id.buttonAdd){
                btn_add.setEnabled(false);
                if((!TextUtils.isEmpty(edtTextName.getText().toString())) && (!TextUtils.isEmpty(edtTextPrice.getText().toString())) && (!TextUtils.isEmpty(edtTextDesc.getText().toString()))){
                    pd.show();
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
                                        if(pd.isShowing()){
                                            pd.dismiss();
                                        }
                                        helper.showMesage(getActivity().getWindow().getDecorView(),"Product Created...");
                                        edtTextName.setText("");
                                        edtTextPrice.setText("");
                                        edtTextDesc.setText("");
                                    }else{
                                        helper.showMesage(getActivity().getWindow().getDecorView(),"Something went wrong...");
                                        if(pd.isShowing()){
                                            pd.dismiss();
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
                                    if(pd.isShowing()){
                                        pd.dismiss();
                                    }
                                    t.printStackTrace();
                                    helper.showMesage(getActivity().getWindow().getDecorView(),t.getMessage());
                                }
                            });


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
        helper=Helper.getInstance();
    }
}
