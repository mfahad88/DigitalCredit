package com.example.administrator.digitalcredit.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.digitalcredit.Model.Product;
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
public class OrderFragment extends Fragment {
    private View viewRoot;
    private RecyclerView recyclerView;
    private List<Product> list;
    private Helper helper;
    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot=inflater.inflate(R.layout.fragment_order, container, false);
        recyclerView=viewRoot.findViewById(R.id.recycler_view);
        helper=Helper.getInstance();
        ApiClient.getInstance().getProduct().enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if(response.isSuccessful() && response.code()==200){

                }else{
                    helper.showMesage(viewRoot,"Something went wrong...");
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {

            }
        });
        return viewRoot;
    }

}
