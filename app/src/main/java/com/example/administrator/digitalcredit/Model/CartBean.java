package com.example.administrator.digitalcredit.Model;

public class CartBean{
    private int ProductId;
    private int Qty;

    public CartBean(int productId, int qty) {
        ProductId = productId;
        Qty = qty;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "ProductId=" + ProductId +
                ", Qty=" + Qty +
                '}';
    }
}
