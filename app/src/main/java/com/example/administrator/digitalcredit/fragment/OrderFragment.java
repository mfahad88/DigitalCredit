package com.example.administrator.digitalcredit.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import com.example.administrator.digitalcredit.Interface.CartInterface;
import com.example.administrator.digitalcredit.Model.CartBean;
import com.example.administrator.digitalcredit.Model.OrderDetail;
import com.example.administrator.digitalcredit.Model.OrderDetailResponse;
import com.example.administrator.digitalcredit.Model.OrderRequest;
import com.example.administrator.digitalcredit.Model.Product;
import com.example.administrator.digitalcredit.R;
import com.example.administrator.digitalcredit.Utils.Helper;
import com.example.administrator.digitalcredit.adapter.ProductAdapter;
import com.example.administrator.digitalcredit.client.ApiClient;

import java.util.ArrayList;
import java.util.List;

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
    private List<CartBean> list;

    private AlertDialog.Builder builder;
    private CartInterface cartInterface;
    public OrderFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot=inflater.inflate(R.layout.fragment_order, container, false);
        init();


        btnCart.setOnClickListener(this);
        ApiClient.getInstance().getProduct().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful() && response.code()==200){
                    try{

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(viewRoot.getContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                                ((LinearLayoutManager) mLayoutManager).getOrientation());
                        recyclerView.addItemDecoration(dividerItemDecoration);
                        if(adapter!=null){
                            adapter.notifyDataSetChanged();
                        }else{
                            adapter=new ProductAdapter(response.body(),adapterInterface);
                            recyclerView.setAdapter(adapter);
                        }


                        if(adapter.getItemCount()>0){
                            progressBar.setVisibility(View.GONE);
                            layout_body.setVisibility(View.VISIBLE);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
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


        };

        return viewRoot;
    }

    @Override
    public void onClick(View v) {


        if(v.getId()==R.id.buttonCart){
           if(rate>0.00f){
               btnCart.setEnabled(false);
            try{
                View child;
                OrderRequest orderRequest=new OrderRequest();
                orderRequest.setUserId(Integer.parseInt(helper.getSession(viewRoot.getContext()).get("user_id").toString()));
                orderRequest.setTotalAmt(rate);
                orderRequest.setTotalItem(items);
                orderRequest.setOrderDetail(list);
                orderRequest.setOrder_status('A');
                for(int i=0;i<recyclerView.getChildCount();i++){
                    child=recyclerView.getChildAt(i);
                    if(Integer.parseInt(((TextView)child.findViewById(R.id.textViewQty)).getText().toString())>0 &&
                            Double.parseDouble(((TextView)child.findViewById(R.id.textViewAmount)).getText().toString())>0.00){

                        list.add(new CartBean(Integer.parseInt(((TextView)child.findViewById(R.id.textViewProductId)).getText().toString()),
                                Integer.parseInt(((TextView)child.findViewById(R.id.textViewQty)).getText().toString())));
                    }
                }


                ApiClient.getInstance().order(orderRequest)
                        .enqueue(new Callback<OrderDetailResponse>() {
                            @Override
                            public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                                if (response.code() == 200 || response.isSuccessful()) {
                                    btnCart.setEnabled(true);
                                    Log.e("OrderDetailResponse--->", response.body().getOrder().toString() + "," +
                                            response.body().getOrder().getOrderId());
                                    cartInterface.orderList(response.body());


                                    helper.putSession(viewRoot.getContext(),"OrderId", String.valueOf(response.body().getOrder().getOrderId()));
                                    //dialog.dismiss();



                                    helper.showMesage(viewRoot.getRootView(), "Successful...");
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
                                                            list.clear();
                                                        }



                                                    }else{
                                                        helper.showMesage(viewRoot,"Something went wrong...");
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<List<Product>> call, Throwable t) {
                                                    helper.showMesage(viewRoot,t.getMessage());
                                                    t.printStackTrace();
                                                }
                                            });
                                } else {
                                    btnCart.setEnabled(true);
                                    //dialog.dismiss();
                                    helper.showMesage(viewRoot.getRootView(), "Something went wrong...");
                                }
                            }

                            @Override
                            public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                                btnCart.setEnabled(true);
                                helper.showMesage(viewRoot.getRootView(), t.getMessage());
                                t.printStackTrace();
                            }
                        });
              /*  builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                    }
                });*/

                /*builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.clear();
                        btnCart.setEnabled(true);
                        dialog.dismiss();
                    }
                });*/

            }catch (Exception e){
                e.printStackTrace();
            }
               //builder.show();
           }
        }


    }

    private void init(){
        list=new ArrayList<>();

        recyclerView=viewRoot.findViewById(R.id.recycler_view);
        tvItems=viewRoot.findViewById(R.id.textViewItems);
        tvTotalAmount=viewRoot.findViewById(R.id.textViewTotalAmount);
        helper=Helper.getInstance();
        btnCart=viewRoot.findViewById(R.id.buttonCart);
        layout_body=viewRoot.findViewById(R.id.relativeLayoutBody);
        progressBar=viewRoot.findViewById(R.id.ProgessBar);
        builder=helper.alertdialog(viewRoot.getContext(),"Confirmation","Are you sure you want to add to cart?");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            Log.e("Attach","Inside");
            cartInterface = (CartInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
        }
    }
}
