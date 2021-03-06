
package com.example.administrator.digitalcredit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetail {

    @SerializedName("order_detail_id")
    @Expose
    private Integer orderDetailId;
    @SerializedName("fk_order_id")
    @Expose
    private Integer fkOrderId;
    @SerializedName("qty")
    @Expose
    private Integer qty;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("fk_product_id")
    @Expose
    private Integer fkProductId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("price")
    @Expose
    private Double price;

    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Integer getFkOrderId() {
        return fkOrderId;
    }

    public void setFkOrderId(Integer fkOrderId) {
        this.fkOrderId = fkOrderId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getFkProductId() {
        return fkProductId;
    }

    public void setFkProductId(Integer fkProductId) {
        this.fkProductId = fkProductId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
