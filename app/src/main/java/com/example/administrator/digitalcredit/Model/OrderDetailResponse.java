
package com.example.administrator.digitalcredit.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailResponse {

    @SerializedName("order")
    @Expose
    private Order order;
    @SerializedName("orderDetail")
    @Expose
    private List<OrderDetail> orderDetail = null;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderDetail> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<OrderDetail> orderDetail) {
        this.orderDetail = orderDetail;
    }

}
