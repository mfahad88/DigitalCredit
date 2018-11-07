package com.example.administrator.digitalcredit.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.digitalcredit.Interface.AdapterInterface;
import com.example.administrator.digitalcredit.Model.CartBean;
import com.example.administrator.digitalcredit.Model.Product;
import com.example.administrator.digitalcredit.R;


import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private List<Product> productList;
    private int qty=0;
    private AdapterInterface adapterInterface;
    private DecimalFormat format;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProduct,tvPrice,tvDesc,tvQty,tvMinus,tvPlus,tvAmount,tvProductId;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);

            tvProduct=view.findViewById(R.id.textViewProduct);
            tvPrice=view.findViewById(R.id.textViewPrices);
            tvDesc=view.findViewById(R.id.textViewDescription);
            tvQty=view.findViewById(R.id.textViewQty);
            tvMinus=view.findViewById(R.id.textViewMinus);
            tvPlus=view.findViewById(R.id.textViewPlus);
            tvAmount=view.findViewById(R.id.textViewAmount);
            tvProductId=view.findViewById(R.id.textViewProductId);
            format=new DecimalFormat("0.00");

        }
    }


    public ProductAdapter(List<Product> productList,AdapterInterface adapterInterface) {
        this.productList = productList;
        this.adapterInterface=adapterInterface;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {
            final Product product=productList.get(position);
            holder.tvProductId.setText(String.valueOf(product.getProductId()));
            holder.tvProduct.setText(product.getProductName());
            holder.tvPrice.setText(String.valueOf(product.getPrice()));
            holder.tvDesc.setText(product.getDesc());
            holder.tvQty.setText(String.valueOf(qty));
            holder.tvPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    qty=Integer.parseInt(holder.tvQty.getText().toString());
                    qty++;
                    holder.tvQty.setText(String.valueOf(qty));
                    holder.tvAmount.setText(String.valueOf(product.getPrice()*qty));
                    adapterInterface.totalItems("plus");
                    adapterInterface.totalAmount(product.getPrice().floatValue(),"plus");


                }
            });
            holder.tvMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    qty=Integer.parseInt(holder.tvQty.getText().toString());
                    if(qty>0){
                        qty--;
                        holder.tvQty.setText(String.valueOf(qty));
                        holder.tvAmount.setText(String.valueOf(product.getPrice()*qty));
                        adapterInterface.totalItems("minus");
                        adapterInterface.totalAmount(product.getPrice().floatValue(),"minus");

                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


}
