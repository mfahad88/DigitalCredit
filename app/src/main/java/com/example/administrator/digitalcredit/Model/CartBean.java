package com.example.administrator.digitalcredit.Model;

public class CartBean {
    private int ProductId;
    private String ProductName;
    private Double Price;
    private Double Amount;
    private int Qty;

    public CartBean(int productId, String productName, Double price, Double amount, int qty) {
        ProductId = productId;
        ProductName = productName;
        Price = price;
        Amount = amount;
        Qty = qty;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    @Override
    public String toString() {
        return "CartBean{" +
                "ProductId=" + ProductId +
                ", ProductName='" + ProductName + '\'' +
                ", Price=" + Price +
                ", Amount=" + Amount +
                ", Qty=" + Qty +
                '}';
    }
}
