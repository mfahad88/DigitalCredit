package com.example.administrator.digitalcredit.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.digitalcredit.Model.Product;
import com.example.administrator.digitalcredit.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private List<Product> productList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProduct,tvPrice,tvDesc,tvQty,tvMinus,tvPlus,tvAmount;
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

        }
    }


    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Product product=productList.get(position);
        holder.tvProduct.setText(product.getProductName());
        holder.tvPrice.setText(String.valueOf(product.getPrice()));
        holder.tvDesc.setText(product.getDesc());
        holder.tvQty.setText("0");
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
