package com.example.administrator.digitalcredit.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.digitalcredit.Interface.AdapterInterface;
import com.example.administrator.digitalcredit.Model.CartBean;
import com.example.administrator.digitalcredit.Model.Product;
import com.example.administrator.digitalcredit.R;
import com.example.administrator.digitalcredit.Utils.Helper;
import com.example.administrator.digitalcredit.adapter.ProductAdapter;
import com.example.administrator.digitalcredit.client.ApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment implements View.OnClickListener {
    private View viewRoot;
    private RecyclerView recyclerView;
    private Helper helper;
    private ProductAdapter adapter;
    private TextView tvItems,tvTotalAmount;
    private AdapterInterface adapterInterface;
    private int items=0;
    private Float rate=0.00f;
    private Button btnCart;
    private ProgressBar progressBar;
    private RelativeLayout layout_body;
    private List<CartBean> list,oldList;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        list=new ArrayList<>();
        oldList=new ArrayList<>();
        viewRoot=inflater.inflate(R.layout.fragment_order, container, false);
        recyclerView=viewRoot.findViewById(R.id.recycler_view);
        tvItems=viewRoot.findViewById(R.id.textViewItems);
        tvTotalAmount=viewRoot.findViewById(R.id.textViewTotalAmount);
        helper=Helper.getInstance();
        btnCart=viewRoot.findViewById(R.id.buttonCart);
        layout_body=viewRoot.findViewById(R.id.relativeLayoutBody);
        progressBar=viewRoot.findViewById(R.id.ProgessBar);

        btnCart.setOnClickListener(this);
        ApiClient.getInstance().getProduct().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful() && response.code()==200){
                    adapter=new ProductAdapter(response.body(),adapterInterface);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(viewRoot.getContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                            ((LinearLayoutManager) mLayoutManager).getOrientation());
                    recyclerView.addItemDecoration(dividerItemDecoration);
                    recyclerView.setAdapter(adapter);
                    if(adapter.getItemCount()>0){
                        progressBar.setVisibility(View.GONE);
                        layout_body.setVisibility(View.VISIBLE);
                    }

                }else{
                    helper.showMesage(viewRoot,"Something went wrong...");
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                helper.showMesage(viewRoot,t.getMessage());
            }
        });



        adapterInterface = new AdapterInterface() {
            @Override
            public void totalItems(String operator) {
                if(operator.equals("plus")) {
                    items++;
                }else{
                    items--;
                }
                tvItems.setText(String.valueOf(items));

            }

            @Override
            public void totalAmount(Float amount, String operator) {
                if(operator.equals("plus")) {
                    rate=rate+amount;
                }else{
                    rate=rate-amount;
                }
                tvTotalAmount.setText("Rs. "+String.valueOf(rate));
            }

            @SuppressLint("NewApi")
            @Override
            public void order(CartBean cartBean) {
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        oldList.add(list.get(i));
                        if(list.get(i).getProductId()==cartBean.getProductId()){
                            list.clear();
                        }
                    }
                    for(int i=0;i<oldList.size();i++){
                        list.add(oldList.get(i));
                    }
                }
                list.add(cartBean);

            }
        };

        return viewRoot;
    }

    @Override
    public void onClick(View v) {
        View child;
        if(v.getId()==R.id.buttonCart){
            for(int i=0;i<recyclerView.getChildCount();i++){
                child=recyclerView.getChildAt(i);
                if(Integer.parseInt(((TextView)child.findViewById(R.id.textViewQty)).getText().toString())>0 &&
                        Double.parseDouble(((TextView)child.findViewById(R.id.textViewAmount)).getText().toString())>0.00){
                  /*  new CartBean(((TextView)child.findViewById(R.id.textViewProductId)).getText().toString(),
                            ((TextView)child.findViewById(R.id.textViewProduct)).getText().toString(),
                            ((TextView)child.findViewById(R.id.textViewProduct)).getText().toString());*/
                    Log.e("recyclerView---->",((TextView)child.findViewById(R.id.textViewAmount)).getText().toString()+","+
                            ((TextView)child.findViewById(R.id.textViewQty)).getText().toString()+","+
                            ((TextView)child.findViewById(R.id.textViewProduct)).getText().toString());
                }

            }
            /*Log.e("Bean---->", list.toString());
            list.clear();
            layout_body.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            ApiClient.getInstance().getProduct().clone()
                    .enqueue(new Callback<List<Product>>() {
                        @Override
                        public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                            if(response.isSuccessful() && response.code()==200){


                                adapter=new ProductAdapter(response.body(),adapterInterface);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(viewRoot.getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                                        ((LinearLayoutManager) mLayoutManager).getOrientation());
                                recyclerView.addItemDecoration(dividerItemDecoration);
                                recyclerView.setAdapter(adapter);
                                if(adapter.getItemCount()>0){
                                    progressBar.setVisibility(View.GONE);
                                    layout_body.setVisibility(View.VISIBLE);
                                    tvItems.setText("0");
                                    tvTotalAmount.setText("Rs. 0.00");
                                }



                            }else{
                                helper.showMesage(viewRoot,"Something went wrong...");
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Product>> call, Throwable t) {
                            helper.showMesage(viewRoot,t.getMessage());
                        }
                    });*/

        }
    }
}
